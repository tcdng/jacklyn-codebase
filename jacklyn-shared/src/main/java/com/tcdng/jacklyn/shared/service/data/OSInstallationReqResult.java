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
package com.tcdng.jacklyn.shared.service.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tcdng.unify.web.RemoteCallResult;

/**
 * OS installation request result.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@XmlRootElement
public class OSInstallationReqResult extends RemoteCallResult {

    private String appName;

    private String appDesc;

    private byte[] appIcon;

    private boolean alreadyInstalled;

    public String getAppName() {
        return appName;
    }

    @XmlElement(required = true)
    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDesc() {
        return appDesc;
    }

    @XmlElement(required = true)
    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public byte[] getAppIcon() {
        return appIcon;
    }

    @XmlElement(required = true)
    public void setAppIcon(byte[] appIcon) {
        this.appIcon = appIcon;
    }

    public boolean isAlreadyInstalled() {
        return alreadyInstalled;
    }

    @XmlElement(required = true)
    public void setAlreadyInstalled(boolean alreadyInstalled) {
        this.alreadyInstalled = alreadyInstalled;
    }

}
