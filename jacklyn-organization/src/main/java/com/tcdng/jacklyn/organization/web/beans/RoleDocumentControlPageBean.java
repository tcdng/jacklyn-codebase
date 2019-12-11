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

package com.tcdng.jacklyn.organization.web.beans;

import java.util.List;

import com.tcdng.jacklyn.common.web.beans.BasePageBean;
import com.tcdng.jacklyn.organization.entities.RolePrivilegeWidget;

/**
 * Role document control page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class RoleDocumentControlPageBean extends BasePageBean {

    private Long searchRoleId;

    private Long searchModuleId;

    private List<RolePrivilegeWidget> rolePrivilegeWidgetList;

    public RoleDocumentControlPageBean() {
        super("searchBodyPanel");
    }

    public Long getSearchRoleId() {
        return searchRoleId;
    }

    public void setSearchRoleId(Long searchRoleId) {
        this.searchRoleId = searchRoleId;
    }

    public Long getSearchModuleId() {
        return searchModuleId;
    }

    public void setSearchModuleId(Long searchModuleId) {
        this.searchModuleId = searchModuleId;
    }

    public List<RolePrivilegeWidget> getRolePrivilegeWidgetList() {
        return rolePrivilegeWidgetList;
    }

    public void setRolePrivilegeWidgetList(List<RolePrivilegeWidget> rolePrivilegeWidgetList) {
        this.rolePrivilegeWidgetList = rolePrivilegeWidgetList;
    }

}
