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

package com.tcdng.jacklyn.audit.web.beans;

import com.tcdng.jacklyn.audit.entities.AuditDefinition;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;
import com.tcdng.unify.core.logging.EventType;

/**
 * Audit setting page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class AuditSettingPageBean extends BaseEntityPageBean<AuditDefinition> {

    private Long searchModuleId;

    private EventType searchEventType;

    private RecordStatus searchStatus;

    public AuditSettingPageBean() {
        super("$m{audit.auditsettings.hint}");
    }

    public Long getSearchModuleId() {
        return searchModuleId;
    }

    public void setSearchModuleId(Long searchModuleId) {
        this.searchModuleId = searchModuleId;
    }

    public EventType getSearchEventType() {
        return searchEventType;
    }

    public void setSearchEventType(EventType searchEventType) {
        this.searchEventType = searchEventType;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

}
