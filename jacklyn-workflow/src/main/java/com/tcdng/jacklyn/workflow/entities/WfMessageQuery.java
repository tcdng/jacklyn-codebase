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
package com.tcdng.jacklyn.workflow.entities;

import java.util.Collection;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntityQuery;

/**
 * Query class for workflow message definitions.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfMessageQuery extends BaseEntityQuery<WfMessage> {

    public WfMessageQuery() {
        super(WfMessage.class);
    }

    public WfMessageQuery wfCategoryId(Long wfCategoryId) {
        return (WfMessageQuery) addEquals("wfCategoryId", wfCategoryId);
    }

    public WfMessageQuery wfCategoryName(String wfCategoryName) {
        return (WfMessageQuery) addEquals("wfCategoryName", wfCategoryName);
    }

    public WfMessageQuery wfCategoryStatus(RecordStatus wfCategoryStatus) {
        return (WfMessageQuery) addEquals("wfCategoryStatus", wfCategoryStatus);
    }

    public WfMessageQuery wfCategoryVersion(String wfCategoryVersion) {
        return (WfMessageQuery) addEquals("wfCategoryVersion", wfCategoryVersion);
    }

    public WfMessageQuery name(String name) {
        return (WfMessageQuery) addEquals("name", name);
    }

    public WfMessageQuery namesIn(Collection<String> names) {
        return (WfMessageQuery) addAmongst("name", names);
    }

    public WfMessageQuery descriptionLike(String description) {
        return (WfMessageQuery) addLike("description", description);
    }
}
