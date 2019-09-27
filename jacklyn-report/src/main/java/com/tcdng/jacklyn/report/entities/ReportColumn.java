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
package com.tcdng.jacklyn.report.entities;

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.constant.OrderType;

/**
 * Report column.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table("JKREPORTCOLUMN")
public class ReportColumn extends BaseEntity {

    @ForeignKey(ReportConfiguration.class)
    private Long reportConfigurationId;

    @Column(name = "FIELD_NM")
    private String fieldName;

    @ForeignKey(nullable = true)
    private OrderType columnOrder;

    @Column(name = "COLUMN_DESC", length = 48)
    private String description;

    @Column(name = "COLUMN_TY", length = 64, nullable = true)
    private String type;

    @Column(name = "WIDTH_RATIO")
    private int width;

    @Column(name = "GROUP_FG")
    private boolean group;

    @Column(name = "SUM_FG")
    private boolean sum;


    @ListOnly(key = "columnOrder", property = "description")
    private String columnOrderDesc;

    public Long getReportConfigurationId() {
        return reportConfigurationId;
    }

    public void setReportConfigurationId(Long reportConfigurationId) {
        this.reportConfigurationId = reportConfigurationId;
    }

    @Override
    public String getDescription() {
        return description;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public boolean isSum() {
        return sum;
    }

    public void setSum(boolean sum) {
        this.sum = sum;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public OrderType getColumnOrder() {
        return columnOrder;
    }

    public void setColumnOrder(OrderType columnOrder) {
        this.columnOrder = columnOrder;
    }

    public String getColumnOrderDesc() {
        return columnOrderDesc;
    }

    public void setColumnOrderDesc(String columnOrderDesc) {
        this.columnOrderDesc = columnOrderDesc;
    }
}
