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

import com.tcdng.jacklyn.common.web.widgets.BaseDialogPanel;
import com.tcdng.jacklyn.workflow.data.CommentsInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Workflow item comments panel.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("ui-wfitemcommentspanel")
@UplBinding("web/workflow/upl/wfitemcommentspanel.upl")
public class WfItemCommentsPanel extends BaseDialogPanel {

    private boolean addComments;

    @Action
    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        CommentsInfo commentsInfo = getValue(CommentsInfo.class);
        setVisible("frmComments", !commentsInfo.isRequired());
        setVisible("frmCommentsReq", commentsInfo.isRequired());

        setVisible("addCommentsPanel", addComments);
        setVisible("saveBtn", addComments);
        setVisible("cancelBtn", addComments);
        setVisible("closeBtn", !addComments);
    }

    public boolean isAddComments() {
        return addComments;
    }

    public void setAddComments(boolean addComments) {
        this.addComments = addComments;
    }
}
