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

package com.tcdng.jacklyn.workflow.business;

import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.jacklyn.workflow.data.WfAlertDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Default workflow item alert logic implementation.
 * 
 * @author Lateef
 * @since 1.0
 */
@Component(name = WorkflowModuleNameConstants.DEFAULTWORKFLOWITEMALERTLOGIC,
        description = "Default Workflow Item Alert Logic")
public class WfItemAlertLogicImpl extends AbstractWfItemAlertLogic {

    @Override
    public void sendAlert(WfItemReader wfItemReader, WfAlertDef wfAlertDef) throws UnifyException {
        // TODO Auto-generated method stub

    }

}
