/*
 * Copyright 2018-2020 The Code Department.
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

import com.tcdng.jacklyn.common.entities.BaseEntity;
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
@Table("JKWFITEMEVENT")
public class WfItemEvent extends BaseEntity {

    @ForeignKey(WfItemHist.class)
    private Long wfItemHistId;

    @Column(length = 64)
    private String wfStepName;

    @Column(type = ColumnType.TIMESTAMP_UTC)
    private Date stepDt;

    @Column(type = ColumnType.TIMESTAMP_UTC, nullable = true)
    private Date expectedDt;

    @Column(type = ColumnType.TIMESTAMP_UTC, nullable = true)
    private Date criticalDt;

    @Column(type = ColumnType.TIMESTAMP_UTC, nullable = true)
    private Date actionDt;

    @Column(nullable = true)
    private String actor;

    @Column(length = 32, nullable = true)
    private String wfAction;

    @Column(name = "ACTOR_COMMENT", length = 512, nullable = true)
    private String comment;

    @Column(length = 64, nullable = true)
    private String srcWfStepName;

    @Column(name = "ERROR_CD", length = 32, nullable = true)
    private String errorCode;

    @Column(name = "ERROR_MSG", length = 512, nullable = true)
    private String errorMsg;

    @ListOnly(key = "wfItemHistId", property = "processGlobalName")
    private String processGlobalName;

    @ListOnly(key = "wfItemHistId", property = "docId")
    private Long docId;

    @ListOnly(key = "wfItemHistId", property = "description")
    private String wfItemDesc;

    @Override
    public String getDescription() {
        return wfItemDesc;
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

    public Date getCriticalDt() {
        return criticalDt;
    }

    public void setCriticalDt(Date criticalDt) {
        this.criticalDt = criticalDt;
    }

    public Date getExpectedDt() {
        return expectedDt;
    }

    public void setExpectedDt(Date expectedDt) {
        this.expectedDt = expectedDt;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSrcWfStepName() {
        return srcWfStepName;
    }

    public void setSrcWfStepName(String srcWfStepName) {
        this.srcWfStepName = srcWfStepName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getProcessGlobalName() {
        return processGlobalName;
    }

    public void setProcessGlobalName(String processGlobalName) {
        this.processGlobalName = processGlobalName;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public String getWfItemDesc() {
        return wfItemDesc;
    }

    public void setWfItemDesc(String wfItemDesc) {
        this.wfItemDesc = wfItemDesc;
    }
}
