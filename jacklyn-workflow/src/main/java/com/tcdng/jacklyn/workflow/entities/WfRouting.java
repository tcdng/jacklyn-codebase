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

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity that represents workflow routing definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "JKWFROUTING", uniqueConstraints = { @UniqueConstraint({ "wfStepId", "name" }),
        @UniqueConstraint({ "wfStepId", "description" }) })
public class WfRouting extends BaseEntity {

    @ForeignKey(WfStep.class)
    private Long wfStepId;

    @Column(name = "TARGET_STEP_NM", length = 32)
    private String targetWfStepName;

    @Column(name = "ROUTING_NM", length = 32)
    private String name;

    @Column(name = "ROUTING_DESC", length = 64)
    private String description;

    @Column(name = "DOC_NM", length = 32, nullable = true)
    private String docName;

    @Column(name = "CLASSIFIER_NM", nullable = true)
    private String classifierName;

    @ListOnly(key = "wfStepId", property = "name")
    private String wfStepName;

    @ListOnly(key = "wfStepId", property = "description")
    private String wfStepDesc;

    @Override
    public String getDescription() {
        return this.description;
    }

    public Long getWfStepId() {
        return wfStepId;
    }

    public void setWfStepId(Long wfStepId) {
        this.wfStepId = wfStepId;
    }

    public String getTargetWfStepName() {
        return targetWfStepName;
    }

    public void setTargetWfStepName(String targetWfStepName) {
        this.targetWfStepName = targetWfStepName;
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

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getClassifierName() {
        return classifierName;
    }

    public void setClassifierName(String classifierName) {
        this.classifierName = classifierName;
    }

    public String getWfStepName() {
        return wfStepName;
    }

    public void setWfStepName(String wfStepName) {
        this.wfStepName = wfStepName;
    }

    public String getWfStepDesc() {
        return wfStepDesc;
    }

    public void setWfStepDesc(String wfStepDesc) {
        this.wfStepDesc = wfStepDesc;
    }

}
