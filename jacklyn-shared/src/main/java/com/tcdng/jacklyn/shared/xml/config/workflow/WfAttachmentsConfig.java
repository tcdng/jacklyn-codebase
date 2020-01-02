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

package com.tcdng.jacklyn.shared.xml.config.workflow;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * Workflow document attachments configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class WfAttachmentsConfig {

    private List<WfAttachmentConfig> wfAttachmentConfigList;

    public List<WfAttachmentConfig> getWfAttachmentConfigList() {
        return wfAttachmentConfigList;
    }

    @XmlElement(name = "attachment", required = true)
    public void setWfAttachmentConfigList(List<WfAttachmentConfig> wfAttachmentConfigList) {
        this.wfAttachmentConfigList = wfAttachmentConfigList;
    }

}
