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

package com.tcdng.jacklyn.workflow;

import com.tcdng.jacklyn.workflow.business.AbstractWfItemEnrichmentLogic;
import com.tcdng.jacklyn.workflow.business.WfItemReaderWriter;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Test customer enrichment logic.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("testcustomer-enrichmentlogic")
public class TestCustomerEnrichmentLogic extends AbstractWfItemEnrichmentLogic {

    @Override
    public void enrich(WfItemReaderWriter wfItemReaderWriter) throws UnifyException {
        // Enrich workflow item
        String firstName = wfItemReaderWriter.readFieldValue(String.class, "firstName");
        String lastName = wfItemReaderWriter.readFieldValue(String.class, "lastName");
        wfItemReaderWriter.writeFieldValue("fullName", firstName + " " + lastName);
        wfItemReaderWriter.writeFieldValue("accountNo", "0123456789");
    }

}
