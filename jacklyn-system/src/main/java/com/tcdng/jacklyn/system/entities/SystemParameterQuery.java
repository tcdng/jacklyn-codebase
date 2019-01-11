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
        return (SystemParameterQuery) equals("moduleId", moduleId);
    }

    public SystemParameterQuery moduleName(String moduleName) {
        return (SystemParameterQuery) equals("moduleName", moduleName);
    }

    public SystemParameterQuery name(String name) {
        return (SystemParameterQuery) equals("name", name);
    }

    public SystemParameterQuery nameLike(String name) {
        return (SystemParameterQuery) like("name", name);
    }

    public SystemParameterQuery descriptionLike(String description) {
        return (SystemParameterQuery) like("description", description);
    }

    public SystemParameterQuery type(SystemParamType type) {
        return (SystemParameterQuery) equals("type", type);
    }

    public SystemParameterQuery control(Boolean control) {
        return (SystemParameterQuery) equals("control", control);
    }
}
