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

package com.tcdng.jacklyn.shared.xml.config.workflow;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.jacklyn.shared.xml.adapter.NotificationTypeXmlAdapter;
import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Workflow alert configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfAlertConfig extends BaseConfig {

    private NotificationType type;

    private String message;

    public NotificationType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(NotificationTypeXmlAdapter.class)
    @XmlAttribute(name = "type", required = true)
    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    @XmlAttribute(required = true)
    public void setMessage(String message) {
        this.message = message;
    }
}
