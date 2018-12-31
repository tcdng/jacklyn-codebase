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
package com.tcdng.jacklyn.shared.xml.config.module;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.tcdng.jacklyn.shared.organization.PrivilegeCategoryConstants;

/**
 * Privilege group configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class PrivilegeGroupConfig {

    private String category;

    private List<PrivilegeConfig> privilegeList;

    public PrivilegeGroupConfig() {
        this(PrivilegeCategoryConstants.APPLICATIONUI);
    }

    public PrivilegeGroupConfig(String category) {
        this.category = category;
        this.privilegeList = new ArrayList<PrivilegeConfig>();
    }

    public List<PrivilegeConfig> getPrivilegeList() {
        return privilegeList;
    }

    @XmlElement(name = "privilege", required = true)
    public void setPrivilegeList(List<PrivilegeConfig> privilegeList) {
        this.privilegeList = privilegeList;
    }

    public String getCategory() {
        return category;
    }

    @XmlAttribute
    public void setCategory(String category) {
        this.category = category;
    }

    public void addPrivilegeConfig(PrivilegeConfig pc) {
        this.privilegeList.add(pc);
    }
}
