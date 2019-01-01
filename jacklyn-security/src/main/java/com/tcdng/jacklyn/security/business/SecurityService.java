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

import java.util.List;

import com.tcdng.jacklyn.common.business.JacklynBusinessService;
import com.tcdng.jacklyn.security.data.UserLargeData;
import com.tcdng.jacklyn.security.entities.User;
import com.tcdng.jacklyn.security.entities.UserQuery;
import com.tcdng.jacklyn.security.entities.UserRole;
import com.tcdng.jacklyn.security.entities.UserRoleQuery;
import com.tcdng.jacklyn.shared.security.BiometricCategory;
import com.tcdng.jacklyn.shared.security.BiometricType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.application.StartupShutdownHook;
import com.tcdng.unify.core.operation.Update;

/**
 * Security business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface SecurityService extends JacklynBusinessService, StartupShutdownHook {

    /**
     * Creates a biometric record.
     * 
     * @param category
     *            biometric category. {@link BiometricCategory}
     * @param type
     *            biometric type. {@link BiometricType}
     * @param biometric
     *            the biometric data
     * @return the biometric record ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createBiometric(BiometricCategory category, BiometricType type, byte[] biometric) throws UnifyException;

    /**
     * Login to application with login ID and password.
     * 
     * @param loginId
     *            the login ID
     * @param password
     *            the password
     * @return the user record
     * @throws UnifyException
     *             if login ID or password is invalid
     */
    User login(String loginId, String password) throws UnifyException;

    /**
     * Logs out current session user.
     * 
     * @param complete
     *            indicates if session should be nullified.
     * @throws UnifyException
     *             if an error occurs
     */
    void logout(boolean complete) throws UnifyException;

    /**
     * Changes a user password for current session user.
     * 
     * @param oldPassword
     *            the old password
     * @param newPassword
     *            the new password
     * @throws UnifyException
     *             if old password is invalid. if password history is enabled and
     *             new password is stale
     */
    void changeUserPassword(String oldPassword, String newPassword) throws UnifyException;

    /**
     * Resets a user password.
     * 
     * @param userId
     *            the user ID
     * @throws UnifyException
     *             if an error occurs
     */
    void resetUserPassword(Long userId) throws UnifyException;

    /**
     * Creates a user.
     * 
     * @param userData
     *            the user record
     * @return the Id of the created record
     * @throws UnifyException
     *             if user creation failed
     */
    Long createUser(User userData) throws UnifyException;

    /**
     * Creates user using supplied document.
     * 
     * @param userDocument
     *            the document to use
     * @return the created user ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createUser(UserLargeData userDocument) throws UnifyException;

    /**
     * Finds users by query.
     * 
     * @param query
     *            the search query
     * @return a list of users
     * @throws UnifyException
     *             if an error occurs
     */
    List<User> findUsers(UserQuery query) throws UnifyException;

    /**
     * Finds user by Id.
     * 
     * @param id
     *            the user Id
     * @return the user record
     * @throws UnifyException
     *             if user with id not found
     */
    User findUser(Long id) throws UnifyException;

    /**
     * Finds user by login Id.
     * 
     * @param userLoginId
     *            the user Id
     * @return the user record
     * @throws UnifyException
     *             if user with id not found
     */
    User findUser(String userLoginId) throws UnifyException;

    /**
     * Updates a user record by Id and version number.
     * 
     * @param userData
     *            the user record to update
     * @return the update count
     * @throws UnifyException
     *             if user record does not exist or version number does not match
     */
    int updateUser(User userData) throws UnifyException;

    /**
     * Updates a user record by Id and version using document.
     * 
     * @param userDocument
     *            the user document to use
     * @return the update count
     * @throws UnifyException
     *             if user record does not exist or version number does not match
     */
    int updateUser(UserLargeData userDocument) throws UnifyException;

    /**
     * Updates a user photograph.
     * 
     * @param userId
     *            the user ID
     * @param photograph
     *            the image to update
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateUserPhotograph(Long userId, byte[] photograph) throws UnifyException;

    /**
     * Updates a user's roles.
     * 
     * @param roleIdList
     *            the role ID list
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateUserRoles(Long userId, List<Long> roleIdList) throws UnifyException;

    /**
     * Updates user record by query. Does not use workflow.
     * 
     * @param query
     *            the user query
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateUsers(UserQuery query, Update update) throws UnifyException;

    /**
     * Deletes user record by id.
     * 
     * @param id
     *            the user id
     * @return the delete count
     * @throws UnifyException
     *             if user with Id does not exists
     */
    int deleteUser(Long id) throws UnifyException;

    /**
     * Deletes user record by query.
     * 
     * @param query
     *            the query
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteUsers(UserQuery query) throws UnifyException;

    /**
     * Gets document for specified user.
     * 
     * @param userId
     *            the user ID
     * @return the user document
     * @throws UnifyException
     *             if an error occurs
     */
    UserLargeData findUserDocument(Long userId) throws UnifyException;

    /**
     * Gets photograph for specified user.
     * 
     * @param userId
     *            the user ID
     * @return the user photograph if found otherwise null
     * @throws UnifyException
     *             if an error occurs
     */
    byte[] findUserPhotograph(Long userId) throws UnifyException;

    /**
     * Gets role ID list for specified user.
     * 
     * @param userId
     *            the user ID
     * @return the role ID list
     * @throws UnifyException
     *             if an error occurs
     */
    List<Long> findUserRoleIds(Long userId) throws UnifyException;

    /**
     * Finds user roles by query.
     * 
     * @param query
     *            the query
     * @return the list of user role data
     * @throws UnifyException
     *             if an error occurs
     */
    List<UserRole> findUserRoles(UserRoleQuery query) throws UnifyException;

    /**
     * Returns the user token for current user session.
     * 
     * @throws UnifyException
     *             if an error occurs
     */
    UserToken getCurrentUserToken() throws UnifyException;

    /**
     * Sets the role of the current user using user role information.
     * 
     * @param userRoleId
     *            the id of the user role information
     * @throws UnifyException
     *             if an error occurs
     */
    void setCurrentUserRole(Long userRoleId) throws UnifyException;
}