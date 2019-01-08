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

import com.tcdng.unify.core.util.StringUtils;

/**
 * Dashboard portlet definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class DashboardPortletDef {

    private String name;

    private String panelName;

    private String dimension;

    private long refreshPeriodMillSec;

    public DashboardPortletDef(String name, String panelName, String dimension, long refreshPeriodMillSec) {
        this.name = name;
        this.panelName = panelName;
        this.dimension = dimension;
        this.refreshPeriodMillSec = refreshPeriodMillSec;
    }

    public String getName() {
        return name;
    }

    public String getPanelName() {
        return panelName;
    }

    public String getDimension() {
        return dimension;
    }

    public long getRefreshPeriod() {
        return refreshPeriodMillSec;
    }

    public boolean isDimension() {
        return !StringUtils.isBlank(dimension);
    }

    public boolean isRefreshPeriod() {
        return refreshPeriodMillSec > 0;
    }
}
