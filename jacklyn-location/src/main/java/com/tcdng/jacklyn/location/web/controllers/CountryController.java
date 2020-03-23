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
package com.tcdng.jacklyn.location.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.location.entities.Country;
import com.tcdng.jacklyn.location.entities.CountryQuery;
import com.tcdng.jacklyn.location.web.beans.CountryPageBean;
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
@Component("/location/country")
@UplBinding("web/location/upl/managecountry.upl")
public class CountryController extends AbstractLocationFormController<CountryPageBean, Country> {

    public CountryController() {
        super(CountryPageBean.class, Country.class, ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<Country> find() throws UnifyException {
        CountryPageBean pageBean = getPageBean();
        CountryQuery query = new CountryQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchCode())) {
            query.iso3Code(pageBean.getSearchCode());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.addOrder("description").ignoreEmptyCriteria(true);
        return getLocationService().findCountries(query);
    }

    @Override
    protected Country find(Long id) throws UnifyException {
        return getLocationService().findCountry(id);
    }

    @Override
    protected Object create(Country country) throws UnifyException {
        return getLocationService().createCountry(country);
    }

    @Override
    protected int update(Country country) throws UnifyException {
        return getLocationService().updateCountry(country);
    }

    @Override
    protected int delete(Country country) throws UnifyException {
        return getLocationService().deleteCountry(country.getId());
    }

}
