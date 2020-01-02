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
import java.util.List;

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Workflow form definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table("JKWFFORM")
public class WfForm extends BaseEntity {

    @ForeignKey(WfDoc.class)
    private Long wfDocId;

    @ListOnly(key = "wfDocId", property = "name")
    private String wfDocName;

    @ListOnly(key = "wfDocId", property = "description")
    private String wfDocDesc;

    @ListOnly(key = "wfDocId", property = "wfCategoryUpdateDt")
    private Date wfCategoryUpdateDt;

    @ChildList
    private List<WfFormTab> tabList;

    @ChildList
    private List<WfFormSection> sectionList;

    @ChildList
    private List<WfFormField> fieldList;

    @Override
    public String getDescription() {
        return wfDocName;
    }

    public Long getWfDocId() {
        return wfDocId;
    }

    public void setWfDocId(Long wfDocId) {
        this.wfDocId = wfDocId;
    }

    public List<WfFormTab> getTabList() {
        return tabList;
    }

    public void setTabList(List<WfFormTab> tabList) {
        this.tabList = tabList;
    }

    public List<WfFormSection> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<WfFormSection> sectionList) {
        this.sectionList = sectionList;
    }

    public List<WfFormField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<WfFormField> fieldList) {
        this.fieldList = fieldList;
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

    public Date getWfCategoryUpdateDt() {
        return wfCategoryUpdateDt;
    }

    public void setWfCategoryUpdateDt(Date wfCategoryUpdateDt) {
        this.wfCategoryUpdateDt = wfCategoryUpdateDt;
    }

}
