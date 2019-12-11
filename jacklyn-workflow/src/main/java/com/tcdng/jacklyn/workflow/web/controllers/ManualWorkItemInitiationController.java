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

package com.tcdng.jacklyn.workflow.web.controllers;

import com.tcdng.jacklyn.workflow.data.ManualInitInfo;
import com.tcdng.jacklyn.workflow.data.ManualWfItem;
import com.tcdng.jacklyn.workflow.web.beans.ManualWorkItemInitiationPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.ui.data.LinkGridInfo;

/**
 * Manual work item initiation controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/workflow/manualworkiteminitiation")
@UplBinding("web/workflow/upl/manualworkiteminitiation.upl")
@ResultMappings({ @ResultMapping(
        name = "switchlisting", response = { "!switchpanelresponse panels:$l{manualWorkItemPanel.listingMainPanel}" }),
        @ResultMapping(
                name = "switchworkitem",
                response = { "!switchpanelresponse panels:$l{manualWorkItemPanel.createItemMainPanel}" }) })
public class ManualWorkItemInitiationController extends AbstractWorkflowPageController<ManualWorkItemInitiationPageBean> {

    private static final int LISTING_MODE = 0;

    private static final int CREATE_ITEM_MODE = 1;

    public ManualWorkItemInitiationController() {
        super(ManualWorkItemInitiationPageBean.class, true, false, false);
    }

    @Action
    public String openCreateItem() throws UnifyException {
        ManualWorkItemInitiationPageBean pageBean = getPageBean();
        pageBean.setTemplateName(getRequestTarget(String.class));
        return prepareCreate();
    }

    @Action
    public String pendCreateItem() throws UnifyException {
        pendItem();
        return cancelCreateItem();
    }

    @Action
    public String pendCreateItemNext() throws UnifyException {
        pendItem();
        return prepareCreate();
    }

    @Action
    public String submitCreateItem() throws UnifyException {
        submitItem();
        return cancelCreateItem();
    }

    @Action
    public String submitCreateItemNext() throws UnifyException {
        submitItem();
        return prepareCreate();
    }

    @Action
    public String cancelCreateItem() throws UnifyException {
        ManualWorkItemInitiationPageBean pageBean = getPageBean();
        pageBean.setMode(LISTING_MODE);
        buildUserRoleTemplateListing();
        return "switchlisting";
    }

    @Override
    protected void onIndexPage() throws UnifyException {
        super.onIndexPage();
        buildUserRoleTemplateListing();
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        ManualWorkItemInitiationPageBean pageBean = getPageBean();
        if (pageBean.getMode() == LISTING_MODE) {
            buildUserRoleTemplateListing();
        }
    }

    private String prepareCreate() throws UnifyException {
        ManualWorkItemInitiationPageBean pageBean = getPageBean();
        ManualWfItem manualInitItem = getWorkflowService().createManualInitItem(pageBean.getTemplateName());
        pageBean.setManualInitItem(manualInitItem);
        pageBean.setMode(CREATE_ITEM_MODE);
        setPageValidationEnabled(true);
        return "switchworkitem";
    }

    private void pendItem() throws UnifyException {
        ManualWorkItemInitiationPageBean pageBean = getPageBean();
        getWorkflowService().pendManualInitItem(pageBean.getManualInitItem());
        hintUser("$m{hint.workflow.manualinit.pend.success}", pageBean.getManualInitItem().getTitle());
    }

    private void submitItem() throws UnifyException {
        ManualWorkItemInitiationPageBean pageBean = getPageBean();
        getWorkflowService().submitManualInitItem(pageBean.getManualInitItem());
        hintUser("$m{hint.workflow.manualinit.submit.success}");
    }

    private void buildUserRoleTemplateListing() throws UnifyException {
        ManualWorkItemInitiationPageBean pageBean = getPageBean();
        LinkGridInfo.Builder lb = LinkGridInfo.newBuilder();
        for (ManualInitInfo manualInitInfo : getWorkflowService().findUserRoleManualInitInfos()) {
            String categoryName = manualInitInfo.getCategoryName();
            if (!lb.isCategory(categoryName)) {
                lb.addCategory(categoryName, manualInitInfo.getCategoryDesc(),
                        "/workflow/manualworkiteminitiation/openCreateItem");
            }

            lb.addLink(categoryName, manualInitInfo.getProcessGlobalName(), manualInitInfo.getProcessDesc());
        }

        LinkGridInfo wfTemplateGridInfo = lb.build();
        pageBean.setWfTemplateGridInfo(wfTemplateGridInfo);
    }
}
