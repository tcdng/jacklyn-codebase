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
package com.tcdng.jacklyn.location.entities;

import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntityQuery;

/**
 * Query class for zones.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ZoneQuery extends BaseVersionedStatusEntityQuery<Zone> {

    public ZoneQuery() {
        super(Zone.class);
    }

    public ZoneQuery name(String name) {
        return (ZoneQuery) addEquals("name", name);
    }

    public ZoneQuery nameLike(String name) {
        return (ZoneQuery) addLike("name", name);
    }

    public ZoneQuery descriptionLike(String description) {
        return (ZoneQuery) addLike("description", description);
    }
}
