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
import com.tcdng.jacklyn.system.data.AuthenticationLargeData;
import com.tcdng.jacklyn.system.entities.Authentication;
import com.tcdng.jacklyn.system.entities.AuthenticationQuery;
import com.tcdng.jacklyn.system.web.beans.AuthenticationPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing system authentications.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/authentication")
@UplBinding("web/system/upl/manageauthentication.upl")
public class AuthenticationController extends AbstractSystemFormController<AuthenticationPageBean, Authentication> {

    public AuthenticationController() {
        super(AuthenticationPageBean.class, Authentication.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.CLIPBOARD
                        | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<Authentication> find() throws UnifyException {
        AuthenticationPageBean pageBean = getPageBean();
        AuthenticationQuery query = new AuthenticationQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchName())) {
            query.nameLike(pageBean.getSearchName());
        }
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }
        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.addOrder("description").ignoreEmptyCriteria(true);
        return getSystemService().findAuthentications(query);
    }

    @Override
    protected Authentication find(Long id) throws UnifyException {
        AuthenticationLargeData largeData = getSystemService().findAuthentication(id);
        AuthenticationPageBean pageBean = getPageBean();
        pageBean.setLargeData(largeData);
        return largeData.getData();
    }

    @Override
    protected Authentication prepareCreate() throws UnifyException {
        AuthenticationLargeData largeData = new AuthenticationLargeData();
        AuthenticationPageBean pageBean = getPageBean();
        pageBean.setLargeData(largeData);
        return largeData.getData();
    }

    @Override
    protected void onPrepareView(Authentication authenticationData, boolean onPaste) throws UnifyException {

    }

    @Override
    protected void onLoseView(Authentication authenticationData) throws UnifyException {
        AuthenticationPageBean pageBean = getPageBean();
        pageBean.setLargeData(new AuthenticationLargeData());
    }

    @Override
    protected Object create(Authentication authenticationData) throws UnifyException {
        AuthenticationPageBean pageBean = getPageBean();
        return getSystemService().createAuthentication(pageBean.getLargeData());
    }

    @Override
    protected int update(Authentication authenticationData) throws UnifyException {
        AuthenticationPageBean pageBean = getPageBean();
        return getSystemService().updateAuthentication(pageBean.getLargeData());
    }

    @Override
    protected int delete(Authentication authenticationData) throws UnifyException {
        return getSystemService().deleteAuthentication(authenticationData.getId());
    }
}
