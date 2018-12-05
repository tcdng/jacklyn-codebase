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
package com.tcdng.jacklyn.shared.xml.config.module;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;
import com.tcdng.unify.core.logging.EventType;
import com.tcdng.unify.core.util.xml.adapter.EventTypeXmlAdapter;

/**
 * Audit configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class AuditConfig extends BaseConfig {

	private EventType action;

	private String auditable;

	public EventType getAction() {
		return action;
	}

    @XmlJavaTypeAdapter(EventTypeXmlAdapter.class)
	@XmlAttribute(required = true)
	public void setAction(EventType action) {
		this.action = action;
	}

	public String getAuditable() {
		return auditable;
	}

	@XmlAttribute
	public void setAuditable(String auditable) {
		this.auditable = auditable;
	}
	
	public boolean isType() {
		return this.auditable != null;
	}
}
