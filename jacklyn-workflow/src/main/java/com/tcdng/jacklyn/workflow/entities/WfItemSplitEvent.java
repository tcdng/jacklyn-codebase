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
import com.tcdng.unify.core.annotation.Table;

/**
 * Represents a workflow item split event.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Table("JKWFITEMSPLITEVENT")
public class WfItemSplitEvent extends BaseEntity {

    @Column(nullable = true)
    private Long parentSplitEventId;

    @Column(name = "SPLIT_ORIGIN_NM", length = 64)
    private String splitOriginName;

    @Column(name = "SPLIT_BRANCH_NM", length = 64)
    private String splitBranchName;

    @Column
    private int splitCount;

    @Override
    public String getDescription() {
        return null;
    }

    public Long getParentSplitEventId() {
        return parentSplitEventId;
    }

    public void setParentSplitEventId(Long parentSplitEventId) {
        this.parentSplitEventId = parentSplitEventId;
    }

    public String getSplitOriginName() {
        return splitOriginName;
    }

    public void setSplitOriginName(String splitOriginName) {
        this.splitOriginName = splitOriginName;
    }

    public String getSplitBranchName() {
        return splitBranchName;
    }

    public void setSplitBranchName(String splitBranchName) {
        this.splitBranchName = splitBranchName;
    }

    public int getSplitCount() {
        return splitCount;
    }

    public void setSplitCount(int splitCount) {
        this.splitCount = splitCount;
    }

}
