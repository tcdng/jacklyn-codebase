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
package com.tcdng.jacklyn.common.web.controllers;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.web.ui.container.Form;

/**
 * Convenient abstract base class for page controllers that manage CRUD actions
 * on records using a form.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@UplBinding("web/common/upl/managerecordformviewer.upl")
public abstract class BaseFormCrudController<T extends Entity, U> extends BaseCrudController<T, U> {

    public BaseFormCrudController(Class<T> entityClass, String hint, int modifier) {
        super(entityClass, hint, modifier);
    }

    protected void setCrudViewerEditable(boolean editable) throws UnifyException {
        setEditable("crudViewPanel.mainBodyPanel", editable);
    }

    protected Form getForm() throws UnifyException {
        return getPageWidgetByShortName(Form.class, "crudViewPanel.form");
    }
}
