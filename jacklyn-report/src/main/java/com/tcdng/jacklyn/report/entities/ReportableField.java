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
package com.tcdng.jacklyn.report.entities;

import com.tcdng.jacklyn.common.entities.BaseInstallEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Table;

/**
 * Entity for storing reportable field information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table("JKREPORTABLEFLD")
public class ReportableField extends BaseInstallEntity {

    @ForeignKey(ReportableDefinition.class)
    private Long reportableId;

    @Column(name = "REPORTABLEFIELD_NM")
    private String name;

    @Column(name = "REPORTABLEFIELD_DESC", length = 64, nullable = true)
    private String description;

    @Column(length = 128, nullable = true)
    private String type;

    @Column(nullable = true)
    private String formatter;

    @Column(nullable = true)
    private String horizontalAlign;

    @Column(nullable = true)
    private Integer width;

    @Column
    private boolean parameterOnly;

    @Override
    public String getDescription() {
        return description;
    }

    public Long getReportableId() {
        return reportableId;
    }

    public void setReportableId(Long reportableId) {
        this.reportableId = reportableId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getHorizontalAlign() {
        return horizontalAlign;
    }

    public void setHorizontalAlign(String horizontalAlign) {
        this.horizontalAlign = horizontalAlign;
    }

    public boolean isParameterOnly() {
        return parameterOnly;
    }

    public void setParameterOnly(boolean parameterOnly) {
        this.parameterOnly = parameterOnly;
    }
}
