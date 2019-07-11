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
import com.tcdng.jacklyn.organization.entities.Country;
import com.tcdng.jacklyn.organization.entities.CountryQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing countries.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/organization/country")
@UplBinding("web/organization/upl/managecountry.upl")
public class CountryController extends AbstractOrganizationCrudController<Country> {

    private String searchCode;

    private String searchDescription;

    public CountryController() {
        super(Country.class, "$m{organization.country.hint}", ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    public String getSearchCode() {
        return searchCode;
    }

    public void setSearchCode(String searchCode) {
        this.searchCode = searchCode;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    @Override
    protected List<Country> find() throws UnifyException {
        CountryQuery query = new CountryQuery();
        if (QueryUtils.isValidStringCriteria(searchCode)) {
            query.code(searchCode);
        }

        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }

        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.order("description").ignoreEmptyCriteria(true);
        return getOrganizationService().findCountries(query);
    }

    @Override
    protected Country find(Long id) throws UnifyException {
        return getOrganizationService().findCountry(id);
    }

    @Override
    protected Country prepareCreate() throws UnifyException {
        return new Country();
    }

    @Override
    protected Object create(Country country) throws UnifyException {
        return getOrganizationService().createCountry(country);
    }

    @Override
    protected int update(Country country) throws UnifyException {
        return getOrganizationService().updateCountry(country);
    }

    @Override
    protected int delete(Country country) throws UnifyException {
        return getOrganizationService().deleteCountry(country.getId());
    }

}
