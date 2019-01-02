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
import com.tcdng.jacklyn.shared.system.SystemParamType;
import com.tcdng.jacklyn.system.entities.SystemParameter;
import com.tcdng.jacklyn.system.entities.SystemParameterQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing system parameters.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/sysparameter")
@UplBinding("web/system/upl/managesysparameter.upl")
public class SystemParameterController extends AbstractSystemCrudController<SystemParameter> {

    private Long searchModuleId;

    private SystemParamType searchType;

    private String searchName;

    private String searchDescription;

    public SystemParameterController() {
        super(SystemParameter.class, "$m{system.sysparameter.hint}", ManageRecordModifier.SECURE | ManageRecordModifier.VIEW
                | ManageRecordModifier.MODIFY | ManageRecordModifier.REPORTABLE);
    }

    public Long getSearchModuleId() {
        return searchModuleId;
    }

    public void setSearchModuleId(Long searchModuleId) {
        this.searchModuleId = searchModuleId;
    }

    public SystemParamType getSearchType() {
        return searchType;
    }

    public void setSearchType(SystemParamType searchType) {
        this.searchType = searchType;
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

    @Override
    protected void onSetPage() throws UnifyException {
        super.onSetPage();
        setEditable("tablePanel", false);
        setEditable("frmName", false);
        setEditable("frmDescription", false);
        setEditable("frmType", false);
    }

    @Override
    protected List<SystemParameter> find() throws UnifyException {
        SystemParameterQuery query = new SystemParameterQuery();
        if (QueryUtils.isValidLongCriteria(searchModuleId)) {
            query.moduleId(searchModuleId);
        }

        if (searchType != null) {
            query.type(searchType);
        }

        if (QueryUtils.isValidStringCriteria(searchName)) {
            query.nameLike(searchName);
        }

        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }
        query.order("description").ignoreEmptyCriteria(true);
        return getSystemService().findSysParameters(query);
    }

    @Override
    protected SystemParameter find(Long id) throws UnifyException {
        return getSystemService().findSysParameter(id);
    }

    @Override
    protected SystemParameter prepareCreate() throws UnifyException {
        return null;
    }

    @Override
    protected Object create(SystemParameter sysParameterData) throws UnifyException {
        return null;
    }

    @Override
    protected int update(SystemParameter sysParameterData) throws UnifyException {
        return getSystemService().setSysParameterValue(sysParameterData.getName(), sysParameterData.getValue());
    }

    @Override
    protected int delete(SystemParameter sysParameterData) throws UnifyException {
        return 0;
    }

    @Override
    protected boolean isEditable(SystemParameter sysParameterData) throws UnifyException {
        return sysParameterData.getEditable();
    }
}
