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

import java.util.Collection;

import com.tcdng.jacklyn.common.entities.BaseInstallEntityQuery;

/**
 * Query class for shortcut tiles.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ShortcutTileQuery extends BaseInstallEntityQuery<ShortcutTile> {

    public ShortcutTileQuery() {
        super(ShortcutTile.class);
    }

    public ShortcutTileQuery moduleId(Long moduleId) {
        return (ShortcutTileQuery) addEquals("moduleId", moduleId);
    }

    public ShortcutTileQuery name(String name) {
        return (ShortcutTileQuery) addEquals("name", name);
    }

    public ShortcutTileQuery nameIn(Collection<String> name) {
        return (ShortcutTileQuery) addAmongst("name", name);
    }

    public ShortcutTileQuery orderByDisplayOrder() {
        return (ShortcutTileQuery) addOrder("displayOrder");
    }
}
