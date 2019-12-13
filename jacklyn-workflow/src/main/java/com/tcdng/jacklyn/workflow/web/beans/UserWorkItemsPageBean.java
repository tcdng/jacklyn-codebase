/*
 * Copyright 2018-2019 The Code Department
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

package com.tcdng.jacklyn.workflow.web.beans;

import java.util.List;
import java.util.Map;

import com.tcdng.jacklyn.common.web.beans.BasePageBean;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem;
import com.tcdng.jacklyn.workflow.data.InteractWfItems;
import com.tcdng.jacklyn.workflow.web.widgets.CommentsInfo;
import com.tcdng.unify.web.ui.data.FileAttachmentsInfo;

/**
 * User workflow items page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class UserWorkItemsPageBean extends BasePageBean {

    private String wfStepName;

    private InteractWfItems csWorkItems;

    private FlowingWfItem workflowItem;

    private List<Long> wfItemIds;

    private int viewIndex;

    private String actionName;

    private FileAttachmentsInfo fileAttachmentsInfo;

    private CommentsInfo commentsInfo;

    private Map<Long, FlowingWfItem> workingCache;

    public UserWorkItemsPageBean() {
        super("userWorkItemsPanel");
        commentsInfo = new CommentsInfo();
    }

    public String getWfStepName() {
        return wfStepName;
    }

    public void setWfStepName(String wfStepName) {
        this.wfStepName = wfStepName;
    }

    public InteractWfItems getCsWorkItems() {
        return csWorkItems;
    }

    public void setCsWorkItems(InteractWfItems csWorkItems) {
        this.csWorkItems = csWorkItems;
    }

    public FlowingWfItem getWorkflowItem() {
        return workflowItem;
    }

    public void setWorkflowItem(FlowingWfItem workflowItem) {
        this.workflowItem = workflowItem;
    }

    public List<Long> getWfItemIds() {
        return wfItemIds;
    }

    public void setWfItemIds(List<Long> wfItemIds) {
        this.wfItemIds = wfItemIds;
    }

    public int getViewIndex() {
        return viewIndex;
    }

    public void setViewIndex(int viewIndex) {
        this.viewIndex = viewIndex;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public FileAttachmentsInfo getFileAttachmentsInfo() {
        return fileAttachmentsInfo;
    }

    public void setFileAttachmentsInfo(FileAttachmentsInfo fileAttachmentsInfo) {
        this.fileAttachmentsInfo = fileAttachmentsInfo;
    }

    public CommentsInfo getCommentsInfo() {
        return commentsInfo;
    }

    public void setCommentsInfo(CommentsInfo commentsInfo) {
        this.commentsInfo = commentsInfo;
    }

    public Map<Long, FlowingWfItem> getWorkingCache() {
        return workingCache;
    }

    public void setWorkingCache(Map<Long, FlowingWfItem> workingCache) {
        this.workingCache = workingCache;
    }

    public int getItemCount() {
        if (wfItemIds != null) {
            return wfItemIds.size();
        }

        return 0;
    }

}
