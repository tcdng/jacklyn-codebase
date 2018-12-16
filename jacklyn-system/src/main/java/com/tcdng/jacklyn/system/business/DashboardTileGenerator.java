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
package com.tcdng.jacklyn.system.business;

import com.tcdng.jacklyn.system.entities.DashboardTile;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.ui.Tile;

/**
 * Interface for dynamically generating a dashboard tile.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface DashboardTileGenerator extends UnifyComponent {

    /**
     * Generates a tile using supplied dashboard tile data.
     * 
     * @param dashboardTileData
     *            the dashboard data
     * @return a user interface tile
     * @throws UnifyException
     *             if an error occurs
     */
    Tile generate(DashboardTile dashboardTileData) throws UnifyException;
}
