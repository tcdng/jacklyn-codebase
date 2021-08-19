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
import com.tcdng.unify.core.util.StringUtils;

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

    private String fireOnPrevStepName;

    private String notificationTemplateCode;

    private String filter;
    
    public WfAlertDef(String docName, String stepGlobalName, String fireOnPrevStepName, String name, String description,
            WorkflowAlertType type, WorkflowParticipantType participant, NotificationType channel,
            String notificationTemplateCode, String filter) {
        super(name, description);
        this.docName = docName;
        this.stepGlobalName = stepGlobalName;
        this.fireOnPrevStepName = fireOnPrevStepName;
        this.type = type;
        this.participant = participant;
        this.channel = channel;
        this.notificationTemplateCode = notificationTemplateCode;
        this.filter = filter;
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

    public String getFireOnPrevStepName() {
        return fireOnPrevStepName;
    }

    public String getFilter() {
		return filter;
	}

    public boolean isWithFilter() {
    	return !StringUtils.isBlank(filter);
    }
    
	public boolean isFireAlertOn(String docName, String prevStepName) {
        return this.docName.equals(docName)
                && (StringUtils.isBlank(this.fireOnPrevStepName) || this.fireOnPrevStepName.equals(prevStepName));
    }

    public boolean isPassThrough() {
        return type.isPassThrough();
    }

    public boolean isUserInteract() {
        return type.isUserInteract();
    }

    public boolean isCriticalNotification() {
        return type.isCriticalNotification();
    }

    public boolean isExpirationNotification() {
        return type.isExpirationNotification();
    }
}
