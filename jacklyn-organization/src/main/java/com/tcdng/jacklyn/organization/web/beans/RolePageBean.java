/*
 * Copyright 2018-2020 The Code Role
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
import com.tcdng.jacklyn.organization.data.RoleLargeData;
import com.tcdng.jacklyn.organization.entities.Role;

/**
 * Role page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class RolePageBean extends BaseEntityPageBean<Role> {

    private String searchName;

    private String searchDescription;

    private Long searchDepartmentId;
    
    private RecordStatus searchStatus;

    private RoleLargeData largeData;

    private RoleLargeData clipboardLargeData;

    public RolePageBean() {
        super("$m{organization.role.hint}");
        largeData = new RoleLargeData();
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

    public Long getSearchDepartmentId() {
        return searchDepartmentId;
    }

    public void setSearchDepartmentId(Long searchDepartmentId) {
        this.searchDepartmentId = searchDepartmentId;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public RoleLargeData getLargeData() {
        return largeData;
    }

    public void setLargeData(RoleLargeData largeData) {
        this.largeData = largeData;
    }

    public RoleLargeData getClipboardLargeData() {
        return clipboardLargeData;
    }

    public void setClipboardLargeData(RoleLargeData clipboardLargeData) {
        this.clipboardLargeData = clipboardLargeData;
    }

}
