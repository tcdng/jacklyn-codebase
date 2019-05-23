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

import com.tcdng.jacklyn.common.business.JacklynBusinessService;
import com.tcdng.jacklyn.datasource.entities.DataSource;
import com.tcdng.jacklyn.datasource.entities.DataSourceDriver;
import com.tcdng.jacklyn.datasource.entities.DataSourceDriverQuery;
import com.tcdng.jacklyn.datasource.entities.DataSourceQuery;
import com.tcdng.unify.core.UnifyException;

/**
 * DataSource business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface DataSourceService extends JacklynBusinessService {

    /**
     * Creates a new datasource driver.
     * 
     * @param dataSourceDriver
     *            the data source driver data
     * @return the created data source driver ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createDataSourceDriver(DataSourceDriver dataSourceDriver) throws UnifyException;

    /**
     * Finds datasource driver by ID.
     * 
     * @param dataSourceDriverId
     *            the data source driver ID
     * @return the data source driver data
     * @throws UnifyException
     *             if data source driver with ID is not found
     */
    DataSourceDriver findDataSourceDriver(Long dataSourceDriverId) throws UnifyException;

    /**
     * Finds datasource drivers by query.
     * 
     * @param query
     *            the datasource driver query
     * @return the list of data source drivers found
     * @throws UnifyException
     *             if an error occurs
     */
    List<DataSourceDriver> findDataSourceDrivers(DataSourceDriverQuery query) throws UnifyException;

    /**
     * Updates a datasource driver.
     * 
     * @param dataSourceDriver
     *            the datasource driver data
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateDataSourceDriver(DataSourceDriver dataSourceDriver) throws UnifyException;

    /**
     * Deletes a datasource driver.
     * 
     * @param dataSourceDriverId
     *            the data source driver ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteDataSourceDriver(Long dataSourceDriverId) throws UnifyException;

    /**
     * Creates a new datasource.
     * 
     * @param dataSource
     *            the data source data
     * @return the created data source ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createDataSource(DataSource dataSource) throws UnifyException;

    /**
     * Finds datasource by ID.
     * 
     * @param dataSourceId
     *            the data source ID
     * @return the datasource data
     * @throws UnifyException
     *             if data source with ID is not found
     */
    DataSource findDataSource(Long dataSourceId) throws UnifyException;

    /**
     * Finds datasources by query.
     * 
     * @param query
     *            the datasource query
     * @return the list of datasources found
     * @throws UnifyException
     *             if an error occurs
     */
    List<DataSource> findDataSources(DataSourceQuery query) throws UnifyException;

    /**
     * Updates a datasource.
     * 
     * @param dataSource
     *            the datasource record
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateDataSource(DataSource dataSource) throws UnifyException;

    /**
     * Deletes a datasource.
     * 
     * @param dataSourceId
     *            the datasource ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteDataSource(Long dataSourceId) throws UnifyException;

    /**
     * Activates datasource in application datasource manager for use if not already
     * activated.
     * 
     * @param dataSourceName
     *            the datasource name
     * @return a true value if activated otherwise false when already activated
     * @throws UnifyException
     *             if an error occurs
     */
    boolean activateDataSource(String dataSourceName) throws UnifyException;

    /**
     * Activates datasource in application datasource manager for use if not already
     * activated.
     * 
     * @param dataSourceId
     *            the datasource ID
     * @return the datasource name
     * @throws UnifyException
     *             if an error occurs
     */
    String activateDataSource(Long dataSourceId) throws UnifyException;
}
