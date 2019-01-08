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

package com.tcdng.jacklyn.system.widgets;

import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.system.data.DashboardDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.upl.AbstractUplGenerator;

/**
 * Base class for dashboard UPL generator.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractDashboardUplGenerator extends AbstractUplGenerator {

    @Configurable
    private SystemService systemService;

    public AbstractDashboardUplGenerator() {
        super("ui-dashboardviewer");
    }

    @Override
    public boolean isNewerVersion(String target) throws UnifyException {
        return !systemService.getRuntimeDashboardDef(target).isRead();
    }

    @Override
    protected void generateBody(StringBuilder sb, String target) throws UnifyException {
        DashboardDef dashboardDef = systemService.getRuntimeDashboardDef(target);
        doGenerateBody(sb, dashboardDef);
        dashboardDef.read();
    }

    protected abstract void doGenerateBody(StringBuilder sb, DashboardDef dashboardDef) throws UnifyException;

}
