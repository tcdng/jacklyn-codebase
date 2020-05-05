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

package com.tcdng.jacklyn.workflow.business;

import java.util.List;

import com.tcdng.jacklyn.workflow.data.FlowingWfItem.Reader;
import com.tcdng.jacklyn.workflow.data.WfItemAssigneeInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Tooling;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Even spread workflow item assignment policy implementation.
 * 
 * @author Lateef
 * @since 1.0
 */
@Tooling(description = "Even-spread Assignment")
@Component(name = "evenspread-wfassignmentpolicy", description = "Even-spread Assignment Policy")
public class EvenSpreadWfItemAssignmentPolicy extends AbstractWfItemAssignmentPolicy {

    @Override
    public String assignWorkItem(List<WfItemAssigneeInfo> wfItemAssigneeInfoList, Reader flowingWfItemReader)
            throws UnifyException {
        if (!DataUtils.isBlank(wfItemAssigneeInfoList)) {
            if(wfItemAssigneeInfoList.size() == 1) {
                return wfItemAssigneeInfoList.get(0).getUserLoginId();
            }

            if (wfItemAssigneeInfoList.size() > 1) {
                DataUtils.sortAscending(wfItemAssigneeInfoList, WfItemAssigneeInfo.class, "assignedCount");
                String assignedTo = wfItemAssigneeInfoList.get(0).getUserLoginId();
                if (!assignedTo.equals(flowingWfItemReader.getItemHeldBy())) {
                    return assignedTo;
                }

                return wfItemAssigneeInfoList.get(1).getUserLoginId();
            }
        }

        return null;
    }

}
