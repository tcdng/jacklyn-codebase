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

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.data.MessageIcon;
import com.tcdng.unify.web.ui.data.MessageMode;

/**
 * Convenient abstract base class for tab date range CRUD controllers.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class BaseEntityDateRangeTabController<T extends BaseEntityDateRangePageBean<U>, U extends BaseEntity>
        extends BaseEntityTabController<T, Long, U> {

    public BaseEntityDateRangeTabController(Class<T> pageBeanClass, Class<U> entityClass, int modifier) {
        super(pageBeanClass, entityClass, modifier);
    }

    @Action
    public String findRecords() throws UnifyException {
        if (ManageRecordModifier.isLimitDateRange(getModifier())) {
            BaseEntityDateRangePageBean<U> baseEntityDateRangePageBean = getPageBean();
            Date searchFromDate = baseEntityDateRangePageBean.getSearchFromDate();
            Date searchToDate = baseEntityDateRangePageBean.getSearchToDate();
            int searchLimitDays = 0;
            if (searchFromDate == null || searchToDate == null || CalendarUtils.getDaysDifference(searchFromDate,
                    searchToDate) > (searchLimitDays = getNormalizedSearchDateRangeLimitDays())) {
                return showMessageBox(MessageIcon.ERROR, MessageMode.OK,
                        this.resolveSessionMessage("$m{managerecord.hint.daterangebeyondlimit}", searchLimitDays));
            }
        }

        return super.findRecords();
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        if (ManageRecordModifier.isInitDateRange(getModifier())) {
            BaseEntityDateRangePageBean<U> baseEntityDateRangePageBean = getPageBean();
            if (baseEntityDateRangePageBean.getSearchFromDate() == null) {
                Date now = CalendarUtils.getMidnightDate(new Date());
                baseEntityDateRangePageBean.setSearchFromDate(now);
                baseEntityDateRangePageBean.setSearchToDate(now);
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
