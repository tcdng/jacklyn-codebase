/*
 * Copyright 2018-2019 The Code Department
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

package com.tcdng.jacklyn.audit.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcdng.unify.core.logging.FieldAudit;

/**
 * Convenient abstract base class for auditable details.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class AbstractAuditableDetails {

    private Map<String, FieldAudit> fieldAuditMap;

    public void clearChanged() {
        fieldAuditMap = null;
    }

    public boolean isChanged() {
        return fieldAuditMap != null && !fieldAuditMap.isEmpty();
    }

    public List<FieldAudit> getFieldAuditList() {
        if (isChanged()) {
            return new ArrayList<FieldAudit>(fieldAuditMap.values());
        }

        return Collections.emptyList();
    }

    protected void audit(String fieldName, Object originalVal, Object newVal) {
        if (fieldAuditMap == null) {
            fieldAuditMap = new HashMap<String, FieldAudit>();
        }

        FieldAudit fieldAudit = fieldAuditMap.get(fieldName);
        if (fieldAudit == null) {
            fieldAuditMap.put(fieldName, new FieldAudit(fieldName, originalVal, newVal));
        } else {
            fieldAudit.setNewValue(newVal);
        }
    }

}
