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
package com.tcdng.jacklyn.workflow.web.widgets;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.ui.Control;
import com.tcdng.unify.web.ui.EventHandler;
import com.tcdng.unify.web.ui.ResponseWriter;
import com.tcdng.unify.web.ui.Widget;
import com.tcdng.unify.web.ui.writer.AbstractControlWriter;

/**
 * Workflow step action button writer.
 *
 * @author Lateef Ojulari
 * @since 1.0
 */
@Writes(WfActionButtons.class)
@Component("wfactionbuttons-writer")
public class WfActionButtonsWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget component) throws UnifyException {
        WfActionButtons wfActionButtons = (WfActionButtons) component;
        writer.write("<div ");
        writeTagAttributes(writer, wfActionButtons);
        writer.write(">");

        boolean verticalLayout = wfActionButtons.isVerticalLayout();
        if (!verticalLayout) {
            writer.write("<div class=\"buttonRow\">");
        }

        Control actionCtrl = wfActionButtons.getActionCtrl();
        for (ValueStore valueStore : wfActionButtons.getValueList()) {
            if (verticalLayout) {
                writer.write("<div class=\"buttonRow\">");
            }

            writer.write("<div class=\"buttonCell\">");
            actionCtrl.setValueStore(valueStore);
            writer.writeStructureAndContent(actionCtrl);
            writer.write("</div>");

            if (verticalLayout) {
                writer.write("</div>");
            }
        }

        if (!verticalLayout) {
            writer.write("</div>");
        }

        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget) throws UnifyException {
        WfActionButtons wfActionButtons = (WfActionButtons) widget;
        // All behavior should be tied to action control
        EventHandler[] eventHandlers = wfActionButtons.getUplAttribute(EventHandler[].class, "eventHandler");
        if (eventHandlers != null) {
            Control actionCtrl = wfActionButtons.getActionCtrl();
            for (ValueStore valueStore : wfActionButtons.getValueList()) {
                actionCtrl.setValueStore(valueStore);
                getRequestContext().setQuickReference(valueStore);
                for (EventHandler eventHandler : eventHandlers) {
                    writer.writeBehavior(eventHandler, actionCtrl.getId());
                }
            }
        }
    }
}
