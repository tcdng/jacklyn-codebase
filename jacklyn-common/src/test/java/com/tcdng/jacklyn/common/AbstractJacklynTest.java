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
package com.tcdng.jacklyn.common;

import com.tcdng.jacklyn.common.constants.CommonModuleNameConstants;
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
        super.doAddSettingsAndDependencies();
        addContainerSetting(UnifyCorePropertyConstants.APPLICATION_NAME, "jacklyn-codebase");
        addContainerSetting(UnifyCorePropertyConstants.APPLICATION_BOOT, CommonModuleNameConstants.JACKLYNBOOTMODULE);
        addContainerSetting(UnifyCorePropertyConstants.APPLICATION_MESSAGES_BASE,
                new String[] { "com.tcdng.unify.core.resources.test" });
    }

    protected void swapApplicationSystemAnonymousUserTokens() throws Exception {
        ((JacklynTestUtils) getComponent("jacklyn-testutils")).swapApplicationSystemAnonymousUserTokens();
    }
}
