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

import com.tcdng.jacklyn.shared.workflow.WorkflowFormElementType;
import com.tcdng.jacklyn.workflow.data.ViewableWfItem;
import com.tcdng.jacklyn.workflow.data.WfFormPrivilegeDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.constant.TriState;
import com.tcdng.unify.web.ui.Control;
import com.tcdng.unify.web.ui.Widget;
import com.tcdng.unify.web.ui.container.Form;

/**
 * Single form workflow item viewer panel.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("ui-wfsingleformviewer")
@UplBinding("web/workflow/upl/wfsingleformviewer.upl")
public class WfSingleFormViewer extends AbstractWfItemViewer {

    @Override
    public void setDocumentMode(ViewableWfItem viewableWfItem) throws UnifyException {
        Form form = (Form) getWidgetByShortName("formPanel.form");
        form.reset();
        String docName = viewableWfItem.getDocName();
        for (WfFormPrivilegeDef wfFormPrivilegeDef : viewableWfItem.getWfStepDef().getFormPrivilegeList()) {
            if (wfFormPrivilegeDef.getDocName().equals(docName)) {
                if (WorkflowFormElementType.SECTION.equals(wfFormPrivilegeDef.getType())) {
                    form.setSectionState(wfFormPrivilegeDef.getName(), wfFormPrivilegeDef.isVisible(),
                            wfFormPrivilegeDef.isEditable(), wfFormPrivilegeDef.isDisabled());
                } else if (WorkflowFormElementType.FIELD.equals(wfFormPrivilegeDef.getType())) {
                    Widget widget = getWidgetByShortName(wfFormPrivilegeDef.getName());
                    widget.setVisible(wfFormPrivilegeDef.isVisible());
                    widget.setEditable(wfFormPrivilegeDef.isEditable());
                    widget.setDisabled(wfFormPrivilegeDef.isDisabled());
                    if (widget instanceof Control) {
                        ((Control) widget).setRequired(TriState.getTriState(wfFormPrivilegeDef.isRequired()));
                    }
                }
            }
        }
    }

}
