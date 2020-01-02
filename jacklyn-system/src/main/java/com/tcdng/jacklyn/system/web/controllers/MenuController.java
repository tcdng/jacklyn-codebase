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
import com.tcdng.jacklyn.system.entities.ApplicationMenu;
import com.tcdng.jacklyn.system.entities.ApplicationMenuQuery;
import com.tcdng.jacklyn.system.web.beans.ApplicationMenuPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;

/**
 * Controller for managing menu records.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/menu")
@UplBinding("web/system/upl/managemenu.upl")
@ResultMappings({
        @ResultMapping(name = "managemenuitems", response = { "!postresponse path:$s{/system/menuitem/openPage}" }),
        @ResultMapping(name = "showorderpopup", response = { "!showpopupresponse popup:$s{orderMenuPopup}" }) })
public class MenuController extends AbstractSystemFormController<ApplicationMenuPageBean, ApplicationMenu> {

    public MenuController() {
        super(ApplicationMenuPageBean.class, ApplicationMenu.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    @Action
    public String prepareMenuItems() throws UnifyException {
        writeOtherControllerProperty("/system/menuitem", "searchMenuId", getSelectedRecord().getId());
        return "managemenuitems";
    }

    @Action
    public String prepareSetMenuOrder() throws UnifyException {
        ApplicationMenuPageBean pageBean = getPageBean();
        ApplicationMenuQuery query = new ApplicationMenuQuery();
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchModuleId())) {
            query.moduleId(pageBean.getSearchModuleId());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }

        query.installed(Boolean.TRUE);
        query.orderByDisplayOrder();
        query.ignoreEmptyCriteria(true);
        pageBean.setMenuOrderList(getSystemService().findMenus(query));
        return "showorderpopup";
    }

    @Action
    public String saveMenuOrder() throws UnifyException {
        ApplicationMenuPageBean pageBean = getPageBean();
        List<ApplicationMenu> menuOrderList = pageBean.getMenuOrderList();
        getSystemService().saveMenuOrder(menuOrderList);
        logUserEvent(SystemModuleAuditConstants.SET_MENU_DISPLAY_ORDER,
                DataUtils.getBeanPropertyArray(String.class, menuOrderList, "caption"));
        hintUser("$m{system.order.menu.saved}");
        pageBean.setMenuOrderList(null);
        return hidePopup();
    }

    @Action
    public String cancelMenuOrder() throws UnifyException {
        ApplicationMenuPageBean pageBean = getPageBean();
        pageBean.setMenuOrderList(null);
        return hidePopup();
    }

    @Override
    protected List<ApplicationMenu> find() throws UnifyException {
        ApplicationMenuPageBean pageBean = getPageBean();
        ApplicationMenuQuery query = new ApplicationMenuQuery();
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchModuleId())) {
            query.moduleId(pageBean.getSearchModuleId());
        }
        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.installed(Boolean.TRUE);
        query.addOrder("caption").ignoreEmptyCriteria(true);
        return getSystemService().findMenus(query);
    }

    @Override
    protected ApplicationMenu find(Long id) throws UnifyException {
        return getSystemService().findMenu(id);
    }

    @Override
    protected ApplicationMenu prepareCreate() throws UnifyException {
        return null;
    }

    @Override
    protected Object create(ApplicationMenu menuData) throws UnifyException {
        return null;
    }

    @Override
    protected int update(ApplicationMenu menuData) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(ApplicationMenu menuData) throws UnifyException {
        return 0;
    }
}
