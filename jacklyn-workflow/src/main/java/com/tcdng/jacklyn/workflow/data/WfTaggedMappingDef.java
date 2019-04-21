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

import java.io.Serializable;
import java.util.Date;

/**
 * Workflow tagged mapping definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfTaggedMappingDef implements Serializable {

    private static final long serialVersionUID = 5731603377730336455L;

    private String globalName;

    private WfDocDef wfDocDef;

    private WfTemplateDef wfTemplateDef;

    private Date timestamp;

    public WfTaggedMappingDef(String globalName, WfDocDef wfDocDef, WfTemplateDef wfTemplateDef, Date timestamp) {
        this.globalName = globalName;
        this.wfDocDef = wfDocDef;
        this.wfTemplateDef = wfTemplateDef;
        this.timestamp = timestamp;
    }

    public String getGlobalName() {
        return globalName;
    }

    public WfDocDef getWfDocDef() {
        return wfDocDef;
    }

    public WfTemplateDef getWfTemplateDef() {
        return wfTemplateDef;
    }

    public Date getTimestamp() {
        return timestamp;
    }

}
