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

package com.tcdng.jacklyn.audit.data;

import java.util.Date;
import java.util.List;

import com.tcdng.unify.core.logging.EventType;

/**
 * Inspect user audit trail item.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class InspectUserAuditItem {

    private String description;

    private String moduleName;

    private String ipAddress;

    private Date timestamp;

    private EventType eventType;

    private String actionDesc;

    private List<String> details;

    public InspectUserAuditItem(String description, String moduleName, String ipAddress, Date timestamp,
            EventType eventType, String actionDesc, List<String> details) {
        this.description = description;
        this.moduleName = moduleName;
        this.ipAddress = ipAddress;
        this.timestamp = timestamp;
        this.eventType = eventType;
        this.actionDesc = actionDesc;
        this.details = details;
    }

    public String getDescription() {
        return description;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getEventStyle() {
        return eventType.colorMode();
    }

    public String getActionDesc() {
        return actionDesc;
    }

    public List<String> getDetails() {
        return details;
    }

}
