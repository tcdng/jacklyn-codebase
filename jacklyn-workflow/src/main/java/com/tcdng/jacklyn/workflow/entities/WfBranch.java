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

import java.util.List;

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity that represents workflow parallel branch definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(
        name = "JKWFBRANCH", uniqueConstraints = { @UniqueConstraint({ "wfStepId", "name" }),
                @UniqueConstraint({ "wfStepId", "description" }) })
public class WfBranch extends BaseEntity {

    @ForeignKey(WfStep.class)
    private Long wfStepId;

    @Column(name = "BRANCH_NM", length = 32)
    private String name;

    @Column(name = "BRANCH_DESC", length = 64)
    private String description;

    @Column(name = "TARGET", length = 64)
    private String target;

    @ListOnly(key = "wfStepId", property = "name")
    private String wfStepName;

    @ChildList
    private List<WfMergeField> mergeFieldList;

    @Override
    public String getDescription() {
        return description;
    }

    public Long getWfStepId() {
        return wfStepId;
    }

    public void setWfStepId(Long wfStepId) {
        this.wfStepId = wfStepId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getWfStepName() {
        return wfStepName;
    }

    public void setWfStepName(String wfStepName) {
        this.wfStepName = wfStepName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<WfMergeField> getMergeFieldList() {
        return mergeFieldList;
    }

    public void setMergeFieldList(List<WfMergeField> mergeFieldList) {
        this.mergeFieldList = mergeFieldList;
    }

}
