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

import com.tcdng.jacklyn.shared.constants.OrientationType;
import com.tcdng.jacklyn.system.data.DashboardDef;
import com.tcdng.jacklyn.system.data.DashboardLayerDef;
import com.tcdng.jacklyn.system.data.DashboardPortletDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Default implementation of dashboard UPL generator.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("dashboard-generator")
public class DashboardUplGeneratorImpl extends AbstractDashboardUplGenerator {

    @Override
    protected void doGenerateBody(StringBuilder sb, DashboardDef dashboardDef) throws UnifyException {
        // Main panel
        sb.append("!ui-panel:dashboardPanel style:$s{width:100%;height:100%;} layout:$d{");
        boolean isHorizontalLayers = OrientationType.HORIZONTAL.equals(dashboardDef.getOrientationType());
        if (isHorizontalLayers) {
            // This is not an error. Horizontal layers are rendered by vertical layout.
            sb.append("!ui-vertical style:$s{width:100%;} heights:$l{");
        } else {
            sb.append("!ui-horizontal style:$s{height:100%;} widths:$l{");
        }

        boolean appendSym = false;
        StringBuilder layerNameSb = new StringBuilder();
        for (DashboardLayerDef dashboardLayerDef : dashboardDef.getLayerList()) {
            if (appendSym) {
                layerNameSb.append(' ');
                sb.append(' ');
            } else {
                appendSym = true;
            }

            layerNameSb.append(dashboardLayerDef.getName());
            if (dashboardLayerDef.isDimension()) {
                sb.append(dashboardLayerDef.getDimension());
            } else {
                sb.append("none");
            }
        }
        sb.append("}} components:$c{").append(layerNameSb).append("}");
        appendNewline(sb);
        appendNewline(sb);

        // Layers
        for (DashboardLayerDef dashboardLayerDef : dashboardDef.getLayerList()) {
            sb.append("ui-panel:").append(dashboardLayerDef.getName()).append(" layout:$d{");
            if (isHorizontalLayers) {
                // Horizontal layer portlets are rendered by horizontal layout.
                sb.append("!ui-horizontal style:$s{width:100%;} widths:$l{");
            } else {
                sb.append("!ui-vertical style:$s{height:100%;} heights:$l{");
            }

            appendSym = false;
            StringBuilder portletNameSb = new StringBuilder();
            StringBuilder portletsStructSb = new StringBuilder();
            for (DashboardPortletDef dashboardPortletDef : dashboardLayerDef.getPortletList()) {
                if (appendSym) {
                    portletNameSb.append(' ');
                    sb.append(' ');
                } else {
                    appendSym = true;
                }

                // Layer info
                portletNameSb.append(dashboardPortletDef.getName());
                if (dashboardPortletDef.isDimension()) {
                    sb.append(dashboardPortletDef.getDimension());
                } else {
                    sb.append("none");
                }

                // Portlet structure
                portletsStructSb.append('!').append(dashboardPortletDef.getPanelName()).append(':')
                        .append(dashboardPortletDef.getName());
                if (dashboardPortletDef.isRefreshPeriod()) {
                    portletsStructSb.append(" refreshEvery:").append(dashboardPortletDef.getRefreshPeriod());
                }
                appendNewline(portletsStructSb);
            }
            sb.append("}} components:$c{").append(portletNameSb).append("}");
            // Portlets
            sb.append(portletsStructSb);
            appendNewline(sb);
            appendNewline(sb);
        }
    }

}
