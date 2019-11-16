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
package com.tcdng.jacklyn.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import com.tcdng.jacklyn.common.AbstractJacklynTest;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.organization.business.OrganizationService;
import com.tcdng.jacklyn.organization.constants.OrganizationModuleNameConstants;
import com.tcdng.jacklyn.organization.entities.Branch;
import com.tcdng.jacklyn.organization.entities.Department;
import com.tcdng.jacklyn.organization.entities.Role;
import com.tcdng.jacklyn.organization.entities.RolePrivilege;
import com.tcdng.jacklyn.organization.entities.RolePrivilegeWidget;
import com.tcdng.jacklyn.security.business.SecurityService;
import com.tcdng.jacklyn.security.constants.SecurityModuleErrorConstants;
import com.tcdng.jacklyn.security.constants.SecurityModuleNameConstants;
import com.tcdng.jacklyn.security.constants.SecurityModuleSysParamConstants;
import com.tcdng.jacklyn.security.data.UserLargeData;
import com.tcdng.jacklyn.security.entities.Biometric;
import com.tcdng.jacklyn.security.entities.PasswordHistory;
import com.tcdng.jacklyn.security.entities.User;
import com.tcdng.jacklyn.security.entities.UserBiometric;
import com.tcdng.jacklyn.security.entities.UserQuery;
import com.tcdng.jacklyn.security.entities.UserRole;
import com.tcdng.jacklyn.security.entities.UserRoleQuery;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.criterion.Update;

