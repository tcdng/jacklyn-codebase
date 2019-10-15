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

package com.tcdng.jacklyn.workflow;

import com.tcdng.jacklyn.workflow.business.AbstractWfItemProcessPolicy;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Test open account process policy.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("testopenaccount-processpolicy")
public class TestOpenAccountProcessPolicy extends AbstractWfItemProcessPolicy {

    private OpenAccountDetails openAccountDetails;

    @Override
    public void execute(FlowingWfItem.Reader flowingWfItemReader) throws UnifyException {
        openAccountDetails = new OpenAccountDetails(flowingWfItemReader.readField(String.class, "fullName"),
                flowingWfItemReader.readField(String.class, "accountNo"));
    }

    public OpenAccountDetails getOpenAccountDetails() {
        return openAccountDetails;
    }

    public void clear() {
        openAccountDetails = null;
    }

    public static class OpenAccountDetails {
        private String fullName;
        private String accountNo;

        public OpenAccountDetails(String fullName, String accountNo) {
            this.fullName = fullName;
            this.accountNo = accountNo;
        }

        public String getFullName() {
            return fullName;
        }

        public String getAccountNo() {
            return accountNo;
        }
    }
}
