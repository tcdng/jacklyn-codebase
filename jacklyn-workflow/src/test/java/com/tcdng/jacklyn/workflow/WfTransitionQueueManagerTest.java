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
package com.tcdng.jacklyn.workflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.tcdng.jacklyn.common.AbstractJacklynTest;
import com.tcdng.jacklyn.workflow.business.WfTransitionQueueManager;
import com.tcdng.jacklyn.workflow.business.WfTransitionQueueManagerImpl;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem;
import com.tcdng.jacklyn.workflow.data.FlowingWfItemTransition;
import com.tcdng.jacklyn.workflow.data.WfStepDef;
import com.tcdng.unify.core.Setting;
import com.tcdng.unify.core.UnifyException;

/**
 * Workflow transition queue manager tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfTransitionQueueManagerTest extends AbstractJacklynTest {

    @Test
    public void testCapacity() throws Exception {
        WfTransitionQueueManager wfTransitionQueueManager = getWfTransitionQueueManager();
        assertEquals(4, wfTransitionQueueManager.capacity());
    }

    @Test
    public void testPushWfItemToTransitionQueue() throws Exception {
        WfTransitionQueueManager wfTransitionQueueManager = getWfTransitionQueueManager();
        WfStepDef wfStepDef1 = new WfStepDef("wfTestCategory.firstTemplate");
        int transitionQueueIndex = wfTransitionQueueManager.pushWfItemToTransitionQueue(20L, wfStepDef1,
                new FlowingWfItem());
        assertEquals(0, transitionQueueIndex);
    }

    @Test
    public void testPushWfItemToTransitionQueueSameAlloc() throws Exception {
        WfTransitionQueueManager wfTransitionQueueManager = getWfTransitionQueueManager();
        WfStepDef wfStepDef1 = new WfStepDef("wfTestCategory.firstTemplate");
        int transitionQueueIndex = wfTransitionQueueManager.pushWfItemToTransitionQueue(20L, wfStepDef1,
                new FlowingWfItem());
        assertEquals(0, transitionQueueIndex);

        WfStepDef wfStepDef2 = new WfStepDef("wfTestCategory.firstTemplate");
        transitionQueueIndex = wfTransitionQueueManager.pushWfItemToTransitionQueue(21L, wfStepDef2,
                new FlowingWfItem());
        assertEquals(0, transitionQueueIndex);
    }

    @Test
    public void testPushWfItemToTransitionQueueNewAlloc() throws Exception {
        WfTransitionQueueManager wfTransitionQueueManager = getWfTransitionQueueManager();
        WfStepDef wfStepDef1 = new WfStepDef("wfTestCategory.firstTemplate");
        int transitionQueueIndex = wfTransitionQueueManager.pushWfItemToTransitionQueue(20L, wfStepDef1,
                new FlowingWfItem());
        assertEquals(0, transitionQueueIndex);

        WfStepDef wfStepDef2 = new WfStepDef("wfTestCategory.secondTemplate");
        transitionQueueIndex = wfTransitionQueueManager.pushWfItemToTransitionQueue(21L, wfStepDef2,
                new FlowingWfItem());
        assertEquals(1, transitionQueueIndex);
    }

    @Test
    public void testPushWfItemToTransitionQueueCycleAlloc() throws Exception {
        WfTransitionQueueManager wfTransitionQueueManager = getWfTransitionQueueManager();
        WfStepDef wfStepDef1 = new WfStepDef("wfTestCategory.firstTemplate");
        int transitionQueueIndex = wfTransitionQueueManager.pushWfItemToTransitionQueue(20L, wfStepDef1,
                new FlowingWfItem());
        assertEquals(0, transitionQueueIndex);

        WfStepDef wfStepDef2 = new WfStepDef("wfTestCategory.secondTemplate");
        transitionQueueIndex = wfTransitionQueueManager.pushWfItemToTransitionQueue(21L, wfStepDef2,
                new FlowingWfItem());
        assertEquals(1, transitionQueueIndex);

        WfStepDef wfStepDef3 = new WfStepDef("wfTestCategory.thirdTemplate");
        transitionQueueIndex = wfTransitionQueueManager.pushWfItemToTransitionQueue(22L, wfStepDef3,
                new FlowingWfItem());
        assertEquals(2, transitionQueueIndex);

        WfStepDef wfStepDef4 = new WfStepDef("wfTestCategory.fourthTemplate");
        transitionQueueIndex = wfTransitionQueueManager.pushWfItemToTransitionQueue(23L, wfStepDef4,
                new FlowingWfItem());
        assertEquals(3, transitionQueueIndex);

        // Since capacity is 4, new cycle starts here
        WfStepDef wfStepDef5 = new WfStepDef("wfTestCategory.fifthTemplate");
        transitionQueueIndex = wfTransitionQueueManager.pushWfItemToTransitionQueue(24L, wfStepDef5,
                new FlowingWfItem());
        assertEquals(0, transitionQueueIndex);

        WfStepDef wfStepDef6 = new WfStepDef("wfTestCategory.sixthTemplate");
        transitionQueueIndex = wfTransitionQueueManager.pushWfItemToTransitionQueue(25L, wfStepDef6,
                new FlowingWfItem());
        assertEquals(1, transitionQueueIndex);
    }

    @Test(expected = UnifyException.class)
    public void testPushWfItemToTransitionQueueSubmissionIdPending() throws Exception {
        WfTransitionQueueManager wfTransitionQueueManager = getWfTransitionQueueManager();
        WfStepDef wfStepDef1 = new WfStepDef("wfTestCategory.firstTemplate");
        wfTransitionQueueManager.pushWfItemToTransitionQueue(20L, wfStepDef1, new FlowingWfItem());
        WfStepDef wfStepDef2 = new WfStepDef("wfTestCategory.firstTemplate");
        wfTransitionQueueManager.pushWfItemToTransitionQueue(20L, wfStepDef2, new FlowingWfItem());
    }

    @Test
    public void testGetNextFlowingWfItemTransitionBlank() throws Exception {
        WfTransitionQueueManager wfTransitionQueueManager = getWfTransitionQueueManager();
        FlowingWfItemTransition transitionItem = wfTransitionQueueManager.getNextFlowingWfItemTransition(0);
        assertNull(transitionItem);
        transitionItem = wfTransitionQueueManager.getNextFlowingWfItemTransition(1);
        assertNull(transitionItem);
        transitionItem = wfTransitionQueueManager.getNextFlowingWfItemTransition(2);
        assertNull(transitionItem);
        transitionItem = wfTransitionQueueManager.getNextFlowingWfItemTransition(3);
        assertNull(transitionItem);
    }

    @Test
    public void testGetNextFlowingWfItemTransition() throws Exception {
        WfTransitionQueueManager wfTransitionQueueManager = getWfTransitionQueueManager();
        WfStepDef wfStepDef1 = new WfStepDef("wfTestCategory.firstTemplate");
        FlowingWfItem wfItem1 = new FlowingWfItem();
        wfTransitionQueueManager.pushWfItemToTransitionQueue(10L, wfStepDef1, wfItem1);

        FlowingWfItem wfItem2 = new FlowingWfItem();
        wfTransitionQueueManager.pushWfItemToTransitionQueue(11L, wfStepDef1, wfItem2);

        FlowingWfItemTransition transitionItem = wfTransitionQueueManager.getNextFlowingWfItemTransition(0);
        assertNotNull(transitionItem);
        assertEquals(Long.valueOf(10L), transitionItem.getSubmissionId());
        assertEquals(wfStepDef1, transitionItem.getTargetWfStepDef());
        assertEquals(wfItem1, transitionItem.getFlowingWfItem());

        transitionItem = wfTransitionQueueManager.getNextFlowingWfItemTransition(0);
        assertNotNull(transitionItem);
        assertEquals(Long.valueOf(11L), transitionItem.getSubmissionId());
        assertEquals(wfStepDef1, transitionItem.getTargetWfStepDef());
        assertEquals(wfItem2, transitionItem.getFlowingWfItem());

        transitionItem = wfTransitionQueueManager.getNextFlowingWfItemTransition(0);
        assertNull(transitionItem);
    }

    @Test
    public void testAcknowledgeTransition() throws Exception {
        WfTransitionQueueManager wfTransitionQueueManager = getWfTransitionQueueManager();
        WfStepDef wfStepDef1 = new WfStepDef("wfTestCategory.firstTemplate");
        FlowingWfItem wfItem1 = new FlowingWfItem();
        wfTransitionQueueManager.pushWfItemToTransitionQueue(10L, wfStepDef1, wfItem1);

        FlowingWfItemTransition transitionItem = wfTransitionQueueManager.getNextFlowingWfItemTransition(0);
        assertTrue(wfTransitionQueueManager.acknowledgeTransition(transitionItem));
        assertFalse(wfTransitionQueueManager.acknowledgeTransition(transitionItem));
    }

    @Override
    protected void doAddSettingsAndDependencies() throws Exception {
        super.doAddSettingsAndDependencies();

        addDependency(WorkflowModuleNameConstants.DEFAULTWORKFLOWTRANSITIONQUEUEMANAGER,
                WfTransitionQueueManagerImpl.class, true, true, new Setting("maxTransitionQueues", 4),
                new Setting("transitionExecTask", ""));
    }

    @Override
    protected void onSetup() throws Exception {

    }

    @Override
    protected void onTearDown() throws Exception {
        getWfTransitionQueueManager().reset();
    }

    private WfTransitionQueueManager getWfTransitionQueueManager() throws Exception {
        return (WfTransitionQueueManager) getComponent(
                WorkflowModuleNameConstants.DEFAULTWORKFLOWTRANSITIONQUEUEMANAGER);
    }
}
