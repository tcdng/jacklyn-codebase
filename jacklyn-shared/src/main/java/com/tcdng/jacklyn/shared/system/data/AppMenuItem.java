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
package com.tcdng.jacklyn.shared.system.data;

import javax.xml.bind.annotation.XmlElement;

/**
 * Application menu item.
 * 
 * @author Lateef
 * @since 1.0
 */
public class AppMenuItem {

    private String name;

    private String description;

    private String pageCaption;

    private String caption;

    private String path;

    public AppMenuItem(String name, String description, String pageCaption, String caption, String path) {
        this.name = name;
        this.description = description;
        this.pageCaption = pageCaption;
        this.caption = caption;
        this.path = path;
    }

    public AppMenuItem() {

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
}
