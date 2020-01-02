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

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity that represents workflow template document definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "JKWFTEMPLATEDOC", uniqueConstraints = { @UniqueConstraint({ "wfTemplateId", "wfDocName" }) })
public class WfTemplateDoc extends BaseEntity {

    @ForeignKey(WfTemplate.class)
    private Long wfTemplateId;

    @Column(name = "DOCUMENT_NM", length = 32)
    private String wfDocName;

    @Column(name = "DOCUMENT_VIEWER", length = 64, nullable = true)
    private String wfDocViewer;

    @Column(name = "MANUAL_FG")
    private Boolean manual;

    @Override
    public String getDescription() {
        return wfDocName;
    }

    public Long getWfTemplateId() {
        return wfTemplateId;
    }

    public void setWfTemplateId(Long wfTemplateId) {
        this.wfTemplateId = wfTemplateId;
    }

    public String getWfDocName() {
        return wfDocName;
    }

    public void setWfDocName(String wfDocName) {
        this.wfDocName = wfDocName;
    }

    public String getWfDocViewer() {
        return wfDocViewer;
    }

    public void setWfDocViewer(String wfDocViewer) {
        this.wfDocViewer = wfDocViewer;
    }

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

}
