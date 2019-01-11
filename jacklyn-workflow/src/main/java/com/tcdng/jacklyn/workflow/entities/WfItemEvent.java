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
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Represents a workflow item event.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Managed(module = WorkflowModuleNameConstants.WORKFLOW_MODULE, title = "Workflow Item Event")
@Table("WFITEMEVENT")
public class WfItemEvent extends BaseEntity {

    @ForeignKey(WfItemHist.class)
    private Long wfItemHistId;

    @Column
    private String wfStepName;

    @Column(type = ColumnType.TIMESTAMP)
    private Date stepDt;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date actionDt;

    @Column(nullable = true)
    private String actor;

    @Column(length = 32, nullable = true)
    private String wfAction;

    @Column(length = 512, nullable = true)
    private String notes;

    @ListOnly(key = "wfItemHistId", property = "documentId")
    private Long documentId;

    @Override
    public String getDescription() {
        return null;
    }

    public Long getWfItemHistId() {
        return wfItemHistId;
    }

    public void setWfItemHistId(Long wfItemHistId) {
        this.wfItemHistId = wfItemHistId;
    }

    public String getWfStepName() {
        return wfStepName;
    }

    public void setWfStepName(String wfStepName) {
        this.wfStepName = wfStepName;
    }

    public Date getStepDt() {
        return stepDt;
    }

    public void setStepDt(Date stepDt) {
        this.stepDt = stepDt;
    }

    public Date getActionDt() {
        return actionDt;
    }

    public void setActionDt(Date actionDt) {
        this.actionDt = actionDt;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getWfAction() {
        return wfAction;
    }

    public void setWfAction(String wfAction) {
        this.wfAction = wfAction;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }
}
