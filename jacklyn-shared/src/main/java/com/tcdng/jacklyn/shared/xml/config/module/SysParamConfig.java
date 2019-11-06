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
package com.tcdng.jacklyn.shared.xml.config.module;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.jacklyn.shared.system.SystemParamType;
import com.tcdng.jacklyn.shared.xml.adapter.SystemParamTypeXmlAdapter;
import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * System parameter configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class SysParamConfig extends BaseConfig {

    private SystemParamType type;

    private String editor;

    private String defaultVal;

    private boolean control;

    private boolean editable;

    public SysParamConfig() {
        type = SystemParamType.STRING;
        editable = true;
    }

    public SystemParamType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(SystemParamTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(SystemParamType type) {
        if (type != null) {
            this.type = type;
        }
    }

    public String getEditor() {
        return editor;
    }

    @XmlAttribute(required = true)
    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    @XmlAttribute(required = true)
    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public boolean isControl() {
        return control;
    }

    @XmlAttribute
    public void setControl(boolean control) {
        this.control = control;
    }

    public boolean isEditable() {
        return editable;
    }

    @XmlAttribute
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
