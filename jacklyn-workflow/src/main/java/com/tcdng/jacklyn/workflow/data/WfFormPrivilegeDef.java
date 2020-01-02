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

import com.tcdng.jacklyn.shared.workflow.WorkflowFormElementType;

/**
 * Workflow form privilege definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfFormPrivilegeDef {

    private WorkflowFormElementType type;

    private String docName;

    private String name;

    private boolean visible;

    private boolean editable;

    private boolean disabled;

    private boolean required;

    public WfFormPrivilegeDef(WorkflowFormElementType type, String docName, String name, boolean visible,
            boolean editable, boolean disabled, boolean required) {
        this.type = type;
        this.docName = docName;
        this.name = name;
        this.visible = visible;
        this.editable = editable;
        this.disabled = disabled;
        this.required = required;
    }

    public WorkflowFormElementType getType() {
        return type;
    }

    public String getDocName() {
        return docName;
    }

    public String getName() {
        return name;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isRequired() {
        return required;
    }

}
