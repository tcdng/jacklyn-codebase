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

package com.tcdng.jacklyn.common.business;

import java.io.OutputStream;
import java.util.List;

import com.tcdng.jacklyn.common.data.ReportOptions;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.report.ReportColumn;

/**
 * Report provider component.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface ReportProvider extends UnifyComponent {

	/**
	 * Creates a new report option for dynamic reportable associated with supplied
	 * record type.
	 * 
	 * @param recordName
	 *            the record type canonical name
	 * @param priorityPropertyList
	 *            the priority property list if any
	 * @return the new report options
	 * @throws UnifyException
	 *             if an error occurs
	 */
	ReportOptions getDynamicReportOptions(String recordName, List<String> priorityPropertyList)
			throws UnifyException;

	/**
	 * Generates a dynamic report based on supplied report options.
	 * 
	 * @param reportOptions
	 *            the report options
	 * @param outputStream
	 *            the output stream where generated report is written to
	 * @throws UnifyException
	 *             if an error occurs
	 */
	void generateDynamicReport(ReportOptions reportOptions, OutputStream outputStream)
			throws UnifyException;

	/**
	 * Finds report columns of a reportable.
	 * 
	 * @param reportableName
	 *            the reportable code
	 * @return the report columns
	 * @throws UnifyException
	 *             if an error occurs
	 */
	ReportColumn[] findReportableColumns(String reportableName) throws UnifyException;

	/**
	 * Returns true if record type is reportable by this. provider.
	 * 
	 * @param recordName
	 *            the record type canonical name
	 * @throws UnifyException
	 *             if an error occurs
	 */
	boolean isReportable(String recordName) throws UnifyException;
}
