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

package com.tcdng.jacklyn.shared.xml.config.workflow;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.jacklyn.shared.workflow.WorkflowBeanMappingType;
import com.tcdng.jacklyn.shared.xml.adapter.WorkflowBeanMappingTypeXmlAdapter;
import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Workflow document bean mapping configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfBeanMappingConfig extends BaseConfig {

    private WorkflowBeanMappingType type;

    private String beanType;

    private List<WfFieldMappingConfig> fieldMappingList;

    public WfBeanMappingConfig() {
        this.type = WorkflowBeanMappingType.NONENTRY;
    }

    public WorkflowBeanMappingType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(WorkflowBeanMappingTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(WorkflowBeanMappingType type) {
        this.type = type;
    }

    public String getBeanType() {
        return beanType;
    }

    @XmlAttribute(name = "bean-type", required = true)
    public void setBeanType(String beanType) {
        this.beanType = beanType;
    }

    public List<WfFieldMappingConfig> getFieldMappingList() {
        return fieldMappingList;
    }

    @XmlElement(name = "field-mapping", required = true)
    public void setFieldMappingList(List<WfFieldMappingConfig> mappingList) {
        this.fieldMappingList = mappingList;
    }
}
