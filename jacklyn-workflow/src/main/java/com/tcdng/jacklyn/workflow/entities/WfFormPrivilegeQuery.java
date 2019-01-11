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

package com.tcdng.jacklyn.workflow.entities;

import com.tcdng.jacklyn.common.entities.BaseEntityQuery;

/**
 * Query class for workflow step form privilege definitions.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfFormPrivilegeQuery extends BaseEntityQuery<WfFormPrivilege> {

    public WfFormPrivilegeQuery() {
        super(WfFormPrivilege.class);
    }

    public WfFormPrivilegeQuery wfStepId(Long wfStepId) {
        return (WfFormPrivilegeQuery) equals("wfStepId", wfStepId);
    }
}
