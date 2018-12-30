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
package com.tcdng.jacklyn.security.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.common.constants.JacklynApplicationAttributeConstants;
import com.tcdng.jacklyn.common.constants.JacklynSessionAttributeConstants;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.data.ManagedEntityPrivilegeNames;
import com.tcdng.jacklyn.common.utils.JacklynUtils;
import com.tcdng.jacklyn.notification.business.NotificationService;
import com.tcdng.jacklyn.notification.data.Message;
import com.tcdng.jacklyn.notification.utils.NotificationUtils;
import com.tcdng.jacklyn.organization.business.OrganizationService;
import com.tcdng.jacklyn.security.constants.SecurityModuleErrorConstants;
import com.tcdng.jacklyn.security.constants.SecurityModuleNameConstants;
import com.tcdng.jacklyn.security.constants.SecurityModuleSysParamConstants;
import com.tcdng.jacklyn.security.data.RoleLargeData;
import com.tcdng.jacklyn.security.data.UserLargeData;
import com.tcdng.jacklyn.security.entities.Biometric;
import com.tcdng.jacklyn.security.entities.BiometricQuery;
import com.tcdng.jacklyn.security.entities.PasswordHistory;
import com.tcdng.jacklyn.security.entities.PasswordHistoryQuery;
import com.tcdng.jacklyn.security.entities.Privilege;
import com.tcdng.jacklyn.security.entities.PrivilegeCategory;
import com.tcdng.jacklyn.security.entities.PrivilegeCategoryQuery;
import com.tcdng.jacklyn.security.entities.PrivilegeGroup;
import com.tcdng.jacklyn.security.entities.PrivilegeGroupQuery;
import com.tcdng.jacklyn.security.entities.PrivilegeQuery;
import com.tcdng.jacklyn.security.entities.Role;
import com.tcdng.jacklyn.security.entities.RolePrivilege;
import com.tcdng.jacklyn.security.entities.RolePrivilegeQuery;
import com.tcdng.jacklyn.security.entities.RolePrivilegeWidget;
import com.tcdng.jacklyn.security.entities.RolePrivilegeWidgetQuery;
import com.tcdng.jacklyn.security.entities.RoleQuery;
import com.tcdng.jacklyn.security.entities.RoleWfStep;
import com.tcdng.jacklyn.security.entities.RoleWfStepQuery;
import com.tcdng.jacklyn.security.entities.User;
import com.tcdng.jacklyn.security.entities.UserBiometric;
import com.tcdng.jacklyn.security.entities.UserBiometricQuery;
import com.tcdng.jacklyn.security.entities.UserQuery;
import com.tcdng.jacklyn.security.entities.UserRole;
import com.tcdng.jacklyn.security.entities.UserRoleQuery;
import com.tcdng.jacklyn.shared.security.BiometricCategory;
import com.tcdng.jacklyn.shared.security.BiometricType;
import com.tcdng.jacklyn.shared.security.PrivilegeCategoryConstants;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.jacklyn.shared.xml.config.module.PrivilegeConfig;
import com.tcdng.jacklyn.shared.xml.config.module.PrivilegeGroupConfig;
import com.tcdng.jacklyn.shared.xml.util.WfNameUtils;
import com.tcdng.jacklyn.system.constants.SystemReservedUserConstants;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.workflow.business.WorkflowService;
import com.tcdng.jacklyn.workflow.entities.WfStep;
import com.tcdng.jacklyn.workflow.entities.WfStepQuery;
import com.tcdng.unify.core.PrivilegeSettings;
import com.tcdng.unify.core.RoleAttributes;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Broadcast;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.annotation.TransactionAttribute;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.TriState;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.operation.Update;
import com.tcdng.unify.core.security.OneWayStringCryptograph;
import com.tcdng.unify.core.security.PasswordGenerator;
import com.tcdng.unify.core.system.UserSessionManager;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.constant.SessionAttributeConstants;

