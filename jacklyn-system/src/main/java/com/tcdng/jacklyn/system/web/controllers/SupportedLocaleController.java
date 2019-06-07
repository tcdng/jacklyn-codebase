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
package com.tcdng.jacklyn.system.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.system.entities.SupportedLocale;
import com.tcdng.jacklyn.system.entities.SupportedLocaleQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing supported locale.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/supportedlocale")
@UplBinding("web/system/upl/managesupportedlocale.upl")
public class SupportedLocaleController extends AbstractSystemCrudController<SupportedLocale> {

    private String searchName;

    private String searchDescription;

    public SupportedLocaleController() {
        super(SupportedLocale.class, "$m{system.supportedlocale.hint}", ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
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
    protected List<SupportedLocale> find() throws UnifyException {
        SupportedLocaleQuery query = new SupportedLocaleQuery();
        if (QueryUtils.isValidStringCriteria(searchName)) {
            query.name(searchName);
        }

        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }

        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.order("description").ignoreEmptyCriteria(true);
        return getSystemService().findSupportedLocales(query);
    }

    @Override
    protected SupportedLocale find(Long id) throws UnifyException {
        return getSystemService().findSupportedLocale(id);
    }

    @Override
    protected SupportedLocale prepareCreate() throws UnifyException {
        return new SupportedLocale();
    }

    @Override
    protected Object create(SupportedLocale supportedLocale) throws UnifyException {
        return getSystemService().createSupportedLocale(supportedLocale);
    }

    @Override
    protected int update(SupportedLocale supportedLocale) throws UnifyException {
        return getSystemService().updateSupportedLocale(supportedLocale);
    }

    @Override
    protected int delete(SupportedLocale supportedLocale) throws UnifyException {
        return getSystemService().deleteSupportedLocale(supportedLocale.getId());
    }

}
