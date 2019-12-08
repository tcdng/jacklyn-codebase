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

import java.util.Collection;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntityQuery;

/**
 * Query class for workflow templates.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfTemplateQuery extends BaseEntityQuery<WfTemplate> {

    public WfTemplateQuery() {
        super(WfTemplate.class);
    }

    public WfTemplateQuery wfDocName(String wfDocName) {
        return (WfTemplateQuery) addEquals("wfDocName", wfDocName);
    }

    public WfTemplateQuery wfCategoryId(Long wfCategoryId) {
        return (WfTemplateQuery) addEquals("wfCategoryId", wfCategoryId);
    }

    public WfTemplateQuery wfCategoryName(String wfCategoryName) {
        return (WfTemplateQuery) addEquals("wfCategoryName", wfCategoryName);
    }

    public WfTemplateQuery wfCategoryNameIn(Collection<String> wfCategoryName) {
        return (WfTemplateQuery) addAmongst("wfCategoryName", wfCategoryName);
    }

    public WfTemplateQuery wfCategoryStatus(RecordStatus wfCategoryStatus) {
        return (WfTemplateQuery) addEquals("wfCategoryStatus", wfCategoryStatus);
    }

    public WfTemplateQuery wfCategoryVersion(String wfCategoryVersion) {
        return (WfTemplateQuery) addEquals("wfCategoryVersion", wfCategoryVersion);
    }

    public WfTemplateQuery name(String name) {
        return (WfTemplateQuery) addEquals("name", name);
    }

    public WfTemplateQuery nameIn(Collection<String> name) {
        return (WfTemplateQuery) addAmongst("name", name);
    }

    public WfTemplateQuery nameLike(String name) {
        return (WfTemplateQuery) addLike("name", name);
    }

    public WfTemplateQuery descriptionLike(String description) {
        return (WfTemplateQuery) addLike("description", description);
    }

    public WfTemplateQuery manualOption(Boolean manualOption) {
        return (WfTemplateQuery) addEquals("manualOption", manualOption);
    }
}
