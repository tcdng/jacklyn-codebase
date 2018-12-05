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

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;
import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.util.xml.adapter.RequirementTypeXmlAdapter;

/**
 * Workflow user action configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class WfUserActionConfig extends BaseConfig {

	private String label;

	private String targetStepName;

	private RequirementType noteRequirement;

	private Boolean validatePage;

	private List<WfAttachmentCheckConfig> attachmentCheckConfigList;

	public WfUserActionConfig() {
		this.noteRequirement = RequirementType.NONE;
		this.validatePage = Boolean.FALSE;
	}

	public String getLabel() {
		return label;
	}

	@XmlAttribute
	public void setLabel(String label) {
		this.label = label;
	}

	public String getTargetStepName() {
		return targetStepName;
	}

	@XmlAttribute(name = "target", required = true)
	public void setTargetStepName(String targetStepName) {
		this.targetStepName = targetStepName;
	}

	public RequirementType getNoteRequirement() {
		return noteRequirement;
	}

	@XmlJavaTypeAdapter(RequirementTypeXmlAdapter.class)
	@XmlAttribute(name = "notes-requirement", required = true)
	public void setNoteRequirement(RequirementType noteRequirement) {
		this.noteRequirement = noteRequirement;
	}

	public Boolean getValidatePage() {
		return validatePage;
	}

	@XmlAttribute(name="validate-page")
	public void setValidatePage(Boolean validatePage) {
		this.validatePage = validatePage;
	}

	public List<WfAttachmentCheckConfig> getAttachmentCheckConfigList() {
		return attachmentCheckConfigList;
	}

	@XmlElement(name = "attachment-check")
	public void setAttachmentCheckConfigList(
			List<WfAttachmentCheckConfig> attachmentCheckConfigList) {
		this.attachmentCheckConfigList = attachmentCheckConfigList;
	}
}
