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
package com.tcdng.jacklyn.organization.entities;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.system.entities.Module;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Privilege group entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "JKPRIVILEGEGRP", uniqueConstraints = { @UniqueConstraint({ "moduleId", "privilegeCategoryId" }) })
public class PrivilegeGroup extends BaseEntity {

    @ForeignKey(Module.class)
    private Long moduleId;

    @ForeignKey(PrivilegeCategory.class)
    private Long privilegeCategoryId;

    @ListOnly(name = "MODULE_NM", key = "moduleId", property = "name")
    private String moduleName;

    @ListOnly(name = "MODULE_DESC", key = "moduleId", property = "description")
    private String moduleDesc;

    @ListOnly(key = "moduleId", property = "status")
    private RecordStatus moduleStatus;

    @ListOnly(key = "privilegeCategoryId", property = "name")
    private String categoryName;

    @ListOnly(key = "privilegeCategoryId", property = "description")
    private String categoryDesc;

    @ListOnly(key = "privilegeCategoryId", property = "status")
    private RecordStatus categoryStatus;

    @Override
    public String getDescription() {
        return null;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getFullDescription() {
        return StringUtils.concatenate(this.moduleDesc, " [", this.categoryDesc, "]");
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }

    public RecordStatus getModuleStatus() {
        return moduleStatus;
    }

    public void setModuleStatus(RecordStatus moduleStatus) {
        this.moduleStatus = moduleStatus;
    }

    public Long getPrivilegeCategoryId() {
        return privilegeCategoryId;
    }

    public void setPrivilegeCategoryId(Long privilegeCategoryId) {
        this.privilegeCategoryId = privilegeCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public RecordStatus getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(RecordStatus categoryStatus) {
        this.categoryStatus = categoryStatus;
    }
}
