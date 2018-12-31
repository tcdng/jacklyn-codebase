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
package com.tcdng.jacklyn.organization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.tcdng.jacklyn.common.AbstractJacklynTest;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.organization.business.OrganizationService;
import com.tcdng.jacklyn.organization.constants.OrganizationModuleNameConstants;
import com.tcdng.jacklyn.organization.data.RoleLargeData;
import com.tcdng.jacklyn.organization.entities.Privilege;
import com.tcdng.jacklyn.organization.entities.PrivilegeQuery;
import com.tcdng.jacklyn.organization.entities.Role;
import com.tcdng.jacklyn.organization.entities.RolePrivilege;
import com.tcdng.jacklyn.organization.entities.RolePrivilegeQuery;
import com.tcdng.jacklyn.organization.entities.RolePrivilegeWidget;
import com.tcdng.jacklyn.organization.entities.RoleQuery;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;

/**
 * Organization business service tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class OrganizationServiceTest extends AbstractJacklynTest {

    @Test
    public void testCreateRole() throws Exception {
        OrganizationService organizationService = (OrganizationService) getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE);
        Role role = getRole("org-001", "Supervisor");
        Long roleId = organizationService.createRole(role);
        assertNotNull(roleId);
    }

    @Test
    public void testFindRole() throws Exception {
        OrganizationService organizationService = (OrganizationService) getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE);
        Role role = getRole("org-001", "Supervisor");
        Long roleId = organizationService.createRole(role);

        Role fetchedRole = organizationService.findRole(roleId);
        assertNotNull(fetchedRole);
        assertEquals(role.getName(), fetchedRole.getName());
        assertEquals(role.getDescription(), fetchedRole.getDescription());
    }

    @Test
    public void testFindRoles() throws Exception {
        OrganizationService organizationService = (OrganizationService) getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE);
        Role role = getRole("org-001", "Supervisor");
        organizationService.createRole(role);

        RoleQuery query = new RoleQuery();
        query.ignoreEmptyCriteria(true);
        query.orderById();
        List<Role> roleList = organizationService.findRoles(query);
        assertNotNull(roleList);
        assertEquals(1, roleList.size());
        assertEquals(role.getName(), roleList.get(0).getName());
        assertEquals(role.getDescription(), roleList.get(0).getDescription());

        role = getRole("org-002", "Adminstrator");
        organizationService.createRole(role);
        roleList = organizationService.findRoles(query);
        assertNotNull(roleList);
        assertEquals(2, roleList.size());
        assertEquals(role.getName(), roleList.get(1).getName());
        assertEquals(role.getDescription(), roleList.get(1).getDescription());
    }

    @Test
    public void testUpdateRole() throws Exception {
        OrganizationService organizationService = (OrganizationService) getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE);
        Role role = getRole("org-001", "Supervisor");
        Long roleId = organizationService.createRole(role);

        Role fetchedRole = organizationService.findRole(roleId);
        fetchedRole.setDescription("Supervisor (Advanced)");
        int count = organizationService.updateRole(fetchedRole);
        assertEquals(1, count);

        Role updatedRole = organizationService.findRole(roleId);
        assertEquals(fetchedRole, updatedRole);
        assertFalse(role.equals(updatedRole));
    }

    @Test
    public void testDeleteRole() throws Exception {
        OrganizationService organizationService = (OrganizationService) getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE);
        Role role = getRole("org-001", "Supervisor");
        Long roleId = organizationService.createRole(role);

        int count = organizationService.deleteRole(roleId);
        assertEquals(1, count);
    }

    @Test
    public void testUpdateRolePrivileges() throws Exception {
        OrganizationService organizationService = (OrganizationService) getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE);
        SystemService systemService = (SystemService) this.getComponent(SystemModuleNameConstants.SYSTEMSERVICE);
        Long moduleId = systemService.findModule("customer").getId();
        List<Long> privilegeIdList = organizationService
                .findPrivilegeIds((PrivilegeQuery) new PrivilegeQuery().moduleId(moduleId).orderById());
        RoleLargeData roleDoc = new RoleLargeData(getRole("org-001", "Supervisor"));
        roleDoc.setPrivilegeIdList(privilegeIdList);
        Long roleId = organizationService.createRole(roleDoc);
        organizationService.updateRolePrivileges(roleId, privilegeIdList);
    }

    @Test
    public void testFindRolePrivileges() throws Exception {
        OrganizationService organizationService = (OrganizationService) this
                .getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE);
        SystemService systemService = (SystemService) this.getComponent(SystemModuleNameConstants.SYSTEMSERVICE);
        Long moduleId = systemService.findModule(SystemModuleNameConstants.SYSTEM_MODULE).getId();
        List<Long> privilegeIdList = organizationService
                .findPrivilegeIds((PrivilegeQuery) new PrivilegeQuery().moduleId(moduleId).orderById());
        RoleLargeData roleDoc = new RoleLargeData(getRole("org-001", "Supervisor"));
        roleDoc.setPrivilegeIdList(privilegeIdList);
        organizationService.createRole(roleDoc);

        List<Long> fetchedModuleActivityIdList = organizationService
                .findPrivilegeIds(new RolePrivilegeQuery().moduleId(moduleId).roleName("org-001"));

        Collections.sort(privilegeIdList);
        Collections.sort(fetchedModuleActivityIdList);
        assertTrue(privilegeIdList.equals(fetchedModuleActivityIdList));
    }

    @Test
    public void testFindModulePrivileges() throws Exception {
        OrganizationService organizationService = (OrganizationService) getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE);
        PrivilegeQuery query = new PrivilegeQuery();
        query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
        query.orderById();
        List<Privilege> privilegeList = organizationService.findPrivileges(query);
        assertNotNull(privilegeList);
        assertFalse(privilegeList.isEmpty());
    }

    @Test
    public void testFindModulePrivilegeIds() throws Exception {
        OrganizationService organizationService = (OrganizationService) getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE);
        PrivilegeQuery query = new PrivilegeQuery();
        query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
        query.orderById();
        List<Privilege> privilegeList = organizationService.findPrivileges(query);
        List<Long> privilegeIdList = organizationService.findPrivilegeIds(query);
        assertNotNull(privilegeIdList);
        assertFalse(privilegeIdList.isEmpty());
        assertEquals(privilegeList.size(), privilegeIdList.size());
        assertEquals(privilegeList.get(0).getId(), privilegeIdList.get(0));
        assertEquals(privilegeList.get(1).getId(), privilegeIdList.get(1));
    }

    @Test
    public void testSetPrivilegeStatus() throws Exception {
        OrganizationService organizationService = (OrganizationService) getComponent(OrganizationModuleNameConstants.ORGANIZATIONSERVICE);
        PrivilegeQuery query = new PrivilegeQuery();
        query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
        query.orderById();
        List<Long> privilegeIdList = organizationService.findPrivilegeIds(query);
        List<Long> sampleIds = new ArrayList<Long>();
        sampleIds.add(privilegeIdList.get(1));
        sampleIds.add(privilegeIdList.get(2));
        int count = organizationService.setPrivilegeStatuses(sampleIds, RecordStatus.INACTIVE);
        assertEquals(2, count);
    }

    @Override
    protected void onSetup() throws Exception {

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onTearDown() throws Exception {
        deleteAll(RolePrivilegeWidget.class, RolePrivilege.class, Role.class);
    }

    private Role getRole(String name, String description) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        role.setStatus(RecordStatus.ACTIVE);
        return role;
    }
}
