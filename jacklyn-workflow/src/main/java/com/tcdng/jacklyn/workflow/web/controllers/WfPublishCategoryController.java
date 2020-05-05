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

package com.tcdng.jacklyn.workflow.web.controllers;

import com.tcdng.jacklyn.shared.workflow.WorkflowCategoryBinaryPublicationTaskConstants;
import com.tcdng.jacklyn.workflow.web.beans.WfPublishCategoryPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Publish workflow category controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/workflow/wfpublishcategory")
@UplBinding("web/workflow/upl/wfpublishcategory.upl")
public class WfPublishCategoryController extends AbstractWorkflowPageController<WfPublishCategoryPageBean> {

    public WfPublishCategoryController() {
        super(WfPublishCategoryPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String startWfPublishCategoryTask() throws UnifyException {
        WfPublishCategoryPageBean pageBean = getPageBean();
        TaskSetup taskSetup =
                TaskSetup.newBuilder().addTask(WorkflowCategoryBinaryPublicationTaskConstants.TASK_NAME)
                        .setParam(WorkflowCategoryBinaryPublicationTaskConstants.WFCATEGORY_BIN,
                                pageBean.getWfCategoryBin())
                        .setParam(WorkflowCategoryBinaryPublicationTaskConstants.WFCATEGORY_ACTIVATE,
                                pageBean.isActivate())
                        .logMessages().build();
        return launchTaskWithMonitorBox(taskSetup, "$m{workflow.wfcategory.publish}");
    }
}
