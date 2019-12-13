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

package com.tcdng.jacklyn.workflow.web.beans;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;
import com.tcdng.jacklyn.workflow.entities.WfDoc;

/**
 * Workflow document entity page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfDocPageBean extends BaseEntityPageBean<WfDoc> {

    private Long searchWfCategoryId;

    private String searchName;

    private String searchDescription;

    private RecordStatus searchStatus;

    public WfDocPageBean() {
        super("$m{workflow.wfdoc.hint}");
    }

    public Long getSearchWfCategoryId() {
        return searchWfCategoryId;
    }

    public void setSearchWfCategoryId(Long searchWfCategoryId) {
        this.searchWfCategoryId = searchWfCategoryId;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

}
