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
package com.tcdng.jacklyn.report.business;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.common.business.ReportMultiFileOptionsGenerator;
import com.tcdng.jacklyn.common.constants.JacklynApplicationAttributeConstants;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.data.ReportColumnOptions;
import com.tcdng.jacklyn.common.data.ReportFileOptions;
import com.tcdng.jacklyn.common.data.ReportFilterOptions;
import com.tcdng.jacklyn.common.data.ReportJoinOptions;
import com.tcdng.jacklyn.common.data.ReportMultiFileOptions;
import com.tcdng.jacklyn.common.data.ReportOptions;
import com.tcdng.jacklyn.common.utils.JacklynUtils;
import com.tcdng.jacklyn.report.constants.ReportModuleNameConstants;
import com.tcdng.jacklyn.report.constants.ReportModuleSysParamConstants;
import com.tcdng.jacklyn.report.entities.ReportConfiguration;
import com.tcdng.jacklyn.report.entities.ReportConfigurationQuery;
import com.tcdng.jacklyn.report.entities.ReportFilter;
import com.tcdng.jacklyn.report.entities.ReportGroup;
import com.tcdng.jacklyn.report.entities.ReportGroupQuery;
import com.tcdng.jacklyn.report.entities.ReportParameter;
import com.tcdng.jacklyn.report.entities.ReportableDefinition;
import com.tcdng.jacklyn.report.entities.ReportableDefinitionQuery;
import com.tcdng.jacklyn.report.entities.ReportableField;
import com.tcdng.jacklyn.report.entities.ReportableFieldQuery;
import com.tcdng.jacklyn.shared.organization.PrivilegeCategoryConstants;
import com.tcdng.jacklyn.shared.report.ReportParameterConstants;
import com.tcdng.jacklyn.shared.xml.config.module.ColumnConfig;
import com.tcdng.jacklyn.shared.xml.config.module.FieldConfig;
import com.tcdng.jacklyn.shared.xml.config.module.FilterConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ManagedConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ParameterConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ReportConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ReportGroupConfig;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.system.constants.SystemModuleSysParamConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.Input;
import com.tcdng.unify.core.data.Inputs;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.sql.SqlDataSourceDialect;
import com.tcdng.unify.core.database.sql.SqlEntityInfo;
import com.tcdng.unify.core.report.Report;
import com.tcdng.unify.core.report.Report.Builder;
import com.tcdng.unify.core.report.ReportColumn;
import com.tcdng.unify.core.report.ReportFormat;
import com.tcdng.unify.core.report.ReportServer;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.GetterSetterInfo;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ThemeManager;

