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

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Workflow document bean mapping configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfBeanMappingConfig extends BaseConfig {

    private String beanType;

    private Boolean receptacleMapping;

    private Boolean primaryMapping;

    private List<WfFieldMappingConfig> fieldMappingList;

    public WfBeanMappingConfig() {
        this.receptacleMapping = Boolean.FALSE;
        this.primaryMapping = Boolean.FALSE;
    }

    public String getBeanType() {
        return beanType;
    }

    @XmlAttribute(name = "bean-type", required = true)
    public void setBeanType(String beanType) {
        this.beanType = beanType;
    }

    public Boolean getReceptacleMapping() {
        return receptacleMapping;
    }

    @XmlAttribute(name = "receptacle")
    public void setReceptacleMapping(Boolean receptacleMapping) {
        this.receptacleMapping = receptacleMapping;
    }

    public List<WfFieldMappingConfig> getFieldMappingList() {
        return fieldMappingList;
    }

    public Boolean getPrimaryMapping() {
        return primaryMapping;
    }

    @XmlAttribute(name = "primary")
    public void setPrimaryMapping(Boolean primaryMapping) {
        this.primaryMapping = primaryMapping;
    }

    @XmlElement(name = "field-mapping", required = true)
    public void setFieldMappingList(List<WfFieldMappingConfig> mappingList) {
        this.fieldMappingList = mappingList;
    }
}
