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
import java.util.Date;
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

    private static final long serialVersionUID = -7947997247077702621L;

    private Long wfTemplateId;

    private String globalName;

    private Date timestamp;

    private WfDocDef wfDocDef;

    private WfStepDef startStep;

    private WfManualInitDef manualInitDef;

    private Map<String, WfStepDef> steps;

    public WfTemplateDef(Long wfTemplateId, String globalName, String name, String description, Date timestamp,
            WfDocDef wfDocDef, List<WfStepDef> stepList) {
        super(name, description);
        this.wfTemplateId = wfTemplateId;
        this.globalName = globalName;
        this.timestamp = timestamp;
        this.wfDocDef = wfDocDef;

        if (stepList != null) {
            this.steps = new HashMap<String, WfStepDef>();
            for (WfStepDef wfStepDef : stepList) {
                if (wfStepDef.isStart()) {
                    startStep = wfStepDef;
                } else if (wfStepDef.isManual()) {
                    manualInitDef = new WfManualInitDef(wfDocDef, wfStepDef);
                }

                this.steps.put(wfStepDef.getName(), wfStepDef);
            }
        } else {
            this.steps = Collections.emptyMap();
        }
    }

    public Long getWfTemplateId() {
        return wfTemplateId;
    }

    public String getGlobalName() {
        return globalName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public WfDocDef getWfDocDef() {
        return wfDocDef;
    }

    public WfStepDef getStartStep() {
        return startStep;
    }

    public Set<String> getWfStepNames() {
        return this.steps.keySet();
    }

    public WfManualInitDef getManualInitDef() throws UnifyException {
        if (manualInitDef == null) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_TEMPLATE_NO_MANUAL_INIT,
                    this.getDescription());
        }

        return manualInitDef;
    }

    public boolean isSupportManualInit() {
        return manualInitDef != null;
    }

    public WfStepDef getWfStepDef(String stepName) throws UnifyException {
        WfStepDef wfStepDef = this.steps.get(stepName);
        if (wfStepDef == null) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_STEP_WITH_NAME_UNKNOWN,
                    this.getDescription(), stepName);
        }

        return wfStepDef;
    }
}