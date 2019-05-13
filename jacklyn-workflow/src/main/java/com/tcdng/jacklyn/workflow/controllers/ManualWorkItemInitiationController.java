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

import com.tcdng.jacklyn.workflow.data.ManualInitInfo;
import com.tcdng.jacklyn.workflow.data.ManualWfItem;
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
public class ManualWorkItemInitiationController extends AbstractWorkflowController {

    private static final int LISTING_MODE = 0;

    private static final int CREATE_ITEM_MODE = 1;

    private LinkGridInfo wfTemplateGridInfo;

    private ManualWfItem manualInitItem;

    private String templateName;

    private int mode;

    public ManualWorkItemInitiationController() {
        super(true, false);
    }

    public LinkGridInfo getWfTemplateGridInfo() {
        return wfTemplateGridInfo;
    }

    public ManualWfItem getManualInitItem() {
        return manualInitItem;
    }

    @Action
    public String openCreateItem() throws UnifyException {
        templateName = getRequestTarget(String.class);
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
        mode = LISTING_MODE;
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
        if (mode == LISTING_MODE) {
            buildUserRoleTemplateListing();
        }
    }

    @Override
    protected String getDocViewPanelName() {
        return "manualWorkItemFrame";
    }

    private String prepareCreate() throws UnifyException {
        manualInitItem = getWorkflowService().createManualInitItem(templateName);
        mode = CREATE_ITEM_MODE;
        setPageValidationEnabled(true);
        return "switchworkitem";
    }

    private void pendItem() throws UnifyException {
        getWorkflowService().pendManualInitItem(manualInitItem);
        hintUser("$m{hint.workflow.manualinit.pend.success}", manualInitItem.getTitle());
    }

    private void submitItem() throws UnifyException {
        getWorkflowService().submitManualInitItem(manualInitItem);
        hintUser("$m{hint.workflow.manualinit.submit.success}");
    }

    private void buildUserRoleTemplateListing() throws UnifyException {
        LinkGridInfo.Builder lb = LinkGridInfo.newBuilder();
        for (ManualInitInfo manualInitInfo : getWorkflowService().findUserRoleManualInitInfos()) {
            String categoryName = manualInitInfo.getCategoryName();
            if (!lb.isCategory(categoryName)) {
                lb.addCategory(categoryName, manualInitInfo.getCategoryDesc(),
                        "/workflow/manualworkiteminitiation/openCreateItem");
            }

            lb.addLink(categoryName, manualInitInfo.getProcessGlobalName(), manualInitInfo.getProcessDesc());
        }

        wfTemplateGridInfo = lb.build();
    }
}
