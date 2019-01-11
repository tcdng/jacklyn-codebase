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
package com.tcdng.jacklyn.system.entities;

import com.tcdng.jacklyn.common.entities.BaseVersionedTimestampedStatusEntityQuery;

/**
 * Query class for dashboards.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class DashboardQuery extends BaseVersionedTimestampedStatusEntityQuery<Dashboard> {

    public DashboardQuery() {
        super(Dashboard.class);
    }

    @Override
    public DashboardQuery order(String field) {
        return (DashboardQuery) super.order(field);
    }

    @Override
    public DashboardQuery select(String field) {
        return (DashboardQuery) super.select(field);
    }

    public DashboardQuery name(String name) {
        return (DashboardQuery) equals("name", name);
    }

    public DashboardQuery nameLike(String name) {
        return (DashboardQuery) like("name", name);
    }

    public DashboardQuery descriptionLike(String description) {
        return (DashboardQuery) like("description", description);
    }
}
