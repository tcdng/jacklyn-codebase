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

package com.tcdng.jacklyn.system.web.beans;

import java.util.List;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;
import com.tcdng.jacklyn.system.entities.ApplicationMenuItem;

/**
 * Application menu item page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ApplicationMenuItemPageBean extends BaseEntityPageBean<ApplicationMenuItem> {

    private Long searchMenuId;

    private RecordStatus searchStatus;
    
    private List<ApplicationMenuItem> menuItemOrderList;

    public ApplicationMenuItemPageBean() {
        super("$m{system.menuitem.hint}");
    }

    public Long getSearchMenuId() {
        return searchMenuId;
    }

    public void setSearchMenuId(Long searchMenuId) {
        this.searchMenuId = searchMenuId;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public List<ApplicationMenuItem> getMenuItemOrderList() {
        return menuItemOrderList;
    }

    public void setMenuItemOrderList(List<ApplicationMenuItem> menuItemOrderList) {
        this.menuItemOrderList = menuItemOrderList;
    }

}
