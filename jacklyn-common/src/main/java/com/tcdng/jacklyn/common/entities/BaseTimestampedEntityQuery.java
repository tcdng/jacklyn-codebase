/*
 * Copyright 2018-2019 The Code Department.
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
 * Query class for base time-stamped entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class BaseTimestampedEntityQuery<T extends BaseTimestampedEntity> extends BaseEventEntityQuery<T> {

    public BaseTimestampedEntityQuery(Class<T> entityClass) {
        super(entityClass);
    }

    public BaseTimestampedEntityQuery<T> updateBy(String updateBy) {
        return (BaseTimestampedEntityQuery<T>) addEquals("updateBy", updateBy);
    }

    public BaseTimestampedEntityQuery<T> updatedOn(Date date) {
        return (BaseTimestampedEntityQuery<T>) addBetween("updateDt", CalendarUtils.getMidnightDate(date),
                CalendarUtils.getLastSecondDate(date));
    }

    public BaseTimestampedEntityQuery<T> updatedBetween(Date fromDate, Date toDate) {
        return (BaseTimestampedEntityQuery<T>) addBetween("updateDt", CalendarUtils.getMidnightDate(fromDate),
                CalendarUtils.getLastSecondDate(toDate));
    }

    public BaseTimestampedEntityQuery<T> updatedBefore(Date date) {
        return (BaseTimestampedEntityQuery<T>) addLessThan("updateDt", date);
    }

    public BaseTimestampedEntityQuery<T> updatedOnBefore(Date date) {
        return (BaseTimestampedEntityQuery<T>) addLessThanEqual("updateDt", date);
    }

    public BaseTimestampedEntityQuery<T> updatedAfter(Date date) {
        return (BaseTimestampedEntityQuery<T>) addGreaterThan("updateDt", date);
    }

    public BaseTimestampedEntityQuery<T> updatedOnAfter(Date date) {
        return (BaseTimestampedEntityQuery<T>) addGreaterThanEqual("updateDt", date);
    }
}
