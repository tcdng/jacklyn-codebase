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

package com.tcdng.jacklyn.workflow.web.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tcdng.jacklyn.common.web.widgets.BasePanel;
import com.tcdng.jacklyn.workflow.business.WorkflowService;
import com.tcdng.jacklyn.workflow.data.MyWorkItemsInfo;
import com.tcdng.jacklyn.workflow.data.WfItemStatusInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.TargetPath;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.control.Table;

/**
 * My work items panel.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("ui-myworkitemspanel")
@UplBinding("web/workflow/upl/myworkitemspanel.upl")
@UplAttributes({ @UplAttribute(
        name = "wfItemViewerPath", type = String.class, defaultVal = "/workflow/myworkflowitem/openPage") })
public class MyWorkItemsPanel extends BasePanel {

    @Configurable
    private WorkflowService workflowService;

    @Action
    public void reloadWorkflowItems() throws UnifyException {
        MyWorkItemsInfo myWorkItemsInfo = getMyWorkItemsInfo();
        List<WfItemStatusInfo> wfItemList =
                workflowService.getCurrentWorkItemStatusList(myWorkItemsInfo.getWfStepName());
        myWorkItemsInfo.setWfItemList(wfItemList);
    }

    @Action
    public void releaseWorkflowItems() throws UnifyException {
        List<Long> wfItemIdList = getSelectedWfItemIds();
        if (!wfItemIdList.isEmpty()) {
            workflowService.releaseCurrentUserWorkItems(wfItemIdList);
            reloadWorkflowItems();
        }
    }

    @Action
    public void openWorkflowItem() throws UnifyException {
        setCommandResponsePath(
                new TargetPath(getUplAttribute(String.class, "wfItemViewerPath"), getRequestTarget(String.class)));
    }

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        MyWorkItemsInfo myWorkItemsInfo = getMyWorkItemsInfo();
        if (myWorkItemsInfo.getWfItemList() == null) {
            reloadWorkflowItems();
        }
    }

    private MyWorkItemsInfo getMyWorkItemsInfo() throws UnifyException {
        ValueStore vs = getValueStore();
        MyWorkItemsInfo myWorkItemsInfo = null;
        if (vs != null) {
            myWorkItemsInfo = (MyWorkItemsInfo) vs.getValueObject();
        } else {
            myWorkItemsInfo = new MyWorkItemsInfo();
            setValueStore(createValueStore(myWorkItemsInfo));
        }
        
        return myWorkItemsInfo;
    }
    
    private List<Long> getSelectedWfItemIds() throws UnifyException {
        MyWorkItemsInfo myWorkItemsInfo = getValue(MyWorkItemsInfo.class);
        if (myWorkItemsInfo != null) {
            Table table = getWidgetByShortName(Table.class, "wfItemsTbl.contentTbl");
            if (table.getSelectedRows() > 0) {
                Integer[] selectedIndexes = table.getSelectedRowIndexes();
                List<Long> selectedIds = new ArrayList<Long>();
                for (int i = 0; i < selectedIndexes.length; i++) {
                    selectedIds.add(myWorkItemsInfo.getWfItemList().get(selectedIndexes[i]).getWfItemId());
                }
                return selectedIds;
            }
        }
        return Collections.emptyList();
    }
}
