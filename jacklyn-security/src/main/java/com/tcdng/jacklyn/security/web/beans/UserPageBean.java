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

package com.tcdng.jacklyn.security.web.beans;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;
import com.tcdng.jacklyn.security.data.UserLargeData;
import com.tcdng.jacklyn.security.entities.User;

/**
 * User page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class UserPageBean extends BaseEntityPageBean<User> {

    private Long searchRoleId;

    private Long searchBranchId;

    private String searchLoginId;

    private String searchFullName;

    private RecordStatus searchStatus;

    private UserLargeData largeData;

    private UserLargeData clipboardLargeData;

    public UserPageBean() {
        super("$m{security.user.hint}");
        largeData = new UserLargeData();
    }

    public Long getSearchRoleId() {
        return searchRoleId;
    }

    public void setSearchRoleId(Long searchRoleId) {
        this.searchRoleId = searchRoleId;
    }

    public Long getSearchBranchId() {
		return searchBranchId;
	}

	public void setSearchBranchId(Long searchBranchId) {
		this.searchBranchId = searchBranchId;
	}

	public String getSearchLoginId() {
        return searchLoginId;
    }

    public void setSearchLoginId(String searchLoginId) {
        this.searchLoginId = searchLoginId;
    }

    public String getSearchFullName() {
        return searchFullName;
    }

    public void setSearchFullName(String searchFullName) {
        this.searchFullName = searchFullName;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public UserLargeData getLargeData() {
        return largeData;
    }

    public void setLargeData(UserLargeData largeData) {
        this.largeData = largeData;
    }

    public UserLargeData getClipboardLargeData() {
        return clipboardLargeData;
    }

    public void setClipboardLargeData(UserLargeData clipboardLargeData) {
        this.clipboardLargeData = clipboardLargeData;
    }

}
