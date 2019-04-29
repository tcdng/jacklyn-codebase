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

package com.tcdng.jacklyn.shared.system.data;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tcdng.unify.web.remotecall.RemoteCallResult;

/**
 * Get tooling transformer types request result.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@XmlRootElement
public class GetToolingTransformerTypeResult extends RemoteCallResult {

    private List<ToolingTransformerTypeItem> transformerTypeList;

    public GetToolingTransformerTypeResult(List<ToolingTransformerTypeItem> transformerTypeList) {
        this.transformerTypeList = transformerTypeList;
    }

    public GetToolingTransformerTypeResult() {

    }

    public List<ToolingTransformerTypeItem> getTransformerTypeList() {
        return transformerTypeList;
    }

    @XmlElement(name = "transformerType")
    public void setTransformerTypeList(List<ToolingTransformerTypeItem> transformerTypeList) {
        this.transformerTypeList = transformerTypeList;
    }
}
