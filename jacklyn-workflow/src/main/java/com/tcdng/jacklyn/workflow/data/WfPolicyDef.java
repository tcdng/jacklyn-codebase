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

import com.tcdng.unify.core.util.StringUtils;

/**
 * Workflow document policy definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfPolicyDef extends BaseWfDef {

    private String docName;

    private String logic;

    public WfPolicyDef(String name, String description, String docName, String logic) {
        super(name, description);
        this.docName = docName;
        this.logic = logic;
    }

    public String getDocName() {
        return docName;
    }

    public String getLogic() {
        return logic;
    }
    
    public boolean isDoc() {
        return !StringUtils.isBlank(docName);
    }
}
