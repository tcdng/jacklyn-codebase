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

import com.tcdng.jacklyn.shared.archiving.ArchivingFieldType;
import com.tcdng.jacklyn.shared.xml.adapter.ArchivingFieldTypeXmlAdapter;
import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Document field configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class FieldConfig extends BaseConfig {

    private String type;

    private ArchivingFieldType archFieldType;

    private String formatter;

    private String list;

    private String hAlign;

    private int width;

    private boolean parameterOnly;

    private boolean mask;

    private boolean auditable;

    private boolean archivable;

    private boolean reportable;

    public FieldConfig() {
        this.width = -1;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArchivingFieldType getArchFieldType() {
        return archFieldType;
    }

    @XmlJavaTypeAdapter(ArchivingFieldTypeXmlAdapter.class)
    @XmlAttribute(name = "archtype", required = true)
    public void setArchFieldType(ArchivingFieldType archFieldType) {
        this.archFieldType = archFieldType;
    }

    public String getFormatter() {
        return formatter;
    }

    @XmlAttribute
    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public String getList() {
        return list;
    }

    @XmlAttribute
    public void setList(String list) {
        this.list = list;
    }

    public String gethAlign() {
        return hAlign;
    }

    @XmlAttribute
    public void sethAlign(String hAlign) {
        this.hAlign = hAlign;
    }

    public int getWidth() {
        return width;
    }

    @XmlAttribute
    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isParameterOnly() {
        return parameterOnly;
    }

    @XmlAttribute
    public void setParameterOnly(boolean parameterOnly) {
        this.parameterOnly = parameterOnly;
    }

    public boolean isMask() {
        return mask;
    }

    @XmlAttribute
    public void setMask(boolean mask) {
        this.mask = mask;
    }

    public boolean isAuditable() {
        return auditable;
    }

    public void setAuditable(boolean auditable) {
        this.auditable = auditable;
    }

    public boolean isArchivable() {
        return archivable;
    }

    public void setArchivable(boolean archivable) {
        this.archivable = archivable;
    }

    public boolean isReportable() {
        return reportable;
    }

    public void setReportable(boolean reportable) {
        this.reportable = reportable;
    }
}
