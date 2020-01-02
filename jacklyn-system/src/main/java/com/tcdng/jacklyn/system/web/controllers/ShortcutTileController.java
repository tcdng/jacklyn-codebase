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

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.system.constants.SystemModuleAuditConstants;
import com.tcdng.jacklyn.system.entities.ShortcutTile;
import com.tcdng.jacklyn.system.entities.ShortcutTileQuery;
import com.tcdng.jacklyn.system.web.beans.ShortcutTilePageBean;
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
public class ShortcutTileController extends AbstractSystemFormController<ShortcutTilePageBean, ShortcutTile> {

    public ShortcutTileController() {
        super(ShortcutTilePageBean.class, ShortcutTile.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    @Action
    public String prepareSetShortcutTileOrder() throws UnifyException {
        ShortcutTilePageBean pageBean = getPageBean();
        ShortcutTileQuery query = new ShortcutTileQuery();
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchModuleId())) {
            query.moduleId(pageBean.getSearchModuleId());
        }
        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.installed(Boolean.TRUE);
        query.orderByDisplayOrder();
        query.ignoreEmptyCriteria(true);
        List<ShortcutTile> shortcutTileOrderList = getSystemService().findShortcutTiles(query);
        pageBean.setShortcutTileOrderList(shortcutTileOrderList);
        return "showorderpopup";
    }

    @Action
    public String saveShortcutTileOrder() throws UnifyException {
        ShortcutTilePageBean pageBean = getPageBean();
        List<ShortcutTile> shortcutTileOrderList = pageBean.getShortcutTileOrderList();
        getSystemService().saveShortcutTileOrder(shortcutTileOrderList);
        logUserEvent(SystemModuleAuditConstants.SET_SHORTCUTTILE_DISPLAY_ORDER,
                DataUtils.getBeanPropertyArray(String.class, shortcutTileOrderList, "description"));
        hintUser("$m{system.order.shortcuttile.saved}");
        return cancelShortcutTileOrder();
    }

    @Action
    public String cancelShortcutTileOrder() throws UnifyException {
        ShortcutTilePageBean pageBean = getPageBean();
        pageBean.setShortcutTileOrderList(null);
        return hidePopup();
    }

    @Override
    protected List<ShortcutTile> find() throws UnifyException {
        ShortcutTilePageBean pageBean = getPageBean();
        ShortcutTileQuery query = new ShortcutTileQuery();
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchModuleId())) {
            query.moduleId(pageBean.getSearchModuleId());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
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
    protected Object create(ShortcutTile shortcutTile) throws UnifyException {
        return null;
    }

    @Override
    protected int update(ShortcutTile shortcutTile) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(ShortcutTile shortcutTile) throws UnifyException {
        return 0;
    }
}
