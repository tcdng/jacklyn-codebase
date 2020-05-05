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

package com.tcdng.jacklyn.workflow.web.beans;

import com.tcdng.jacklyn.common.web.beans.BasePageBean;
import com.tcdng.jacklyn.workflow.data.CommentsInfo;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem;
import com.tcdng.unify.web.ui.data.FileAttachmentsInfo;

/**
 * My workflow item page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class MyWorkItemPageBean extends BasePageBean {

    private FlowingWfItem workflowItem;

    private String actionName;

    private FileAttachmentsInfo fileAttachmentsInfo;

    private CommentsInfo commentsInfo;

    public MyWorkItemPageBean() {
        super("myWorkItemPanel");
        commentsInfo = new CommentsInfo();
    }

    public FlowingWfItem getWorkflowItem() {
        return workflowItem;
    }

    public void setWorkflowItem(FlowingWfItem workflowItem) {
        this.workflowItem = workflowItem;
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

}
