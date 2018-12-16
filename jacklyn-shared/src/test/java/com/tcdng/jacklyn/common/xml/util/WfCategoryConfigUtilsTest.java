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

package com.tcdng.jacklyn.common.xml.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.tcdng.jacklyn.shared.xml.config.workflow.WfCategoryConfig;
import com.tcdng.jacklyn.shared.xml.util.WfCategoryConfigUtils;
import com.tcdng.unify.core.UnifyError;

/**
 * Workflow category configuration tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfCategoryConfigUtilsTest {

	@Test
	public void testReadWfCategoryConfig() throws Exception {
		WfCategoryConfig wfCategoryConfig = WfCategoryConfigUtils.readWfCategoryConfig("xml/wfcustomer.xml");
		assertNotNull(wfCategoryConfig);
	}

	@Test
	public void testValidateWfCategoryConfig() throws Exception {
		WfCategoryConfig wfCategoryConfig = WfCategoryConfigUtils.readWfCategoryConfig("xml/wfcustomer.xml");
		List<UnifyError> errorList = WfCategoryConfigUtils.validate(wfCategoryConfig);
		assertNotNull(errorList);
		assertEquals(0, errorList.size());
	}
}
