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

import com.tcdng.jacklyn.statistics.data.QuickRatio;
import com.tcdng.jacklyn.statistics.data.QuickRatioVisual;
import com.tcdng.unify.core.UnifyException;

/**
 * Abstract base class for visual quick ratio providers.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractQuickRatioVisualProvider extends AbstractStatisticsProvider<QuickRatioVisual>
        implements QuickRatioVisualProvider {

    @Override
    public QuickRatioVisual provide(Object... params) throws UnifyException {
        QuickRatio quickRatio = doProvideRatio(params);
        return new QuickRatioVisual(quickRatio, doProvidePresentation(quickRatio));
    }

    protected abstract QuickRatio doProvideRatio(Object... params) throws UnifyException;

    protected abstract byte[] doProvidePresentation(QuickRatio quickRatio) throws UnifyException;

}
