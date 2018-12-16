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

package com.tcdng.jacklyn.workflow.data;

import java.util.Collections;
import java.util.List;

/**
 * Workflow form section definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfFormSectionDef extends BaseLabelWfDef {

    private static final long serialVersionUID = 800388073154871802L;

    private String binding;

    private List<WfFormFieldDef> fieldList;

    public WfFormSectionDef(String name, String description, String label, String binding,
            List<WfFormFieldDef> fieldList) {
        super(name, description, label);
        this.binding = binding;
        if (fieldList != null && !fieldList.isEmpty()) {
            this.fieldList = Collections.unmodifiableList(fieldList);
        } else {
            this.fieldList = Collections.emptyList();
        }
    }

    public String getBinding() {
        return binding;
    }

    public List<WfFormFieldDef> getFieldList() {
        return fieldList;
    }

}
