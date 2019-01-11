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
package com.tcdng.jacklyn.workflow.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcdng.jacklyn.shared.workflow.WorkflowApplyActionTaskConstants;
import com.tcdng.jacklyn.workflow.data.WfAction;
import com.tcdng.jacklyn.workflow.data.WfItemAttachmentInfo;
import com.tcdng.jacklyn.workflow.data.WfItemHistObject;
import com.tcdng.jacklyn.workflow.data.WfItemObject;
import com.tcdng.jacklyn.workflow.data.WfItemObjects;
import com.tcdng.jacklyn.workflow.widgets.NotesInfo;
import com.tcdng.jacklyn.workflow.widgets.UserWorkItemsPage;
import com.tcdng.jacklyn.workflow.widgets.WfItemNotesPanel;
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
        @ResultMapping(name = "refreshitems",
                response = { "!refreshpanelresponse panels:$l{wfItemsPanel actionOnMultiplePanel}" }),
        @ResultMapping(name = "switchdocviewer",
                response = { "!switchpanelresponse panels:$l{userWorkItemsBodyPanel.wfItemViewerPanel}" }),
        @ResultMapping(name = "switchsearchitems",
                response = { "!hidepopupresponse",
                        "!switchpanelresponse panels:$l{userWorkItemsBodyPanel.wfItemsMainPanel}" }),
        @ResultMapping(name = "showattachments",
                response = { "!validationerrorresponse", "!showpopupresponse popup:$s{attachmentsPopup}" }),
        @ResultMapping(name = "shownotes",
                response = { "!validationerrorresponse", "!showpopupresponse popup:$s{notesPopup}" }) })
public class UserWorkItemsController extends AbstractWorkflowController {

    private String wfStepName;

    private WfItemObjects csWorkItems;

    private WfItemObject workflowItem;

    private Table table;

    private List<Long> wfItemIds;

    private int viewIndex;

    private String actionName;

    private FileAttachmentsInfo fileAttachmentsInfo;

    private NotesInfo notesInfo;

    private Map<Long, WfItemObject> workingCache;

    private WfItemNotesPanel wfItemNotesPanel;

    public UserWorkItemsController() {
        super(true, false);
        notesInfo = new NotesInfo();
    }

    @Action
    public String refreshWorkflowItemSummary() throws UnifyException {
        refreshWorkflowItems();
        return "refreshsummary";
    }

    @Action
    public String refreshWorkflowItems() throws UnifyException {
        if (QueryUtils.isValidStringCriteria(wfStepName)) {
            csWorkItems = getWorkflowService().getCurrentUserWorkItems(wfStepName);
            ((UserWorkItemsPage) getPage()).setPageValidationActions(csWorkItems.getValidatePageActions());
        } else {
            csWorkItems = null;
            ((UserWorkItemsPage) getPage()).setPageValidationActions(null);
        }

        manageDisplay();
        return "refreshitems";
    }

    @Action
    public String applyActionToMultipleWorkflowItems() throws UnifyException {
        actionName = getRequestTarget(String.class);
        List<Long> wfItemIds = getSelectedIds();
        TaskSetup taskSetup = TaskSetup.newBuilder().addTask(WorkflowApplyActionTaskConstants.TASK_NAME)
                .setParam(WorkflowApplyActionTaskConstants.WFACTION_NAME, actionName)
                .setParam(WorkflowApplyActionTaskConstants.WFITEMS_IDLIST, wfItemIds).logMessages().build();
        String donePath = getName() + "/refreshWorkflowItemSummary";
        return launchTaskWithMonitorBox(taskSetup, "$m{workflow.applyactiontomultiple.execution}", donePath, donePath);
    }

    @Action
    public String applyActionToWorkflowItem() throws UnifyException {
        actionName = getRequestTarget(String.class);
        notesInfo.setNotes(null);

        // Check for notes
        for (WfAction wfAction : workflowItem.getActionList()) {
            if (actionName.equals(wfAction.getName())) {
                RequirementType notesReq = wfAction.getNoteReqType();
                if (notesReq == null || RequirementType.NONE.equals(notesReq)) {
                    break;
                }

                notesInfo.setRequired(RequirementType.MANDATORY.equals(notesReq));
                notesInfo.setApplyActionCaption(getSessionMessage("button.addnotesapplyaction", wfAction.getLabel()));
                return showNotes(true);
            }
        }

        return internalApplyActionToWorkflowItem();
    }

    @Action
    public String applyActionWithNotes() throws UnifyException {
        workflowItem.setNotes(notesInfo.getNotes());
        return internalApplyActionToWorkflowItem();
    }

    @Action
    public String prepareViewWorkflowItems() throws UnifyException {
        wfItemIds = getSelectedIds();
        viewIndex = 0;
        return prepareWorkflowItemForView();
    }

    @Action
    public String fetchWorkflowItems() throws UnifyException {
        getWorkflowService().grabCurrentUserWorkItems(wfStepName);
        return refreshWorkflowItemSummary();
    }

    @Action
    public String releaseWorkflowItems() throws UnifyException {
        List<Long> wfItemIds = getSelectedIds();
        getWorkflowService().releaseCurrentUserWorkItems(wfStepName, wfItemIds);
        return refreshWorkflowItemSummary();
    }

