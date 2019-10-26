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
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.tcdng.jacklyn.shared.workflow.WorkflowExecuteTransitionTaskConstants;
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
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.ThreadUtils;

/**
 * Default implementation of workflow transition queue manager.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component(name = WorkflowModuleNameConstants.DEFAULTTRANSITIONQUEUEMANAGER,
        description = "Default Workflow Transition Queue Manager")
public class WfTransitionQueueManagerImpl extends AbstractWfTransitionQueueManager {

    @Configurable
    private TaskLauncher taskLauncher;

    @Configurable("16")
    private int maxTransitionUnits;

    private Set<Long> pendingSubmissionIds;

    private List<Queue<FlowingWfItemTransition>> transitionQueueList;

    private Map<String, Integer> transitionAllocs;

    private int transitionUnitAllocCounter;

    public WfTransitionQueueManagerImpl() {
        this.transitionQueueList = new ArrayList<Queue<FlowingWfItemTransition>>();
        this.transitionAllocs = new ConcurrentHashMap<String, Integer>();
        this.pendingSubmissionIds = ConcurrentHashMap.newKeySet();
    }

    @Override
    public void pushWfItemToTransitionQueue(Long submissionId, WfStepDef targetWfStepDef, FlowingWfItem flowingWfItem)
            throws UnifyException {
        pendingSubmissionIds.add(submissionId);
        getTransitionQueue(targetWfStepDef)
                .offer(new FlowingWfItemTransition(submissionId, targetWfStepDef, flowingWfItem));
    }

    @Override
    public FlowingWfItemTransition getNextFlowingWfItemTransition(int transitionUnitIndex) throws UnifyException {
        if (transitionUnitIndex >= transitionQueueList.size()) {
            // TODO Throw exception
        }

        return transitionQueueList.get(transitionUnitIndex).poll();
    }

    @Override
    public void acknowledgeTransition(FlowingWfItemTransition flowingWfItemTransition) {
        pendingSubmissionIds.remove(flowingWfItemTransition.getSubmissionId());
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

    private Queue<FlowingWfItemTransition> getTransitionQueue(WfStepDef wfStepDef) throws UnifyException {
        // All workflow steps belonging to the same template should processed with the
        // same transition queue. Forces all such step transitions for a process to be
        // processed in sequence.
        Integer transitionUnitIndex = transitionAllocs.get(wfStepDef.getTemplateGlobalName());
        if (transitionUnitIndex == null) {
            synchronized (WfTransitionQueueManagerImpl.class) {
                transitionUnitIndex = transitionAllocs.get(wfStepDef.getTemplateGlobalName());
                if (transitionUnitIndex == null) {
                    transitionUnitIndex = transitionUnitAllocCounter;
                    transitionAllocs.put(wfStepDef.getTemplateGlobalName(), transitionUnitIndex);

                    if (++transitionUnitAllocCounter > transitionQueueList.size()) {
                        // Create transition unit and launch corresponding periodic transition execution
                        // task.
                        transitionQueueList.add(new ConcurrentLinkedQueue<FlowingWfItemTransition>());
                        TaskSetup taskSetup = TaskSetup.newBuilder(TaskExecType.RUN_PERIODIC)
                                .addTask(WorkflowExecuteTransitionTaskConstants.TASK_NAME)
                                .setParam(WorkflowExecuteTransitionTaskConstants.TRANSITIONUNIT_INDEX,
                                        transitionUnitIndex)
                                .periodInMillSec(PeriodicType.EXTREME.getPeriodInMillSec()).build();
                        taskLauncher.launchTask(taskSetup);
                    }

                    if (transitionUnitAllocCounter >= maxTransitionUnits) {
                        transitionUnitAllocCounter = 0;
                    }
                }
            }
        }

        return transitionQueueList.get(transitionUnitIndex);
    }
}
