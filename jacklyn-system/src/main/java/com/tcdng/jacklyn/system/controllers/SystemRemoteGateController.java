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

import java.util.ArrayList;
import java.util.List;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.controllers.BaseRemoteCallController;
import com.tcdng.jacklyn.shared.system.SystemRemoteCallNameConstants;
import com.tcdng.jacklyn.shared.system.data.AppMenuItem;
import com.tcdng.jacklyn.shared.system.data.AppMenuItemGroup;
import com.tcdng.jacklyn.shared.system.data.AppModule;
import com.tcdng.jacklyn.shared.system.data.GetAppInfoParams;
import com.tcdng.jacklyn.shared.system.data.GetAppInfoResult;
import com.tcdng.jacklyn.shared.system.data.GetAppMenuParams;
import com.tcdng.jacklyn.shared.system.data.GetAppMenuResult;
import com.tcdng.jacklyn.shared.system.data.GetAppModulesParams;
import com.tcdng.jacklyn.shared.system.data.GetAppModulesResult;
import com.tcdng.jacklyn.shared.system.data.GetToolingListTypeParams;
import com.tcdng.jacklyn.shared.system.data.GetToolingListTypeResult;
import com.tcdng.jacklyn.shared.system.data.GetToolingRecordTypeParams;
import com.tcdng.jacklyn.shared.system.data.GetToolingRecordTypeResult;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.jacklyn.system.constants.SystemModuleSysParamConstants;
import com.tcdng.jacklyn.system.entities.ApplicationMenu;
import com.tcdng.jacklyn.system.entities.ApplicationMenuItem;
import com.tcdng.jacklyn.system.entities.ApplicationMenuItemQuery;
import com.tcdng.jacklyn.system.entities.ApplicationMenuQuery;
import com.tcdng.jacklyn.system.entities.Module;
import com.tcdng.jacklyn.system.entities.ModuleQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.GatewayAction;

/**
 * System module remote call controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = SystemModuleNameConstants.SYSTEM_MODULE)
@Component("/system/gate")
public class SystemRemoteGateController extends BaseRemoteCallController {

    @Configurable(SystemModuleNameConstants.SYSTEMSERVICE)
    private SystemService systemService;

    @GatewayAction(name = SystemRemoteCallNameConstants.GET_APPLICATION_INFO,
            description = "$m{system.gate.remotecall.getapplicationinfo}")
    public GetAppInfoResult getApplicationInfo(GetAppInfoParams params) throws UnifyException {
        return new GetAppInfoResult(getApplicationName(),
                systemService.getSysParameterValue(String.class,
                        SystemModuleSysParamConstants.SYSPARAM_APPLICATION_VERSION),
                systemService.getSysParameterValue(String.class, SystemModuleSysParamConstants.SYSPARAM_CLIENT_TITLE));
    }

    @GatewayAction(name = SystemRemoteCallNameConstants.GET_APPLICATION_MENU,
            description = "$m{system.gate.remotecall.getapplicationmenu}")
    public GetAppMenuResult getApplicationMenu(GetAppMenuParams params) throws UnifyException {
        List<AppMenuItemGroup> appMenuItemGroupList = new ArrayList<AppMenuItemGroup>();
        ApplicationMenuQuery menuQuery = new ApplicationMenuQuery();
        if (QueryUtils.isValidStringCriteria(params.getModuleName())) {
            menuQuery.moduleName(params.getModuleName());
        }

        if (QueryUtils.isValidStringCriteria(params.getMenuName())) {
            menuQuery.name(params.getMenuName());
        }
        menuQuery.ignoreEmptyCriteria(true);
        menuQuery.orderByDisplayOrder();

        List<ApplicationMenu> menuList = systemService.findMenus(menuQuery);
        for (ApplicationMenu menuData : menuList) {
            List<AppMenuItem> appMenuItemList = new ArrayList<AppMenuItem>();
            List<ApplicationMenuItem> menuItemList = systemService
                    .findMenuItems(new ApplicationMenuItemQuery().menuId(menuData.getId()).orderByDisplayOrder());
            for (ApplicationMenuItem menuItemData : menuItemList) {
                String remotePath = menuItemData.getRemotePath();
                if (!StringUtils.isBlank(remotePath)) {
                    appMenuItemList.add(new AppMenuItem(menuItemData.getName(),
                            resolveApplicationMessage(menuItemData.getDescription()),
                            resolveApplicationMessage(menuItemData.getPageCaption()),
                            resolveApplicationMessage(menuItemData.getCaption()), remotePath));
                }
            }

            String remotePath = menuData.getRemotePath();
            if (!StringUtils.isBlank(remotePath) || !appMenuItemList.isEmpty()) {
                appMenuItemGroupList.add(
                        new AppMenuItemGroup(menuData.getName(), resolveApplicationMessage(menuData.getDescription()),
                                resolveApplicationMessage(menuData.getPageCaption()),
                                resolveApplicationMessage(menuData.getCaption()), remotePath, appMenuItemList));
            }
        }

        return new GetAppMenuResult(appMenuItemGroupList);
    }

    @GatewayAction(name = SystemRemoteCallNameConstants.GET_APPLICATION_MODULES,
            description = "$m{system.gate.remotecall.getapplicationmodule}")
    public GetAppModulesResult getApplicationModules(GetAppModulesParams params) throws UnifyException {
        List<AppModule> appModuleList = new ArrayList<AppModule>();
        List<Module> moduleList = systemService.findModules((ModuleQuery) new ModuleQuery().ignoreEmptyCriteria(true));
        for (Module moduleData : moduleList) {
            appModuleList.add(
                    new AppModule(moduleData.getName(), moduleData.getDescription(), moduleData.getStatus().code()));
        }

        return new GetAppModulesResult(appModuleList);
    }

    @GatewayAction(name = SystemRemoteCallNameConstants.GET_TOOLING_RECORD_TYPES,
            description = "$m{system.gate.remotecall.getrecordtypes}")
    public GetToolingRecordTypeResult getToolingRecordTypes(GetToolingRecordTypeParams params) throws UnifyException {
        return new GetToolingRecordTypeResult(systemService.findToolingRecordTypes());
    }

    @GatewayAction(name = SystemRemoteCallNameConstants.GET_TOOLING_LIST_TYPES,
            description = "$m{system.gate.remotecall.getlisttypes}")
    public GetToolingListTypeResult getToolingListTypes(GetToolingListTypeParams params) throws UnifyException {
        return new GetToolingListTypeResult(systemService.findToolingListTypes());
    }

}
