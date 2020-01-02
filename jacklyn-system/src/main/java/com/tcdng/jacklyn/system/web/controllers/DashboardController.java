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
package com.tcdng.jacklyn.system.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.annotation.CrudPanelList;
import com.tcdng.jacklyn.common.annotation.SessionAttr;
import com.tcdng.jacklyn.common.annotation.SessionLoading;
import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.system.data.DashboardLargeData;
import com.tcdng.jacklyn.system.entities.Dashboard;
import com.tcdng.jacklyn.system.entities.DashboardQuery;
import com.tcdng.jacklyn.system.web.beans.DashboardPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing dashboard records.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/dashboard")
@UplBinding("web/system/upl/managedashboard.upl")
@SessionLoading(
        sessionAttributes = { @SessionAttr(name = "largeData.layerList", property = "largeData.layerList") },
        crudPanelLists = { @CrudPanelList(panel = "frmLayerListPanel", property = "largeData.layerList"),
                @CrudPanelList(panel = "frmPortletListPanel", property = "largeData.portletList") })
public class DashboardController extends AbstractSystemFormController<DashboardPageBean, Dashboard> {

    public DashboardController() {
        super(DashboardPageBean.class, Dashboard.class, ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<Dashboard> find() throws UnifyException {
        DashboardPageBean pageBean = getPageBean();
        DashboardQuery query = new DashboardQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchName())) {
            query.nameLike(pageBean.getSearchName());
        }
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }
        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.addOrder("description").ignoreEmptyCriteria(true);
        return getSystemService().findDashboards(query);
    }

    @Override
    protected Dashboard find(Long id) throws UnifyException {
        DashboardLargeData largeData = getSystemService().findDashboard(id);
        DashboardPageBean pageBean = getPageBean();
        pageBean.setLargeData(largeData);
        return largeData.getData();
    }

    @Override
    protected Dashboard prepareCreate() throws UnifyException {
        DashboardLargeData largeData = new DashboardLargeData();
        DashboardPageBean pageBean = getPageBean();
        pageBean.setLargeData(largeData);
        return largeData.getData();
    }

    @Override
    protected void onLoseView(Dashboard dashboardData) throws UnifyException {
        DashboardPageBean pageBean = getPageBean();
        pageBean.setLargeData(new DashboardLargeData());
    }

    @Override
    protected Object create(Dashboard dashboardData) throws UnifyException {
        DashboardPageBean pageBean = getPageBean();
        return (Long) getSystemService().createDashboard(pageBean.getLargeData().getData());
    }

    @Override
    protected int update(Dashboard dashboardData) throws UnifyException {
        DashboardPageBean pageBean = getPageBean();
        return getSystemService().updateDashboard(pageBean.getLargeData());
    }

    @Override
    protected int delete(Dashboard applicationData) throws UnifyException {
        return getSystemService().deleteDashboard(applicationData.getId());
    }
}
