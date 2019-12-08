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

import com.tcdng.jacklyn.common.entities.BaseInstallEntityQuery;

/**
 * Query class for menus.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ApplicationMenuQuery extends BaseInstallEntityQuery<ApplicationMenu> {

    public ApplicationMenuQuery() {
        super(ApplicationMenu.class);
    }

    @Override
    public ApplicationMenuQuery addOrder(String field) {
        return (ApplicationMenuQuery) super.addOrder(field);
    }

    public ApplicationMenuQuery moduleId(Long moduleId) {
        return (ApplicationMenuQuery) addEquals("moduleId", moduleId);
    }

    public ApplicationMenuQuery moduleName(String moduleName) {
        return (ApplicationMenuQuery) addEquals("moduleName", moduleName);
    }

    public ApplicationMenuQuery name(String name) {
        return (ApplicationMenuQuery) addEquals("name", name);
    }

    public ApplicationMenuQuery orderByDisplayOrder() {
        return (ApplicationMenuQuery) addOrder("displayOrder");
    }

    public ApplicationMenuQuery orderByModuleDesc() {
        return (ApplicationMenuQuery) addOrder("moduleDesc");
    }
}