/**
 * Security business service tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class SecurityServiceTest extends AbstractJacklynTest {

    private Long departmentId;

    @Test
    public void testCreateUser() throws Exception {
        User user = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);

        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        Long id = securityService.createUser(user);

        // Check if versioned record policy was applied
        assertNotNull(id);

        // Check if user record policy was applied
        assertEquals(Boolean.TRUE, user.getChangePassword());
        assertEquals(Integer.valueOf(0), user.getLoginAttempts());
        assertFalse(user.getLoginLocked());
    }

    @Test
    public void testCreateUserWithPhotoAndRoles() throws Exception {
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        OrganizationService organizationService =
                (OrganizationService) getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE);

        User user = new User(0L, "Joe Moe", "JOEMOE", "joe.moe@thecodedepartment.com.ng", false);
        byte[] photograph = { (byte) 0x89, (byte) 0x4E, (byte) 0xBD };
        List<Long> roleIdList = new ArrayList<Long>();
        roleIdList.add(organizationService.createRole(getRole("sec-001", "Supervisor")));
        roleIdList.add(organizationService.createRole(getRole("sec-002", "Adminstrator")));

        UserLargeData userDoc = new UserLargeData(user);
        userDoc.setPhotograph(photograph);
        userDoc.setRoleIdList(roleIdList);
        Long userId = securityService.createUser(userDoc);
        assertNotNull(userId);

        User fetchedUser = securityService.findUser(userId);
        assertEquals(user.getLoginId(), fetchedUser.getLoginId());
        assertEquals(user.getFullName(), fetchedUser.getFullName());
        assertEquals(user.getEmail(), fetchedUser.getEmail());
        assertEquals(user.getPassword(), fetchedUser.getPassword());
        assertEquals(user.getPasswordExpires(), fetchedUser.getPasswordExpires());
    }

    @Test
    public void testFindUserPhotograph() throws Exception {
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);

        User user = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        byte[] photograph = { (byte) 0x89, (byte) 0x4E, (byte) 0xBD };

        UserLargeData userDoc = new UserLargeData(user);
        userDoc.setPhotograph(photograph);
        securityService.createUser(userDoc);
        byte[] fetchedPhotograph = securityService.findUserPhotograph("joemoe");
        assertNotNull(fetchedPhotograph);
        assertTrue(Arrays.equals(photograph, fetchedPhotograph));
    }

    @Test
    public void testFindUserRoleIds() throws Exception {
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        OrganizationService organizationService =
                (OrganizationService) getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE);

        User user = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        List<Long> roleIdList = new ArrayList<Long>();
        roleIdList.add(organizationService.createRole(getRole("sec-001", "Supervisor")));
        roleIdList.add(organizationService.createRole(getRole("sec-002", "Adminstrator")));

        UserLargeData userDoc = new UserLargeData(user);
        userDoc.setRoleIdList(roleIdList);
        Long userId = securityService.createUser(userDoc);

        List<Long> fetchedRoleIdList = securityService.findUserRoleIds(userId);
        assertNotNull(fetchedRoleIdList);
    }

    @Test
    public void testFindUserRoles() throws Exception {
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        OrganizationService organizationService =
                (OrganizationService) getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE);

        User user = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        List<Long> roleIdList = new ArrayList<Long>();
        roleIdList.add(organizationService.createRole(getRole("sec-001", "Supervisor")));
        roleIdList.add(organizationService.createRole(getRole("sec-002", "Adminstrator")));

        UserLargeData userDoc = new UserLargeData(user);
        userDoc.setRoleIdList(roleIdList);
        Long userId = securityService.createUser(userDoc);

        UserRoleQuery query = new UserRoleQuery();
        query.userId(userId);
        query.orderById();
        List<UserRole> userRoleList = securityService.findUserRoles(query);
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
        User userB = new User(0L, "Jane Bane", "janebane", "jane.bane@thecodedepartment.com.ng", false);
        User userC = new User(0L, "Driss Criss", "drisscriss", "driss.criss@thecodedepartment.com.ng", false);

        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        securityService.createUser(userA);
        securityService.createUser(userB);
        securityService.createUser(userC);
        UserQuery query = new UserQuery();
        query.notReserved();
        List<User> userList = securityService.findUsers(query);
        assertEquals(3, userList.size());

        query = new UserQuery();
        query.fullNameLike("J");
        userList = securityService.findUsers(query);
        assertEquals(2, userList.size());
    }

    @Test
    public void testFindUser() throws Exception {
        User user = new User(0L, "Joe Moe", "JOEMOE", "joe.moe@thecodedepartment.com.ng", false);

        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        securityService.createUser(user);

        User fetchedUser = securityService.findUser(user.getId());
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
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        Long id = securityService.createUser(userA);

        // Update user
        User user = securityService.findUser(id);
        user.setFullName("Joel Spolsky");
        user.setEmail("joel@spolsky.com");
        securityService.updateUser(user);

        // Changes should have taken effect immediately since no workflow
        User updatedUser = securityService.findUser(id);
        assertEquals("Joel Spolsky", updatedUser.getFullName());
        assertEquals("joel@spolsky.com", updatedUser.getEmail());
    }

    @Test
    public void testUpdateUserPhotograph() throws Exception {
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);

        User user = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        byte[] photograph1 = { (byte) 0x89, (byte) 0x4E, (byte) 0xBD };
        byte[] photograph2 = { (byte) 0xD2, (byte) 0x33 };

        UserLargeData userDoc = new UserLargeData(user);
        userDoc.setPhotograph(photograph1);
        Long userId = securityService.createUser(userDoc);
        securityService.updateUserPhotograph(userId, photograph2);
        byte[] fetchedPhotograph = securityService.findUserPhotograph("joemoe");
        assertTrue(Arrays.equals(photograph2, fetchedPhotograph));
    }

    @Test
    public void testUpdateUserRoles() throws Exception {
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        OrganizationService organizationService =
                (OrganizationService) getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE);

        User user = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        List<Long> roleIdList1 = new ArrayList<Long>();
        List<Long> roleIdList2 = new ArrayList<Long>();
        roleIdList1.add(organizationService.createRole(getRole("sec-001", "Supervisor")));
        roleIdList1.add(organizationService.createRole(getRole("sec-002", "Adminstrator")));

        UserLargeData userDoc = new UserLargeData(user);
        userDoc.setRoleIdList(roleIdList1);
        Long userId = securityService.createUser(userDoc);
        securityService.updateUserRoles(userId, roleIdList2);
    }

    @Test
    public void testUpdateUserByCriteria() throws Exception {
        User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        User userB = new User(0L, "Jane Bane", "janebane", "jane.bane@thecodedepartment.com.ng", false);
        User userC = new User(0L, "Driss Criss", "drisscriss", "driss.criss@thecodedepartment.com.ng", false);

        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        securityService.createUser(userA);
        securityService.createUser(userB);
        securityService.createUser(userC);

        UserQuery query = new UserQuery();
        query.fullNameLike("J");
        Update update = new Update();
        update.add("email", "croc.doc@thecodedepartment.com.ng");
        int count = securityService.updateUsers(query, update);
        assertEquals(2, count); // Only 2 record (A & B) should be
                                // updated
        User user = securityService.findUser(userA.getId());
        assertEquals("croc.doc@thecodedepartment.com.ng", user.getEmail());
        user = securityService.findUser(userB.getId());
        assertEquals("croc.doc@thecodedepartment.com.ng", user.getEmail());
        user = securityService.findUser(userC.getId());
        assertFalse("croc.doc@thecodedepartment.com.ng".equals(user.getEmail()));
    }

    @Test
    public void testDeleteUserById() throws Exception {
        User userA = new User(0L, "Joe Moe", "JOEMOE", "joe.moe@thecodedepartment.com.ng", false);
        User userB = new User(0L, "Jane Bane", "JANEBANE", "jane.bane@thecodedepartment.com.ng", false);
        User userC = new User(0L, "Driss Criss", "DRISSCRISS", "driss.criss@thecodedepartment.com.ng", false);

        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        securityService.createUser(userA);
        securityService.createUser(userB);
        securityService.createUser(userC);

        int count = securityService.deleteUser(userB.getId());
        assertEquals(1, count);

        UserQuery query = new UserQuery();
        query.notReserved();
        query.order("loginId");
        List<User> userList = securityService.findUsers(query);
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
        User userB = new User(0L, "Jane Bane", "JANEBANE", "jane.bane@thecodedepartment.com.ng", false);
        User userC = new User(0L, "Driss Criss", "DRISSCRISS", "driss.criss@thecodedepartment.com.ng", false);

        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        securityService.createUser(userA);
        securityService.createUser(userB);
        securityService.createUser(userC);

        UserQuery query = new UserQuery();
        query.notReserved();
        query.fullNameLike("J");
        int count = securityService.deleteUsers(query);
        assertEquals(2, count);

        query.clear();
        query.notReserved();
        List<User> userList = securityService.findUsers(query);
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
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        securityService.createUser(userA);
        securityService.loginUser("joemoe", "joemoe", null);
    }

    @Test
    public void testUserLoginWithLocale() throws Exception {
        User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        securityService.createUser(userA);
        securityService.loginUser("joemoe", "joemoe", Locale.ENGLISH);
    }

    @Test
    public void testUserLogout() throws Exception {
        User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        SecurityService securityService =
                (SecurityService) this.getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        securityService.createUser(userA);

        securityService.loginUser("joemoe", "joemoe", null);
        UserToken userToken = securityService.getCurrentUserToken();
        assertTrue("Joe Moe".equals(userToken.getUserName()));

        securityService.logoutUser(true);
        userToken = securityService.getCurrentUserToken();
        assertNotNull(userToken);
        assertFalse("Joe Moe".equals(userToken.getUserName()));
    }

    @Test
    public void testChangeUserPassword() throws Exception {
        User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        securityService.createUser(userA);
        securityService.loginUser("joemoe", "joemoe", null);
        securityService.changeUserPassword("joemoe", "demo");
        securityService.logoutUser(true);
        securityService.loginUser("joemoe", "demo", null);
    }

    @Test
    public void testChangeUserPasswordInvalidOldPassword() throws Exception {
        User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        SecurityService securityService =
                (SecurityService) this.getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        securityService.createUser(userA);
        securityService.loginUser("joemoe", "joemoe", null);

        try {
            // Try to change using invalid current password
            securityService.changeUserPassword("mosquito", "secret");

            // Should not get here
            assertTrue(false);
        } catch (UnifyException e) {
            assertEquals(SecurityModuleErrorConstants.INVALID_OLD_PASSWORD, e.getErrorCode());
        }
    }

    @Test
    public void testChangeUserPasswordHistory() throws Exception {
        // Enable password history
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);
        systemService.setSysParameterValue(SecurityModuleSysParamConstants.ENABLE_PASSWORD_HISTORY, Boolean.TRUE);

        // Continue
        User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        securityService.createUser(userA);
        securityService.loginUser("joemoe", "joemoe", null);
        securityService.changeUserPassword("joemoe", "demo");

        try {
            // Try to change to old password. Should fail
            securityService.changeUserPassword("demo", "joemoe");

            // Should not get here
            assertTrue(false);
        } catch (UnifyException e) {
            assertEquals(SecurityModuleErrorConstants.NEW_PASSWORD_IS_STALE, e.getErrorCode());
        }
    }

    @Test
    public void testChangeUserPasswordHistoryExpired() throws Exception {
        // Enable password history and set length
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);
        systemService.setSysParameterValue(SecurityModuleSysParamConstants.ENABLE_PASSWORD_HISTORY, Boolean.TRUE);
        systemService.setSysParameterValue(SecurityModuleSysParamConstants.PASSWORD_HISTORY_LENGTH, 2);

        // Continue
        User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        securityService.createUser(userA);
        securityService.loginUser("joemoe", "joemoe", null);
        securityService.changeUserPassword("joemoe", "password1");
        securityService.changeUserPassword("password1", "password2");
        // This should push out 'joemoe' from history
        securityService.changeUserPassword("password2", "password3");

        // 'joemoe' can be used again here since it should expired from history
        securityService.changeUserPassword("password3", "joemoe");
    }

    @Test
    public void testUserAccountLocking() throws Exception {
        User user = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        Long userId = securityService.createUser(user);

        // Force account lock
        try {
            securityService.loginUser("joemoe", "terces", null);
        } catch (Exception e) {
        }
        try {
            securityService.loginUser("joemoe", "retaw", null);
        } catch (Exception e) {
        }
        try {
            securityService.loginUser("joemoe", "tset", null);
        } catch (Exception e) {
        }
        try {
            securityService.loginUser("joemoe", "alcatraz", null);
        } catch (Exception e) {
        }

        // Locked message exception should be thrown on any other attempt
        boolean isLocked = false;
        try {
            securityService.loginUser("joemoe", "tset", null);
        } catch (UnifyException ue) {
            if (!(isLocked = SecurityModuleErrorConstants.USER_ACCOUNT_IS_LOCKED.equals(ue.getErrorCode()))) {
                throw ue;
            }
        }
        assertTrue(isLocked);

        // Make sure that locked state is persisted
        user = securityService.findUser(userId);
        assertTrue(user.getLoginLocked());
    }

    @Test
    public void testResetUserPassword() throws Exception {
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        Long userId = securityService.createUser(userA);
        securityService.loginUser("joemoe", "joemoe", null);
        securityService.changeUserPassword("joemoe", "demo");
        securityService.resetUserPassword(userId);
        securityService.logoutUser(true);
        securityService.loginUser("joemoe", "joemoe", null);
    }

    @Test
    public void testResetAccountLockedUser() throws Exception {
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        User userA = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        Long userId = securityService.createUser(userA);

        // Force account lock
        try {
            securityService.loginUser("joemoe", "terces", null);
        } catch (Exception e) {
        }
        try {
            securityService.loginUser("joemoe", "retaw", null);
        } catch (Exception e) {
        }
        try {
            securityService.loginUser("joemoe", "tset", null);
        } catch (Exception e) {
        }
        try {
            securityService.loginUser("joemoe", "ming", null);
        } catch (Exception e) {
        }

        // User should be locked
        User user = securityService.findUser(userId);
        assertTrue(user.getLoginLocked());

        // Reset user password
        securityService.resetUserPassword(userId);

        // Reset should have unlocked user
        user = securityService.findUser(userId);
        assertFalse(user.getLoginLocked());
    }

    @Test
    public void testUserLoginAttempts() throws Exception {
        User user = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        Long userId = securityService.createUser(user);
        try {
            securityService.loginUser("joemoe", "terces", null);
        } catch (Exception e) {
        }
        try {
            securityService.loginUser("joemoe", "retaw", null);
        } catch (Exception e) {
        }
        try {
            securityService.loginUser("joemoe", "tset", null);
        } catch (Exception e) {
        }

        // Make sure that login attempts is persisted
        user = securityService.findUser(userId);
        assertEquals(Integer.valueOf(3), user.getLoginAttempts());
    }

    @Test
    public void testGetCurrentUserToken() throws Exception {
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        User user = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);

        securityService.createUser(user);
        securityService.loginUser("joemoe", "joemoe", null);

        UserToken userToken = securityService.getCurrentUserToken();
        assertNotNull(userToken);
        assertEquals("JOEMOE", userToken.getUserLoginId());
        assertEquals("Joe Moe", userToken.getUserName());
    }

    @Test
    public void testSetCurrentUserRole() throws Exception {
        SecurityService securityService = (SecurityService) getComponent(SecurityModuleNameConstants.SECURITYSERVICE);
        OrganizationService organizationService =
                (OrganizationService) getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE);
        List<Long> roleIdList = new ArrayList<Long>();
        roleIdList.add(organizationService.createRole(getRole("sec-001", "Supervisor")));
        User user = new User(0L, "Joe Moe", "joemoe", "joe.moe@thecodedepartment.com.ng", false);

        UserLargeData userDoc = new UserLargeData(user);
        userDoc.setRoleIdList(roleIdList);
        Long userId = securityService.createUser(userDoc);
        securityService.loginUser("joemoe", "joemoe", null);

        List<UserRole> userRoleList = securityService.findUserRoles(new UserRoleQuery().userId(userId));
        securityService.setCurrentUserRole(userRoleList.get(0));

        UserToken userToken = securityService.getCurrentUserToken();
        assertEquals("sec-001", userToken.getRoleCode());
    }

    @Override
    protected void onSetup() throws Exception {
        departmentId = createTestDepartment();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onTearDown() throws Exception {
        deleteAll(PasswordHistory.class, UserBiometric.class, Biometric.class, UserRole.class, User.class,
                RolePrivilegeWidget.class, RolePrivilege.class, Role.class, Department.class, Branch.class);
    }

    private Long createTestDepartment() throws Exception {
        Department department = new Department();
        department.setName("testDepartment");
        department.setDescription("Test Department");
        return ((OrganizationService) getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE))
                .createDepartment(department);
    }

    private Role getRole(String name, String description) {
        Role role = new Role();
        role.setDepartmentId(departmentId);
        role.setName(name);
        role.setDescription(description);
        role.setStatus(RecordStatus.ACTIVE);
        return role;
    }
}
