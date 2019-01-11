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

import com.tcdng.jacklyn.shared.workflow.WorkflowFormElementType;
import com.tcdng.jacklyn.shared.xml.adapter.WorkflowFormElementTypeXmlAdapter;

/**
 * Workflow step form privilege configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfFormPrivilegeConfig {

    private WorkflowFormElementType type;

    private String name;

    private Boolean visible;

    private Boolean editable;

    private Boolean disabled;

    private Boolean required;

    public WfFormPrivilegeConfig() {
        this.visible = Boolean.TRUE;
        this.editable = Boolean.TRUE;
        this.disabled = Boolean.FALSE;
        this.required = Boolean.TRUE;
    }

    public WorkflowFormElementType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(WorkflowFormElementTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(WorkflowFormElementType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute(name = "name", required = true)
    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVisible() {
        return visible;
    }

    @XmlAttribute(required = true)
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getEditable() {
        return editable;
    }

    @XmlAttribute(required = true)
    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    @XmlAttribute(required = true)
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getRequired() {
        return required;
    }

    @XmlAttribute(required = true)
    public void setRequired(Boolean required) {
        this.required = required;
    }
}
