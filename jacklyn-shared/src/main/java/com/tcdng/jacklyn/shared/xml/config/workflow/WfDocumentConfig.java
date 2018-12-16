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
 * Workflow document configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@XmlRootElement(name = "document")
public class WfDocumentConfig extends BaseConfig {

	private String version;

	private String itemDescFormat;
	
	private WfFieldsConfig wfFieldsConfig;

	private WfClassifiersConfig wfClassifiersConfig;

	private WfAttachmentsConfig wfAttachmentsConfig;
	
	private WfBeanMappingsConfig wfBeanMappingsConfig;

	private WfFormConfig wfFormConfig;

	public String getItemDescFormat() {
		return itemDescFormat;
	}

	@XmlElement(name = "item-description-format", required = true)
	public void setItemDescFormat(String itemDescFormat) {
		this.itemDescFormat = itemDescFormat;
	}

	public String getVersion() {
		return version;
	}

	@XmlAttribute(name = "version", required = true)
	public void setVersion(String version) {
		this.version = version;
	}
	
	public WfFieldsConfig getWfFieldsConfig() {
		return wfFieldsConfig;
	}

	@XmlElement(name="fields", required=true)
	public void setWfFieldsConfig(WfFieldsConfig wfFieldsConfig) {
		this.wfFieldsConfig = wfFieldsConfig;
	}

	public WfClassifiersConfig getWfClassifiersConfig() {
		return wfClassifiersConfig;
	}

	@XmlElement(name="classifiers", required=true)
	public void setWfClassifiersConfig(WfClassifiersConfig wfClassifiersConfig) {
		this.wfClassifiersConfig = wfClassifiersConfig;
	}

	public WfAttachmentsConfig getWfAttachmentsConfig() {
		return wfAttachmentsConfig;
	}

	@XmlElement(name="attachments", required=true)
	public void setWfAttachmentsConfig(WfAttachmentsConfig wfAttachmentsConfig) {
		this.wfAttachmentsConfig = wfAttachmentsConfig;
	}

	public WfBeanMappingsConfig getWfBeanMappingsConfig() {
		return wfBeanMappingsConfig;
	}

	@XmlElement(name="bean-mappings", required=true)
	public void setWfBeanMappingsConfig(WfBeanMappingsConfig wfBeanMappingsConfig) {
		this.wfBeanMappingsConfig = wfBeanMappingsConfig;
	}

	public WfFormConfig getWfFormConfig() {
		return wfFormConfig;
	}

	@XmlElement(name = "form", required = true)
	public void setWfFormConfig(WfFormConfig wfFormConfig) {
		this.wfFormConfig = wfFormConfig;
	}

}
