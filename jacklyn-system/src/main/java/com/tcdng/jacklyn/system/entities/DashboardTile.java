/*
 * Copyright 2018 The Code Department
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
package com.tcdng.jacklyn.system.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseInstallEntity;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Dashboard tile entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = SystemModuleNameConstants.SYSTEM_MODULE, title = "Dashboard Tile", reportable = true,
        auditable = true)
@Table(name = "DASHBOARDTILE", uniqueConstraints = { @UniqueConstraint({ "moduleId", "name" }) })
public class DashboardTile extends BaseInstallEntity {

    @ForeignKey(Module.class)
    private Long moduleId;

    @Column(name = "DASHBOARDTILE_NM", length = 40)
    private String name;

    @Column(name = "DASHBOARDTILE_DESC", length = 64)
    private String description;

    @Column(length = 64, nullable = true)
    private String imageSrc;

    @Column(length = 255, nullable = true)
    private String caption;

    @Column(length = 128)
    private String path;

    @Column(nullable = true)
    private String generator;

    @Column
    private boolean landscape;

    @Column
    private int displayOrder;

    @ListOnly(name = "MODULE_NM", key = "moduleId", property = "name")
    private String moduleName;

    @ListOnly(name = "MODULE_DESC", key = "moduleId", property = "description")
    private String moduleDesc;

    @ListOnly(key = "moduleId", property = "status")
    private RecordStatus moduleStatus;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
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

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLandscape() {
        return landscape;
    }

    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public RecordStatus getModuleStatus() {
        return moduleStatus;
    }

    public void setModuleStatus(RecordStatus moduleStatus) {
        this.moduleStatus = moduleStatus;
    }
}
