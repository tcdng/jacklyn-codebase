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
import com.tcdng.jacklyn.system.data.AuthenticationLargeData;
import com.tcdng.jacklyn.system.entities.Authentication;
import com.tcdng.jacklyn.system.entities.AuthenticationQuery;
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
public class AuthenticationController extends AbstractSystemCrudController<Authentication> {

    private String searchName;

    private String searchDescription;

    private RecordStatus searchStatus;

    private AuthenticationLargeData largeData;

    public AuthenticationController() {
        super(Authentication.class, "$m{system.authentication.hint}",
                ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.CLIPBOARD
                        | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
        largeData = new AuthenticationLargeData();
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

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public AuthenticationLargeData getLargeData() {
        return largeData;
    }

    public void setLargeData(AuthenticationLargeData largeData) {
        this.largeData = largeData;
    }

    @Override
    protected List<Authentication> find() throws UnifyException {
        AuthenticationQuery query = new AuthenticationQuery();
        if (QueryUtils.isValidStringCriteria(searchName)) {
            query.nameLike(searchName);
        }
        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }
        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.order("description").ignoreEmptyCriteria(true);
        return getSystemService().findAuthentications(query);
    }

    @Override
    protected Authentication find(Long id) throws UnifyException {
        largeData = getSystemService().findAuthentication(id);
        return largeData.getData();
    }

    @Override
    protected Authentication prepareCreate() throws UnifyException {
        largeData = new AuthenticationLargeData();
        return largeData.getData();
    }

    @Override
    protected void onPrepareView(Authentication authenticationData, boolean onPaste) throws UnifyException {

    }

    @Override
    protected void onLoseView(Authentication authenticationData) throws UnifyException {
        this.largeData = new AuthenticationLargeData();
    }

    @Override
    protected Object create(Authentication authenticationData) throws UnifyException {
        return getSystemService().createAuthentication(largeData);
    }

    @Override
    protected int update(Authentication authenticationData) throws UnifyException {
        return getSystemService().updateAuthentication(largeData);
    }

    @Override
    protected int delete(Authentication authenticationData) throws UnifyException {
        return getSystemService().deleteAuthentication(authenticationData.getId());
    }
}
