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
package com.tcdng.jacklyn.audit.web.controllers;

import com.tcdng.jacklyn.audit.business.AuditService;
import com.tcdng.jacklyn.common.web.controllers.BaseCrudController;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.database.Entity;

/**
 * Abstract base class for all audit module record management controllers.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractAuditCrudController<T extends Entity> extends BaseCrudController<T, Long> {

    @Configurable
    private AuditService auditService;

    public AbstractAuditCrudController(Class<T> entityClass, String hintKey, int modifier) {
        super(entityClass, hintKey, modifier);
    }

    protected AuditService getAuditService() throws UnifyException {
        return auditService;
    }
}
