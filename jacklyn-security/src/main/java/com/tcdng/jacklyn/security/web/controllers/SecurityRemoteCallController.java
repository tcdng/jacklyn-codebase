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
package com.tcdng.jacklyn.security.web.controllers;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.web.controllers.BaseRemoteCallController;
import com.tcdng.jacklyn.security.business.SecurityService;
import com.tcdng.jacklyn.security.constants.SecurityModuleNameConstants;
import com.tcdng.jacklyn.shared.security.SecurityRemoteCallNameConstants;
import com.tcdng.jacklyn.shared.security.data.OSInstallationReqParams;
import com.tcdng.jacklyn.shared.security.data.OSInstallationReqResult;
import com.tcdng.jacklyn.shared.security.data.OSResources;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.web.annotation.RemoteAction;
import com.tcdng.unify.web.ui.PageManager;

/**
 * Security module remote call controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = SecurityModuleNameConstants.SECURITY_MODULE)
@Component("/security/remotecall")
public class SecurityRemoteCallController extends BaseRemoteCallController {

    @Configurable
    private SecurityService securityService;

    @Configurable
    private PageManager pageManager;

    @RemoteAction(
            name = SecurityRemoteCallNameConstants.OS_REQUEST_INSTALL,
            description = "$m{security.remotecall.osrequestinstall}", restricted = false)
    public OSInstallationReqResult osRequestInstall(OSInstallationReqParams oSInstallationReqParams)
            throws UnifyException {
        OSInstallationReqResult osirResult = securityService.processOSInstallationRequest(oSInstallationReqParams);
        osirResult.setResources(IOUtils.streamToBytes(
                new OSResources(pageManager.getPageName("resourceName"), pageManager.getPageName("contentType"),
                        pageManager.getDocumentStyleSheets(), pageManager.getDocumentsScripts())));
        return osirResult;
    }

}
