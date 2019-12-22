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

import java.util.ArrayList;
import java.util.List;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.web.controllers.BaseRemoteCallController;
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
import com.tcdng.jacklyn.shared.system.data.GetToolingBaseTypeParams;
import com.tcdng.jacklyn.shared.system.data.GetToolingBaseTypeResult;
import com.tcdng.jacklyn.shared.system.data.GetToolingEnumTypeParams;
import com.tcdng.jacklyn.shared.system.data.GetToolingEnumTypeResult;
import com.tcdng.jacklyn.shared.system.data.GetToolingListTypeParams;
import com.tcdng.jacklyn.shared.system.data.GetToolingListTypeResult;
import com.tcdng.jacklyn.shared.system.data.GetToolingRecordTypeParams;
import com.tcdng.jacklyn.shared.system.data.GetToolingRecordTypeResult;
import com.tcdng.jacklyn.shared.system.data.GetToolingTransformerTypeParams;
import com.tcdng.jacklyn.shared.system.data.GetToolingTransformerTypeResult;
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
import com.tcdng.unify.web.annotation.RemoteAction;

/**
 * System module remote call controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = SystemModuleNameConstants.SYSTEM_MODULE)
@Component("/system/remotecall")
public class SystemRemoteCallController extends BaseRemoteCallController {

    @Configurable
    private SystemService systemService;

    @RemoteAction(
            name = SystemRemoteCallNameConstants.GET_APPLICATION_INFO,
            description = "$m{system.remotecall.getapplicationinfo}")
    public GetAppInfoResult getApplicationInfo(GetAppInfoParams params) throws UnifyException {
        return new GetAppInfoResult(getApplicationName(),
                systemService.getSysParameterValue(String.class,
                        SystemModuleSysParamConstants.SYSPARAM_APPLICATION_VERSION),
                systemService.getSysParameterValue(String.class, SystemModuleSysParamConstants.SYSPARAM_CLIENT_TITLE));
    }

    @RemoteAction(
            name = SystemRemoteCallNameConstants.GET_APPLICATION_MENU,
            description = "$m{system.remotecall.getapplicationmenu}")
    public GetAppMenuResult getApplicationMenu(GetAppMenuParams params) throws UnifyException {
        List<AppMenuItemGroup> appMenuItemGroupList = new ArrayList<AppMenuItemGroup>();
        ApplicationMenuQuery menuQuery = new ApplicationMenuQuery();
        if (QueryUtils.isValidStringCriteria(params.getModuleName())) {
            menuQuery.moduleName(params.getModuleName());
        }

        if (QueryUtils.isValidStringCriteria(params.getMenuName())) {
            menuQuery.name(params.getMenuName());
        }
        menuQuery.installed(Boolean.TRUE);
        menuQuery.orderByDisplayOrder();

        List<ApplicationMenu> menuList = systemService.findMenus(menuQuery);
        for (ApplicationMenu menu : menuList) {
            List<AppMenuItem> appMenuItemList = new ArrayList<AppMenuItem>();
            List<ApplicationMenuItem> menuItemList =
                    systemService.findMenuItems((ApplicationMenuItemQuery) new ApplicationMenuItemQuery()
                            .menuId(menu.getId()).orderByDisplayOrder().installed(Boolean.TRUE));
            for (ApplicationMenuItem menuItem : menuItemList) {
                String path = menuItem.getPath();
                if (StringUtils.isNotBlank(path)) {
                    appMenuItemList.add(
                            new AppMenuItem(menuItem.getName(), resolveApplicationMessage(menuItem.getDescription()),
                                    resolveApplicationMessage(menuItem.getPageCaption()),
                                    resolveApplicationMessage(menuItem.getCaption()), path, menuItem.isHidden()));
                }
            }

            String path = menu.getPath();
            if (StringUtils.isNotBlank(path) || !appMenuItemList.isEmpty()) {
                appMenuItemGroupList
                        .add(new AppMenuItemGroup(menu.getName(), resolveApplicationMessage(menu.getDescription()),
                                resolveApplicationMessage(menu.getPageCaption()),
                                resolveApplicationMessage(menu.getCaption()), path, appMenuItemList));
            }
        }

        return new GetAppMenuResult(appMenuItemGroupList);
    }

    @RemoteAction(
            name = SystemRemoteCallNameConstants.GET_APPLICATION_MODULES,
            description = "$m{system.remotecall.getapplicationmodule}")
    public GetAppModulesResult getApplicationModules(GetAppModulesParams params) throws UnifyException {
        List<AppModule> appModuleList = new ArrayList<AppModule>();
        List<Module> moduleList = systemService.findModules((ModuleQuery) new ModuleQuery().installed(Boolean.TRUE));
        for (Module moduleData : moduleList) {
            appModuleList.add(
                    new AppModule(moduleData.getName(), moduleData.getDescription(), moduleData.getStatus().code()));
        }

        return new GetAppModulesResult(appModuleList);
    }

    @RemoteAction(
            name = SystemRemoteCallNameConstants.GET_TOOLING_BASE_TYPES,
            description = "$m{system.remotecall.getbasetypes}")
    public GetToolingBaseTypeResult getToolingBaseTypes(GetToolingBaseTypeParams params) throws UnifyException {
        return new GetToolingBaseTypeResult(systemService.findToolingBaseTypes());
    }

    @RemoteAction(
            name = SystemRemoteCallNameConstants.GET_TOOLING_RECORD_TYPES,
            description = "$m{system.remotecall.getrecordtypes}")
    public GetToolingRecordTypeResult getToolingRecordTypes(GetToolingRecordTypeParams params) throws UnifyException {
        return new GetToolingRecordTypeResult(systemService.findToolingDocumentTypes());
    }

    @RemoteAction(
            name = SystemRemoteCallNameConstants.GET_TOOLING_ENUMERATION_TYPES,
            description = "$m{system.remotecall.getenumtypes}")
    public GetToolingEnumTypeResult getToolingEnumTypes(GetToolingEnumTypeParams params) throws UnifyException {
        return new GetToolingEnumTypeResult(systemService.findToolingEnumTypes());
    }

    @RemoteAction(
            name = SystemRemoteCallNameConstants.GET_TOOLING_TRANSFORMER_TYPES,
            description = "$m{system.remotecall.gettransformertypes}")
    public GetToolingTransformerTypeResult getToolingTransformerTypes(GetToolingTransformerTypeParams params)
            throws UnifyException {
        return new GetToolingTransformerTypeResult(systemService.findToolingTransformerTypes());
    }

    @RemoteAction(
            name = SystemRemoteCallNameConstants.GET_TOOLING_LIST_TYPES,
            description = "$m{system.remotecall.getlisttypes}")
    public GetToolingListTypeResult getToolingListTypes(GetToolingListTypeParams params) throws UnifyException {
        return new GetToolingListTypeResult(systemService.findToolingListTypes());
    }

}
