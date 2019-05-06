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

package com.tcdng.jacklyn.workflow.data;

import com.tcdng.jacklyn.shared.notification.NotificationType;

/**
 * Workflow document alert definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfAlertDef extends BaseWfDef {

    private static final long serialVersionUID = 9168747124616981593L;

    private String globalStepName;

    private NotificationType type;

    private String notificationTemplateCode;

    public WfAlertDef(String globalStepName, String name, String description, NotificationType type,
            String notificationTemplateCode) {
        super(name, description);
        this.globalStepName = globalStepName;
        this.type = type;
        this.notificationTemplateCode = notificationTemplateCode;
    }

    public String getGlobalStepName() {
        return globalStepName;
    }

    public NotificationType getType() {
        return type;
    }

    public String getNotificationTemplateCode() {
        return notificationTemplateCode;
    }
}