/**
 * Default implementation of security business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(SecurityModuleNameConstants.SECURITYSERVICE)
public class SecurityServiceImpl extends AbstractJacklynBusinessService implements SecurityService {

    @Configurable
    private UserSessionManager userSessionManager;

    @Configurable
    private SystemService systemService;

    @Configurable
    private NotificationService notificationService;

    @Configurable
    private OrganizationService organizationService;

    @Configurable
    private WorkflowService workflowService;

    @Configurable("oneway-stringcryptograph")
    private OneWayStringCryptograph passwordCryptograph;

    @Override
    public Long createRole(Role role) throws UnifyException {
        return (Long) db().create(role);
    }

    @Override
    public Long createRole(RoleLargeData roleFormData) throws UnifyException {
        Long roleId = (Long) db().create(roleFormData.getData());
        updateRolePrivileges(roleId, roleFormData.getPrivilegeIdList());
        updateRoleWorkflowSteps(roleId, roleFormData.getWfStepIdList());
        return roleId;
    }

    @Override
    public Role findRole(Long roleId) throws UnifyException {
        return db().list(Role.class, roleId);
    }

    @Override
    public RoleLargeData findRoleForm(Long roleId) throws UnifyException {
        Role role = db().list(Role.class, roleId);
        List<Long> privilegeIdList =
                db().valueList(Long.class, "privilegeId", new RolePrivilegeQuery().roleId(roleId).orderById());
        List<Long> wfStepIdList = getWfStepIdListForRole(roleId);
        return new RoleLargeData(role, privilegeIdList, wfStepIdList);
    }

    @Override
    public List<Role> findRoles(RoleQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateRole(Role role) throws UnifyException {
        return db().updateByIdVersion(role);
    }

    @Override
    public int updateRole(RoleLargeData roleFormData) throws UnifyException {
        Role role = roleFormData.getData();
        int updateCount = db().updateByIdVersion(role);
        updateRolePrivileges(role.getId(), roleFormData.getPrivilegeIdList());
        updateRoleWorkflowSteps(role.getId(), roleFormData.getWfStepIdList());
        return updateCount;
    }

    @Override
    public int deleteRole(Long id) throws UnifyException {
        db().deleteAll(new RolePrivilegeWidgetQuery().roleId(id));
        db().deleteAll(new RolePrivilegeQuery().roleId(id));
        db().deleteAll(new RoleWfStepQuery().roleId(id));
        return db().delete(Role.class, id);
    }

    @Synchronized("register-privilege-category")
    @Override
    public Long registerPrivilegeCategory(String categoryName, String descriptionKey) throws UnifyException {
        PrivilegeCategory privilegeCategory = findPrivilegeCategory(categoryName);
        String description = getApplicationMessage(descriptionKey);
        if (privilegeCategory == null) {
            privilegeCategory = new PrivilegeCategory();
            privilegeCategory.setName(categoryName);
            privilegeCategory.setDescription(description);
            privilegeCategory.setStatus(RecordStatus.ACTIVE);
            return (Long) db().create(privilegeCategory);
        }

        privilegeCategory.setDescription(description);
        privilegeCategory.setStatus(RecordStatus.ACTIVE);
        db().updateById(privilegeCategory);
        return privilegeCategory.getId();
    }

    @Synchronized("register-privilege")
    @Override
    public Long registerPrivilege(String categoryName, String moduleName, String privilegeName, String privilegeDesc)
            throws UnifyException {
        PrivilegeGroup privilegeGroup =
                db().find(new PrivilegeGroupQuery().categoryName(categoryName).moduleName(moduleName));
        Long privilegeGroupId = null;
        if (privilegeGroup == null) {
            privilegeGroup = new PrivilegeGroup();
            privilegeGroup.setPrivilegeCategoryId(
                    db().value(Long.class, "id", new PrivilegeCategoryQuery().name(categoryName)));
            privilegeGroup.setModuleId(systemService.getModuleId(moduleName));
            privilegeGroupId = (Long) db().create(privilegeGroup);
        } else {
            privilegeGroupId = privilegeGroup.getId();
        }

        Privilege privilege = db().find(new PrivilegeQuery().privilegeGroupId(privilegeGroupId).name(privilegeName));
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setPrivilegeGroupId(privilegeGroupId);
            privilege.setName(privilegeName);
            privilege.setDescription(privilegeDesc);
            privilege.setStatus(RecordStatus.ACTIVE);
            return (Long) db().create(privilege);
        }
        return privilege.getId();
    }

    @Synchronized("update-privilege")
    @Override
    public boolean updateRegisteredPrivilege(String categoryName, String moduleName, String privilegeName,
            String privilegeDesc) throws UnifyException {
        Privilege privilege =
                db().find(new PrivilegeQuery().categoryName(categoryName).moduleName(moduleName).name(privilegeName));
        if (privilege != null) {
            privilege.setDescription(privilegeDesc);
            db().updateById(privilege);
            return true;
        }
        return false;
    }

    @Synchronized("unregister-privilege")
    @Override
    public void unregisterPrivilege(String categoryName, String moduleName, String... privilegeName)
            throws UnifyException {
        for (String name : privilegeName) {
            Privilege privilege =
                    db().find(new PrivilegeQuery().categoryName(categoryName).moduleName(moduleName).name(name));
            if (privilege != null) {
                Long privilegeId = privilege.getId();
                db().deleteAll(new RolePrivilegeQuery().privilegeId(privilegeId));
                db().delete(Privilege.class, privilegeId);
            }
        }
    }

    @Override
    public PrivilegeCategory findPrivilegeCategory(String name) throws UnifyException {
        return db().list(new PrivilegeCategoryQuery().name(name));
    }

    @Override
    public List<PrivilegeCategory> findPrivilegeCategories(PrivilegeCategoryQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updatePrivilegeCategory(PrivilegeCategory privilegeCategory) throws UnifyException {
        return db().updateById(privilegeCategory);
    }

    @Override
    public List<PrivilegeGroup> findPrivilegeGroups(PrivilegeGroupQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public List<Privilege> findPrivileges(PrivilegeQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public List<Long> findPrivilegeIds(PrivilegeQuery query) throws UnifyException {
        return db().valueList(Long.class, "id", query);
    }

    @Override
    public int setPrivilegeStatuses(List<Long> privilegeIds, RecordStatus status) throws UnifyException {
        return db().updateAll(new PrivilegeQuery().idIn(privilegeIds), new Update().add("status", status));
    }

    @Override
    public List<RolePrivilegeWidget> findRoleDocumentControls(RolePrivilegeWidgetQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateRoleDocumentControls(List<RolePrivilegeWidget> rolePrivilegeWidgetList) throws UnifyException {
        for (RolePrivilegeWidget rolePrivilegeWidget : rolePrivilegeWidgetList) {
            db().updateById(rolePrivilegeWidget);
        }
        return rolePrivilegeWidgetList.size();
    }

    @Override
    public List<Long> findPrivilegeIds(RolePrivilegeQuery query) throws UnifyException {
        return db().valueList(Long.class, "privilegeId", query);
    }

    @Override
    public int updateRolePrivileges(Long roleId, List<Long> privilegeIdList) throws UnifyException {
        int updateCount = 0;
        if (privilegeIdList != null && !privilegeIdList.isEmpty()) {
            Set<Long> oldPrivilegeIds =
                    db().valueSet(Long.class, "privilegeId", new RolePrivilegeQuery().roleId(roleId));
            List<Long> existPrivilegeList = new ArrayList<Long>();
            List<Long> newPrivilegeList = new ArrayList<Long>();
            for (Long id : privilegeIdList) {
                if (oldPrivilegeIds.contains(id)) {
                    existPrivilegeList.add(id);
                } else {
                    newPrivilegeList.add(id);
                }
            }

            // Delete discarded privileges and widget privileges
            RolePrivilegeQuery query = new RolePrivilegeQuery().roleId(roleId);
            if (!existPrivilegeList.isEmpty()) {
                query.privilegeIdNotIn(existPrivilegeList);
            }
            updateCount = deleteRolePrivileges(query);

            // Create new privileges
            updateCount += createRolePrivileges(roleId, newPrivilegeList);
        } else {
            // Delete old privileges.
            updateCount = deleteRolePrivileges(new RolePrivilegeQuery().roleId(roleId));
        }

        if (updateCount > 0) {
            String roleName = db().value(String.class, "name", new RoleQuery().id(roleId));
            if (isRoleAttributes(roleName)) {
                loadRoleAttributesToApplication(new String[] { roleName });
            }
        }
        return updateCount;
    }

    @Override
    public int updateRoleWorkflowSteps(Long roleId, List<Long> wfStepIdList) throws UnifyException {
        int updateCount = 0;

        // Delete old step privileges.
        updateCount = db().deleteAll(new RoleWfStepQuery().roleId(roleId));

        if (!DataUtils.isBlank(wfStepIdList)) {
            // Create new privileges
            RoleWfStep roleWfStep = new RoleWfStep();
            roleWfStep.setRoleId(roleId);

            List<WfStep> wfStepList =
                    workflowService.findSteps(
                            ((WfStepQuery) new WfStepQuery().idIn(wfStepIdList).select("wfTemplateId", "name")));
            for (WfStep wfStepData : wfStepList) {
                roleWfStep.setWfTemplateId(wfStepData.getWfTemplateId());
                roleWfStep.setStepName(wfStepData.getName());
                db().create(roleWfStep);
            }

            updateCount += wfStepIdList.size();
        }

        if (updateCount > 0) {
            String roleName = db().value(String.class, "name", new RoleQuery().id(roleId));
            if (isRoleAttributes(roleName)) {
                loadRoleAttributesToApplication(new String[] { roleName });
            }
        }

        return updateCount;
    }

    @Override
    public Long createBiometric(BiometricCategory category, BiometricType type, byte[] image) throws UnifyException {
        Biometric biometric = new Biometric();
        biometric.setCategory(category);
        biometric.setType(type);
        biometric.setBiometric(image);
        return (Long) db().create(biometric);
    }

    @Override
    public Long createUser(User user) throws UnifyException {
        String password = generatePassword(user, SecurityModuleSysParamConstants.NEW_PASSWORD_MESSAGE_TEMPLATE);
        user.setPassword(passwordCryptograph.encrypt(password));
        return (Long) db().create(user);
    }

    @Override
    public Long createUser(UserLargeData userDocument) throws UnifyException {
        // Create user
        User user = userDocument.getData();
        String password = generatePassword(user, SecurityModuleSysParamConstants.NEW_PASSWORD_MESSAGE_TEMPLATE);
        user.setPassword(passwordCryptograph.encrypt(password));
        Long userId = (Long) db().create(user);

        // Create biometric record
        UserBiometric userBiometric = new UserBiometric();
        Long biometricId =
                createBiometric(BiometricCategory.USERS, BiometricType.PHOTOGRAPH, userDocument.getPhotograph());
        userBiometric.setUserId(userId);
        userBiometric.setBiometricId(biometricId);
        db().create(userBiometric);

        // Create roles
        updateUserRoles(userId, userDocument.getRoleIdList());

        return userId;
    }

    @Override
    public List<User> findUsers(UserQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public User findUser(Long id) throws UnifyException {
        return db().list(User.class, id);
    }

    @Override
    public User findUser(String loginId) throws UnifyException {
        return db().list(new UserQuery().loginId(loginId));
    }

    @Override
    public int updateUser(User user) throws UnifyException {
        return db().updateByIdVersion(user);
    }

    @Override
    public int updateUser(UserLargeData userDocument) throws UnifyException {
        User user = userDocument.getData();
        int result = db().updateByIdVersion(user);
        updateUserPhotograph(user.getId(), userDocument.getPhotograph());
        updateUserRoles(user.getId(), userDocument.getRoleIdList());
        return result;
    }

    @Override
    public int updateUserPhotograph(Long userId, byte[] photograph) throws UnifyException {
        Long biometricId =
                db().value(Long.class, "biometricId",
                        new UserBiometricQuery().typeName(BiometricType.PHOTOGRAPH).userId(userId));
        return db().updateAll(new BiometricQuery().id(biometricId), new Update().add("biometric", photograph));
    }

    @Override
    public int updateUserRoles(Long userId, List<Long> roleIdList) throws UnifyException {
        int updateCount = 0;
        db().deleteAll(new UserRoleQuery().userId(userId));
        if (QueryUtils.isValidListCriteria(roleIdList)) {
            Set<Long> existSet = new HashSet<Long>();
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            for (Long roleId : roleIdList) {
                // Make sure we don't create duplicate roles for user
                if (!existSet.contains(roleId)) {
                    userRole.setRoleId(roleId);
                    db().create(userRole);
                    existSet.add(roleId);
                    updateCount++;
                }
            }
        }
        return updateCount;
    }

    @Override
    public int updateUsers(UserQuery query, Update update) throws UnifyException {
        return db().updateAll(query, update);
    }

    @Override
    public int deleteUser(Long id) throws UnifyException {
        // Delete biometric
        List<Long> biometricIdList = db().valueList(Long.class, "biometricId", new UserBiometricQuery().userId(id));
        if (!biometricIdList.isEmpty()) {
            db().deleteAll(new UserBiometricQuery().userId(id));
            db().deleteAll(new BiometricQuery().idIn(biometricIdList));
        }

        // Delete roles
        db().deleteAll(new UserRoleQuery().userId(id));

        return db().delete(User.class, id);
    }

    @Override
    public int deleteUsers(UserQuery query) throws UnifyException {
        List<Long> idList = db().valueList(Long.class, "id", query);
        for (Long userId : idList) {
            deleteUser(userId);
        }
        return idList.size();
    }

    @Override
    public UserLargeData findUserDocument(Long userId) throws UnifyException {
        User user = db().list(User.class, userId);
        byte[] photograph =
                db().value(byte[].class, "biometric",
                        new UserBiometricQuery().userId(userId).typeName(BiometricType.PHOTOGRAPH).mustMatch(false));
        List<Long> roleIdList = db().valueList(Long.class, "roleId", new UserRoleQuery().userId(userId).orderById());
        return new UserLargeData(user, photograph, roleIdList);
    }

    @Override
    public byte[] findUserPhotograph(Long userId) throws UnifyException {
        return db().value(byte[].class, "biometric",
                new UserBiometricQuery().userId(userId).typeName(BiometricType.PHOTOGRAPH).mustMatch(false));
    }

    @Override
    public List<Long> findUserRoleIds(Long userId) throws UnifyException {
        return db().valueList(Long.class, "roleId", new UserRoleQuery().userId(userId).orderById());
    }

    @Override
    public List<UserRole> findUserRoles(UserRoleQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public User login(String loginId, String password) throws UnifyException {
        User user = db().list(new UserQuery().loginId(loginId));
        if (user == null) {
            throw new UnifyException(SecurityModuleErrorConstants.INVALID_LOGIN_ID_PASSWORD);
        }

        boolean accountLockingEnabled =
                systemService.getSysParameterValue(boolean.class,
                        SecurityModuleSysParamConstants.ENABLE_ACCOUNT_LOCKING);
        if (accountLockingEnabled && user.getLoginLocked()) {
            throw new UnifyException(SecurityModuleErrorConstants.USER_ACCOUNT_IS_LOCKED);
        }

        if (!RecordStatus.ACTIVE.equals(user.getStatus())) {
            throw new UnifyException(SecurityModuleErrorConstants.USER_ACCOUNT_NOT_ACTIVE);
        }

        if (SystemReservedUserConstants.ANONYMOUS_ID.equals(user.getId())) {
            throw new UnifyException(SecurityModuleErrorConstants.LOGIN_AS_ANONYMOUS_NOT_ALLOWED);
        }

        password = passwordCryptograph.encrypt(password);
        if (!user.getPassword().equals(password)) {
            if (accountLockingEnabled) {
                updateLoginAttempts(user);
            }

            throw new UnifyException(SecurityModuleErrorConstants.INVALID_LOGIN_ID_PASSWORD);
        }

        Update update = new Update().add("lastLoginDt", new Date()).add("loginAttempts", Integer.valueOf(0));
        Date paswwordExpiryDt = user.getPasswordExpiryDt();
        if (paswwordExpiryDt != null && paswwordExpiryDt.before(new Date())) {
            update.add("changePassword", Boolean.TRUE);
            user.setChangePassword(Boolean.TRUE);
        }
        db().updateAll(new UserQuery().id(user.getId()), update);

        // Login to session and set session attributes
        userSessionManager.logIn(createUserToken(user));
        setSessionAttribute(JacklynSessionAttributeConstants.USERID, user.getId());
        setSessionAttribute(JacklynSessionAttributeConstants.USERNAME, user.getFullName());
        setSessionAttribute(JacklynSessionAttributeConstants.BRANCHID, user.getBranchId());
        String branchDesc = user.getBranchDesc();
        if (StringUtils.isBlank(branchDesc)) {
            branchDesc = getApplicationMessage("application.no.branch");
        }
        setSessionAttribute(JacklynSessionAttributeConstants.BRANCHDESC, branchDesc);
        setSessionAttribute(JacklynSessionAttributeConstants.RESERVEDFLAG, user.isReserved());
        setSessionAttribute(JacklynSessionAttributeConstants.SUPERVISORFLAG, user.getSupervisor());
        setSessionAttribute(JacklynSessionAttributeConstants.USERROLEOPTIONS, null);
        setSessionAttribute(JacklynSessionAttributeConstants.REPORTOPTIONS, null);
        setSessionAttribute(JacklynSessionAttributeConstants.MESSAGEBOX, null);
        setSessionAttribute(JacklynSessionAttributeConstants.TASKMONITORINFO, null);
        setSessionAttribute(JacklynSessionAttributeConstants.SHORTCUTDECK, null);

        return user;
    }

    @Transactional(TransactionAttribute.REQUIRES_NEW)
    public void updateLoginAttempts(User user) throws UnifyException {
        int loginAttempts = user.getLoginAttempts() + 1;
        Update update = new Update();
        update.add("loginAttempts", Integer.valueOf(loginAttempts));
        if (systemService.getSysParameterValue(int.class,
                SecurityModuleSysParamConstants.MAXIMUM_LOGIN_TRIES) <= loginAttempts) {
            update.add("loginLocked", Boolean.TRUE);
        }

        db().updateAll(new UserQuery().id(user.getId()), update);
    }

    @Override
    public void logout(boolean complete) throws UnifyException {
        userSessionManager.logOut(complete);
    }

    @Override
    public void changeUserPassword(String oldPassword, String newPassword) throws UnifyException {
        oldPassword = passwordCryptograph.encrypt(oldPassword);
        User user = db().find(new UserQuery().password(oldPassword).loginId(getUserToken().getUserLoginId()));
        if (user == null) {
            throw new UnifyException(SecurityModuleErrorConstants.INVALID_OLD_PASSWORD);
        }

        Long userId = user.getId();
        newPassword = passwordCryptograph.encrypt(newPassword);
        if (systemService.getSysParameterValue(boolean.class,
                SecurityModuleSysParamConstants.ENABLE_PASSWORD_HISTORY)) {
            PasswordHistoryQuery query = new PasswordHistoryQuery().userId(userId).password(newPassword);
            if (db().countAll(query) > 0) {
                throw new UnifyException(SecurityModuleErrorConstants.NEW_PASSWORD_IS_STALE);
            }

            query.clear();
            query.userId(userId);
            query.orderById();
            List<Long> passwordHistoryIdList = db().valueList(Long.class, "id", query);
            if (passwordHistoryIdList.size() >= systemService.getSysParameterValue(int.class,
                    SecurityModuleSysParamConstants.PASSWORD_HISTORY_LENGTH)) {
                db().delete(PasswordHistory.class, passwordHistoryIdList.get(0));
            }

            PasswordHistory passwordHistory = new PasswordHistory();
            passwordHistory.setUserId(userId);
            passwordHistory.setPassword(oldPassword);
            db().create(passwordHistory);
        }

        // Update user
        user.setPassword(newPassword);
        user.setPasswordExpiryDt(null);
        user.setChangePassword(Boolean.FALSE);
        db().updateByIdVersion(user);
    }

    @Override
    public void resetUserPassword(Long userId) throws UnifyException {
        // Reset password.
        User user = db().find(User.class, userId);
        String password = generatePassword(user, SecurityModuleSysParamConstants.RESET_PASSWORD_MESSAGE_TEMPLATE);
        password = passwordCryptograph.encrypt(password);
        user.setPassword(password);
        user.setChangePassword(Boolean.TRUE);
        user.setLoginLocked(Boolean.FALSE);
        db().updateByIdVersion(user);
    }

    @Override
    public UserToken getCurrentUserToken() throws UnifyException {
        return getUserToken();
    }

    @Override
    public void setCurrentUserRole(Long userRoleId) throws UnifyException {
        if (userRoleId != null) {
            UserRole userRole = db().list(UserRole.class, userRoleId);
            if (!isRoleAttributes(userRole.getRoleName())) {
                loadRoleAttributesToApplication(new String[] { userRole.getRoleName() });
            }

            UserToken userToken = getUserToken();
            userToken.setRoleCode(userRole.getRoleName());
            userToken.setThemePath(getUserRoleThemeResourcePath(userRole.getId()));
            setSessionAttribute(JacklynSessionAttributeConstants.ROLEDESCRIPTION, userRole.getRoleDesc());
        } else {
            setSessionAttribute(JacklynSessionAttributeConstants.ROLEDESCRIPTION, getUserToken().getUserName());
        }
    }

    @Broadcast
    @Override
    public synchronized void loadRoleAttributesToApplication(String... roleNames) throws UnifyException {
        if (roleNames.length > 0) {
            for (String roleName : roleNames) {
                // Do document privileges
                Map<String, PrivilegeSettings> docPrivilegeSettings = new HashMap<String, PrivilegeSettings>();
                List<Long> rolePrivilegeIdList =
                        db().valueList(Long.class, "id", new RolePrivilegeQuery().roleName(roleName)
                                .categoryName(PrivilegeCategoryConstants.DOCUMENTCONTROL));
                if (!rolePrivilegeIdList.isEmpty()) {
                    List<RolePrivilegeWidget> rolePrivilegeWidgetList =
                            db().listAll(new RolePrivilegeWidgetQuery().rolePrivilegeIdIn(rolePrivilegeIdList));
                    for (RolePrivilegeWidget rolePrivilegeWidget : rolePrivilegeWidgetList) {
                        docPrivilegeSettings.put(rolePrivilegeWidget.getPrivilegeName(),
                                new PrivilegeSettings(rolePrivilegeWidget.isVisible(), rolePrivilegeWidget.isEditable(),
                                        rolePrivilegeWidget.isDisabled(),
                                        TriState.getTriState(rolePrivilegeWidget.isRequired())));
                    }
                }

                // Do non-document privileges
                List<RolePrivilege> rolePrivilegeList =
                        db().listAll(new RolePrivilegeQuery().roleName(roleName)
                                .categoryNameNot(PrivilegeCategoryConstants.DOCUMENTCONTROL));
                Map<String, Set<String>> nonWidgetPrivilegeNames = new HashMap<String, Set<String>>();
                Set<String> allAccessWidgetPrivileges = new HashSet<String>();
                for (RolePrivilege rpd : rolePrivilegeList) {
                    String categoryName = rpd.getCategoryName();
                    if (PrivilegeCategoryConstants.APPLICATIONUI.equals(categoryName)) {
                        allAccessWidgetPrivileges.add(rpd.getPrivilegeName());
                    } else {
                        Set<String> privilegeNameList = nonWidgetPrivilegeNames.get(categoryName);
                        if (privilegeNameList == null) {
                            privilegeNameList = new HashSet<String>();
                            nonWidgetPrivilegeNames.put(categoryName, privilegeNameList);
                        }
                        privilegeNameList.add(rpd.getPrivilegeName());
                    }
                }

                // Workflow steps
                Set<String> wfStepNames = new HashSet<String>();
                List<RoleWfStep> roleWfStepList = db().listAll(new RoleWfStepQuery().roleName(roleName));
                for (RoleWfStep roleWfStep : roleWfStepList) {
                    wfStepNames.add(WfNameUtils.getGlobalStepName(roleWfStep.getWfCategoryName(),
                            roleWfStep.getWfTemplateName(), roleWfStep.getStepName()));
                }

                // Create and set role attributes
                Role role = db().find(new RoleQuery().name(roleName));
                setRoleAttributes(role.getName(), new RoleAttributes(role.getName(), role.getDescription(),
                        docPrivilegeSettings, allAccessWidgetPrivileges, nonWidgetPrivilegeNames, wfStepNames));
            }

            broadcastToOtherSessions(SessionAttributeConstants.REFRESH_MENU, Boolean.TRUE);
        }
    }

    @Override
    public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {
        logInfo("Managing security...");
        logDebug("Registering security privilege categories...");
        registerPrivilegeCategory(PrivilegeCategoryConstants.APPLICATIONUI, "reserved.privilegecategory.applicationui");
        registerPrivilegeCategory(PrivilegeCategoryConstants.SHORTCUT, "reserved.privilegecategory.shortcut");
        registerPrivilegeCategory(PrivilegeCategoryConstants.DOCUMENTCONTROL,
                "reserved.privilegecategory.documentcontrol");
        registerPrivilegeCategory(PrivilegeCategoryConstants.REPORTABLE, "reserved.privilegecategory.reportable");

        // Uninstall old
        db().updateAll(new PrivilegeQuery(), new Update().add("installed", Boolean.FALSE));

        // Install new and update old
        Map<String, PrivilegeCategory> categoryMap =
                db().listAllMap(String.class, "name", new PrivilegeCategoryQuery().status(RecordStatus.ACTIVE));

        Privilege privilege = new Privilege();
        privilege.setStatus(RecordStatus.ACTIVE);
        privilege.setInstalled(Boolean.TRUE);
        for (ModuleConfig moduleConfig : moduleConfigList) {
            if (moduleConfig.getPrivileges() != null) {
                logDebug("Installing security privilege definitions for module [{0}]...",
                        resolveApplicationMessage(moduleConfig.getDescription()));
                PrivilegeQuery pQuery = new PrivilegeQuery();
                Long moduleId = systemService.getModuleId(moduleConfig.getName());

                PrivilegeGroup privilegeGroup = new PrivilegeGroup();
                privilegeGroup.setModuleId(moduleId);
                for (PrivilegeGroupConfig privilegeGroupConfig : moduleConfig.getPrivileges().getPrivilegeGroupList()) {
                    Long privilegeCategoryId = categoryMap.get(privilegeGroupConfig.getCategory()).getId();
                    PrivilegeGroup oldPrivilegeGroup =
                            db().find(new PrivilegeGroupQuery().moduleId(moduleId)
                                    .privilegeCategoryId(privilegeCategoryId));
                    Long privilegeGroupId = null;
                    if (oldPrivilegeGroup == null) {
                        privilegeGroup.setPrivilegeCategoryId(privilegeCategoryId);
                        privilegeGroupId = (Long) db().create(privilegeGroup);
                    } else {
                        oldPrivilegeGroup.setPrivilegeCategoryId(privilegeCategoryId);
                        db().updateByIdVersion(oldPrivilegeGroup);
                        privilegeGroupId = oldPrivilegeGroup.getId();
                    }

                    privilege.setPrivilegeGroupId(privilegeGroupId);
                    for (PrivilegeConfig privilegeConfig : privilegeGroupConfig.getPrivilegeList()) {
                        pQuery.clear();
                        Privilege oldPrivilege =
                                db().find(pQuery.privilegeGroupId(privilegeGroupId).name(privilegeConfig.getName()));
                        String description = resolveApplicationMessage(privilegeConfig.getDescription());
                        if (oldPrivilege == null) {
                            privilege.setName(privilegeConfig.getName());
                            privilege.setDescription(description);
                            db().create(privilege);
                        } else {
                            oldPrivilege.setName(privilegeConfig.getName());
                            oldPrivilege.setDescription(description);
                            oldPrivilege.setInstalled(Boolean.TRUE);
                            db().updateByIdVersion(oldPrivilege);
                        }
                    }
                }
            }
        }

        if (db().countAll(new UserQuery().id(SystemReservedUserConstants.SYSTEM_ID)) == 0) {
            createUser(new User(SystemReservedUserConstants.SYSTEM_ID, "System",
                    SystemReservedUserConstants.SYSTEM_LOGINID, "info@tcdng.com", Boolean.FALSE));
        }

        if (db().countAll(new UserQuery().id(SystemReservedUserConstants.ANONYMOUS_ID)) == 0) {
            createUser(new User(SystemReservedUserConstants.ANONYMOUS_ID, "Anonymous",
                    SystemReservedUserConstants.ANONYMOUS_LOGINID, "info@tcdng.com", Boolean.FALSE));
        }
    }

    @Override
    public void onApplicationStartup() throws UnifyException {
        // Managed privileges
        Map<Class<? extends Entity>, ManagedEntityPrivilegeNames> managedPrivileges =
                new HashMap<Class<? extends Entity>, ManagedEntityPrivilegeNames>();
        for (Class<? extends Entity> entityClass : getAnnotatedClasses(Entity.class, Managed.class)) {
            String title = JacklynUtils.generateManagedRecordTitle(entityClass);
            title = resolveApplicationMessage(title);
            String reportableName = JacklynUtils.generateManagedRecordReportableName(entityClass, title);
            managedPrivileges.put(entityClass, new ManagedEntityPrivilegeNames(reportableName));
        }

        setApplicationAttribute(JacklynApplicationAttributeConstants.MANAGED_PRIVILEGES,
                Collections.unmodifiableMap(managedPrivileges));
    }

    @Override
    public void onApplicationShutdown() throws UnifyException {

    }

    private UserToken createUserToken(User user) throws UnifyException {
        boolean allowMultipleLogin = user.isReserved();
        if (!allowMultipleLogin) {
            allowMultipleLogin = Boolean.TRUE.equals(user.getAllowMultipleLogin());
        }

        if (systemService.getSysParameterValue(boolean.class,
                SecurityModuleSysParamConstants.ENABLE_SYSTEMWIDE_MULTILOGIN_RULE)) {
            allowMultipleLogin =
                    systemService.getSysParameterValue(boolean.class,
                            SecurityModuleSysParamConstants.SYSTEMWIDE_MULTILOGIN);
        }

        boolean globalAccess = user.isReserved();
        if(!globalAccess) {
            globalAccess = organizationService.getBranchHeadOfficeFlag(user.getBranchId());
        }

        return new UserToken(user.getLoginId(), user.getFullName(), getSessionContext().getRemoteAddress(),
                user.getId(), user.getBranchName(), globalAccess, user.isReserved(), allowMultipleLogin, false);
    }

    private String generatePassword(User user, String sysParamNotificationTemplateName) throws UnifyException {
        PasswordGenerator passwordGenerator =
                (PasswordGenerator) getComponent(systemService.getSysParameterValue(String.class,
                        SecurityModuleSysParamConstants.USER_PASSWORD_GENERATOR));
        int passwordLength =
                systemService.getSysParameterValue(int.class, SecurityModuleSysParamConstants.USER_PASSWORD_LENGTH);

        String password = passwordGenerator.generatePassword(user.getLoginId(), passwordLength);

        // Send email if necessary
        if (systemService.getSysParameterValue(boolean.class,
                SecurityModuleSysParamConstants.USER_PASSWORD_SEND_EMAIL)) {
            String notificationTemplateName =
                    systemService.getSysParameterValue(String.class, sysParamNotificationTemplateName);
            String notificationChannelName =
                    systemService.getSysParameterValue(String.class,
                            SecurityModuleSysParamConstants.SECURITY_EMAIL_CHANNEL);
            String administratorName =
                    systemService.getSysParameterValue(String.class,
                            SecurityModuleSysParamConstants.ADMINISTRATOR_NAME);
            String administratorEmail =
                    systemService.getSysParameterValue(String.class,
                            SecurityModuleSysParamConstants.ADMINISTRATOR_EMAIL);
            Message message =
                    Message.newBuilder(NotificationUtils.getGlobalTemplateName(
                            SecurityModuleNameConstants.SECURITY_MODULE, notificationTemplateName))
                                    .fromSender(administratorName, administratorEmail)
                                    .toRecipient(user.getFullName(), user.getEmail())
                                    .usingDictionaryEntry("loginId", user.getLoginId())
                                    .usingDictionaryEntry("password", password).sendVia(notificationChannelName)
                                    .build();
            notificationService.sendNotification(message);
        }

        return password;
    }

    private String getUserRoleThemeResourcePath(Long userRoleId) throws UnifyException {
        UserRole userRole = db().list(new UserRoleQuery().id(userRoleId));
        Long themeId = null;
        if (systemService.getSysParameterValue(boolean.class, SecurityModuleSysParamConstants.ENABLE_USER_THEMES)) {
            themeId = userRole.getUserThemeId();
        }

        if (themeId == null && systemService.getSysParameterValue(boolean.class,
                SecurityModuleSysParamConstants.ENABLE_ROLE_THEMES)) {
            themeId = userRole.getRoleThemeId();
        }

        if (themeId != null) {
            return systemService.findTheme(themeId).getResourcePath();
        }

        return null;
    }

    private int createRolePrivileges(Long roleId, List<Long> privilegeIdList) throws UnifyException {
        // Create new privileges
        RolePrivilege rolePrivilege = new RolePrivilege();
        RolePrivilegeWidget rolePrivilegeWidget = new RolePrivilegeWidget();
        rolePrivilege.setRoleId(roleId);
        for (Long privilegeId : privilegeIdList) {
            rolePrivilege.setPrivilegeId(privilegeId);
            Long rolePrivilegeId = (Long) db().create(rolePrivilege);
            String category = db().value(String.class, "categoryName", new PrivilegeQuery().id(privilegeId));
            if (PrivilegeCategoryConstants.DOCUMENTCONTROL.equals(category)) {
                rolePrivilegeWidget.setRolePrivilegeId(rolePrivilegeId);
                rolePrivilegeWidget.setVisible(true);
                rolePrivilegeWidget.setEditable(true);
                rolePrivilegeWidget.setDisabled(false);
                rolePrivilegeWidget.setRequired(false);
                db().create(rolePrivilegeWidget);
            }
        }
        return privilegeIdList.size();
    }

    private int deleteRolePrivileges(RolePrivilegeQuery query) throws UnifyException {
        List<Long> rolePrivilegeIds = db().valueList(Long.class, "id", query);
        if (!rolePrivilegeIds.isEmpty()) {
            db().deleteAll(new RolePrivilegeWidgetQuery().rolePrivilegeIdIn(rolePrivilegeIds));
        }
        return db().deleteAll(query);
    }

    private List<Long> getWfStepIdListForRole(Long roleId) throws UnifyException {
        List<Long> wfStepIdList = new ArrayList<Long>();
        Set<Long> wfTemplateIds = db().valueSet(Long.class, "wfTemplateId", new RoleWfStepQuery().roleId(roleId));
        for (Long wfTemplateId : wfTemplateIds) {
            Set<String> stepNames =
                    db().valueSet(String.class, "stepName",
                            new RoleWfStepQuery().roleId(roleId).wfTemplateId(wfTemplateId));
            wfStepIdList.addAll(workflowService.findStepIds(wfTemplateId, stepNames));
        }

        return wfStepIdList;
    }
}
