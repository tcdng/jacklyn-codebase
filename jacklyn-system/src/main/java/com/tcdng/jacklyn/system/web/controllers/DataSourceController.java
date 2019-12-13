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
import com.tcdng.jacklyn.system.constants.SystemDataSourceTaskConstants;
import com.tcdng.jacklyn.system.constants.SystemDataSourceTaskParamConstants;
import com.tcdng.jacklyn.system.entities.DataSource;
import com.tcdng.jacklyn.system.entities.DataSourceQuery;
import com.tcdng.jacklyn.system.web.beans.DataSourcePageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.constant.BooleanType;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;

/**
 * Page controller for managing data sources.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/datasource")
@UplBinding("web/system/upl/managedatasource.upl")
public class DataSourceController extends AbstractSystemFormController<DataSourcePageBean, DataSource> {

    public DataSourceController() {
        super(DataSourcePageBean.class, DataSource.class, ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Action
    public String testDataSource() throws UnifyException {
        DataSourcePageBean pageBean = getPageBean();
        TaskSetup taskSetup =
                TaskSetup.newBuilder().addTask(SystemDataSourceTaskConstants.DATASOURCETESTTASK)
                        .setParam(SystemDataSourceTaskParamConstants.DATASOURCE, pageBean.getRecord()).logMessages()
                        .build();
        return launchTaskWithMonitorBox(taskSetup, "$m{system.datasource.test}");
    }

    @Override
    protected Object create(DataSource dataSource) throws UnifyException {
        return getSystemService().createDataSource(dataSource);
    }

    @Override
    protected int delete(DataSource dataSource) throws UnifyException {
        return getSystemService().deleteDataSource(dataSource.getId());
    }

    @Override
    protected List<DataSource> find() throws UnifyException {
        DataSourcePageBean pageBean = getPageBean();
        DataSourceQuery query = new DataSourceQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchCode())) {
            query.name(pageBean.getSearchCode());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }

        if (pageBean.getSearchAppReserved() != null) {
            query.appReserved(pageBean.getSearchAppReserved());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }

        query.addOrder("description").ignoreEmptyCriteria(true);
        return getSystemService().findDataSources(query);
    }

    @Override
    protected DataSource find(Long dataSourceId) throws UnifyException {
        return getSystemService().findDataSource(dataSourceId);
    }

    @Override
    protected DataSource prepareCreate() throws UnifyException {
        return new DataSource();
    }

    @Override
    protected int update(DataSource dataSource) throws UnifyException {
        return getSystemService().updateDataSource(dataSource);
    }

    @Override
    protected void onPrepareCrudViewer(DataSource dataSource, int mode) throws UnifyException {
        setPageWidgetVisible("frmAppReserved", ManageRecordModifier.ADD != mode);
        if (ManageRecordModifier.isEditable(mode)) {
            setCrudViewerEditable(!BooleanType.TRUE.equals(dataSource.getAppReserved()));
        }
    }
}
