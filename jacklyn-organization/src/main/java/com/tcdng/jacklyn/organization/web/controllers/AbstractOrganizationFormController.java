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
package com.tcdng.jacklyn.organization.web.controllers;

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;
import com.tcdng.jacklyn.common.web.controllers.BaseEntityFormController;
import com.tcdng.jacklyn.organization.business.OrganizationService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Abstract base organization service record management page controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractOrganizationFormController<T extends BaseEntityPageBean<U>, U extends BaseEntity>
        extends BaseEntityFormController<T, Long, U> {

    @Configurable
    private OrganizationService organizationService;

    public AbstractOrganizationFormController(Class<T> pageBeanClass, Class<U> entityClass, int modifier) {
        super(pageBeanClass, entityClass, modifier);
    }

    protected OrganizationService getOrganizationService() throws UnifyException {
        return organizationService;
    }
}
