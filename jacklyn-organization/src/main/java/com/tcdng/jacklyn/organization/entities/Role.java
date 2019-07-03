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
package com.tcdng.jacklyn.organization.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.organization.constants.OrganizationModuleNameConstants;
import com.tcdng.jacklyn.system.entities.Dashboard;
import com.tcdng.jacklyn.system.entities.Theme;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity for storing role information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(
        module = OrganizationModuleNameConstants.ORGANIZATION_MODULE, title = "Role", reportable = true,
        auditable = true)
@Table(name = "JKROLE", uniqueConstraints = { @UniqueConstraint({ "name" }) })
public class Role extends BaseVersionedStatusEntity {

    @ForeignKey(Department.class)
    private Long departmentId;

    @ForeignKey(type = Dashboard.class, nullable = true)
    private Long dashboardId;

    @ForeignKey(type = Theme.class, nullable = true)
    private Long themeId;

    @Column(name = "ROLE_NM", length = 40)
    private String name;

    @Column(name = "ROLE_DESC", length = 64)
    private String description;

    @Column(type = ColumnType.TIMESTAMP_UTC, transformer = "timeofday-transformer", nullable = true)
    private Date activeAfter;

    @Column(type = ColumnType.TIMESTAMP_UTC, transformer = "timeofday-transformer", nullable = true)
    private Date activeBefore;

    @Column(length = 64, nullable = true)
    private String application;

    @Column(length = 64, nullable = true)
    private String email;

    @ListOnly(key = "departmentId", property = "name")
    private String departmentName;

    @ListOnly(key = "departmentId", property = "description")
    private String departmentDesc;
    
    @ListOnly(key = "dashboardId", property = "name")
    private String dashboardName;

    @ListOnly(key = "themeId", property = "description")
    private String themeDesc;

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getActiveAfter() {
        return activeAfter;
    }

    public void setActiveAfter(Date activeAfter) {
        this.activeAfter = activeAfter;
    }

    public Date getActiveBefore() {
        return activeBefore;
    }

    public void setActiveBefore(Date activeBefore) {
        this.activeBefore = activeBefore;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getThemeDesc() {
        return themeDesc;
    }

    public void setThemeDesc(String themeDesc) {
        this.themeDesc = themeDesc;
    }

    public String getDashboardName() {
        return dashboardName;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentDesc() {
        return departmentDesc;
    }

    public void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
    }
}
