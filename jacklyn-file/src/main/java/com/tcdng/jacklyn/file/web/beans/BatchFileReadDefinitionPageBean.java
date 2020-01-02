/*
 * Copyright 2018-2020 The Code Department
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

package com.tcdng.jacklyn.file.web.beans;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;
import com.tcdng.jacklyn.file.data.BatchFileReadDefinitionLargeData;
import com.tcdng.jacklyn.file.entities.BatchFileReadDefinition;

/**
 * Batch file read definition page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class BatchFileReadDefinitionPageBean extends BaseEntityPageBean<BatchFileReadDefinition> {

    private String searchName;

    private String searchDescription;

    private RecordStatus searchStatus;

    private BatchFileReadDefinitionLargeData largeData;

    private BatchFileReadDefinitionLargeData clipboardLargeData;

    public BatchFileReadDefinitionPageBean() {
        super("$m{file.batchfilereaddefinition.hint}");
        largeData = new BatchFileReadDefinitionLargeData();
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public BatchFileReadDefinitionLargeData getLargeData() {
        return largeData;
    }

    public void setLargeData(BatchFileReadDefinitionLargeData largeData) {
        this.largeData = largeData;
    }

    public BatchFileReadDefinitionLargeData getClipboardLargeData() {
        return clipboardLargeData;
    }

    public void setClipboardLargeData(BatchFileReadDefinitionLargeData clipboardLargeData) {
        this.clipboardLargeData = clipboardLargeData;
    }

}
