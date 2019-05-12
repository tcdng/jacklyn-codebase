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

import com.tcdng.unify.core.data.PackableDoc;

/**
 * Represents a viewable workflow item.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface ViewableWfItem {

    /**
     * Returns the item label
     */
    String getTitle();

    /**
     * Returns the document viewer name
     */
    String getDocViewer();

    /**
     * Returns workflow item packable document.
     */
    PackableDoc getPd();

    /**
     * Returns the workflow item template document definition.
     */
    WfTemplateDocDef getWfTemplateDocDef();

    /**
     * Returns the workflow item step.
     */
    WfStepDef getWfStepDef();

    /**
     * Returns the process global name
     */
    String getProcessGlobalName();
    
    /**
     * Returns the document name
     */
    String getDocName();

}
