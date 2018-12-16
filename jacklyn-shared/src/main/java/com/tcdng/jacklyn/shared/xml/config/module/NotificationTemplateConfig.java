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
package com.tcdng.jacklyn.shared.xml.config.module;

import javax.xml.bind.annotation.XmlAttribute;

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Notification template configurations.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class NotificationTemplateConfig extends BaseConfig {

    private String subject;

    private String template;

    private boolean html;

    public String getSubject() {
        return subject;
    }

    @XmlAttribute(required = true)
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplate() {
        return template;
    }

    @XmlAttribute(required = true)
    public void setTemplate(String template) {
        this.template = template;
    }

    public boolean isHtml() {
        return html;
    }

    @XmlAttribute(required = true)
    public void setHtml(boolean html) {
        this.html = html;
    }
}
