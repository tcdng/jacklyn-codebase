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

import javax.xml.bind.annotation.XmlElement;

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Workflow steps configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class WfStepsConfig extends BaseConfig {

    private List<WfStepConfig> wfStepConfigList;

    public List<WfStepConfig> getWfStepConfigList() {
        return wfStepConfigList;
    }

    @XmlElement(name = "step", required = true)
    public void setWfStepConfigList(List<WfStepConfig> wfStepConfigList) {
        this.wfStepConfigList = wfStepConfigList;
    }

}
