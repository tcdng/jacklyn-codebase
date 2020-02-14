/*
 * Copyright 2018-2020 The Code Department
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

import com.tcdng.jacklyn.workflow.web.widgets.WfDocUplGenerator;

/**
 * Workflow template document definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfTemplateDocDef {

    private WfDocDef wfDocDef;

    private WfDocUplGenerator wfDocUplGenerator;

    private String workAssignerName;

    private boolean manual;

    public WfTemplateDocDef(WfDocDef wfDocDef, WfDocUplGenerator wfDocUplGenerator, String workAssignerName,
            boolean manual) {
        this.wfDocDef = wfDocDef;
        this.wfDocUplGenerator = wfDocUplGenerator;
        this.workAssignerName = workAssignerName;
        this.manual = manual;
    }

    public WfDocDef getWfDocDef() {
        return wfDocDef;
    }

    public String getDocName() {
        return wfDocDef.getName();
    }

    public WfDocUplGenerator getWfDocUplGenerator() {
        return wfDocUplGenerator;
    }

    public String getWorkAssignerName() {
        return workAssignerName;
    }

    public boolean isManual() {
        return manual;
    }
}
