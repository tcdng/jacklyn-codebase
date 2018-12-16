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

package com.tcdng.jacklyn.workflow.business;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.PackableDoc;
import com.tcdng.unify.core.format.Formatter;

/**
 * Workflow item reader writer.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfItemReaderWriter extends WfItemReader {

    public WfItemReaderWriter(PackableDoc pd) {
        super(pd);
    }

    public void writeFieldValue(String name, Object value) throws UnifyException {
        getPd().writeFieldValue(name, value);
    }

    public void writeFieldValue(String name, Object value, Formatter<?> formatter) throws UnifyException {
        getPd().writeFieldValue(name, value, formatter);
    }

}
