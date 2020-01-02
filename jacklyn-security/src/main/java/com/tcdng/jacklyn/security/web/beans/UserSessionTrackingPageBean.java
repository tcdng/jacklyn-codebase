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

package com.tcdng.jacklyn.security.web.beans;

import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;
import com.tcdng.unify.core.system.entities.UserSessionTracking;

/**
 * User session tracking page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class UserSessionTrackingPageBean extends BaseEntityPageBean<UserSessionTracking> {

    private String searchLoginId;

    private String searchNodeId;

    public UserSessionTrackingPageBean() {
        super("$m{security.usersession.hint}");
    }

    public String getSearchLoginId() {
        return searchLoginId;
    }

    public void setSearchLoginId(String searchLoginId) {
        this.searchLoginId = searchLoginId;
    }

    public String getSearchNodeId() {
        return searchNodeId;
    }

    public void setSearchNodeId(String searchNodeId) {
        this.searchNodeId = searchNodeId;
    }

}
