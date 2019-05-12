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

package com.tcdng.jacklyn.workflow.data;

import com.tcdng.unify.core.data.Document;
import com.tcdng.unify.core.data.PackableDocRWConfig;

/**
 * Workflow document bean mapping definition.
 * 
 * @author Lateef
 * @since 1.0
 */
public class WfDocBeanMappingDef extends BaseWfDef {

    private PackableDocRWConfig rwConfig;

    private boolean receptacleMapping;

    private boolean primaryMapping;

    public WfDocBeanMappingDef(String name, String description, PackableDocRWConfig rwConfig, boolean receptacleMapping,
            boolean primaryMapping) {
        super(name, description);
        this.rwConfig = rwConfig;
        this.receptacleMapping = receptacleMapping;
        this.primaryMapping = primaryMapping;
    }

    public PackableDocRWConfig getRwConfig() {
        return rwConfig;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Document> getBeanType() {
        return (Class<? extends Document>) rwConfig.getBeanType();
    }

    public boolean isReceptacleMapping() {
        return receptacleMapping;
    }

    public boolean isPrimaryMapping() {
        return primaryMapping;
    }

}
