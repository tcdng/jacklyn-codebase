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
package com.tcdng.jacklyn.security.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.tcdng.jacklyn.common.constants.JacklynSessionAttributeConstants;
import com.tcdng.jacklyn.shared.security.PrivilegeCategoryConstants;
import com.tcdng.jacklyn.system.business.SystemModule;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.jacklyn.system.entities.DashboardTileQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.ui.Tile;

/**
 * Dashboard controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/security/landing")
@UplBinding("web/security/upl/dashboardlanding.upl")
public class DashboardController extends AbstractSecurityController {

	public DashboardController() {
		super(true, false);// Secure and read-only
	}

	@Override
	protected void onOpenPage() throws UnifyException {
		List<Tile> tileList = Collections.emptyList();
		DashboardTileQuery query = new DashboardTileQuery().orderByDisplayOrder();
		if (getUserToken().isReservedUser()) {
			query.ignoreEmptyCriteria(true);
			tileList = getSystemBusinessModule().generateTiles(query);
		} else {
			Set<String> dashboardNames
					= getPrivilegeCodes(PrivilegeCategoryConstants.DASHBOARD);
			if (!dashboardNames.isEmpty()) {
				query.nameIn(dashboardNames);
				tileList = getSystemBusinessModule().generateTiles(query);
			}
		}

		setSessionAttribute(JacklynSessionAttributeConstants.DASHBOARDDECK, tileList);
	}

	protected SystemModule getSystemBusinessModule() throws UnifyException {
		return (SystemModule) getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);
	}
}
