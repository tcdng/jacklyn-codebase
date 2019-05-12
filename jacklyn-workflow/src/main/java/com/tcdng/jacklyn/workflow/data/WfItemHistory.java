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
package com.tcdng.jacklyn.workflow.data;

import java.io.Serializable;
import java.util.List;

/**
 * Workflow item history.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfItemHistory implements Serializable {

    private static final long serialVersionUID = -4226588442406967392L;

    private Long id;

    private Long docId;

    private String docGlobalName;

    private String templateGlobalName;

    private String description;

    private List<WfItemHistEvent> eventList;

    public WfItemHistory(Long id, Long docId, String docGlobalName, String templateGlobalName,
            String description, List<WfItemHistEvent> eventList) {
        this.id = id;
        this.docId = docId;
        this.docGlobalName = docGlobalName;
        this.templateGlobalName = templateGlobalName;
        this.description = description;
        this.eventList = eventList;
    }

    public Long getId() {
        return id;
    }

    public Long getDocId() {
        return docId;
    }

    public String getDocGlobalName() {
        return docGlobalName;
    }

    public void setDocGlobalName(String docGlobalName) {
        this.docGlobalName = docGlobalName;
    }

    public String getTemplateGlobalName() {
        return templateGlobalName;
    }

    public void setTemplateGlobalName(String templateGlobalName) {
        this.templateGlobalName = templateGlobalName;
    }

    public String getDescription() {
        return description;
    }

    public List<WfItemHistEvent> getEventList() {
        return eventList;
    }
}
