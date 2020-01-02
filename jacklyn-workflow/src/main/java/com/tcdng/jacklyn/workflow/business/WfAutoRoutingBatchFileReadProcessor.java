/*
 * Copyright 2018-2020 The Code Department
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

package com.tcdng.jacklyn.workflow.business;

import com.tcdng.jacklyn.workflow.constants.WfBatchFileReadProcessorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Parameters;
import com.tcdng.unify.core.batch.BatchFileReadConfig;
import com.tcdng.unify.core.batch.BatchFileReadProcessor;
import com.tcdng.unify.core.batch.BatchFileReader;
import com.tcdng.unify.core.data.Document;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Workflow automatic routing batch file read processor.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component(name = "wfautobatchfilereadprocessor", description = "$m{workflow-autobatchfilereadprocessor}")
@Parameters({ @Parameter(
        name = WfBatchFileReadProcessorConstants.WORKFLOW_CATEGORYNAME,
        description = "$m{wfbatchfilereadprocessor.workflowcategoryname}",
        editor = "!ui-select list:wfcategorylist listKey:name blankOption:$s{}", mandatory = true) })
public class WfAutoRoutingBatchFileReadProcessor extends AbstractWfBatchFileReadProcessor
        implements BatchFileReadProcessor {

    @SuppressWarnings("unchecked")
    @Override
    protected Object doProcess(BatchFileReadConfig batchFileReadConfig, BatchFileReader reader) throws UnifyException {
        String wfCategoryName =
                batchFileReadConfig.getParameter(String.class, WfBatchFileReadProcessorConstants.WORKFLOW_CATEGORYNAME);
        if (reader.detectPreferredBean()) {
            Class<? extends Document> documentType = (Class<? extends Document>) reader.getPreferredBean();
            String templateGlobalName = getWorkflowService().autoDetectTemplate(wfCategoryName, documentType);
            Document item = ReflectUtils.newInstance(documentType);
            ValueStore itemStore = getValueStore(item);
            while (reader.readNextRecord(itemStore)) {
                getWorkflowService().submitToWorkflow(templateGlobalName, item);
            }
        }

        return null;
    }

}
