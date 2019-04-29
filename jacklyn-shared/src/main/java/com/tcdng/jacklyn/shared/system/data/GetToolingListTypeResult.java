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
 * Get tooling list types request result.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@XmlRootElement
public class GetToolingListTypeResult extends RemoteCallResult {

    private List<ToolingListTypeItem> listTypeList;

    public GetToolingListTypeResult(List<ToolingListTypeItem> listTypeList) {
        this.listTypeList = listTypeList;
    }

    public GetToolingListTypeResult() {

    }

    public List<ToolingListTypeItem> getListTypeList() {
        return listTypeList;
    }

    @XmlElement(name = "listType")
    public void setListTypeList(List<ToolingListTypeItem> listTypeList) {
        this.listTypeList = listTypeList;
    }
}
