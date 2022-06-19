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
package com.tcdng.jacklyn.shared.file;

import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * File inbox read status constants.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@StaticList(name = "fileinboxreadlist", description = "File Inbox Read List")
public enum FileInboxReadStatus implements EnumConst {

    NOT_READ("N"), READ("R"), READ_FAILED("F");

    private final String code;

    private FileInboxReadStatus(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return NOT_READ.code;
    }

    public static FileInboxReadStatus fromCode(String code) {
        return EnumUtils.fromCode(FileInboxReadStatus.class, code);
    }

    public static FileInboxReadStatus fromName(String name) {
        return EnumUtils.fromName(FileInboxReadStatus.class, name);
    }
}
