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
package com.tcdng.jacklyn.report.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.report.entities.ReportableDefinition;
import com.tcdng.jacklyn.report.entities.ReportableDefinitionQuery;
import com.tcdng.jacklyn.report.web.beans.ReportableDefinitionPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing report definition records.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/report/reportdefinition")
@UplBinding("web/report/upl/managereportdefinitions.upl")
public class ReportDefinitionController
        extends AbstractReportFormController<ReportableDefinitionPageBean, ReportableDefinition> {

    public ReportDefinitionController() {
        super(ReportableDefinitionPageBean.class, ReportableDefinition.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<ReportableDefinition> find() throws UnifyException {
        ReportableDefinitionPageBean pageBean = getPageBean();
        ReportableDefinitionQuery query = new ReportableDefinitionQuery();
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchModuleId())) {
            query.moduleId(pageBean.getSearchModuleId());
        }
        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.installed(Boolean.TRUE);
        query.ignoreEmptyCriteria(true);
        return getReportService().findReportables(query);
    }

    @Override
    protected ReportableDefinition find(Long id) throws UnifyException {
        return getReportService().findReportDefinition(id);
    }

    @Override
    protected ReportableDefinition prepareCreate() throws UnifyException {
        return null;
    }

    @Override
    protected Object create(ReportableDefinition reportableDefinitionData) throws UnifyException {
        return null;
    }

    @Override
    protected int update(ReportableDefinition reportableDefinitionData) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(ReportableDefinition reportableDefinitionData) throws UnifyException {
        return 0;
    }
}
