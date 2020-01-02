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
package com.tcdng.jacklyn.shared.xml.config.workflow;

import javax.xml.bind.annotation.XmlAttribute;

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Workflow routing configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfRoutingConfig extends BaseConfig {

    private String document;

    private String targetStepName;

    private String classifierName;

    public String getDocument() {
        return document;
    }

    @XmlAttribute
    public void setDocument(String document) {
        this.document = document;
    }

    public String getTargetStepName() {
        return targetStepName;
    }

    @XmlAttribute(name = "target", required = true)
    public void setTargetStepName(String targetStepName) {
        this.targetStepName = targetStepName;
    }

    public String getClassifierName() {
        return classifierName;
    }

    @XmlAttribute(name = "classifier")
    public void setClassifierName(String classifierName) {
        this.classifierName = classifierName;
    }

}
