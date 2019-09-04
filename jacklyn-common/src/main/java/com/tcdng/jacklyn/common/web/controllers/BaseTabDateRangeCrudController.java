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
package com.tcdng.jacklyn.common.web.controllers;

import java.util.Date;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.data.MessageIcon;
import com.tcdng.unify.web.ui.data.MessageMode;

/**
 * Convenient abstract base class for form date range CRUD controllers.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class BaseTabDateRangeCrudController<T extends Entity> extends BaseTabCrudController<T, Long> {

    private Date searchFromDate;

    private Date searchToDate;

    public BaseTabDateRangeCrudController(Class<T> entityClass, String hint, int modifier) {
        super(entityClass, hint, modifier);
    }

    @Action
    public String findRecords() throws UnifyException {
        if (ManageRecordModifier.isLimitDateRange(getModifier())) {
            int searchLimitDays = 0;
            if (searchFromDate == null || searchToDate == null || CalendarUtils.getDaysDifference(searchFromDate,
                    searchToDate) > (searchLimitDays = getNormalizedSearchDateRangeLimitDays())) {
                return showMessageBox(MessageIcon.ERROR, MessageMode.OK,
                        this.resolveSessionMessage("$m{managerecord.hint.daterangebeyondlimit}", searchLimitDays));
            }
        }

        return super.findRecords();
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

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        
        if (ManageRecordModifier.isInitDateRange(getModifier())) {
            if (searchFromDate == null) {
                Date now = CalendarUtils.getMidnightDate(new Date());
                searchFromDate = now;
                searchToDate = now;
            }
        }
    }

    protected abstract int getSearchDateRangeLimitDays() throws UnifyException;

    private int getNormalizedSearchDateRangeLimitDays() throws UnifyException {
        int searchLimitDays = getSearchDateRangeLimitDays();
        if (searchLimitDays <= 0) {
            return 1;
        }

        return searchLimitDays;
    }
}