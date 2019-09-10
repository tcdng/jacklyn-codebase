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

package com.tcdng.jacklyn.workflow.business;

import java.util.List;
import java.util.Set;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.PackableDoc;

/**
 * Workflow item reader.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfItemReader {

    private PackableDoc pd;

    public WfItemReader(PackableDoc pd) {
        this.pd = pd;
    }

    public Set<String> getFieldNames() {
        return pd.getConfig().getFieldNames();
    }

    public Class<?> getFieldType(String fieldName) throws UnifyException {
        return pd.getConfig().getFieldConfig(fieldName).getDataType();
    }

    public boolean isList(String fieldName) throws UnifyException {
        return pd.getConfig().getFieldConfig(fieldName).isList();
    }

    public boolean isComplex(String fieldName) throws UnifyException {
        return pd.getConfig().getFieldConfig(fieldName).isComplex();
    }
    
    public Object readField(String fieldName) throws UnifyException {
        return pd.read(fieldName);
    }

    public <T> T readField(Class<T> type, String fieldName) throws UnifyException {
        return pd.read(type, fieldName);
    }

    public <T> List<T> readListField(Class<T> type, String fieldName) throws UnifyException {
        return pd.readList(type, fieldName);
    }

    protected PackableDoc getPd() {
        return pd;
    }
}
