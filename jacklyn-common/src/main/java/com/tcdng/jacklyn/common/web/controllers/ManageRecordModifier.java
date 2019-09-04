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
package com.tcdng.jacklyn.common.web.controllers;

/**
 * Base CRUD controller manage record modifier.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public final class ManageRecordModifier {
    public static final int SEARCH = 0x00000001;
    public static final int ADD = 0x00000002;
    public static final int VIEW = 0x00000004;
    public static final int MODIFY = 0x00000008;
    public static final int DELETE = 0x00000010;
    public static final int ACTIVATABLE = 0x00000020;
    public static final int SECURE = 0x00000040;
    public static final int COPY = 0x00000080;
    public static final int PASTE = 0x00000100;
    public static final int COPY_TO_ADD = 0x00000200;
    public static final int REPORTABLE = 0x00000400;
    public static final int SEARCH_ON_OPEN = 0x00000800;
    public static final int ALTERNATE_SAVE = 0x00001000;
    public static final int INIT_DATE_RANGE = 0x00002000;
    public static final int LIMIT_DATE_RANGE = 0x00003000;

    public static final int CRUD = ADD | VIEW | MODIFY | DELETE;
    public static final int CLIPBOARD = COPY | PASTE;

    public static boolean isSearchable(int modifier) {
        return (SEARCH & modifier) != 0;
    }

    public static boolean isAddable(int modifier) {
        return (ADD & modifier) != 0;
    }

    public static boolean isViewable(int modifier) {
        return (VIEW & modifier) != 0;
    }

    public static boolean isEditable(int modifier) {
        return (MODIFY & modifier) != 0;
    }

    public static boolean isDeletable(int modifier) {
        return (DELETE & modifier) != 0;
    }

    public static boolean isActivatable(int modifier) {
        return (ACTIVATABLE & modifier) != 0;
    }

    public static boolean isSecure(int modifier) {
        return (SECURE & modifier) != 0;
    }

    public static boolean isCopyable(int modifier) {
        return (COPY & modifier) != 0;
    }

    public static boolean isPastable(int modifier) {
        return (PASTE & modifier) != 0;
    }

    public static boolean isCopyToAdd(int modifier) {
        return (COPY_TO_ADD & modifier) != 0;
    }

    public static boolean isReportable(int modifier) {
        return (REPORTABLE & modifier) != 0;
    }

    public static boolean isAlternateSave(int modifier) {
        return (ALTERNATE_SAVE & modifier) != 0;
    }

    public static boolean isInitDateRange(int modifier) {
        return (INIT_DATE_RANGE & modifier) != 0;
    }

    public static boolean isLimitDateRange(int modifier) {
        return (LIMIT_DATE_RANGE & modifier) != 0;
    }
}
