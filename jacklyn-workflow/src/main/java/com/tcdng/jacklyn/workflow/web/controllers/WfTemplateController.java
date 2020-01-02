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

import java.util.List;

import com.tcdng.jacklyn.common.annotation.CrudPanelList;
import com.tcdng.jacklyn.common.annotation.SessionLoading;
import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.workflow.data.WfTemplateLargeData;
import com.tcdng.jacklyn.workflow.entities.WfTemplate;
import com.tcdng.jacklyn.workflow.entities.WfTemplateQuery;
import com.tcdng.jacklyn.workflow.web.beans.WfTemplatePageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing workflow templates.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/workflow/wftemplate")
@UplBinding("web/workflow/upl/managewftemplate.upl")
@SessionLoading(crudPanelLists = { @CrudPanelList(panel = "frmWfStepListPanel", property = "largeData.stepList"),
        @CrudPanelList(panel = "frmWfEnrichmentListPanel", property = "largeData.enrichmentList"),
        @CrudPanelList(panel = "frmWfRoutingListPanel", property = "largeData.routingList"),
        @CrudPanelList(panel = "frmWfUserActionListPanel", property = "largeData.userActionList"),
        @CrudPanelList(panel = "frmWfRecordActionListPanel", property = "largeData.recordActionList"),
        @CrudPanelList(panel = "frmWfFormPrivilegeListPanel", property = "largeData.formPrivilegeList"),
        @CrudPanelList(panel = "frmWfPolicyListPanel", property = "largeData.policyList"),
        @CrudPanelList(panel = "frmWfAlertListPanel", property = "largeData.alertList") })
public class WfTemplateController extends AbstractWorkflowFormController<WfTemplatePageBean, WfTemplate> {

    public WfTemplateController() {
        super(WfTemplatePageBean.class, WfTemplate.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<WfTemplate> find() throws UnifyException {
        WfTemplatePageBean pageBean = getPageBean();
        WfTemplateQuery query = new WfTemplateQuery();
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchWfCategoryId())) {
            query.wfCategoryId(pageBean.getSearchWfCategoryId());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchName())) {
            query.nameLike(pageBean.getSearchName());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }

        if (pageBean.getSearchStatus() != null) {
            query.wfCategoryStatus(pageBean.getSearchStatus());
        }

        query.ignoreEmptyCriteria(true);
        return getWorkflowService().findWfTemplates(query);
    }

    @Override
    protected WfTemplate find(Long id) throws UnifyException {
        WfTemplateLargeData largeData = getWorkflowService().findLargeWfTemplate(id);
        WfTemplatePageBean pageBean = getPageBean();
        pageBean.setLargeData(largeData);
        return largeData.getData();
    }

    @Override
    protected WfTemplate prepareCreate() throws UnifyException {
        return null;
    }

    @Override
    protected Object create(WfTemplate wfTemplate) throws UnifyException {
        return null;
    }

    @Override
    protected int update(WfTemplate wfTemplate) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(WfTemplate wfTemplate) throws UnifyException {
        return 0;
    }
}
