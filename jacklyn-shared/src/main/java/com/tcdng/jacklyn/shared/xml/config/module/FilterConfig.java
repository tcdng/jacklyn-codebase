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
package com.tcdng.jacklyn.shared.xml.config.module;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.jacklyn.shared.xml.adapter.RestrictionTypeXmlAdapter;
import com.tcdng.unify.core.criterion.RestrictionType;

/**
 * Document filter configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class FilterConfig {

    private String fieldName;

    private RestrictionType op;

    private String value1;

    private String value2;

    private boolean useParameter;

    private List<FilterConfig> filterList;
    
    public String getFieldName() {
        return fieldName;
    }

    @XmlAttribute(name = "field")
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public RestrictionType getOp() {
        return op;
    }

    @XmlJavaTypeAdapter(RestrictionTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setOp(RestrictionType op) {
        this.op = op;
    }

    public String getValue1() {
        return value1;
    }

    @XmlAttribute
    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    @XmlAttribute
    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public boolean isUseParameter() {
        return useParameter;
    }

    @XmlAttribute
    public void setUseParameter(boolean useParameter) {
        this.useParameter = useParameter;
    }

    public List<FilterConfig> getFilterList() {
        return filterList;
    }

    @XmlElement(name = "filter")
    public void setFilterList(List<FilterConfig> filterList) {
        this.filterList = filterList;
    }

}
