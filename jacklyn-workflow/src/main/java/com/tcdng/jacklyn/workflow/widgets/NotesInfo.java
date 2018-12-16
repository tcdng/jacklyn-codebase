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
package com.tcdng.jacklyn.workflow.widgets;

import java.util.List;

import com.tcdng.jacklyn.workflow.data.WfItemHistEvent;

/**
 * Notes information data object.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class NotesInfo {

    private List<WfItemHistEvent> notesHistEventList;

    private String notes;

    private String applyActionCaption;

    private boolean required;

    public List<WfItemHistEvent> getNotesHistEventList() {
        return notesHistEventList;
    }

    public void setNotesHistEventList(List<WfItemHistEvent> notesHistEventList) {
        this.notesHistEventList = notesHistEventList;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getApplyActionCaption() {
        return applyActionCaption;
    }

    public void setApplyActionCaption(String applyActionCaption) {
        this.applyActionCaption = applyActionCaption;
    }
}
