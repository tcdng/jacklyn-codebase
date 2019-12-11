/*
 * Copyright 2018-2019 The Code Department
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

package com.tcdng.jacklyn.common.web.controllers;

import java.util.List;

import com.tcdng.unify.core.database.Entity;

/**
 * Base entity page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class BaseEntityPageBean<T extends Entity> extends BasePageBean {

    private String itemCountLabel;

    private String modeDescription;

    private String modeStyle;

    private String reportType;

    private String noRecordMessage;

    private String recordHintName;

    private List<T> recordList;

    private T record;

    private T clipRecord;

    private T oldRecord;

    private int mode;

    private boolean prefetch;

    public BaseEntityPageBean(String recordHintName) {
        super("manageRecordPanel");
        noRecordMessage = "$m{common.report.norecordintable}";
        prefetch = true;
        this.recordHintName = recordHintName;
    }

    public String getItemCountLabel() {
        return itemCountLabel;
    }

    public void setItemCountLabel(String itemCountLabel) {
        this.itemCountLabel = itemCountLabel;
    }

    public String getModeDescription() {
        return modeDescription;
    }

    public void setModeDescription(String modeDescription) {
        this.modeDescription = modeDescription;
    }

    public String getModeStyle() {
        return modeStyle;
    }

    public void setModeStyle(String modeStyle) {
        this.modeStyle = modeStyle;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getNoRecordMessage() {
        return noRecordMessage;
    }

    public void setNoRecordMessage(String noRecordMessage) {
        this.noRecordMessage = noRecordMessage;
    }

    public String getRecordHintName() {
        return recordHintName;
    }

    public void setRecordHintName(String recordHintName) {
        this.recordHintName = recordHintName;
    }

    public List<T> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<T> recordList) {
        this.recordList = recordList;
    }

    public T getRecord() {
        return record;
    }

    public void setRecord(T record) {
        this.record = record;
    }

    public T getClipRecord() {
        return clipRecord;
    }

    public void setClipRecord(T clipRecord) {
        this.clipRecord = clipRecord;
    }

    public T getOldRecord() {
        return oldRecord;
    }

    public void setOldRecord(T oldRecord) {
        this.oldRecord = oldRecord;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isPrefetch() {
        return prefetch;
    }

    public void setPrefetch(boolean prefetch) {
        this.prefetch = prefetch;
    }

}
