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
package com.tcdng.jacklyn.report.business;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.common.constants.JacklynApplicationAttributeConstants;
import com.tcdng.jacklyn.common.data.ReportColumnOptions;
import com.tcdng.jacklyn.common.data.ReportFilterOptions;
import com.tcdng.jacklyn.common.data.ReportJoinOptions;
import com.tcdng.jacklyn.common.data.ReportOptions;
import com.tcdng.jacklyn.common.utils.JacklynUtils;
import com.tcdng.jacklyn.report.constants.ReportModuleNameConstants;
import com.tcdng.jacklyn.report.constants.ReportModuleSysParamConstants;
import com.tcdng.jacklyn.report.entities.ReportableDefinition;
import com.tcdng.jacklyn.report.entities.ReportableDefinitionQuery;
import com.tcdng.jacklyn.report.entities.ReportableField;
import com.tcdng.jacklyn.report.entities.ReportableFieldQuery;
import com.tcdng.jacklyn.shared.report.ReportParameterConstants;
import com.tcdng.jacklyn.shared.xml.config.module.FieldConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ManagedConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ParameterConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ReportConfig;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.system.constants.SystemModuleSysParamConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.operation.Update;
import com.tcdng.unify.core.report.Report;
import com.tcdng.unify.core.report.ReportColumn;
import com.tcdng.unify.core.report.ReportFormat;
import com.tcdng.unify.core.report.ReportLayout;
import com.tcdng.unify.core.report.ReportServer;
import com.tcdng.unify.core.system.entities.ParameterDef;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.web.util.WebUtils;

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
    private SystemService systemService;

    @Override
    public List<ReportableDefinition> findReportables(ReportableDefinitionQuery query) throws UnifyException {
        return db().listAll(query.installed(Boolean.TRUE));
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
    public ReportColumn[] findReportableColumns(String reportableName) throws UnifyException {
        ReportableDefinition reportableDefinition = db().find(new ReportableDefinitionQuery().name(reportableName));

        List<ReportableField> reportFieldList =
                db().findAll(new ReportableFieldQuery().reportableId(reportableDefinition.getId())
                        .installed(Boolean.TRUE).orderById());

        ReportColumn[] reportColumns = new ReportColumn[reportFieldList.size()];
        for (int i = 0; i < reportColumns.length; i++) {
            ReportableField reportableField = reportFieldList.get(i);
            reportColumns[i] =
                    ReportColumn.newBuilder().title(reportableField.getDescription()).name(reportableField.getName())
                            .className(reportableField.getType()).widthRatio(reportableField.getWidth())
                            .formatter(reportableField.getFormatter())
                            .horizontalAlignment(HAlignType.fromName(reportableField.getHorizontalAlign())).build();
        }
        return reportColumns;
    }

    @Override
    public ReportOptions getDynamicReportOptions(String recordName, List<String> priorityPropertyList)
            throws UnifyException {
        ReportableDefinition reportableDefinition =
                db().find(new ReportableDefinitionQuery().recordName(recordName).dynamic(true));
        ReportOptions reportOptions = new ReportOptions();
        reportOptions.setReportName(reportableDefinition.getName());
        reportOptions.setTitle(reportableDefinition.getTitle());
        reportOptions.setRecordName(recordName);

        Map<String, ReportableField> fieldMap =
                db().listAllMap(String.class, "name", new ReportableFieldQuery()
                        .reportableId(reportableDefinition.getId()).parameterOnly(false).installed(Boolean.TRUE));
        boolean isSelectAll = priorityPropertyList == null;
        if (!isSelectAll) {
            for (String property : priorityPropertyList) {
                if (property != null) {
                    ReportableField reportableField = fieldMap.remove(property);
                    if (reportableField != null) {
                        reportOptions.addColumnOptions(createReportColumnOptions(reportableField, true));
                    }
                }
            }
        }

        for (ReportableField reportableField : fieldMap.values()) {
            // Add what's left
            reportOptions.addColumnOptions(createReportColumnOptions(reportableField, isSelectAll));
        }

        return reportOptions;
    }

    @Override
    public void generateDynamicReport(ReportOptions reportOptions, OutputStream outputStream) throws UnifyException {
        Report.Builder rb = Report.newBuilder();
        rb.code(reportOptions.getReportName());
        rb.title(reportOptions.getTitle());
        rb.dataSource(reportOptions.getDataSource());
        rb.dynamicDataSource(reportOptions.isDynamicDataSource());
        rb.printColumnNames(reportOptions.isPrintColumnNames());
        rb.underlineRows(reportOptions.isUnderlineRows());
        rb.shadeOddRows(reportOptions.isShadeOddRows());
        rb.landscape(reportOptions.isLandscape());
        rb.format(ReportFormat.fromName(reportOptions.getReportFormat()));
        if (reportOptions.isColumnarLayout()) {
            rb.layout(ReportLayout.COLUMNAR);
        }

        List<ReportColumnOptions> reportColumnOptionsList =
                new ArrayList<ReportColumnOptions>(reportOptions.getColumnOptionsList());
        DataUtils.sort(reportColumnOptionsList, ReportColumnOptions.class, "group", false);

        List<ReportColumnOptions> sortReportColumnOptionsList = new ArrayList<ReportColumnOptions>();
        for (ReportColumnOptions reportColumnOptions : reportColumnOptionsList) {
            if (reportColumnOptions.isIncluded()) {
                if (reportColumnOptions.isGroup() || reportColumnOptions.getOrder() != null) {
                    sortReportColumnOptionsList.add(reportColumnOptions);
                }

                rb.addColumn(reportColumnOptions.getDescription(), reportColumnOptions.getTableName(),
                        reportColumnOptions.getColumnName(), reportColumnOptions.getType(),
                        reportColumnOptions.getFormatter(), OrderType.fromName(reportColumnOptions.getOrder()),
                        reportColumnOptions.getHorizontalAlignment(), reportColumnOptions.getWidth(),
                        reportColumnOptions.isGroup(), reportColumnOptions.isSum());
            }
        }

        if (reportOptions.isBeanCollection()) {
            Class<?> dataClass = ReflectUtils.getClassForName(reportOptions.getRecordName());
            List<?> content = reportOptions.getContent();
            for (int i = sortReportColumnOptionsList.size() - 1; i >= 0; i--) {
                ReportColumnOptions reportColumnOptions = sortReportColumnOptionsList.get(i);
                DataUtils.sort(content, dataClass, reportColumnOptions.getColumnName(),
                        OrderType.ASCENDING.code().equals(reportColumnOptions.getOrder()));
            }
            rb.beanCollection(content);
        } else {
            rb.query(reportOptions.getQuery());
            rb.table(reportOptions.getTableName());

            if (reportOptions.isJoinOptions()) {
                for (ReportJoinOptions rjo : reportOptions.getJoinOptionsList()) {
                    rb.addJoin(rjo.getTableA(), rjo.getColumnA(), rjo.getTableB(), rjo.getColumnB());
                }
            }

            if (reportOptions.isFilterOptions()) {
                for (ReportFilterOptions rfo : reportOptions.getFilterOptionsList()) {
                    rb.addFilter(rfo.getOp(), rfo.getTableName(), rfo.getColumnName(), rfo.getParam1(),
                            rfo.getParam2());
                }
            }
        }

        Report report = rb.build();
        setCommonReportParameters(report);
        getCommonReportServer().generateReport(report, outputStream);
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
            if (moduleConfig.getReports() != null) {
                logDebug("Installing report definitions for module [{0}]...",
                        resolveApplicationMessage(moduleConfig.getDescription()));
                ReportableDefinitionQuery rdQuery = new ReportableDefinitionQuery();
                for (ReportConfig reportConfig : moduleConfig.getReports().getReportList()) {
                    rdQuery.clear();
                    String reportName = reportConfig.getName();
                    String description = resolveApplicationMessage(reportConfig.getDescription());
                    String title = reportConfig.getTitle();
                    if (title == null) {
                        title = description;
                    }

                    ReportableDefinition oldReportableDefinition = db().find(rdQuery.name(reportName));
                    Long reportableId = null;
                    if (oldReportableDefinition == null) {
                        reportableDefinition = new ReportableDefinition();
                        reportableDefinition.setModuleId(moduleId);
                        reportableDefinition.setName(reportName);
                        reportableDefinition.setRecordName(reportConfig.getReportable());
                        reportableDefinition.setTitle(title);
                        reportableDefinition.setDescription(description);
                        reportableDefinition.setTemplate(reportConfig.getTemplate());
                        reportableDefinition.setProcessor(reportConfig.getProcessor());
                        reportableDefinition.setDynamic(reportConfig.isDynamic());
                        reportableId = (Long) db().create(reportableDefinition);
                    } else {
                        // Update old definition
                        oldReportableDefinition.setRecordName(reportConfig.getReportable());
                        oldReportableDefinition.setTitle(title);
                        oldReportableDefinition.setDescription(description);
                        oldReportableDefinition.setTemplate(reportConfig.getTemplate());
                        oldReportableDefinition.setProcessor(reportConfig.getProcessor());
                        oldReportableDefinition.setDynamic(reportConfig.isDynamic());
                        oldReportableDefinition.setInstalled(Boolean.TRUE);
                        db().updateByIdVersion(oldReportableDefinition);
                        reportableId = oldReportableDefinition.getId();

                        // Delete old fields
                        db().deleteAll(new ReportableFieldQuery().reportableId(reportableId));
                    }

                    // Re-create/Create report fields
                    if (reportConfig.isManaged()) {
                        ManagedConfig managedConfig =
                                JacklynUtils.getManagedConfig(moduleConfig, reportConfig.getReportable());
                        ReportableField reportableField = new ReportableField();
                        reportableField.setReportableId(reportableId);
                        for (FieldConfig rfd : managedConfig.getFieldList()) {
                            if (rfd.isReportable()) {
                                reportableField.setDescription(rfd.getDescription());
                                reportableField.setFormatter(rfd.getFormatter());
                                reportableField.setHorizontalAlign(rfd.gethAlign());
                                reportableField.setName(rfd.getName());
                                reportableField.setParameterOnly(rfd.isParameterOnly());
                                reportableField.setType(rfd.getType());
                                reportableField.setWidth(rfd.getWidth());
                                db().create(reportableField);
                            }
                        }
                    }

                    // Re-create/Create report parameters
                    if (reportConfig.getParameters() != null) {
                        List<ParameterDef> parameterDefinitionList = new ArrayList<ParameterDef>();
                        for (ParameterConfig rpd : reportConfig.getParameters().getParameterList()) {
                            ParameterDef parameterDef = new ParameterDef();
                            parameterDef.setName(rpd.getName());
                            parameterDef.setDescription(rpd.getDescription());
                            parameterDef.setEditor(rpd.getEditor());
                            parameterDef.setType(rpd.getType());
                            parameterDef.setMandatory(rpd.isMandatory());
                            parameterDefinitionList.add(parameterDef);
                        }

                        getParameterService().defineParameters(reportName, parameterDefinitionList);
                    }
                }
            }
        }
    }

    private ReportServer getCommonReportServer() throws UnifyException {
        return (ReportServer) getApplicationAttribute(JacklynApplicationAttributeConstants.COMMON_REPORT_SERVER);
    }

    private ReportColumnOptions createReportColumnOptions(ReportableField reportableField, boolean included) {
        return new ReportColumnOptions(reportableField.getName(), reportableField.getDescription(),
                reportableField.getType(), reportableField.getFormatter(),
                HAlignType.fromName(reportableField.getHorizontalAlign()), reportableField.getWidth(), included);
    }

    private void setCommonReportParameters(Report report) throws UnifyException {
        report.setParameter(ReportParameterConstants.APPLICATION_TITLE, getUnifyComponentContext().getInstanceName());
        report.setParameter(ReportParameterConstants.CLIENT_TITLE,
                systemService.getSysParameterValue(String.class, SystemModuleSysParamConstants.SYSPARAM_CLIENT_TITLE));
        report.setParameter(ReportParameterConstants.REPORT_TITLE, report.getTitle());

        String imagePath =
                WebUtils.expandThemeTag(systemService.getSysParameterValue(String.class,
                        ReportModuleSysParamConstants.REPORT_CLIENT_LOGO));
        byte[] clientLogo = IOUtils.readFileResourceInputStream(imagePath, getUnifyComponentContext().getWorkingPath());
        report.setParameter(ReportParameterConstants.CLIENT_LOGO, clientLogo);

        String templatePath =
                systemService.getSysParameterValue(String.class, ReportModuleSysParamConstants.REPORT_TEMPLATE_PATH);
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
