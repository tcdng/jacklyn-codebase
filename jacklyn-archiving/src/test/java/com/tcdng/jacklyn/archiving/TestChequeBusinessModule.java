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
package com.tcdng.jacklyn.archiving;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.business.BusinessModule;

/**
 * Test cheque business module.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface TestChequeBusinessModule extends BusinessModule {

	/**
	 * Creates a cheque image record.
	 * 
	 * @param record
	 *            the record to create
	 * @return the record ID
	 * @throws UnifyException
	 *             if an error occurs
	 */
	Long createChequeImage(TestChequeImageData record) throws UnifyException;

	/**
	 * Finds cheque image by ID.
	 * 
	 * @param id
	 *            the ID to search with
	 * @return cheque image record
	 * @throws UnifyException
	 *             if recotd with ID is not found
	 */
	TestChequeImageData findChequeImage(Long id) throws UnifyException;
}
