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
package com.tcdng.jacklyn.workflow.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Workflow item history event.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfItemHistEvent implements Serializable {

    private static final long serialVersionUID = 8850442281117998741L;

    private Long id;

    private String wfStep;

    private Date stepDt;

    private Date actionDt;

    private String actor;

    private String wfAction;

    private String wfActionDesc;

    private String comments;

    private String errorSource;

    private String errorCode;

    private String errorMsg;

    public WfItemHistEvent(Long id, String wfStep, Date stepDt, Date actionDt, String actor, String wfAction,
            String wfActionDesc, String comments, String errorSource, String errorCode, String errorMsg) {
        this.id = id;
        this.wfStep = wfStep;
        this.stepDt = stepDt;
        this.actionDt = actionDt;
        this.actor = actor;
        this.wfAction = wfAction;
        this.wfActionDesc = wfActionDesc;
        this.comments = comments;
        this.errorSource = errorSource;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Long getId() {
        return id;
    }

    public String getWfStep() {
        return wfStep;
    }

    public Date getStepDt() {
        return stepDt;
    }

    public Date getActionDt() {
        return actionDt;
    }

    public String getActor() {
        return actor;
    }

    public String getWfAction() {
        return wfAction;
    }

    public String getWfActionDesc() {
        return wfActionDesc;
    }

    public String getComments() {
        return comments;
    }

    public String getErrorSource() {
        return errorSource;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
