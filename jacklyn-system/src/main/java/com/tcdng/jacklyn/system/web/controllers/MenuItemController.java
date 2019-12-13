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
package com.tcdng.jacklyn.system.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.system.constants.SystemModuleAuditConstants;
import com.tcdng.jacklyn.system.entities.ApplicationMenuItem;
import com.tcdng.jacklyn.system.entities.ApplicationMenuItemQuery;
import com.tcdng.jacklyn.system.web.beans.ApplicationMenuItemPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;

/**
 * Controller for managing menu items.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/menuitem")
@UplBinding("web/system/upl/managemenuitem.upl")
@ResultMappings({
        @ResultMapping(name = "showorderpopup", response = { "!showpopupresponse popup:$s{orderMenuItemPopup}" }) })
public class MenuItemController extends AbstractSystemFormController<ApplicationMenuItemPageBean, ApplicationMenuItem> {

    public MenuItemController() {
        super(ApplicationMenuItemPageBean.class, ApplicationMenuItem.class, ManageRecordModifier.SECURE
                | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE | ManageRecordModifier.SEARCH_ON_OPEN);
    }

    @Action
    public String prepareSetMenuItemOrder() throws UnifyException {
        ApplicationMenuItemPageBean pageBean = getPageBean();
        ApplicationMenuItemQuery query = new ApplicationMenuItemQuery();
        query.menuId(pageBean.getSearchMenuId());
        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }

        query.hidden(Boolean.FALSE);
        query.installed(Boolean.TRUE);
        query.orderByDisplayOrder();
        query.ignoreEmptyCriteria(true);
        pageBean.setMenuItemOrderList(getSystemService().findMenuItems(query));
        return "showorderpopup";
    }

    @Action
    public String saveMenuItemOrder() throws UnifyException {
        ApplicationMenuItemPageBean pageBean = getPageBean();
        List<ApplicationMenuItem> menuItemOrderList = pageBean.getMenuItemOrderList();
        getSystemService().saveMenuItemOrder(menuItemOrderList);
        logUserEvent(SystemModuleAuditConstants.SET_MENUITEM_DISPLAY_ORDER,
                DataUtils.getBeanPropertyArray(String.class, menuItemOrderList, "caption"));
        hintUser("$m{system.order.menuitem.saved}");
        pageBean.setMenuItemOrderList(null);
        return hidePopup();
    }

    @Action
    public String cancelMenuItemOrder() throws UnifyException {
        ApplicationMenuItemPageBean pageBean = getPageBean();
        pageBean.setMenuItemOrderList(null);
        return hidePopup();
    }

    @Override
    protected List<ApplicationMenuItem> find() throws UnifyException {
        ApplicationMenuItemPageBean pageBean = getPageBean();
        ApplicationMenuItemQuery query = new ApplicationMenuItemQuery();
        query.menuId(pageBean.getSearchMenuId());
        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.hidden(Boolean.FALSE);
        query.installed(Boolean.TRUE);
        query.addOrder("caption").ignoreEmptyCriteria(true);
        return getSystemService().findMenuItems(query);
    }

    @Override
    protected ApplicationMenuItem find(Long id) throws UnifyException {
        return getSystemService().findMenuItem(id);
    }

    @Override
    protected ApplicationMenuItem prepareCreate() throws UnifyException {
        return null;
    }

    @Override
    protected Object create(ApplicationMenuItem menuItem) throws UnifyException {
        return null;
    }

    @Override
    protected int update(ApplicationMenuItem menuItem) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(ApplicationMenuItem menuItem) throws UnifyException {
        return 0;
    }
}
