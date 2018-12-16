/*
 * Copyright 2018 The Code Department
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

import com.tcdng.unify.core.data.AbstractWrappedData;

/**
 * Abstract base large data.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class BaseLargeData<T extends BaseEntity> extends AbstractWrappedData<T> {

    public BaseLargeData(T data) {
        super(data);
    }

    public void setId(Long id) {
        this.getData().setId(id);
    }

    public Long getId() {
        return (Long) this.getData().getId();
    }

    public Object getOwnerId() {
        return this.getData().getOwnerId();
    }

    public String getDescription() {
        return this.getData().getDescription();
    }

    public boolean isReserved() {
        return this.getData().isReserved();
    }

    public String getListKey() {
        return this.getData().getListKey();
    }

    public String getListDescription() {
        return this.getData().getListDescription();
    }
}
