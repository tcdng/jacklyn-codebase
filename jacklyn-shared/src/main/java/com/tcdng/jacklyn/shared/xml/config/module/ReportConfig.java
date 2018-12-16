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
import javax.xml.bind.annotation.XmlElement;

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Report configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class ReportConfig extends BaseConfig {

    private String title;

    private String processor;

    private String reportable;

    private String template;

    private boolean dynamic;

    private boolean managed;

    private ParametersConfig parameters;

    public String getTitle() {
        return title;
    }

    @XmlAttribute(required = true)
    public void setTitle(String title) {
        this.title = title;
    }

    public String getProcessor() {
        return processor;
    }

    @XmlAttribute
    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getReportable() {
        return reportable;
    }

    @XmlAttribute
    public void setReportable(String reportable) {
        this.reportable = reportable;
    }

    public ParametersConfig getParameters() {
        return parameters;
    }

    @XmlElement
    public void setParameters(ParametersConfig parameters) {
        this.parameters = parameters;
    }

    public String getTemplate() {
        return template;
    }

    @XmlAttribute
    public void setTemplate(String template) {
        this.template = template;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    @XmlAttribute
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public boolean isManaged() {
        return managed;
    }

    public void setManaged(boolean managed) {
        this.managed = managed;
    }
}
