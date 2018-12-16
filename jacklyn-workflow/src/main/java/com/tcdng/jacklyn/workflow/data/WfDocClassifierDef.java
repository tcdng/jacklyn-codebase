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

package com.tcdng.jacklyn.workflow.data;

import java.util.Collections;
import java.util.List;

/**
 * Workflow document classifier definition.
 * 
 * @author Lateef
 * @since 1.0
 */
public class WfDocClassifierDef extends BaseWfDef {

    private static final long serialVersionUID = 4973270364809680350L;

    private String logic;

    private List<WfDocClassifierFilterDef> filterList;

    public WfDocClassifierDef(String name, String description, String logic,
            List<WfDocClassifierFilterDef> filterList) {
        super(name, description);
        this.logic = logic;
        if (filterList != null) {
            this.filterList = Collections.unmodifiableList(filterList);
        } else {
            this.filterList = Collections.emptyList();
        }
    }

    public String getLogic() {
        return logic;
    }

    public List<WfDocClassifierFilterDef> getFilterList() {
        return filterList;
    }
}
