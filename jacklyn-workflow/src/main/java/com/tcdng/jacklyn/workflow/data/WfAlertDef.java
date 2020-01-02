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

package com.tcdng.jacklyn.workflow.data;

import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.jacklyn.shared.workflow.WorkflowAlertType;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;

/**
 * Workflow document alert definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfAlertDef extends BaseWfDef {

    private WorkflowAlertType type;
    
    private WorkflowParticipantType participant;

    private NotificationType channel;

    private String docName;

    private String stepGlobalName;

    private String notificationTemplateCode;

    public WfAlertDef(String docName, String stepGlobalName, String name, String description, WorkflowAlertType type,
            WorkflowParticipantType participant, NotificationType channel, String notificationTemplateCode) {
        super(name, description);
        this.docName = docName;
        this.stepGlobalName = stepGlobalName;
        this.type = type;
        this.participant = participant;
        this.channel = channel;
        this.notificationTemplateCode = notificationTemplateCode;
    }

    public String getStepGlobalName() {
        return stepGlobalName;
    }

    public WorkflowAlertType getType() {
        return type;
    }

    public WorkflowParticipantType getParticipant() {
        return participant;
    }

    public NotificationType getChannel() {
        return channel;
    }

    public String getNotificationTemplateCode() {
        return notificationTemplateCode;
    }

    public String getDocName() {
        return docName;
    }

    public boolean isPassThrough() {
        return type.isPassThrough();
    }

    public boolean isUserInteract() {
        return type.isUserInteract();
    }
}
