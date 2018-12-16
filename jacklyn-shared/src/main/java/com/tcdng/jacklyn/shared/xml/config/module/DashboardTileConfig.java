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
package com.tcdng.jacklyn.shared.xml.config.module;

import javax.xml.bind.annotation.XmlAttribute;

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Dashboard tile configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class DashboardTileConfig extends BaseConfig {

    private String caption;

    private String path;

    private String image;

    private String generator;

    private boolean landscape;

    public String getCaption() {
        return caption;
    }

    @XmlAttribute(required = true)
    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPath() {
        return path;
    }

    @XmlAttribute(required = true)
    public void setPath(String path) {
        this.path = path;
    }

    public String getImage() {
        return image;
    }

    @XmlAttribute
    public void setImage(String image) {
        this.image = image;
    }

    public String getGenerator() {
        return generator;
    }

    @XmlAttribute
    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public boolean isLandscape() {
        return landscape;
    }

    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }
}
