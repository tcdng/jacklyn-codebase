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

package com.tcdng.jacklyn.system.web.beans;

import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;
import com.tcdng.jacklyn.shared.system.SystemParamType;
import com.tcdng.jacklyn.system.entities.SystemParameter;

/**
 * System parameter entity page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class SystemParameterPageBean extends BaseEntityPageBean<SystemParameter> {

    private Long searchModuleId;

    private SystemParamType searchType;

    private String searchName;

    private String searchDescription;

    public SystemParameterPageBean() {
        super("$m{system.sysparameter.hint}");
    }

    public Long getSearchModuleId() {
        return searchModuleId;
    }

    public void setSearchModuleId(Long searchModuleId) {
        this.searchModuleId = searchModuleId;
    }

    public SystemParamType getSearchType() {
        return searchType;
    }

    public void setSearchType(SystemParamType searchType) {
        this.searchType = searchType;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

}
