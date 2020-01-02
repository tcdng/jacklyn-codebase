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
package com.tcdng.jacklyn.workflow.business;

import com.tcdng.jacklyn.workflow.data.FlowingWfItem.Reader;
import com.tcdng.jacklyn.workflow.data.WfDocClassifierDef;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Workflow item classifier logic.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface WfItemClassifierLogic extends UnifyComponent {

    /**
     * Tests if workflow item matches classification.
     * 
     * @param flowingWfItemReader
     *            the flowing workflow item reader
     * @param wfDocClassifierDef
     *            classifier definition
     * @return a true value if matched otherwise false
     * @throws UnifyException
     *             if an error occurs
     */
    boolean match(Reader flowingWfItemReader, WfDocClassifierDef wfDocClassifierDef) throws UnifyException;
}
