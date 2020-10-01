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
package com.tcdng.jacklyn.common.data;

import java.util.List;

import com.tcdng.unify.core.criterion.RestrictionType;

/**
 * Report filter option.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ReportFilterOptions {

    private RestrictionType op;

    private String fieldName;

    private String tableName;

    private String columnName;

    private Object param1;

    private Object param2;
    
    private boolean useParam;

    private List<ReportFilterOptions> subFilterOptionList;

	public ReportFilterOptions(RestrictionType op, String fieldName, String tableName, String columnName, Object param1,
			Object param2, boolean useParam) {
		this.op = op;
		this.fieldName = fieldName;
		this.tableName = tableName;
		this.columnName = columnName;
		this.param1 = param1;
		this.param2 = param2;
		this.useParam = useParam;
	}

    public ReportFilterOptions(RestrictionType op, List<ReportFilterOptions> subFilterOptionList) {
        this.op = op;
        this.subFilterOptionList = subFilterOptionList;
    }

    public RestrictionType getOp() {
        return op;
    }

    public boolean isCompound() {
        return op.isCompound();
    }

    public String getFieldName() {
		return fieldName;
	}

	public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public Object getParam1() {
        return param1;
    }

    public Object getParam2() {
        return param2;
    }

    public boolean isUseParam() {
		return useParam;
	}

	public List<ReportFilterOptions> getSubFilterOptionList() {
        return subFilterOptionList;
    }

	@Override
	public String toString() {
		return "ReportFilterOptions [op=" + op + ", fieldName=" + fieldName + ", tableName=" + tableName
				+ ", columnName=" + columnName + ", param1=" + param1 + ", param2=" + param2 + ", subFilterOptionList="
				+ subFilterOptionList + "]";
	}
}
