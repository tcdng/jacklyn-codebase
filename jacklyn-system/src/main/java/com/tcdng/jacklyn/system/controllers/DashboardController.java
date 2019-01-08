/*
 * Copyright 2018 The Code Department
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
package com.tcdng.jacklyn.system.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.annotation.CrudPanelList;
import com.tcdng.jacklyn.common.annotation.SessionLoading;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.system.data.DashboardLargeData;
import com.tcdng.jacklyn.system.entities.Dashboard;
import com.tcdng.jacklyn.system.entities.DashboardQuery;
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
@SessionLoading(crudPanelLists = { @CrudPanelList(panel = "frmLayerListPanel", field = "largeData.layerList"),
        @CrudPanelList(panel = "frmPortletListPanel", field = "largeData.portletList") })
public class DashboardController extends AbstractSystemCrudController<Dashboard> {

    private String searchName;

    private String searchDescription;

    private RecordStatus searchStatus;

    private DashboardLargeData largeData;

    public DashboardController() {
        super(Dashboard.class, "$m{system.dashboard.hint}", ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
        largeData = new DashboardLargeData();
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public DashboardLargeData getLargeData() {
        return largeData;
    }

    public void setLargeData(DashboardLargeData largeData) {
        this.largeData = largeData;
    }

    @Override
    protected List<Dashboard> find() throws UnifyException {
        DashboardQuery query = new DashboardQuery();
        if (QueryUtils.isValidStringCriteria(searchName)) {
            query.nameLike(searchName);
        }
        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }
        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.order("description").ignoreEmptyCriteria(true);
        return getSystemService().findDashboards(query);
    }

    @Override
    protected Dashboard find(Long id) throws UnifyException {
        largeData = getSystemService().findDashboard(id);
        return largeData.getData();
    }

    @Override
    protected Dashboard prepareCreate() throws UnifyException {
        largeData = new DashboardLargeData();
        return largeData.getData();
    }

    @Override
    protected void onLoseView(Dashboard dashboardData) throws UnifyException {
        largeData = new DashboardLargeData();
    }

    @Override
    protected Object create(Dashboard dashboardData) throws UnifyException {
        return (Long) getSystemService().createDashboard(largeData);
    }

    @Override
    protected int update(Dashboard dashboardData) throws UnifyException {
        return getSystemService().updateDashboard(largeData);
    }

    @Override
    protected int delete(Dashboard applicationData) throws UnifyException {
        return getSystemService().deleteDashboard(applicationData.getId());
    }
}
