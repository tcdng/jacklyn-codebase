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

package com.tcdng.jacklyn.system.web.widgets;

import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.annotation.UplBinding;

/**
 * Memory utilization portlet panel.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component(name = "ui-memoryutilizationportlet", description = "$m{system.memoryutilization.portlet}")
@UplBinding("web/system/upl/memoryutilizationportletpanel.upl")
@UplAttributes({
        @UplAttribute(name = "provider", type = String.class, defaultVal = "memoryutilization-portletprovider") })
public class MemoryUtilizationPortletPanel extends AbstractSimpleDialPortletPanel {

}
