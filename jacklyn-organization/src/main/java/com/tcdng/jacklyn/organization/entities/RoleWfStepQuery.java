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

import java.util.Collection;

import com.tcdng.jacklyn.common.entities.BaseEntityQuery;

/**
 * Role workflow step query.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class RoleWfStepQuery extends BaseEntityQuery<RoleWfStep> {

    public RoleWfStepQuery() {
        super(RoleWfStep.class);
    }

    public RoleWfStepQuery roleId(Long roleId) {
        return (RoleWfStepQuery) addEquals("roleId", roleId);
    }

    public RoleWfStepQuery roleName(String roleName) {
        return (RoleWfStepQuery) addEquals("roleName", roleName);
    }

    public RoleWfStepQuery wfTemplateId(Long wfTemplateId) {
        return (RoleWfStepQuery) addEquals("wfTemplateId", wfTemplateId);
    }

    public RoleWfStepQuery wfCategoryName(String wfCategoryName) {
        return (RoleWfStepQuery) addEquals("wfCategoryName", wfCategoryName);
    }

    public RoleWfStepQuery wfTemplateName(String wfTemplateName) {
        return (RoleWfStepQuery) addEquals("wfTemplateName", wfTemplateName);
    }

    public RoleWfStepQuery stepName(String stepName) {
        return (RoleWfStepQuery) addEquals("stepName", stepName);
    }

    public RoleWfStepQuery wfStepNameIn(Collection<String> stepName) {
        return (RoleWfStepQuery) addAmongst("stepName", stepName);
    }

    public RoleWfStepQuery wfStepNameNotIn(Collection<String> stepName) {
        return (RoleWfStepQuery) addNotAmongst("stepName", stepName);
    }
}
