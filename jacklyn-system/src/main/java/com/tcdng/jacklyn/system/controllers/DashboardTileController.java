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

import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.system.constants.SystemModuleAuditConstants;
import com.tcdng.jacklyn.system.entities.DashboardTile;
import com.tcdng.jacklyn.system.entities.DashboardTileQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;

/**
 * Controller for managing dashboard tile record.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/dashboardtile")
@UplBinding("web/system/upl/managedashboardtiles.upl")
@ResultMappings({ @ResultMapping(name = "showorderpopup",
		response = { "!showpopupresponse popup:$s{orderDashboardTilePopup}" }) })
public class DashboardTileController extends AbstractSystemRecordController<DashboardTile> {

	private Long searchModuleId;

	private List<DashboardTile> dashboardTileOrderList;

	public DashboardTileController() {
		super(DashboardTile.class, "system.dashboardtile.hint", ManageRecordModifier.SECURE
				| ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
	}

	@Action
	public String prepareSetDashboardTileOrder() throws UnifyException {
		DashboardTileQuery query = new DashboardTileQuery();
		if (QueryUtils.isValidLongCriteria(searchModuleId)) {
			query.moduleId(searchModuleId);
		}
		if (getSearchStatus() != null) {
			query.status(getSearchStatus());
		}
		query.orderByDisplayOrder();
		query.ignoreEmptyCriteria(true);
		dashboardTileOrderList = getSystemModule().findDashboardTiles(query);
		return "showorderpopup";
	}

	@Action
	public String saveDashboardTileOrder() throws UnifyException {
		getSystemModule().saveDashboardTileOrder(dashboardTileOrderList);
		logUserEvent(SystemModuleAuditConstants.AUDIT_SET_DASHBOARDTILE_DISPLAY_ORDER, DataUtils
				.getBeanPropertyArray(String.class, dashboardTileOrderList, "description"));
		hintUser("system.order.dashboardtile.saved");
		return cancelDashboardTileOrder();
	}

	@Action
	public String cancelDashboardTileOrder() throws UnifyException {
		dashboardTileOrderList = null;
		return hidePopup();
	}

	public Long getSearchModuleId() {
		return searchModuleId;
	}

	public void setSearchModuleId(Long searchModuleId) {
		this.searchModuleId = searchModuleId;
	}

	public List<DashboardTile> getDashboardTileOrderList() {
		return dashboardTileOrderList;
	}

	public void setDashboardTileOrderList(List<DashboardTile> dashboardTileOrderList) {
		this.dashboardTileOrderList = dashboardTileOrderList;
	}

	@Override
	protected List<DashboardTile> find() throws UnifyException {
		DashboardTileQuery query = new DashboardTileQuery();
		if (QueryUtils.isValidLongCriteria(searchModuleId)) {
			query.moduleId(searchModuleId);
		}
		if (getSearchStatus() != null) {
			query.status(getSearchStatus());
		}
		query.ignoreEmptyCriteria(true);
		return getSystemModule().findDashboardTiles(query);

	}

	@Override
	protected DashboardTile find(Long id) throws UnifyException {
		return getSystemModule().findDashboardTile(id);
	}

	@Override
	protected DashboardTile prepareCreate() throws UnifyException {
		return null;
	}

	@Override
	protected Object create(DashboardTile record) throws UnifyException {
		return null;
	}

	@Override
	protected int update(DashboardTile record) throws UnifyException {
		return 0;
	}

	@Override
	protected int delete(DashboardTile record) throws UnifyException {
		return 0;
	}
}
