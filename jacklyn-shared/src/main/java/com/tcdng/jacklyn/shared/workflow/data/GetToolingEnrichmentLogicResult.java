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

package com.tcdng.jacklyn.shared.workflow.data;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tcdng.unify.web.RemoteCallResult;

/**
 * Get tooling enrichment logic request result.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@XmlRootElement
public class GetToolingEnrichmentLogicResult extends RemoteCallResult {

    private List<ToolingEnrichmentLogicItem> enrichmentLogicList;

    public GetToolingEnrichmentLogicResult(List<ToolingEnrichmentLogicItem> enrichmentLogicList) {
        this.enrichmentLogicList = enrichmentLogicList;
    }

    public GetToolingEnrichmentLogicResult() {

    }

    public List<ToolingEnrichmentLogicItem> getEnrichmentLogicList() {
        return enrichmentLogicList;
    }

    @XmlElement(name = "enrichmentLogic")
    public void setEnrichmentLogicList(List<ToolingEnrichmentLogicItem> enrichmentLogicList) {
        this.enrichmentLogicList = enrichmentLogicList;
    }
}
