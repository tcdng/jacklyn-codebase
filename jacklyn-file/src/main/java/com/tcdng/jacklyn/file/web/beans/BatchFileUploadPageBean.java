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

package com.tcdng.jacklyn.file.web.beans;

import com.tcdng.jacklyn.common.web.beans.BasePageBean;
import com.tcdng.jacklyn.file.data.BatchFileReadInputParameters;
import com.tcdng.unify.core.logging.EventType;

/**
 * Batch file upload page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class BatchFileUploadPageBean extends BasePageBean {

    private Long batchUploadConfigId;

    private BatchFileReadInputParameters batchUploadParameters;

    public BatchFileUploadPageBean() {
        super("manageBatchUploadPanel");
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

    public String getModeStyle() {
        return EventType.CREATE.colorMode();
    }

}
