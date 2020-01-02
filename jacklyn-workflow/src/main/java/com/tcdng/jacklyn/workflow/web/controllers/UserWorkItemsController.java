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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.tcdng.jacklyn.shared.workflow.WorkflowApplyActionTaskConstants;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem;
import com.tcdng.jacklyn.workflow.data.InteractWfItems;
import com.tcdng.jacklyn.workflow.data.WfAction;
import com.tcdng.jacklyn.workflow.data.WfItemAttachmentInfo;
import com.tcdng.jacklyn.workflow.data.WfItemHistory;
import com.tcdng.jacklyn.workflow.web.beans.UserWorkItemsPageBean;
import com.tcdng.jacklyn.workflow.web.widgets.UserWorkItemsPage;
import com.tcdng.jacklyn.workflow.web.widgets.WfItemCommentsPanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.ui.control.Table;
import com.tcdng.unify.web.ui.data.FileAttachmentInfo;
import com.tcdng.unify.web.ui.data.FileAttachmentsInfo;

/**
 * Controller for changing user password.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/workflow/userworkflowitems")
@UplBinding("web/workflow/upl/userworkflowitems.upl")
@ResultMappings({
        @ResultMapping(name = "refreshsummary", response = { "!refreshpanelresponse panels:$l{wfItemsMainPanel}" }),
        @ResultMapping(
                name = "refreshitems",
                response = { "!refreshpanelresponse panels:$l{wfItemsPanel actionOnMultiplePanel}" }),
        @ResultMapping(
                name = "switchdocviewer",
                response = { "!hidepopupresponse",
                        "!switchpanelresponse panels:$l{userWorkItemsBodyPanel.wfItemViewerPanel}" }),
        @ResultMapping(
                name = "switchsearchitems",
                response = { "!hidepopupresponse",
                        "!switchpanelresponse panels:$l{userWorkItemsBodyPanel.wfItemsMainPanel}" }),
        @ResultMapping(
                name = "showattachments",
                response = { "!validationerrorresponse", "!showpopupresponse popup:$s{attachmentsPopup}" }),
        @ResultMapping(
                name = "showcomments",
                response = { "!validationerrorresponse", "!showpopupresponse popup:$s{commentsPopup}" }) })
public class UserWorkItemsController extends AbstractWorkflowPageController<UserWorkItemsPageBean> {

    public UserWorkItemsController() {
        super(UserWorkItemsPageBean.class, true, false, false);
    }

    @Action
    public String refreshWorkflowItemSummary() throws UnifyException {
        refreshWorkflowItems();
        return "refreshsummary";
    }

    @Action
    public String refreshWorkflowItems() throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
        if (QueryUtils.isValidStringCriteria(pageBean.getWfStepName())) {
            InteractWfItems csWorkItems = getWorkflowService().getCurrentUserWorkItems(pageBean.getWfStepName());
            pageBean.setCsWorkItems(csWorkItems);
            ((UserWorkItemsPage) getPage()).setPageValidationActions(csWorkItems.getValidatePageActions());
        } else {
            pageBean.setCsWorkItems(null);
            ((UserWorkItemsPage) getPage()).setPageValidationActions(null);
        }

        manageDisplay();
        return "refreshitems";
    }

    @Action
    public String applyActionToMultipleWorkflowItems() throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
        String actionName = getRequestTarget(String.class);
        pageBean.setActionName(actionName);
        List<Long> wfItemIds = getSelectedIds();
        TaskSetup taskSetup =
                TaskSetup.newBuilder().addTask(WorkflowApplyActionTaskConstants.TASK_NAME)
                        .setParam(WorkflowApplyActionTaskConstants.WFACTION_NAME, actionName)
                        .setParam(WorkflowApplyActionTaskConstants.WFITEMS_IDLIST, wfItemIds).logMessages().build();
        String donePath = getName() + "/refreshWorkflowItemSummary";
        return launchTaskWithMonitorBox(taskSetup, "$m{workflow.applyactiontomultiple.execution}", donePath, donePath);
    }

    @Action
    public String applyActionToWorkflowItem() throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
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
        UserWorkItemsPageBean pageBean = getPageBean();
        pageBean.getWorkflowItem().setComment(pageBean.getCommentsInfo().getComment());
        return internalApplyActionToWorkflowItem();
    }

    @Action
    public String prepareViewWorkflowItems() throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
        pageBean.setWfItemIds(getSelectedIds());
        pageBean.setViewIndex(0);
        return prepareWorkflowItemForView();
    }

    @Action
    public String fetchWorkflowItems() throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
        getWorkflowService().grabCurrentUserWorkItems(pageBean.getWfStepName());
        return refreshWorkflowItemSummary();
    }

    @Action
    public String releaseWorkflowItems() throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
        List<Long> wfItemIds = getSelectedIds();
        getWorkflowService().releaseCurrentUserWorkItems(pageBean.getWfStepName(), wfItemIds);
        return refreshWorkflowItemSummary();
    }

    @Action
    public String showWorkflowItemAttachments() throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
        Long wfItemId = pageBean.getWfItemIds().get(pageBean.getViewIndex());
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

    @Action
    public String closeViewWorkflowItems() throws UnifyException {
        refreshWorkflowItemSummary();

        UserWorkItemsPageBean pageBean = getPageBean();
        pageBean.setWfItemIds(null);
        pageBean.setViewIndex(0);
        pageBean.setWorkflowItem(null);
        pageBean.setWorkingCache(null);
        return "switchsearchitems";
    }

    @Action
    public String firstRecord() throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
        pageBean.setViewIndex(0);
        return prepareWorkflowItemForView();
    }

    @Action
    public String previousRecord() throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
        pageBean.setViewIndex(pageBean.getViewIndex() - 1);
        return prepareWorkflowItemForView();
    }

    @Action
    public String nextRecord() throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
        pageBean.setViewIndex(pageBean.getViewIndex() + 1);
        return prepareWorkflowItemForView();
    }

    @Action
    public String lastRecord() throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
        pageBean.setViewIndex(pageBean.getWfItemIds().size() - 1);
        return prepareWorkflowItemForView();
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        manageDisplay();
    }

    private String showComments(boolean isAddComment) throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
        WfItemHistory wih =
                getWorkflowService().findWorkflowItemHistory(pageBean.getWorkflowItem().getWfItemHistId(), true);
        pageBean.getCommentsInfo().setCommentsHistEventList(wih.getEventList());
        getWfItemCommentsPanel().setAddComments(isAddComment);
        return "showcomments";
    }

    private String prepareWorkflowItemForView() throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
        // Load workflow item
        FlowingWfItem workflowItem = null;
        Long wfItemId = pageBean.getWfItemIds().get(pageBean.getViewIndex());
        if (pageBean.getWorkingCache() != null && pageBean.getWorkingCache().containsKey(wfItemId)) {
            workflowItem = pageBean.getWorkingCache().get(wfItemId);
        } else {
            workflowItem = getWorkflowService().findWorkflowItem(wfItemId);
            workflowItem.setActionList(pageBean.getCsWorkItems().getActionList());

            if (pageBean.getWorkingCache() == null) {
                pageBean.setWorkingCache(new HashMap<Long, FlowingWfItem>());
            }

            pageBean.getWorkingCache().put(wfItemId, workflowItem);
        }

        workflowItem.setLabel(
                getSessionMessage("label.itemcount", pageBean.getViewIndex() + 1, pageBean.getWfItemIds().size()));
        pageBean.setWorkflowItem(workflowItem);
        return "switchdocviewer";
    }

    private String internalApplyActionToWorkflowItem() throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
        getWorkflowService().applyWorkflowAction(pageBean.getWorkflowItem(), pageBean.getActionName());
        hintUser("$m{hint.workflow.userworkitems.applyaction.success}");
        Long wfItemId = pageBean.getWfItemIds().remove(pageBean.getViewIndex());
        pageBean.getWorkingCache().remove(wfItemId);

        if (!pageBean.getWfItemIds().isEmpty()) {
            if (pageBean.getViewIndex() >= pageBean.getWfItemIds().size()) {
                pageBean.setViewIndex(pageBean.getWfItemIds().size() - 1);
            }
            return prepareWorkflowItemForView();
        }

        return closeViewWorkflowItems();
    }

    private List<Long> getSelectedIds() throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
        if (getTable().getSelectedRows() > 0) {
            Integer[] selectedIndexes = getTable().getSelectedRowIndexes();
            List<Long> selectedIds = new ArrayList<Long>();
            for (int i = 0; i < selectedIndexes.length; i++) {
                selectedIds.add(pageBean.getCsWorkItems().getWfItemList().get(selectedIndexes[i]).getId());
            }
            return selectedIds;
        }
        return Collections.emptyList();
    }

    private void manageDisplay() throws UnifyException {
        UserWorkItemsPageBean pageBean = getPageBean();
        boolean isItems = pageBean.getCsWorkItems() != null && pageBean.getCsWorkItems().isActionListItems();
        setPageWidgetVisible("wfActionButtonsOnMultiple", isItems);
        setPageWidgetDisabled("fetchItemsBtn", StringUtils.isBlank(pageBean.getWfStepName()));
        setPageWidgetDisabled("releaseItemsBtn", !isItems);
        setPageWidgetDisabled("viewItemsBtn", !isItems);
    }

    private Table getTable() throws UnifyException {
        return getPageWidgetByShortName(Table.class, "wfItemsTbl");
    }

    private WfItemCommentsPanel getWfItemCommentsPanel() throws UnifyException {
        return getPageWidgetByShortName(WfItemCommentsPanel.class, "commentsPopup");
    }
}
