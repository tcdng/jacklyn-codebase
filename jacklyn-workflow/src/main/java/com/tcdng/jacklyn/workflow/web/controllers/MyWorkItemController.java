/*
 * Copyright 2018-2020 The Code Department
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

import java.util.ArrayList;
import java.util.List;

import com.tcdng.jacklyn.workflow.data.FlowingWfItem;
import com.tcdng.jacklyn.workflow.data.WfAction;
import com.tcdng.jacklyn.workflow.data.WfItemAttachmentInfo;
import com.tcdng.jacklyn.workflow.data.WfItemHistory;
import com.tcdng.jacklyn.workflow.web.beans.MyWorkItemPageBean;
import com.tcdng.jacklyn.workflow.web.widgets.WfItemCommentsPanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.ui.data.FileAttachmentInfo;
import com.tcdng.unify.web.ui.data.FileAttachmentsInfo;

/**
 * Controller for managing my work item.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/workflow/myworkflowitem")
@UplBinding("web/workflow/upl/myworkflowitem.upl")
@ResultMappings({
    @ResultMapping(
            name = "showattachments",
            response = { "!validationerrorresponse", "!showpopupresponse popup:$s{attachmentsPopup}" }),
    @ResultMapping(
            name = "showcomments",
            response = { "!validationerrorresponse", "!showpopupresponse popup:$s{commentsPopup}" }) })
public class MyWorkItemController extends AbstractWorkflowPageController<MyWorkItemPageBean> {

    public MyWorkItemController() {
        super(MyWorkItemPageBean.class, true, false, false);
    }

    @Action
    public String applyActionToWorkflowItem() throws UnifyException {
        MyWorkItemPageBean pageBean = getPageBean();
        String actionName = getRequestTarget(String.class);
        pageBean.setActionName(actionName);
        pageBean.getCommentsInfo().setComment(null);

        // Check for comments
        for (WfAction wfAction : pageBean.getWorkflowItem().getActionList()) {
            if (actionName.equals(wfAction.getName())) {
                RequirementType commentsReq = wfAction.getCommentReqType();
                if (commentsReq == null || RequirementType.NONE.equals(commentsReq)) {
                    break;
                }

                pageBean.getCommentsInfo().setRequired(RequirementType.MANDATORY.equals(commentsReq));
                pageBean.getCommentsInfo()
                        .setApplyActionCaption(getSessionMessage("button.addcommentsapplyaction", wfAction.getLabel()));
                return showComments(true);
            }
        }

        return internalApplyActionToWorkflowItem();
    }

    @Action
    public String applyActionWithComments() throws UnifyException {
        MyWorkItemPageBean pageBean = getPageBean();
        pageBean.getWorkflowItem().setComment(pageBean.getCommentsInfo().getComment());
        return internalApplyActionToWorkflowItem();
    }

    @Action
    public String showWorkflowItemAttachments() throws UnifyException {
        MyWorkItemPageBean pageBean = getPageBean();
        Long wfItemId = pageBean.getWorkflowItem().getWfItemId();
        List<WfItemAttachmentInfo> wfAttachmentList = getWorkflowService().fetchWorkflowItemAttachments(wfItemId, true);
        List<FileAttachmentInfo> filaAttachmentInfoList = new ArrayList<FileAttachmentInfo>();
        for (WfItemAttachmentInfo workflowItemAttachment : wfAttachmentList) {
            FileAttachmentInfo fileAttachmentInfo =
                    new FileAttachmentInfo(workflowItemAttachment.getName(), workflowItemAttachment.getLabel(),
                            workflowItemAttachment.getType());
            fileAttachmentInfo.setFilename(workflowItemAttachment.getFilename());
            filaAttachmentInfoList.add(fileAttachmentInfo);
        }

        FileAttachmentsInfo fileAttachmentsInfo = new FileAttachmentsInfo(wfItemId, filaAttachmentInfoList);
        pageBean.setFileAttachmentsInfo(fileAttachmentsInfo);
        return "showattachments";
    }

    @Action
    public String showWorkflowItemComments() throws UnifyException {
        return showComments(false);
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        MyWorkItemPageBean pageBean = getPageBean();
        Long wfItemId = getRequestTarget(Long.class);
        if(QueryUtils.isValidLongCriteria(wfItemId)) {
            FlowingWfItem workflowItem = getWorkflowService().findWorkflowItem(wfItemId);
            workflowItem.setActionList(
                    getWorkflowService().getWorkflowStepActions(workflowItem.getWfStepDef().getGlobalName()));
            pageBean.setWorkflowItem(workflowItem);
        }        
    }

    @Override
    protected void onClosePage() throws UnifyException {
        super.onClosePage();
        MyWorkItemPageBean pageBean = getPageBean();
        pageBean.setWorkflowItem(null);
    }

    private String showComments(boolean isAddComment) throws UnifyException {
        MyWorkItemPageBean pageBean = getPageBean();
        WfItemHistory wih =
                getWorkflowService().findWorkflowItemHistory(pageBean.getWorkflowItem().getWfItemHistId(), true);
        pageBean.getCommentsInfo().setCommentsHistEventList(wih.getEventList());
        WfItemCommentsPanel wfItemCommentsPanel = getPageWidgetByShortName(WfItemCommentsPanel.class, "commentsPopup");
        wfItemCommentsPanel.setAddComments(isAddComment);
        return "showcomments";
    }

    private String internalApplyActionToWorkflowItem() throws UnifyException {
        MyWorkItemPageBean pageBean = getPageBean();
        getWorkflowService().applyWorkflowAction(pageBean.getWorkflowItem(), pageBean.getActionName());
        hintUser("$m{hint.workflow.myworkitem.applyaction.success}");
        return closePage();
    }

}
