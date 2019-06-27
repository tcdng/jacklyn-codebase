/*
 * Copyright 2018-2019 The Code Department
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

package com.tcdng.jacklyn.workflow.web.widgets;

import com.tcdng.unify.core.data.PackableDoc;
import com.tcdng.unify.core.upl.AbstractUplGenerator;
import com.tcdng.unify.core.upl.UplUtils;

/**
 * Convenient abstract base class for workflow document UPL generators.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractWfDocUplGenerator extends AbstractUplGenerator implements WfDocUplGenerator {

    public AbstractWfDocUplGenerator(String uplComponentName) {
        super(uplComponentName);
    }

    /**
     * This default implementation creates a viewer name using the generator name as
     * base and the packable document global workflow document name as target.
     */
    @Override
    public String getDocViewer(PackableDoc pd) {
        return UplUtils.generateUplGeneratorTargetName(getName(), pd.getConfig().getName());
    }
}
