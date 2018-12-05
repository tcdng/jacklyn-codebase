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

import javax.xml.bind.annotation.XmlElement;

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Workflow document configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfDocConfig extends BaseConfig {
	
	private WfDocFieldsConfig wfDocFieldsConfig;

	private WfDocClassifiersConfig wfDocClassifiersConfig;

	private WfDocAttachmentsConfig wfDocAttachmentsConfig;
	
	private WfDocBeanMappingsConfig wfDocBeanMappingsConfig;
	
	public WfDocFieldsConfig getWfDocFieldsConfig() {
		return wfDocFieldsConfig;
	}

	@XmlElement(name="fields", required=true)
	public void setWfDocFieldsConfig(WfDocFieldsConfig wfDocFieldsConfig) {
		this.wfDocFieldsConfig = wfDocFieldsConfig;
	}

	public WfDocClassifiersConfig getWfDocClassifiersConfig() {
		return wfDocClassifiersConfig;
	}

	@XmlElement(name="classifiers", required=true)
	public void setWfDocClassifiersConfig(WfDocClassifiersConfig wfDocClassifiersConfig) {
		this.wfDocClassifiersConfig = wfDocClassifiersConfig;
	}

	public WfDocAttachmentsConfig getWfDocAttachmentsConfig() {
		return wfDocAttachmentsConfig;
	}

	@XmlElement(name="attachments", required=true)
	public void setWfDocAttachmentsConfig(WfDocAttachmentsConfig wfDocAttachmentsConfig) {
		this.wfDocAttachmentsConfig = wfDocAttachmentsConfig;
	}

	public WfDocBeanMappingsConfig getWfDocBeanMappingsConfig() {
		return wfDocBeanMappingsConfig;
	}

	@XmlElement(name="bean-mappings", required=true)
	public void setWfDocBeanMappingsConfig(WfDocBeanMappingsConfig wfDocBeanMappingsConfig) {
		this.wfDocBeanMappingsConfig = wfDocBeanMappingsConfig;
	}
}
