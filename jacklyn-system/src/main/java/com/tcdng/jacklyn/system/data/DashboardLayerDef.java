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

import java.util.List;

/**
 * Dashboard layer definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class DashboardLayerDef {

    private String name;
    
    private int numberOfSections;
    
    private List<DashboardPortletDef> portletList;

    public DashboardLayerDef(String name, int numberOfSections, List<DashboardPortletDef> portletList) {
        this.name = name;
        this.numberOfSections = numberOfSections;
        this.portletList = portletList;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfSections() {
        return numberOfSections;
    }

    public List<DashboardPortletDef> getPortletList() {
        return portletList;
    }
}