    @Action
    public String showWorkflowItemAttachments() throws UnifyException {
        Long wfItemId = wfItemIds.get(viewIndex);
        List<WfItemAttachmentInfo> wfAttachmentList = getWorkflowService().fetchWorkflowItemAttachments(wfItemId, true);
        List<FileAttachmentInfo> filaAttachmentInfoList = new ArrayList<FileAttachmentInfo>();
        for (WfItemAttachmentInfo workflowItemAttachment : wfAttachmentList) {
            FileAttachmentInfo fileAttachmentInfo = new FileAttachmentInfo(workflowItemAttachment.getName(),
                    workflowItemAttachment.getLabel(), workflowItemAttachment.getType());
            fileAttachmentInfo.setFilename(workflowItemAttachment.getFilename());
            filaAttachmentInfoList.add(fileAttachmentInfo);
        }

        fileAttachmentsInfo = new FileAttachmentsInfo(wfItemId, filaAttachmentInfoList);
        return "showattachments";
    }

    @Action
    public String showWorkflowItemNotes() throws UnifyException {
        return showNotes(false);
    }

    @Action
    public String closeViewWorkflowItems() throws UnifyException {
        refreshWorkflowItemSummary();

        wfItemIds = null;
        viewIndex = 0;
        workflowItem = null;
        workingCache = null;
        return "switchsearchitems";
    }

    @Action
    public String firstRecord() throws UnifyException {
        viewIndex = 0;
        return prepareWorkflowItemForView();
    }

    @Action
    public String previousRecord() throws UnifyException {
        viewIndex--;
        return prepareWorkflowItemForView();
    }

    @Action
    public String nextRecord() throws UnifyException {
        viewIndex++;
        return prepareWorkflowItemForView();
    }

    @Action
    public String lastRecord() throws UnifyException {
        viewIndex = wfItemIds.size() - 1;
        return prepareWorkflowItemForView();
    }

    public String getWfStepName() {
        return wfStepName;
    }

    public void setWfStepName(String wfStepName) {
        this.wfStepName = wfStepName;
    }

    public WfItemObjects getCsWorkItems() {
        return csWorkItems;
    }

    public void setCsWorkItems(WfItemObjects csWorkItems) {
        this.csWorkItems = csWorkItems;
    }

    public WfItemObject getWorkflowItem() {
        return workflowItem;
    }

    public int getItemCount() {
        if (wfItemIds != null) {
            return wfItemIds.size();
        }

        return 0;
    }

    public int getViewIndex() {
        return viewIndex;
    }

    public FileAttachmentsInfo getFileAttachmentsInfo() {
        return fileAttachmentsInfo;
    }

    public NotesInfo getNotesInfo() {
        return notesInfo;
    }

    @Override
    protected void onSetPage() throws UnifyException {
        table = getPageWidgetByShortName(Table.class, "wfItemsTbl");
        wfItemNotesPanel = getPageWidgetByShortName(WfItemNotesPanel.class, "notesPopup");
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        manageDisplay();
    }

    private String showNotes(boolean isAddNotes) throws UnifyException {
        WfItemHistObject wih = getWorkflowService().findWorkflowItemHistory(workflowItem.getWfItemHistId(), true);
        notesInfo.setNotesHistEventList(wih.getEventList());
        wfItemNotesPanel.setAddNotes(isAddNotes);
        return "shownotes";
    }

    private String prepareWorkflowItemForView() throws UnifyException {
        // Load workflow item
        Long wfItemId = wfItemIds.get(viewIndex);
        if (workingCache != null && workingCache.containsKey(wfItemId)) {
            workflowItem = workingCache.get(wfItemId);
        } else {
            workflowItem = getWorkflowService().findWorkflowItem(wfItemId);
            workflowItem.setActionList(csWorkItems.getActionList());

            if (workingCache == null) {
                workingCache = new HashMap<Long, WfItemObject>();
            }

            workingCache.put(wfItemId, workflowItem);
        }

        workflowItem.setLabel(getSessionMessage("label.itemcount", viewIndex + 1, wfItemIds.size()));
        return "switchdocviewer";
    }

    private String internalApplyActionToWorkflowItem() throws UnifyException {
        getWorkflowService().applyWorkflowAction(workflowItem, actionName);
        Long wfItemId = wfItemIds.remove(viewIndex);
        workingCache.remove(wfItemId);

        if (!wfItemIds.isEmpty()) {
            if (viewIndex >= wfItemIds.size()) {
                viewIndex = wfItemIds.size() - 1;
            }
            return prepareWorkflowItemForView();
        }

        return closeViewWorkflowItems();
    }

    private List<Long> getSelectedIds() throws UnifyException {
        if (table.getSelectedRows() > 0) {
            Integer[] selectedIndexes = table.getSelectedRowIndexes();
            List<Long> selectedIds = new ArrayList<Long>();
            for (int i = 0; i < selectedIndexes.length; i++) {
                selectedIds.add((Long) csWorkItems.getWfItemList().get(selectedIndexes[i]).getId());
            }
            return selectedIds;
        }
        return Collections.emptyList();
    }

    private void manageDisplay() throws UnifyException {
        boolean isItems = csWorkItems != null && csWorkItems.isActionListItems();
        setVisible("wfActionButtonsOnMultiple", isItems);
        setDisabled("fetchItemsBtn", StringUtils.isBlank(wfStepName));
        setDisabled("releaseItemsBtn", !isItems);
        setDisabled("viewItemsBtn", !isItems);
    }
}
