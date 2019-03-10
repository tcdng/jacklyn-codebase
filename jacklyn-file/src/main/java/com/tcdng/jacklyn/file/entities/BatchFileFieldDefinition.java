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
package com.tcdng.jacklyn.file.entities;

import com.tcdng.jacklyn.common.entities.BaseVersionedEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.constant.PadDirection;

/**
 * Batch file field definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "JKBATCHFILEFLDDEF", uniqueConstraints = { @UniqueConstraint({ "batchFileDefinitionId", "name" }) })
public class BatchFileFieldDefinition extends BaseVersionedEntity {

    @ForeignKey(BatchFileDefinition.class)
    private Long batchFileDefinitionId;

    @Column(name = "BATCHFILEFIELD_NM")
    private String name;

    @Column(nullable = true)
    private String mappedField;

    @Column(nullable = true)
    private Character padChar;

    @Column(nullable = true)
    private PadDirection padDirection;

    @Column(nullable = true)
    private String formatter;

    @Column(name = "FIELD_LEN")
    private int length;

    @Column(name = "FIELD_INDEX")
    private int index;

    @Column(name = "PAD_FG")
    private boolean pad;

    @Column(name = "TRIM_FG")
    private boolean trim;

    @Column
    private boolean updateOnConstraint;

    @Override
    public String getDescription() {
        return name;
    }

    public Long getBatchFileDefinitionId() {
        return batchFileDefinitionId;
    }

    public void setBatchFileDefinitionId(Long batchFileDefinitionId) {
        this.batchFileDefinitionId = batchFileDefinitionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMappedField() {
        return mappedField;
    }

    public void setMappedField(String mappedField) {
        this.mappedField = mappedField;
    }

    public Character getPadChar() {
        return padChar;
    }

    public void setPadChar(Character padChar) {
        this.padChar = padChar;
    }

    public PadDirection getPadDirection() {
        return padDirection;
    }

    public void setPadDirection(PadDirection padDirection) {
        this.padDirection = padDirection;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isPad() {
        return pad;
    }

    public void setPad(boolean pad) {
        this.pad = pad;
    }

    public boolean isTrim() {
        return trim;
    }

    public void setTrim(boolean trim) {
        this.trim = trim;
    }

    public boolean isUpdateOnConstraint() {
        return updateOnConstraint;
    }

    public void setUpdateOnConstraint(boolean updateOnConstraint) {
        this.updateOnConstraint = updateOnConstraint;
    }
}
