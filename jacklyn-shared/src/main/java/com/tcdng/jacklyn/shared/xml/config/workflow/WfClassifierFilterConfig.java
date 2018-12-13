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
package com.tcdng.jacklyn.shared.xml.config.workflow;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.unify.core.operation.FilterConditionType;
import com.tcdng.unify.core.util.xml.adapter.FilterConditionTypeXmlAdapter;

/**
 * Workflow document Classifier filters configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfClassifierFilterConfig {

	private String field;
	
	private FilterConditionType op;
	
	private String value1;
	
	private String value2;
	
	private Boolean fieldOnly;

	public WfClassifierFilterConfig() {
		this.fieldOnly = Boolean.FALSE;
	}

	public String getField() {
		return field;
	}

	@XmlAttribute(required=true)
	public void setField(String field) {
		this.field = field;
	}

	public FilterConditionType getOp() {
		return op;
	}

    @XmlJavaTypeAdapter(FilterConditionTypeXmlAdapter.class)
	@XmlAttribute(required=true)
	public void setOp(FilterConditionType op) {
		this.op = op;
	}

	public String getValue1() {
		return value1;
	}

	@XmlAttribute
	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	@XmlAttribute
	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public Boolean getFieldOnly() {
		return fieldOnly;
	}

	@XmlAttribute(name="field-only")
	public void setFieldOnly(Boolean fieldOnly) {
		this.fieldOnly = fieldOnly;
	}
}
