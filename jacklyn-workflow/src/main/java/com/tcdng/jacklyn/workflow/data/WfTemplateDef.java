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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.workflow.constants.WorkflowModuleErrorConstants;
import com.tcdng.unify.core.UnifyException;

/**
 * Workflow template definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfTemplateDef extends BaseWfDef {

    private Long wfTemplateId;

    private String wfCategoryName;

    private String globalName;

    private long versionTimestamp;

    private WfStepDef startStep;

    private WfStepDef errorStep;

    private Map<String, WfTemplateDocDef> templateDocDefs;

    private Map<String, WfManualInitDef> manualInitDefs;

    private Map<String, WfStepDef> steps;

    public WfTemplateDef(Long wfTemplateId, String wfCategoryName, String globalName, String name, String description,
            long versionTimestamp, Map<String, WfTemplateDocDef> wfTemplateDocDefs, List<WfStepDef> wfStepDefList) {
        super(name, description);
        this.wfTemplateId = wfTemplateId;
        this.wfCategoryName = wfCategoryName;
        this.globalName = globalName;
        this.versionTimestamp = versionTimestamp;
        this.templateDocDefs = wfTemplateDocDefs;

        if (wfStepDefList != null) {
            steps = new HashMap<String, WfStepDef>();
            for (WfStepDef wfStepDef : wfStepDefList) {
                if (wfStepDef.isStart()) {
                    startStep = wfStepDef;
                } else if (wfStepDef.isError()) {
                    errorStep = wfStepDef;
                } else if (wfStepDef.isManual()) {
                    for (WfTemplateDocDef wfTemplateDocDef : this.templateDocDefs.values()) {
                        if (wfTemplateDocDef.isManual()) {
                            if (manualInitDefs == null) {
                                manualInitDefs = new HashMap<String, WfManualInitDef>();
                            }

                            WfDocDef wfDocDef = wfTemplateDocDef.getWfDocDef();
                            manualInitDefs.put(wfDocDef.getName(), new WfManualInitDef(wfDocDef, wfStepDef));
                        }
                    }
                }

                steps.put(wfStepDef.getName(), wfStepDef);
            }
        } else {
            steps = Collections.emptyMap();
        }
    }

    public Long getWfTemplateId() {
        return wfTemplateId;
    }

    public String getWfCategoryName() {
        return wfCategoryName;
    }

    public String getGlobalName() {
        return globalName;
    }

    public long getVersionTimestamp() {
        return versionTimestamp;
    }

    public WfStepDef getStartStep() {
        return startStep;
    }

    public WfStepDef getErrorStep() {
        return errorStep;
    }

    public Set<String> getWfStepNames() {
        return steps.keySet();
    }

    public WfTemplateDocDef getWfTemplateDocDef(String docName) throws UnifyException {
        WfTemplateDocDef wfTemplateDocDef = templateDocDefs.get(docName);
        if (wfTemplateDocDef == null) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_TEMPLATE_DOCUMENT_WITH_NAME_UNKNOWN,
                    this.getDescription(), docName);
        }

        return wfTemplateDocDef;
    }

    public WfManualInitDef getManualInitDef(String docName) throws UnifyException {
        if (manualInitDefs == null) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_TEMPLATE_NO_MANUAL_INIT, getDescription());
        }

        WfManualInitDef manualInitDef = manualInitDefs.get(docName);
        if (manualInitDef == null) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_TEMPLATE_NO_DOC_MANUAL_INIT,
                    getDescription(), docName);
        }

        return manualInitDef;
    }

    public boolean isSupportManualInit() {
        return manualInitDefs != null;
    }

    public WfStepDef getWfStepDef(String stepName) throws UnifyException {
        WfStepDef wfStepDef = steps.get(stepName);
        if (wfStepDef == null) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_STEP_WITH_NAME_UNKNOWN,
                    this.getDescription(), stepName);
        }

        return wfStepDef;
    }
}