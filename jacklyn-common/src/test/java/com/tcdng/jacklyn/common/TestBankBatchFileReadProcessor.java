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

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Parameters;
import com.tcdng.unify.core.batch.AbstractDBBatchItemFileReadProcessor;
import com.tcdng.unify.core.business.BusinessLogicInput;

/**
 * Test bank batch file read processor.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("test-bankbatchfilereadprocessor")
@Parameters({ @Parameter(description = "Country", name = "country", editor = "!ui-text", mandatory = true) })
public class TestBankBatchFileReadProcessor extends AbstractDBBatchItemFileReadProcessor<TestBank> {

    public TestBankBatchFileReadProcessor() {
        super(TestBank.class);
    }

    @Override
    protected void preBatchItemCreate(BusinessLogicInput input, TestBank batchItem) throws UnifyException {
        // Set bank country as input parameter
        String country = input.getParameter(String.class, "country");
        ((TestBank) batchItem).setCountry(country);
    }
}
