/*
 * Copyright 2018-2020 The Code Department
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

import java.util.Date;

import com.tcdng.jacklyn.common.web.beans.BasePageBean;
import com.tcdng.unify.core.logging.EventType;

/**
 * File transfer page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class FileTransferPageBean extends BasePageBean {

    private Long fileTransferConfigId;

    private Date workingDt;

    public FileTransferPageBean() {
        super("basePanel");
    }

    public Long getFileTransferConfigId() {
        return fileTransferConfigId;
    }

    public void setFileTransferConfigId(Long fileTransferConfigId) {
        this.fileTransferConfigId = fileTransferConfigId;
    }

    public Date getWorkingDt() {
        return workingDt;
    }

    public void setWorkingDt(Date workingDt) {
        this.workingDt = workingDt;
    }

    public String getModeStyle() {
        return EventType.CREATE.colorMode();
    }

}
