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
import java.util.Date;
import java.util.List;

/**
 * Workflow form definition.
 * 
 * @author Lateef
 * @since 1.0
 */
public class WfFormDef extends BaseWfDef {

	private static final long serialVersionUID = 5731603377730336455L;

	private Long wfFormId;

	private String globalName;

	private Date timestamp;

	private List<WfFormTabDef> tabs;

	private boolean read;

	public WfFormDef(Long wfFormId, String globalName, String name, String description,
			Date timestamp, List<WfFormTabDef> tabList) {
		super(name, description);
		this.wfFormId = wfFormId;
		this.globalName = globalName;
		this.timestamp = timestamp;
		if (tabList != null) {
			tabs = Collections.unmodifiableList(tabList);
		} else {
			tabs = Collections.emptyList();
		}

		read = false;
	}

	public Long getWfFormId() {
		return wfFormId;
	}

	public List<WfFormTabDef> getTabs() {
		return tabs;
	}

	public String getGlobalName() {
		return globalName;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public List<WfFormTabDef> getTabList() {
		return tabs;
	}

	public boolean isRead() {
		return read;
	}

	public void read() {
		read = true;
	}
}
