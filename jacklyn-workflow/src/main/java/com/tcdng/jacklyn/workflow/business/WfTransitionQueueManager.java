/*
 * Copyright 2018-2020 The Code Department.
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

import com.tcdng.jacklyn.workflow.data.FlowingWfItem;
import com.tcdng.jacklyn.workflow.data.FlowingWfItemTransition;
import com.tcdng.jacklyn.workflow.data.WfStepDef;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Workflow transition queue manager.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface WfTransitionQueueManager extends UnifyComponent {

    /**
     * Pushes a workflow item to the manager's transition queue.
     * 
     * @param submissionId
     *            the submission ID
     * @param targetWfStepDef
     *            the target workflow step
     * @param flowingWfItem
     *            the workflow item
     * @return the transition queue index
     * @throws UnifyException
     *             if an error occurs
     */
    int pushWfItemToTransitionQueue(Long submissionId, WfStepDef targetWfStepDef, FlowingWfItem flowingWfItem)
            throws UnifyException;

    /**
     * Gets the number of transition queues managed by this component.
     * 
     * @return the transition queue manager capacity
     */
    int capacity();

    /**
     * Resets the transition queues managed by this component.
     */
    void reset();

    /**
     * Gets the next available transition item for supplied transition queue index.
     * 
     * @param transitionQueueIndex
     *            the transition queue index
     * @return the next transition item otherwise null
     * @throws UnifyException
     *             if an error occurs
     */
    FlowingWfItemTransition getNextFlowingWfItemTransition(int transitionQueueIndex) throws UnifyException;

    /**
     * Acknowledges workflow item transition.
     * 
     * @param flowingWfItemTransition
     *            the transition item
     * @return true if transition is acknowledged
     */
    boolean acknowledgeTransition(FlowingWfItemTransition flowingWfItemTransition);

    /**
     * Blocks until all transition items with supplied submission IDs are processed.
     * 
     * @param submissionId
     *            the submission ID list
     * @throws UnifyException
     *             if an error occurs
     */
    void ensureSubmissionsProcessed(Long... submissionId) throws UnifyException;
}
