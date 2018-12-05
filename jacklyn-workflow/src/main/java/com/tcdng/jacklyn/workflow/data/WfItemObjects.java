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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tcdng.jacklyn.workflow.entities.WfItem;

/**
 * Workflow items.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfItemObjects implements Serializable {

	private static final long serialVersionUID = 161375477672079722L;

	private String wfStepName;

	private List<WfItem> wfItemList;

	private List<WfAction> actions;

	private Set<String> validatePageActions;

	public WfItemObjects(String wfStepName, List<WfItem> wfItemList, List<WfAction> actions) {
		this.wfStepName = wfStepName;
		this.wfItemList = wfItemList;
		this.actions = actions;
		this.validatePageActions = new HashSet<String>();
		for (WfAction wfAction : actions) {
			if (wfAction.isValidatePage()) {
				this.validatePageActions.add(wfAction.getName());
			}
		}

		this.validatePageActions = Collections.unmodifiableSet(this.validatePageActions);
	}

	public String getWfStepName() {
		return wfStepName;
	}

	public List<WfItem> getWfItemList() {
		return wfItemList;
	}

	public List<WfAction> getActionList() {
		return actions;
	}

	public boolean isActionListItems() {
		return !this.actions.isEmpty();
	}

	public Set<String> getValidatePageActions() {
		return validatePageActions;
	}
}
