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

import java.util.List;

import com.tcdng.jacklyn.common.entities.BaseInstallEntityQuery;

/**
 * Query class for menu items.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ApplicationMenuItemQuery extends BaseInstallEntityQuery<ApplicationMenuItem> {

    public ApplicationMenuItemQuery() {
        super(ApplicationMenuItem.class);
    }

    public ApplicationMenuItemQuery moduleId(Long moduleId) {
        return (ApplicationMenuItemQuery) addEquals("moduleId", moduleId);
    }

    public ApplicationMenuItemQuery moduleName(String moduleName) {
        return (ApplicationMenuItemQuery) addEquals("moduleName", moduleName);
    }

    public ApplicationMenuItemQuery menuId(Long menuId) {
        return (ApplicationMenuItemQuery) addEquals("menuId", menuId);
    }

    public ApplicationMenuItemQuery parentMenuItemId(Long parentMenuItemId) {
        return (ApplicationMenuItemQuery) addEquals("parentMenuItemId", parentMenuItemId);
    }

    public ApplicationMenuItemQuery name(String name) {
        return (ApplicationMenuItemQuery) addEquals("name", name);
    }

    public ApplicationMenuItemQuery nameNotIn(List<String> names) {
        return (ApplicationMenuItemQuery) addNotAmongst("name", names);
    }

    public ApplicationMenuItemQuery hidden(Boolean hidden) {
        return (ApplicationMenuItemQuery) addEquals("hidden", hidden);
    }

    public ApplicationMenuItemQuery orderByDisplayOrder() {
        return (ApplicationMenuItemQuery) addOrder("displayOrder");
    }
}
