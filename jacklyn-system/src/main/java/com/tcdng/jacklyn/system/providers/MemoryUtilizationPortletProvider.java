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

package com.tcdng.jacklyn.system.providers;

import com.tcdng.jacklyn.statistics.data.QuickPercentage;
import com.tcdng.jacklyn.statistics.providers.AbstractSimpleDialUtilizationProvider;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Memory utilization portlet provider.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("memoryutilization-portletprovider")
public class MemoryUtilizationPortletProvider extends AbstractSimpleDialUtilizationProvider {

    private static final int IMAGE_WIDTH = 420;
    private static final int IMAGE_HEIGHT = 240;
    
    public MemoryUtilizationPortletProvider() {
        super("Memory Utilization", IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    protected QuickPercentage doProvidePercentage(Object... params) throws UnifyException {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long usedMemory = totalMemory - runtime.freeMemory();
        return new QuickPercentage(usedMemory, totalMemory);
    }


}
