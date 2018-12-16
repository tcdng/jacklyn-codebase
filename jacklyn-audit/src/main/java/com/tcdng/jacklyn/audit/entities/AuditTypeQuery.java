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
package com.tcdng.jacklyn.audit.entities;

import com.tcdng.jacklyn.common.entities.BaseInstallEntityQuery;

/**
 * Audit type query.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class AuditTypeQuery extends BaseInstallEntityQuery<AuditType> {

    public AuditTypeQuery() {
        super(AuditType.class);
    }

    public AuditTypeQuery recordName(String recordName) {
        return (AuditTypeQuery) equals("recordName", recordName);
    }

}
