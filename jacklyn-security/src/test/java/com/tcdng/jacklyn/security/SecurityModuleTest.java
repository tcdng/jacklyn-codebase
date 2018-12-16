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
package com.tcdng.jacklyn.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.tcdng.jacklyn.common.AbstractJacklynTest;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.security.business.SecurityModule;
import com.tcdng.jacklyn.security.constants.SecurityModuleErrorConstants;
import com.tcdng.jacklyn.security.constants.SecurityModuleNameConstants;
import com.tcdng.jacklyn.security.constants.SecurityModuleSysParamConstants;
import com.tcdng.jacklyn.security.data.RoleLargeData;
import com.tcdng.jacklyn.security.data.UserLargeData;
import com.tcdng.jacklyn.security.entities.Biometric;
import com.tcdng.jacklyn.security.entities.PasswordHistory;
import com.tcdng.jacklyn.security.entities.Privilege;
import com.tcdng.jacklyn.security.entities.PrivilegeQuery;
import com.tcdng.jacklyn.security.entities.Role;
import com.tcdng.jacklyn.security.entities.RolePrivilege;
import com.tcdng.jacklyn.security.entities.RolePrivilegeQuery;
import com.tcdng.jacklyn.security.entities.RolePrivilegeWidget;
import com.tcdng.jacklyn.security.entities.RoleQuery;
import com.tcdng.jacklyn.security.entities.User;
import com.tcdng.jacklyn.security.entities.UserBiometric;
import com.tcdng.jacklyn.security.entities.UserQuery;
import com.tcdng.jacklyn.security.entities.UserRole;
import com.tcdng.jacklyn.security.entities.UserRoleQuery;
import com.tcdng.jacklyn.system.business.SystemModule;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.operation.Update;

