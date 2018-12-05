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

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Workflow message configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfMessageConfig extends BaseConfig {

	private String subject;
	
	private String body;
	
	private String attachmentGenerator;

	private Boolean html;
	
	public WfMessageConfig() {
		html = Boolean.FALSE;
	}
	
	public String getSubject() {
		return subject;
	}

	@XmlElement(required = true)
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	@XmlElement(required = true)
	public void setBody(String body) {
		this.body = body;
	}

	public String getAttachmentGenerator() {
		return attachmentGenerator;
	}

	@XmlAttribute(name="attachment-generator", required = true)
	public void setAttachmentGenerator(String attachmentGenerator) {
		this.attachmentGenerator = attachmentGenerator;
	}

	public Boolean getHtml() {
		return html;
	}

	@XmlAttribute(required = true)
	public void setHtml(Boolean html) {
		this.html = html;
	}
}
