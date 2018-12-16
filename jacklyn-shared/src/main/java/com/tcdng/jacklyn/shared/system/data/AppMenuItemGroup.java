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
package com.tcdng.jacklyn.shared.system.data;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * Application menu item group.
 * 
 * @author Lateef
 * @since 1.0
 */
public class AppMenuItemGroup {

    private String name;

    private String description;

    private String pageCaption;

    private String caption;

    private String path;

    private List<AppMenuItem> menuItemList;

    public AppMenuItemGroup(String name, String description, String pageCaption, String caption, String path,
            List<AppMenuItem> menuItemList) {
        this.name = name;
        this.description = description;
        this.pageCaption = pageCaption;
        this.caption = caption;
        this.path = path;
        this.menuItemList = menuItemList;
    }

    public AppMenuItemGroup() {

    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPageCaption() {
        return pageCaption;
    }

    @XmlElement
    public void setPageCaption(String pageCaption) {
        this.pageCaption = pageCaption;
    }

    public String getCaption() {
        return caption;
    }

    @XmlElement
    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPath() {
        return path;
    }

    @XmlElement
    public void setPath(String path) {
        this.path = path;
    }

    public List<AppMenuItem> getMenuItemList() {
        return menuItemList;
    }

    @XmlElement(name = "menuItem")
    public void setMenuItemList(List<AppMenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }
}
