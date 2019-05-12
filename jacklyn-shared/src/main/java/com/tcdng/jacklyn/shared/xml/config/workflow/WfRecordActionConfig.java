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

package com.tcdng.jacklyn.shared.xml.config.workflow;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.jacklyn.shared.workflow.WorkflowRecordActionType;
import com.tcdng.jacklyn.shared.xml.adapter.WorkflowPersistenceTypeXmlAdapter;
import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Workflow record action configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class WfRecordActionConfig extends BaseConfig {

    private String document;
    
    private WorkflowRecordActionType actionType;

    private String docMappingName;

    public String getDocument() {
        return document;
    }

    @XmlAttribute(required = true)
    public void setDocument(String document) {
        this.document = document;
    }

    public WorkflowRecordActionType getActionType() {
        return actionType;
    }

    @XmlJavaTypeAdapter(WorkflowPersistenceTypeXmlAdapter.class)
    @XmlAttribute(name = "type", required = true)
    public void setActionType(WorkflowRecordActionType actionType) {
        this.actionType = actionType;
    }

    public String getDocMappingName() {
        return docMappingName;
    }

    @XmlAttribute(name = "mapping", required = true)
    public void setDocMappingName(String docMappingName) {
        this.docMappingName = docMappingName;
    }

}
