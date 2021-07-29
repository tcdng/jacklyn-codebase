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
package com.tcdng.jacklyn.shared.archiving;

import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Archiving field type.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@StaticList(name = "archivingfieldtypelist", description = "Archiving Field Type List")
public enum ArchivingFieldType implements EnumConst {

    BLOB(ColumnType.BLOB.code()),
    CLOB(ColumnType.CLOB.code()),
    BOOLEAN(ColumnType.BOOLEAN.code()),
    DATE(ColumnType.DATE.code()),
    TIMESTAMP_UTC(ColumnType.TIMESTAMP_UTC.code()),
    TIMESTAMP(ColumnType.TIMESTAMP.code());

    private final String code;

    private ArchivingFieldType(String code) {
        this.code = code;
    }

    public boolean isLob() {
        return BLOB.equals(this) || CLOB.equals(this);
    }

    public boolean isTimestamp() {
        return DATE.equals(this) || TIMESTAMP_UTC.equals(this) || TIMESTAMP.equals(this);
    }

    public boolean isIndicator() {
        return BOOLEAN.equals(this);
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return BLOB.code;
    }

    public static ArchivingFieldType fromCode(String code) {
        return EnumUtils.fromCode(ArchivingFieldType.class, code);
    }

    public static ArchivingFieldType fromName(String name) {
        return EnumUtils.fromName(ArchivingFieldType.class, name);
    }
}
