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

package com.tcdng.jacklyn.notification.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.controllers.BaseRemoteCallController;
import com.tcdng.jacklyn.notification.business.NotificationService;
import com.tcdng.jacklyn.notification.constants.NotificationModuleNameConstants;
import com.tcdng.jacklyn.shared.notification.data.GetToolingAttachmentGenParams;
import com.tcdng.jacklyn.shared.notification.data.GetToolingAttachmentGenResult;
import com.tcdng.jacklyn.shared.notification.data.ToolingAttachmentGenItem;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.web.annotation.GatewayAction;

/**
 * Message module remote call controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = NotificationModuleNameConstants.NOTIFICATION_MODULE)
@Component("/notification/gate")
public class NotificationRemoteGateController extends BaseRemoteCallController {

    @Configurable
    private NotificationService notificationService;

    @GatewayAction(
            name = com.tcdng.jacklyn.shared.notification.NotificationRemoteCallNameConstants.GET_TOOLING_ATTACHMENT_GENERATOR_LIST,
            description = "$m{notification.gate.remotecall.gettoolingattachmentgen}")
    public GetToolingAttachmentGenResult getToolingAttachmentGenList(GetToolingAttachmentGenParams params)
            throws UnifyException {
        List<ToolingAttachmentGenItem> list = notificationService.findToolingAttachmentGenTypes();
        return new GetToolingAttachmentGenResult(list);
    }

}
