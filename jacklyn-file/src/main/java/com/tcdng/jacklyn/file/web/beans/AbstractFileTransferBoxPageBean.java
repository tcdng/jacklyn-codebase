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

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;

/**
 * Abstract base class for file transfer box.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractFileTransferBoxPageBean<T extends BaseEntity> extends BaseEntityPageBean<T> {

    private Date searchCreateDt;

    private Long searchFileTransferConfigId;

    public AbstractFileTransferBoxPageBean(String recordHintName) {
        super(recordHintName);
    }

    public Date getSearchCreateDt() {
        return searchCreateDt;
    }

    public void setSearchCreateDt(Date searchCreateDt) {
        this.searchCreateDt = searchCreateDt;
    }

    public Long getSearchFileTransferConfigId() {
        return searchFileTransferConfigId;
    }

    public void setSearchFileTransferConfigId(Long searchFileTransferConfigId) {
        this.searchFileTransferConfigId = searchFileTransferConfigId;
    }

}