/**
 * Default implementation of report business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(ReportModuleNameConstants.REPORTSERVICE)
public class ReportServiceImpl extends AbstractJacklynBusinessService implements ReportService {

	@Configurable
	private ThemeManager themeManager;

	@Configurable
	private SystemService systemService;

	@Override
	public List<ReportableDefinition> findReportables(ReportableDefinitionQuery query) throws UnifyException {
		return db().listAll(query);
	}

	@Override
	public ReportableDefinition findReportDefinition(Long id) throws UnifyException {
		return db().list(ReportableDefinition.class, id);
	}

	@Override
	public List<ReportableDefinition> findRoleReportables(Long moduleId) throws UnifyException {
		// TODO
		return null;
	}

	@Override
	public List<ReportConfiguration> getRoleReportListing(String roleCode) throws UnifyException {
		ReportConfigurationQuery query = new ReportConfigurationQuery();
		if (StringUtils.isNotBlank(roleCode)) {
			Set<String> privilegeCodes = getRolePrivilegeCodes(roleCode, PrivilegeCategoryConstants.CONFIGUREDREPORTS);
			if (!privilegeCodes.isEmpty()) {
				query.nameIn(privilegeCodes);
			} else {
				return Collections.emptyList();
			}
		}

		query.status(RecordStatus.ACTIVE);
		query.addOrder("reportGroupDesc", "id");
		return db().listAll(query);
	}

	@Override
	public ReportOptions getReportOptionsForConfiguration(String reportConfigName) throws UnifyException {
		ReportConfiguration reportConfiguration = db().list(new ReportConfigurationQuery().name(reportConfigName));
		ReportOptions reportOptions = new ReportOptions();
		reportOptions.setReportLayout(reportConfiguration.getLayout());
		reportOptions.setReportName(reportConfiguration.getName());
		reportOptions.setReportDescription(reportConfiguration.getDescription().toUpperCase());
		reportOptions.setTitle(resolveSessionMessage(reportConfiguration.getTitle()));
		reportOptions.setProcessor(reportConfiguration.getProcessor());
		String multiFilePath = reportConfiguration.getMultiFilePathSystemParam() == null ? null
				: systemService.getSysParameterValue(String.class, reportConfiguration.getMultiFilePathSystemParam());
		reportOptions.setMultiReportPath(multiFilePath);
		reportOptions.setMultiReportOptionsGenerator(reportConfiguration.getMultiFileOptionsGenerator());
		reportOptions.setShowGrandFooter(reportConfiguration.isShowGrandFooter());
		reportOptions.setInvertGroupColors(reportConfiguration.isInvertGroupColors());
		reportOptions.setLandscape(reportConfiguration.isLandscape());
		reportOptions.setShadeOddRows(reportConfiguration.isShadeOddRows());
		reportOptions.setUnderlineRows(reportConfiguration.isUnderlineRows());

		// Report parameters
		List<Input<?>> userInputList = new ArrayList<Input<?>>();
		List<Input<?>> systemInputList = new ArrayList<Input<?>>();
		for (ReportParameter reportParam : reportConfiguration.getParameterList()) {
			String label = resolveSessionMessage(reportParam.getLabel());
			Input<?> holder = DataUtils.newInput(reportParam.getType().javaClass(), reportParam.getName(), label,
					reportParam.getEditor(), reportParam.getMandatory());
			String defaultVal = reportParam.getDefaultVal();
			if (defaultVal != null) {
				holder.setStringValue(defaultVal);
			}

			if (StringUtils.isNotBlank(reportParam.getEditor())) {
				userInputList.add(holder);
			} else {
				systemInputList.add(holder);
			}
		}
		reportOptions.setUserInputList(userInputList);
		reportOptions.setSystemInputList(systemInputList);

		return reportOptions;
	}

	@Override
	public void populateExtraConfigurationReportOptions(ReportOptions reportOptions) throws UnifyException {
		ReportConfiguration reportConfiguration = db()
				.list(new ReportConfigurationQuery().name(reportOptions.getReportName()));

		// Report column options
		SqlEntityInfo sqlEntityInfo = null;
		String className = reportConfiguration.getRecordName();
		if (StringUtils.isBlank(className)) {
			className = reportConfiguration.getBeanType();
		}

		Class<?> targetReportClass = null;
		if (StringUtils.isNotBlank(className)) {
			targetReportClass = ReflectUtils.getClassForName(className);
			if (Entity.class.isAssignableFrom(targetReportClass)) {
				sqlEntityInfo = ((SqlDataSourceDialect) db().getDataSource().getDialect())
						.findSqlEntityInfo(targetReportClass);
			}
		}

		Map<String, ReportableField> fieldMap = Collections.emptyMap();
		Long reportableDefinitionId = reportConfiguration.getReportableId();
		boolean isWithReportableDefinition = reportableDefinitionId != null;
		if (sqlEntityInfo != null) {
			fieldMap = db().listAllMap(String.class, "name",
					new ReportableFieldQuery().reportableId(reportableDefinitionId).installed(Boolean.TRUE));
			reportOptions.setTableName(sqlEntityInfo.getPreferredViewName());
		}

		if (!reportOptions.isWithColumnOptions()) { // Populate column options only on first run
			for (com.tcdng.jacklyn.report.entities.ReportColumn reportColumn : reportConfiguration.getColumnList()) {
				ReportColumnOptions reportColumnOptions = new ReportColumnOptions();
				reportColumnOptions.setDescription(reportColumn.getDescription());
				reportColumnOptions.setGroup(reportColumn.isGroup());
				reportColumnOptions.setGroupOnNewPage(reportColumn.isGroupOnNewPage());
				reportColumnOptions.setSum(reportColumn.isSum());
				reportColumnOptions.setOrderType(reportColumn.getColumnOrder());
				reportColumnOptions.setIncluded(true);

				String type = reportColumn.getType();
				String formatter = reportColumn.getFormatter();
				HAlignType hAlignType = reportColumn.getHorizAlignType();
				int width = reportColumn.getWidth();
				if (sqlEntityInfo != null) {
					reportColumnOptions.setTableName(sqlEntityInfo.getPreferredViewName());

					if (StringUtils.isNotBlank(reportColumn.getFieldName())) {
						reportColumnOptions.setColumnName(
								sqlEntityInfo.getListFieldInfo(reportColumn.getFieldName()).getPreferredColumnName());

						if (isWithReportableDefinition) {
							ReportableField reportableField = fieldMap.get(reportColumn.getFieldName());
							if (type == null) {
								type = reportableField.getType();
							}

							if (formatter == null) {
								formatter = reportableField.getFormatter();
							}

							if (width <= 0 && reportableField.getWidth() != null) {
								width = reportableField.getWidth();
							}

							if (hAlignType == null) {
								hAlignType = HAlignType.fromName(reportableField.getHorizontalAlign());
							}
						}
					}
				} else {
					reportColumnOptions.setColumnName(reportColumn.getFieldName());
				}

				if (type == null) {
					GetterSetterInfo getterSetterInfo = ReflectUtils.getGetterInfo(targetReportClass,
							reportColumn.getFieldName());
					type = DataUtils.getWrapperClassName(getterSetterInfo.getType());
				}

				reportColumnOptions.setType(type);
				reportColumnOptions.setFormatter(formatter);
				reportColumnOptions.setHorizontalAlignment(hAlignType);
				reportColumnOptions.setWidth(width);
				reportOptions.addColumnOptions(reportColumnOptions);
			}
		}

		// Filter options
		List<ReportFilter> reportFilterList = reportConfiguration.getFilterList();
		if (DataUtils.isNotBlank(reportFilterList)) {
			Map<String, Object> parameters = Inputs.getTypeValuesByName(reportOptions.getSystemInputList());
			Inputs.getTypeValuesByNameIntoMap(reportOptions.getUserInputList(), parameters);
			ReportFilterOptions rootFilterOptions = new ReportFilterOptions(reportFilterList.get(0).getOperation(),
					new ArrayList<ReportFilterOptions>());
			popuplateChildReportFilterOptions(sqlEntityInfo, rootFilterOptions, parameters, reportFilterList, 1, 1);
			reportOptions.setFilterOptions(rootFilterOptions);
		}
	}

	@Override
	public ReportColumn[] findReportableColumns(String reportableName) throws UnifyException {
		ReportableDefinition reportableDefinition = db().find(new ReportableDefinitionQuery().name(reportableName));

		List<ReportableField> reportFieldList = db().findAll(new ReportableFieldQuery()
				.reportableId(reportableDefinition.getId()).installed(Boolean.TRUE).orderById());

		ReportColumn[] reportColumns = new ReportColumn[reportFieldList.size()];
		for (int i = 0; i < reportColumns.length; i++) {
			ReportableField reportableField = reportFieldList.get(i);
			reportColumns[i] = ReportColumn.newBuilder().title(reportableField.getDescription())
					.name(reportableField.getName()).className(reportableField.getType())
					.widthRatio(reportableField.getWidth()).formatter(reportableField.getFormatter())
					.horizontalAlignment(HAlignType.fromName(reportableField.getHorizontalAlign())).build();
		}
		return reportColumns;
	}

	@Override
	public ReportOptions getDynamicReportOptions(String recordName, List<String> priorityPropertyList)
			throws UnifyException {
		ReportableDefinition reportableDefinition = db().find(new ReportableDefinitionQuery().recordName(recordName));
		ReportOptions reportOptions = new ReportOptions();
		reportOptions.setReportName(reportableDefinition.getName());
		reportOptions.setTitle(reportableDefinition.getTitle());
		reportOptions.setRecordName(recordName);

		Map<String, ReportableField> fieldMap = db().listAllMap(String.class, "name", new ReportableFieldQuery()
				.reportableId(reportableDefinition.getId()).parameterOnly(false).installed(Boolean.TRUE));
		boolean isSelectAll = priorityPropertyList == null;
		if (!isSelectAll) {
			for (String property : priorityPropertyList) {
				if (property != null) {
					ReportableField reportableField = fieldMap.remove(property);
					if (reportableField != null) {
						ReportColumnOptions remoteColumnOptions = new ReportColumnOptions(reportableField.getName(),
								reportableField.getDescription(), reportableField.getType(),
								reportableField.getFormatter(),
								HAlignType.fromName(reportableField.getHorizontalAlign()), reportableField.getWidth(),
								true);
						reportOptions.addColumnOptions(remoteColumnOptions);
					}
				}
			}
		}

		// Add what's left
		for (ReportableField reportableField : fieldMap.values()) {
			ReportColumnOptions remoteColumnOptions = new ReportColumnOptions(reportableField.getName(),
					reportableField.getDescription(), reportableField.getType(), reportableField.getFormatter(),
					HAlignType.fromName(reportableField.getHorizontalAlign()), reportableField.getWidth(), isSelectAll);
			reportOptions.addColumnOptions(remoteColumnOptions);
		}

		return reportOptions;
	}

	@Override
	public void generateDynamicReport(ReportOptions reportOptions, OutputStream outputStream) throws UnifyException {
		ReportServer reportServer = getCommonReportServer();
		if (reportOptions.isMultiFileReport()) {
			List<Input<?>> inputList = new ArrayList<Input<?>>();
			if (!DataUtils.isBlank(reportOptions.getSystemInputList())) {
				inputList.addAll(reportOptions.getSystemInputList());
			}
			
			if (!DataUtils.isBlank(reportOptions.getUserInputList())) {
				inputList.addAll(reportOptions.getUserInputList());
			}
			
			ReportMultiFileOptionsGenerator optionsGenerator = (ReportMultiFileOptionsGenerator) getComponent(
					reportOptions.getMultiReportOptionsGenerator());
			ReportMultiFileOptions multiFileOptions = optionsGenerator.generate(reportOptions.getMultiReportPath(),
					inputList);
			final String path = multiFileOptions.getOutputPath();
			IOUtils.ensureDirectoryExists(path);
			for (ReportFileOptions fileOptions : multiFileOptions.getFileOptions()) {
				Report report = buildReport(reportOptions, fileOptions);
				reportServer.generateReport(report, IOUtils.buildFilename(path, fileOptions.getFileName()));
			}
		} else {
			Report report = buildReport(reportOptions, null);
			reportServer.generateReport(report, outputStream);
		}
	}
	
	@Override
	public boolean isReportable(String recordName) throws UnifyException {
		return db().find(new ReportableDefinitionQuery().name(recordName)) != null;
	}

	@Override
	public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {
		// Uninstall old records
		db().updateAll(new ReportableDefinitionQuery().installed(Boolean.TRUE),
				new Update().add("installed", Boolean.FALSE));

		// Install new and update old
		ReportableDefinition reportableDefinition = new ReportableDefinition();
		for (ModuleConfig moduleConfig : moduleConfigList) {
			Long moduleId = systemService.getModuleId(moduleConfig.getName());

			// Handle reportables first
			Map<String, Long> reportableIds = new HashMap<String, Long>();
			if (DataUtils.isNotBlank(moduleConfig.getReportableList())) {
				logDebug("Installing reportable definitions for module [{0}]...",
						resolveApplicationMessage(moduleConfig.getDescription()));
				ReportableDefinitionQuery rdQuery = new ReportableDefinitionQuery();
				for (ReportConfig reportConfig : moduleConfig.getReportableList()) {
					rdQuery.clear();
					String reportName = reportConfig.getName();
					String description = resolveApplicationMessage(reportConfig.getDescription());
					String title = reportConfig.getTitle();
					if (title == null) {
						title = description;
					}

					ReportableDefinition oldReportableDefinition = db().findLean(rdQuery.name(reportName));
					Long reportableId = null;
					if (oldReportableDefinition == null) {
						reportableDefinition = new ReportableDefinition();
						reportableDefinition.setModuleId(moduleId);
						reportableDefinition.setName(reportName);
						reportableDefinition.setRecordName(reportConfig.getReportable());
						reportableDefinition.setTitle(title);
						reportableDefinition.setDescription(description);
						reportableDefinition.setInstalled(Boolean.TRUE);
						populateChildList(moduleConfig, reportableDefinition);
						reportableId = (Long) db().create(reportableDefinition);
					} else {
						// Update old definition
						oldReportableDefinition.setRecordName(reportConfig.getReportable());
						oldReportableDefinition.setTitle(title);
						oldReportableDefinition.setDescription(description);
						oldReportableDefinition.setInstalled(Boolean.TRUE);
						populateChildList(moduleConfig, oldReportableDefinition);
						db().updateByIdVersion(oldReportableDefinition);
						reportableId = oldReportableDefinition.getId();
					}

					reportableIds.put(reportConfig.getReportable(), reportableId);
				}
			}

			// Handle configured reports
			if (moduleConfig.getReports() != null
					&& DataUtils.isNotBlank(moduleConfig.getReports().getReportGroupList())) {
				logDebug("Installing configured report definitions for module [{0}]...",
						resolveApplicationMessage(moduleConfig.getDescription()));
				for (ReportGroupConfig reportGroupConfig : moduleConfig.getReports().getReportGroupList()) {
					ReportGroupQuery rgQuery = new ReportGroupQuery();
					ReportGroup oldReportGroup = db()
							.find(rgQuery.moduleId(moduleId).name(reportGroupConfig.getName()));
					Long reportGroupId = null;
					if (oldReportGroup == null) {
						ReportGroup reportGroup = new ReportGroup();
						reportGroup.setModuleId(moduleId);
						reportGroup.setName(reportGroupConfig.getName());
						reportGroup.setDescription(reportGroupConfig.getDescription());
						reportGroupId = (Long) db().create(reportGroup);
					} else {
						oldReportGroup.setDescription(reportGroupConfig.getDescription());
						db().updateByIdVersion(oldReportGroup);
						reportGroupId = oldReportGroup.getId();
					}

					ReportConfigurationQuery rcQuery = new ReportConfigurationQuery();
					if (DataUtils.isNotBlank(reportGroupConfig.getReportList())) {
						for (ReportConfig reportConfig : reportGroupConfig.getReportList()) {
							rcQuery.clear();
							String description = resolveApplicationMessage(reportConfig.getDescription());
							String title = reportConfig.getTitle();
							if (title == null) {
								title = description;
							}

							ReportConfiguration oldReportConfiguration = db()
									.findLean(rcQuery.reportGroupId(reportGroupId).name(reportConfig.getName()));
							Long reportableId = null;
							if (StringUtils.isNotBlank(reportConfig.getReportable())) {
								reportableId = reportableIds.get(reportConfig.getReportable());
							}

							if (oldReportConfiguration == null) {
								ReportConfiguration reportConfiguration = new ReportConfiguration();
								reportConfiguration.setReportGroupId(reportGroupId);
								reportConfiguration.setReportableId(reportableId);
								if (reportableId == null) {
									reportConfiguration.setBeanType(reportConfig.getReportable());
								}

								reportConfiguration.setName(reportConfig.getName());
								reportConfiguration.setDescription(description);
								reportConfiguration.setTitle(title);
								reportConfiguration.setTemplate(reportConfig.getTemplate());
								reportConfiguration.setLayout(reportConfig.getLayout());
								reportConfiguration.setProcessor(reportConfig.getProcessor());
								reportConfiguration
										.setMultiFileOptionsGenerator(reportConfig.getMultiFileOptionsGenerator());
								reportConfiguration
										.setMultiFilePathSystemParam(reportConfig.getMultiFilePathSystemParam());
								reportConfiguration.setShowGrandFooter(reportConfig.isShowGrandFooter());
								reportConfiguration.setInvertGroupColors(reportConfig.isInvertGroupColors());
								reportConfiguration.setLandscape(reportConfig.isLandscape());
								reportConfiguration.setShadeOddRows(reportConfig.isShadeOddRows());
								reportConfiguration.setUnderlineRows(reportConfig.isUnderlineRows());
								populateChildList(reportConfig, reportConfiguration);
								db().create(reportConfiguration);
							} else {
								oldReportConfiguration.setReportableId(reportableId);
								if (reportableId == null) {
									oldReportConfiguration.setBeanType(reportConfig.getReportable());
								}

								oldReportConfiguration.setDescription(description);
								oldReportConfiguration.setTitle(title);
								oldReportConfiguration.setTemplate(reportConfig.getTemplate());
								oldReportConfiguration.setLayout(reportConfig.getLayout());
								oldReportConfiguration.setProcessor(reportConfig.getProcessor());
								oldReportConfiguration
										.setMultiFileOptionsGenerator(reportConfig.getMultiFileOptionsGenerator());
								oldReportConfiguration
										.setMultiFilePathSystemParam(reportConfig.getMultiFilePathSystemParam());
								oldReportConfiguration.setShowGrandFooter(reportConfig.isShowGrandFooter());
								oldReportConfiguration.setInvertGroupColors(reportConfig.isInvertGroupColors());
								oldReportConfiguration.setLandscape(reportConfig.isLandscape());
								oldReportConfiguration.setShadeOddRows(reportConfig.isShadeOddRows());
								oldReportConfiguration.setUnderlineRows(reportConfig.isUnderlineRows());
								populateChildList(reportConfig, oldReportConfiguration);
								db().updateByIdVersion(oldReportConfiguration);
							}
						}
					}

				}

			}
		}
	}

	private void populateChildList(ModuleConfig moduleConfig, ReportableDefinition reportableDefinition)
			throws UnifyException {
		List<ReportableField> fieldList = new ArrayList<ReportableField>();
		// Re-create/Create report fields
		ManagedConfig managedConfig = JacklynUtils.getManagedConfig(moduleConfig, reportableDefinition.getRecordName());
		for (FieldConfig rfd : managedConfig.getFieldList()) {
			if (rfd.isReportable()) {
				ReportableField reportableField = new ReportableField();
				reportableField.setDescription(rfd.getDescription());
				reportableField.setFormatter(rfd.getFormatter());
				reportableField.setHorizontalAlign(rfd.gethAlign());
				reportableField.setName(rfd.getName());
				reportableField.setParameterOnly(rfd.isParameterOnly());
				reportableField.setType(rfd.getType());
				reportableField.setWidth(rfd.getWidth());
				reportableField.setInstalled(Boolean.TRUE);
				fieldList.add(reportableField);
			}
		}

		reportableDefinition.setFieldList(fieldList);
	}

	private void populateChildList(ReportConfig reportConfig, ReportConfiguration reportConfiguration)
			throws UnifyException {
		// Columns
		if (reportConfig.getColumns() != null && DataUtils.isNotBlank(reportConfig.getColumns().getColumnList())) {
			List<com.tcdng.jacklyn.report.entities.ReportColumn> columnList = new ArrayList<com.tcdng.jacklyn.report.entities.ReportColumn>();
			for (ColumnConfig columnConfig : reportConfig.getColumns().getColumnList()) {
				com.tcdng.jacklyn.report.entities.ReportColumn reportColumn = new com.tcdng.jacklyn.report.entities.ReportColumn();
				reportColumn.setColumnOrder(columnConfig.getColumnOrder());
				reportColumn.setFieldName(columnConfig.getFieldName());
				reportColumn.setDescription(columnConfig.getDescription());
				reportColumn.setType(columnConfig.getType());
				reportColumn.setFormatter(columnConfig.getFormatter());
				reportColumn.setHorizAlignType(columnConfig.getHorizAlignType());
				reportColumn.setWidth(columnConfig.getWidth());
				reportColumn.setGroup(columnConfig.isGroup());
				reportColumn.setGroupOnNewPage(columnConfig.isGroupOnNewPage());
				reportColumn.setSum(columnConfig.isSum());
				columnList.add(reportColumn);
			}

			reportConfiguration.setColumnList(columnList);
		}

		// Parameters
		if (reportConfig.getParameters() != null
				&& DataUtils.isNotBlank(reportConfig.getParameters().getParameterList())) {
			List<ReportParameter> parameterList = new ArrayList<ReportParameter>();
			for (ParameterConfig parameterConfig : reportConfig.getParameters().getParameterList()) {
				ReportParameter reportParameter = new ReportParameter();
				reportParameter.setName(parameterConfig.getName());
				String description = parameterConfig.getDescription();
				if (StringUtils.isBlank(description)) {
					description = resolveApplicationMessage(parameterConfig.getLabel());
				}

				reportParameter.setDescription(description);
				reportParameter.setEditor(parameterConfig.getEditor());
				reportParameter.setLabel(parameterConfig.getLabel());
				reportParameter.setMandatory(parameterConfig.isMandatory());
				reportParameter.setType(parameterConfig.getType());
				reportParameter.setDefaultVal(parameterConfig.getDefaultVal());
				parameterList.add(reportParameter);
			}

			reportConfiguration.setParameterList(parameterList);
		}

		// Filters
		if (reportConfig.getFilter() != null) {
			List<ReportFilter> filterList = new ArrayList<ReportFilter>();
			populateChildFilterList(reportConfig.getFilter(), filterList, 0);
			reportConfiguration.setFilterList(filterList);
		}
	}

	private void populateChildFilterList(FilterConfig filterConfig, List<ReportFilter> filterList, int compoundIndex) {
		ReportFilter reportFilter = new ReportFilter();
		reportFilter.setFieldName(filterConfig.getFieldName());
		reportFilter.setOperation(filterConfig.getOp());
		reportFilter.setValue1(filterConfig.getValue1());
		reportFilter.setValue2(filterConfig.getValue2());
		reportFilter.setUseParameter(filterConfig.isUseParameter());
		reportFilter.setCompoundIndex(compoundIndex);
		filterList.add(reportFilter);

		if (filterConfig.getOp().isCompound() && DataUtils.isNotBlank(filterConfig.getFilterList())) {
			int subCompoundIndex = compoundIndex + 1;
			for (FilterConfig subFilterConfig : filterConfig.getFilterList()) {
				populateChildFilterList(subFilterConfig, filterList, subCompoundIndex);
			}
		}
	}

	private void buildReportFilter(Builder rb, ReportFilterOptions filterOptions, ReportFileOptions fileOptions) {
		rb.beginCompoundFilter(filterOptions.getOp());
		for (ReportFilterOptions subFilterOptions : filterOptions.getSubFilterOptionList()) {
			if (subFilterOptions.isCompound()) {
				buildReportFilter(rb, subFilterOptions, fileOptions);
			} else {
				Object param1 = subFilterOptions.getParam1();
				if (fileOptions != null && fileOptions.isParam(subFilterOptions.getFieldName())) {
					param1 = fileOptions.getValue(subFilterOptions.getFieldName());
				}

				if (subFilterOptions.isUseParam() && param1 == null) {
					continue;
				}

				rb.addSimpleFilter(subFilterOptions.getOp(), subFilterOptions.getTableName(),
						subFilterOptions.getColumnName(), param1, subFilterOptions.getParam2());
			}
		}

		rb.endCompoundFilter();
	}

	private int popuplateChildReportFilterOptions(SqlEntityInfo sqlEntityInfo, ReportFilterOptions parentFilterOptions,
			Map<String, Object> parameters, List<ReportFilter> reportFilterList, int index, int subCompoundIndex)
			throws UnifyException {
		while (index < reportFilterList.size()) {
			ReportFilter reportFilter = reportFilterList.get(index++);
			if (reportFilter.getCompoundIndex() == subCompoundIndex) {
				if (reportFilter.getOperation().isCompound()) {
					ReportFilterOptions childFilterOptions = new ReportFilterOptions(reportFilter.getOperation(),
							new ArrayList<ReportFilterOptions>());
					index = popuplateChildReportFilterOptions(sqlEntityInfo, parentFilterOptions, parameters,
							reportFilterList, index, subCompoundIndex + 1);
					parentFilterOptions.getSubFilterOptionList().add(childFilterOptions);
				} else {
					Object param1 = reportFilter.getValue1();
					Object param2 = reportFilter.getValue2();
					if (reportFilter.isUseParameter()) {
						if (reportFilter.getValue1() != null) {
							param1 = parameters.get(reportFilter.getValue1());
//							if (param1 == null) {
//								continue;
//							}
						}

						if (reportFilter.getValue2() != null) {
							param2 = parameters.get(reportFilter.getValue2());
//							if (param2 == null) {
//								continue;
//							}
						}
					}

					if (reportFilter.getOperation().isRange()) {
						if (param1 instanceof Date) {
							param1 = CalendarUtils.getMidnightDate((Date) param1);
						}

						if (param2 instanceof Date) {
							param2 = CalendarUtils.getLastSecondDate((Date) param2);
						}
					}

					ReportFilterOptions childFilterOptions = new ReportFilterOptions(reportFilter.getOperation(),
							reportFilter.getFieldName(), sqlEntityInfo.getPreferredViewName(),
							sqlEntityInfo.getListFieldInfo(reportFilter.getFieldName()).getPreferredColumnName(),
							param1, param2, reportFilter.isUseParameter());
					parentFilterOptions.getSubFilterOptionList().add(childFilterOptions);
				}
			} else {
				index--;
				break;
			}
		}

		return index;
	}

	private ReportServer getCommonReportServer() throws UnifyException {
		return (ReportServer) getApplicationAttribute(JacklynApplicationAttributeConstants.COMMON_REPORT_SERVER);
	}

	private Report buildReport(ReportOptions reportOptions, ReportFileOptions fileOptions) throws UnifyException {
		Report.Builder rb = Report.newBuilder();
		rb.code(reportOptions.getReportName());
		rb.title(reportOptions.getTitle());
		rb.dataSource(reportOptions.getDataSource());
		rb.processor(reportOptions.getProcessor());
		rb.pageWidth(reportOptions.getPageWidth());
		rb.pageHeight(reportOptions.getPageHeight());
		rb.dynamicDataSource(reportOptions.isDynamicDataSource());
		rb.printColumnNames(reportOptions.isPrintColumnNames());
		rb.printGroupColumnNames(reportOptions.isPrintGroupColumnNames());
		rb.showGrandFooter(reportOptions.isShowGrandFooter());
		rb.invertGroupColors(reportOptions.isInvertGroupColors());
		rb.underlineRows(reportOptions.isUnderlineRows());
		rb.shadeOddRows(reportOptions.isShadeOddRows());
		rb.landscape(reportOptions.isLandscape());
		rb.format(ReportFormat.fromName(reportOptions.getReportFormat()));
		if (StringUtils.isNotBlank(reportOptions.getReportLayout())) {
			rb.layout(reportOptions.getReportLayout());
		}

		if (DataUtils.isNotBlank(reportOptions.getSystemInputList())) {
			for (Input<?> input : reportOptions.getSystemInputList()) {
				rb.addParameter(input.getName(), input.getDescription(), input.getTypeValue());
			}
		}

		if (DataUtils.isNotBlank(reportOptions.getUserInputList())) {
			for (Input<?> input : reportOptions.getUserInputList()) {
				rb.addParameter(input.getName(), input.getDescription(), input.getTypeValue());
			}
		}
		
		List<ReportColumnOptions> reportColumnOptionsList = new ArrayList<ReportColumnOptions>(
				reportOptions.getColumnOptionsList());
		DataUtils.sortDescending(reportColumnOptionsList, ReportColumnOptions.class, "group");

		List<ReportColumnOptions> sortReportColumnOptionsList = new ArrayList<ReportColumnOptions>();
		// TODO Get from report data source. For now just get from application data
		// source
		String sqlBlobTypeName = ((SqlDataSourceDialect) db().getDataSource().getDialect()).getSqlBlobType();
		for (ReportColumnOptions reportColumnOptions : reportColumnOptionsList) {
			if (reportColumnOptions.isIncluded()) {
				if (reportColumnOptions.isGroup() || reportColumnOptions.getOrderType() != null) {
					sortReportColumnOptionsList.add(reportColumnOptions);
				}

				rb.addColumn(reportColumnOptions.getDescription(), reportColumnOptions.getTableName(),
						reportColumnOptions.getColumnName(), reportColumnOptions.getType(), sqlBlobTypeName,
						reportColumnOptions.getFormatter(), reportColumnOptions.getOrderType(),
						reportColumnOptions.getHorizontalAlignment(), reportColumnOptions.getWidth(),
						reportColumnOptions.isGroup(), reportColumnOptions.isGroupOnNewPage(),
						reportColumnOptions.isSum());
			}
		}

		if (reportOptions.isBeanCollection()) {
			Class<?> dataClass = ReflectUtils.getClassForName(reportOptions.getRecordName());
			List<?> content = reportOptions.getContent();
			for (int i = sortReportColumnOptionsList.size() - 1; i >= 0; i--) {
				ReportColumnOptions reportColumnOptions = sortReportColumnOptionsList.get(i);
				if (OrderType.ASCENDING.equals(reportColumnOptions.getOrderType())) {
					DataUtils.sortAscending(content, dataClass, reportColumnOptions.getColumnName());
				} else {
					DataUtils.sortDescending(content, dataClass, reportColumnOptions.getColumnName());
				}
			}
			rb.beanCollection(content);
		} else {
			rb.query(reportOptions.getQuery());
			rb.table(reportOptions.getTableName());

			if (reportOptions.isWithJoinOptions()) {
				for (ReportJoinOptions rjo : reportOptions.getJoinOptionsList()) {
					rb.addJoin(rjo.getTableA(), rjo.getColumnA(), rjo.getTableB(), rjo.getColumnB());
				}
			}

			if (reportOptions.isWithFilterOptions()) {
				buildReportFilter(rb, reportOptions.getFilterOptions(), fileOptions);
			}
		}

		Report report = rb.build();
		setCommonReportParameters(report);
		return report;
	}

	private void setCommonReportParameters(Report report) throws UnifyException {
		report.setParameter(ReportParameterConstants.APPLICATION_TITLE, "Application Title",
				getUnifyComponentContext().getInstanceName());
		report.setParameter(ReportParameterConstants.CLIENT_TITLE, "Client Title",
				systemService.getSysParameterValue(String.class, SystemModuleSysParamConstants.SYSPARAM_CLIENT_TITLE));
		report.setParameter(ReportParameterConstants.REPORT_TITLE, "Report Title", report.getTitle());

		String imagePath = themeManager.expandThemeTag(
				systemService.getSysParameterValue(String.class, ReportModuleSysParamConstants.REPORT_CLIENT_LOGO));
		byte[] clientLogo = IOUtils.readFileResourceInputStream(imagePath, getUnifyComponentContext().getWorkingPath());
		report.setParameter(ReportParameterConstants.CLIENT_LOGO, "Client Logo", clientLogo);

		String templatePath = systemService.getSysParameterValue(String.class,
				ReportModuleSysParamConstants.REPORT_TEMPLATE_PATH);
		String template = report.getTemplate();
		if (template == null) {
			String templateParameter = ReportModuleSysParamConstants.DYNAMIC_REPORT_PORTRAIT_TEMPLATE;
			if (report.isLandscape()) {
				templateParameter = ReportModuleSysParamConstants.DYNAMIC_REPORT_LANDSCAPE_TEMPLATE;
			}
			template = systemService.getSysParameterValue(String.class, templateParameter);
		}
		report.setTemplate(IOUtils.buildFilename(templatePath, template));
	}
}
