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
package com.tcdng.jacklyn.file.web.controllers;

import com.tcdng.jacklyn.file.entities.AbstractFileTransferBox;
import com.tcdng.jacklyn.file.web.beans.AbstractFileTransferBoxPageBean;
import com.tcdng.unify.core.UnifyException;

/**
 * Abstract base file transfer box page controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractFileTransferBoxFormController<T extends AbstractFileTransferBoxPageBean<U>, U extends AbstractFileTransferBox>
        extends AbstractFileFormController<T, U> {

    public AbstractFileTransferBoxFormController(Class<T> pageBeanClass, Class<U> entityClass, int modifier) {
        super(pageBeanClass, entityClass, modifier);
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        AbstractFileTransferBoxPageBean<U> pageBean = getPageBean();
        if (pageBean.getSearchCreateDt() == null) {
            pageBean.setSearchCreateDt(getFileService().getToday());
        }
    }

    @Override
    protected U prepareCreate() throws UnifyException {
        return null;
    }

    @Override
    protected Object create(U record) throws UnifyException {
        return null;
    }

    @Override
    protected int update(U record) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(U record) throws UnifyException {
        return 0;
    }
}
