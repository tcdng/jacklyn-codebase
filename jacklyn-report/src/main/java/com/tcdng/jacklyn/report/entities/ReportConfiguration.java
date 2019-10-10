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

import java.util.List;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.report.constants.ReportModuleNameConstants;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Report configuration data. Represents a report setup.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = ReportModuleNameConstants.REPORT_MODULE, title = "Report Configuration", reportable = true, auditable = true)
@Table(name = "JKREPORTCONFIG", uniqueConstraints = { @UniqueConstraint({ "name" }),
		@UniqueConstraint({ "description" }) })
public class ReportConfiguration extends BaseVersionedStatusEntity {

	@ForeignKey(ReportGroup.class)
	private Long reportGroupId;

	@ForeignKey(type = ReportableDefinition.class, nullable = true)
	private Long reportableId;
    
    @Column(name = "BEAN_TY", length = 96, nullable =  true)
    private String beanType;
	
	@Column(name = "REPORTCONFIG_NM")
	private String name;

	@Column(name = "REPORTCONFIG_DESC", length = 48)
	private String description;

    @Column(length = 96)
    private String title;

    @Column(length = 64, nullable = true)
    private String template;

    @Column(length = 64, nullable = true)
    private String layout;

    @Column(length = 64, nullable = true)
    private String processor;

    @Column
    private boolean invertGroupColors;

    @Column
    private boolean landscape;

    @Column
    private boolean underlineRows;

    @Column
    private boolean shadeOddRows;

	@ChildList
	private List<ReportColumn> columnList;

	@ChildList
	private List<ReportParameter> parameterList;

    @ChildList
    private List<ReportFilter> filterList;

	@ListOnly(key = "reportGroupId", property = "name")
	private String reportGroupCode;

	@ListOnly(key = "reportGroupId", property = "description")
	private String reportGroupDesc;

	@ListOnly(key = "reportGroupId", property = "moduleId")
	private Long moduleId;

    @ListOnly(key = "reportGroupId", property = "moduleDesc")
    private String moduleDesc;

    @ListOnly(key = "reportableId", property = "recordName")
    private String recordName;

	@Override
	public String getDescription() {
		return description;
	}

	public Long getReportGroupId() {
		return reportGroupId;
	}

	public void setReportGroupId(Long reportGroupId) {
		this.reportGroupId = reportGroupId;
	}

	public Long getReportableId() {
        return reportableId;
    }

    public void setReportableId(Long reportableId) {
        this.reportableId = reportableId;
    }

    public String getBeanType() {
        return beanType;
    }

    public void setBeanType(String beanType) {
        this.beanType = beanType;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public boolean isInvertGroupColors() {
        return invertGroupColors;
    }

    public void setInvertGroupColors(boolean invertGroupColors) {
        this.invertGroupColors = invertGroupColors;
    }

    public boolean isLandscape() {
        return landscape;
    }

    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }

    public boolean isUnderlineRows() {
        return underlineRows;
    }

    public void setUnderlineRows(boolean underlineRows) {
        this.underlineRows = underlineRows;
    }

    public boolean isShadeOddRows() {
        return shadeOddRows;
    }

    public void setShadeOddRows(boolean shadeOddRows) {
        this.shadeOddRows = shadeOddRows;
    }

    public List<ReportColumn> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ReportColumn> columnList) {
		this.columnList = columnList;
	}

	public List<ReportParameter> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<ReportParameter> parameterList) {
		this.parameterList = parameterList;
	}

	public List<ReportFilter> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<ReportFilter> filterList) {
        this.filterList = filterList;
    }

    public String getReportGroupCode() {
		return reportGroupCode;
	}

	public void setReportGroupCode(String reportGroupCode) {
		this.reportGroupCode = reportGroupCode;
	}

	public String getReportGroupDesc() {
		return reportGroupDesc;
	}

	public void setReportGroupDesc(String reportGroupDesc) {
		this.reportGroupDesc = reportGroupDesc;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleDesc() {
		return moduleDesc;
	}

	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }
}
