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

package com.tcdng.jacklyn.organization.web.beans;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;
import com.tcdng.jacklyn.organization.entities.Branch;

/**
 * Branch page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class BranchPageBean extends BaseEntityPageBean<Branch> {

    private String searchCode;

    private String searchDescription;

    private Long searchZoneId;

    private Long searchStateId;

    private Long searchHubId;

    private RecordStatus searchStatus;

    public BranchPageBean() {
        super("$m{organization.branch.hint}");
    }

    public String getSearchCode() {
        return searchCode;
    }

    public void setSearchCode(String searchCode) {
        this.searchCode = searchCode;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public Long getSearchZoneId() {
        return searchZoneId;
    }

    public void setSearchZoneId(Long searchZoneId) {
        this.searchZoneId = searchZoneId;
    }

    public Long getSearchStateId() {
        return searchStateId;
    }

    public void setSearchStateId(Long searchStateId) {
        this.searchStateId = searchStateId;
    }

    public Long getSearchHubId() {
        return searchHubId;
    }

    public void setSearchHubId(Long searchHubId) {
        this.searchHubId = searchHubId;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

}
