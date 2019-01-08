/*
 * Copyright 2018 The Code Department
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

package com.tcdng.jacklyn.system.entities;

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.shared.constants.DimensionType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Dashboard layer entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "DASHBOARDLAYER", uniqueConstraints = { @UniqueConstraint({ "dashboardId", "name" }),
        @UniqueConstraint({ "dashboardId", "description" }) })
public class DashboardLayer extends BaseEntity {

    @ForeignKey(Dashboard.class)
    private Long dashboardId;
    
    @ForeignKey(name="LAYER_DIMENSION_TY", nullable=true)
    private DimensionType dimensionType;

    @Column(name = "DASHBOARDLAYER_NM", length = 32)
    private String name;

    @Column(name = "DASHBOARDLAYER_DESC", length = 64)
    private String description;
    
    @Column(name="LAYER_DIMENSION", nullable=true)
    private Integer dimension;
    
    @ListOnly(key="dimensionType", property="description")
    private String dimensionTypeDesc;

    @Override
    public String getDescription() {
        return description;
    }

    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
    }

    public DimensionType getDimensionType() {
        return dimensionType;
    }

    public void setDimensionType(DimensionType dimensionType) {
        this.dimensionType = dimensionType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDimension() {
        return dimension;
    }

    public void setDimension(Integer dimension) {
        this.dimension = dimension;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDimensionTypeDesc() {
        return dimensionTypeDesc;
    }

    public void setDimensionTypeDesc(String dimensionTypeDesc) {
        this.dimensionTypeDesc = dimensionTypeDesc;
    }

}
