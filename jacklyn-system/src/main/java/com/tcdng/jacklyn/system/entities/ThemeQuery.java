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

import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntityQuery;

/**
 * Query class for themes.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ThemeQuery extends BaseVersionedStatusEntityQuery<Theme> {

    public ThemeQuery() {
        super(Theme.class);
    }

    @Override
    public ThemeQuery order(String field) {
        return (ThemeQuery) super.order(field);
    }

    @Override
    public ThemeQuery select(String field) {
        return (ThemeQuery) super.select(field);
    }

    public ThemeQuery name(String name) {
        return (ThemeQuery) equals("name", name);
    }

    public ThemeQuery descriptionLike(String description) {
        return (ThemeQuery) like("description", description);
    }
}
