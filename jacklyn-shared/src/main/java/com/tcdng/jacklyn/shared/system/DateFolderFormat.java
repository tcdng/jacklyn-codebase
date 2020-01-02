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
package com.tcdng.jacklyn.shared.system;

import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Date folder format constants.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@StaticList("datefolderformatlist")
public enum DateFolderFormat implements EnumConst {

    DDMMYYYY("ddMMyyyy"), MMDDYYYY("MMddyyyy"), YYYYMMDD("yyyyMMdd");

    private final String code;

    private DateFolderFormat(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return DDMMYYYY.code;
    }

    public static DateFolderFormat fromCode(String code) {
        return EnumUtils.fromCode(DateFolderFormat.class, code);
    }

    public static DateFolderFormat fromName(String name) {
        return EnumUtils.fromName(DateFolderFormat.class, name);
    }
}
