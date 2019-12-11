/*
 * Copyright 2018-2019 The Code Department
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

package com.tcdng.jacklyn.system.web.beans;

import java.util.List;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;
import com.tcdng.jacklyn.system.entities.ShortcutTile;

/**
 * Shortcut tile entity page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ShortcutTilePageBean extends BaseEntityPageBean<ShortcutTile> {

    private Long searchModuleId;

    private RecordStatus searchStatus;
    
    private List<ShortcutTile> shortcutTileOrderList;

    public ShortcutTilePageBean() {
        super("$m{system.shortcuttile.hint}");
    }

    public Long getSearchModuleId() {
        return searchModuleId;
    }

    public void setSearchModuleId(Long searchModuleId) {
        this.searchModuleId = searchModuleId;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public List<ShortcutTile> getShortcutTileOrderList() {
        return shortcutTileOrderList;
    }

    public void setShortcutTileOrderList(List<ShortcutTile> shortcutTileOrderList) {
        this.shortcutTileOrderList = shortcutTileOrderList;
    }

}
