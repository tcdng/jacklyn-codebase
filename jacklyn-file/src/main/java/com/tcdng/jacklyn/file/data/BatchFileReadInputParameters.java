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
package com.tcdng.jacklyn.file.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.Input;

/**
 * Batch file read input parameters.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class BatchFileReadInputParameters {

    private String name;

    private String description;

    private List<Input> inputParameterList;

    private byte[] fileBlob;

    private File file;

    public BatchFileReadInputParameters() {
        this.inputParameterList = new ArrayList<Input>();
    }

    public <T> void setParameter(String name, Object value) throws UnifyException {
        if (inputParameterList != null) {
            for (Input parameter : inputParameterList) {
                if (name.equals(parameter.getName())) {
                    parameter.setTypeValue(value);
                    break;
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Input> getInputParameterList() {
        return inputParameterList;
    }

    public void addInputParameterList(List<Input> inputParameterList) {
        this.inputParameterList.addAll(inputParameterList);
    }

    public byte[] getFileBlob() {
        return fileBlob;
    }

    public void setFileBlob(byte[] fileBlob) {
        this.fileBlob = fileBlob;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
