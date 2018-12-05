/*
 * Copyright 2018 The Code Department
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

import com.tcdng.jacklyn.shared.workflow.WorkflowFormElementType;
import com.tcdng.jacklyn.workflow.data.WfFormPrivilegeDef;
import com.tcdng.jacklyn.workflow.data.WfStepDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.constant.TriState;
import com.tcdng.unify.web.ui.Control;
import com.tcdng.unify.web.ui.Widget;
import com.tcdng.unify.web.ui.container.Form;

/**
 * Base workflow item viewer panel.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("ui-wfsingleformdocviewer")
@UplBinding("web/workflow/upl/wfsingleformdocviewer.upl")
public class WfSingleFormDocViewer extends AbstractWfDocViewer {

	@Override
	public void setDocumentMode(WfStepDef wfStepDef) throws UnifyException {
		Form form = (Form) this.getWidgetByShortName("formPanel.form");
		form.reset();
		for (WfFormPrivilegeDef wfFormPrivilegeDef : wfStepDef.getFormPrivilegeList()) {
			if (WorkflowFormElementType.SECTION.equals(wfFormPrivilegeDef.getType())) {
				form.setSectionState(wfFormPrivilegeDef.getName(), wfFormPrivilegeDef.isVisible(),
						wfFormPrivilegeDef.isEditable(), wfFormPrivilegeDef.isDisabled());
			} else if (WorkflowFormElementType.FIELD.equals(wfFormPrivilegeDef.getType())) {
				Widget widget = getWidgetByShortName(wfFormPrivilegeDef.getName());
				widget.setVisible(wfFormPrivilegeDef.isVisible());
				widget.setEditable(wfFormPrivilegeDef.isEditable());
				widget.setDisabled(wfFormPrivilegeDef.isDisabled());
				if (widget instanceof Control) {
					((Control) widget)
							.setRequired(TriState.getTriState(wfFormPrivilegeDef.isRequired()));
				}
			}
		}
	}

}
