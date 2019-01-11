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
package com.tcdng.jacklyn.common.data;

import java.util.ArrayList;
import java.util.List;

import com.tcdng.unify.core.data.Input;

/**
 * Report options.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ReportOptions {

    private String reportName;

    private String recordName;

    private String reportFormat;

    private String reportResourcePath;

    private String dataSource;

    private String title;

    private String filename;

    private String query;

    private String tableName;

    private List<ReportColumnOptions> columnOptionsList;

    private List<ReportJoinOptions> joinOptionsList;

    private List<ReportFilterOptions> filterOptionsList;

    private List<?> content;

    private List<Input> userInputList;

    private List<Input> systemInputList;

    private boolean dynamicDataSource;

    private boolean printColumnNames;

    private boolean shadeOddRows;

    private boolean underlineRows;

    private boolean columnarLayout;

    private boolean landscape;

    private boolean download;

    public ReportOptions() {
        columnOptionsList = new ArrayList<ReportColumnOptions>();
        joinOptionsList = new ArrayList<ReportJoinOptions>();
        filterOptionsList = new ArrayList<ReportFilterOptions>();
        printColumnNames = true;
        shadeOddRows = true;
    }

    public void reset() {
        printColumnNames = true;
        shadeOddRows = true;
        underlineRows = false;
        landscape = false;
        download = false;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getReportFormat() {
        return reportFormat;
    }

    public void setReportFormat(String reportFormat) {
        this.reportFormat = reportFormat;
    }

    public String getReportResourcePath() {
        return reportResourcePath;
    }

    public void setReportResourcePath(String reportResourcePath) {
        this.reportResourcePath = reportResourcePath;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public List<?> getContent() {
        return content;
    }

    public void setContent(List<?> content) {
        this.content = content;
    }

    public boolean isBeanCollection() {
        return content != null;
    }

    public void addColumnOptions(ReportColumnOptions reportColumnOptions) {
        columnOptionsList.add(reportColumnOptions);
    }

    public List<ReportColumnOptions> getColumnOptionsList() {
        return columnOptionsList;
    }

    public void addJoinOptions(ReportJoinOptions reportJoinOptions) {
        joinOptionsList.add(reportJoinOptions);
    }

    public List<ReportJoinOptions> getJoinOptionsList() {
        return joinOptionsList;
    }

    public void addFilterOptions(ReportFilterOptions reportFilterOptions) {
        filterOptionsList.add(reportFilterOptions);
    }

    public List<ReportFilterOptions> getFilterOptionsList() {
        return filterOptionsList;
    }

    public List<Input> getUserInputList() {
        return userInputList;
    }

    public void setUserInputList(List<Input> userInputList) {
        this.userInputList = userInputList;
    }

    public List<Input> getSystemInputList() {
        return systemInputList;
    }

    public void setSystemInputList(List<Input> systemInputList) {
        this.systemInputList = systemInputList;
    }

    public boolean isUserInput() {
        return userInputList != null && !userInputList.isEmpty();
    }

    public boolean isColumnOptions() {
        return columnOptionsList != null && !columnOptionsList.isEmpty();
    }

    public boolean isJoinOptions() {
        return joinOptionsList != null && !joinOptionsList.isEmpty();
    }

    public boolean isFilterOptions() {
        return filterOptionsList != null && !filterOptionsList.isEmpty();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isDynamicDataSource() {
        return dynamicDataSource;
    }

    public void setDynamicDataSource(boolean dynamicDataSource) {
        this.dynamicDataSource = dynamicDataSource;
    }

    public boolean isPrintColumnNames() {
        return printColumnNames;
    }

    public void setPrintColumnNames(boolean printColumnNames) {
        this.printColumnNames = printColumnNames;
    }

    public boolean isShadeOddRows() {
        return shadeOddRows;
    }

    public void setShadeOddRows(boolean shadeOddRows) {
        this.shadeOddRows = shadeOddRows;
    }

    public boolean isUnderlineRows() {
        return underlineRows;
    }

    public void setUnderlineRows(boolean underlineRows) {
        this.underlineRows = underlineRows;
    }

    public boolean isColumnarLayout() {
        return columnarLayout;
    }

    public void setColumnarLayout(boolean columnarLayout) {
        this.columnarLayout = columnarLayout;
    }

    public boolean isLandscape() {
        return landscape;
    }

    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }
}
