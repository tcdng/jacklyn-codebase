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
package com.tcdng.jacklyn.shared.notification;

import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Notification type constants.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@StaticList("notificationtypelist")
public enum NotificationType implements EnumConst {

    SYSTEM("X", true), EMAIL("E", false), SMS("S", false);

    private final String code;

    private final boolean internal;
    
    private NotificationType(String code, boolean internal) {
        this.code = code;
        this.internal = internal;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String defaultCode() {
        return SYSTEM.code;
    }

    public boolean internal() {
        return internal;
    }

    public static NotificationType fromCode(String code) {
        return EnumUtils.fromCode(NotificationType.class, code);
    }

    public static NotificationType fromName(String name) {
        return EnumUtils.fromName(NotificationType.class, name);
    }
}
