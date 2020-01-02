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

package com.tcdng.jacklyn.system.entities;

import java.util.List;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedTimestampedStatusEntity;
import com.tcdng.jacklyn.shared.constants.OrientationType;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Dashboard entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = SystemModuleNameConstants.SYSTEM_MODULE, title = "Dashboard", reportable = true, auditable = true)
@Table(name = "JKDASHBOARD", uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }) })
public class Dashboard extends BaseVersionedTimestampedStatusEntity {

    @ForeignKey
    private OrientationType orientationType;

    @Column(name = "DASHBOARD_NM", length = 32)
    private String name;

    @Column(name = "DASHBOARD_DESC", length = 64)
    private String description;

    @ListOnly(key = "orientationType", property = "description")
    private String orientationTypeDesc;

    @ChildList
    private List<DashboardLayer> layerList;
    
    @ChildList
    private List<DashboardPortlet> portletList;
    
    @Override
    public String getDescription() {
        return description;
    }

    public Dashboard(OrientationType orientationType, String name, String description) {
        this.orientationType = orientationType;
        this.name = name;
        this.description = description;
    }

    public Dashboard() {

    }

    public OrientationType getOrientationType() {
        return orientationType;
    }

    public void setOrientationType(OrientationType orientationType) {
        this.orientationType = orientationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrientationTypeDesc() {
        return orientationTypeDesc;
    }

    public void setOrientationTypeDesc(String orientationTypeDesc) {
        this.orientationTypeDesc = orientationTypeDesc;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DashboardLayer> getLayerList() {
        return layerList;
    }

    public void setLayerList(List<DashboardLayer> layerList) {
        this.layerList = layerList;
    }

    public List<DashboardPortlet> getPortletList() {
        return portletList;
    }

    public void setPortletList(List<DashboardPortlet> portletList) {
        this.portletList = portletList;
    }

}
