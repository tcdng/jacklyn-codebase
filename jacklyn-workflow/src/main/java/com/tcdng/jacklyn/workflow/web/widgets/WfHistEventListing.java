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
package com.tcdng.jacklyn.workflow.web.widgets;

import java.util.List;

import com.tcdng.jacklyn.workflow.data.WfItemHistEvent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.format.Formatter;
import com.tcdng.unify.web.ui.AbstractWidget;

/**
 * Workflow history event listing.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("ui-wfhisteventlisting")
@UplAttributes({ @UplAttribute(name = "timeFormatter", type = Formatter.class,
        defaultVal = "$d{!datetimeformat style:long}") })
public class WfHistEventListing extends AbstractWidget {

    @SuppressWarnings("unchecked")
    public List<WfItemHistEvent> getWorkflowItemHistEventList() throws UnifyException {
        return (List<WfItemHistEvent>) this.getValue();
    }

    public Formatter<?> getFormatter() throws UnifyException {
        return this.getUplAttribute(Formatter.class, "timeFormatter");
    }
}
