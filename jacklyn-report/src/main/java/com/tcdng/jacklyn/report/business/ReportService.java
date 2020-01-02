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
package com.tcdng.jacklyn.report.business;

import java.util.List;

import com.tcdng.jacklyn.common.business.JacklynBusinessService;
import com.tcdng.jacklyn.common.business.ReportProvider;
import com.tcdng.jacklyn.common.data.ReportOptions;
import com.tcdng.jacklyn.report.entities.ReportConfiguration;
import com.tcdng.jacklyn.report.entities.ReportableDefinition;
import com.tcdng.jacklyn.report.entities.ReportableDefinitionQuery;
import com.tcdng.unify.core.UnifyException;

/**
 * Report business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface ReportService extends JacklynBusinessService, ReportProvider {

    /**
     * Finds reportable by query.
     * 
     * @param query
     *            the reportable query
     * @return a list of reportable data
     * @throws UnifyException
     *             if an error occurs
     */
    List<ReportableDefinition> findReportables(ReportableDefinitionQuery query) throws UnifyException;

    /**
     * Finds a reportable by ID.
     * 
     * @param id
     *            the reportable ID
     * @return the reportable
     * @throws UnifyException
     *             if definition with ID is not found
     */
    ReportableDefinition findReportDefinition(Long id) throws UnifyException;

    /**
     * Finds static reportables by role and module.
     * 
     * @param moduleId
     *            the module ID
     * @return list of reportable data
     * @throws UnifyException
     *             if an error occurs
     */
    List<ReportableDefinition> findRoleReportables(Long moduleId) throws UnifyException;

    /**
     * Gets configured report listing based on user role.
     * 
     * @param roleCode
     *            the role code. Assumes super user if role is null.
     * @return the report listing
     * @throws UnifyException
     *             if an error occurs
     */
    List<ReportConfiguration> getRoleReportListing(String roleCode) throws UnifyException;

    /**
     * Get report options for configured report.
     * 
     * @param reportConfigName
     *            the report configuration name
     * @return the report options object
     * @throws UnifyException
     *             if an error occurs
     */
    ReportOptions getReportOptionsForConfiguration(String reportConfigName) throws UnifyException;

    /**
     * Populates extra report options data that depend on input parameters.
     * 
     * @param reportOptions
     *            the report options to populate
     * @throws UnifyException
     *             if an error occurs
     */
    void populateExtraConfigurationReportOptions(ReportOptions reportOptions) throws UnifyException;
}
