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

import com.tcdng.jacklyn.workflow.business.WorkflowModule;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.jacklyn.workflow.data.WfItemAttachmentInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.web.ui.control.AbstractFileAttachmentHandler;
import com.tcdng.unify.web.ui.data.FileAttachmentInfo;

/**
 * Default workflow item file attachment handler.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component(WorkflowModuleNameConstants.DEFAULTWORKFLOWITEMATTACHMENTHANDLER)
public class WfItemFileAttachmentHandler extends AbstractFileAttachmentHandler {

    @Configurable(WorkflowModuleNameConstants.WORKFLOWBUSINESSMODULE)
    private WorkflowModule workflowModule;

    @Override
    public void handleAttach(Object parentId, FileAttachmentInfo fileAttachmentInfo) throws UnifyException {
        WfItemAttachmentInfo wfItemAttachment = new WfItemAttachmentInfo(fileAttachmentInfo.getName(), null,
                fileAttachmentInfo.getFilename(), fileAttachmentInfo.getType(), fileAttachmentInfo.getAttachment());
        workflowModule.attachToWorkflowItem((Long) parentId, wfItemAttachment);
    }

    @Override
    public FileAttachmentInfo handleView(Object parentId, FileAttachmentInfo fileAttachmentInfo) throws UnifyException {
        WfItemAttachmentInfo wfItemAttachment = workflowModule.fetchWorkflowItemAttachment((Long) parentId,
                fileAttachmentInfo.getName());
        FileAttachmentInfo retAttachmentInfo = new FileAttachmentInfo(wfItemAttachment.getName(),
                resolveSessionMessage(wfItemAttachment.getLabel()), wfItemAttachment.getType());
        retAttachmentInfo.setFilename(wfItemAttachment.getFilename());
        retAttachmentInfo.setAttachment(wfItemAttachment.getData());
        return retAttachmentInfo;
    }

    @Override
    public void handleDetach(Object parentId, FileAttachmentInfo fileAttachmentInfo) throws UnifyException {
        workflowModule.deleteWorkflowItemAttachment((Long) parentId, fileAttachmentInfo.getName());
    }

}
