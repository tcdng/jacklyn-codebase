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

package com.tcdng.jacklyn.system.entities;

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Dashboard portlet entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "JKDASHBOARDPORTLET", uniqueConstraints = { @UniqueConstraint({ "dashboardId", "name" }),
        @UniqueConstraint({ "dashboardId", "description" }) })
public class DashboardPortlet extends BaseEntity {

    @ForeignKey(Dashboard.class)
    private Long dashboardId;

    @Column(name = "DASHBOARDPORTLET_NM", length = 32)
    private String name;

    @Column(name = "DASHBOARDPORTLET_DESC", length = 64)
    private String description;
   
    @Column(name="LAYER_NM")
    private String layerName;

    @Column(name="PANEL_NM", length=64)
    private String panelName;
    
    @Column
    private Integer numberOfSections;
    
    @Column(nullable = true) //Refresh period in seconds
    private Integer refreshPeriod;
    
    public DashboardPortlet(String name, String description, String layerName, String panelName,
            Integer numberOfSections, Integer refreshPeriod) {
        this.name = name;
        this.description = description;
        this.layerName = layerName;
        this.panelName = panelName;
        this.numberOfSections = numberOfSections;
        this.refreshPeriod = refreshPeriod;
    }
    
    public DashboardPortlet() {

    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getPanelName() {
        return panelName;
    }

    public void setPanelName(String panelName) {
        this.panelName = panelName;
    }

    public Integer getNumberOfSections() {
        return numberOfSections;
    }

    public void setNumberOfSections(Integer numberOfSections) {
        this.numberOfSections = numberOfSections;
    }

    public Integer getRefreshPeriod() {
        return refreshPeriod;
    }

    public void setRefreshPeriod(Integer refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
