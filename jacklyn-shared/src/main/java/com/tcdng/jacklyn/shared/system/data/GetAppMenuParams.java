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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tcdng.jacklyn.shared.system.SystemRemoteCallNameConstants;
import com.tcdng.unify.web.RemoteCallParams;

/**
 * Get application menu request parameters.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@XmlRootElement
public class GetAppMenuParams extends RemoteCallParams {

    private String moduleName;

    private String menuName;

    public GetAppMenuParams(String moduleName, String menuName) {
        super(SystemRemoteCallNameConstants.GET_APPLICATION_MENU);
        this.moduleName = moduleName;
        this.menuName = menuName;
    }

    public GetAppMenuParams() {
        super(SystemRemoteCallNameConstants.GET_APPLICATION_MENU);
    }

    public String getModuleName() {
        return moduleName;
    }

    @XmlElement
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMenuName() {
        return menuName;
    }

    @XmlElement
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
