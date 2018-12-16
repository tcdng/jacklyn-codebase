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
package com.tcdng.jacklyn.shared.xml.config.workflow;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.util.xml.adapter.FileAttachmentTypeXmlAdapter;

/**
 * Workflow document attachment configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class WfAttachmentConfig extends BaseConfig {

    private String label;

    private FileAttachmentType type;

    public String getLabel() {
        return label;
    }

    @XmlAttribute
    public void setLabel(String label) {
        this.label = label;
    }

    public FileAttachmentType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(FileAttachmentTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(FileAttachmentType type) {
        this.type = type;
    }

}
