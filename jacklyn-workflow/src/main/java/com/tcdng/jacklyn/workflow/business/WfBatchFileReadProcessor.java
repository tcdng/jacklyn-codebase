/*
 * Copyright 2018-2019 The Code Department
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

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Parameters;
import com.tcdng.unify.core.batch.AbstractBatchFileReadProcessor;
import com.tcdng.unify.core.batch.BatchFileReadConfig;
import com.tcdng.unify.core.batch.BatchFileReadProcessor;
import com.tcdng.unify.core.batch.BatchFileReader;
import com.tcdng.unify.core.data.Document;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Workflow batch file read processor.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component(name = "wfbatchfilereadprocessor", description = "$m{workflow-batchfilereadprocessor}")
@Parameters({
        @Parameter(
                name = WfBatchFileReadProcessorConstants.WORKFLOW_TEMPLATENAME,
                description = "$m{wfbatchfilereadprocessor.workflowtemplatename}",
                editor = "!ui-name minLen:1 size:24 period:true", mandatory = true),
        @Parameter(
                name = WfBatchFileReadProcessorConstants.WORKFLOW_DOCUMENTTYPE,
                description = "$m{wfbatchfilereadprocessor.documenttype}",
                editor = "!ui-select list:toolingdocumentlist blankOption:$s{}", mandatory = true) })
public class WfBatchFileReadProcessor extends AbstractBatchFileReadProcessor implements BatchFileReadProcessor {

    @Configurable
    private WorkflowService workflowService;

    @Override
    protected Object doProcess(BatchFileReadConfig batchFileReadConfig, BatchFileReader reader) throws UnifyException {
        String globalTemplateName =
                batchFileReadConfig.getParameter(String.class, WfBatchFileReadProcessorConstants.WORKFLOW_TEMPLATENAME);
        String documentType =
                batchFileReadConfig.getParameter(String.class, WfBatchFileReadProcessorConstants.WORKFLOW_DOCUMENTTYPE);
        Document item = ReflectUtils.newInstance(Document.class, documentType);
        ValueStore itemStore = getValueStore(item);
        while (reader.readNextRecord(itemStore)) {
            workflowService.submitToWorkflow(globalTemplateName, (Document) item);
        }

        return null;
    }

}
