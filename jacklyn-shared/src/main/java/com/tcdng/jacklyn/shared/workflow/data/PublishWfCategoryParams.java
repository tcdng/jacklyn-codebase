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

package com.tcdng.jacklyn.shared.workflow.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tcdng.jacklyn.shared.workflow.WorkflowRemoteCallNameConstants;
import com.tcdng.unify.web.RemoteCallParams;

/**
 * Publish workflow category request parameters.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@XmlRootElement
public class PublishWfCategoryParams extends RemoteCallParams {

    private byte[] wfCategoryXml;

    private boolean activate;

    public PublishWfCategoryParams() {
        this(null, false);
    }

    public PublishWfCategoryParams(byte[] wfCategoryXml, boolean activate) {
        super(WorkflowRemoteCallNameConstants.PUBLISH_WORKFLOW_CATEGORY);
        this.wfCategoryXml = wfCategoryXml;
        this.activate = activate;
    }

    public byte[] getWfCategoryXml() {
        return wfCategoryXml;
    }

    @XmlElement
    public void setWfCategoryXml(byte[] wfCategoryXml) {
        this.wfCategoryXml = wfCategoryXml;
    }

    public boolean isActivate() {
        return activate;
    }

    @XmlElement
    public void setActivate(boolean activate) {
        this.activate = activate;
    }

}
