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

package com.tcdng.jacklyn.shared.xml.config.workflow;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Workflow template document configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfTemplateDocConfig {

    private String name;
    
    private String viewer;
    
    private String assigner;
    
    private boolean manual;

    public String getName() {
        return name;
    }

    @XmlAttribute(required = true)
    public void setName(String name) {
        this.name = name;
    }

    public String getViewer() {
        return viewer;
    }

    @XmlAttribute
    public void setViewer(String viewer) {
        this.viewer = viewer;
    }

    public String getAssigner() {
        return assigner;
    }

    @XmlAttribute
    public void setAssigner(String assigner) {
        this.assigner = assigner;
    }

    public boolean isManual() {
        return manual;
    }

    @XmlAttribute(required = true)
    public void setManual(boolean manual) {
        this.manual = manual;
    }
}
