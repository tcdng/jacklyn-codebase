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

package com.tcdng.jacklyn.file.web.beans;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;
import com.tcdng.jacklyn.file.entities.FileTransferConfig;
import com.tcdng.jacklyn.shared.file.FileTransferDirection;

/**
 * file transfer configuration page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class FileTransferConfigPageBean extends BaseEntityPageBean<FileTransferConfig> {

    private String searchName;

    private FileTransferDirection searchDirection;

    private RecordStatus searchStatus;

    public FileTransferConfigPageBean() {
        super("$m{file.filetransferconfig.hint}");
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public FileTransferDirection getSearchDirection() {
        return searchDirection;
    }

    public void setSearchDirection(FileTransferDirection searchDirection) {
        this.searchDirection = searchDirection;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

}
