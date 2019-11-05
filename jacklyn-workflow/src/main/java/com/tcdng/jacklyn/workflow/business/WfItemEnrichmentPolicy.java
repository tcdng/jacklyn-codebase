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

package com.tcdng.jacklyn.workflow.business;

import com.tcdng.jacklyn.workflow.data.FlowingWfItem.ReaderWriter;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Workflow item enrichment policy.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface WfItemEnrichmentPolicy extends UnifyComponent {

    /**
     * Enriches workflow item using supplied reader-writer.
     * 
     * @param flowingWfItemReaderWriter
     *            the reader-writer to use
     * @throws UnifyException
     *             if an error occurs
     */
    void enrich(ReaderWriter flowingWfItemReaderWriter) throws UnifyException;
}
