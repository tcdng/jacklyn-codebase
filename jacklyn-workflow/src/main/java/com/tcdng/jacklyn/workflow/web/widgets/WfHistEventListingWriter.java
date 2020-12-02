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
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.format.Formatter;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractWidgetWriter;

/**
 * Workflow history event listing writer.
 *
 * @author Lateef Ojulari
 * @since 1.0
 */
@Writes(WfHistEventListing.class)
@Component("wfhisteventlisting-writer")
public class WfHistEventListingWriter extends AbstractWidgetWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        WfHistEventListing wfHistEventListing = (WfHistEventListing) widget;
        writer.write("<div ");
        writeTagAttributes(writer, wfHistEventListing);
        writer.write("><div class=\"wwtable\">");

        List<WfItemHistEvent> listing = wfHistEventListing.getWorkflowItemHistEventList();
        if (listing != null) {
            Formatter<?> formatter = wfHistEventListing.getFormatter();

            for (int i = 0; i < listing.size(); i++) {
                // Event row
                writer.write("<div class=\"");
                if (i % 2 == 0) {
                    writer.write("wwodd");
                } else {
                    writer.write("wweven");
                }
                writer.write("\">");
                writer.write("<div class=\"wwserialno\">").write(i + 1).write(".</div>"); // Localization?

                // Event
                writer.write("<div class=\"wwbody\">");
                writer.write("<div style=\"display:table;width:100%;\">");
                WfItemHistEvent workflowItemHistEvent = listing.get(i);
                writeEventAttributeRow(writer, "wwcontent", workflowItemHistEvent.getComments());
                writeEventAttributeRow(writer, "wwlabel", getSessionMessage("wfhisteventlisting.user.action",
                        workflowItemHistEvent.getActor(), workflowItemHistEvent.getWfActionDesc()));
                writeEventAttributeRow(writer, "wwlabel",
                        DataUtils.convert(String.class, workflowItemHistEvent.getActionDt(), formatter));
                writer.write("</div>");
                writer.write("</div>");
                // End event

                // End event row
                writer.write("</div>");
            }
        }

        writer.write("</div></div>");
    }

    private void writeEventAttributeRow(ResponseWriter writer, String styleClass, String content)
            throws UnifyException {
        writer.write("<div style=\"display:table-row;\">");
        writer.write("<div class=\"").write(styleClass).write("\"><span>");
        if (content != null) {
            writer.writeWithHtmlEscape(content);
        }
        writer.write("</span></div>");
        writer.write("</div>");
    }

}
