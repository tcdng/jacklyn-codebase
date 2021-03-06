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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Workflow category configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@XmlRootElement(name = "category")
public class WfCategoryConfig extends BaseConfig {

    private String version;

    private WfDocumentsConfig wfDocumentsConfig;

    private WfMessagesConfig wfMessagesConfig;

    private WfTemplatesConfig wfTemplatesConfig;

    public String getVersion() {
        return version;
    }

    @XmlAttribute(name = "version", required = true)
    public void setVersion(String version) {
        this.version = version;
    }

    public WfDocumentsConfig getWfDocumentsConfig() {
        return wfDocumentsConfig;
    }

    @XmlElement(name = "documents")
    public void setWfDocumentsConfig(WfDocumentsConfig wfDocumentsConfig) {
        this.wfDocumentsConfig = wfDocumentsConfig;
    }

    public WfMessagesConfig getWfMessagesConfig() {
        return wfMessagesConfig;
    }

    @XmlElement(name = "messages")
    public void setWfMessagesConfig(WfMessagesConfig wfMessagesConfig) {
        this.wfMessagesConfig = wfMessagesConfig;
    }

    public WfTemplatesConfig getWfTemplatesConfig() {
        return wfTemplatesConfig;
    }

    @XmlElement(name = "templates", required = true)
    public void setWfTemplatesConfig(WfTemplatesConfig wfTemplatesConfig) {
        this.wfTemplatesConfig = wfTemplatesConfig;
    }

}
