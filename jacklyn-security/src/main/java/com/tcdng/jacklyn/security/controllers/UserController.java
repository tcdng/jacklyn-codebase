/*
 * Copyright 2018 The Code Department
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

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.security.constants.SecurityModuleAuditConstants;
import com.tcdng.jacklyn.security.data.UserLargeData;
import com.tcdng.jacklyn.security.entities.User;
import com.tcdng.jacklyn.security.entities.UserQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
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
public class UserController extends AbstractSecurityRecordController<User> {

	private String searchLoginId;

	private String searchFullName;

	private RecordStatus searchStatus;

	private UserLargeData largeData;

	private UserLargeData clipboardLargeData;

	public UserController() {
		super(User.class, "security.user.hint",
				ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
						| ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD
						| ManageRecordModifier.REPORTABLE);
		this.largeData = new UserLargeData();
	}

	@Override
	@Action
	public String copyRecord() throws UnifyException {
		this.clipboardLargeData = ReflectUtils.shallowBeanCopy(this.largeData);
		return super.copyRecord();
	}

	@Action
	public String resetUserPassword() throws UnifyException {
		User userData = this.getRecord();
		this.getSecurityModule().resetUserPassword(userData.getId());
		this.logUserEvent(SecurityModuleAuditConstants.RESET_PASSWORD, userData.getFullName());
		this.hintUser("security.user.hint.passwordreset", userData.getFullName());
		return this.noResult();
	}

	public String getSearchLoginId() {
		return searchLoginId;
	}

	public void setSearchLoginId(String searchLoginId) {
		this.searchLoginId = searchLoginId;
	}

	public RecordStatus getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(RecordStatus searchStatus) {
		this.searchStatus = searchStatus;
	}

	public String getSearchFullName() {
		return searchFullName;
	}

	public void setSearchFullName(String searchFullName) {
		this.searchFullName = searchFullName;
	}

	public UserLargeData getLargeData() {
		return largeData;
	}

	public void setLargeData(UserLargeData largeData) {
		this.largeData = largeData;
	}

	@Override
	protected List<User> find() throws UnifyException {
		UserQuery query = new UserQuery();
		if (QueryUtils.isValidStringCriteria(searchLoginId)) {
			query.loginId(searchLoginId);
		}
		if (QueryUtils.isValidStringCriteria(searchFullName)) {
			query.fullNameLike(searchFullName);
		}
		if (this.getSearchStatus() != null) {
			query.status(this.getSearchStatus());
		}
		query.order("fullName").ignoreEmptyCriteria(true);
		return this.getSecurityModule().findUsers(query);
	}

	@Override
	protected User find(Long id) throws UnifyException {
		this.largeData = this.getSecurityModule().findUserDocument(id);
		return this.largeData.getData();
	}

	@Override
	protected User prepareCreate() throws UnifyException {
		this.setEditable("frmLoginId", true);

		this.largeData = new UserLargeData();
		User userData = this.largeData.getData();
		userData.setStatus(RecordStatus.ACTIVE);
		userData.setPasswordExpires(Boolean.TRUE);
		return userData;
	}

	@Override
	protected void onPrepareView(User userData, boolean onPaste) throws UnifyException {
		if (onPaste) {
			this.largeData.setPhotograph(this.clipboardLargeData.getPhotograph());
			this.largeData.setRoleIdList(this.clipboardLargeData.getRoleIdList());
		}

		if (ManageRecordModifier.ADD == this.getMode()) {
			this.setDisabled("resetBtn", true);
			this.setDisabled("frmImage", false);
			this.setEditable("frmLoginId", true);
			this.setEditable("frmRoleAssignPanel", true);
			this.setEditable("crudFormPanel.mainBodyPanel", true);
		} else {
			boolean isDisabled = userData.isReserved();
			this.setDisabled("resetBtn", isDisabled);
			this.setDisabled("frmImage", isDisabled);
			this.setEditable("frmLoginId", false);
			this.setEditable("frmRoleAssignPanel", !isDisabled);
			this.setEditable("crudFormPanel.mainBodyPanel", !isDisabled);
		}
	}

	@Override
	protected void onLoseView(User userData) throws UnifyException {
		this.largeData = new UserLargeData();
		this.clipboardLargeData = null;
	}

	@Override
	protected Object create(User userData) throws UnifyException {
		return this.getSecurityModule().createUser(largeData);
	}

	@Override
	protected int update(User userData) throws UnifyException {
		return this.getSecurityModule().updateUser(this.largeData);
	}

	@Override
	protected int delete(User userData) throws UnifyException {
		return this.getSecurityModule().deleteUser(userData.getId());
	}

	@Override
	protected void onPrepareForm(int mode) throws UnifyException {
		this.setVisible("resetBtn", mode != ManageRecordModifier.ADD);
		if (mode == ManageRecordModifier.ADD) {
			this.setEditable("frmRoleAssignPanel", true);
			this.setDisabled("frmImage", false);
		}
	}
}
