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

package com.tcdng.jacklyn.workflow.widgets;

import com.tcdng.jacklyn.workflow.data.WfFormDef;
import com.tcdng.jacklyn.workflow.data.WfFormFieldDef;
import com.tcdng.jacklyn.workflow.data.WfFormSectionDef;
import com.tcdng.jacklyn.workflow.data.WfFormTabDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Single form document viewer UPL generator.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("wfsingleformdocviewer-generator")
public class WfSingleFormDocViewerUplGenerator extends AbstractWorkflowUplGenerator {

    public WfSingleFormDocViewerUplGenerator() {
        super("ui-wfsingleformdocviewer");
    }

    @Override
    protected void doGenerateBody(StringBuilder sb, WfFormDef wfFormDef) throws UnifyException {
        // Generate viewer attributes
        sb.append("formColumns:2"); // TODO fetch form definition
        appendNewline(sb);
        
        // Generate form sections and widgets
        StringBuilder fsb = new StringBuilder();
        boolean appendFocus = true;
        for (WfFormTabDef wfFormTabDef : wfFormDef.getTabList()) {
            for (WfFormSectionDef wfFormSectionDef : wfFormTabDef.getSectionList()) {
                sb.append("formSection:$d{!ui-section:").append(wfFormSectionDef.getName()).append(" caption:$s{");
                appendLabel(sb, wfFormSectionDef);
                sb.append("} components:$c{");
                boolean appendSym = false;
                for (WfFormFieldDef wfFormFieldDef : wfFormSectionDef.getFieldList()) {
                    if (appendSym) {
                        sb.append(' ');
                    } else {
                        appendSym = true;
                    }

                    // Form field
                    String editorUpl = wfFormFieldDef.getEditorUpl();
                    String secFieldName = wfFormFieldDef.getName();
                    boolean isPicture = editorUpl.startsWith("!ui-picture");
                    if (isPicture) {
                        // Always wrap picture field in panel
                        secFieldName = secFieldName + "Panel";
                        fsb.append("!ui-panel:");
                        fsb.append(secFieldName);
                        fsb.append(" caption:$s{");
                        appendLabel(fsb, wfFormFieldDef);
                        fsb.append("} components:$c{");
                        fsb.append(wfFormFieldDef.getName());
                        fsb.append("}");
                        appendNewline(fsb);
                    }
                    
                    sb.append(secFieldName);

                    int spaceIndex = editorUpl.indexOf(' ');
                    if (spaceIndex > 0) {
                        fsb.append(editorUpl.substring(0, spaceIndex));
                        fsb.append(':').append(wfFormFieldDef.getName());
                        fsb.append(editorUpl.substring(spaceIndex));
                    } else {
                        fsb.append(editorUpl);
                        fsb.append(':').append(wfFormFieldDef.getName());
                    }
                    fsb.append(" caption:$s{");
                    appendLabel(fsb, wfFormFieldDef);
                    fsb.append('}');
                    fsb.append(" binding:").append(wfFormFieldDef.getBinding());
                    if (wfFormFieldDef.isRequired()) {
                        fsb.append(" required:true");
                    }
                    
                    if (appendFocus) {
                        fsb.append(" focus:true");
                        appendFocus = false;
                    }
                    
                    appendNewline(fsb);
                }
                sb.append("}");
                if (wfFormSectionDef.isBinding()) {
                    sb.append(" binding:").append(wfFormSectionDef.getBinding());
                }
                sb.append("}");
                appendNewline(sb);
            }
        }

        // Compose
        sb.append(fsb);
    }

}
