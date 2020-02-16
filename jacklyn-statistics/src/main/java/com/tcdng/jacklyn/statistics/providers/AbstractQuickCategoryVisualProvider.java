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
import com.tcdng.jacklyn.statistics.data.QuickCategoryVisual;
import com.tcdng.unify.core.UnifyException;

/**
 * Abstract base class for visual quick category providers.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractQuickCategoryVisualProvider extends AbstractStatisticsProvider<QuickCategoryVisual>
        implements QuickCategoryVisualProvider {

    @Override
    public QuickCategoryVisual provide(Object... params) throws UnifyException {
        QuickCategory quickCategory = doProvideCategory(params);
        return new QuickCategoryVisual(quickCategory, doProvidePresentation(quickCategory));
    }

    protected abstract QuickCategory doProvideCategory(Object... params) throws UnifyException;

    protected abstract byte[] doProvidePresentation(QuickCategory quickCategory) throws UnifyException;
}
