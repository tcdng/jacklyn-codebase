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
package com.tcdng.jacklyn.common;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.business.BusinessService;

/**
 * Test customer business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface TestCustomerService extends BusinessService {

    /**
     * Creates a customer record in persistence.
     * 
     * @param customer
     *            the record to create
     * @return the customer ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createCustomer(TestCustomer customer) throws UnifyException;

    /**
     * Finds a customer by supplied ID.
     * 
     * @param customerId
     *            the customer ID
     * @return found customer
     * @throws UnifyException
     *             if customer with ID is not found. If an error occurs
     */
    TestCustomer findCustomer(Long customerId) throws UnifyException;

    /**
     * Finds a customer by first name.
     * 
     * @param firstName
     *            the first name
     * @return customer if found otherwise null
     * @throws UnifyException
     *             If an error occurs
     */
    TestCustomer findCustomer(String firstName) throws UnifyException;

    /**
     * Updates a customer record in persistence.
     * 
     * @param customer
     *            the record to update
     * @return 1
     * @throws UnifyException
     *             if an error occurs
     */
    int updateCustomer(TestCustomer customer) throws UnifyException;
}
