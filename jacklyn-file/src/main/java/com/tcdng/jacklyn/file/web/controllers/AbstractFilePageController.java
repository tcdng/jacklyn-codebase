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
package com.tcdng.jacklyn.file.web.controllers;

import com.tcdng.jacklyn.common.web.beans.BasePageBean;
import com.tcdng.jacklyn.common.web.controllers.BasePageController;
import com.tcdng.jacklyn.file.business.FileService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Abstract base page controller for file module.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractFilePageController<T extends BasePageBean> extends BasePageController<T> {

    @Configurable
    private FileService fileService;

    public AbstractFilePageController(Class<T> pageBeanClass, Secured secured, ReadOnly readOnly,
            ResetOnWrite resetOnWrite) {
        super(pageBeanClass, secured, readOnly, resetOnWrite);
    }

    protected FileService getFileService() throws UnifyException {
        return fileService;
    }
}
