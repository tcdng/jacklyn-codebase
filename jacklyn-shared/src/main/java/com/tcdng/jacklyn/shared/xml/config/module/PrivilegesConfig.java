/*
 * Copyright 2018-2020 The Code Department.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

/**
 * Privilege configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class PrivilegesConfig {

    private List<PrivilegeGroupConfig> privilegeGroupList;

    private Map<String, PrivilegeGroupConfig> privilegeGroups;

    public List<PrivilegeGroupConfig> getPrivilegeGroupList() {
        return this.privilegeGroupList;
    }

    @XmlElement(name = "privilege-group", required = true)
    public void setPrivilegeGroupList(List<PrivilegeGroupConfig> privilegeGroupList) {
        this.privilegeGroupList = privilegeGroupList;
    }

    public void toMap() {
        this.privilegeGroups = new HashMap<String, PrivilegeGroupConfig>();
        if (this.privilegeGroupList != null) {
            for (PrivilegeGroupConfig pgc : this.privilegeGroupList) {
                this.privilegeGroups.put(pgc.getCategory(), pgc);
            }
        } else {
            this.privilegeGroupList = new ArrayList<PrivilegeGroupConfig>();
        }
    }

    public void addPrivilegeGroup(PrivilegeGroupConfig privilegeGroupConfig) {
        this.privilegeGroupList.add(privilegeGroupConfig);
        this.privilegeGroups.put(privilegeGroupConfig.getCategory(), privilegeGroupConfig);
    }

    public PrivilegeGroupConfig getPrivilegeGroupConfig(String category) {
        return this.privilegeGroups.get(category);
    }
}
