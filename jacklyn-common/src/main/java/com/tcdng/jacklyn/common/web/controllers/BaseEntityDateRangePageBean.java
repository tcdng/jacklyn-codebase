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

import java.util.Date;

import com.tcdng.jacklyn.common.entities.BaseEntity;

/**
 * Base entity date range page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class BaseEntityDateRangePageBean<T extends BaseEntity> extends BaseEntityPageBean<T> {

    private Date searchFromDate;

    private Date searchToDate;

    public BaseEntityDateRangePageBean(String recordHintName) {
        super(recordHintName);
    }

    public Date getSearchFromDate() {
        return searchFromDate;
    }

    public void setSearchFromDate(Date searchFromDate) {
        this.searchFromDate = searchFromDate;
    }

    public Date getSearchToDate() {
        return searchToDate;
    }

    public void setSearchToDate(Date searchToDate) {
        this.searchToDate = searchToDate;
    }

}
