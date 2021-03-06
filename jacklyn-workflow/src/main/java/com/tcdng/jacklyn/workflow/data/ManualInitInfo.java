/*
 * Copyright 2018-2020 The Code Department
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

/**
 * Manual initiation information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ManualInitInfo {

    private String categoryName;

    private String categoryDesc;

    private String processGlobalName;

    private String processDesc;

    public ManualInitInfo(String categoryName, String categoryDesc, String processGlobalName, String processDesc) {
        this.categoryName = categoryName;
        this.categoryDesc = categoryDesc;
        this.processGlobalName = processGlobalName;
        this.processDesc = processDesc;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public String getProcessGlobalName() {
        return processGlobalName;
    }

    public String getProcessDesc() {
        return processDesc;
    }

}
