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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.jacklyn.shared.xml.adapter.BinaryLogicTypeXmlAdapter;
import com.tcdng.jacklyn.shared.xml.config.BaseConfig;
import com.tcdng.unify.core.constant.BinaryLogicType;

/**
 * Workflow document classifier configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfClassifierConfig extends BaseConfig {

    private String logic;

    private BinaryLogicType filterLogic;
    
    private List<WfClassifierFilterConfig> wfClassifierFilterConfigList;

    public WfClassifierConfig() {
        filterLogic = BinaryLogicType.AND;
    }
    
    public String getLogic() {
        return logic;
    }

    @XmlAttribute
    public void setLogic(String logic) {
        this.logic = logic;
    }

    public BinaryLogicType getFilterLogic() {
        return filterLogic;
    }

    @XmlJavaTypeAdapter(BinaryLogicTypeXmlAdapter.class)
    @XmlAttribute(name="filterLogic")
    public void setFilterLogic(BinaryLogicType filterLogic) {
        this.filterLogic = filterLogic;
    }

    public List<WfClassifierFilterConfig> getWfClassifierFilterConfigList() {
        return wfClassifierFilterConfigList;
    }

    @XmlElement(name = "filter", required = true)
    public void setWfClassifierFilterConfigList(List<WfClassifierFilterConfig> wfClassifierFilterConfigList) {
        this.wfClassifierFilterConfigList = wfClassifierFilterConfigList;
    }
}
