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

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.system.entities.Module;
import com.tcdng.jacklyn.system.entities.ModuleQuery;
import com.tcdng.jacklyn.system.web.beans.ModulePageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing modules.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/module")
@UplBinding("web/system/upl/managemodule.upl")
public class ModuleController extends AbstractSystemFormController<ModulePageBean, Module> {

    public ModuleController() {
        super(ModulePageBean.class, Module.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.MODIFY
                        | ManageRecordModifier.ACTIVATABLE | ManageRecordModifier.REPORTABLE
                        | ManageRecordModifier.ALTERNATE_SAVE);
    }

    @Override
    protected List<Module> find() throws UnifyException {
        ModulePageBean pageBean  = getPageBean();
        ModuleQuery query = new ModuleQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }
        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.installed(Boolean.TRUE);
        query.addOrder("description").ignoreEmptyCriteria(true);
        return getSystemService().findModules(query);
    }

    @Override
    protected Module find(Long id) throws UnifyException {
        return getSystemService().findModule(id);
    }

    @Override
    protected Module prepareCreate() throws UnifyException {
        return null;
    }

    @Override
    protected void onPrepareView(Module module, boolean paste) throws UnifyException {
        setPageWidgetEditable("frmStatus", module.getDeactivatable());
    }

    @Override
    protected Object create(Module module) throws UnifyException {
        return null;
    }

    @Override
    protected int update(Module module) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(Module module) throws UnifyException {
        return 0;
    }

    @Override
    protected boolean isActivatable(Module module) throws UnifyException {
        return module.getDeactivatable();
    }

    @Override
    protected int activate(Module module) throws UnifyException {
        getSystemService().activateModule(module.getName());
        module.setStatus(RecordStatus.ACTIVE);
        return 1;
    }

    @Override
    protected int deactivate(Module module) throws UnifyException {
        getSystemService().deactivateModule(module.getName());
        module.setStatus(RecordStatus.INACTIVE);
        return 1;
    }

    @Override
    protected void onInitPage() throws UnifyException {
        super.onInitPage();
        setPageWidgetEditable("frmName", false);
        setPageWidgetEditable("frmDescription", false);
    }
}
