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
package com.tcdng.jacklyn.system.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.system.entities.Module;
import com.tcdng.jacklyn.system.entities.ModuleQuery;
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
public class ModuleController extends AbstractSystemCrudController<Module> {

    private String searchDescription;

    public ModuleController() {
        super(Module.class, "$m{system.module.hint}",
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.MODIFY
                        | ManageRecordModifier.ACTIVATABLE | ManageRecordModifier.REPORTABLE
                        | ManageRecordModifier.ALTERNATE_SAVE);
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    @Override
    protected List<Module> find() throws UnifyException {
        ModuleQuery query = new ModuleQuery();
        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }
        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.order("description").ignoreEmptyCriteria(true);
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
    protected void onPrepareView(Module moduleData, boolean paste) throws UnifyException {
        setEditable("frmStatus", moduleData.getDeactivatable());
    }

    @Override
    protected Object create(Module moduleData) throws UnifyException {
        return null;
    }

    @Override
    protected int update(Module moduleData) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(Module moduleData) throws UnifyException {
        return 0;
    }

    @Override
    protected boolean isActivatable(Module moduleData) throws UnifyException {
        return moduleData.getDeactivatable();
    }

    @Override
    protected int activate(Module moduleData) throws UnifyException {
        getSystemService().activateModule(moduleData.getName());
        moduleData.setStatus(RecordStatus.ACTIVE);
        return 1;
    }

    @Override
    protected int deactivate(Module moduleData) throws UnifyException {
        getSystemService().deactivateModule(moduleData.getName());
        moduleData.setStatus(RecordStatus.INACTIVE);
        return 1;
    }

    @Override
    protected void onSetPage() throws UnifyException {
        super.onSetPage();
        setEditable("frmName", false);
        setEditable("frmDescription", false);
    }
}
