/*
 * Copyright 2018 The Code Department
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
public class WfItemHistObject implements Serializable {

    private static final long serialVersionUID = -4226588442406967392L;

    private Long id;

    private Long documentId;

    private String globalDocName;

    private String globalTemplateName;

    private String description;

    private List<WfItemHistEvent> eventList;

    public WfItemHistObject(Long id, Long documentId, String globalDocName, String globalTemplateName,
            String description, List<WfItemHistEvent> eventList) {
        this.id = id;
        this.documentId = documentId;
        this.globalDocName = globalDocName;
        this.globalTemplateName = globalTemplateName;
        this.description = description;
        this.eventList = eventList;
    }

    public Long getId() {
        return id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public String getGlobalDocName() {
        return globalDocName;
    }

    public void setGlobalDocName(String globalDocName) {
        this.globalDocName = globalDocName;
    }

    public String getGlobalTemplateName() {
        return globalTemplateName;
    }

    public void setGlobalTemplateName(String globalTemplateName) {
        this.globalTemplateName = globalTemplateName;
    }

    public String getDescription() {
        return description;
    }

    public List<WfItemHistEvent> getEventList() {
        return eventList;
    }
}
