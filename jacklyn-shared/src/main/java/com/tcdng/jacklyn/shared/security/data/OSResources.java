/*
 * Copyright 2018-2020 The Code Department
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

package com.tcdng.jacklyn.shared.security.data;

import java.io.Serializable;
import java.util.List;

/**
 * OS resources.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class OSResources implements Serializable {

    private static final long serialVersionUID = 720276654494334107L;

    private String resourceName;

    private String contentType;

    private List<String> styleSheets;

    private List<String> scripts;

    public OSResources(String resourceName, String contentType, List<String> styleSheets, List<String> scripts) {
        this.resourceName = resourceName;
        this.contentType = contentType;
        this.styleSheets = styleSheets;
        this.scripts = scripts;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<String> getStyleSheets() {
        return styleSheets;
    }

    public void setStyleSheets(List<String> styleSheets) {
        this.styleSheets = styleSheets;
    }

    public List<String> getScripts() {
        return scripts;
    }

    public void setScripts(List<String> scripts) {
        this.scripts = scripts;
    }

}
