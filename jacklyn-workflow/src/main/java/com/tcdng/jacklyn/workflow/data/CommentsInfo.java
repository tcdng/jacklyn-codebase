/*
 * Copyright 2018-2020 The Code Department.
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

import java.util.List;

/**
 * Comments information data object.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class CommentsInfo {

    private List<WfItemHistEvent> commentsHistEventList;

    private String comment;

    private String applyActionCaption;

    private boolean required;

    public List<WfItemHistEvent> getCommentsHistEventList() {
        return commentsHistEventList;
    }

    public void setCommentsHistEventList(List<WfItemHistEvent> commentsHistEventList) {
        this.commentsHistEventList = commentsHistEventList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
