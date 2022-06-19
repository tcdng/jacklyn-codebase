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

package com.tcdng.jacklyn.shared.notification;

import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Message type constants.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@StaticList(name = "messagetypelist", description = "Message Type List")
public enum MessageType implements EnumConst {

    INFORMATION("I", "$t{images/info.png}"),
    WARNING("W", "$t{images/warning.png}"),
    ACTION("A", "$t{images/takeaction.png}");

    private final String code;

    private final String icon;

    private MessageType(String code, String icon) {
        this.code = code;
        this.icon = icon;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String defaultCode() {
        return INFORMATION.code;
    }

    public String icon() {
        return icon;
    }

    public static MessageType fromCode(String code) {
        return EnumUtils.fromCode(MessageType.class, code);
    }

    public static MessageType fromName(String name) {
        return EnumUtils.fromName(MessageType.class, name);
    }
}
