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
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Workflow document field mapping definition entity.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Table("JKWFDOCFIELDMAPPING")
public class WfDocFieldMapping extends BaseEntity {

    @ForeignKey(WfDocBeanMapping.class)
    private Long wfDocBeanMappingId;

    @Column(name = "DOCFIELD_NM", length = 32)
    private String docFieldName;

    @Column(name = "BEANFIELD_NM", length = 32)
    private String beanFieldName;

    @Override
    public String getDescription() {
        return StringUtils.concatenate(docFieldName, "<->", beanFieldName);
    }

    public Long getWfDocBeanMappingId() {
        return wfDocBeanMappingId;
    }

    public void setWfDocBeanMappingId(Long wfDocBeanMappingId) {
        this.wfDocBeanMappingId = wfDocBeanMappingId;
    }

    public String getDocFieldName() {
        return docFieldName;
    }

    public void setDocFieldName(String docFieldName) {
        this.docFieldName = docFieldName;
    }

    public String getBeanFieldName() {
        return beanFieldName;
    }

    public void setBeanFieldName(String beanFieldName) {
        this.beanFieldName = beanFieldName;
    }

}
