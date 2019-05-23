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
package com.tcdng.jacklyn.datasource.business;

import java.util.List;

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.datasource.constants.DataSourceModuleNameConstants;
import com.tcdng.jacklyn.datasource.constants.DataSourceTaskConstants;
import com.tcdng.jacklyn.datasource.constants.DataSourceTaskParamConstants;
import com.tcdng.jacklyn.datasource.entities.DataSource;
import com.tcdng.jacklyn.datasource.entities.DataSourceDriver;
import com.tcdng.jacklyn.datasource.entities.DataSourceDriverQuery;
import com.tcdng.jacklyn.datasource.entities.DataSourceQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Taskable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.database.sql.DynamicSqlDataSourceConfig;
import com.tcdng.unify.core.database.sql.DynamicSqlDataSourceManager;
import com.tcdng.unify.core.task.TaskExecLimit;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Default implementation of dataSource business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(DataSourceModuleNameConstants.DATASOURCESERVICE)
public class DataSourceServiceImpl extends AbstractJacklynBusinessService implements DataSourceService {

    @Configurable
    private DynamicSqlDataSourceManager dataSourceManager;

    @Override
    public Long createDataSourceDriver(DataSourceDriver datasourceDriver) throws UnifyException {
        return (Long) db().create(datasourceDriver);
    }

    @Override
    public DataSourceDriver findDataSourceDriver(Long datasourceDriverId) throws UnifyException {
        return db().find(DataSourceDriver.class, datasourceDriverId);
    }

    @Override
    public List<DataSourceDriver> findDataSourceDrivers(DataSourceDriverQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateDataSourceDriver(DataSourceDriver datasourceDriver) throws UnifyException {
        return db().updateByIdVersion(datasourceDriver);
    }

    @Override
    public int deleteDataSourceDriver(Long id) throws UnifyException {
        return db().delete(DataSourceDriver.class, id);
    }

    @Override
    public Long createDataSource(DataSource dataSourceName) throws UnifyException {
        return (Long) db().create(dataSourceName);
    }

    @Override
    public DataSource findDataSource(Long dataSourceId) throws UnifyException {
        return db().find(DataSource.class, dataSourceId);
    }

    @Override
    public List<DataSource> findDataSources(DataSourceQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateDataSource(DataSource dataSource) throws UnifyException {
        int updateCount = db().updateByIdVersion(dataSource);
        if (dataSourceManager.isConfigured(dataSource.getName())) {
            dataSourceManager.reconfigure(getDynamicSqlDataSourceConfig(dataSource));
        }
        return updateCount;
    }

    @Override
    public int deleteDataSource(Long id) throws UnifyException {
        String name = db().value(String.class, "name", new DataSourceQuery().id(id));
        int updateCount = db().delete(DataSource.class, id);
        if (dataSourceManager.isConfigured(name)) {
            dataSourceManager.terminateConfiguration(name);
        }
        return updateCount;
    }

    @Override
    public boolean activateDataSource(String dataSourceName) throws UnifyException {
        if (!dataSourceManager.isConfigured(dataSourceName)) {
            DataSource dataSource = db().list(new DataSourceQuery().name(dataSourceName));
            dataSourceManager.configure(getDynamicSqlDataSourceConfig(dataSource));
            return true;
        }
        return false;
    }

    @Override
    public String activateDataSource(Long dataSourceId) throws UnifyException {
        DataSource dataSource = db().list(new DataSourceQuery().id(dataSourceId));
        if (!dataSourceManager.isConfigured(dataSource.getName())) {
            dataSourceManager.configure(getDynamicSqlDataSourceConfig(dataSource));
        }
        return dataSource.getName();
    }

    @Taskable(name = DataSourceTaskConstants.DATASOURCETESTTASK, description = "DataSource Test Task", parameters = {
            @Parameter(name = DataSourceTaskParamConstants.DATASOURCE, type = DataSource.class, mandatory = true) }, limit = TaskExecLimit.ALLOW_MULTIPLE)
    public boolean executeTestDataSourceTask(TaskMonitor taskMonitor, DataSource dataSource)
            throws UnifyException {
        boolean result = false;

        addTaskMessage(taskMonitor, "$m{datasource.datasource.taskmonitor.performing}");
        addTaskMessage(taskMonitor, "$m{datasource.datasource.taskmonitor.connecting}",
                dataSource.getConnectionUrl());
        DataSourceDriver driver = findDataSourceDriver(dataSource.getDataSourceDriverId());
        result = dataSourceManager.testConfiguration(new DynamicSqlDataSourceConfig(taskMonitor.getTaskId(0),
                driver.getDialect(), driver.getDriverType(), dataSource.getConnectionUrl(),
                dataSource.getUserName(), dataSource.getPassword(), 1, false));
        addTaskMessage(taskMonitor, "$m{datasource.datasource.taskmonitor.completed}", result);
        return result;
    }

    private DynamicSqlDataSourceConfig getDynamicSqlDataSourceConfig(DataSource dataSource) {
        return new DynamicSqlDataSourceConfig(dataSource.getName(), dataSource.getDialect(), dataSource.getDriverType(),
                dataSource.getConnectionUrl(), dataSource.getUserName(), dataSource.getPassword(), dataSource.getMaxConnections(),
                false);
    }
}
