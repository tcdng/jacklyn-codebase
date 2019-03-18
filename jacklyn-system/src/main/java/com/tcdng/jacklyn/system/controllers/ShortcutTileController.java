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
package com.tcdng.jacklyn.system.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.system.constants.SystemModuleAuditConstants;
import com.tcdng.jacklyn.system.entities.ShortcutTile;
import com.tcdng.jacklyn.system.entities.ShortcutTileQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;

/**
 * Controller for managing shortcut tile record.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/shortcuttile")
@UplBinding("web/system/upl/manageshortcuttiles.upl")
@ResultMappings({ @ResultMapping(name = "showorderpopup",
        response = { "!showpopupresponse popup:$s{orderShortcutTilePopup}" }) })
public class ShortcutTileController extends AbstractSystemCrudController<ShortcutTile> {

    private Long searchModuleId;

    private List<ShortcutTile> shortcutTileOrderList;

    public ShortcutTileController() {
        super(ShortcutTile.class, "$m{system.shortcuttile.hint}",
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    @Action
    public String prepareSetShortcutTileOrder() throws UnifyException {
        ShortcutTileQuery query = new ShortcutTileQuery();
        if (QueryUtils.isValidLongCriteria(searchModuleId)) {
            query.moduleId(searchModuleId);
        }
        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.installed(Boolean.TRUE);
        query.orderByDisplayOrder();
        query.ignoreEmptyCriteria(true);
        shortcutTileOrderList = getSystemService().findShortcutTiles(query);
        return "showorderpopup";
    }

    @Action
    public String saveShortcutTileOrder() throws UnifyException {
        getSystemService().saveShortcutTileOrder(shortcutTileOrderList);
        logUserEvent(SystemModuleAuditConstants.SET_SHORTCUTTILE_DISPLAY_ORDER,
                DataUtils.getBeanPropertyArray(String.class, shortcutTileOrderList, "description"));
        hintUser("$m{system.order.shortcuttile.saved}");
        return cancelShortcutTileOrder();
    }

    @Action
    public String cancelShortcutTileOrder() throws UnifyException {
        shortcutTileOrderList = null;
        return hidePopup();
    }

    public Long getSearchModuleId() {
        return searchModuleId;
    }

    public void setSearchModuleId(Long searchModuleId) {
        this.searchModuleId = searchModuleId;
    }

    public List<ShortcutTile> getShortcutTileOrderList() {
        return shortcutTileOrderList;
    }

    public void setShortcutTileOrderList(List<ShortcutTile> shortcutTileOrderList) {
        this.shortcutTileOrderList = shortcutTileOrderList;
    }

    @Override
    protected List<ShortcutTile> find() throws UnifyException {
        ShortcutTileQuery query = new ShortcutTileQuery();
        if (QueryUtils.isValidLongCriteria(searchModuleId)) {
            query.moduleId(searchModuleId);
        }

        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }

        query.installed(Boolean.TRUE);
        query.ignoreEmptyCriteria(true);
        return getSystemService().findShortcutTiles(query);

    }

    @Override
    protected ShortcutTile find(Long id) throws UnifyException {
        return getSystemService().findShortcutTile(id);
    }

    @Override
    protected ShortcutTile prepareCreate() throws UnifyException {
        return null;
    }

    @Override
    protected Object create(ShortcutTile record) throws UnifyException {
        return null;
    }

    @Override
    protected int update(ShortcutTile record) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(ShortcutTile record) throws UnifyException {
        return 0;
    }
}
