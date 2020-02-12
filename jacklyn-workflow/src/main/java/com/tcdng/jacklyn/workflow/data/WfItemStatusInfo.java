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

package com.tcdng.jacklyn.workflow.data;

import com.tcdng.unify.web.ui.data.BadgeInfo;

/**
 * Workflow item status information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfItemStatusInfo {

    private Long wfItemId;

    private String wfTemplateDesc;

    private String wfDocDesc;

    private String wfStepDesc;

    private String wfItemDesc;

    private String forwardedBy;

    private String priority;

    private BadgeInfo status;

    public WfItemStatusInfo(Long wfItemId, String wfTemplateDesc, String wfDocDesc, String wfStepDesc,
            String wfItemDesc, String forwardedBy, String priority, BadgeInfo status) {
        this.wfItemId = wfItemId;
        this.wfTemplateDesc = wfTemplateDesc;
        this.wfDocDesc = wfDocDesc;
        this.wfStepDesc = wfStepDesc;
        this.wfItemDesc = wfItemDesc;
        this.forwardedBy = forwardedBy;
        this.priority = priority;
        this.status = status;
    }

    public Long getWfItemId() {
        return wfItemId;
    }

    public String getWfTemplateDesc() {
        return wfTemplateDesc;
    }

    public String getWfDocDesc() {
        return wfDocDesc;
    }

    public String getWfStepDesc() {
        return wfStepDesc;
    }

    public String getWfItemDesc() {
        return wfItemDesc;
    }

    public String getForwardedBy() {
        return forwardedBy;
    }

    public String getPriority() {
        return priority;
    }

    public BadgeInfo getStatus() {
        return status;
    }

}
