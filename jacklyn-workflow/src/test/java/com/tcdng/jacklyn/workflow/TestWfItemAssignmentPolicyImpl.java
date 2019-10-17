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

package com.tcdng.jacklyn.workflow;

import com.tcdng.jacklyn.workflow.business.AbstractWfItemAssignmentPolicy;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Test workflow item assignment policy implementation.
 * 
 * @author Lateef
 * @since 1.0
 */
@Component("test-wfitemassignmentpolicy")
public class TestWfItemAssignmentPolicyImpl extends AbstractWfItemAssignmentPolicy {

    @Override
    public String execute(FlowingWfItem.Reader flowingWfItemReader) throws UnifyException {
        return "terry5432";
    }

}
