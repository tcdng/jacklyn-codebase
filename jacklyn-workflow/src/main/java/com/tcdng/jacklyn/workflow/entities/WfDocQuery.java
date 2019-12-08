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
 * Query class for workflow document definitions.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfDocQuery extends BaseEntityQuery<WfDoc> {

    public WfDocQuery() {
        super(WfDoc.class);
    }

    public WfDocQuery wfCategoryId(Long wfCategoryId) {
        return (WfDocQuery) addEquals("wfCategoryId", wfCategoryId);
    }

    public WfDocQuery wfCategoryName(String wfCategoryName) {
        return (WfDocQuery) addEquals("wfCategoryName", wfCategoryName);
    }

    public WfDocQuery wfCategoryStatus(RecordStatus wfCategoryStatus) {
        return (WfDocQuery) addEquals("wfCategoryStatus", wfCategoryStatus);
    }

    public WfDocQuery wfCategoryVersion(String wfCategoryVersion) {
        return (WfDocQuery) addEquals("wfCategoryVersion", wfCategoryVersion);
    }

    public WfDocQuery name(String name) {
        return (WfDocQuery) addEquals("name", name);
    }

    public WfDocQuery nameLike(String name) {
        return (WfDocQuery) addLike("name", name);
    }

    public WfDocQuery descriptionLike(String description) {
        return (WfDocQuery) addLike("description", description);
    }
}
