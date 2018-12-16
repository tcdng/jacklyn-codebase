/*
 * Copyright 2018 The Code Department
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
 * Query class for base time-stamped entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class BaseTimestampedEntityQuery<T extends BaseTimestampedEntity> extends BaseEntityQuery<T> {

    public BaseTimestampedEntityQuery(Class<T> entityClass) {
        super(entityClass);
    }

    public BaseTimestampedEntityQuery<T> createdOn(Date date) {
        return (BaseTimestampedEntityQuery<T>) between("createDt", CalendarUtils.getMidnightDate(date),
                CalendarUtils.getLastSecondDate(date));
    }

    public BaseTimestampedEntityQuery<T> createdBetween(Date fromDate, Date toDate) {
        return (BaseTimestampedEntityQuery<T>) between("createDt", CalendarUtils.getMidnightDate(fromDate),
                CalendarUtils.getLastSecondDate(toDate));
    }

    public BaseTimestampedEntityQuery<T> createdBefore(Date date) {
        return (BaseTimestampedEntityQuery<T>) less("createDt", date);
    }

    public BaseTimestampedEntityQuery<T> createdOnBefore(Date date) {
        return (BaseTimestampedEntityQuery<T>) lessEqual("createDt", date);
    }

    public BaseTimestampedEntityQuery<T> createdAfter(Date date) {
        return (BaseTimestampedEntityQuery<T>) greater("createDt", date);
    }

    public BaseTimestampedEntityQuery<T> createdOnAfter(Date date) {
        return (BaseTimestampedEntityQuery<T>) greaterEqual("createDt", date);
    }
}
