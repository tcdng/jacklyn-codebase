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

package com.tcdng.jacklyn.shared.system.data;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.tcdng.jacklyn.shared.ToolingItem;

/**
 * Tooling entity type item.
 * 
 * @author Lateef
 * @since 1.0
 */
public class ToolingEntityItem implements ToolingItem {

    private String name;

    private String description;

    private String type;

    private String idField;

    private boolean guarded;

    private List<ToolingEntityFieldItem> fieldList;

    public ToolingEntityItem(String name, String description, String type, String idField,
            boolean guarded, List<ToolingEntityFieldItem> fieldList) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.idField = idField;
        this.guarded = guarded;
        this.fieldList = fieldList;
    }

    public ToolingEntityItem() {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description;
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

    public boolean isGuarded() {
        return guarded;
    }

    @XmlElement
    public void setGuarded(boolean guarded) {
        this.guarded = guarded;
    }

    public List<ToolingEntityFieldItem> getFieldList() {
        return fieldList;
    }

    @XmlElement(name = "field")
    public void setFieldList(List<ToolingEntityFieldItem> fieldList) {
        this.fieldList = fieldList;
    }
}
