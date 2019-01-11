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
import com.tcdng.jacklyn.security.data.ClientAppLargeData;
import com.tcdng.jacklyn.security.data.UserLargeData;
import com.tcdng.jacklyn.security.entities.Biometric;
import com.tcdng.jacklyn.security.entities.BiometricQuery;
import com.tcdng.jacklyn.security.entities.ClientApp;
import com.tcdng.jacklyn.security.entities.ClientAppAsset;
import com.tcdng.jacklyn.security.entities.ClientAppAssetQuery;
import com.tcdng.jacklyn.security.entities.ClientAppQuery;
import com.tcdng.jacklyn.security.entities.PasswordHistory;
import com.tcdng.jacklyn.security.entities.PasswordHistoryQuery;
import com.tcdng.jacklyn.security.entities.User;
import com.tcdng.jacklyn.security.entities.UserBiometric;
import com.tcdng.jacklyn.security.entities.UserBiometricQuery;
import com.tcdng.jacklyn.security.entities.UserQuery;
import com.tcdng.jacklyn.security.entities.UserRole;
import com.tcdng.jacklyn.security.entities.UserRoleQuery;
import com.tcdng.jacklyn.shared.constants.OrientationType;
import com.tcdng.jacklyn.shared.security.BiometricCategory;
import com.tcdng.jacklyn.shared.security.BiometricType;
import com.tcdng.jacklyn.shared.security.data.OSInstallationReqParams;
import com.tcdng.jacklyn.shared.security.data.OSInstallationReqResult;
import com.tcdng.jacklyn.shared.system.ClientAppType;
import com.tcdng.jacklyn.shared.system.SystemAssetType;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.system.constants.SystemModuleErrorConstants;
import com.tcdng.jacklyn.system.constants.SystemModuleSysParamConstants;
import com.tcdng.jacklyn.system.constants.SystemReservedUserConstants;
import com.tcdng.jacklyn.system.entities.Dashboard;
import com.tcdng.jacklyn.system.entities.DashboardLayer;
import com.tcdng.jacklyn.system.entities.DashboardPortlet;
import com.tcdng.jacklyn.system.entities.SystemAssetQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Broadcast;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.TransactionAttribute;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.operation.Update;
import com.tcdng.unify.core.security.OneWayStringCryptograph;
import com.tcdng.unify.core.security.PasswordGenerator;
import com.tcdng.unify.core.system.UserSessionManager;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.util.WebUtils;

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

    @Configurable("oneway-stringcryptograph")
    private OneWayStringCryptograph passwordCryptograph;

    private Map<String, Set<String>> clientAppAccessFlags;

    public SecurityServiceImpl() {
        clientAppAccessFlags = new HashMap<String, Set<String>>();
    }

    @Override
    public Long createClientApp(ClientAppLargeData clientAppLargeData) throws UnifyException {
        Long clientAppId = (Long) db().create(clientAppLargeData.getData());
        updateClientAppAssets(clientAppId, clientAppLargeData.getSystemAssetIdList());
        return clientAppId;
    }

    @Override
    public ClientAppLargeData findClientApp(Long id) throws UnifyException {
        ClientApp clientApp = db().list(ClientApp.class, id);
        List<Long> clientAppAssetIdList =
                db().valueList(Long.class, "systemAssetId", new ClientAppAssetQuery().clientAppId(id));
        return new ClientAppLargeData(clientApp, clientAppAssetIdList);
    }

    @Override
    public List<ClientApp> findClientApps(ClientAppQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateClientApp(ClientAppLargeData clientAppLargeData) throws UnifyException {
        int result = db().updateByIdVersion(clientAppLargeData.getData());
        updateClientAppAssets(clientAppLargeData.getId(), clientAppLargeData.getSystemAssetIdList());
        clearClientAppAssetAccess(clientAppLargeData.getData().getName());
        return result;
    }

    @Override
    public int deleteClientApp(Long id) throws UnifyException {
        db().deleteAll(new ClientAppAssetQuery().clientAppId(id));
        return db().delete(ClientApp.class, id);
    }

    @Override
    public boolean accessSystemAsset(String clientAppName, SystemAssetType systemAssetType, String assetName)
            throws UnifyException {
        Set<String> accessFlags = clientAppAccessFlags.get(clientAppName);
        if (accessFlags == null) {
            accessFlags = new HashSet<String>();
            clientAppAccessFlags.put(clientAppName, accessFlags);
        }

        String assetCheckKey = StringUtils.dotify(systemAssetType, assetName);
        if (!accessFlags.contains(assetCheckKey)) {
            if (db().countAll(new ClientAppQuery().name(clientAppName)) == 0) {
                throw new UnifyException(SystemModuleErrorConstants.APPLICATION_UNKNOWN, clientAppName);
            }

            ClientAppAsset clientAppAsset =
                    db().list(new ClientAppAssetQuery().clientAppName(clientAppName).assetType(systemAssetType)
                            .assetName(assetName));
            if (clientAppAsset == null) {
                throw new UnifyException(SystemModuleErrorConstants.APPLICATION_NO_SUCH_ASSET, clientAppName,
                        assetName);
            }

            if (RecordStatus.INACTIVE.equals(clientAppAsset.getClientAppStatus())) {
                throw new UnifyException(SystemModuleErrorConstants.APPLICATION_INACTIVE, clientAppName);
            }

            if (RecordStatus.INACTIVE.equals(clientAppAsset.getAssetStatus())) {
                throw new UnifyException(SystemModuleErrorConstants.APPLICATION_ASSET_INACTIVE, clientAppName,
                        assetName);
            }

            accessFlags.add(assetCheckKey);
        }

        return true;
    }

    @Override
    public OSInstallationReqResult processOSInstallationRequest(OSInstallationReqParams oSInstallationReqParams)
            throws UnifyException {
        logDebug("Processing OS installation request for [{0}]...", oSInstallationReqParams.getOsName());

        // Create application here
        boolean isAlreadyInstalled = true;
        Long clientAppId = null;
        ClientApp oldClientApp =
                db().list(new ClientAppQuery().name(oSInstallationReqParams.getClientAppCode()).type(ClientAppType.OS));
        if (oldClientApp == null) {
            logDebug("Creating application of type OS...");
            ClientApp clientApp = new ClientApp();
            clientApp.setName(oSInstallationReqParams.getClientAppCode());
            clientApp.setDescription(oSInstallationReqParams.getOsName());
            clientApp.setType(ClientAppType.OS);
            clientAppId = (Long) db().create(clientApp);
            isAlreadyInstalled = false;
        } else {
            logDebug("...application [{0}] of type OS is already installed.", oSInstallationReqParams.getOsName());
            oldClientApp.setDescription(oSInstallationReqParams.getOsName());
            db().updateByIdVersion(oldClientApp);
            clientAppId = oldClientApp.getId();
        }

        // Grant OS access to all remote calls.
        List<Long> systemAssetIdList =
                systemService.findSystemAssetIds((SystemAssetQuery) new SystemAssetQuery()
                        .type(SystemAssetType.REMOTECALLMETHOD).installed(Boolean.TRUE));
        updateClientAppAssets(clientAppId, systemAssetIdList);

        // Return result
        logDebug("Preparing installation result...");
        OSInstallationReqResult airResult = new OSInstallationReqResult();
        airResult.setAppName(getApplicationName());
        airResult.setAppName(getApplicationName());
        String bannerFilename =
                WebUtils.expandThemeTag(systemService.getSysParameterValue(String.class,
                        SystemModuleSysParamConstants.SYSPARAM_APPLICATION_BANNER));
        byte[] icon = IOUtils.readFileResourceInputStream(bannerFilename);
        airResult.setAppIcon(icon);
        airResult.setAlreadyInstalled(isAlreadyInstalled);
        logDebug("OS installation for [{0}] completed.", oSInstallationReqParams.getOsName());
        return airResult;
    }

    @Broadcast
    public void clearClientAppAssetAccess(String... params) throws UnifyException {
        for (String clientAppCode : params) {
            clientAppAccessFlags.remove(clientAppCode);
        }
    }

    private void updateClientAppAssets(Long clientAppId, List<Long> systemAssetIdList) throws UnifyException {
        db().deleteAll(new ClientAppAssetQuery().clientAppId(clientAppId));
        ClientAppAsset clientAppAsset = new ClientAppAsset();
        clientAppAsset.setClientAppId(clientAppId);
        for (Long systemAssetId : systemAssetIdList) {
            clientAppAsset.setSystemAssetId(systemAssetId);
            db().create(clientAppAsset);
        }
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
    public int countUsers(UserQuery query) throws UnifyException {
        return db().countAll(query);
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
                organizationService.loadRoleAttributesToApplication(new String[] { userRole.getRoleName() });
            }

            UserToken userToken = getUserToken();
            userToken.setRoleCode(userRole.getRoleName());
            userToken.setThemePath(getUserRoleThemeResourcePath(userRole.getId()));
            setSessionAttribute(JacklynSessionAttributeConstants.ROLEDESCRIPTION, userRole.getRoleDesc());
        } else {
            setSessionAttribute(JacklynSessionAttributeConstants.ROLEDESCRIPTION, getUserToken().getUserName());
        }
    }

    @Override
    public String getCurrentUserRoleDashboardViewer() throws UnifyException {
        UserToken userToken = getUserToken();
        String dashboardName = null;
        if (!userToken.isReservedUser()) {
            dashboardName = organizationService.getRoleDashboard(getUserToken().getRoleCode());
            if (StringUtils.isBlank(dashboardName)) {
                dashboardName =
                        systemService.getSysParameterValue(String.class,
                                SecurityModuleSysParamConstants.DEFAULT_DASHBOARD);
            }
        } else {
            dashboardName =
                    systemService.getSysParameterValue(String.class,
                            SecurityModuleSysParamConstants.DEFAULT_SYSTEM_DASHBOARD);
        }

        return systemService.getRuntimeDashboardDef(dashboardName).getViewer();
    }

    @Override
    public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {
        logInfo("Managing security module...");
        if (db().countAll(new UserQuery().id(SystemReservedUserConstants.SYSTEM_ID)) == 0) {
            createUser(new User(SystemReservedUserConstants.SYSTEM_ID, "System",
                    SystemReservedUserConstants.SYSTEM_LOGINID, "info@tcdng.com", Boolean.FALSE));
        }

        if (db().countAll(new UserQuery().id(SystemReservedUserConstants.ANONYMOUS_ID)) == 0) {
            createUser(new User(SystemReservedUserConstants.ANONYMOUS_ID, "Anonymous",
                    SystemReservedUserConstants.ANONYMOUS_LOGINID, "info@tcdng.com", Boolean.FALSE));
        }

        // Check for default dashboard and create if necessary
        String dashboardName =
                systemService.getSysParameterValue(String.class, SecurityModuleSysParamConstants.DEFAULT_DASHBOARD);
        Dashboard dashboard = systemService.findDashboard(dashboardName);
        if (dashboard == null) {
            dashboard = new Dashboard(OrientationType.VERTICAL, dashboardName, "Default Dashboard");
            systemService.createDashboard(dashboard);
        }

        // Check for default system dashboard and create if necessary
        dashboardName =
                systemService.getSysParameterValue(String.class,
                        SecurityModuleSysParamConstants.DEFAULT_SYSTEM_DASHBOARD);
        dashboard = systemService.findDashboard(dashboardName);
        if (dashboard == null) {
            dashboard = new Dashboard(OrientationType.VERTICAL, dashboardName, "Default System Dashboard");
            List<DashboardLayer> layerList = new ArrayList<DashboardLayer>();
            layerList.add(new DashboardLayer("layer1", "Layer One", 1));
            layerList.add(new DashboardLayer("layer2", "Layer Two", 1));
            layerList.add(new DashboardLayer("layer3", "Layer Three", 1));
            layerList.add(new DashboardLayer("layer4", "Layer Four", 1));
            dashboard.setLayerList(layerList);

            List<DashboardPortlet> portletList = new ArrayList<DashboardPortlet>();
            portletList
                    .add(new DashboardPortlet("usersOnline", "Users Online", "layer1", "ui-usersonlineportlet", 1, 30));
            portletList.add(new DashboardPortlet("memUtilization", "Memory Utilization", "layer2",
                    "ui-memoryutilizationportlet", 1, 40));
            dashboard.setPortletList(portletList);
            systemService.createDashboard(dashboard);
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
        if (!globalAccess) {
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
                            .usingDictionaryEntry("password", password).sendVia(notificationChannelName).build();
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
}
