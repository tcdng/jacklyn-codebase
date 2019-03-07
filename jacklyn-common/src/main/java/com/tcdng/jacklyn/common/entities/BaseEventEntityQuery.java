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
package com.tcdng.jacklyn.common.entities;

import java.util.Date;

import com.tcdng.unify.core.util.CalendarUtils;

/**
 * Query class for base event entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class BaseEventEntityQuery<T extends BaseEventEntity> extends BaseEntityQuery<T> {

    public BaseEventEntityQuery(Class<T> entityClass) {
        super(entityClass);
    }

    public BaseEventEntityQuery<T> createdOn(Date date) {
        return (BaseEventEntityQuery<T>) between("createDt", CalendarUtils.getMidnightDate(date),
                CalendarUtils.getLastSecondDate(date));
    }

    public BaseEventEntityQuery<T> createdBetween(Date fromDate, Date toDate) {
        return (BaseEventEntityQuery<T>) between("createDt", CalendarUtils.getMidnightDate(fromDate),
                CalendarUtils.getLastSecondDate(toDate));
    }

    public BaseEventEntityQuery<T> createdBefore(Date date) {
        return (BaseEventEntityQuery<T>) less("createDt", date);
    }

    public BaseEventEntityQuery<T> createdOnBefore(Date date) {
        return (BaseEventEntityQuery<T>) lessEqual("createDt", date);
    }

    public BaseEventEntityQuery<T> createdAfter(Date date) {
        return (BaseEventEntityQuery<T>) greater("createDt", date);
    }

    public BaseEventEntityQuery<T> createdOnAfter(Date date) {
        return (BaseEventEntityQuery<T>) greaterEqual("createDt", date);
    }
}
