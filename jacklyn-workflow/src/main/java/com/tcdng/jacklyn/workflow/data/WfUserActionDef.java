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

import java.util.Collections;
import java.util.List;

import com.tcdng.unify.core.constant.RequirementType;

/**
 * Workflow user action definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfUserActionDef extends BaseLabelWfDef {

    private static final long serialVersionUID = -7944354332014625017L;

    private RequirementType noteReqType;

    private String targetGlobalName;

    private boolean validatePage;

    private List<WfAttachmentCheckDef> attachmentCheckList;

    public WfUserActionDef(String name, String description, String label, RequirementType noteReqType,
            String targetGlobalName, boolean validatePage, List<WfAttachmentCheckDef> attachmentCheckList) {
        super(name, description, label);
        this.noteReqType = noteReqType;
        this.targetGlobalName = targetGlobalName;
        this.validatePage = validatePage;
        if (attachmentCheckList != null) {
            this.attachmentCheckList = Collections.unmodifiableList(attachmentCheckList);
        } else {
            this.attachmentCheckList = Collections.emptyList();
        }
    }

    public RequirementType getNoteReqType() {
        return noteReqType;
    }

    public String getTargetGlobalName() {
        return targetGlobalName;
    }

    public boolean isValidatePage() {
        return validatePage;
    }

    public List<WfAttachmentCheckDef> getAttachmentCheckList() {
        return attachmentCheckList;
    }
}