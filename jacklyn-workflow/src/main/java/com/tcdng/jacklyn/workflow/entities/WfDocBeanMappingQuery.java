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

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntityQuery;

/**
 * Query class for workflow document bean mappings.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfDocBeanMappingQuery extends BaseEntityQuery<WfDocBeanMapping> {

    public WfDocBeanMappingQuery() {
        super(WfDocBeanMapping.class);
    }

    public WfDocBeanMappingQuery beanType(String beanType) {
        return (WfDocBeanMappingQuery) addEquals("beanType", beanType);
    }

    public WfDocBeanMappingQuery wfCategoryName(String wfCategoryName) {
        return (WfDocBeanMappingQuery) addEquals("wfCategoryName", wfCategoryName);
    }

    public WfDocBeanMappingQuery wfCategoryStatus(RecordStatus wfCategoryStatus) {
        return (WfDocBeanMappingQuery) addEquals("wfCategoryStatus", wfCategoryStatus);
    }

    public WfDocBeanMappingQuery primaryMapping(Boolean primaryMapping) {
        return (WfDocBeanMappingQuery) addEquals("primaryMapping", primaryMapping);
    }
}
