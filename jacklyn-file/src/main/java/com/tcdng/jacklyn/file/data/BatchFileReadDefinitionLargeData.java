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
package com.tcdng.jacklyn.file.data;

import com.tcdng.jacklyn.common.entities.BaseLargeData;
import com.tcdng.jacklyn.file.entities.BatchFileReadDefinition;
import com.tcdng.unify.core.data.Inputs;

/**
 * Batch file read definition large data.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class BatchFileReadDefinitionLargeData extends BaseLargeData<BatchFileReadDefinition> {

    private Inputs fileReaderParams;

    private Inputs fileProcessorParams;

    public BatchFileReadDefinitionLargeData(BatchFileReadDefinition batchFileReadDefinition, Inputs fileReaderParams,
            Inputs fileProcessorParams) {
        super(batchFileReadDefinition);
        this.fileReaderParams = fileReaderParams;
        this.fileProcessorParams = fileProcessorParams;
    }

    public BatchFileReadDefinitionLargeData(BatchFileReadDefinition batchFileReadDefinition) {
        super(batchFileReadDefinition);
    }

    public BatchFileReadDefinitionLargeData() {
        super(new BatchFileReadDefinition());
    }

    public Inputs getFileReaderParams() {
        return fileReaderParams;
    }

    public void setFileReaderParams(Inputs fileReaderParams) {
        this.fileReaderParams = fileReaderParams;
    }

    public Inputs getFileProcessorParams() {
        return fileProcessorParams;
    }

    public void setFileProcessorParams(Inputs fileProcessorParams) {
        this.fileProcessorParams = fileProcessorParams;
    }

}
