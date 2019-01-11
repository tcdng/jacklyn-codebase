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

package com.tcdng.jacklyn.shared.system.data;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * Tooling record type item.
 * 
 * @author Lateef
 * @since 1.0
 */
public class ToolingRecordTypeItem {

    private String title;

    private String type;

    private String idField;

    private List<String> fieldList;

    public ToolingRecordTypeItem(String title, String type, String idField, List<String> fieldList) {
        this.title = title;
        this.type = type;
        this.idField = idField;
        this.fieldList = fieldList;
    }

    public ToolingRecordTypeItem() {

    }

    public String getTitle() {
        return title;
    }

    @XmlElement
    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    @XmlElement
    public void setType(String type) {
        this.type = type;
    }

    public String getIdField() {
        return idField;
    }

    @XmlElement
    public void setIdField(String idField) {
        this.idField = idField;
    }

    public List<String> getFieldList() {
        return fieldList;
    }

    @XmlElement(name = "field")
    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }
}
