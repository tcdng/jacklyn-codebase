/*
 * Copyright 2018-2020 The Code Department.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * UnaddLessThan required by applicable law or agreed to in writing, software
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

    public BaseTimestampedStatusEntityQuery<T> createBy(String createBy) {
        return (BaseTimestampedStatusEntityQuery<T>) addEquals("createBy", createBy);
    }

    public BaseTimestampedStatusEntityQuery<T> createdOn(Date date) {
        return (BaseTimestampedStatusEntityQuery<T>) addBetween("createDt", CalendarUtils.getMidnightDate(date),
                CalendarUtils.getLastSecondDate(date));
    }

    public BaseTimestampedStatusEntityQuery<T> createdBetween(Date fromDate, Date toDate) {
        return (BaseTimestampedStatusEntityQuery<T>) addBetween("createDt", CalendarUtils.getMidnightDate(fromDate),
                CalendarUtils.getLastSecondDate(toDate));
    }

    public BaseTimestampedStatusEntityQuery<T> createdBefore(Date date) {
        return (BaseTimestampedStatusEntityQuery<T>) addLessThan("createDt", date);
    }

    public BaseTimestampedStatusEntityQuery<T> createdOnBefore(Date date) {
        return (BaseTimestampedStatusEntityQuery<T>) addLessThanEqual("createDt", date);
    }

    public BaseTimestampedStatusEntityQuery<T> createdAfter(Date date) {
        return (BaseTimestampedStatusEntityQuery<T>) addGreaterThan("createDt", date);
    }

    public BaseTimestampedStatusEntityQuery<T> createdOnAfter(Date date) {
        return (BaseTimestampedStatusEntityQuery<T>) addGreaterThanEqual("createDt", date);
    }
    
    public BaseTimestampedStatusEntityQuery<T> updateBy(String updateBy) {
        return (BaseTimestampedStatusEntityQuery<T>) addEquals("updateBy", updateBy);
    }

    public BaseTimestampedStatusEntityQuery<T> updatedOn(Date date) {
        return (BaseTimestampedStatusEntityQuery<T>) addBetween("updateDt", CalendarUtils.getMidnightDate(date),
                CalendarUtils.getLastSecondDate(date));
    }

    public BaseTimestampedStatusEntityQuery<T> updatedBetween(Date fromDate, Date toDate) {
        return (BaseTimestampedStatusEntityQuery<T>) addBetween("updateDt", CalendarUtils.getMidnightDate(fromDate),
                CalendarUtils.getLastSecondDate(toDate));
    }

    public BaseTimestampedStatusEntityQuery<T> updatedBefore(Date date) {
        return (BaseTimestampedStatusEntityQuery<T>) addLessThan("updateDt", date);
    }

    public BaseTimestampedStatusEntityQuery<T> updatedOnBefore(Date date) {
        return (BaseTimestampedStatusEntityQuery<T>) addLessThanEqual("updateDt", date);
    }

    public BaseTimestampedStatusEntityQuery<T> updatedAfter(Date date) {
        return (BaseTimestampedStatusEntityQuery<T>) addGreaterThan("updateDt", date);
    }

    public BaseTimestampedStatusEntityQuery<T> updatedOnAfter(Date date) {
        return (BaseTimestampedStatusEntityQuery<T>) addGreaterThanEqual("updateDt", date);
    }
    
}
