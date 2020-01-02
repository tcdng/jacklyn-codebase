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
package com.tcdng.jacklyn.shared.system.data;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.tcdng.jacklyn.shared.BaseToolingItem;

/**
 * Application menu item group.
 * 
 * @author Lateef
 * @since 1.0
 */
public class AppMenuItemGroup extends BaseToolingItem {

    private String pageCaption;

    private String caption;

    private String openPath;

    private List<AppMenuItem> menuItemList;

    public AppMenuItemGroup(String name, String description, String pageCaption, String caption, String openPath,
            List<AppMenuItem> menuItemList) {
        super(name, description);
        this.pageCaption = pageCaption;
        this.caption = caption;
        this.openPath = openPath;
        this.menuItemList = menuItemList;
    }

    public AppMenuItemGroup() {

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

    public String getOpenPath() {
        return openPath;
    }

    @XmlElement
    public void setOpenPath(String openPath) {
        this.openPath = openPath;
    }

    public List<AppMenuItem> getMenuItemList() {
        return menuItemList;
    }

    @XmlElement(name = "menuItem")
    public void setMenuItemList(List<AppMenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }
}
