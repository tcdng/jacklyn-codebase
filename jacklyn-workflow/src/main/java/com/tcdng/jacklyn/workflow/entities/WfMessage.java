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
package com.tcdng.jacklyn.workflow.entities;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Workflow message entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "WFMESSAGE",
		uniqueConstraints = { @UniqueConstraint({ "wfTemplateId", "name" }),
				@UniqueConstraint({ "wfTemplateId", "description" }) })
public class WfMessage extends BaseEntity {

	@ForeignKey(WfTemplate.class)
	private Long wfTemplateId;

	@Column(name = "MESSAGE_NM", length = 64)
	private String name;

	@Column(name = "MESSAGE_DESC", length = 64)
	private String description;

	@Column(length = 64)
	private String subject;

	@Column(length = 2048)
	private String template;

	@Column(name = "HTML_FG")
	private Boolean htmlFlag;

	@Column(length = 32, nullable = true)
	private String attachmentGenerator;
	
	@ListOnly(key = "wfTemplateId", property = "name")
	private String wfTemplateName;

	@ListOnly(key = "wfTemplateId", property = "wfCategoryId")
	private Long wfCategoryId;

	@ListOnly(key = "wfTemplateId", property = "wfCategoryStatus")
	private RecordStatus wfCategoryStatus;

	@ListOnly(key = "wfTemplateId", property = "wfCategoryVersion")
	private String wfCategoryVersion;

	@ListOnly(key = "wfTemplateId", property = "wfCategoryName")
	private String wfCategoryName;

	@Override
	public String getDescription() {
		return this.description;
	}

	public Long getWfTemplateId() {
		return wfTemplateId;
	}

	public void setWfTemplateId(Long wfTemplateId) {
		this.wfTemplateId = wfTemplateId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getHtmlFlag() {
		return htmlFlag;
	}

	public void setHtmlFlag(Boolean htmlFlag) {
		this.htmlFlag = htmlFlag;
	}

	public String getAttachmentGenerator() {
		return attachmentGenerator;
	}

	public void setAttachmentGenerator(String attachmentGenerator) {
		this.attachmentGenerator = attachmentGenerator;
	}

	public String getWfTemplateName() {
		return wfTemplateName;
	}

	public void setWfTemplateName(String wfTemplateName) {
		this.wfTemplateName = wfTemplateName;
	}

	public Long getWfCategoryId() {
		return wfCategoryId;
	}

	public void setWfCategoryId(Long wfCategoryId) {
		this.wfCategoryId = wfCategoryId;
	}

	public String getWfCategoryName() {
		return wfCategoryName;
	}

	public void setWfCategoryName(String wfCategoryName) {
		this.wfCategoryName = wfCategoryName;
	}

	public RecordStatus getWfCategoryStatus() {
		return wfCategoryStatus;
	}

	public void setWfCategoryStatus(RecordStatus wfCategoryStatus) {
		this.wfCategoryStatus = wfCategoryStatus;
	}

	public String getWfCategoryVersion() {
		return wfCategoryVersion;
	}

	public void setWfCategoryVersion(String wfCategoryVersion) {
		this.wfCategoryVersion = wfCategoryVersion;
	}
}
