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

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseTimestampedEntity;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Represents a workflow item history entity.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Managed(module = WorkflowModuleNameConstants.WORKFLOW_MODULE, title = "Workflow Item History")
@Table("WFITEMHISTORY")
public class WfItemHist extends BaseTimestampedEntity {

    @ForeignKey(WfTemplate.class)
    private Long wfTemplateId;

    @ForeignKey(WfDoc.class)
    private Long wfDocId;

    @Column(nullable = true)
    private Long documentId;

    @Column(length = 64)
    private String description;

    @ListOnly(key = "wfTemplateId", property = "wfCategoryName")
    private String wfCategoryName;

    @ListOnly(key = "wfTemplateId", property = "name")
    private String wfTemplateName;

    @ListOnly(key = "wfTemplateId", property = "description")
    private String wfTemplateDesc;

    @ListOnly(key = "wfDocId", property = "name")
    private String wfDocName;

    @ListOnly(key = "wfDocId", property = "description")
    private String wfDocDesc;

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getWfTemplateId() {
        return wfTemplateId;
    }

    public void setWfTemplateId(Long wfTemplateId) {
        this.wfTemplateId = wfTemplateId;
    }

    public Long getWfDocId() {
        return wfDocId;
    }

    public void setWfDocId(Long wfDocId) {
        this.wfDocId = wfDocId;
    }

    public String getWfCategoryName() {
        return wfCategoryName;
    }

    public void setWfCategoryName(String wfCategoryName) {
        this.wfCategoryName = wfCategoryName;
    }

    public String getWfTemplateName() {
        return wfTemplateName;
    }

    public void setWfTemplateName(String wfTemplateName) {
        this.wfTemplateName = wfTemplateName;
    }

    public String getWfTemplateDesc() {
        return wfTemplateDesc;
    }

    public void setWfTemplateDesc(String wfTemplateDesc) {
        this.wfTemplateDesc = wfTemplateDesc;
    }

    public String getWfDocName() {
        return wfDocName;
    }

    public void setWfDocName(String wfDocName) {
        this.wfDocName = wfDocName;
    }

    public String getWfDocDesc() {
        return wfDocDesc;
    }

    public void setWfDocDesc(String wfDocDesc) {
        this.wfDocDesc = wfDocDesc;
    }

}
