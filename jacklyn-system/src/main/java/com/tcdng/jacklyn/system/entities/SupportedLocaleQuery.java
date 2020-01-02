/*
 * Copyright 2018-2020 The Code Department.
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
 * Query class for supported locale.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class SupportedLocaleQuery extends BaseVersionedStatusEntityQuery<SupportedLocale> {

    public SupportedLocaleQuery() {
        super(SupportedLocale.class);
    }

    @Override
    public SupportedLocaleQuery addOrder(String field) {
        return (SupportedLocaleQuery) super.addOrder(field);
    }

    @Override
    public SupportedLocaleQuery addSelect(String field) {
        return (SupportedLocaleQuery) super.addSelect(field);
    }

    public SupportedLocaleQuery name(String name) {
        return (SupportedLocaleQuery) addEquals("name", name);
    }

    public SupportedLocaleQuery descriptionLike(String description) {
        return (SupportedLocaleQuery) addLike("description", description);
    }
}
