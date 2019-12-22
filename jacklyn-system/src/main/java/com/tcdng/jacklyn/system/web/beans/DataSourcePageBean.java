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

package com.tcdng.jacklyn.system.web.beans;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;
import com.tcdng.jacklyn.system.entities.DataSource;
import com.tcdng.unify.core.constant.BooleanType;

/**
 * Datasource entity page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class DataSourcePageBean extends BaseEntityPageBean<DataSource> {

    private String searchCode;

    private String searchDescription;

    private BooleanType searchAppReserved;
    
    private RecordStatus searchStatus;

    public DataSourcePageBean() {
        super("$m{system.datasource.hint}");
    }

    public String getSearchCode() {
        return searchCode;
    }

    public void setSearchCode(String searchCode) {
        this.searchCode = searchCode;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public BooleanType getSearchAppReserved() {
        return searchAppReserved;
    }

    public void setSearchAppReserved(BooleanType searchAppReserved) {
        this.searchAppReserved = searchAppReserved;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

}