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
package com.tcdng.jacklyn.organization.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.organization.constants.OrganizationModuleNameConstants;
import com.tcdng.jacklyn.workflow.entities.WfTemplate;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Role workflow step entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = OrganizationModuleNameConstants.ORGANIZATION_MODULE, title = "Role Workflow Step")
@Table("JKROLEWFSTEP")
public class RoleWfStep extends BaseEntity {

    @ForeignKey(Role.class)
    private Long roleId;

    @ForeignKey(WfTemplate.class)
    private Long wfTemplateId;

    @Column(name = "STEP_NM", length = 32)
    private String stepName;

    @ListOnly(key = "roleId", property = "name")
    private String roleName;

    @ListOnly(key = "roleId", property = "description")
    private String roleDesc;

    @ListOnly(key = "wfTemplateId", property = "name")
    private String wfTemplateName;

    @ListOnly(key = "wfTemplateId", property = "description")
    private String wfTemplateDesc;

    @ListOnly(key = "wfTemplateId", property = "wfCategoryId")
    private Long wfCategoryId;

    @ListOnly(key = "wfTemplateId", property = "wfCategoryName")
    private String wfCategoryName;

    @ListOnly(key = "wfTemplateId", property = "wfCategoryDesc")
    private String wfCategoryDesc;

    @Override
    public String getDescription() {
        return stepName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getWfTemplateId() {
        return wfTemplateId;
    }

    public void setWfTemplateId(Long wfTemplateId) {
        this.wfTemplateId = wfTemplateId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getWfTemplateName() {
        return wfTemplateName;
    }

    public void setWfTemplateName(String wfTemplateName) {
        this.wfTemplateName = wfTemplateName;
    }

    public String getWfTemplateDesc() {
        return wfTemplateDesc;
    }

    public void setWfTemplateDesc(String wfTemplateDesc) {
        this.wfTemplateDesc = wfTemplateDesc;
    }

    public Long getWfCategoryId() {
        return wfCategoryId;
    }

    public void setWfCategoryId(Long wfCategoryId) {
        this.wfCategoryId = wfCategoryId;
    }

    public String getWfCategoryName() {
        return wfCategoryName;
    }

    public void setWfCategoryName(String wfCategoryName) {
        this.wfCategoryName = wfCategoryName;
    }

    public String getWfCategoryDesc() {
        return wfCategoryDesc;
    }

    public void setWfCategoryDesc(String wfCategoryDesc) {
        this.wfCategoryDesc = wfCategoryDesc;
    }

}
