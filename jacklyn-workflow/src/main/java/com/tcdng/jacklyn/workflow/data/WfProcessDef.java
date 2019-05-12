/*
 * Copyright 2018-2019 The Code Department
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

/**
 * Workflow process definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfProcessDef {

    private WfTemplateDef wfTemplateDef;

    private WfTemplateDocDef wfTemplateDocDef;

    private String globalName;

    public WfProcessDef(String globalName, WfTemplateDef wfTemplateDef, WfTemplateDocDef wfTemplateDocDef) {
        this.wfTemplateDef = wfTemplateDef;
        this.wfTemplateDocDef = wfTemplateDocDef;
        this.globalName = globalName;
    }

    public WfTemplateDef getWfTemplateDef() {
        return wfTemplateDef;
    }

    public WfTemplateDocDef getWfTemplateDocDef() {
        return wfTemplateDocDef;
    }

    public WfDocDef getWfDocDef() {
        return wfTemplateDocDef.getWfDocDef();
    }

    public String getGlobalName() {
        return globalName;
    }

    public String getViewer() {
        return wfTemplateDocDef.getViewer();
    }

    public String getTemplateGlobalName() {
        return wfTemplateDef.getGlobalName();
    }

    public String getTemplateName() {
        return wfTemplateDef.getName();
    }

    public String getDocGlobalName() {
        return wfTemplateDocDef.getWfDocDef().getGlobalName();
    }

    public String getDocName() {
        return wfTemplateDocDef.getWfDocDef().getName();
    }

    public long getTimestamp() {
        return wfTemplateDef.getTimestamp();
    }
}