/**
 * Security business module tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class SecurityModuleTest extends AbstractJacklynTest {

	@Test
	public void testCreateRole() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		Role role = getRole("sec-001", "Supervisor");
		Long roleId = securityModule.createRole(role);
		assertNotNull(roleId);
	}

	@Test
	public void testFindRole() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		Role role = getRole("sec-001", "Supervisor");
		Long roleId = securityModule.createRole(role);

		Role fetchedRole = securityModule.findRole(roleId);
		assertNotNull(fetchedRole);
		assertEquals(role.getName(), fetchedRole.getName());
		assertEquals(role.getDescription(), fetchedRole.getDescription());
	}

	@Test
	public void testFindRoles() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		Role role = getRole("sec-001", "Supervisor");
		securityModule.createRole(role);

		RoleQuery query = new RoleQuery();
		query.ignoreEmptyCriteria(true);
		query.orderById();
		List<Role> roleList = securityModule.findRoles(query);
		assertNotNull(roleList);
		assertEquals(1, roleList.size());
		assertEquals(role.getName(), roleList.get(0).getName());
		assertEquals(role.getDescription(), roleList.get(0).getDescription());

		role = getRole("sec-002", "Adminstrator");
		securityModule.createRole(role);
		roleList = securityModule.findRoles(query);
		assertNotNull(roleList);
		assertEquals(2, roleList.size());
		assertEquals(role.getName(), roleList.get(1).getName());
		assertEquals(role.getDescription(), roleList.get(1).getDescription());
	}

	@Test
	public void testUpdateRole() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		Role role = getRole("sec-001", "Supervisor");
		Long roleId = securityModule.createRole(role);

		Role fetchedRole = securityModule.findRole(roleId);
		fetchedRole.setDescription("Supervisor (Advanced)");
		int count = securityModule.updateRole(fetchedRole);
		assertEquals(1, count);

		Role updatedRole = securityModule.findRole(roleId);
		assertEquals(fetchedRole, updatedRole);
		assertFalse(role.equals(updatedRole));
	}

	@Test
	public void testDeleteRole() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		Role role = getRole("sec-001", "Supervisor");
		Long roleId = securityModule.createRole(role);

		int count = securityModule.deleteRole(roleId);
		assertEquals(1, count);
	}

	@Test
	public void testUpdateRolePrivileges() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);
		Long moduleId = systemModule.findModule("customer").getId();
		List<Long> privilegeIdList = securityModule.findPrivilegeIds(
				(PrivilegeQuery) new PrivilegeQuery().moduleId(moduleId).orderById());
		RoleLargeData roleDoc = new RoleLargeData(getRole("sec-001", "Supervisor"));
		roleDoc.setPrivilegeIdList(privilegeIdList);
		Long roleId = securityModule.createRole(roleDoc);
		securityModule.updateRolePrivileges(roleId, privilegeIdList);
	}

	@Test
	public void testFindRolePrivileges() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);
		Long moduleId = systemModule.findModule(SystemModuleNameConstants.SYSTEM_MODULE).getId();
		List<Long> privilegeIdList = securityModule.findPrivilegeIds(
				(PrivilegeQuery) new PrivilegeQuery().moduleId(moduleId).orderById());
		RoleLargeData roleDoc = new RoleLargeData(getRole("sec-001", "Supervisor"));
		roleDoc.setPrivilegeIdList(privilegeIdList);
		securityModule.createRole(roleDoc);

		List<Long> fetchedModuleActivityIdList = securityModule
				.findPrivilegeIds(new RolePrivilegeQuery().moduleId(moduleId).roleName("sec-001"));

		Collections.sort(privilegeIdList);
		Collections.sort(fetchedModuleActivityIdList);
		assertTrue(privilegeIdList.equals(fetchedModuleActivityIdList));
	}

	@Test
	public void testFindModulePrivileges() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		PrivilegeQuery query = new PrivilegeQuery();
		query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
		query.orderById();
		List<Privilege> privilegeList = securityModule.findPrivileges(query);
		assertNotNull(privilegeList);
		assertFalse(privilegeList.isEmpty());
	}

	@Test
	public void testFindModulePrivilegeIds() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		PrivilegeQuery query = new PrivilegeQuery();
		query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
		query.orderById();
		List<Privilege> privilegeList = securityModule.findPrivileges(query);
		List<Long> privilegeIdList = securityModule.findPrivilegeIds(query);
		assertNotNull(privilegeIdList);
		assertFalse(privilegeIdList.isEmpty());
		assertEquals(privilegeList.size(), privilegeIdList.size());
		assertEquals(privilegeList.get(0).getId(), privilegeIdList.get(0));
		assertEquals(privilegeList.get(1).getId(), privilegeIdList.get(1));
	}

	@Test
	public void testSetPrivilegeStatus() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		PrivilegeQuery query = new PrivilegeQuery();
		query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
		query.orderById();
		List<Long> privilegeIdList = securityModule.findPrivilegeIds(query);
		List<Long> sampleIds = new ArrayList<Long>();
		sampleIds.add(privilegeIdList.get(1));
		sampleIds.add(privilegeIdList.get(2));
		int count = securityModule.setPrivilegeStatuses(sampleIds, RecordStatus.INACTIVE);
		assertEquals(2, count);
	}

	@Test
	public void testCreateUser() throws Exception {
		User user
				= new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);

		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		Long id = securityModule.createUser(user);

		// Check if versioned record policy was applied
		assertNotNull(id);

		// Check if user record policy was applied
		assertEquals(Boolean.TRUE, user.getChangePassword());
		assertEquals(Integer.valueOf(0), user.getLoginAttempts());
		assertFalse(user.getLoginLocked());
	}

	@Test
	public void testCreateUserWithPhotoAndRoles() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);

		User user
				= new User(0L, "Joe Moe", "JOEMOE", "joe.moe@thecodedepartment.com.ng", false);
		byte[] photograph = { (byte) 0x89, (byte) 0x4E, (byte) 0xBD };
		List<Long> roleIdList = new ArrayList<Long>();
		roleIdList.add(securityModule.createRole(getRole("sec-001", "Supervisor")));
		roleIdList.add(securityModule.createRole(getRole("sec-002", "Adminstrator")));

		UserLargeData userDoc = new UserLargeData(user);
		userDoc.setPhotograph(photograph);
		userDoc.setRoleIdList(roleIdList);
		Long userId = securityModule.createUser(userDoc);
		assertNotNull(userId);

		User fetchedUser = securityModule.findUser(userId);
		assertEquals(user.getLoginId(), fetchedUser.getLoginId());
		assertEquals(user.getFullName(), fetchedUser.getFullName());
		assertEquals(user.getEmail(), fetchedUser.getEmail());
		assertEquals(user.getPassword(), fetchedUser.getPassword());
		assertEquals(user.getPasswordExpires(), fetchedUser.getPasswordExpires());
	}

	@Test
	public void testFindUserPhotograph() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);

		User user
				= new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		byte[] photograph = { (byte) 0x89, (byte) 0x4E, (byte) 0xBD };

		UserLargeData userDoc = new UserLargeData(user);
		userDoc.setPhotograph(photograph);
		Long userId = securityModule.createUser(userDoc);
		byte[] fetchedPhotograph = securityModule.findUserPhotograph(userId);
		assertNotNull(fetchedPhotograph);
		assertTrue(Arrays.equals(photograph, fetchedPhotograph));
	}

	@Test
	public void testFindUserRoleIds() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);

		User user
				= new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		List<Long> roleIdList = new ArrayList<Long>();
		roleIdList.add(securityModule.createRole(getRole("sec-001", "Supervisor")));
		roleIdList.add(securityModule.createRole(getRole("sec-002", "Adminstrator")));

		UserLargeData userDoc = new UserLargeData(user);
		userDoc.setRoleIdList(roleIdList);
		Long userId = securityModule.createUser(userDoc);

		List<Long> fetchedRoleIdList = securityModule.findUserRoleIds(userId);
		assertNotNull(fetchedRoleIdList);
	}

	@Test
	public void testFindUserRoles() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);

		User user
				= new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		List<Long> roleIdList = new ArrayList<Long>();
		roleIdList.add(securityModule.createRole(getRole("sec-001", "Supervisor")));
		roleIdList.add(securityModule.createRole(getRole("sec-002", "Adminstrator")));

		UserLargeData userDoc = new UserLargeData(user);
		userDoc.setRoleIdList(roleIdList);
		Long userId = securityModule.createUser(userDoc);

		UserRoleQuery query = new UserRoleQuery();
		query.userId(userId);
		query.orderById();
		List<UserRole> userRoleList = securityModule.findUserRoles(query);
		assertNotNull(userRoleList);
		assertEquals(2, userRoleList.size());

		UserRole userRole = userRoleList.get(0);
		assertEquals(userId, userRole.getUserId());
		assertEquals(roleIdList.get(0), userRole.getRoleId());
		assertEquals("Joe Moe", userRole.getUserName());
		assertEquals("sec-001", userRole.getRoleName());
		assertEquals("Supervisor", userRole.getRoleDesc());

		userRole = userRoleList.get(1);
		assertEquals(userId, userRole.getUserId());
		assertEquals(roleIdList.get(1), userRole.getRoleId());
		assertEquals("Joe Moe", userRole.getUserName());
		assertEquals("sec-002", userRole.getRoleName());
		assertEquals("Adminstrator", userRole.getRoleDesc());
	}

	@Test
	public void testFindUsers() throws Exception {
		User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		User userB = new User(0L, "Jane Bane", "janebane", "jane.bane@thecodedepartment.com.ng",
				false);
		User userC = new User(0L, "Driss Criss", "drisscriss",
				"driss.criss@thecodedepartment.com.ng", false);

		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		securityModule.createUser(userA);
		securityModule.createUser(userB);
		securityModule.createUser(userC);
		UserQuery query = new UserQuery();
		query.notReserved();
		List<User> userList = securityModule.findUsers(query);
		assertEquals(3, userList.size());

		query = new UserQuery();
		query.fullNameLike("J");
		userList = securityModule.findUsers(query);
		assertEquals(2, userList.size());
	}

	@Test
	public void testFindUser() throws Exception {
		User user = new User(0L, "Joe Moe", "JOEMOE", "joe.moe@thecodedepartment.com.ng", false);

		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		securityModule.createUser(user);

		User fetchedUser = securityModule.findUser(user.getId());
		assertEquals(fetchedUser.getLoginId(), user.getLoginId());
		assertEquals(fetchedUser.getFullName(), user.getFullName());
		assertEquals(fetchedUser.getEmail(), user.getEmail());
		assertEquals(fetchedUser.getPassword(), user.getPassword());
		assertEquals(fetchedUser.getPasswordExpires(), user.getPasswordExpires());
	}

	@Test
	public void testUpdateUserWithWorkflowDisabled() throws Exception {
		User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);

		// Create user
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		Long id = securityModule.createUser(userA);

		// Update user
		User user = securityModule.findUser(id);
		user.setFullName("Joel Spolsky");
		user.setEmail("joel@spolsky.com");
		securityModule.updateUser(user);

		// Changes should have taken effect immediately since no workflow
		User updatedUser = securityModule.findUser(id);
		assertEquals("Joel Spolsky", updatedUser.getFullName());
		assertEquals("joel@spolsky.com", updatedUser.getEmail());
	}

	@Test
	public void testUpdateUserPhotograph() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);

		User user
				= new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		byte[] photograph1 = { (byte) 0x89, (byte) 0x4E, (byte) 0xBD };
		byte[] photograph2 = { (byte) 0xD2, (byte) 0x33 };

		UserLargeData userDoc = new UserLargeData(user);
		userDoc.setPhotograph(photograph1);
		Long userId = securityModule.createUser(userDoc);
		securityModule.updateUserPhotograph(userId, photograph2);
		byte[] fetchedPhotograph = securityModule.findUserPhotograph(userId);
		assertTrue(Arrays.equals(photograph2, fetchedPhotograph));
	}

	@Test
	public void testUpdateUserRoles() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);

		User user
				= new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		List<Long> roleIdList1 = new ArrayList<Long>();
		List<Long> roleIdList2 = new ArrayList<Long>();
		roleIdList1.add(securityModule.createRole(getRole("sec-001", "Supervisor")));
		roleIdList1.add(securityModule.createRole(getRole("sec-002", "Adminstrator")));

		UserLargeData userDoc = new UserLargeData(user);
		userDoc.setRoleIdList(roleIdList1);
		Long userId = securityModule.createUser(userDoc);
		securityModule.updateUserRoles(userId, roleIdList2);
	}

	@Test
	public void testUpdateUserByCriteria() throws Exception {
		User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		User userB = new User(0L, "Jane Bane", "janebane", "jane.bane@thecodedepartment.com.ng",
				false);
		User userC = new User(0L, "Driss Criss", "drisscriss",
				"driss.criss@thecodedepartment.com.ng", false);

		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		securityModule.createUser(userA);
		securityModule.createUser(userB);
		securityModule.createUser(userC);

		UserQuery query = new UserQuery();
		query.fullNameLike("J");
		Update update = new Update();
		update.add("email", "croc.doc@thecodedepartment.com.ng");
		int count = securityModule.updateUsers(query, update);
		assertEquals(2, count); // Only 2 record (A & B) should be
								// updated
		User user = securityModule.findUser(userA.getId());
		assertEquals("croc.doc@thecodedepartment.com.ng", user.getEmail());
		user = securityModule.findUser(userB.getId());
		assertEquals("croc.doc@thecodedepartment.com.ng", user.getEmail());
		user = securityModule.findUser(userC.getId());
		assertFalse("croc.doc@thecodedepartment.com.ng".equals(user.getEmail()));
	}

	@Test
	public void testDeleteUserById() throws Exception {
		User userA = new User(0L, "Joe Moe", "JOEMOE", "joe.moe@thecodedepartment.com.ng", false);
		User userB = new User(0L, "Jane Bane", "JANEBANE", "jane.bane@thecodedepartment.com.ng",
				false);
		User userC = new User(0L, "Driss Criss", "DRISSCRISS",
				"driss.criss@thecodedepartment.com.ng", false);

		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		securityModule.createUser(userA);
		securityModule.createUser(userB);
		securityModule.createUser(userC);

		int count = securityModule.deleteUser(userB.getId());
		assertEquals(1, count);

		UserQuery query = new UserQuery();
		query.notReserved();
		query.order("loginId");
		List<User> userList = securityModule.findUsers(query);
		assertEquals(2, userList.size());

		User fetchedUser = userList.get(0);
		assertEquals(fetchedUser.getLoginId(), userC.getLoginId());
		assertEquals(fetchedUser.getFullName(), userC.getFullName());
		assertEquals(fetchedUser.getEmail(), userC.getEmail());
		assertEquals(fetchedUser.getPassword(), userC.getPassword());
		assertEquals(fetchedUser.getPasswordExpires(), userC.getPasswordExpires());

		fetchedUser = userList.get(1);
		assertEquals(fetchedUser.getLoginId(), userA.getLoginId());
		assertEquals(fetchedUser.getFullName(), userA.getFullName());
		assertEquals(fetchedUser.getEmail(), userA.getEmail());
		assertEquals(fetchedUser.getPassword(), userA.getPassword());
		assertEquals(fetchedUser.getPasswordExpires(), userA.getPasswordExpires());
	}

	@Test
	public void testDeleteUserByCriteria() throws Exception {
		User userA = new User(0L, "Joe Moe", "JOEMOE", "joe.moe@thecodedepartment.com.ng", false);
		User userB = new User(0L, "Jane Bane", "JANEBANE", "jane.bane@thecodedepartment.com.ng",
				false);
		User userC = new User(0L, "Driss Criss", "DRISSCRISS",
				"driss.criss@thecodedepartment.com.ng", false);

		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		securityModule.createUser(userA);
		securityModule.createUser(userB);
		securityModule.createUser(userC);

		UserQuery query = new UserQuery();
		query.notReserved();
		query.fullNameLike("J");
		int count = securityModule.deleteUsers(query);
		assertEquals(2, count);

		query.clear();
		query.notReserved();
		List<User> userList = securityModule.findUsers(query);
		assertEquals(1, userList.size());
		User user = userList.get(0);
		assertEquals(user.getLoginId(), userC.getLoginId());
		assertEquals(user.getFullName(), userC.getFullName());
		assertEquals(user.getEmail(), userC.getEmail());
		assertEquals(user.getPassword(), userC.getPassword());
		assertEquals(user.getPasswordExpires(), userC.getPasswordExpires());
	}

	@Test
	public void testUserLogin() throws Exception {
		User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		securityModule.createUser(userA);
		securityModule.login("joemoe", "joemoe");
	}

	@Test
	public void testUserLogout() throws Exception {
		User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		securityModule.createUser(userA);

		securityModule.login("joemoe", "joemoe");
		UserToken userToken = securityModule.getCurrentUserToken();
		assertTrue("Joe Moe".equals(userToken.getUserName()));

		securityModule.logout(true);
		userToken = securityModule.getCurrentUserToken();
		assertNotNull(userToken);
		assertFalse("Joe Moe".equals(userToken.getUserName()));
	}

	@Test
	public void testChangeUserPassword() throws Exception {
		User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		securityModule.createUser(userA);
		securityModule.login("joemoe", "joemoe");
		securityModule.changeUserPassword("joemoe", "demo");
		securityModule.logout(true);
		securityModule.login("joemoe", "demo");
	}

	@Test
	public void testChangeUserPasswordInvalidOldPassword() throws Exception {
		User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		securityModule.createUser(userA);
		securityModule.login("joemoe", "joemoe");

		try {
			// Try to change using invalid current password
			securityModule.changeUserPassword("mosquito", "secret");

			// Should not get here
			assertTrue(false);
		} catch (UnifyException e) {
			assertEquals(SecurityModuleErrorConstants.INVALID_OLD_PASSWORD, e.getErrorCode());
		}
	}

	@Test
	public void testChangeUserPasswordHistory() throws Exception {
		// Enable password history
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);
		systemModule.setSysParameterValue(SecurityModuleSysParamConstants.ENABLE_PASSWORD_HISTORY,
				Boolean.TRUE);

		// Continue
		User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		securityModule.createUser(userA);
		securityModule.login("joemoe", "joemoe");
		securityModule.changeUserPassword("joemoe", "demo");

		try {
			// Try to change to old password. Should fail
			securityModule.changeUserPassword("demo", "joemoe");

			// Should not get here
			assertTrue(false);
		} catch (UnifyException e) {
			assertEquals(SecurityModuleErrorConstants.NEW_PASSWORD_IS_STALE, e.getErrorCode());
		}
	}

	@Test
	public void testChangeUserPasswordHistoryExpired() throws Exception {
		// Enable password history and set length
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);
		systemModule.setSysParameterValue(SecurityModuleSysParamConstants.ENABLE_PASSWORD_HISTORY,
				Boolean.TRUE);
		systemModule.setSysParameterValue(SecurityModuleSysParamConstants.PASSWORD_HISTORY_LENGTH,
				2);

		// Continue
		User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		securityModule.createUser(userA);
		securityModule.login("joemoe", "joemoe");
		securityModule.changeUserPassword("joemoe", "password1");
		securityModule.changeUserPassword("password1", "password2");
		// This should push out 'joemoe' from history
		securityModule.changeUserPassword("password2", "password3");

		// 'joemoe' can be used again here since it should expired from history
		securityModule.changeUserPassword("password3", "joemoe");
	}

	@Test
	public void testUserAccountLocking() throws Exception {
		User user = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		Long userId = securityModule.createUser(user);

		// Force account lock
		try {
			securityModule.login("joemoe", "terces");
		} catch (Exception e) {
		}
		try {
			securityModule.login("joemoe", "retaw");
		} catch (Exception e) {
		}
		try {
			securityModule.login("joemoe", "tset");
		} catch (Exception e) {
		}
		try {
			securityModule.login("joemoe", "alcatraz");
		} catch (Exception e) {
		}

		// Locked message exception should be thrown on any other attempt
		boolean isLocked = false;
		try {
			securityModule.login("joemoe", "tset");
		} catch (UnifyException ue) {
			if (!(isLocked = SecurityModuleErrorConstants.USER_ACCOUNT_IS_LOCKED
					.equals(ue.getErrorCode()))) {
				throw ue;
			}
		}
		assertTrue(isLocked);

		// Make sure that locked state is persisted
		user = securityModule.findUser(userId);
		assertTrue(user.getLoginLocked());
	}

	@Test
	public void testResetUserPassword() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		Long userId = securityModule.createUser(userA);
		securityModule.login("joemoe", "joemoe");
		securityModule.changeUserPassword("joemoe", "demo");
		securityModule.resetUserPassword(userId);
		securityModule.logout(true);
		securityModule.login("joemoe", "joemoe");
	}

	@Test
	public void testResetAccountLockedUser() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		Long userId = securityModule.createUser(userA);

		// Force account lock
		try {
			securityModule.login("joemoe", "terces");
		} catch (Exception e) {
		}
		try {
			securityModule.login("joemoe", "retaw");
		} catch (Exception e) {
		}
		try {
			securityModule.login("joemoe", "tset");
		} catch (Exception e) {
		}
		try {
			securityModule.login("joemoe", "ming");
		} catch (Exception e) {
		}

		// User should be locked
		User user = securityModule.findUser(userId);
		assertTrue(user.getLoginLocked());

		// Reset user password
		securityModule.resetUserPassword(userId);

		// Reset should have unlocked user
		user = securityModule.findUser(userId);
		assertFalse(user.getLoginLocked());
	}

	@Test
	public void testUserLoginAttempts() throws Exception {
		User user = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		Long userId = securityModule.createUser(user);
		try {
			securityModule.login("joemoe", "terces");
		} catch (Exception e) {
		}
		try {
			securityModule.login("joemoe", "retaw");
		} catch (Exception e) {
		}
		try {
			securityModule.login("joemoe", "tset");
		} catch (Exception e) {
		}

		// Make sure that login attempts is persisted
		user = securityModule.findUser(userId);
		assertEquals(Integer.valueOf(3), user.getLoginAttempts());
	}

	@Test
	public void testGetCurrentUserToken() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		User user = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);

		securityModule.createUser(user);
		securityModule.login("joemoe", "joemoe");

		UserToken userToken = securityModule.getCurrentUserToken();
		assertNotNull(userToken);
		assertEquals("JOEMOE", userToken.getUserLoginId());
		assertEquals("Joe Moe", userToken.getUserName());
	}

	@Test
	public void testSetCurrentUserRole() throws Exception {
		SecurityModule securityModule = (SecurityModule) this
				.getComponent(SecurityModuleNameConstants.SECURITYBUSINESSMODULE);
		List<Long> roleIdList = new ArrayList<Long>();
		roleIdList.add(securityModule.createRole(getRole("sec-001", "Supervisor")));
		User user
				= new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);

		UserLargeData userDoc = new UserLargeData(user);
		userDoc.setRoleIdList(roleIdList);
		Long userId = securityModule.createUser(userDoc);
		securityModule.login("joemoe", "joemoe");

		List<UserRole> userRoleList
				= securityModule.findUserRoles(new UserRoleQuery().userId(userId));
		securityModule.setCurrentUserRole(userRoleList.get(0).getId());

		UserToken userToken = securityModule.getCurrentUserToken();
		assertEquals("sec-001", userToken.getRoleCode());
	}

	@Override
	protected void onSetup() throws Exception {

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onTearDown() throws Exception {
		deleteAll(PasswordHistory.class,
				UserBiometric.class, Biometric.class, UserRole.class, User.class,
				RolePrivilegeWidget.class, RolePrivilege.class, Role.class);
	}

	private Role getRole(String name, String description) {
		Role role = new Role();
		role.setName(name);
		role.setDescription(description);
		role.setStatus(RecordStatus.ACTIVE);
		return role;
	}
}
