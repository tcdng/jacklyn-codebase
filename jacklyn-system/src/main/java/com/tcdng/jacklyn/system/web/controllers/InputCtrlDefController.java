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
import com.tcdng.jacklyn.system.entities.InputCtrlDef;
import com.tcdng.jacklyn.system.entities.InputCtrlDefQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing input control definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/inputctrldef")
@UplBinding("web/system/upl/manageinputctrldef.upl")
public class InputCtrlDefController extends AbstractSystemCrudController<InputCtrlDef> {

    private Long searchModuleId;

    private String searchName;

    private String searchDescription;

    public InputCtrlDefController() {
        super(InputCtrlDef.class, "$m{system.inputctrldef.hint}", ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    public Long getSearchModuleId() {
        return searchModuleId;
    }

    public void setSearchModuleId(Long searchModuleId) {
        this.searchModuleId = searchModuleId;
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
    protected List<InputCtrlDef> find() throws UnifyException {
        InputCtrlDefQuery query = new InputCtrlDefQuery();
        if (QueryUtils.isValidLongCriteria(searchModuleId)) {
            query.moduleId(searchModuleId);
        }

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
        return getSystemService().findInputCtrlDefs(query);
    }

    @Override
    protected InputCtrlDef find(Long id) throws UnifyException {
        return getSystemService().findInputCtrlDef(id);
    }

    @Override
    protected InputCtrlDef prepareCreate() throws UnifyException {
        return new InputCtrlDef();
    }

    @Override
    protected Object create(InputCtrlDef inputCtrlDefData) throws UnifyException {
        return getSystemService().createInputCtrlDef(inputCtrlDefData);
    }

    @Override
    protected int update(InputCtrlDef inputCtrlDefData) throws UnifyException {
        return getSystemService().updateInputCtrlDef(inputCtrlDefData);
    }

    @Override
    protected int delete(InputCtrlDef inputCtrlDefData) throws UnifyException {
        return getSystemService().deleteInputCtrlDef(inputCtrlDefData.getId());
    }

}
