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
package com.tcdng.jacklyn.organization.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.organization.constants.OrganizationModuleNameConstants;
import com.tcdng.jacklyn.organization.data.RoleLargeData;
import com.tcdng.jacklyn.organization.entities.Branch;
import com.tcdng.jacklyn.organization.entities.BranchQuery;
import com.tcdng.jacklyn.organization.entities.Department;
import com.tcdng.jacklyn.organization.entities.DepartmentQuery;
import com.tcdng.jacklyn.organization.entities.Hub;
import com.tcdng.jacklyn.organization.entities.HubQuery;
import com.tcdng.jacklyn.organization.entities.Privilege;
import com.tcdng.jacklyn.organization.entities.PrivilegeCategory;
import com.tcdng.jacklyn.organization.entities.PrivilegeCategoryQuery;
import com.tcdng.jacklyn.organization.entities.PrivilegeGroup;
import com.tcdng.jacklyn.organization.entities.PrivilegeGroupQuery;
import com.tcdng.jacklyn.organization.entities.PrivilegeQuery;
import com.tcdng.jacklyn.organization.entities.Role;
import com.tcdng.jacklyn.organization.entities.RolePrivilege;
import com.tcdng.jacklyn.organization.entities.RolePrivilegeQuery;
import com.tcdng.jacklyn.organization.entities.RolePrivilegeWidget;
import com.tcdng.jacklyn.organization.entities.RolePrivilegeWidgetQuery;
import com.tcdng.jacklyn.organization.entities.RoleQuery;
import com.tcdng.jacklyn.organization.entities.RoleWfStep;
import com.tcdng.jacklyn.organization.entities.RoleWfStepQuery;
import com.tcdng.jacklyn.shared.organization.PrivilegeCategoryConstants;
import com.tcdng.jacklyn.shared.organization.RoleWfStepType;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.jacklyn.shared.xml.config.module.PrivilegeConfig;
import com.tcdng.jacklyn.shared.xml.config.module.PrivilegeGroupConfig;
import com.tcdng.jacklyn.shared.xml.util.WfNameUtils;
import com.tcdng.jacklyn.shared.xml.util.WfNameUtils.StepNameParts;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.workflow.business.WorkflowService;
import com.tcdng.jacklyn.workflow.entities.WfStep;
import com.tcdng.jacklyn.workflow.entities.WfStepQuery;
import com.tcdng.unify.core.RoleAttributes;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.ViewDirective;
import com.tcdng.unify.core.annotation.Broadcast;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.TriState;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Default implementation of organization business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(OrganizationModuleNameConstants.ORGANIZATIONSERVICE)
public class OrganizationServiceImpl extends AbstractJacklynBusinessService implements OrganizationService {

    private static final String REGISTER_PRIVILEGE_CATEGORY_LOCK = "org::registerprivilegecat-lock";

    private static final String REGISTER_PRIVILEGE_LOCK = "org::registerprivilege-lock";

    private static final String UPDATE_PRIVILEGE_LOCK = "org::updateprivilege-lock";

    private static final String UNREGISTER_PRIVILEGE_LOCK = "org::unregisterprivilege-lock";

    @Configurable
    private SystemService systemService;

    @Configurable
    private WorkflowService workflowService;

    @Override
    public Long createBranch(Branch branch) throws UnifyException {
        return (Long) db().create(branch);
    }

    @Override
    public Branch findBranch(Long branchId) throws UnifyException {
        return db().find(Branch.class, branchId);
    }

    @Override
    public Long findBranchId(String branchCode) throws UnifyException {
        return db().value(Long.class, "id", new BranchQuery().code(branchCode));
    }

    @Override
    public Branch findBranch(BranchQuery query) throws UnifyException {
        return db().find(query);
    }

