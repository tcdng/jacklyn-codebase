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
package com.tcdng.jacklyn.system.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.system.entities.InputCtrlDef;
import com.tcdng.jacklyn.system.entities.InputCtrlDefQuery;
import com.tcdng.jacklyn.system.web.beans.InputCtrlDefPageBean;
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
public class InputCtrlDefController extends AbstractSystemFormController<InputCtrlDefPageBean, InputCtrlDef> {

    public InputCtrlDefController() {
        super(InputCtrlDefPageBean.class, InputCtrlDef.class, ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<InputCtrlDef> find() throws UnifyException {
        InputCtrlDefPageBean pageBean = getPageBean();
        InputCtrlDefQuery query = new InputCtrlDefQuery();
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchModuleId())) {
            query.moduleId(pageBean.getSearchModuleId());
        }

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
        return getSystemService().findInputCtrlDefs(query);
    }

    @Override
    protected InputCtrlDef find(Long id) throws UnifyException {
        return getSystemService().findInputCtrlDef(id);
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
