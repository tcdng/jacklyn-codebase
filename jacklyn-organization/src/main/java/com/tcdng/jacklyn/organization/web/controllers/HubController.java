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
package com.tcdng.jacklyn.organization.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.organization.entities.Hub;
import com.tcdng.jacklyn.organization.entities.HubQuery;
import com.tcdng.jacklyn.organization.web.beans.HubPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing hubs.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/organization/hub")
@UplBinding("web/organization/upl/managehub.upl")
public class HubController extends AbstractOrganizationFormController<HubPageBean, Hub> {

    public HubController() {
        super(HubPageBean.class, Hub.class, ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<Hub> find() throws UnifyException {
        HubPageBean pageBean = getPageBean();
        HubQuery query = new HubQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchName())) {
            query.name(pageBean.getSearchName());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.addOrder("description").ignoreEmptyCriteria(true);
        return getOrganizationService().findHubs(query);
    }

    @Override
    protected Hub find(Long id) throws UnifyException {
        return getOrganizationService().findHub(id);
    }

    @Override
    protected Hub prepareCreate() throws UnifyException {
        return new Hub();
    }

    @Override
    protected Object create(Hub hubData) throws UnifyException {
        return getOrganizationService().createHub(hubData);
    }

    @Override
    protected int update(Hub hubData) throws UnifyException {
        return getOrganizationService().updateHub(hubData);
    }

    @Override
    protected int delete(Hub hubData) throws UnifyException {
        return getOrganizationService().deleteHub(hubData.getId());
    }

}
