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
 * Query class for workflow tagged mapping.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfTaggedMappingQuery extends BaseEntityQuery<WfTaggedMapping> {

    public WfTaggedMappingQuery() {
        super(WfTaggedMapping.class);
    }

    public WfTaggedMappingQuery tagName(String tagName) {
        return (WfTaggedMappingQuery) equals("tagName", tagName);
    }

    public WfTaggedMappingQuery wfCategoryId(Long wfCategoryId) {
        return (WfTaggedMappingQuery) equals("wfCategoryId", wfCategoryId);
    }

    public WfTaggedMappingQuery wfCategoryName(String wfCategoryName) {
        return (WfTaggedMappingQuery) equals("wfCategoryName", wfCategoryName);
    }

    public WfTaggedMappingQuery wfCategoryStatus(RecordStatus wfCategoryStatus) {
        return (WfTaggedMappingQuery) equals("wfCategoryStatus", wfCategoryStatus);
    }
}
