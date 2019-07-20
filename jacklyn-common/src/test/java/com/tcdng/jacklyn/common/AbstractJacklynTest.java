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
package com.tcdng.jacklyn.common;

import com.tcdng.jacklyn.common.constants.CommonModuleNameConstants;
import com.tcdng.jacklyn.common.constants.JacklynContainerPropertyConstants;
import com.tcdng.unify.core.UnifyCorePropertyConstants;
import com.tcdng.unify.web.AbstractUnifyWebTest;

/**
 * Abstract jacklyn test.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractJacklynTest extends AbstractUnifyWebTest {

    @Override
    protected void doAddSettingsAndDependencies() throws Exception {
        super.doAddSettingsAndDependencies();//JacklynContainerPropertyConstants
        addContainerSetting(UnifyCorePropertyConstants.APPLICATION_NAME, "jacklyn-codebase");
        addContainerSetting(UnifyCorePropertyConstants.APPLICATION_BOOT, CommonModuleNameConstants.JACKLYNBOOTSERVICE);
        addContainerSetting(UnifyCorePropertyConstants.APPLICATION_MESSAGES_BASE,
                new String[] { "com.tcdng.unify.core.resources.test" });
        addContainerSetting(JacklynContainerPropertyConstants.SYSTEM_DEFAULT_EMAIL, "info@tcdng.com");
        addContainerSetting(JacklynContainerPropertyConstants.ADMINISTRATOR_DEFAULT_EMAIL, "info@tcdng.com");
    }

    protected void swapApplicationSystemAnonymousUserTokens() throws Exception {
        ((JacklynTestUtils) getComponent("jacklyn-testutils")).swapApplicationSystemAnonymousUserTokens();
    }
}
