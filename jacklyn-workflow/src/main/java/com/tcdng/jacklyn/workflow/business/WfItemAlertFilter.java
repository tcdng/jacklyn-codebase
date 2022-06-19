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

import com.tcdng.jacklyn.workflow.data.WfAlertDef;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem.Reader;
import com.tcdng.unify.core.UnifyException;

/**
 * Workflow item alert filter.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface WfItemAlertFilter extends WfItemPolicy {

	/**
	 * Accepts for alert.
	 * 
	 * @param flowingWfItemReader the workflow item reader
	 * @param wfAlertDef          the alert definition
	 * @return true if alert is accepted otherwise false
	 * @throws UnifyException if an error occurs
	 */
	boolean acceptAlert(Reader flowingWfItemReader, WfAlertDef wfAlertDef) throws UnifyException;
}
