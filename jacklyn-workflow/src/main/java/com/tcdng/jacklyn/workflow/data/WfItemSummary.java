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

import java.io.Serializable;

import com.tcdng.unify.core.data.Listable;

/**
 * Workflow item summary.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfItemSummary implements Listable, Serializable {

    private static final long serialVersionUID = -5355531853254797968L;

    private String description;

    private String globalStepName;

    private String stepDesc;

    private int itemCount;

    private int holdCount;

    public WfItemSummary(String description, String globalStepName, String stepDesc, int itemCount, int holdCount) {
        this.description = description;
        this.globalStepName = globalStepName;
        this.stepDesc = stepDesc;
        this.itemCount = itemCount;
        this.holdCount = holdCount;
    }

    @Override
    public String getListKey() {
        return globalStepName;
    }

    @Override
    public String getListDescription() {
        return this.description;
    }

    public String getDescription() {
        return description;
    }

    public String getGlobalStepName() {
        return globalStepName;
    }

    public String getStepDesc() {
        return stepDesc;
    }

    public int getItemCount() {
        return itemCount;
    }

    public int getHoldCount() {
        return holdCount;
    }
}
