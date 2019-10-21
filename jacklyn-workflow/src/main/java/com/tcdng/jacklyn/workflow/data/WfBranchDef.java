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

import java.util.Set;

import com.tcdng.unify.core.util.DataUtils;

/**
 * Workflow document parallel branch definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfBranchDef extends BaseWfDef {

    private String target;

    private Set<String> mergeFields;

    public WfBranchDef(String name, String description, String target, Set<String> mergeFields) {
        super(name, description);
        this.target = target;
        this.mergeFields = DataUtils.unmodifiableSet(mergeFields);
    }

    public String getTarget() {
        return target;
    }

    public Set<String> getMergeFields() {
        return mergeFields;
    }
    
    public boolean isWithMergeFields() {
        return !mergeFields.isEmpty();
    }
}
