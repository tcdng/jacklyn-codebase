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
import java.util.Arrays;
import java.util.List;

import com.tcdng.jacklyn.statistics.data.QuickCategory;
import com.tcdng.jacklyn.statistics.providers.AbstractBarChartProvider;
import com.tcdng.jacklyn.workflow.business.WorkflowService;
import com.tcdng.jacklyn.workflow.data.WfProcessWorkloadInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.chart.AbstractChart.AnnotationType;
import com.tcdng.unify.core.chart.AbstractChart.ValueFormat;
import com.tcdng.unify.core.constant.ColorPalette;

/**
 * My workload bar chat provider.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("myworkloadbarchart-provider")
public class MyWorkloadBarChartProvider extends AbstractBarChartProvider {

    private static final int MAX_CATEGORIES = 3;

    @Configurable
    private WorkflowService workflowService;

    public MyWorkloadBarChartProvider() {
        super(ColorPalette.BLUE_SCALE, AnnotationType.LABEL_VALUE, ValueFormat.COUNT, 720, 400, true, true, false);
    }

    @Override
    protected QuickCategory doProvideCategory(Object... params) throws UnifyException {
        List<WfProcessWorkloadInfo> workloadInfoList = workflowService.getCurrentUserProcessWorkloadList();
        List<String> categoryNameList = new ArrayList<String>();
        List<Integer> allSeries = new ArrayList<Integer>();
        List<Integer> userSeries = new ArrayList<Integer>();
        int count = 0;
        for (WfProcessWorkloadInfo wfProcessWorkloadInfo : workloadInfoList) {
            categoryNameList.add(wfProcessWorkloadInfo.getDocumentName());
            allSeries.add(wfProcessWorkloadInfo.getTotalWorkload());
            userSeries.add(wfProcessWorkloadInfo.getUserWorkload());
            if (++count >= MAX_CATEGORIES) {
                break;
            }
        }

        return new QuickCategory(categoryNameList,
                Arrays.asList(
                        new QuickCategory.Category(resolveSessionMessage("$m{workflow.chart.alltasks}"), allSeries),
                        new QuickCategory.Category(resolveSessionMessage("$m{workflow.chart.workload}"), userSeries)));
    }

}
