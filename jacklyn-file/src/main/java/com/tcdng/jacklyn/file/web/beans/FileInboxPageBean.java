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

import com.tcdng.jacklyn.file.entities.FileInbox;
import com.tcdng.jacklyn.shared.file.FileInboxReadStatus;
import com.tcdng.jacklyn.shared.file.FileInboxStatus;

/**
 * File inbox page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class FileInboxPageBean extends AbstractFileTransferBoxPageBean<FileInbox> {

    private FileInboxReadStatus searchReadStatus;

    private FileInboxStatus searchStatus;

    public FileInboxPageBean() {
        super("$m{file.fileinbox.hint}");
    }

    public FileInboxReadStatus getSearchReadStatus() {
        return searchReadStatus;
    }

    public void setSearchReadStatus(FileInboxReadStatus searchReadStatus) {
        this.searchReadStatus = searchReadStatus;
    }

    public FileInboxStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(FileInboxStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

}
