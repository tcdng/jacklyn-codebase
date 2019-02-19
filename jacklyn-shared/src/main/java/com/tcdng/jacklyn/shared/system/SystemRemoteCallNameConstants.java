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
package com.tcdng.jacklyn.shared.system;

/**
 * System module remote gate name constants.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface SystemRemoteCallNameConstants {

    String GET_APPLICATION_INFO = "sysGetAppInfo";

    String GET_APPLICATION_MENU = "sysGetAppMenu";

    String GET_APPLICATION_MODULES = "sysGetAppModules";

    String GET_TOOLING_BASE_TYPES = "sysGetToolingBaseTypes";

    String GET_TOOLING_RECORD_TYPES = "sysGetToolingRecordTypes";
    
    String GET_TOOLING_LIST_TYPES = "sysGetToolingListTypes";
    
    String GET_TOOLING_TRANSFORMER_TYPES = "sysGetToolingTransformerTypes";
}
