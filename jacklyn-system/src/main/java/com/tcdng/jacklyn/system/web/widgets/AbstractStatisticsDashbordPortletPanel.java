/*
 * Copyright 2018-2020 The Code Department.
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

import com.tcdng.jacklyn.statistics.providers.StatisticsProvider;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.web.annotation.Action;

/**
 * Abstract base class for statistics dashboard portlet panels.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@UplAttributes({ @UplAttribute(name = "provider", type = String.class, mandatory = true) })
public abstract class AbstractStatisticsDashbordPortletPanel<T> extends AbstractDashboardPortletPanel {

    protected T statistics;

    @SuppressWarnings("unchecked")
    @Action
    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        StatisticsProvider<T> statisticsProvider =
                (StatisticsProvider<T>) getComponent(getUplAttribute(String.class, "provider"));
        statistics = statisticsProvider.provide();
    }

    public abstract T getStatistics();

}
