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
package com.tcdng.jacklyn.common;

import com.tcdng.jacklyn.common.constants.CommonModuleNameConstants;
import com.tcdng.jacklyn.common.web.controllers.CommonRemoteCallGateImpl;
import com.tcdng.unify.core.Setting;
import com.tcdng.unify.core.UnifyCorePropertyConstants;
import com.tcdng.unify.jetty.JettyApplicationComponents;
import com.tcdng.unify.jetty.http.JettyEmbeddedWebServer;
import com.tcdng.unify.web.WebApplicationComponents;
import com.tcdng.unify.web.remotecall.WebClient;

/**
 * Abstract jacklyn web test.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractJacklynWebTest extends AbstractJacklynTest {

    protected static final short TEST_HTTPPORT = 6082;

    @Override
    protected void doAddSettingsAndDependencies() throws Exception {
        super.doAddSettingsAndDependencies();
        addContainerSetting(UnifyCorePropertyConstants.APPLICATION_INTERFACES,
                new String[] { JettyApplicationComponents.JETTY_EMBEDDEDWEBSERVER });

        addDependency(CommonModuleNameConstants.JACKLYNAPPLICATIONSERVICEGATE, CommonRemoteCallGateImpl.class, true,
                true, new Setting("openMode", "true"));
        addDependency(JettyApplicationComponents.JETTY_EMBEDDEDWEBSERVER, JettyEmbeddedWebServer.class, true, true,
                new Setting("contextPath", "/jacklyn"), new Setting("httpPort", String.valueOf(TEST_HTTPPORT)));
    }

    protected WebClient getWebClient() throws Exception {
        return (WebClient) getComponent(WebApplicationComponents.APPLICATION_WEBCLIENT);
    }
}
