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
package com.tcdng.jacklyn.workflow.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Workflow tagged mapping.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Managed(
        module = WorkflowModuleNameConstants.WORKFLOW_MODULE, title = "Workflow Tagged Mapping", reportable = true,
        auditable = true)
@Table(
        name = "JKWFTAGGEDMAPPING",
        uniqueConstraints = { @UniqueConstraint({ "wfCategoryId", "name" }),
                @UniqueConstraint({ "wfCategoryId", "description" }),
                @UniqueConstraint({ "wfCategoryId", "tagName" }) })
public class WfTaggedMapping extends BaseEntity {

    @ForeignKey(WfCategory.class)
    private Long wfCategoryId;

    @Column(name = "TAGGEDMAPPING_NM", length = 32)
    private String name;

    @Column(name = "TAGGEDMAPPING_DESC", length = 64)
    private String description;

    @Column(name = "DOCUMENT_NM", length = 32)
    private String wfDocName;

    @Column(name = "TEMPLATE_NM", length = 32)
    private String wfTemplateName;

    @Column(name = "TAG_NM", length = 32)
    private String tagName;

    @ListOnly(key = "wfCategoryId", property = "name")
    private String wfCategoryName;

    @ListOnly(key = "wfCategoryId", property = "status")
    private RecordStatus wfCategoryStatus;

    @ListOnly(key = "wfCategoryId", property = "updateDt")
    private Date wfCategoryUpdateDt;

    @Override
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getWfCategoryId() {
        return wfCategoryId;
    }

    public void setWfCategoryId(Long wfCategoryId) {
        this.wfCategoryId = wfCategoryId;
    }

    public String getWfDocName() {
        return wfDocName;
    }

    public void setWfDocName(String wfDocName) {
        this.wfDocName = wfDocName;
    }

    public String getWfTemplateName() {
        return wfTemplateName;
    }

    public void setWfTemplateName(String wfTemplateName) {
        this.wfTemplateName = wfTemplateName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
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

    public Date getWfCategoryUpdateDt() {
        return wfCategoryUpdateDt;
    }

    public void setWfCategoryUpdateDt(Date wfCategoryUpdateDt) {
        this.wfCategoryUpdateDt = wfCategoryUpdateDt;
    }
}
