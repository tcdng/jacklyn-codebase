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
package com.tcdng.jacklyn.system.entities;

import com.tcdng.jacklyn.common.entities.BaseEntityQuery;
import com.tcdng.jacklyn.shared.system.SystemParamType;

/**
 * Query class for system parameters.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class SystemParameterQuery extends BaseEntityQuery<SystemParameter> {

    public SystemParameterQuery() {
        super(SystemParameter.class);
    }

    public SystemParameterQuery moduleId(Long moduleId) {
        return (SystemParameterQuery) addEquals("moduleId", moduleId);
    }

    public SystemParameterQuery moduleName(String moduleName) {
        return (SystemParameterQuery) addEquals("moduleName", moduleName);
    }

    public SystemParameterQuery name(String name) {
        return (SystemParameterQuery) addEquals("name", name);
    }

    public SystemParameterQuery nameLike(String name) {
        return (SystemParameterQuery) addLike("name", name);
    }

    public SystemParameterQuery descriptionLike(String description) {
        return (SystemParameterQuery) addLike("description", description);
    }

    public SystemParameterQuery type(SystemParamType type) {
        return (SystemParameterQuery) addEquals("type", type);
    }

    public SystemParameterQuery control(Boolean control) {
        return (SystemParameterQuery) addEquals("control", control);
    }
}
