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
import com.tcdng.unify.core.chart.AbstractChart.AnnotationType;
import com.tcdng.unify.core.chart.AbstractChart.ValueFormat;
import com.tcdng.unify.core.chart.LineChart;
import com.tcdng.unify.core.constant.ColorPalette;

/**
 * Abstract base class for line chart providers.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractLineChartProvider extends AbstractXYChartProvider {

    public AbstractLineChartProvider(ColorPalette colorPalette, AnnotationType annotationType, ValueFormat valueFormat,
            int width, int height, boolean showLegend, boolean showXAxisTicks, boolean showYAxisTicks) {
        super(colorPalette, annotationType, valueFormat, width, height, showLegend, showXAxisTicks, showYAxisTicks);
    }

    public AbstractLineChartProvider(int width, int height) {
        super(width, height);
    }

    @Override
    protected byte[] doProvidePresentation(QuickXY quickXY) throws UnifyException {
        if(quickXY.isSanityCheck()) {
            LineChart.Builder lcb =
                    LineChart.newBuilder(width, height).colorPalette(colorPalette).annotationType(annotationType)
                            .valueFormat(valueFormat).showLegend(showLegend).showXAxisTicks(showXAxisTicks)
                            .showYAxisTicks(showYAxisTicks);
            for (QuickXY.XY xy : quickXY.getXyList()) {
                lcb.addSeries(xy.getName(), xy.getXValueList(), xy.getYValueList(), xy.getColor());
            }

            LineChart chart = lcb.build();
            return getChartGenerator().generateImage(chart);
        }

        return null;
    }

}
