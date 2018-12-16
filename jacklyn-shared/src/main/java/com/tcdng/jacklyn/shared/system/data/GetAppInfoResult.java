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
package com.tcdng.jacklyn.shared.system.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tcdng.unify.web.RemoteCallResult;

/**
 * Get application information request result.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@XmlRootElement
public class GetAppInfoResult extends RemoteCallResult {

    private String appName;

    private String appVersion;

    private String clientTitle;

    public GetAppInfoResult(String appName, String appVersion, String clientTitle) {
        this.appName = appName;
        this.appVersion = appVersion;
        this.clientTitle = clientTitle;
    }

    public GetAppInfoResult() {

    }

    public String getAppName() {
        return appName;
    }

    @XmlElement
    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    @XmlElement
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getClientTitle() {
        return clientTitle;
    }

    @XmlElement
    public void setClientTitle(String clientTitle) {
        this.clientTitle = clientTitle;
    }
}
