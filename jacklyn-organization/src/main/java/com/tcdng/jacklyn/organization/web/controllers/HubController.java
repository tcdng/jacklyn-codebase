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

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.organization.entities.Hub;
import com.tcdng.jacklyn.organization.entities.HubQuery;
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
public class HubController extends AbstractOrganizationCrudController<Hub> {

    private String searchName;

    private String searchDescription;

    public HubController() {
        super(Hub.class, "$m{organization.hub.hint}", ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    @Override
    protected List<Hub> find() throws UnifyException {
        HubQuery query = new HubQuery();
        if (QueryUtils.isValidStringCriteria(searchName)) {
            query.name(searchName);
        }

        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }

        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
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
