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

package com.tcdng.jacklyn.statistics.business;

import java.util.Map;

import com.tcdng.jacklyn.statistics.data.QuickRatio;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.chart.ChartGenerator;
import com.tcdng.unify.core.chart.PieChart;
import com.tcdng.unify.core.chart.PieChart.AnnotationType;
import com.tcdng.unify.core.chart.PieChart.ValueFormat;
import com.tcdng.unify.core.constant.ColorPalette;

/**
 * Abstract base class for pie chart providers.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractPieChartProvider extends AbstractQuickRatioVisualProvider {

    @Configurable
    private ChartGenerator chartGenerator;

    private ColorPalette colorPalette;

    private AnnotationType annotationType;

    private ValueFormat valueFormat;

    private int width;

    private int height;

    private boolean showLegend;

    public AbstractPieChartProvider(int width, int height) {
        this(ColorPalette.DEFAULT, AnnotationType.LABEL_VALUE, ValueFormat.DECIMAL, width, height, true);
    }

    public AbstractPieChartProvider(ColorPalette colorPalette, AnnotationType annotationType, ValueFormat valueFormat,
            int width, int height, boolean showLegend) {
        this.colorPalette = colorPalette;
        this.annotationType = annotationType;
        this.valueFormat = valueFormat;
        this.width = width;
        this.height = height;
        this.showLegend = showLegend;
    }

    @Override
    protected byte[] doProvidePresentation(QuickRatio quickRatio) throws UnifyException {
        PieChart.Builder pcb =
                PieChart.newBuilder(width, height).colorPalette(colorPalette).annotationType(annotationType)
                        .valueFormat(valueFormat).showLegend(showLegend);
        for (Map.Entry<String, Double> entry : quickRatio.getRatios().entrySet()) {
            pcb.addSeries(entry.getKey(), entry.getValue());
        }

        PieChart chart = pcb.build();
        return chartGenerator.generateImage(chart);
    }

}
