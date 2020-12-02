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

package com.tcdng.jacklyn.workflow.web.beans;

import com.tcdng.jacklyn.common.web.beans.BasePageBean;
import com.tcdng.jacklyn.workflow.data.ManualWfItem;
import com.tcdng.unify.web.ui.widget.data.LinkGridInfo;

/**
 * Manual work item initiation page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ManualWorkItemInitiationPageBean extends BasePageBean {

    private LinkGridInfo wfTemplateGridInfo;

    private ManualWfItem manualInitItem;

    private String templateName;

    private int mode;

    public ManualWorkItemInitiationPageBean() {
        super("manualWorkItemFrame");
    }

    public LinkGridInfo getWfTemplateGridInfo() {
        return wfTemplateGridInfo;
    }

    public void setWfTemplateGridInfo(LinkGridInfo wfTemplateGridInfo) {
        this.wfTemplateGridInfo = wfTemplateGridInfo;
    }

    public ManualWfItem getManualInitItem() {
        return manualInitItem;
    }

    public void setManualInitItem(ManualWfItem manualInitItem) {
        this.manualInitItem = manualInitItem;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

}
