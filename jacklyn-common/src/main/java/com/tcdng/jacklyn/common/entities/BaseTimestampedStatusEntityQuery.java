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
 * Query class for base time-stamped status entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class BaseTimestampedStatusEntityQuery<T extends BaseTimestampedStatusEntity>
        extends BaseStatusEntityQuery<T> {

    public BaseTimestampedStatusEntityQuery(Class<T> entityClass) {
        super(entityClass);
    }

    public BaseTimestampedStatusEntityQuery<T> createdOn(Date date) {
        return (BaseTimestampedStatusEntityQuery<T>) between("createDt", CalendarUtils.getMidnightDate(date),
                CalendarUtils.getLastSecondDate(date));
    }

    public BaseTimestampedStatusEntityQuery<T> createdBetween(Date fromDate, Date toDate) {
        return (BaseTimestampedStatusEntityQuery<T>) between("createDt", CalendarUtils.getMidnightDate(fromDate),
                CalendarUtils.getLastSecondDate(toDate));
    }

    public BaseTimestampedStatusEntityQuery<T> createdBefore(Date date) {
        return (BaseTimestampedStatusEntityQuery<T>) less("createDt", date);
    }

    public BaseTimestampedStatusEntityQuery<T> createdOnBefore(Date date) {
        return (BaseTimestampedStatusEntityQuery<T>) lessEqual("createDt", date);
    }

    public BaseTimestampedStatusEntityQuery<T> createdAfter(Date date) {
        return (BaseTimestampedStatusEntityQuery<T>) greater("createDt", date);
    }

    public BaseTimestampedStatusEntityQuery<T> createdOnAfter(Date date) {
        return (BaseTimestampedStatusEntityQuery<T>) greaterEqual("createDt", date);
    }
}
