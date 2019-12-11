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
package com.tcdng.jacklyn.security.web.controllers;

import com.tcdng.jacklyn.common.web.beans.BasePageBean;
import com.tcdng.jacklyn.security.constants.SecurityModuleSysParamConstants;
import com.tcdng.jacklyn.security.entities.UserRole;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;

/**
 * Provides application forwarding method based on user role.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@ResultMappings({ @ResultMapping(
        name = "forwardtoapplication", response = { "!forwardresponse pathBinding:$s{applicationPath}" }) })
public abstract class AbstractApplicationForwarderController<T extends BasePageBean>
        extends AbstractSecurityPageController<T> {

    private String applicationPath;

    public AbstractApplicationForwarderController(Class<T> pageBeanClass, boolean secured, boolean readOnly,
            boolean resetOnWrite) {
        super(pageBeanClass, secured, readOnly, resetOnWrite);
    }

    public String getApplicationPath() {
        return applicationPath;
    }

    public void setApplicationPath(String applicationPath) {
        this.applicationPath = applicationPath;
    }

    /**
     * Forwards to application base on supplied user role.
     * 
     * @param userRole
     *            the user role information
     * @return the forward-to-application result mapping name
     * @throws UnifyException
     *             if an error occurs
     */
    protected String forwardToApplication(UserRole userRole) throws UnifyException {
        if (userRole != null) {
            applicationPath = userRole.getRoleApplication();
        }

        if (StringUtils.isBlank(applicationPath)) {
            applicationPath =
                    getSystemService().getSysParameterValue(String.class,
                            SecurityModuleSysParamConstants.USER_DEFAULT_APPLICATION);
        }

        getSecurityService().setCurrentUserRole(userRole);
        return "forwardtoapplication";
    }

    protected SystemService getSystemService() throws UnifyException {
        return (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);
    }

}
