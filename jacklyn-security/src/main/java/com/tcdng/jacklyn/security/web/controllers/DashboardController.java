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
package com.tcdng.jacklyn.security.web.controllers;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;

/**
 * Dashboard controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/security/landing")
@UplBinding("web/security/upl/dashboardlanding.upl")
public class DashboardController extends AbstractSecurityController {

    private String dashboardViewer;

    public DashboardController() {
        super(true, false);// Secure and read-only
    }

    public String getDashboardViewer() {
        return dashboardViewer;
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        dashboardViewer = getSecurityService().getCurrentUserRoleDashboardViewer();
   }

    @Override
    protected String getDocViewPanelName() {
        return "mainDashboardPanel";
    }
}
