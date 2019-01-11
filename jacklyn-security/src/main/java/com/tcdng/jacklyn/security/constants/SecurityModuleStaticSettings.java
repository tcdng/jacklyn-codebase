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

package com.tcdng.jacklyn.security.constants;

import com.tcdng.jacklyn.common.constants.AbstractJacklynModuleStaticSettings;
import com.tcdng.unify.core.annotation.AutoDetect;

/**
 * Security module static settings
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@AutoDetect
public class SecurityModuleStaticSettings extends AbstractJacklynModuleStaticSettings {

    public static final int INSTALLATION_INDEX = 16;

    public SecurityModuleStaticSettings() {
        super(SecurityModuleNameConstants.SECURITYSERVICE, "config/security-module.xml",
                "com.tcdng.jacklyn.resources.security-messages", INSTALLATION_INDEX);
    }

}
