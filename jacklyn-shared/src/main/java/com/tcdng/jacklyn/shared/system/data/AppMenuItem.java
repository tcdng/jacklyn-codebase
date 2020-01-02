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

import javax.xml.bind.annotation.XmlElement;

import com.tcdng.jacklyn.shared.BaseToolingItem;

/**
 * Application menu item.
 * 
 * @author Lateef
 * @since 1.0
 */
public class AppMenuItem extends BaseToolingItem {

    private String pageCaption;

    private String caption;

    private String openPath;

    private boolean hidden;

    public AppMenuItem(String name, String description, String pageCaption, String caption, String openPath,
            boolean hidden) {
        super(name, description);
        this.pageCaption = pageCaption;
        this.caption = caption;
        this.openPath = openPath;
        this.hidden = hidden;
    }

    public AppMenuItem() {

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

    public boolean isHidden() {
        return hidden;
    }

    @XmlElement
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
