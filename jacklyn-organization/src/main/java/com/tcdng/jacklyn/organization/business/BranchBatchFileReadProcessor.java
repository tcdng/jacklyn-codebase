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

import com.tcdng.jacklyn.organization.entities.Branch;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.batch.AbstractDBBatchItemFileReadProcessor;

/**
 * Branch batch file read processor.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component(name = "branchbatchfilereadprocessor", description = "$m{branch-batchfilereadprocessor}")
public class BranchBatchFileReadProcessor extends AbstractDBBatchItemFileReadProcessor<Branch> {

    public BranchBatchFileReadProcessor() {
        super(Branch.class);
    }

}
