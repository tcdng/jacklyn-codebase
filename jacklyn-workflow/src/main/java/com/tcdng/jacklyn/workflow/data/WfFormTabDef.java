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

import java.util.Collections;
import java.util.List;

/**
 * Workflow form tab definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfFormTabDef extends BaseLabelWfDef {

    private boolean pseudo;

    private List<WfFormSectionDef> sectionList;

    public WfFormTabDef(String name, String description, String label, List<WfFormSectionDef> sectionList,
            boolean pseudo) {
        super(name, description, label);
        this.pseudo = pseudo;
        if (sectionList != null) {
            this.sectionList = Collections.unmodifiableList(sectionList);
        } else {
            this.sectionList = Collections.emptyList();
        }
    }

    public List<WfFormSectionDef> getSectionList() {
        return sectionList;
    }

    public boolean isPseudo() {
        return this.pseudo;
    }
}
