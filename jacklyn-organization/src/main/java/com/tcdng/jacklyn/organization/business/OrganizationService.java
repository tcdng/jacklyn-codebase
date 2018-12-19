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

import com.tcdng.jacklyn.common.business.JacklynBusinessService;
import com.tcdng.jacklyn.organization.entities.Branch;
import com.tcdng.jacklyn.organization.entities.BranchQuery;
import com.tcdng.unify.core.UnifyException;

/**
 * Organization business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface OrganizationService extends JacklynBusinessService {

    /**
     * Creates a new branch.
     * 
     * @param branch
     *            the branch data
     * @return the created branch ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createBranch(Branch branch) throws UnifyException;

    /**
     * Finds branch by ID.
     * 
     * @param branchId
     *            the branch ID
     * @return the branch data
     * @throws UnifyException
     *             if branch with ID is not found
     */
    Branch findBranch(Long branchId) throws UnifyException;

    /**
     * Finds branchs by query.
     * 
     * @param query
     *            the branch query
     * @return the list of branchs found
     * @throws UnifyException
     *             if an error occurs
     */
    List<Branch> findBranches(BranchQuery query) throws UnifyException;

    /**
     * Updates a branch.
     * 
     * @param branch
     *            the branch data
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateBranch(Branch branch) throws UnifyException;

    /**
     * Deletes a branch.
     * 
     * @param id
     *            the branch ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteBranch(Long id) throws UnifyException;
}
