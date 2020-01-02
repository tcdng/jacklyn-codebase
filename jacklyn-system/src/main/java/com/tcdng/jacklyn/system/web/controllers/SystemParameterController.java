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
import com.tcdng.jacklyn.system.entities.SystemParameter;
import com.tcdng.jacklyn.system.entities.SystemParameterQuery;
import com.tcdng.jacklyn.system.web.beans.SystemParameterPageBean;
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
public class SystemParameterController extends AbstractSystemFormController<SystemParameterPageBean, SystemParameter> {

    public SystemParameterController() {
        super(SystemParameterPageBean.class, SystemParameter.class, ManageRecordModifier.SECURE
                | ManageRecordModifier.VIEW | ManageRecordModifier.MODIFY | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected void onInitPage() throws UnifyException {
        super.onInitPage();

        setPageWidgetEditable("tablePanel", false);
        setPageWidgetEditable("frmName", false);
        setPageWidgetEditable("frmDescription", false);
        setPageWidgetEditable("frmType", false);
    }

    @Override
    protected List<SystemParameter> find() throws UnifyException {
        SystemParameterPageBean pageBean = getPageBean();
        SystemParameterQuery query = new SystemParameterQuery();
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchModuleId())) {
            query.moduleId(pageBean.getSearchModuleId());
        }

        if (pageBean.getSearchType() != null) {
            query.type(pageBean.getSearchType());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchName())) {
            query.nameLike(pageBean.getSearchName());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }
        query.addOrder("description").ignoreEmptyCriteria(true);
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
    protected Object create(SystemParameter sysParameter) throws UnifyException {
        return null;
    }

    @Override
    protected int update(SystemParameter sysParameter) throws UnifyException {
        return getSystemService().setSysParameterValue(sysParameter.getName(), sysParameter.getValue());
    }

    @Override
    protected int delete(SystemParameter sysParameter) throws UnifyException {
        return 0;
    }

    @Override
    protected boolean isEditable(SystemParameter sysParameter) throws UnifyException {
        return sysParameter.getEditable();
    }
}
