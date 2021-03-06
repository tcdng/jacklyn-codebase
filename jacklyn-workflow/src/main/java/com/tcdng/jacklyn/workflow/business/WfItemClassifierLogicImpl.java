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
package com.tcdng.jacklyn.workflow.business;

import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem.Reader;
import com.tcdng.jacklyn.workflow.data.WfDocClassifierDef;
import com.tcdng.jacklyn.workflow.data.WfDocClassifierFilterDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.constant.BinaryLogicType;

/**
 * Default workflow item classifier logic implementation.
 * 
 * @author Lateef
 * @since 1.0
 */
@Component(
        name = WorkflowModuleNameConstants.DEFAULTWORKFLOWITEMCLASSIFIERLOGIC,
        description = "Default Workflow Item Classifier Logic")
public class WfItemClassifierLogicImpl extends AbstractWfItemClassifierLogic {

    @Override
    public boolean match(Reader flowingWfItemReader, WfDocClassifierDef wfDocClassifierDef) throws UnifyException {
        if (BinaryLogicType.AND.equals(wfDocClassifierDef.getFilterLogic())) {
            for (WfDocClassifierFilterDef filter : wfDocClassifierDef.getFilterList()) {
                if (!applyFilter(flowingWfItemReader, filter)) {
                    return false;
                }
            }

            return true;
        } else if (BinaryLogicType.OR.equals(wfDocClassifierDef.getFilterLogic())) {
            for (WfDocClassifierFilterDef filter : wfDocClassifierDef.getFilterList()) {
                if (applyFilter(flowingWfItemReader, filter)) {
                    return true;
                }
            }
            return true;
        }

        return false;
    }

}
