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
package com.tcdng.jacklyn.system.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.tcdng.jacklyn.common.AbstractJacklynWebTest;
import com.tcdng.jacklyn.shared.system.SystemRemoteCallNameConstants;
import com.tcdng.jacklyn.shared.system.data.GetAppInfoParams;
import com.tcdng.jacklyn.shared.system.data.GetAppInfoResult;
import com.tcdng.jacklyn.shared.system.data.GetAppModulesParams;
import com.tcdng.jacklyn.shared.system.data.GetAppModulesResult;

/**
 * System module remote gate test.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class SystemRemoteGateTest extends AbstractJacklynWebTest {

    private static final String TEST_REMOTE_APP_URL = "http://localhost:" + TEST_HTTPPORT + "/jacklyn";

    @Test
    public void testGetApplicationInfo() throws Exception {
        GetAppInfoParams params = new GetAppInfoParams();
        GetAppInfoResult result = getWebClient().remoteCall(GetAppInfoResult.class, TEST_REMOTE_APP_URL, params);
        assertNotNull(result);
        assertFalse(result.isError());
        assertEquals("jacklyn-codebase", result.getAppName());
        assertEquals("1.0.0", result.getAppVersion());
        assertEquals("Client Title", result.getClientTitle());
        assertNull(result.getErrorCode());
        assertNull(result.getErrorMsg());
    }

    @Test
    public void testGetApplicationModules() throws Exception {
        GetAppModulesParams params = new GetAppModulesParams();
        GetAppModulesResult result = getWebClient().remoteCall(GetAppModulesResult.class, TEST_REMOTE_APP_URL,
                params);
        assertNotNull(result);
        assertFalse(result.isError());
        assertNotNull(result.getModuleList());
        assertFalse(result.getModuleList().isEmpty());
        assertNull(result.getErrorCode());
        assertNull(result.getErrorMsg());
    }

    @Override
    protected void onSetup() throws Exception {
        if (!getWebClient().isRemoteCallSetup(TEST_REMOTE_APP_URL,
                SystemRemoteCallNameConstants.GET_APPLICATION_INFO)) {
            getWebClient().setupRemoteCall(TEST_REMOTE_APP_URL,
                    SystemRemoteCallNameConstants.GET_APPLICATION_INFO);
            getWebClient().setupRemoteCall(TEST_REMOTE_APP_URL,
                    SystemRemoteCallNameConstants.GET_APPLICATION_MODULES);
        }
    }

    @Override
    protected void onTearDown() throws Exception {

    }
}
