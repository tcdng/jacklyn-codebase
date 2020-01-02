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
package com.tcdng.jacklyn.organization.data;

import java.util.List;

import com.tcdng.jacklyn.common.entities.BaseLargeData;
import com.tcdng.jacklyn.organization.entities.Role;

/**
 * Role large data.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class RoleLargeData extends BaseLargeData<Role> {

    private List<Long> privilegeIdList;

    private List<Long> wfStepIdList;

    public RoleLargeData() {
        super(new Role());
    }

    public RoleLargeData(Role roleData) {
        super(roleData);
    }

    public RoleLargeData(Role roleData, List<Long> privilegeIdList, List<Long> wfStepIdList) {
        super(roleData);
        this.privilegeIdList = privilegeIdList;
        this.wfStepIdList = wfStepIdList;
    }

    public List<Long> getPrivilegeIdList() {
        return privilegeIdList;
    }

    public void setPrivilegeIdList(List<Long> privilegeIdList) {
        this.privilegeIdList = privilegeIdList;
    }

    public List<Long> getWfStepIdList() {
        return wfStepIdList;
    }

    public void setWfStepIdList(List<Long> wfStepIdList) {
        this.wfStepIdList = wfStepIdList;
    }

}
