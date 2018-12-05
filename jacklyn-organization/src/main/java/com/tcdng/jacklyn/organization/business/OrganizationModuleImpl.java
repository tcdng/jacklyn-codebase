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
package com.tcdng.jacklyn.organization.business;

import java.util.List;

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessModule;
import com.tcdng.jacklyn.organization.constants.OrganizationModuleNameConstants;
import com.tcdng.jacklyn.organization.entities.Branch;
import com.tcdng.jacklyn.organization.entities.BranchQuery;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;

/**
 * Default implementation of organization business module.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(OrganizationModuleNameConstants.ORGANIZATIONBUSINESSMODULE)
public class OrganizationModuleImpl extends AbstractJacklynBusinessModule
		implements OrganizationModule {

	@Override
	public Long createBranch(Branch branch) throws UnifyException {
		return (Long) db().create(branch);
	}

	@Override
	public Branch findBranch(Long branchId) throws UnifyException {
		return db().find(Branch.class, branchId);
	}

	@Override
	public List<Branch> findBranches(BranchQuery query) throws UnifyException {
		return db().listAll(query);
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
	public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {

	}
}
