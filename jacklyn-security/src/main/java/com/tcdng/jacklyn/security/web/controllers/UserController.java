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
package com.tcdng.jacklyn.security.web.controllers;

import java.util.Collections;
import java.util.List;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.security.constants.SecurityModuleAuditConstants;
import com.tcdng.jacklyn.security.data.UserLargeData;
import com.tcdng.jacklyn.security.entities.User;
import com.tcdng.jacklyn.security.entities.UserQuery;
import com.tcdng.jacklyn.security.web.beans.UserPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.web.annotation.Action;

/**
 * Controller for managing users.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/security/user")
@UplBinding("web/security/upl/manageuser.upl")
public class UserController extends AbstractSecurityFormController<UserPageBean, User> {

    public UserController() {
        super(UserPageBean.class, User.class, ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Override
    @Action
    public String copyRecord() throws UnifyException {
        UserPageBean pageBean = getPageBean();
        UserLargeData clipboardLargeData = ReflectUtils.shallowBeanCopy(pageBean.getLargeData());
        pageBean.setClipboardLargeData(clipboardLargeData);

        return super.copyRecord();
    }

    @Action
    public String resetUserPassword() throws UnifyException {
        UserPageBean pageBean = getPageBean();
        User user = pageBean.getRecord();
        getSecurityService().resetUserPassword(user.getId());
        logUserEvent(SecurityModuleAuditConstants.RESET_PASSWORD, user.getFullName());
        hintUser("$m{security.user.hint.passwordreset}", user.getFullName());
        return noResult();
    }

    @Override
    protected List<User> find() throws UnifyException {
        UserPageBean pageBean = getPageBean();
        UserQuery query = new UserQuery();
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchRoleId())) {
            List<Long> userIdList = getSecurityService().findRoleUserIds(pageBean.getSearchRoleId());
            if (DataUtils.isBlank(userIdList)) {
                return Collections.emptyList();
            }

            query.idIn(userIdList);
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchLoginId())) {
            query.loginIdLike(pageBean.getSearchLoginId());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchFullName())) {
            query.fullNameLike(pageBean.getSearchFullName());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }

        query.excludeSysRecords();
        query.addOrder("fullName").ignoreEmptyCriteria(true);
        return getSecurityService().findUsers(query);
    }

    @Override
    protected User find(Long id) throws UnifyException {
        UserLargeData largeData = getSecurityService().findUserDocument(id);
        UserPageBean pageBean = getPageBean();
        pageBean.setLargeData(largeData);
        return largeData.getData();
    }

    @Override
    protected void onPrepareCreate(User user) throws UnifyException {
        setPageWidgetEditable("frmLoginId", true);

        UserPageBean pageBean = getPageBean();
        user.setStatus(RecordStatus.ACTIVE);
        user.setPasswordExpires(Boolean.TRUE);
        pageBean.setLargeData(new UserLargeData(user));
    }

    @Override
    protected void onPrepareView(User user, boolean onPaste) throws UnifyException {
        UserPageBean pageBean = getPageBean();
        UserLargeData largeData = pageBean.getLargeData();
        if (onPaste) {
            largeData.setPhotograph(pageBean.getClipboardLargeData().getPhotograph());
            largeData.setRoleIdList(pageBean.getClipboardLargeData().getRoleIdList());
        }

        if (ManageRecordModifier.ADD == getMode()) {
            setPageWidgetDisabled("resetBtn", true);
            setPageWidgetDisabled("frmImage", false);
            setPageWidgetEditable("frmLoginId", true);
            setPageWidgetEditable("frmRoleAssignPanel", true);
            setCrudViewerEditable(true);
        } else {
            boolean isDisabled = user.isReserved();
            setPageWidgetDisabled("resetBtn", isDisabled);
            setPageWidgetDisabled("frmImage", isDisabled);
            setPageWidgetEditable("frmLoginId", false);
            setPageWidgetEditable("frmRoleAssignPanel", !isDisabled);
            setCrudViewerEditable(!isDisabled);
        }
    }

    @Override
    protected void onLoseView(User user) throws UnifyException {
        UserPageBean pageBean = getPageBean();
        pageBean.setLargeData(new UserLargeData());
        pageBean.setClipboardLargeData(null);
    }

    @Override
    protected Object create(User user) throws UnifyException {
        UserPageBean pageBean = getPageBean();
        return getSecurityService().createUser(pageBean.getLargeData());
    }

    @Override
    protected int update(User user) throws UnifyException {
        UserPageBean pageBean = getPageBean();
        return getSecurityService().updateUser(pageBean.getLargeData());
    }

    @Override
    protected int delete(User user) throws UnifyException {
        return getSecurityService().deleteUser(user.getId());
    }

    @Override
    protected void onPrepareCrudViewer(User record, int mode) throws UnifyException {
        setPageWidgetVisible("resetBtn", mode == ManageRecordModifier.VIEW);
        if (mode == ManageRecordModifier.ADD) {
            setPageWidgetEditable("frmRoleAssignPanel", true);
            setPageWidgetDisabled("frmImage", false);
        }
    }
}
