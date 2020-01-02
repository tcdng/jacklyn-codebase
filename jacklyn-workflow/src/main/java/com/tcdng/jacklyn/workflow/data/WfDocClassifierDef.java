/*
 * Copyright 2018-2020 The Code Department.
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

import java.util.List;

import com.tcdng.unify.core.constant.BinaryLogicType;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Workflow document classifier definition.
 * 
 * @author Lateef
 * @since 1.0
 */
public class WfDocClassifierDef extends BaseWfDef {
    
    private String logic;

    private BinaryLogicType filterLogic;

    private List<WfDocClassifierFilterDef> filterList;

    public WfDocClassifierDef(String name, String description, String logic,
            BinaryLogicType filterLogic, List<WfDocClassifierFilterDef> filterList) {
        super(name, description);
        this.logic = logic;
        this.filterLogic = filterLogic;
        this.filterList = DataUtils.unmodifiableList(filterList);
    }

    public String getLogic() {
        return logic;
    }

    public BinaryLogicType getFilterLogic() {
        return filterLogic;
    }

    public List<WfDocClassifierFilterDef> getFilterList() {
        return filterList;
    }
}
