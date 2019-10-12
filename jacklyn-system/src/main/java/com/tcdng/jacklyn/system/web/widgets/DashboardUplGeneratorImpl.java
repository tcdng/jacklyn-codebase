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

package com.tcdng.jacklyn.system.web.widgets;

import com.tcdng.jacklyn.shared.constants.OrientationType;
import com.tcdng.jacklyn.system.data.DashboardDef;
import com.tcdng.jacklyn.system.data.DashboardLayerDef;
import com.tcdng.jacklyn.system.data.DashboardPortletDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.DataUtils;

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
        if (OrientationType.HORIZONTAL.equals(dashboardDef.getOrientationType())) {
            appendHorizontalStructure(sb, dashboardDef);
        } else {
            appendVerticalStructure(sb, dashboardDef);
        }
    }

    private void appendHorizontalStructure(StringBuilder sb, DashboardDef dashboardDef) throws UnifyException {
        // Main panel
        sb.append("!ui-panel:dashboardPanel style:$s{width:100%;height:100%;} layout:$d{!ui-vertical style:$s{width:100%;height:100%;} heights:$l{");
        boolean appendSym = false;
        if (DataUtils.isNotBlank(dashboardDef.getLayerList()) && dashboardDef.getLayerList().size() > 1) {
            int len = dashboardDef.getLayerList().size();
            int layerPercentage = 100 / len;
            if (appendSym) {
                sb.append(' ');
            } else {
                appendSym = true;
            }
            
            sb.append(layerPercentage).append('%');
        } else {
            sb.append("100%");
        }
        sb.append("}}");
        
        appendSym = false;
        StringBuilder layerNameSb = new StringBuilder();
        for (DashboardLayerDef dashboardLayerDef : dashboardDef.getLayerList()) {
            if (appendSym) {
                layerNameSb.append(' ');
            } else {
                appendSym = true;
            }

            layerNameSb.append(dashboardLayerDef.getName());
        }
        sb.append(" components:$c{").append(layerNameSb).append("}");
        appendNewline(sb);
        appendNewline(sb);

        // Layers
        for (DashboardLayerDef dashboardLayerDef : dashboardDef.getLayerList()) {
            sb.append("!ui-panel:").append(dashboardLayerDef.getName()).append("  style:$s{width:100%;height:100%;} layout:$d{");
            sb.append("!ui-horizontal style:$s{width:100%;height:100%;} widths:$l{");

            int availableSections = dashboardLayerDef.getNumberOfSections();
            int sectionPercentage = 100 / dashboardLayerDef.getNumberOfSections();
            int usedPercentage = 0;

            appendSym = false;
            StringBuilder nameSb = new StringBuilder();
            StringBuilder wDimSb = new StringBuilder();
            StringBuilder hDimSb = new StringBuilder();
            StringBuilder structSb = new StringBuilder();
            for (DashboardPortletDef dashboardPortletDef : dashboardLayerDef.getPortletList()) {
                if (appendSym) {
                    nameSb.append(' ');
                    wDimSb.append(' ');
                    hDimSb.append(' ');
                } else {
                    appendSym = true;
                }

                hDimSb.append("100%");
                if (dashboardPortletDef.getNumberOfSections() <= availableSections) {
                    availableSections -= dashboardPortletDef.getNumberOfSections();
                    // Append dimension
                    int percentage = dashboardPortletDef.getNumberOfSections() * sectionPercentage;
                    wDimSb.append(percentage).append('%');
                    usedPercentage += percentage;

                    // Append name
                    nameSb.append(dashboardPortletDef.getName());

                    // Append portlet structure
                    appendPortlet(structSb, dashboardPortletDef);
                } else {
                    break;
                }
            }

            if (usedPercentage < 100) {
                if (appendSym) {
                    nameSb.append(' ');
                    wDimSb.append(' ');
                }

                // Append dimension
                wDimSb.append(100 - usedPercentage).append('%');

                // Append name
                String emptyPortletName = dashboardLayerDef.getName() + "Empty";
                nameSb.append(emptyPortletName);

                // Append empty structure
                appendEmptyPortlet(structSb, emptyPortletName);
            }

            sb.append(wDimSb).append("} heights:$l{").append(hDimSb);
            sb.append("}} components:$c{").append(nameSb).append("}");
            appendNewline(sb);

            // Portlets
            sb.append(structSb);
            appendNewline(sb);
            appendNewline(sb);
        }
    }

    private void appendVerticalStructure(StringBuilder sb, DashboardDef dashboardDef) throws UnifyException {
        // Main panel
        sb.append("!ui-panel:dashboardPanel style:$s{width:100%;height:100%;} layout:$d{!ui-horizontal style:$s{width:100%;} widths:$l{");
        boolean appendSym = false;
        String globalEmptyPortlet = dashboardDef.getName() + "Empty";
        StringBuilder nameSb = new StringBuilder();
        StringBuilder dimSb = new StringBuilder();
        if (dashboardDef.getLayerList().isEmpty()) {
            dimSb.append("100%");
            nameSb.append(globalEmptyPortlet);
        } else {
            int width = 100 / dashboardDef.getLayerList().size();
            for (DashboardLayerDef dashboardLayerDef : dashboardDef.getLayerList()) {
                if (appendSym) {
                    nameSb.append(' ');
                    dimSb.append(' ');
                } else {
                    appendSym = true;
                }

                nameSb.append(dashboardLayerDef.getName());
                dimSb.append(width).append('%');
            }
        }
        sb.append(dimSb).append("}} components:$c{").append(nameSb).append("}");
        appendNewline(sb);
        appendNewline(sb);

        // Layers
        if (dashboardDef.getLayerList().isEmpty()) {
            // Append empty structure
            appendEmptyPortlet(sb, globalEmptyPortlet);
        } else {
            for (DashboardLayerDef dashboardLayerDef : dashboardDef.getLayerList()) {
                sb.append("!ui-panel:").append(dashboardLayerDef.getName()).append(" layout:$d{!ui-vertical style:$s{width:100%;}}");
                appendSym = false;
                nameSb = new StringBuilder();
                StringBuilder structSb = new StringBuilder();
                if (dashboardLayerDef.getPortletList().isEmpty()) {
                    String layerEmptyPortlet = dashboardLayerDef.getName() + "Empty";
                    nameSb.append(layerEmptyPortlet);
                    appendEmptyPortlet(structSb, layerEmptyPortlet);
                } else {
                    for (DashboardPortletDef dashboardPortletDef : dashboardLayerDef.getPortletList()) {
                        if (appendSym) {
                            nameSb.append(' ');
                        } else {
                            appendSym = true;
                        }

                        // Append name
                        nameSb.append(dashboardPortletDef.getName());

                        // Append portlet structure
                        appendPortlet(structSb, dashboardPortletDef);
                    }
                }
                sb.append(" components:$c{").append(nameSb).append("}");
                appendNewline(sb);

                // Portlets
                sb.append(structSb);
                appendNewline(sb);
                appendNewline(sb);
            }
        }
    }

    private void appendEmptyPortlet(StringBuilder sb, String emptyPortletName) throws UnifyException {
        sb.append("!ui-label:").append(emptyPortletName).append(" caption:$s{}");
        appendNewline(sb);
    }

    private void appendPortlet(StringBuilder sb, DashboardPortletDef dashboardPortletDef) throws UnifyException {
        sb.append('!').append(dashboardPortletDef.getPanelName()).append(':').append(dashboardPortletDef.getName());
        if (dashboardPortletDef.isRefreshPeriod()) {
            sb.append(" refreshEvery:").append(dashboardPortletDef.getRefreshPeriod());
        }
        appendNewline(sb);
    }
}
