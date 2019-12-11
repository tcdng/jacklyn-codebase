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
import com.tcdng.jacklyn.system.entities.DataSourceDriver;
import com.tcdng.jacklyn.system.entities.DataSourceDriverQuery;
import com.tcdng.jacklyn.system.web.beans.DataSourceDriverPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Page controller for managing data source drivers.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/datasourcedriver")
@UplBinding("web/system/upl/managedatasourcedriver.upl")
public class DataSourceDriverController
        extends AbstractSystemFormController<DataSourceDriverPageBean, DataSourceDriver> {

    public DataSourceDriverController() {
        super(DataSourceDriverPageBean.class, DataSourceDriver.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.CLIPBOARD
                        | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected Object create(DataSourceDriver datasourceDriver) throws UnifyException {
        return getSystemService().createDataSourceDriver(datasourceDriver);
    }

    @Override
    protected int delete(DataSourceDriver datasourceDriver) throws UnifyException {
        return getSystemService().deleteDataSourceDriver(datasourceDriver.getId());
    }

    @Override
    protected List<DataSourceDriver> find() throws UnifyException {
        DataSourceDriverPageBean pageBean = getPageBean();
        DataSourceDriverQuery query = new DataSourceDriverQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchCode())) {
            query.name(pageBean.getSearchCode());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }

        query.addOrder("description").ignoreEmptyCriteria(true);
        return getSystemService().findDataSourceDrivers(query);
    }

    @Override
    protected DataSourceDriver find(Long dataSourceDriverId) throws UnifyException {
        return getSystemService().findDataSourceDriver(dataSourceDriverId);
    }

    @Override
    protected DataSourceDriver prepareCreate() throws UnifyException {
        return new DataSourceDriver();
    }

    @Override
    protected int update(DataSourceDriver datasourceDriver) throws UnifyException {
        return getSystemService().updateDataSourceDriver(datasourceDriver);
    }
}
