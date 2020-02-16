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

import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.chart.AbstractChart.AnnotationType;
import com.tcdng.unify.core.chart.AbstractChart.ValueFormat;
import com.tcdng.unify.core.chart.ChartGenerator;
import com.tcdng.unify.core.constant.ColorPalette;

/**
 * Abstract base class for XY chart providers.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractXYChartProvider extends AbstractQuickXYVisualProvider {

    @Configurable
    private ChartGenerator chartGenerator;

    protected final ColorPalette colorPalette;

    protected final AnnotationType annotationType;

    protected final ValueFormat valueFormat;

    protected final int width;

    protected final int height;

    protected final boolean showLegend;

    protected final boolean showXAxisTicks;

    protected final boolean showYAxisTicks;

    public AbstractXYChartProvider(int width, int height) {
        this(ColorPalette.DEFAULT, AnnotationType.LABEL_VALUE, ValueFormat.COUNT, width, height, true, true, false);
    }

    public AbstractXYChartProvider(ColorPalette colorPalette, AnnotationType annotationType, ValueFormat valueFormat,
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

    protected ChartGenerator getChartGenerator() {
        return chartGenerator;
    }

}
