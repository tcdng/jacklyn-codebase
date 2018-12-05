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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Workflow category configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@XmlRootElement(name = "wfcategory")
public class WfCategoryConfig extends BaseConfig {

	private String version;

	private WfDocsConfig wfDocsConfig;

	private WfFormsConfig wfFormsConfig;

	private WfMessagesConfig wfMessagesConfig;

	private WfTemplatesConfig wfTemplatesConfig;

	public String getVersion() {
		return version;
	}

	@XmlAttribute(required = true)
	public void setVersion(String version) {
		this.version = version;
	}

	public WfDocsConfig getWfDocsConfig() {
		return wfDocsConfig;
	}

	@XmlElement(name = "documents", required = true)
	public void setWfDocsConfig(WfDocsConfig wfDocsConfig) {
		this.wfDocsConfig = wfDocsConfig;
	}

	public WfFormsConfig getWfFormsConfig() {
		return wfFormsConfig;
	}

	@XmlElement(name = "forms", required = true)
	public void setWfFormsConfig(WfFormsConfig wfFormsConfig) {
		this.wfFormsConfig = wfFormsConfig;
	}

	public WfMessagesConfig getWfMessagesConfig() {
		return wfMessagesConfig;
	}

	@XmlElement(name = "messages", required = true)
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
