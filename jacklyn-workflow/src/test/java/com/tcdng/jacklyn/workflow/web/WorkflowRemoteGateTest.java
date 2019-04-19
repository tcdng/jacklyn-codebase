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

package com.tcdng.jacklyn.workflow.web;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;

import com.tcdng.jacklyn.common.AbstractJacklynWebTest;
import com.tcdng.jacklyn.shared.workflow.WorkflowRemoteCallNameConstants;
import com.tcdng.jacklyn.shared.workflow.data.PublishWfCategoryParams;
import com.tcdng.jacklyn.shared.workflow.data.PublishWfCategoryResult;
import com.tcdng.jacklyn.workflow.entities.WfCategory;
import com.tcdng.jacklyn.workflow.entities.WfDoc;
import com.tcdng.jacklyn.workflow.entities.WfForm;
import com.tcdng.jacklyn.workflow.entities.WfMessage;
import com.tcdng.jacklyn.workflow.entities.WfTemplate;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.IOUtils;

/**
 * Workflow module remote gate test.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WorkflowRemoteGateTest extends AbstractJacklynWebTest {

    private static final String TEST_REMOTE_APP_URL = "http://localhost:" + TEST_HTTPPORT + "/jacklyn";

    @Test(expected = UnifyException.class)
    public void testPublishWfCategoryNoXml() throws Exception {
        PublishWfCategoryParams params = new PublishWfCategoryParams();
        PublishWfCategoryResult result = getRemoteCallClient().remoteCall(PublishWfCategoryResult.class,
                TEST_REMOTE_APP_URL, params);
        assertNotNull(result);
    }

    @Test
    public void testPublishWfCategory() throws Exception {
        InputStream inputStream = null;
        try {
            inputStream = IOUtils.openClassLoaderResourceInputStream("xml/wfcustomer.xml");
            byte[] xml = IOUtils.readAll(inputStream);

            PublishWfCategoryParams params = new PublishWfCategoryParams(xml, true);
            PublishWfCategoryResult result = getRemoteCallClient().remoteCall(PublishWfCategoryResult.class,
                    TEST_REMOTE_APP_URL, params);
            assertNotNull(result);
            assertFalse(result.isError());
        } finally {
            IOUtils.close(inputStream);
        }
    }

    @Override
    protected void onSetup() throws Exception {
        if (!getRemoteCallClient().isRemoteCallSetup(TEST_REMOTE_APP_URL,
                WorkflowRemoteCallNameConstants.PUBLISH_WORKFLOW_CATEGORY)) {
            getRemoteCallClient().setupRemoteCall(TEST_REMOTE_APP_URL,
                    WorkflowRemoteCallNameConstants.PUBLISH_WORKFLOW_CATEGORY);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onTearDown() throws Exception {
        deleteAll(WfMessage.class, WfTemplate.class, WfForm.class, WfDoc.class, WfCategory.class);
    }

}
