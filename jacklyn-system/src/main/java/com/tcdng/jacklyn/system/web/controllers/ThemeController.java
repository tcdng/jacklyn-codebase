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
import com.tcdng.jacklyn.system.entities.Theme;
import com.tcdng.jacklyn.system.entities.ThemeQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing themes.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/theme")
@UplBinding("web/system/upl/managetheme.upl")
public class ThemeController extends AbstractSystemCrudController<Theme> {

    private String searchName;

    private String searchDescription;

    public ThemeController() {
        super(Theme.class, "$m{system.theme.hint}", ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
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
    protected List<Theme> find() throws UnifyException {
        ThemeQuery query = new ThemeQuery();
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
        return getSystemService().findThemes(query);
    }

    @Override
    protected Theme find(Long id) throws UnifyException {
        return getSystemService().findTheme(id);
    }

    @Override
    protected Theme prepareCreate() throws UnifyException {
        return new Theme();
    }

    @Override
    protected Object create(Theme theme) throws UnifyException {
        return getSystemService().createTheme(theme);
    }

    @Override
    protected int update(Theme theme) throws UnifyException {
        return getSystemService().updateTheme(theme);
    }

    @Override
    protected int delete(Theme theme) throws UnifyException {
        return getSystemService().deleteTheme(theme.getId());
    }

}
