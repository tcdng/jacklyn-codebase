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

package com.tcdng.jacklyn.common.constants;

import com.tcdng.unify.core.AbstractUnifyStaticSettings;

/**
 * Abstract base class for Jacklyn module static settings.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractJacklynModuleStaticSettings extends AbstractUnifyStaticSettings
        implements JacklynModuleStaticSettings {

    public static final int INSTALLATION_INDEX = 128;

    private String moduleComponent;

    private String moduleConfig;

    public AbstractJacklynModuleStaticSettings(String moduleComponent, String moduleConfig, String messageBase,
            int level) {
        super(messageBase, level);
        this.moduleComponent = moduleComponent;
        this.moduleConfig = moduleConfig;
    }

    public AbstractJacklynModuleStaticSettings(String moduleComponent, String moduleConfig, String messageBase) {
        super(messageBase, INSTALLATION_INDEX);
        this.moduleComponent = moduleComponent;
        this.moduleConfig = moduleConfig;
    }

    @Override
    public String getModuleComponent() {
        return moduleComponent;
    }

    @Override
    public String getModuleConfig() {
        return moduleConfig;
    }

}
