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
@StaticList("archivingfieldtypelist")
public enum ArchivingFieldType implements EnumConst {

    BLOB(ColumnType.BLOB.code(), true, false), CLOB(ColumnType.CLOB.code(), true, false), DATE(ColumnType.DATE.code(),
            false, true), TIMESTAMP(ColumnType.TIMESTAMP.code(), false, true);

    private final String code;

    private final boolean lob;

    private final boolean timestamp;

    private ArchivingFieldType(String code, boolean lob, boolean timestamp) {
        this.code = code;
        this.lob = lob;
        this.timestamp = timestamp;
    }

    public boolean isLob() {
        return lob;
    }

    public boolean isTimestamp() {
        return timestamp;
    }

    @Override
    public String code() {
        return this.code;
    }

    public static ArchivingFieldType fromCode(String code) {
        return EnumUtils.fromCode(ArchivingFieldType.class, code);
    }

    public static ArchivingFieldType fromName(String name) {
        return EnumUtils.fromName(ArchivingFieldType.class, name);
    }
}