    @Override
    public List<Branch> findBranches(BranchQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
	public Map<Long, Branch> findBranchesMapById(BranchQuery query) throws UnifyException {
		return db().findAllMap(Long.class, "id", query);
	}

	@Override
    public int updateBranch(Branch branch) throws UnifyException {
        return db().updateByIdVersion(branch);
    }

    @Override
    public int deleteBranch(Long id) throws UnifyException {
        return db().delete(Branch.class, id);
    }

    @Override
    public boolean getBranchHeadOfficeFlag(Long id) throws UnifyException {
        if (id != null) {
            return db().value(boolean.class, "headOffice", new BranchQuery().id(id));
        }

        return false;
    }

    @Override
    public Long createDepartment(Department department) throws UnifyException {
        return (Long) db().create(department);
    }

    @Override
    public Department findDepartment(Long departmentId) throws UnifyException {
        return db().find(Department.class, departmentId);
    }

    @Override
    public Department findDepartment(DepartmentQuery query) throws UnifyException {
        return db().find(query);
    }

    @Override
    public List<Department> findDepartments(DepartmentQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateDepartment(Department department) throws UnifyException {
        return db().updateByIdVersion(department);
    }

    @Override
    public int deleteDepartment(Long id) throws UnifyException {
        return db().delete(Department.class, id);
    }

    @Override
    public Long createHub(Hub hub) throws UnifyException {
        return (Long) db().create(hub);
    }

    @Override
    public Hub findHub(Long hubId) throws UnifyException {
        return db().find(Hub.class, hubId);
    }

    @Override
    public Hub findHub(HubQuery query) throws UnifyException {
        return db().find(query);
    }

    @Override
    public List<Hub> findHubs(HubQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateHub(Hub hub) throws UnifyException {
        return db().updateByIdVersion(hub);
    }

    @Override
    public int deleteHub(Long id) throws UnifyException {
        return db().delete(Hub.class, id);
    }

    @Override
    public List<Branch> findHubBranchesByHub(Long hubId) throws UnifyException {
        return db().listAll(new BranchQuery().hubId(hubId).addOrder("description"));
    }

    @Override
    public List<Branch> findHubBranchesByBranch(Long branchId) throws UnifyException {
        Long hubId = db().value(Long.class, "hubId", new BranchQuery().id(branchId));
        return db().listAll(new BranchQuery().hubId(hubId).addOrder("description"));
    }

    @Override
    public List<Long> findHubBranchIdsByBranch(String branchCode) throws UnifyException {
        Long hubId = db().value(Long.class, "hubId", new BranchQuery().code(branchCode));
        return db().valueList(Long.class, "id", new BranchQuery().hubId(hubId));
    }

    @Override
    public List<Branch> findHubBranchesByHub(String hubCode) throws UnifyException {
        return db().listAll(new BranchQuery().hubName(hubCode).addOrder("description"));
    }

    @Override
    public List<Branch> findHubBranchesByBranch(String branchCode) throws UnifyException {
        Long hubId = db().value(Long.class, "hubId", new BranchQuery().code(branchCode));
        return db().listAll(new BranchQuery().hubId(hubId).addOrder("description"));
    }

    @Override
    public Hub findHubByBranch(String branchCode) throws UnifyException {
        Long hubId = db().value(Long.class, "hubId", new BranchQuery().code(branchCode));
        return db().list(Hub.class, hubId);
    }

    @Override
    public Long createRole(Role role) throws UnifyException {
        return (Long) db().create(role);
    }

    @Override
    public Long createRole(RoleLargeData roleFormData) throws UnifyException {
        Long roleId = (Long) db().create(roleFormData.getData());
        updateRolePrivileges(roleId, roleFormData.getPrivilegeIdList());
        updateRoleWorkflowSteps(RoleWfStepType.USER_INTERACT, roleId, roleFormData.getWfStepIdList());
        updateRoleWorkflowSteps(RoleWfStepType.NOTIFY_UNATTENDED, roleId, roleFormData.getWfNotifStepIdList());
        return roleId;
    }

    @Override
    public Role findRole(Long roleId) throws UnifyException {
        return db().list(Role.class, roleId);
    }

    @Override
    public Role findRole(RoleQuery query) throws UnifyException {
        return db().find(query);
    }

    @Override
    public RoleLargeData findRoleForm(Long roleId) throws UnifyException {
        Role role = db().list(Role.class, roleId);
        List<Long> privilegeIdList =
                db().valueList(Long.class, "privilegeId", new RolePrivilegeQuery().roleId(roleId).orderById());
        List<Long> wfStepIdList = getWfStepIdListForRole(RoleWfStepType.USER_INTERACT, roleId);
        List<Long> wfNotifStepIdList = getWfStepIdListForRole(RoleWfStepType.NOTIFY_UNATTENDED, roleId);
        return new RoleLargeData(role, privilegeIdList, wfStepIdList, wfNotifStepIdList);
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
        updateRoleWorkflowSteps(RoleWfStepType.USER_INTERACT, role.getId(), roleFormData.getWfStepIdList());
        updateRoleWorkflowSteps(RoleWfStepType.NOTIFY_UNATTENDED, role.getId(), roleFormData.getWfNotifStepIdList());
        return updateCount;
    }

    @Override
    public int deleteRole(Long id) throws UnifyException {
        db().deleteAll(new RolePrivilegeWidgetQuery().roleId(id));
        db().deleteAll(new RolePrivilegeQuery().roleId(id));
        db().deleteAll(new RoleWfStepQuery().roleId(id));
        return db().delete(Role.class, id);
    }

    @Override
    public String getRoleDashboard(String roleName) throws UnifyException {
        return db().value(String.class, "dashboardName", new RoleQuery().name(roleName));
    }

    @Synchronized(REGISTER_PRIVILEGE_CATEGORY_LOCK)
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

    @Synchronized(REGISTER_PRIVILEGE_LOCK)
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

    @Synchronized(UPDATE_PRIVILEGE_LOCK)
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

    @Synchronized(UNREGISTER_PRIVILEGE_LOCK)
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
    public int updateRoleWorkflowSteps(RoleWfStepType type, Long roleId, List<Long> wfStepIdList)
            throws UnifyException {
        int updateCount = 0;

        // Delete old step privileges.
        updateCount = db().deleteAll(new RoleWfStepQuery().type(type).roleId(roleId));

        if (DataUtils.isNotBlank(wfStepIdList)) {
            // Create new privileges
            RoleWfStep roleWfStep = new RoleWfStep();
            roleWfStep.setType(type);
            roleWfStep.setRoleId(roleId);

            List<WfStep> wfStepList =
                    workflowService.findSteps(
                            ((WfStepQuery) new WfStepQuery().idIn(wfStepIdList).addSelect("wfTemplateId", "name")));
            for (WfStep wfStep : wfStepList) {
                roleWfStep.setWfTemplateId(wfStep.getWfTemplateId());
                roleWfStep.setStepName(wfStep.getName());
                db().create(roleWfStep);
            }

            updateCount += wfStepIdList.size() * 2;
        }

        if (updateCount > 0) {
            String roleName = db().value(String.class, "name", new RoleQuery().id(roleId));
            if (isRoleAttributes(roleName)) {
                loadRoleAttributesToApplication(new String[] { roleName });
            }
        }

        return updateCount;
    }

    @Broadcast
    @Override
    public synchronized void loadRoleAttributesToApplication(String... roleNames) throws UnifyException {
        if (roleNames.length > 0) {
            for (String roleName : roleNames) {
                // Do dynamic view directive privileges
                Map<String, ViewDirective> dynamicViewDirectives = new HashMap<String, ViewDirective>();
                List<Long> rolePrivilegeIdList =
                        db().valueList(Long.class, "id", new RolePrivilegeQuery().roleName(roleName)
                                .categoryName(PrivilegeCategoryConstants.DOCUMENTCONTROL));
                if (!rolePrivilegeIdList.isEmpty()) {
                    List<RolePrivilegeWidget> rolePrivilegeWidgetList =
                            db().listAll(new RolePrivilegeWidgetQuery().rolePrivilegeIdIn(rolePrivilegeIdList));
                    for (RolePrivilegeWidget rolePrivilegeWidget : rolePrivilegeWidgetList) {
                        dynamicViewDirectives.put(rolePrivilegeWidget.getPrivilegeName(),
                                new ViewDirective(rolePrivilegeWidget.isVisible(), rolePrivilegeWidget.isEditable(),
                                        rolePrivilegeWidget.isDisabled(),
                                        TriState.getTriState(rolePrivilegeWidget.isRequired())));
                    }
                }

                // Do non-dynamic privileges
                List<RolePrivilege> rolePrivilegeList =
                        db().listAll(new RolePrivilegeQuery().roleName(roleName)
                                .categoryNameNot(PrivilegeCategoryConstants.DOCUMENTCONTROL));
                Map<String, Set<String>> nonViewDirectivePrivilegeCodes = new HashMap<String, Set<String>>();
                Set<String> staticViewDirectivePrivilegeCodes = new HashSet<String>();
                for (RolePrivilege rpd : rolePrivilegeList) {
                    String categoryName = rpd.getCategoryName();
                    if (PrivilegeCategoryConstants.APPLICATIONUI.equals(categoryName)) {
                        // View directive privileges
                        staticViewDirectivePrivilegeCodes.add(rpd.getPrivilegeName());
                    } else {
                        Set<String> privilegeCodes = nonViewDirectivePrivilegeCodes.get(categoryName);
                        if (privilegeCodes == null) {
                            privilegeCodes = new HashSet<String>();
                            nonViewDirectivePrivilegeCodes.put(categoryName, privilegeCodes);
                        }
                        privilegeCodes.add(rpd.getPrivilegeName());
                    }
                }

                // Workflow steps
                Set<String> wfStepCodes = new HashSet<String>();
                List<RoleWfStep> roleWfStepList =
                        db().listAll(new RoleWfStepQuery().type(RoleWfStepType.USER_INTERACT).roleName(roleName));
                for (RoleWfStep roleWfStep : roleWfStepList) {
                    wfStepCodes.add(WfNameUtils.getStepGlobalName(roleWfStep.getWfCategoryName(),
                            roleWfStep.getWfTemplateName(), roleWfStep.getStepName()));
                }

                // Create and set role attributes
                Role role = db().find(new RoleQuery().name(roleName));
                setRoleAttributes(role.getName(),
                        new RoleAttributes(role.getName(), role.getDescription(), dynamicViewDirectives,
                                staticViewDirectivePrivilegeCodes, nonViewDirectivePrivilegeCodes, wfStepCodes));
            }

            broadcastRefreshMenu();
        }
    }

    @Override
    public List<String> findWfStepRoles(RoleWfStepType type, String stepGlobalName) throws UnifyException {
        StepNameParts stepNameParts = WfNameUtils.getStepNameParts(stepGlobalName);
        return db().valueList(String.class, "roleName",
                new RoleWfStepQuery().type(type).stepName(stepNameParts.getStepName())
                        .wfTemplateName(stepNameParts.getTemplateName())
                        .wfCategoryName(stepNameParts.getCategoryName()));
    }

    @Override
    public boolean confirmUserPrivilege(Long roleId, String privilegeCatCode, String privilegeCode)
            throws UnifyException {
        return db().countAll(new RolePrivilegeQuery().roleId(roleId).categoryName(privilegeCatCode)
                .privilegeName(privilegeCode)) > 0;
    }

    @Override
    public boolean confirmUserPrivilege(List<Long> roleIdList, String privilegeCatCode, String privilegeCode)
            throws UnifyException {
        if (!DataUtils.isBlank(roleIdList)) {
            return db().countAll(new RolePrivilegeQuery().roleIdIn(roleIdList).categoryName(privilegeCatCode)
                    .privilegeName(privilegeCode)) > 0;
        }

        return false;
    }

    @Override
    public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {
        logInfo("Managing organization...");
        logDebug("Registering role privilege categories...");
        registerPrivilegeCategory(PrivilegeCategoryConstants.APPLICATIONUI, "reserved.privilegecategory.applicationui");
        registerPrivilegeCategory(PrivilegeCategoryConstants.SHORTCUT, "reserved.privilegecategory.shortcut");
        registerPrivilegeCategory(PrivilegeCategoryConstants.DOCUMENTCONTROL,
                "reserved.privilegecategory.documentcontrol");
        registerPrivilegeCategory(PrivilegeCategoryConstants.REPORTABLE, "reserved.privilegecategory.reportable");
        registerPrivilegeCategory(PrivilegeCategoryConstants.CONFIGUREDREPORTS,
                "reserved.privilegecategory.configuredreport");

        // Uninstall old standard categories
        db().updateAll(new PrivilegeQuery()
                .categoryNameIn(Arrays.asList(PrivilegeCategoryConstants.APPLICATIONUI,
                        PrivilegeCategoryConstants.SHORTCUT, PrivilegeCategoryConstants.DOCUMENTCONTROL,
                        PrivilegeCategoryConstants.REPORTABLE, PrivilegeCategoryConstants.CONFIGUREDREPORTS))
                .installed(Boolean.TRUE), new Update().add("installed", Boolean.FALSE));

        // Install new and update old
        Map<String, PrivilegeCategory> categoryMap =
                db().listAllMap(String.class, "name", new PrivilegeCategoryQuery().status(RecordStatus.ACTIVE));

        Privilege privilege = new Privilege();
        privilege.setStatus(RecordStatus.ACTIVE);
        privilege.setInstalled(Boolean.TRUE);
        for (ModuleConfig moduleConfig : moduleConfigList) {
            if (moduleConfig.getPrivileges() != null
                    && DataUtils.isNotBlank(moduleConfig.getPrivileges().getPrivilegeGroupList())) {
                logDebug("Installing role privilege definitions for module [{0}]...",
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
                            privilege.setInstalled(Boolean.TRUE);
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

    private List<Long> getWfStepIdListForRole(RoleWfStepType type, Long roleId) throws UnifyException {
        List<Long> wfStepIdList = new ArrayList<Long>();
        Set<Long> wfTemplateIds =
                db().valueSet(Long.class, "wfTemplateId", new RoleWfStepQuery().type(type).roleId(roleId));
        for (Long wfTemplateId : wfTemplateIds) {
            Set<String> stepNames =
                    db().valueSet(String.class, "stepName",
                            new RoleWfStepQuery().type(type).roleId(roleId).wfTemplateId(wfTemplateId));
            wfStepIdList.addAll(workflowService.findStepIds(wfTemplateId, stepNames));
        }

        return wfStepIdList;
    }
}
