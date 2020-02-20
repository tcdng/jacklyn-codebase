/*
 * Copyright 2018-2020 The Code Department
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

package com.tcdng.jacklyn.workflow.providers;

import java.util.ArrayList;
import java.util.List;

import com.tcdng.jacklyn.statistics.data.QuickRatio;
import com.tcdng.jacklyn.statistics.providers.AbstractPieChartProvider;
import com.tcdng.jacklyn.workflow.business.WorkflowService;
import com.tcdng.jacklyn.workflow.data.WfItemCountStatusInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.chart.AbstractChart.AnnotationType;
import com.tcdng.unify.core.chart.AbstractChart.ValueFormat;
import com.tcdng.unify.core.constant.ColorPalette;

/**
 * My work items pie provider.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("myworkitemspiechart-provider")
public class MyWorkItemsPieChartProvider extends AbstractPieChartProvider {

    @Configurable
    private WorkflowService workflowService;

    public MyWorkItemsPieChartProvider() {
        super(ColorPalette.CUSTOM, AnnotationType.LABEL_VALUE, ValueFormat.COUNT, 720, 400, true);
    }

    @Override
    protected QuickRatio doProvideRatio(Object... params) throws UnifyException {
        List<QuickRatio.Ratio> ratios = new ArrayList<QuickRatio.Ratio>();
        List<WfItemCountStatusInfo> wfItemStatusInfoList = workflowService.getCurrentWorkItemCountStatusList();
        for (WfItemCountStatusInfo wfItemCountStatusInfo : wfItemStatusInfoList) {
            if (wfItemCountStatusInfo.getItemCount() != 0) {
                String name = resolveSessionMessage(wfItemCountStatusInfo.getStatus().getCaption());
                ratios.add(new QuickRatio.Ratio(name, wfItemCountStatusInfo.getItemCount().doubleValue(),
                        wfItemCountStatusInfo.getStatus().getColorScheme().pallete().getShade(0)));
            }
        }

        return new QuickRatio(ratios);
    }

}
