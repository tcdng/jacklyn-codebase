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

import com.tcdng.unify.core.constant.RequirementType;

/**
 * Workflow action.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfAction {

    private String name;

    private String label;

    private RequirementType commentReqType;

    private boolean validatePage;

    public WfAction(String name, String label, RequirementType commentReqType, boolean validatePage) {
        this.name = name;
        this.label = label;
        this.commentReqType = commentReqType;
        this.validatePage = validatePage;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public RequirementType getCommentReqType() {
        return commentReqType;
    }

    public boolean isValidatePage() {
        return validatePage;
    }
}
