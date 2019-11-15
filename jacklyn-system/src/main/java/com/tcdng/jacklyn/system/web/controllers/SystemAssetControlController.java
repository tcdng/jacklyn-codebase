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

import java.util.Collections;
import java.util.List;

import com.tcdng.jacklyn.shared.system.SystemAssetType;
import com.tcdng.jacklyn.shared.system.SystemRestrictionType;
import com.tcdng.jacklyn.system.entities.SystemAsset;
import com.tcdng.jacklyn.system.entities.SystemAssetQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.logging.EventType;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;

/**
 * System control controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/systemassetcontrol")
@UplBinding("web/system/upl/systemassetcontrol.upl")
@ResultMappings({
        @ResultMapping(name = "refreshresult", response = { "!refreshpanelresponse panels:$l{searchResultPanel}" }) })
public class SystemAssetControlController extends AbstractSystemPageController {

    private Long searchModuleId;

    private SystemAssetType searchAssetType;

    private List<SystemAsset> systemAssetList;

    public SystemAssetControlController() {
        super(true, false); // Secured and not read-only
    }

    @Action
    public String findAll() throws UnifyException {
        if (QueryUtils.isValidLongCriteria(searchModuleId)) {
            SystemAssetQuery query = new SystemAssetQuery().moduleId(searchModuleId);
            if (searchAssetType != null) {
                query.type(searchAssetType);
            }
            
            query.order("description");
            systemAssetList =
                    getSystemService()
                            .findSystemAssets(query);
        } else {
            systemAssetList = Collections.emptyList();
        }

        return "refreshresult";
    }

    @Action
    public String openAll() throws UnifyException {
        return flipAllRestriction(SystemRestrictionType.OPEN);
    }

    @Action
    public String restrictAll() throws UnifyException {
        return flipAllRestriction(SystemRestrictionType.RESTRICTED);
    }

    @Action
    public String closeAll() throws UnifyException {
        return flipAllRestriction(SystemRestrictionType.CLOSED);
    }

    @Action
    public String applyChanges() throws UnifyException {
        getSystemService().updateSystemAssetRestrictions(systemAssetList);
        hintUser("$m{system.systemaccesscontrol.restrictions.applied.hint}");
        return findAll();
    }

    @Action
    public String done() throws UnifyException {
        return closePage();
    }

    public String getModeStyle() {
        return EventType.UPDATE.colorMode();
    }

    public Long getSearchModuleId() {
        return searchModuleId;
    }

    public void setSearchModuleId(Long searchModuleId) {
        this.searchModuleId = searchModuleId;
    }

    public SystemAssetType getSearchAssetType() {
        return searchAssetType;
    }

    public void setSearchAssetType(SystemAssetType searchAssetType) {
        this.searchAssetType = searchAssetType;
    }

    public List<SystemAsset> getSystemAssetList() {
        return systemAssetList;
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        searchModuleId = null;
        searchAssetType = null;
        systemAssetList = null;
    }

    @Override
    protected void onClosePage() throws UnifyException {
        searchModuleId = null;
        searchAssetType = null;
        systemAssetList = null;
    }

    @Override
    protected String getDocViewPanelName() {
        return "systemAssetControlBasePanel";
    }

    private String flipAllRestriction(SystemRestrictionType restriction) {
        for (SystemAsset systemAsset : systemAssetList) {
            systemAsset.setRestriction(restriction);
        }

        return "refreshresult";
    }

}
