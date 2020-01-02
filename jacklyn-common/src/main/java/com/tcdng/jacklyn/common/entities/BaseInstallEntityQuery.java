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
package com.tcdng.jacklyn.common.entities;

/**
 * Base install entity query.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public abstract class BaseInstallEntityQuery<T extends BaseInstallEntity> extends BaseStatusEntityQuery<T> {

    public BaseInstallEntityQuery(Class<T> entityClass) {
        super(entityClass);
    }

    public BaseInstallEntityQuery<T> installed(Boolean installed) {
        return (BaseInstallEntityQuery<T>) addEquals("installed", installed);
    }
}
