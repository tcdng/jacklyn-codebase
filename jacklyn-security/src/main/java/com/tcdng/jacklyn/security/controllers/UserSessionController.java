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
package com.tcdng.jacklyn.security.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.controllers.BaseCrudController;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.system.UserSessionManager;
import com.tcdng.unify.core.system.entities.UserSessionTracking;
import com.tcdng.unify.core.system.entities.UserSessionTrackingQuery;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;

/**
 * Controller for managing user sessions.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/security/usersession")
@UplBinding("web/security/upl/manageusersession.upl")
public class UserSessionController extends BaseCrudController<UserSessionTracking, String> {

    @Configurable
    private UserSessionManager userSessionManager;

    private String searchLoginId;

    private String searchNodeId;

    public UserSessionController() {
        super(UserSessionTracking.class, "$m{security.usersession.hint}",
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    public String getSearchLoginId() {
        return searchLoginId;
    }

    public void setSearchLoginId(String searchLoginId) {
        this.searchLoginId = searchLoginId;
    }

    public String getSearchNodeId() {
        return searchNodeId;
    }

    public void setSearchNodeId(String searchNodeId) {
        this.searchNodeId = searchNodeId;
    }

    @Action
    public String forceLogOut() throws UnifyException {
        return findRecords();
    }

    @Action
    public String forceLogOutAll() throws UnifyException {
        return findRecords();
    }

    @Override
    protected List<UserSessionTracking> find() throws UnifyException {
        UserSessionTrackingQuery query = new UserSessionTrackingQuery();
        if (QueryUtils.isValidStringCriteria(searchLoginId)) {
            query.userLoginId(searchLoginId);
        }

        if (QueryUtils.isValidStringCriteria(searchNodeId)) {
            query.node(searchNodeId);
        }
        query.ignoreEmptyCriteria(true);
        return userSessionManager.findUserSessions(query);
    }

    @Override
    protected UserSessionTracking find(String id) throws UnifyException {
        return userSessionManager.findUserSession(id);
    }

    @Override
    protected UserSessionTracking prepareCreate() throws UnifyException {
        return null;
    }

    @Override
    protected Object create(UserSessionTracking record) throws UnifyException {
        return null;
    }

    @Override
    protected int update(UserSessionTracking record) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(UserSessionTracking record) throws UnifyException {
        return 0;
    }
}
