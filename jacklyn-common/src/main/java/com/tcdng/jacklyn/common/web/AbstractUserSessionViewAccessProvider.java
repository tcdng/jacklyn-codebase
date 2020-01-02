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

package com.tcdng.jacklyn.common.web;

import com.tcdng.jacklyn.shared.security.SecurityPrivilegeConstants;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Convenient base class for user session view access providers.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractUserSessionViewAccessProvider extends AbstractUnifyComponent
        implements UserSessionViewAccessProvider {

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    /**
     * Checks if current user has application administrator view.
     * 
     * @return a true value if current user has such privilege
     * @throws UnifyException
     *             if an error occurs
     */
    protected boolean isAppAdminView() throws UnifyException {
        return getViewDirective(SecurityPrivilegeConstants.APPLICATION_ADMIN).isVisible();
    }

    /**
     * Checks if current user has hub administrator view.
     * 
     * @return a true value if current user has such privilege
     * @throws UnifyException
     *             if an error occurs
     */
    protected boolean isHubAdminView() throws UnifyException {
        return getViewDirective(SecurityPrivilegeConstants.HUB_ADMIN).isVisible();
    }

}
