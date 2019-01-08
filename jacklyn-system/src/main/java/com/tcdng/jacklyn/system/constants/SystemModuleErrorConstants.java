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
package com.tcdng.jacklyn.system.constants;

/**
 * System module errors.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface SystemModuleErrorConstants {

    /** Module [{0}] is not deactivatable */
    String MODULE_DATA_NOT_DEACTIVATABLE = "SYSTEM_0001";

    /** Unknown module referenced. Managed name = {0}. */
    String MODULE_UNKNOWN_MODULE_NAME_REFERENCED = "SYSTEM_0002";

    /**
     * MenuItemSet with name {0} is unknown. Referencing menu item name {1} and
     * definition class is {2}
     */
    String MENU_WITH_NAME_UNKNOWN = "SYSTEM_0003";

    /** System parameter with name {0} is unknown. */
    String SYSPARAM_WITH_NAME_UNKNOWN = "SYSTEM_0004";

    /**
     * Multiple module configurations with module name {0} found. Only extension can
     * use existing name.
     */
    String MODULE_MULTIPLE_CONFIGURATIONS_WITH_NAME = "SYSTEM_0005";

    /** Application is unknown. Application name = {0} */
    String APPLICATION_UNKNOWN = "SYSTEM_0006";

    /** Application as no such asset. Application name = {0}, Asset name = {1} */
    String APPLICATION_NO_SUCH_ASSET = "SYSTEM_0007";

    /** Application is inactive. Application name = {0} */
    String APPLICATION_INACTIVE = "SYSTEM_0008";

    /** Application asset is inactive. Application name = {0}, Asset name = {1} */
    String APPLICATION_ASSET_INACTIVE = "SYSTEM_0009";

    /** Dashboard with name {0} is unknown*/
    String DASHBOARD_NAME_UNKNOWN = "SYSTEM_000A";
}
