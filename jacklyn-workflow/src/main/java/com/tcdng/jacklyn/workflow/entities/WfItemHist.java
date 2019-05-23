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

import com.tcdng.jacklyn.common.entities.BaseTimestampedEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Table;

/**
 * Represents a workflow item history entity.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Table("JKWFITEMHISTORY")
public class WfItemHist extends BaseTimestampedEntity {

    @Column(name="GLOBAL_ITEM_TYPE_NM", length = 96)
    private String processGlobalName;

    @Column(nullable = true)
    private Long docId;

    @Column(name= "BRANCH_CD", nullable = true)
    private String branchCode;

    @Column(name= "DEPARTMENT_CD", nullable = true)
    private String departmentCode;

    @Column(name = "ITEM_DESC", length = 64)
    private String description;

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

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
