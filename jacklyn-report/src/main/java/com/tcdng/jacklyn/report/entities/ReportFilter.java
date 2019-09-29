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
package com.tcdng.jacklyn.report.entities;

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.criterion.RestrictionType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Report filter.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table("JKREPORTFILTER")
public class ReportFilter extends BaseEntity {

	@ForeignKey(ReportConfiguration.class)
	private Long reportConfigurationId;

	@ForeignKey(name = "OPERATION_TY")
	private RestrictionType operation;

    @Column(name = "FIELD_NM", nullable = true)
    private String fieldName;

    @Column(name = "FILTER_VALUE1", nullable = true, length = 128)
    private String value1;

    @Column(name = "FILTER_VALUE2", nullable = true, length = 128)
    private String value2;

	@Column
	private boolean useParameter;

    @Column
	private int compoundIndex;

	@ListOnly(key = "operation", property = "description", name = "OPERATION_DESC")
	private String operationDesc;

	@Override
	public String getDescription() {
		return StringUtils.concatenate(operationDesc, "(", fieldName, ')');
	}

	public Long getReportConfigurationId() {
		return reportConfigurationId;
	}

	public void setReportConfigurationId(Long reportConfigurationId) {
		this.reportConfigurationId = reportConfigurationId;
	}

	public RestrictionType getOperation() {
		return operation;
	}

	public void setOperation(RestrictionType operation) {
		this.operation = operation;
	}

	public boolean isUseParameter() {
		return useParameter;
	}

	public void setUseParameter(boolean useParameter) {
		this.useParameter = useParameter;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public int getCompoundIndex() {
        return compoundIndex;
    }

    public void setCompoundIndex(int compoundIndex) {
        this.compoundIndex = compoundIndex;
    }

    public String getOperationDesc() {
		return operationDesc;
	}

	public void setOperationDesc(String operationDesc) {
		this.operationDesc = operationDesc;
	}

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
