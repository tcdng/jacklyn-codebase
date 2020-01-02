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
package com.tcdng.jacklyn.system.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseInstallEntity;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity for storing menu item information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = SystemModuleNameConstants.SYSTEM_MODULE, title = "Application Menu Item", reportable = true,
        auditable = true)
@Table(name = "JKMENUITEM", uniqueConstraints = { @UniqueConstraint({ "menuId", "name" }) })
public class ApplicationMenuItem extends BaseInstallEntity {

    @ForeignKey(ApplicationMenu.class)
    private Long menuId;

    @Column(nullable = true)
    private Long parentMenuItemId;

    @Column(name = "MENUITEM_NM", length = 48)
    private String name;

    @Column(name = "MENUITEM_DESC", length = 64)
    private String description;

    @Column(length = 64)
    private String pageCaption;

    @Column(length = 64)
    private String caption;

    @Column(length = 128)
    private String path;

    @Column(length = 128, nullable = true)
    private String remotePath;

    @Column
    private int displayOrder;

    @Column(name = "HIDDEN_FG")
    private Boolean hidden;
    
    @ListOnly(key = "menuId", property = "moduleId")
    private Long moduleId;

    @ListOnly(name = "MODULE_NM", key = "menuId", property = "moduleName")
    private String moduleName;

    @ListOnly(key = "menuId", property = "caption")
    private String menuCaption;

    @Override
    public String getDescription() {
        return this.description;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getParentMenuItemId() {
        return parentMenuItemId;
    }

    public void setParentMenuItemId(Long parentMenuItemId) {
        this.parentMenuItemId = parentMenuItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPageCaption() {
        return pageCaption;
    }

    public void setPageCaption(String pageCaption) {
        this.pageCaption = pageCaption;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean isHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

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

    public String getMenuCaption() {
        return menuCaption;
    }

    public void setMenuCaption(String menuCaption) {
        this.menuCaption = menuCaption;
    }
}
