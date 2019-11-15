/*
 * Copyright 2018-2019 The Code Department.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tcdng.jacklyn.workflow.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.tcdng.jacklyn.shared.workflow.WorkflowExecuteTransitionTaskConstants;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleErrorConstants;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem;
import com.tcdng.jacklyn.workflow.data.FlowingWfItemTransition;
import com.tcdng.jacklyn.workflow.data.WfStepDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.task.TaskExecType;
import com.tcdng.unify.core.task.TaskLauncher;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.ThreadUtils;

/**
 * Default implementation of workflow transition queue manager.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component(name = WorkflowModuleNameConstants.DEFAULTWORKFLOWTRANSITIONQUEUEMANAGER,
        description = "Default Workflow Transition Queue Manager")
public class WfTransitionQueueManagerImpl extends AbstractWfTransitionQueueManager {

    @Configurable
    private TaskLauncher taskLauncher;

    @Configurable(WorkflowExecuteTransitionTaskConstants.TASK_NAME)
    private String transitionExecTask;
    
    @Configurable("32")
    private int maxTransitionQueues;

    private Set<Long> pendingSubmissionIds;

    private List<TransitionQueue> transitionQueueList;

    private List<TaskMonitor> taskMonitorList;

    private Map<String, Integer> transitionAllocs;

    private int transitionUnitAllocCounter;

    public WfTransitionQueueManagerImpl() {
        this.transitionQueueList = new ArrayList<TransitionQueue>();
        this.taskMonitorList = new ArrayList<TaskMonitor>();
        this.transitionAllocs = new ConcurrentHashMap<String, Integer>();
        this.pendingSubmissionIds = Collections.newSetFromMap(new ConcurrentHashMap<Long, Boolean>());
    }

    @Override
    public int pushWfItemToTransitionQueue(Long submissionId, WfStepDef targetWfStepDef, FlowingWfItem flowingWfItem)
            throws UnifyException {
        if (pendingSubmissionIds.contains(submissionId)) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_QUEUEMANAGER_SUBMISSION_ID_PENDING,
                    submissionId);
        }

        pendingSubmissionIds.add(submissionId);
        TransitionQueue transitionQueue = getTransitionQueue(targetWfStepDef);
        transitionQueue.getQueue().offer(new FlowingWfItemTransition(submissionId, targetWfStepDef, flowingWfItem));
        return transitionQueue.getIndex();
    }

    @Override
    public int capacity() {
        return maxTransitionQueues;
    }

    @Override
    public void reset() {
        transitionAllocs.clear();
        pendingSubmissionIds.clear();
        transitionUnitAllocCounter = 0;
        transitionQueueList.clear();
        for (int transitionQueueIndex = 0; transitionQueueIndex < maxTransitionQueues; transitionQueueIndex++) {
            transitionQueueList.add(new TransitionQueue(transitionQueueIndex));
        }

        cancelTasks();
    }
    
    @Override
    public FlowingWfItemTransition getNextFlowingWfItemTransition(int transitionQueueIndex) throws UnifyException {
        if (transitionQueueIndex < 0 || transitionQueueIndex >= transitionQueueList.size()) {
            throw new UnifyException(
                    WorkflowModuleErrorConstants.WORKFLOW_QUEUEMANAGER_TRANSITION_QUEUE_INDEX_OUT_BOUNDS,
                    transitionQueueIndex);
        }

        return transitionQueueList.get(transitionQueueIndex).getQueue().poll();
    }

    @Override
    public boolean acknowledgeTransition(FlowingWfItemTransition flowingWfItemTransition) {
        return pendingSubmissionIds.remove(flowingWfItemTransition.getSubmissionId());
    }

    @Override
    public void ensureSubmissionsProcessed(Long... submissionId) throws UnifyException {
        boolean present;
        do {
            present = false;
            for (Long id : submissionId) {
                present = pendingSubmissionIds.contains(id);
                if (present) {
                    break;
                }
            }

            if (present) {
                ThreadUtils.sleep(250);
            }
        } while (present);
    }

    @Override
    protected void onInitialize() throws UnifyException {
        super.onInitialize();
        reset();
    }

    @Override
    protected void onTerminate() throws UnifyException {
        cancelTasks();
        super.onTerminate();
    }

    private TransitionQueue getTransitionQueue(WfStepDef wfStepDef) throws UnifyException {
        // All workflow steps belonging to the same template should processed with the
        // same transition queue. Forces all such step transitions for a process to be
        // processed in sequence.
        Integer transitionQueueIndex = transitionAllocs.get(wfStepDef.getTemplateGlobalName());
        if (transitionQueueIndex == null) {
            synchronized (WfTransitionQueueManagerImpl.class) {
                transitionQueueIndex = transitionAllocs.get(wfStepDef.getTemplateGlobalName());
                if (transitionQueueIndex == null) {
                    transitionQueueIndex = transitionUnitAllocCounter;
                    transitionAllocs.put(wfStepDef.getTemplateGlobalName(), transitionQueueIndex);
                    if (++transitionUnitAllocCounter >= maxTransitionQueues) {
                        transitionUnitAllocCounter = 0;
                    }
                }
            }
        }

        // Activate transition queue if necessary
        TransitionQueue transitionQueue = transitionQueueList.get(transitionQueueIndex);
        if (!transitionQueue.isActive()) {
            // Launch corresponding periodic transition execution task.
            if (!StringUtils.isBlank(transitionExecTask)) {
                TaskSetup taskSetup = TaskSetup.newBuilder(TaskExecType.RUN_PERIODIC)
                        .addTask(transitionExecTask)
                        .setParam(WorkflowExecuteTransitionTaskConstants.TRANSITIONUNIT_INDEX, transitionQueueIndex)
                        .periodInMillSec(PeriodicType.EXTREME.getPeriodInMillSec()).build();
                TaskMonitor taskMonitor = taskLauncher.launchTask(taskSetup);
                taskMonitorList.add(taskMonitor);
            }

            transitionQueue.setActive(true);
        }
        return transitionQueue;
    }

    private void cancelTasks() {
        for (TaskMonitor taskMonitor : taskMonitorList) {
            taskMonitor.cancel();
        }
        taskMonitorList.clear();
    }

    private class TransitionQueue {

        private Queue<FlowingWfItemTransition> queue;

        private int index;

        private boolean active;

        public TransitionQueue(int index) {
            this.index = index;
            this.queue = new ConcurrentLinkedQueue<FlowingWfItemTransition>();
        }

        public Queue<FlowingWfItemTransition> getQueue() {
            return queue;
        }

        public int getIndex() {
            return index;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
