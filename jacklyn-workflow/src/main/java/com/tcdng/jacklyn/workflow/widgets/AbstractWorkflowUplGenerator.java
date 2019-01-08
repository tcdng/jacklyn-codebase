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

package com.tcdng.jacklyn.workflow.widgets;

import com.tcdng.jacklyn.workflow.business.WorkflowService;
import com.tcdng.jacklyn.workflow.data.BaseLabelWfDef;
import com.tcdng.jacklyn.workflow.data.WfFormDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.upl.AbstractUplGenerator;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Base class for workflow UPL generator.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractWorkflowUplGenerator extends AbstractUplGenerator {

    @Configurable
    private WorkflowService workflowService;

    public AbstractWorkflowUplGenerator(String uplComponentName) {
        super(uplComponentName);
    }

    @Override
    public boolean isNewerVersion(String target) throws UnifyException {
        WfFormDef wfFormDef = workflowService.getRuntimeWfFormDef(target);
        return !wfFormDef.isRead();
    }

    @Override
    protected void generateBody(StringBuilder sb, String target) throws UnifyException {
        WfFormDef wfFormDef = workflowService.getRuntimeWfFormDef(target);
        doGenerateBody(sb, wfFormDef);
        wfFormDef.read(); // Indicate generation has been done for this document instance
    }

    protected WorkflowService getWorkflowModule() {
        return workflowService;
    }

    protected void appendLabel(StringBuilder sb, BaseLabelWfDef baseWfDef) throws UnifyException {
        String label = baseWfDef.getDescription();
        if (!StringUtils.isBlank(baseWfDef.getLabel())) {
            label = resolveSessionMessage(baseWfDef.getLabel());
        }

        sb.append(label);
    }

    protected abstract void doGenerateBody(StringBuilder sb, WfFormDef wfFormDef) throws UnifyException;

}
