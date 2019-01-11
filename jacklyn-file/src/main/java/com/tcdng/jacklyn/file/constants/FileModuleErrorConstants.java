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
package com.tcdng.jacklyn.file.constants;

/**
 * File module errors.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface FileModuleErrorConstants {

    /** File transfer configuration with name {0} is unknown */
    String FILETRANSFERCONFIG_NAME_UNKNOWN = "FILE_0001";

    /** File transfer configuration invalid local path. [{0}] */
    String FILETRANSFERCONFIG_INVALID_LOCALPATH = "FILE_0002";

    /** Batch upload configuration with name {0} is unknown */
    String BATCHUPLOADCONFIG_NAME_UNKNOWN = "FILE_0003";

    /** Bulk file definition with name {0} is unknown */
    String BATCHFILEDEFINITION_NAME_UNKNOWN = "FILE_0004";

}
