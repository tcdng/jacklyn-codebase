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
package com.tcdng.jacklyn.common;

import java.util.List;

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessModule;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;

/**
 * Test customer business module implementation.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component("test-customerservice")
public class TestCustomerModuleImpl extends AbstractJacklynBusinessModule implements TestCustomerModule {

    @Override
    public Long createCustomer(TestCustomer customer) throws UnifyException {
        return (Long) db().create(customer);
    }

    @Override
    public TestCustomer findCustomer(String firstName) throws UnifyException {
        return db().find(new TestCustomerQuery().firstName(firstName));
    }

    @Override
    public TestCustomer findCustomer(Long customerId) throws UnifyException {
        return db().find(TestCustomer.class, customerId);
    }

    @Override
    public int updateCustomer(TestCustomer customer) throws UnifyException {
        return db().updateByIdVersion(customer);
    }

    @Override
    public void installFeatures(List<ModuleConfig> featureDefinitions) throws UnifyException {

    }
}
