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

package com.tcdng.jacklyn.system.data;

import java.util.Date;
import java.util.List;

import com.tcdng.jacklyn.shared.constants.OrientationType;

/**
 * Dashboard definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class DashboardDef {

    private String name;
    
    private OrientationType orientationType;
    
    private Date timestamp;

    private List<DashboardLayerDef> layerList;
    
    private String viewer;

    private boolean read;

    public DashboardDef(String name, OrientationType orientationType, Date timestamp,
            List<DashboardLayerDef> layerList, String viewer) {
        this.name = name;
        this.orientationType = orientationType;
        this.timestamp = timestamp;
        this.layerList = layerList;
        this.viewer = viewer;
    }

    public String getName() {
        return name;
    }

    public OrientationType getOrientationType() {
        return orientationType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public List<DashboardLayerDef> getLayerList() {
        return layerList;
    }

    public String getViewer() {
        return viewer;
    }

    public boolean isRead() {
        return read;
    }

    public void read() {
        read = true;
    }

}
