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
package com.tcdng.jacklyn.workflow.controllers;

import com.tcdng.jacklyn.common.controllers.BasePageController;
import com.tcdng.jacklyn.workflow.business.WorkflowModule;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Abstract base page controller for workflow module.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractWorkflowController extends BasePageController {

    @Configurable(WorkflowModuleNameConstants.WORKFLOWBUSINESSMODULE)
    private WorkflowModule workflowModule;

    public AbstractWorkflowController(boolean secured, boolean readOnly) {
        super(secured, readOnly);
    }

    protected WorkflowModule getWorkflowModule() throws UnifyException {
        return workflowModule;
    }
}
