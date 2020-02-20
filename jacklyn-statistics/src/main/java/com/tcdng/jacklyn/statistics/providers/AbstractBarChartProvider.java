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

import com.tcdng.jacklyn.statistics.data.QuickCategory;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.chart.AbstractChart.AnnotationType;
import com.tcdng.unify.core.chart.AbstractChart.ValueFormat;
import com.tcdng.unify.core.chart.BarChart;
import com.tcdng.unify.core.chart.ChartGenerator;
import com.tcdng.unify.core.constant.ColorPalette;

/**
 * Abstract base class for bar chart providers.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractBarChartProvider extends AbstractQuickCategoryVisualProvider {

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

    public AbstractBarChartProvider(int width, int height) {
        this(ColorPalette.DEFAULT, AnnotationType.LABEL_VALUE, ValueFormat.COUNT, width, height, true, true, false);
    }

    public AbstractBarChartProvider(ColorPalette colorPalette, AnnotationType annotationType, ValueFormat valueFormat,
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
    protected byte[] doProvidePresentation(QuickCategory quickCategory) throws UnifyException {
        if (quickCategory.isSanityCheck()) {
            BarChart.Builder bcb =
                    BarChart.newBuilder(width, height).colorPalette(colorPalette).annotationType(annotationType)
                            .valueFormat(valueFormat).showLegend(showLegend).showXAxisTicks(showXAxisTicks)
                            .showYAxisTicks(showYAxisTicks);
            for (QuickCategory.Category category : quickCategory.getCategoryList()) {
                bcb.addSeries(category.getName(), quickCategory.getXValueList(), category.getYValueList(),
                        category.getColor());
            }

            BarChart chart = bcb.build();
            return chartGenerator.generateImage(chart);
        }

        return null;
    }

}
