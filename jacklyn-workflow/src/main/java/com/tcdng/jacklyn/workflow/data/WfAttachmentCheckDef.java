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

import java.io.Serializable;

import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Workflow attachment check definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfAttachmentCheckDef implements Serializable {

    private static final long serialVersionUID = 6307175089367863678L;

    private RequirementType requirementType;

    private WfDocAttachmentDef attachment;

    private String docName;

    public WfAttachmentCheckDef(String docName, RequirementType requirementType, WfDocAttachmentDef attachment) {
        this.docName = docName;
        this.requirementType = requirementType;
        this.attachment = attachment;
    }

    public RequirementType getRequirementType() {
        return requirementType;
    }

    public WfDocAttachmentDef getAttachment() {
        return attachment;
    }

    public String getDocName() {
        return docName;
    }

    public boolean isDoc() {
        return StringUtils.isNotBlank(docName);
    }
}
