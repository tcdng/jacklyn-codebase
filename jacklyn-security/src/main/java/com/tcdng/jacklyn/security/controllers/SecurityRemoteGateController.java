/*
 * Copyright 2018 The Code Department
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
package com.tcdng.jacklyn.security.controllers;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.controllers.BaseRemoteCallController;
import com.tcdng.jacklyn.security.business.SecurityService;
import com.tcdng.jacklyn.security.constants.SecurityModuleNameConstants;
import com.tcdng.jacklyn.shared.security.SecurityRemoteCallNameConstants;
import com.tcdng.jacklyn.shared.security.data.OSInstallationReqParams;
import com.tcdng.jacklyn.shared.security.data.OSInstallationReqResult;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.web.annotation.GatewayAction;

/**
 * Security module remote call controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = SecurityModuleNameConstants.SECURITY_MODULE)
@Component("/security/gate")
public class SecurityRemoteGateController extends BaseRemoteCallController {

    @Configurable
    private SecurityService securityService;

    @GatewayAction(
            name = SecurityRemoteCallNameConstants.OS_REQUEST_INSTALL,
            description = "$m{security.gate.remotecall.osrequestinstall}", restricted = false)
    public OSInstallationReqResult osRequestInstall(OSInstallationReqParams oSInstallationReqParams)
            throws UnifyException {
        return securityService.processOSInstallationRequest(oSInstallationReqParams);
    }

}
