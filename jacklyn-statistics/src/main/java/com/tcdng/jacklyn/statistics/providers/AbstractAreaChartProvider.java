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

package com.tcdng.jacklyn.statistics.providers;

import com.tcdng.jacklyn.statistics.data.QuickXY;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.chart.AbstractChart.AnnotationType;
import com.tcdng.unify.core.chart.AbstractChart.ValueFormat;
import com.tcdng.unify.core.chart.ChartGenerator;
import com.tcdng.unify.core.chart.AreaChart;
import com.tcdng.unify.core.constant.ColorPalette;

/**
 * Abstract base class for area chart providers.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractAreaChartProvider extends AbstractQuickXYVisualProvider {

    @Configurable
    private ChartGenerator chartGenerator;

    private ColorPalette colorPalette;

    private AnnotationType annotationType;

    private ValueFormat valueFormat;

    private int width;

    private int height;

    private boolean showLegend;

    private boolean showXAxisTicks;

    private boolean showYAxisTicks;

    public AbstractAreaChartProvider(int width, int height) {
        this(ColorPalette.DEFAULT, AnnotationType.LABEL_VALUE, ValueFormat.COUNT, width, height, true, true, false);
    }

    public AbstractAreaChartProvider(ColorPalette colorPalette, AnnotationType annotationType, ValueFormat valueFormat,
            int width, int height, boolean showLegend, boolean showXAxisTicks, boolean showYAxisTicks) {
        this.colorPalette = colorPalette;
        this.annotationType = annotationType;
        this.valueFormat = valueFormat;
        this.width = width;
        this.height = height;
        this.showLegend = showLegend;
        this.showXAxisTicks = showXAxisTicks;
        this.showYAxisTicks = showYAxisTicks;
    }

    @Override
    protected byte[] doProvidePresentation(QuickXY quickXY) throws UnifyException {
        if (quickXY.isSanityCheck()) {
            AreaChart.Builder acb =
                    AreaChart.newBuilder(width, height).colorPalette(colorPalette).annotationType(annotationType)
                            .valueFormat(valueFormat).showLegend(showLegend).showXAxisTicks(showXAxisTicks)
                            .showYAxisTicks(showYAxisTicks);
            for (QuickXY.XY xy : quickXY.getXyList()) {
                acb.addSeries(xy.getName(), xy.getXValueList(), xy.getYValueList(), xy.getColor());
            }

            AreaChart chart = acb.build();
            return chartGenerator.generateImage(chart);
        }
        
        return null;
    }

}
