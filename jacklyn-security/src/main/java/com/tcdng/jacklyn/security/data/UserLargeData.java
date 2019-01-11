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
package com.tcdng.jacklyn.security.data;

import java.util.List;

import com.tcdng.jacklyn.common.entities.BaseLargeData;
import com.tcdng.jacklyn.security.entities.User;

/**
 * User large data.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class UserLargeData extends BaseLargeData<User> {

    private byte[] photograph;

    private List<Long> roleIdList;

    public UserLargeData() {
        super(new User());
    }

    public UserLargeData(User userData) {
        super(userData);
    }

    public UserLargeData(User userData, byte[] photograph, List<Long> roleIdList) {
        super(userData);
        this.photograph = photograph;
        this.roleIdList = roleIdList;
    }

    public byte[] getPhotograph() {
        return photograph;
    }

    public void setPhotograph(byte[] photograph) {
        this.photograph = photograph;
    }

    public List<Long> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<Long> roleIdList) {
        this.roleIdList = roleIdList;
    }
}
