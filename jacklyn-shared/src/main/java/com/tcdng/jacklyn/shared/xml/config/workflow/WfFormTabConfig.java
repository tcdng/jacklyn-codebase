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

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Workflow form tab configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfFormTabConfig extends BaseConfig {

	private String label;

	private Boolean pseudo;
	
	private List<WfFormSectionConfig> wfFormSectionConfigList;
	
	public WfFormTabConfig() {
		this.pseudo = Boolean.TRUE;
	}

	public String getLabel() {
		return label;
	}

	@XmlAttribute(name = "label")
	public void setLabel(String label) {
		this.label = label;
	}

	public Boolean getPseudo() {
		return pseudo;
	}

	@XmlAttribute(name = "label")
	public void setPseudo(Boolean pseudo) {
		this.pseudo = pseudo;
	}

	public List<WfFormSectionConfig> getWfFormSectionConfigList() {
		return wfFormSectionConfigList;
	}

	@XmlElement(name = "section", required = true)
	public void setWfFormSectionConfigList(List<WfFormSectionConfig> wfFormSectionConfigList) {
		this.wfFormSectionConfigList = wfFormSectionConfigList;
	}

}
