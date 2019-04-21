/*
 * Copyright 2018-2019 The Code Department
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

package com.tcdng.jacklyn.notification.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.tcdng.jacklyn.common.AbstractJacklynWebTest;
import com.tcdng.jacklyn.shared.notification.NotificationRemoteCallNameConstants;
import com.tcdng.jacklyn.shared.notification.data.GetToolingAttachmentGenParams;
import com.tcdng.jacklyn.shared.notification.data.GetToolingAttachmentGenResult;
import com.tcdng.jacklyn.shared.notification.data.ToolingAttachmentGenItem;

/**
 * Notification module remote gate test.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class NotificationRemoteGateTest extends AbstractJacklynWebTest {

    private static final String TEST_REMOTE_APP_URL = "http://localhost:" + TEST_HTTPPORT + "/jacklyn";

    @Test
    public void testGetToolingAttachmentGenList() throws Exception {
        GetToolingAttachmentGenParams params = new GetToolingAttachmentGenParams();
        GetToolingAttachmentGenResult result =
                getWebClient().remoteCall(GetToolingAttachmentGenResult.class, TEST_REMOTE_APP_URL, params);
        assertNotNull(result);
        assertFalse(result.isError());
        assertNull(result.getErrorCode());
        assertNull(result.getErrorMsg());
        assertNotNull(result.getAttachmentGenList());
        assertEquals(1, result.getAttachmentGenList().size());

        ToolingAttachmentGenItem item = result.getAttachmentGenList().get(0);
        assertNotNull(item);
        assertEquals("test-messageattachmentgenerator", item.getName());
        assertEquals("Test Message Attachment Generator", item.getDescription());
    }

    @Override
    protected void onSetup() throws Exception {
        if (!getWebClient().isRemoteCallSetup(TEST_REMOTE_APP_URL,
                NotificationRemoteCallNameConstants.GET_TOOLING_ATTACHMENT_GENERATOR_LIST)) {
            getWebClient().setupRemoteCall(TEST_REMOTE_APP_URL,
                    NotificationRemoteCallNameConstants.GET_TOOLING_ATTACHMENT_GENERATOR_LIST);
        }
    }

    @Override
    protected void onTearDown() throws Exception {

    }

}
