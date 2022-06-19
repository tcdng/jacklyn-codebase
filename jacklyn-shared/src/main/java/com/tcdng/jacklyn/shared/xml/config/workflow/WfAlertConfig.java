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

package com.tcdng.jacklyn.shared.xml.config.workflow;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.jacklyn.shared.workflow.WorkflowAlertType;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.shared.xml.adapter.NotificationTypeXmlAdapter;
import com.tcdng.jacklyn.shared.xml.adapter.WorkflowAlertTypeXmlAdapter;
import com.tcdng.jacklyn.shared.xml.adapter.WorkflowParticipantTypeXmlAdapter;
import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Workflow alert configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfAlertConfig extends BaseConfig {

    private WorkflowAlertType type;
    
    private WorkflowParticipantType participant;
    
    private NotificationType channel;

    private String document;
    
    private String message;
    
    private String fireOnPrevStepName;
    
    private String filter;

    public WfAlertConfig() {
        type = WorkflowAlertType.USER_INTERACT;
        participant = WorkflowParticipantType.ALL;
        channel = NotificationType.SYSTEM;
    }
    
    public String getDocument() {
        return document;
    }

    @XmlAttribute(required = true)
    public void setDocument(String document) {
        this.document = document;
    }

    public WorkflowAlertType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(WorkflowAlertTypeXmlAdapter.class)
    @XmlAttribute(name = "type")
    public void setType(WorkflowAlertType type) {
        if (type != null) {
            this.type = type;
        }        
    }

    public WorkflowParticipantType getParticipant() {
        return participant;
    }

    @XmlJavaTypeAdapter(WorkflowParticipantTypeXmlAdapter.class)
    @XmlAttribute(name = "participant")
    public void setParticipant(WorkflowParticipantType participant) {
        if (participant != null) {
            this.participant = participant;
        }
    }

    public NotificationType getChannel() {
        return channel;
    }

    @XmlJavaTypeAdapter(NotificationTypeXmlAdapter.class)
    @XmlAttribute(name = "channel")
    public void setChannel(NotificationType channel) {
        if (channel != null) {
            this.channel = channel;
        }
    }

    public String getMessage() {
        return message;
    }

    @XmlAttribute(required = true)
    public void setMessage(String message) {
        this.message = message;
    }

    public String getFireOnPrevStepName() {
        return fireOnPrevStepName;
    }

    @XmlAttribute(name = "fire-on-prev-step")
    public void setFireOnPrevStepName(String fireOnPrevStepName) {
        this.fireOnPrevStepName = fireOnPrevStepName;
    }

	public String getFilter() {
		return filter;
	}

    @XmlAttribute
	public void setFilter(String filter) {
		this.filter = filter;
	}
}
