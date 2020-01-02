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
package com.tcdng.jacklyn.common.constants;

/**
 * Common module error constants.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface CommonModuleErrorConstants {

    /** Unknown module {0} referenced by managed record {1} */
    String UNKNOWN_MODULE_REFERENCED_BY_RECORD = "COMMON_0001";

    /** Data item already in workflow. Description = {0} */
    String DATA_ITEM_IN_WORKFLOW = "COMMON_0002";

    /** Unknown managed type {0} in module {1}. */
    String UNKNOWN_MANAGED_TYPE = "COMMON_0003";

    /**
     * Application service access denied. Application name = {0}, Asset name = {1}
     */
    String APPLICATION_SERVICE_ACCESSDENIED = "COMMON_0004";
}
