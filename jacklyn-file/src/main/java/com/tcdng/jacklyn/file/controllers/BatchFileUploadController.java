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
package com.tcdng.jacklyn.file.controllers;

import com.tcdng.jacklyn.file.constants.BatchFileReadTaskConstants;
import com.tcdng.jacklyn.file.data.BatchFileReadInputParameters;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.logging.EventType;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;

/**
 * Controller for batch upload page used for initiating and monitoring a batch
 * upload.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/file/batchupload")
@UplBinding("web/file/upl/batchupload.upl")
@ResultMappings({ @ResultMapping(name = "refreshinputparameters",
        response = { "!refreshpanelresponse panels:$l{batchParamPanel}" }) })
public class BatchFileUploadController extends AbstractFileController {

    private Long batchUploadConfigId;

    private BatchFileReadInputParameters batchUploadParameters;

    public BatchFileUploadController() {
        super(true, false);
    }

    @Action
    public String prepareBatchUpload() throws UnifyException {
        batchUploadParameters = null;
        if (QueryUtils.isValidLongCriteria(batchUploadConfigId)) {
            batchUploadParameters = getFileService().getBatchFileReadInputParameters(batchUploadConfigId);
        }
        return "refreshinputparameters";
    }

    @Action
    public String startBatchUploadTask() throws UnifyException {
        TaskSetup taskSetup = TaskSetup.newBuilder().addTask(BatchFileReadTaskConstants.BATCHFILEREADTASK)
                .setParam(BatchFileReadTaskConstants.BATCHFILEREADINPUTPARAMS, batchUploadParameters).logMessages()
                .build();
        return launchTaskWithMonitorBox(taskSetup, "$m{file.batchupload.execution}");
    }

    public String getModeStyle() {
        return EventType.CREATE.colorMode();
    }

    public Long getBatchUploadConfigId() {
        return batchUploadConfigId;
    }

    public void setBatchUploadConfigId(Long batchUploadConfigId) {
        this.batchUploadConfigId = batchUploadConfigId;
    }

    public BatchFileReadInputParameters getBatchUploadParameters() {
        return batchUploadParameters;
    }

    public void setBatchUploadParameters(BatchFileReadInputParameters batchUploadParameters) {
        this.batchUploadParameters = batchUploadParameters;
    }
}
