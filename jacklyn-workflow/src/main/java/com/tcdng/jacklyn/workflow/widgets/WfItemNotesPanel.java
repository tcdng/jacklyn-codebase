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

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.panel.AbstractDialogPanel;

/**
 * Workflow item notes panel.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("ui-wfitemnotespanel")
@UplBinding("web/workflow/upl/wfitemnotespanel.upl")
public class WfItemNotesPanel extends AbstractDialogPanel {

	private boolean addNotes;

	@Action
	@Override
	public void switchState() throws UnifyException {
		super.switchState();

		NotesInfo notesInfo = getValue(NotesInfo.class);
		setVisible("frmNotes", !notesInfo.isRequired());
		setVisible("frmNotesReq", notesInfo.isRequired());

		setVisible("addNotesPanel", addNotes);
		setVisible("saveBtn", addNotes);
		setVisible("cancelBtn", addNotes);
		setVisible("closeBtn", !addNotes);
	}

	public boolean isAddNotes() {
		return addNotes;
	}

	public void setAddNotes(boolean addNotes) {
		this.addNotes = addNotes;
	}
}
