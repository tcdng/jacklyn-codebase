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

import java.util.List;

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessModule;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;

/**
 * Test cheque module implementation.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component("test-chequeservice")
public class TestChequeBusinessModuleImpl extends AbstractJacklynBusinessModule
		implements TestChequeBusinessModule {

	@Override
	public Long createChequeImage(TestChequeImageData record) throws UnifyException {
		return (Long) db().create(record);
	}

	@Override
	public TestChequeImageData findChequeImage(Long id) throws UnifyException {
		return db().find(TestChequeImageData.class, id);
	}

	@Override
	public void installFeatures(List<ModuleConfig> featureDefinitions) throws UnifyException {

	}

}
