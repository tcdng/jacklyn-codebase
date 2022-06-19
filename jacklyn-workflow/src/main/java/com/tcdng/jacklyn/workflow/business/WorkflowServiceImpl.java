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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.common.constants.JacklynSessionAttributeConstants;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.notification.business.NotificationService;
import com.tcdng.jacklyn.notification.entities.NotificationChannel;
import com.tcdng.jacklyn.notification.entities.NotificationChannelQuery;
import com.tcdng.jacklyn.notification.entities.NotificationTemplate;
import com.tcdng.jacklyn.notification.utils.NotificationUtils;
import com.tcdng.jacklyn.shared.constants.OrientationType;
import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.jacklyn.shared.workflow.WorkflowApplyActionTaskConstants;
import com.tcdng.jacklyn.shared.workflow.WorkflowCategoryBinaryPublicationTaskConstants;
import com.tcdng.jacklyn.shared.workflow.WorkflowCategoryPublicationTaskConstants;
import com.tcdng.jacklyn.shared.workflow.WorkflowExecuteTransitionTaskConstants;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.shared.workflow.data.ToolingEnrichmentLogicItem;
import com.tcdng.jacklyn.shared.workflow.data.ToolingItemClassifierLogicItem;
import com.tcdng.jacklyn.shared.workflow.data.ToolingPolicyLogicItem;
import com.tcdng.jacklyn.shared.workflow.data.ToolingWfDocUplGeneratorItem;
import com.tcdng.jacklyn.shared.workflow.data.ToolingWfItemAssignerItem;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfAlertConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfAttachmentCheckConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfAttachmentConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfBeanMappingConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfBranchConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfCategoryConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfClassifierConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfClassifierFilterConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfComplexFieldConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocumentConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfEnrichmentConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFieldConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFieldMappingConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormFieldConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormPrivilegeConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormSectionConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormTabConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfMergeConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfMessageConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfPolicyConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfRecordActionConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfRoutingConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfStepConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfTemplateConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfTemplateDocConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfUserActionConfig;
import com.tcdng.jacklyn.shared.xml.util.WfCategoryConfigUtils;
import com.tcdng.jacklyn.shared.xml.util.WfNameUtils;
import com.tcdng.jacklyn.shared.xml.util.WfNameUtils.DocNameParts;
import com.tcdng.jacklyn.shared.xml.util.WfNameUtils.ProcessNameParts;
import com.tcdng.jacklyn.shared.xml.util.WfNameUtils.StepNameParts;
import com.tcdng.jacklyn.shared.xml.util.WfNameUtils.TemplateNameParts;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.system.entities.Dashboard;
import com.tcdng.jacklyn.system.entities.DashboardLayer;
import com.tcdng.jacklyn.system.entities.DashboardPortlet;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleErrorConstants;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleSysParamConstants;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem;
import com.tcdng.jacklyn.workflow.data.FlowingWfItemTransition;
import com.tcdng.jacklyn.workflow.data.InteractWfItems;
import com.tcdng.jacklyn.workflow.data.ManualInitInfo;
import com.tcdng.jacklyn.workflow.data.ManualWfItem;
import com.tcdng.jacklyn.workflow.data.WfAction;
import com.tcdng.jacklyn.workflow.data.WfAlertDef;
import com.tcdng.jacklyn.workflow.data.WfAttachmentCheckDef;
import com.tcdng.jacklyn.workflow.data.WfBranchDef;
import com.tcdng.jacklyn.workflow.data.WfDocAttachmentDef;
import com.tcdng.jacklyn.workflow.data.WfDocClassifierDef;
import com.tcdng.jacklyn.workflow.data.WfDocClassifierFilterDef;
import com.tcdng.jacklyn.workflow.data.WfDocDef;
import com.tcdng.jacklyn.workflow.data.WfEnrichmentDef;
import com.tcdng.jacklyn.workflow.data.WfFormDef;
import com.tcdng.jacklyn.workflow.data.WfFormFieldDef;
import com.tcdng.jacklyn.workflow.data.WfFormPrivilegeDef;
import com.tcdng.jacklyn.workflow.data.WfFormSectionDef;
import com.tcdng.jacklyn.workflow.data.WfFormTabDef;
import com.tcdng.jacklyn.workflow.data.WfItemAssigneeInfo;
import com.tcdng.jacklyn.workflow.data.WfItemAttachmentInfo;
import com.tcdng.jacklyn.workflow.data.WfItemCountStatusInfo;
import com.tcdng.jacklyn.workflow.data.WfItemHistEvent;
import com.tcdng.jacklyn.workflow.data.WfItemHistory;
import com.tcdng.jacklyn.workflow.data.WfItemStatusInfo;
import com.tcdng.jacklyn.workflow.data.WfItemSummary;
import com.tcdng.jacklyn.workflow.data.WfManualInitDef;
import com.tcdng.jacklyn.workflow.data.WfPolicyDef;
import com.tcdng.jacklyn.workflow.data.WfProcessDef;
import com.tcdng.jacklyn.workflow.data.WfProcessWorkloadInfo;
import com.tcdng.jacklyn.workflow.data.WfRecordActionDef;
import com.tcdng.jacklyn.workflow.data.WfRoutingDef;
import com.tcdng.jacklyn.workflow.data.WfStepDef;
import com.tcdng.jacklyn.workflow.data.WfTemplateDef;
import com.tcdng.jacklyn.workflow.data.WfTemplateDocDef;
import com.tcdng.jacklyn.workflow.data.WfTemplateLargeData;
import com.tcdng.jacklyn.workflow.data.WfUserActionDef;
import com.tcdng.jacklyn.workflow.entities.WfAlert;
import com.tcdng.jacklyn.workflow.entities.WfAttachmentCheck;
import com.tcdng.jacklyn.workflow.entities.WfBranch;
import com.tcdng.jacklyn.workflow.entities.WfCategory;
import com.tcdng.jacklyn.workflow.entities.WfCategoryQuery;
import com.tcdng.jacklyn.workflow.entities.WfDoc;
import com.tcdng.jacklyn.workflow.entities.WfDocAttachment;
import com.tcdng.jacklyn.workflow.entities.WfDocBeanMapping;
import com.tcdng.jacklyn.workflow.entities.WfDocBeanMappingQuery;
import com.tcdng.jacklyn.workflow.entities.WfDocClassifier;
import com.tcdng.jacklyn.workflow.entities.WfDocClassifierFilter;
import com.tcdng.jacklyn.workflow.entities.WfDocField;
import com.tcdng.jacklyn.workflow.entities.WfDocFieldMapping;
import com.tcdng.jacklyn.workflow.entities.WfDocQuery;
import com.tcdng.jacklyn.workflow.entities.WfEnrichment;
import com.tcdng.jacklyn.workflow.entities.WfForm;
import com.tcdng.jacklyn.workflow.entities.WfFormField;
import com.tcdng.jacklyn.workflow.entities.WfFormPrivilege;
import com.tcdng.jacklyn.workflow.entities.WfFormSection;
import com.tcdng.jacklyn.workflow.entities.WfFormTab;
import com.tcdng.jacklyn.workflow.entities.WfItem;
import com.tcdng.jacklyn.workflow.entities.WfItemAttachment;
import com.tcdng.jacklyn.workflow.entities.WfItemAttachmentQuery;
import com.tcdng.jacklyn.workflow.entities.WfItemEvent;
import com.tcdng.jacklyn.workflow.entities.WfItemEventQuery;
import com.tcdng.jacklyn.workflow.entities.WfItemHist;
import com.tcdng.jacklyn.workflow.entities.WfItemPackedDoc;
import com.tcdng.jacklyn.workflow.entities.WfItemPackedDocQuery;
import com.tcdng.jacklyn.workflow.entities.WfItemQuery;
import com.tcdng.jacklyn.workflow.entities.WfItemSplitEvent;
import com.tcdng.jacklyn.workflow.entities.WfMergeField;
import com.tcdng.jacklyn.workflow.entities.WfMessage;
import com.tcdng.jacklyn.workflow.entities.WfMessageQuery;
import com.tcdng.jacklyn.workflow.entities.WfPolicy;
import com.tcdng.jacklyn.workflow.entities.WfRecordAction;
import com.tcdng.jacklyn.workflow.entities.WfRouting;
import com.tcdng.jacklyn.workflow.entities.WfStep;
import com.tcdng.jacklyn.workflow.entities.WfStepQuery;
import com.tcdng.jacklyn.workflow.entities.WfTemplate;
import com.tcdng.jacklyn.workflow.entities.WfTemplateDoc;
import com.tcdng.jacklyn.workflow.entities.WfTemplateDocQuery;
import com.tcdng.jacklyn.workflow.entities.WfTemplateQuery;
import com.tcdng.jacklyn.workflow.entities.WfUserAction;
import com.tcdng.jacklyn.workflow.web.widgets.WfDocUplGenerator;
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Periodic;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.annotation.Taskable;
import com.tcdng.unify.core.annotation.TransactionAttribute;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.business.GenericService;
import com.tcdng.unify.core.constant.ColorScheme;
import com.tcdng.unify.core.constant.FrequencyUnit;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.criterion.Aggregate;
import com.tcdng.unify.core.criterion.AggregateType;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.BeanMappingConfig;
import com.tcdng.unify.core.data.Document;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.GroupAggregation;
import com.tcdng.unify.core.data.PackableDoc;
import com.tcdng.unify.core.data.PackableDocConfig;
import com.tcdng.unify.core.data.PackableDocConfig.Builder;
import com.tcdng.unify.core.system.SequenceNumberService;
import com.tcdng.unify.core.task.TaskExecLimit;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.StringUtils.StringToken;
import com.tcdng.unify.web.ui.widget.data.BadgeInfo;

/**
 * Default workflow business service implementation.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(WorkflowModuleNameConstants.WORKFLOWSERVICE)
public class WorkflowServiceImpl extends AbstractJacklynBusinessService implements WorkflowService {

    private static final String DEFAULT_WF_SYS_NOTIFICATION_CHANNEL = "defWfSysNotifChannel";

    private static final String DEFAULT_WFDOCGENERATOR = "wfsingleformviewer-generator";

    private static final String WORKFLOW_SUBMISSION_ID_SEQUENCE = "wfsubmissionid-sequence";

    private static final String CRITICAL_EXPIRED_NOTIFICATION_LOCK = "wf::criticalexpirednotiflock";

    private static final int CRITICAL_EXPIRED_NOTIFICATION_LIMIT = 100; // TODO Should be system parameter

    private final BadgeInfo PENDING_BADGEINFO =
            new BadgeInfo(ColorScheme.GREEN, "$m{workflow.workitem.status.pending}");

    private final BadgeInfo CRITICAL_BADGEINFO =
            new BadgeInfo(ColorScheme.YELLOW, "$m{workflow.workitem.status.critical}");

    private final BadgeInfo OVERDUE_BADGEINFO = new BadgeInfo(ColorScheme.RED, "$m{workflow.workitem.status.overdue}");

    @Configurable
    private SystemService systemService;

    @Configurable
    private NotificationService notificationService;

    @Configurable
    private GenericService genericService;

    @Configurable
    private SequenceNumberService sequenceNumberService;

    @Configurable
    private WfTransitionQueueManager wfTransitionQueueManager;

    @Configurable
    private WfItemAlertPolicy wfItemAlertLogic;

    @Configurable
    private WfStepUserInformationProvider wfStepUserInformationProvider;

    private FactoryMap<String, WfDocDef> wfDocs;

    private FactoryMap<String, WfTemplateDef> wfTemplates;

    private FactoryMap<String, WfStepDef> wfSteps;

    private FactoryMap<String, WfProcessDef> wfProcesses;

    public WorkflowServiceImpl() {
        wfDocs = new FactoryMap<String, WfDocDef>(true) {

            @Override
            protected boolean stale(String globalName, WfDocDef wfDocDef) throws Exception {
                boolean stale = false;
                try {
                    DocNameParts docNames = WfNameUtils.getDocNameParts(globalName);
                    Date updateDt =
                            db().value(Date.class, "wfCategoryUpdateDt",
                                    new WfDocQuery().wfCategoryName(docNames.getCategoryName())
                                            .name(docNames.getDocName()).wfCategoryStatus(RecordStatus.ACTIVE));
                    stale = resolveUTC(updateDt) != wfDocDef.getVersionTimestamp();
                } catch (Exception e) {
                    logError(e);
                }

                return stale;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected WfDocDef create(String docGlobalName, Object... params) throws Exception {
                DocNameParts docNames = WfNameUtils.getDocNameParts(docGlobalName);
                WfDoc wfDoc =
                        db().list(new WfDocQuery().wfCategoryName(docNames.getCategoryName())
                                .name(docNames.getDocName()).wfCategoryStatus(RecordStatus.ACTIVE));
                if (wfDoc == null) {
                    throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_DOCUMENT_WITH_NAME_UNKNOWN,
                            docGlobalName);
                }

                // Construct packable document configuration
                Long wfDocId = wfDoc.getId();
                PackableDocConfig.Builder pdcb = PackableDocConfig.newBuilder(docGlobalName);
                Map<String, ComplexFieldConfigBuilderInfo> complexFieldConfigBuilders =
                        new HashMap<String, ComplexFieldConfigBuilderInfo>();

                // Fields
                int size = wfDoc.getFieldList().size();
                for (int i = 0; i < size; i++) {
                    WfDocField wfDocField = wfDoc.getFieldList().get(i);
                    if (wfDocField.getDataType() == null) { // Complex
                        String complexFieldName = wfDocField.getName();
                        PackableDocConfig.Builder cfpdcb =
                                PackableDocConfig.newBuilder(StringUtils.dotify(docGlobalName, complexFieldName));
                        for (i++; i < size; i++) {
                            WfDocField subWfDocField = wfDoc.getFieldList().get(i);
                            if (subWfDocField.getParentName() == null) {
                                i--;
                                break;
                            }

                            cfpdcb.addFieldConfig(subWfDocField.getName(), subWfDocField.getDataType(),
                                    subWfDocField.getList());
                        }

                        complexFieldConfigBuilders.put(complexFieldName,
                                new ComplexFieldConfigBuilderInfo(cfpdcb, wfDocField.getList()));
                    } else {
                        pdcb.addFieldConfig(wfDocField.getName(), wfDocField.getDataType(), wfDocField.getList());
                    }
                }

                // Bean mappings
                Map<String, Class<? extends Document>> beanClassByMapping =
                        new HashMap<String, Class<? extends Document>>();
                for (WfDocBeanMapping wfDocBeanMapping : wfDoc.getBeanMappingList()) {
                    Class<? extends Document> beanClass =
                            (Class<? extends Document>) ReflectUtils.classForName(wfDocBeanMapping.getBeanType());
                    BeanMappingConfig.Builder bcmb = BeanMappingConfig.newBuilder(beanClass);
                    for (WfDocFieldMapping wfDocFieldMapping : wfDocBeanMapping.getFieldMappingList()) {
                        bcmb.addMapping(wfDocFieldMapping.getDocFieldName(), wfDocFieldMapping.getBeanFieldName());
                    }

                    PackableDocConfig.Builder targetcb = null;
                    if (StringUtils.isNotBlank(wfDocBeanMapping.getComplexFieldName())) {
                        targetcb = complexFieldConfigBuilders.get(wfDocBeanMapping.getComplexFieldName()).getCpdcb();
                    } else {
                        targetcb = pdcb;
                        beanClassByMapping.put(wfDocBeanMapping.getName(), beanClass);
                    }

                    targetcb.addBeanConfig(bcmb.build());
                }

                // End construct packable document configuration
                for (Map.Entry<String, ComplexFieldConfigBuilderInfo> entry : complexFieldConfigBuilders.entrySet()) {
                    ComplexFieldConfigBuilderInfo ccb = entry.getValue();
                    pdcb.addComplexFieldConfig(entry.getKey(), ccb.getCpdcb().build(), ccb.isList());
                }

                // Attachments
                List<WfDocAttachmentDef> attachmentList = null;
                if (DataUtils.isNotBlank(wfDoc.getAttachmentList())) {
                    attachmentList = new ArrayList<WfDocAttachmentDef>();
                    for (WfDocAttachment wfDocAttachment : wfDoc.getAttachmentList()) {
                        String label = wfDocAttachment.getLabel();
                        if (StringUtils.isBlank(label)) {
                            label = wfDocAttachment.getDescription();
                        }
                        attachmentList.add(new WfDocAttachmentDef(wfDocAttachment.getName(),
                                wfDocAttachment.getDescription(), label, wfDocAttachment.getAttachmentType()));
                    }
                }

                // Classifiers
                List<WfDocClassifierDef> classifierList = null;
                if (DataUtils.isNotBlank(wfDoc.getClassifierList())) {
                    classifierList = new ArrayList<WfDocClassifierDef>();
                    for (WfDocClassifier wfDocClassifier : wfDoc.getClassifierList()) {
                        List<WfDocClassifierFilterDef> filterList = null;
                        if (DataUtils.isNotBlank(wfDocClassifier.getFilterList())) {
                            filterList = new ArrayList<WfDocClassifierFilterDef>();
                            for (WfDocClassifierFilter wfDocClassifierFilter : wfDocClassifier.getFilterList()) {
                                filterList.add(new WfDocClassifierFilterDef(wfDocClassifierFilter.getOp(),
                                        wfDocClassifierFilter.getFieldName(), wfDocClassifierFilter.getValue1(),
                                        wfDocClassifierFilter.getValue2(), wfDocClassifierFilter.getFieldOnly()));
                            }
                        }

                        String logic = wfDocClassifier.getLogic();
                        if (StringUtils.isBlank(logic)) {
                            logic = WorkflowModuleNameConstants.DEFAULTWORKFLOWITEMCLASSIFIERLOGIC;
                        }
                        classifierList.add(new WfDocClassifierDef(wfDocClassifier.getName(),
                                wfDocClassifier.getDescription(), logic, wfDocClassifier.getFilterLogic(), filterList));
                    }
                }

                // Form
                WfForm wfForm = wfDoc.getWfForm();
                WfFormDef wfFormDef = null;
                if (wfForm != null) {
                    Long wfFormId = wfForm.getId();
                    // Fields
                    Map<String, List<WfFormFieldDef>> fieldsBySection = new HashMap<String, List<WfFormFieldDef>>();
                    for (WfFormField wfFormField : wfForm.getFieldList()) {
                        List<WfFormFieldDef> fieldDefList = fieldsBySection.get(wfFormField.getSectionName());
                        if (fieldDefList == null) {
                            fieldDefList = new ArrayList<WfFormFieldDef>();
                            fieldsBySection.put(wfFormField.getSectionName(), fieldDefList);
                        }

                        fieldDefList.add(new WfFormFieldDef(wfFormField.getBinding(), wfFormField.getLabel(),
                                wfFormField.getEditorUpl(), wfFormField.getRequired()));
                    }

                    // Sections
                    Map<String, List<WfFormSectionDef>> sectionsByTab = new HashMap<String, List<WfFormSectionDef>>();
                    for (WfFormSection wfDocSection : wfForm.getSectionList()) {
                        List<WfFormSectionDef> sectionDefList = sectionsByTab.get(wfDocSection.getTabName());
                        if (sectionDefList == null) {
                            sectionDefList = new ArrayList<WfFormSectionDef>();
                            sectionsByTab.put(wfDocSection.getTabName(), sectionDefList);
                        }

                        sectionDefList.add(new WfFormSectionDef(wfDocSection.getName(), wfDocSection.getDescription(),
                                wfDocSection.getLabel(), wfDocSection.getBinding(),
                                fieldsBySection.get(wfDocSection.getName())));
                    }

                    // Tabs
                    List<WfFormTabDef> tabList = new ArrayList<WfFormTabDef>();
                    for (WfFormTab wfFormTab : wfForm.getTabList()) {
                        tabList.add(new WfFormTabDef(wfFormTab.getName(), wfFormTab.getDescription(),
                                wfFormTab.getLabel(), sectionsByTab.get(wfFormTab.getName()), wfFormTab.getPseudo()));
                    }

                    wfFormDef = new WfFormDef(wfFormId, tabList);
                }

                List<StringToken> itemDescFormat = StringUtils.breakdownParameterizedString(wfDoc.getItemDescFormat());
                return new WfDocDef(wfDocId, docNames.getCategoryName(), docGlobalName, wfDoc.getName(),
                        wfDoc.getDescription(), pdcb.build(), resolveUTC(wfDoc.getWfCategoryUpdateDt()), wfFormDef,
                        itemDescFormat, beanClassByMapping, attachmentList, classifierList);
            }

        };

        wfTemplates = new FactoryMap<String, WfTemplateDef>(true) {

            @Override
            protected boolean stale(String globalName, WfTemplateDef wfTemplateDef) throws Exception {
                return checkWfTemplateStale(globalName, wfTemplateDef.getVersionTimestamp());
            }

            @Override
            protected WfTemplateDef create(String templateGlobalName, Object... params) throws Exception {
                TemplateNameParts templateNames = WfNameUtils.getTemplateNameParts(templateGlobalName);
                WfTemplate wfTemplate =
                        db().list(new WfTemplateQuery().wfCategoryName(templateNames.getCategoryName())
                                .name(templateNames.getTemplateName()).wfCategoryStatus(RecordStatus.ACTIVE));
                if (wfTemplate == null) {
                    throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_TEMPLATE_WITH_NAME_UNKNOWN,
                            templateGlobalName);
                }

                String templateGlobalLockName = "wflock::" + templateGlobalName;
                Long wfTemplateId = wfTemplate.getId();
                long templateTimestamp = resolveUTC(wfTemplate.getWfCategoryUpdateDt());

                // Workflow documents
                Map<String, WfTemplateDocDef> wfTemplateDocDefs = new HashMap<String, WfTemplateDocDef>();
                for (WfTemplateDoc wfTemplateDoc : wfTemplate.getTemplateDocList()) {
                    String docName = wfTemplateDoc.getWfDocName();
                    String docGlobalName = WfNameUtils.getDocGlobalName(wfTemplate.getWfCategoryName(), docName);
                    String viewerGenerator = wfTemplateDoc.getWfDocViewer();
                    if (StringUtils.isBlank(viewerGenerator)) {
                        viewerGenerator = DEFAULT_WFDOCGENERATOR;
                    }

                    WfDocUplGenerator wfDocUplGenerator = (WfDocUplGenerator) getComponent(viewerGenerator);
                    wfTemplateDocDefs.put(docName, new WfTemplateDocDef(wfDocs.get(docGlobalName), wfDocUplGenerator,
                            wfTemplateDoc.getWorkAssigner(), wfTemplateDoc.getManual()));
                }

                // Steps
                List<WfStepDef> stepList = new ArrayList<WfStepDef>();
                for (WfStep wfStep : wfTemplate.getStepList()) {
                    String stepGlobalName =
                            WfNameUtils.getStepGlobalName(templateNames.getCategoryName(),
                                    templateNames.getTemplateName(), wfStep.getName());

                    // Branch information
                    List<WfBranchDef> branchList = null;
                    if (DataUtils.isNotBlank(wfStep.getBranchList())) {
                        branchList = new ArrayList<WfBranchDef>();
                        for (WfBranch wfBranch : wfStep.getBranchList()) {
                            Set<String> mergeFieldNames = null;
                            if (!DataUtils.isBlank(wfBranch.getMergeFieldList())) {
                                mergeFieldNames = new HashSet<String>();
                                for (WfMergeField wfMergeField : wfBranch.getMergeFieldList()) {
                                    mergeFieldNames.add(wfMergeField.getFieldName());
                                }
                            }

                            branchList.add(new WfBranchDef(wfBranch.getName(), wfBranch.getDescription(),
                                    wfBranch.getTarget(), mergeFieldNames));
                        }
                    }

                    // Enrichment information
                    List<WfEnrichmentDef> enrichmentList = null;
                    if (DataUtils.isNotBlank(wfStep.getEnrichmentList())) {
                        enrichmentList = new ArrayList<WfEnrichmentDef>();
                        for (WfEnrichment wfEnrichment : wfStep.getEnrichmentList()) {
                            enrichmentList.add(new WfEnrichmentDef(wfEnrichment.getName(),
                                    wfEnrichment.getDescription(), wfEnrichment.getDocName(), wfEnrichment.getLogic()));
                        }
                    }

                    // Routing information
                    List<WfRoutingDef> routingList = null;
                    if (DataUtils.isNotBlank(wfStep.getRoutingList())) {
                        routingList = new ArrayList<WfRoutingDef>();
                        for (WfRouting wfRouting : wfStep.getRoutingList()) {
                            WfDocClassifierDef wfDocClassifierDef = null;
                            if (wfRouting.getClassifierName() != null) {
                                wfDocClassifierDef =
                                        wfTemplateDocDefs.get(wfRouting.getDocName()).getWfDocDef()
                                                .getWfDocClassifierDef(wfRouting.getClassifierName());
                            }

                            routingList.add(new WfRoutingDef(wfRouting.getName(), wfRouting.getDescription(),
                                    wfRouting.getDocName(),
                                    WfNameUtils.getStepGlobalName(templateNames.getCategoryName(),
                                            templateNames.getTemplateName(), wfRouting.getTargetWfStepName()),
                                    wfDocClassifierDef));
                        }
                    }

                    // Entity action information
                    List<WfRecordActionDef> recordActionList = null;
                    if (DataUtils.isNotBlank(wfStep.getRecordActionList())) {
                        recordActionList = new ArrayList<WfRecordActionDef>();
                        for (WfRecordAction wfRecordAction : wfStep.getRecordActionList()) {
                            recordActionList.add(new WfRecordActionDef(wfRecordAction.getName(),
                                    wfRecordAction.getDescription(), wfRecordAction.getDocName(),
                                    wfRecordAction.getActionType(), wfTemplateDocDefs.get(wfRecordAction.getDocName())
                                            .getWfDocDef().getMappingBeanClass(wfRecordAction.getDocMappingName())));
                        }
                    }

                    // User action information
                    List<WfUserActionDef> userActionList = null;
                    if (DataUtils.isNotBlank(wfStep.getUserActionList())) {
                        userActionList = new ArrayList<WfUserActionDef>();
                        for (WfUserAction wfUserAction : wfStep.getUserActionList()) {
                            List<WfAttachmentCheckDef> attachmentCheckList = null;
                            if (DataUtils.isNotBlank(wfUserAction.getAttachmentCheckList())) {
                                attachmentCheckList = new ArrayList<WfAttachmentCheckDef>();
                                for (WfAttachmentCheck wfAttachmentCheck : wfUserAction.getAttachmentCheckList()) {
                                    String docName = wfAttachmentCheck.getDocName();
                                    attachmentCheckList.add(
                                            new WfAttachmentCheckDef(docName, wfAttachmentCheck.getRequirementType(),
                                                    wfTemplateDocDefs.get(docName).getWfDocDef().getWfDocAttachmentDef(
                                                            wfAttachmentCheck.getWfDocAttachmentName())));
                                }
                            }

                            userActionList.add(new WfUserActionDef(wfUserAction.getName(),
                                    wfUserAction.getDescription(), wfUserAction.getLabel(),
                                    wfUserAction.getCommentReqType(),
                                    WfNameUtils.getStepGlobalName(templateNames.getCategoryName(),
                                            templateNames.getTemplateName(), wfUserAction.getTargetWfStepName()),
                                    wfUserAction.getValidatePage(), attachmentCheckList));
                        }
                    }

                    // Form privilege information
                    List<WfFormPrivilegeDef> formPrivilegeList = null;
                    if (DataUtils.isNotBlank(wfStep.getFormPrivilegeList())) {
                        formPrivilegeList = new ArrayList<WfFormPrivilegeDef>();
                        for (WfFormPrivilege wfFormPrivilege : wfStep.getFormPrivilegeList()) {
                            formPrivilegeList.add(new WfFormPrivilegeDef(wfFormPrivilege.getType(),
                                    wfFormPrivilege.getDocName(), wfFormPrivilege.getWfFormElementName(),
                                    wfFormPrivilege.getVisible(), wfFormPrivilege.getEditable(),
                                    wfFormPrivilege.getDisabled(), wfFormPrivilege.getRequired()));
                        }
                    }

                    // Alert information
                    List<WfAlertDef> alertList = null;
                    if (DataUtils.isNotBlank(wfStep.getAlertList())) {
                        alertList = new ArrayList<WfAlertDef>();
                        for (WfAlert wfAlert : wfStep.getAlertList()) {
                            String notifTemplateGlobalName =
                                    NotificationUtils.getTemplateGlobalName(WorkflowModuleNameConstants.WORKFLOW_MODULE,
                                            WfNameUtils.getMessageGlobalName(templateNames.getCategoryName(),
                                                    wfAlert.getNotificationTemplateCode()));
                            alertList.add(new WfAlertDef(wfAlert.getDocName(), stepGlobalName,
                                    wfAlert.getFireOnPrevStepName(), wfAlert.getName(), wfAlert.getDescription(),
                                    wfAlert.getType(), wfAlert.getParticipant(), wfAlert.getChannel(),
                                    notifTemplateGlobalName, wfAlert.getFilterName()));
                        }
                    }

                    // Policy information
                    List<WfPolicyDef> policyList = null;
                    if (DataUtils.isNotBlank(wfStep.getPolicyList())) {
                        policyList = new ArrayList<WfPolicyDef>();
                        for (WfPolicy wfPolicy : wfStep.getPolicyList()) {
                            policyList.add(new WfPolicyDef(wfPolicy.getName(), wfPolicy.getDescription(),
                                    wfPolicy.getDocName(), wfPolicy.getLogic()));
                        }
                    }

                    // Step
                    String originGlobalName = null;
                    if (!StringUtils.isBlank(wfStep.getOrigin())) {
                        originGlobalName =
                                WfNameUtils.getStepGlobalName(templateNames.getCategoryName(),
                                        templateNames.getTemplateName(), wfStep.getOrigin());
                    }

                    long criticalMilliSec = 0;
                    if (wfStep.getCriticalMinutes() != null) {
                        criticalMilliSec =
                                CalendarUtils.getMilliSecondsByFrequency(FrequencyUnit.MINUTE,
                                        wfStep.getCriticalMinutes());
                    }

                    long expiryMilliSec =
                            CalendarUtils.getMilliSecondsByFrequency(FrequencyUnit.MINUTE, wfStep.getExpiryMinutes());
                    stepList.add(new WfStepDef(wfTemplateId, templateGlobalName, templateGlobalLockName, stepGlobalName,
                            originGlobalName, wfStep.getName(), wfStep.getDescription(), wfStep.getLabel(),
                            wfStep.getWorkAssigner(), wfStep.getPriorityLevelDesc(), wfStep.getStepType(),
                            wfStep.getParticipantType(), branchList, enrichmentList, routingList, recordActionList,
                            userActionList, formPrivilegeList, alertList, policyList, wfStep.getItemsPerSession(),
                            criticalMilliSec, expiryMilliSec, wfStep.getAudit(), wfStep.getBranchOnly(),
                            wfStep.getDepartmentOnly(), wfStep.getIncludeForwarder(), templateTimestamp));
                }

                return new WfTemplateDef(wfTemplateId, templateNames.getCategoryName(), templateGlobalName,
                        wfTemplate.getName(), wfTemplate.getDescription(), templateTimestamp, wfTemplateDocDefs,
                        stepList);
            }
        };

        wfSteps = new FactoryMap<String, WfStepDef>(true) {

            @Override
            protected boolean stale(String stepGlobalName, WfStepDef wfStepDef) throws Exception {
                StepNameParts stepNameParts = WfNameUtils.getStepNameParts(stepGlobalName);
                return checkWfTemplateStale(stepNameParts.getTemplateGlobalName(), wfStepDef.getVersionTimestamp());
            }

            @Override
            protected WfStepDef create(String stepGlobalName, Object... params) throws Exception {
                StepNameParts stepNameParts = WfNameUtils.getStepNameParts(stepGlobalName);
                return wfTemplates.get(stepNameParts.getTemplateGlobalName()).getWfStepDef(stepNameParts.getStepName());
            }

        };

        wfProcesses = new FactoryMap<String, WfProcessDef>(true) {

            @Override
            protected boolean stale(String processGlobalName, WfProcessDef wfProcessDef) throws Exception {
                return checkWfTemplateStale(wfProcessDef.getTemplateGlobalName(), wfProcessDef.getVersionTimestamp());
            }

            @Override
            protected WfProcessDef create(String processGlobalName, Object... params) throws Exception {
                ProcessNameParts processNameParts = WfNameUtils.getProcessNameParts(processGlobalName);
                WfTemplateDef wfTemplateDef = wfTemplates.get(processNameParts.getTemplateGlobalName());
                return new WfProcessDef(processGlobalName, wfTemplates.get(processNameParts.getTemplateGlobalName()),
                        wfTemplateDef.getWfTemplateDocDef(processNameParts.getDocName()));
            }

        };
    }

    @Override
    public TaskMonitor startWorkflowCategoryPublication(byte[] wfCategoryConfigBin, boolean activate)
            throws UnifyException {
        TaskSetup taskSetup =
                TaskSetup.newBuilder().addTask(WorkflowCategoryBinaryPublicationTaskConstants.TASK_NAME)
                        .setParam(WorkflowCategoryBinaryPublicationTaskConstants.WFCATEGORY_BIN, wfCategoryConfigBin)
                        .setParam(WorkflowCategoryBinaryPublicationTaskConstants.WFCATEGORY_ACTIVATE, activate).build();
        return launchTask(taskSetup);
    }

    @Override
    public TaskMonitor startWorkflowCategoryPublication(WfCategoryConfig wfCategoryConfig, boolean activate)
            throws UnifyException {
        TaskSetup taskSetup =
                TaskSetup.newBuilder().addTask(WorkflowCategoryPublicationTaskConstants.TASK_NAME)
                        .setParam(WorkflowCategoryPublicationTaskConstants.WFCATEGORY_CONFIG, wfCategoryConfig)
                        .setParam(WorkflowCategoryPublicationTaskConstants.WFCATEGORY_ACTIVATE, activate).build();
        return launchTask(taskSetup);
    }

    @Override
    public WfCategory findWfCategory(Long wfCategoryId) throws UnifyException {
        return db().find(WfCategory.class, wfCategoryId);
    }

    @Override
    public List<WfCategory> findWfCategories(WfCategoryQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateWfCategory(WfCategory wfCategory) throws UnifyException {
        return db().updateByIdVersion(wfCategory);
    }

    @Override
    public WfDoc findWfDoc(Long wfDocId) throws UnifyException {
        return db().list(WfDoc.class, wfDocId);
    }

    @Override
    public List<WfDoc> findWfDocs(WfDocQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public List<WfDoc> findWfDocs(Long wfCategoryId) throws UnifyException {
        return db().listAll(new WfDocQuery().wfCategoryId(wfCategoryId));
    }

    @Override
    public List<WfMessage> findWfMessages(WfMessageQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public List<WfMessage> findWfMessages(Long wfCategoryId) throws UnifyException {
        return db().listAll(new WfMessageQuery().wfCategoryId(wfCategoryId));
    }

    @Override
    public List<WfTemplate> findWfTemplates(WfTemplateQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public List<WfTemplate> findWfTemplates(Long wfCategoryId) throws UnifyException {
        return db().listAll(new WfTemplateQuery().wfCategoryId(wfCategoryId).addOrder("description"));
    }

    @Override
    public List<ManualInitInfo> findUserRoleManualInitInfos() throws UnifyException {
        WfTemplateQuery query = new WfTemplateQuery();
        if (!getUserToken().isReservedUser()) {
            Set<String> steps = getCurrentUserRoleStepCodes();
            if (steps.isEmpty()) {
                return Collections.emptyList();
            }

            Set<String> categoryNames = new HashSet<String>();
            Set<String> templateNames = new HashSet<String>();
            for (String stepGlobalName : steps) {
                StepNameParts stepNameParts = WfNameUtils.getStepNameParts(stepGlobalName);
                categoryNames.add(stepNameParts.getCategoryName());
                templateNames.add(stepNameParts.getTemplateName());
            }

            query.wfCategoryNameIn(categoryNames);
            query.nameIn(templateNames);
        }

        query.addSelect("id", "name", "description", "wfCategoryName", "wfCategoryDesc");
        query.wfCategoryStatus(RecordStatus.ACTIVE);
        query.manualOption(Boolean.TRUE);
        query.addOrder("wfCategoryDesc", "description");
        List<WfTemplate> templateList = db().listAll(query);

        if (!templateList.isEmpty()) {
            List<ManualInitInfo> resultList = new ArrayList<ManualInitInfo>();
            for (WfTemplate wfTemplate : templateList) {
                for (WfTemplateDoc wfTemplateDoc : db()
                        .findAll(new WfTemplateDocQuery().wfTemplateId(wfTemplate.getId()).manual(Boolean.TRUE))) {
                    String processGlobalName =
                            WfNameUtils.getProcessGlobalName(wfTemplate.getWfCategoryName(), wfTemplate.getName(),
                                    wfTemplateDoc.getWfDocName());
                    WfDocDef wfDocDef =
                            wfDocs.get(WfNameUtils.getDocGlobalName(wfTemplate.getWfCategoryName(),
                                    wfTemplateDoc.getWfDocName()));
                    String processDesc = String.format("%s/%s", wfTemplate.getDescription(), wfDocDef.getDescription());
                    resultList.add(new ManualInitInfo(wfTemplate.getWfCategoryName(), wfTemplate.getWfCategoryDesc(),
                            processGlobalName, processDesc));
                }
            }

            return resultList;
        }

        return Collections.emptyList();
    }

    @Override
    public WfTemplate findWfTemplate(Long wfTemplateId) throws UnifyException {
        return db().list(WfTemplate.class, wfTemplateId);
    }

    @Override
    public WfTemplateLargeData findLargeWfTemplate(Long wfTemplateId) throws UnifyException {
        WfTemplate wfTemplate = db().list(WfTemplate.class, wfTemplateId);
        List<WfStep> stepList = wfTemplate.getStepList();
        wfTemplate.setStepList(null);

        List<WfBranch> branchList = new ArrayList<WfBranch>();
        List<WfEnrichment> enrichmentList = new ArrayList<WfEnrichment>();
        List<WfRouting> routingList = new ArrayList<WfRouting>();
        List<WfRecordAction> recordActionList = new ArrayList<WfRecordAction>();
        List<WfUserAction> userActionList = new ArrayList<WfUserAction>();
        List<WfFormPrivilege> formPrivilegeList = new ArrayList<WfFormPrivilege>();
        List<WfPolicy> policyList = new ArrayList<WfPolicy>();
        List<WfAlert> alertList = new ArrayList<WfAlert>();
        for (WfStep wfStep : stepList) {
            branchList.addAll(wfStep.getBranchList());
            wfStep.setBranchList(null);

            enrichmentList.addAll(wfStep.getEnrichmentList());
            wfStep.setEnrichmentList(null);

            routingList.addAll(wfStep.getRoutingList());
            wfStep.setRoutingList(null);

            recordActionList.addAll(wfStep.getRecordActionList());
            wfStep.setRecordActionList(null);

            userActionList.addAll(wfStep.getUserActionList());
            wfStep.setUserActionList(null);

            formPrivilegeList.addAll(wfStep.getFormPrivilegeList());
            wfStep.setFormPrivilegeList(null);

            policyList.addAll(wfStep.getPolicyList());
            wfStep.setPolicyList(null);

            alertList.addAll(wfStep.getAlertList());
            wfStep.setAlertList(null);
        }

        return new WfTemplateLargeData(wfTemplate, stepList, branchList, enrichmentList, routingList, recordActionList,
                userActionList, formPrivilegeList, policyList, alertList);
    }

    @Override
    public String autoDetectTemplate(String categoryName, Class<? extends Document> documentType)
            throws UnifyException {
        String wfDocName =
                db().value(String.class, "wfDocName",
                        new WfDocBeanMappingQuery().beanType(documentType.getName()).wfCategoryName(categoryName)
                                .wfCategoryStatus(RecordStatus.ACTIVE).primaryMapping(Boolean.TRUE));

        String wfTemplateName =
                db().value(String.class, "name",
                        new WfTemplateQuery().wfCategoryName(categoryName).wfDocName(wfDocName));
        return WfNameUtils.getTemplateGlobalName(categoryName, wfTemplateName);
    }

    @Override
    public List<WfStep> findSteps(WfStepQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public List<Long> findStepIds(Long wfTemplateId, Collection<String> stepNames) throws UnifyException {
        if (!stepNames.isEmpty()) {
            return db().valueList(Long.class, "id", new WfStepQuery().wfTemplateId(wfTemplateId).namesIn(stepNames));
        }

        return Collections.emptyList();
    }

    @Override
    public WfProcessDef getRuntimeWfProcessDef(String processGlobalName) throws UnifyException {
        return wfProcesses.get(processGlobalName);
    }

    @Override
    public WfFormDef getRuntimeWfFormDef(String docGlobalName) throws UnifyException {
        WfDocDef wfDocDef = wfDocs.get(docGlobalName);
        if (!wfDocDef.isForm()) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_DOCUMENT_NO_FORM, docGlobalName);
        }

        return wfDocDef.getWfFormDef();
    }

    @Override
    public ManualWfItem createManualInitItem(String processGlobalName) throws UnifyException {
        WfProcessDef wfProcessDef = wfProcesses.get(processGlobalName);
        WfManualInitDef manualInitDef = wfProcessDef.getWfTemplateDef().getManualInitDef(wfProcessDef.getDocName());
        UserToken userToken = getUserToken();
        return new ManualWfItem(manualInitDef.getWfStepDef(), wfProcessDef.getWfTemplateDocDef(), processGlobalName,
                userToken.getBranchCode(), userToken.getDepartmentCode(),
                new PackableDoc(wfProcessDef.getWfDocDef().getDocConfig()));
    }

    @Override
    public void pendManualInitItem(ManualWfItem manualInitItem) throws UnifyException {
        WfProcessDef wfProcessDef = wfProcesses.get(manualInitItem.getProcessGlobalName());
        UserToken userToken = getUserToken();
        submitToReceptacle(wfProcessDef, manualInitItem.getWfStepDef(), userToken.getBranchCode(),
                userToken.getDepartmentCode(), manualInitItem.getPd());
    }

    @Override
    public Long submitManualInitItem(ManualWfItem manualInitItem) throws UnifyException {
        UserToken userToken = getUserToken();
        return submitToWorkflow(manualInitItem.getProcessGlobalName(), userToken.getBranchCode(),
                userToken.getDepartmentCode(), manualInitItem.getPd());
    }

    @Override
    public Long submitToWorkflow(String processGlobalName, String branchCode, String departmentCode,
            PackableDoc packableDoc) throws UnifyException {
        WfProcessDef wfProcessDef = wfProcesses.get(processGlobalName);
        return submitToReceptacle(wfProcessDef, wfProcessDef.getWfTemplateDef().getStartStep(), branchCode,
                departmentCode, packableDoc);
    }

    @Override
    public Long submitToWorkflow(String processGlobalName, Document document) throws UnifyException {
        WfProcessDef wfProcessDef = wfProcesses.get(processGlobalName);
        WfDocDef wfDocDef = wfProcessDef.getWfDocDef();
        WfTemplateDef wfTemplateDef = wfProcessDef.getWfTemplateDef();
        WfStepDef wfStepDef = wfTemplateDef.getStartStep();

        // Create and populate packable document
        PackableDoc pDoc = new PackableDoc(wfDocDef.getDocConfig(), wfStepDef.isAudit());
        pDoc.setId(document.getId());
        pDoc.readFrom(document);

        // Submit to workflow
        return submitToReceptacle(wfProcessDef, wfStepDef, document.getBranchCode(), document.getDepartmentCode(),
                pDoc);
    }

    @Override
    public List<Long> submitToWorkflow(String processGlobalName, Document... documents) throws UnifyException {
        WfProcessDef wfProcessDef = wfProcesses.get(processGlobalName);
        WfDocDef wfDocDef = wfProcessDef.getWfDocDef();
        WfTemplateDef wfTemplateDef = wfProcessDef.getWfTemplateDef();
        WfStepDef wfStepDef = wfTemplateDef.getStartStep();

        List<Long> submissionIdList = new ArrayList<Long>();
        for (Document document : documents) {
            // Create and populate packable document
            PackableDoc pDoc = new PackableDoc(wfDocDef.getDocConfig(), wfStepDef.isAudit());
            pDoc.setId(document.getId());
            pDoc.readFrom(document);

            // Submit to workflow
            Long submissionId =
                    submitToReceptacle(wfProcessDef, wfStepDef, document.getBranchCode(), document.getDepartmentCode(),
                            pDoc);
            submissionIdList.add(submissionId);
        }

        return submissionIdList;
    }

    @Override
    public void ensureSubmissionsProcessed(Long... submissionId) throws UnifyException {
        wfTransitionQueueManager.ensureSubmissionsProcessed(submissionId);
    }

    @Override
    public String findLeastEngagedUserForWorkflowStep(String stepGlobalName, Collection<String> eligibleUsers)
            throws UnifyException {
        if (!DataUtils.isBlank(eligibleUsers)) {
            if (eligibleUsers.size() == 1) {
                return eligibleUsers.toArray(new String[1])[0];
            }

            String nominated = null;
            int least = Integer.MAX_VALUE;
            for (String userLoginId : eligibleUsers) {
                int pending = db().countAll(new WfItemQuery().stepGlobalName(stepGlobalName).heldBy(userLoginId));
                if (pending == 0) {
                    // User has no item in this step. Free to be nominated.
                    return userLoginId;
                }

                if (pending < least) {
                    nominated = userLoginId;
                    least = pending;
                }
            }

            return nominated;
        }

        return null;
    }

    @Override
    public List<Long> grabCurrentUserWorkItems(String stepGlobalName) throws UnifyException {
        WfStepDef wfStepDef = accessCurrentUserStep(stepGlobalName);
        WfItemQuery wfItemQuery = getCurrentUserParticipationWfItemQuery(wfStepDef);

        String userLoginID = getUserToken().getUserLoginId();
        if (!wfStepDef.isIncludeForwarder()) {
            wfItemQuery.notForwardedBy(userLoginID);
        }

        wfItemQuery.isUnheldOrHeldBy(userLoginID);
        if (wfStepDef.getItemsPerSession() > 0) {
            wfItemQuery.setLimit(wfStepDef.getItemsPerSession());
        }
        db().updateAll(wfItemQuery, new Update().add("heldBy", userLoginID));

        wfItemQuery.clear();
        wfItemQuery.stepGlobalName(wfStepDef.getGlobalName());
        wfItemQuery.heldBy(userLoginID);
        return db().valueList(Long.class, "id", wfItemQuery);
    }

    @Override
    public int releaseCurrentUserWorkItems(List<Long> wfItemIds) throws UnifyException {
        String userLoginId = getUserToken().getUserLoginId();
        int releaseCount = 0;
        for (Long wfItemId : wfItemIds) {
            FlowingWfItem flowingWfItem = findWorkflowItem(wfItemId);
            if (flowingWfItem.getHeldBy().equals(userLoginId)) {
                if (assignWorkflowItem(flowingWfItem, flowingWfItem.getWfStepDef(), false)) {
                    releaseCount++;
                }
            }
        }

        return releaseCount;
    }

    @Override
    public InteractWfItems getCurrentUserWorkItems(String stepGlobalName) throws UnifyException {
        WfStepDef wfStepDef = accessCurrentUserStep(stepGlobalName);
        String useLoginID = getUserToken().getUserLoginId();
        List<WfItem> wfItemList =
                db().listAll(new WfItemQuery().stepGlobalName(wfStepDef.getGlobalName()).heldBy(useLoginID));
        return new InteractWfItems(stepGlobalName, wfItemList, getWorkflowStepActions(wfStepDef));
    }

    @Override
    public List<WfAction> getWorkflowStepActions(String stepGlobalName) throws UnifyException {
        WfStepDef wfStepDef = wfSteps.get(stepGlobalName);
        return getWorkflowStepActions(wfStepDef);
    }

    @Override
    public List<WfItemSummary> getCurrentUserWorkItemSummary() throws UnifyException {
        Set<String> stepNames = getCurrentUserRoleStepCodes();
        if (!stepNames.isEmpty()) {
            List<WfItemSummary> summaryList = new ArrayList<WfItemSummary>();
            for (String stepGlobalName : stepNames) {
                WfStepDef wfStepDef = wfSteps.get(stepGlobalName);
                WfItemQuery wfItemQuery = getCurrentUserParticipationWfItemQuery(wfStepDef);
                int itemCount = db().countAll(wfItemQuery);
                if (itemCount > 0) {
                    String stepDesc = wfStepDef.getDescription();
                    WfTemplateDef wfTemplateDef = wfTemplates.get(wfStepDef.getTemplateGlobalName());
                    String description =
                            getSessionMessage("workflowitem.summary", wfTemplateDef.getDescription(), stepDesc);
                    int holdCount = db().countAll(wfItemQuery.isHeld());
                    summaryList.add(new WfItemSummary(description, stepGlobalName, stepDesc, itemCount, holdCount));
                }
            }

            if (!summaryList.isEmpty()) {
                DataUtils.sortAscending(summaryList, WfItemSummary.class, "stepDesc");
                return summaryList;
            }
        }

        return Collections.emptyList();
    }

    @Override
    public List<WfItemStatusInfo> getCurrentWorkItemStatusList(String stepGlobalName) throws UnifyException {
        WfItemQuery wfItemQuery = new WfItemQuery();
        wfItemQuery.heldBy(getUserToken().getUserLoginId());
        if (!StringUtils.isBlank(stepGlobalName)) {
            wfItemQuery.stepGlobalName(stepGlobalName);
        }

        List<WfItem> wfItemList = db().listAll(wfItemQuery);
        if (!wfItemList.isEmpty()) {
            List<WfItemStatusInfo> wfItemStatusInfoList = new ArrayList<WfItemStatusInfo>();
            for (WfItem wfItem : wfItemList) {
                WfProcessDef wfProcessDef = wfProcesses.get(wfItem.getProcessGlobalName());
                WfStepDef wfStepDef = wfSteps.get(wfItem.getStepGlobalName());
                BadgeInfo badgeInfo = getStatusBadge(wfItem);
                WfItemStatusInfo wfItemStatusInfo =
                        new WfItemStatusInfo(wfItem.getId(), wfProcessDef.getWfTemplateDef().getDescription(),
                                wfProcessDef.getWfDocDef().getDescription(), wfStepDef.getDescription(),
                                wfItem.getWfItemDesc(), wfItem.getForwardedBy(), wfStepDef.getPriorityLevelDesc(),
                                wfItem.getStepDt(), badgeInfo);
                wfItemStatusInfoList.add(wfItemStatusInfo);
            }

            return wfItemStatusInfoList;
        }

        return Collections.emptyList();
    }

    @Override
    public List<WfItemCountStatusInfo> getCurrentWorkItemCountStatusList() throws UnifyException {
        List<WfItem> wfItemList =
                db().listAll(new WfItemQuery().heldBy(getUserToken().getUserLoginId()).addSelect("criticalDt",
                        "expectedDt", "stepGlobalName"));
        int pendingCount = 0;
        int criticalCount = 0;
        int overdueCount = 0;
        if (!wfItemList.isEmpty()) {
            for (WfItem wfItem : wfItemList) {
                BadgeInfo badgeInfo = getStatusBadge(wfItem);
                if (PENDING_BADGEINFO.equals(badgeInfo)) {
                    pendingCount++;
                } else if (CRITICAL_BADGEINFO.equals(badgeInfo)) {
                    criticalCount++;
                } else {
                    overdueCount++;
                }
            }
        }

        return Arrays.asList(new WfItemCountStatusInfo(PENDING_BADGEINFO, pendingCount),
                new WfItemCountStatusInfo(CRITICAL_BADGEINFO, criticalCount),
                new WfItemCountStatusInfo(OVERDUE_BADGEINFO, overdueCount));
    }

    @Override
    public List<WfProcessWorkloadInfo> getCurrentUserProcessWorkloadList() throws UnifyException {
        List<GroupAggregation> userGroupAggregationList =
                db().aggregateGroupMany(new Aggregate().add(AggregateType.COUNT, "stepGlobalName"),
                        new WfItemQuery().heldBy(getUserToken().getUserLoginId()).addGroupBy("processGlobalName")
                                .addOrder("processGlobalName"));
        if (!DataUtils.isBlank(userGroupAggregationList)) {
            List<String> processNameList = new ArrayList<String>();
            for (GroupAggregation groupAggregation : userGroupAggregationList) {
                processNameList.add(groupAggregation.getGroupingValue(String.class, "processGlobalName"));
            }

            List<GroupAggregation> totalGroupAggregationList =
                    db().aggregateGroupMany(new Aggregate().add(AggregateType.COUNT, "stepGlobalName"),
                            new WfItemQuery().processGlobalNameIn(processNameList).addGroupBy("processGlobalName")
                                    .addOrder("processGlobalName"));

            List<WfProcessWorkloadInfo> workloadInfoList = new ArrayList<WfProcessWorkloadInfo>();
            Map<String, WfProcessWorkloadInfo> summaryMap = new LinkedHashMap<String, WfProcessWorkloadInfo>();
            int len = userGroupAggregationList.size();
            for (int i = 0; i < len; i++) {
                GroupAggregation userWorkAggregation = userGroupAggregationList.get(i);
                GroupAggregation totalWorkAggregation = totalGroupAggregationList.get(i);
                WfProcessDef wfProcessDef = wfProcesses.get(userWorkAggregation.getGroupingValue(String.class, 0));
                String templateName = wfProcessDef.getTemplateName();
                WfProcessWorkloadInfo wfProcessWorkloadInfo =
                        new WfProcessWorkloadInfo(wfProcessDef.getDocName(),
                                totalWorkAggregation.getAggregationValue(Integer.class, 0),
                                userWorkAggregation.getAggregationValue(Integer.class, 0));
                if (summaryMap.containsKey(templateName)) {
                    WfProcessWorkloadInfo existWfProcessWorkloadInfo = summaryMap.get(templateName);
                    wfProcessWorkloadInfo =
                            new WfProcessWorkloadInfo(templateName,
                                    existWfProcessWorkloadInfo.getTotalWorkload()
                                            + totalWorkAggregation.getAggregationValue(Integer.class, 0),
                                    existWfProcessWorkloadInfo.getUserWorkload()
                                            + userWorkAggregation.getAggregationValue(Integer.class, 0));
                } else {
                    wfProcessWorkloadInfo =
                            new WfProcessWorkloadInfo(templateName,
                                    totalWorkAggregation.getAggregationValue(Integer.class, 0),
                                    userWorkAggregation.getAggregationValue(Integer.class, 0));
                }

                summaryMap.put(templateName, wfProcessWorkloadInfo);
                workloadInfoList.add(wfProcessWorkloadInfo);
            }

            return workloadInfoList;
        }

        return Collections.emptyList();
    }

    @Override
    public Long applyWorkflowAction(FlowingWfItem flowingWfItem, String actionName) throws UnifyException {
        WfUserActionDef wfUserActionDef = flowingWfItem.getWfStepDef().getWfUserActionDef(actionName);
        WfStepDef trgWfStep = wfSteps.get(wfUserActionDef.getTargetGlobalName());

        // Check if current user holds workflow item
        Long wfItemId = flowingWfItem.getWfItemId();
        String userLoginId = getUserToken().getUserLoginId();
        if (db().countAll(((WfItemQuery) new WfItemQuery().id(wfItemId)).heldBy(userLoginId)) > 0) {
            // Log history
            Date actionDt = db().getNow();
            db().updateById(WfItemEvent.class, flowingWfItem.getWfHistEventId(), new Update().add("actionDt", actionDt)
                    .add("actor", userLoginId).add("wfAction", actionName).add("comment", flowingWfItem.getComment()));

            // Release workflow item
            db().updateById(WfItem.class, wfItemId, new Update().add("heldBy", null));

            // Push to transition queue
            Long submissionId = sequenceNumberService.getCachedBlockNextSequenceNumber(WORKFLOW_SUBMISSION_ID_SEQUENCE);
            wfTransitionQueueManager.pushWfItemToTransitionQueue(submissionId, trgWfStep, flowingWfItem);
            return submissionId;
        }

        throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_APPLY_ACTION_UNHELD, actionName,
                flowingWfItem.getDescription());
    }

    @Override
    public FlowingWfItem findWorkflowItem(Long wfItemId) throws UnifyException {
        WfItem wfItem = db().list(WfItem.class, wfItemId);
        return createFlowingWfItem(wfItem);
    }

    @Override
    public FlowingWfItem findWorkflowItemBySubmission(Long submissionId) throws UnifyException {
        WfItem wfItem = db().list(new WfItemQuery().submissionId(submissionId));
        if (wfItem != null) {
            return createFlowingWfItem(wfItem);
        }

        return null;
    }

    @Override
    public WfItemHistory findWorkflowItemHistory(Long wfItemHistId, boolean commentsOnly) throws UnifyException {
        WfItemHist wfHist = db().list(WfItemHist.class, wfItemHistId);
        WfItemEventQuery query = new WfItemEventQuery();
        if (commentsOnly) {
            query.commentsOnly();
        }

        query.wfItemHistId(wfItemHistId);
        query.orderById();
        List<WfItemEvent> wfHistEventList = db().listAll(query);

        ProcessNameParts processNameParts = WfNameUtils.getProcessNameParts(wfHist.getProcessGlobalName());
        WfTemplateDef wfTemplateDef = wfTemplates.get(processNameParts.getTemplateGlobalName());
        List<WfItemHistEvent> eventList = new ArrayList<WfItemHistEvent>();
        for (WfItemEvent wfHistEvent : wfHistEventList) {
            WfStepDef wfStepDef = wfTemplateDef.getWfStepDef(wfHistEvent.getWfStepName());
            String actionDesc = null;
            if (wfHistEvent.getWfAction() != null) {
                actionDesc = resolveSessionMessage(wfStepDef.getWfUserActionDef(wfHistEvent.getWfAction()).getLabel());
            }

            eventList.add(new WfItemHistEvent(wfHistEvent.getId(), wfHistEvent.getWfStepName(),
                    wfHistEvent.getPrevWfStepName(), wfHistEvent.getStepDt(), wfHistEvent.getActionDt(),
                    wfHistEvent.getActor(), wfHistEvent.getWfAction(), actionDesc, wfHistEvent.getComment(),
                    wfHistEvent.getSrcWfStepName(), wfHistEvent.getErrorCode(), wfHistEvent.getErrorMsg()));
        }

        return new WfItemHistory(wfHist.getId(), wfHist.getDocId(), processNameParts.getDocGlobalName(),
                processNameParts.getTemplateGlobalName(), wfHist.getDescription(),
                Collections.unmodifiableList(eventList));
    }

    @Override
    public void attachToWorkflowItem(Long wfItemId, WfItemAttachmentInfo attachment) throws UnifyException {
        WfDocDef wfDocDef = getWfItemWfDocDef(wfItemId);
        WfDocAttachmentDef wfDocAttachmentDef = wfDocDef.getWfDocAttachmentDef(attachment.getName());
        if (!wfDocAttachmentDef.getAttachmentType().equals(attachment.getType())) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_ATTACHMENT_INCOMPATIPLE_TYPE,
                    wfDocDef.getName(), wfDocAttachmentDef.getAttachmentType(), attachment.getType());
        }

        Long wfItemAttachmentRefId = db().value(Long.class, "wfItemAttachmentRefId", new WfItemQuery().id(wfItemId));
        WfItemAttachment wfItemAttachment =
                db().find(new WfItemAttachmentQuery().wfItemAttachmentRefId(wfItemAttachmentRefId)
                        .name(attachment.getName()).addSelect("wfItemAttachmentRefId", "name"));
        if (wfItemAttachment == null) {
            wfItemAttachment = new WfItemAttachment();
            wfItemAttachment.setWfItemAttachmentRefId(wfItemAttachmentRefId);
            wfItemAttachment.setName(attachment.getName());
            wfItemAttachment.setFileName(attachment.getFilename());
            wfItemAttachment.setData(attachment.getData());
            db().create(wfItemAttachment);
        } else {
            wfItemAttachment.setFileName(attachment.getFilename());
            wfItemAttachment.setData(attachment.getData());
            db().updateById(wfItemAttachment);
        }
    }

    @Override
    public List<WfItemAttachmentInfo> fetchWorkflowItemAttachments(Long wfItemId, boolean attributesOnly)
            throws UnifyException {
        WfDocDef wfDocDef = getWfItemWfDocDef(wfItemId);
        List<WfItemAttachmentInfo> resultList = new ArrayList<WfItemAttachmentInfo>();
        Set<String> attachmentNames = wfDocDef.getWfDocAttachmentNames();
        if (!attachmentNames.isEmpty()) {
            Long wfItemAttachmentRefId =
                    db().value(Long.class, "wfItemAttachmentRefId", new WfItemQuery().id(wfItemId));
            WfItemAttachmentQuery query = new WfItemAttachmentQuery();
            query.wfItemAttachmentRefId(wfItemAttachmentRefId);
            if (attributesOnly) {
                query.addSelect("name", "fileName");
            }

            Map<String, WfItemAttachment> map = db().listAllMap(String.class, "name", query);

            for (String name : attachmentNames) {
                WfItemAttachment wfItemAttachment = map.get(name);
                WfDocAttachmentDef wfDocAttachmentDef = wfDocDef.getWfDocAttachmentDef(name);
                if (wfItemAttachment == null) {
                    // Blank slot
                    resultList.add(new WfItemAttachmentInfo(wfDocAttachmentDef.getName(), wfDocAttachmentDef.getLabel(),
                            null, wfDocAttachmentDef.getAttachmentType()));
                } else {
                    // Filled slot
                    resultList.add(new WfItemAttachmentInfo(wfItemAttachment.getName(), wfDocAttachmentDef.getLabel(),
                            wfItemAttachment.getFileName(), wfDocAttachmentDef.getAttachmentType(),
                            wfItemAttachment.getData()));
                }
            }
        }

        return resultList;
    }

    @Override
    public WfItemAttachmentInfo fetchWorkflowItemAttachment(Long wfItemId, String name) throws UnifyException {
        WfDocDef wfDocDef = getWfItemWfDocDef(wfItemId);
        WfDocAttachmentDef wfDocAttachmentDef = wfDocDef.getWfDocAttachmentDef(name);

        WfItemAttachmentInfo result = null;
        Long wfItemAttachmentRefId = db().value(Long.class, "wfItemAttachmentRefId", new WfItemQuery().id(wfItemId));
        WfItemAttachment wfItemAttachment =
                db().find(new WfItemAttachmentQuery().wfItemAttachmentRefId(wfItemAttachmentRefId).name(name));
        if (wfItemAttachment == null) {
            result =
                    new WfItemAttachmentInfo(wfDocAttachmentDef.getName(), wfDocAttachmentDef.getLabel(), null,
                            wfDocAttachmentDef.getAttachmentType());
        } else {
            result =
                    new WfItemAttachmentInfo(wfItemAttachment.getName(), wfDocAttachmentDef.getLabel(),
                            wfItemAttachment.getFileName(), wfDocAttachmentDef.getAttachmentType(),
                            wfItemAttachment.getData());
        }

        return result;
    }

    @Override
    public int deleteWorkflowItemAttachment(Long wfItemId, String name) throws UnifyException {
        Long wfItemAttachmentRefId = db().value(Long.class, "wfItemAttachmentRefId", new WfItemQuery().id(wfItemId));
        return db().deleteAll(new WfItemAttachmentQuery().wfItemAttachmentRefId(wfItemAttachmentRefId).name(name));
    }

    @Override
    public List<ToolingItemClassifierLogicItem> findToolingItemClassifierLogicTypes() throws UnifyException {
        return getToolingTypes(ToolingItemClassifierLogicItem.class, WfItemClassifierLogic.class);
    }

    @Override
    public List<ToolingEnrichmentLogicItem> findToolingEnrichmentLogicTypes() throws UnifyException {
        return getToolingTypes(ToolingEnrichmentLogicItem.class, WfItemEnrichmentPolicy.class);
    }

    @Override
    public List<ToolingPolicyLogicItem> findToolingPolicyLogicTypes() throws UnifyException {
        return getToolingTypes(ToolingPolicyLogicItem.class, WfItemProcessPolicy.class);
    }

    @Override
    public List<ToolingWfDocUplGeneratorItem> findToolingWfDocUplGeneratorTypes() throws UnifyException {
        return getToolingTypes(ToolingWfDocUplGeneratorItem.class, WfDocUplGenerator.class);
    }

    @Override
    public List<ToolingWfItemAssignerItem> findToolingWfItemAssignerTypes() throws UnifyException {
        return getToolingTypes(ToolingWfItemAssignerItem.class, WfItemAssignmentPolicy.class);
    }

    @Taskable(
            name = WorkflowApplyActionTaskConstants.TASK_NAME,
            description = "Apply Action to Multiple Workflow Items Task",
            parameters = { @Parameter(name = WorkflowApplyActionTaskConstants.WFITEMS_IDLIST, type = List.class),
                    @Parameter(WorkflowApplyActionTaskConstants.WFACTION_NAME),
                    @Parameter(WorkflowApplyActionTaskConstants.COMMENT) },
            limit = TaskExecLimit.ALLOW_MULTIPLE)
    public int executeApplyActionToMultipleWorkflowItems(TaskMonitor taskMonitor, List<Long> wfItemIdList,
            String actionName, String comment) throws UnifyException {
        for (Long wfItemId : wfItemIdList) {
            FlowingWfItem flowingWfItem = findWorkflowItem(wfItemId);
            addTaskMessage(taskMonitor, "$m{workflow.taskmonitor.itemgrabbed}", flowingWfItem.getDescription());
            flowingWfItem.setComment(comment);
            applyWorkflowAction(flowingWfItem, actionName);
            addTaskMessage(taskMonitor, "$m{workflow.taskmonitor.actionapplied}");
        }
        return wfItemIdList.size();
    }

    @Synchronized(CRITICAL_EXPIRED_NOTIFICATION_LOCK)
    @Periodic(PeriodicType.SLOWEST)
    public void sendCriticalExpiredNotifications(TaskMonitor taskMonitor) throws UnifyException {
        logDebug("Sending notifications for critical and expired workflow items...");
        Date now = getNow();
        // Critical
        List<WfItem> criticalWfItemList =
                db().listAll(new WfItemQuery().criticalAlertNotSent().expiredAlertNotSent().isCritical(now).orderById()
                        .setLimit(CRITICAL_EXPIRED_NOTIFICATION_LIMIT));
        if (!DataUtils.isBlank(criticalWfItemList)) {
            List<Long> wfItemIdList = new ArrayList<Long>();
            for (WfItem wfItem : criticalWfItemList) {
                FlowingWfItem flowingWfItem = createFlowingWfItem(wfItem);
                for (WfAlertDef wfAlertDef : flowingWfItem.getWfStepDef().getAlertList()) {
                    if (wfAlertDef.isCriticalNotification()) {
                        wfItemAlertLogic.sendAlert(flowingWfItem.getReader(), wfAlertDef);
                    }
                }
                wfItemIdList.add(wfItem.getId());
            }

            db().updateAll(new WfItemQuery().idIn(wfItemIdList), new Update().add("criticalAlertSent", Boolean.TRUE));
        }

        // Expired
        List<WfItem> expiredWfItemList =
                db().listAll(new WfItemQuery().expiredAlertNotSent().isExpired(now).orderById()
                        .setLimit(CRITICAL_EXPIRED_NOTIFICATION_LIMIT));
        if (!DataUtils.isBlank(expiredWfItemList)) {
            List<Long> wfItemIdList = new ArrayList<Long>();
            for (WfItem wfItem : expiredWfItemList) {
                FlowingWfItem flowingWfItem = createFlowingWfItem(wfItem);
                for (WfAlertDef wfAlertDef : flowingWfItem.getWfStepDef().getAlertList()) {
                    if (wfAlertDef.isExpirationNotification()) {
                        wfItemAlertLogic.sendAlert(flowingWfItem.getReader(), wfAlertDef);
                    }
                }
                wfItemIdList.add(wfItem.getId());
            }

            db().updateAll(new WfItemQuery().idIn(wfItemIdList), new Update().add("expirationAlertSent", Boolean.TRUE));
        }
    }

    @Override
    public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {
        // Create workflow system notification channel if not existing
        if (notificationService.findNotificationChannel(
                new NotificationChannelQuery().name(DEFAULT_WF_SYS_NOTIFICATION_CHANNEL)) == null) {
            logDebug("Creating default workflow system notification channel...");
            NotificationChannel notificationChannel = new NotificationChannel();
            notificationChannel.setName(DEFAULT_WF_SYS_NOTIFICATION_CHANNEL);
            notificationChannel
                    .setDescription(resolveApplicationMessage("$m{workflow.default.system.notification.channel}"));
            notificationChannel.setNotificationType(NotificationType.SYSTEM);
            notificationService.createNotificationChannel(notificationChannel);
            logDebug("Default workflow system notification channel created.");
        }

        // Create default workflow dashboards
        String dashboardName =
                systemService.getSysParameterValue(String.class,
                        WorkflowModuleSysParamConstants.SYSPARAM_WORKFLOW_WORKITEMS_DASHBOARD);
        Dashboard dashboard = systemService.findDashboard(dashboardName);
        if (dashboard == null) {
            dashboard = new Dashboard(OrientationType.HORIZONTAL, dashboardName, "My Work Items Workflow Dashboard");
            List<DashboardLayer> layerList = new ArrayList<DashboardLayer>();
            layerList.add(new DashboardLayer("layer1", "Layer One", 1));
            dashboard.setLayerList(layerList);

            List<DashboardPortlet> portletList = new ArrayList<DashboardPortlet>();
            portletList
                    .add(new DashboardPortlet("myWorkItems", "My Work Items", "layer1", "ui-myworkitemsportlet", 1, 0));
            dashboard.setPortletList(portletList);
            systemService.createDashboard(dashboard);
        }

        dashboardName =
                systemService.getSysParameterValue(String.class,
                        WorkflowModuleSysParamConstants.SYSPARAM_WORKFLOW_WORKITEMS_GRAPH_DASHBOARD);
        dashboard = systemService.findDashboard(dashboardName);
        if (dashboard == null) {
            dashboard =
                    new Dashboard(OrientationType.HORIZONTAL, dashboardName,
                            "My Work Items with Graph Workflow Dashboard");
            List<DashboardLayer> layerList = new ArrayList<DashboardLayer>();
            layerList.add(new DashboardLayer("layer1", "Layer One", 1));
            dashboard.setLayerList(layerList);

            List<DashboardPortlet> portletList = new ArrayList<DashboardPortlet>();
            portletList.add(new DashboardPortlet("myWorkItemsWithGraph", "My Work Items (Graph)", "layer1",
                    "ui-myworkitemswithgraphportlet", 1, 0));
            dashboard.setPortletList(portletList);
            systemService.createDashboard(dashboard);
        }

    }

    @Override
    public void clearCache() throws UnifyException {
        wfDocs.clear();
        wfTemplates.clear();
    }

	@Override
	public void publishWorkflowCategoryFromResourceFile(TaskMonitor taskMonitor, String resourceName, boolean activate)
			throws UnifyException {
		InputStream inputStream = null;
		try {
			inputStream = IOUtils.openClassLoaderResourceInputStream(resourceName);
			WfCategoryConfig wfCategoryConfig = WfCategoryConfigUtils.readWfCategoryConfig(inputStream);
			executeWorkflowCategoryPublicationTask(taskMonitor, wfCategoryConfig, activate);
		} finally {
			IOUtils.close(inputStream);
		}
	}

    @Override
    @Taskable(
            name = WorkflowCategoryBinaryPublicationTaskConstants.TASK_NAME,
            description = "Workflow Category Binary Publication Task",
            parameters = {
                    @Parameter(
                            name = WorkflowCategoryBinaryPublicationTaskConstants.WFCATEGORY_BIN, type = byte[].class),
                    @Parameter(
                            name = WorkflowCategoryBinaryPublicationTaskConstants.WFCATEGORY_ACTIVATE,
                            type = boolean.class) },
            limit = TaskExecLimit.ALLOW_MULTIPLE)
    public boolean executeWorkflowCategoryPublicationTask(TaskMonitor taskMonitor, byte[] wfCategoryConfigBin,
            boolean activate) throws UnifyException {
        addTaskMessage(taskMonitor, "Extracting category configuration from binary...");
        WfCategoryConfig wfCategoryConfig =
                WfCategoryConfigUtils.readWfCategoryConfig(new ByteArrayInputStream(wfCategoryConfigBin));
        return executeWorkflowCategoryPublicationTask(taskMonitor, wfCategoryConfig, activate);
    }

    @Override
    @Taskable(
            name = WorkflowCategoryPublicationTaskConstants.TASK_NAME,
            description = "Workflow Category Publication Task",
            parameters = { @Parameter(
                    name = WorkflowCategoryPublicationTaskConstants.WFCATEGORY_CONFIG, type = WfCategoryConfig.class),
                    @Parameter(
                            name = WorkflowCategoryPublicationTaskConstants.WFCATEGORY_ACTIVATE,
                            type = boolean.class) },
            limit = TaskExecLimit.ALLOW_MULTIPLE)
    public boolean executeWorkflowCategoryPublicationTask(TaskMonitor taskMonitor, WfCategoryConfig wfCategoryConfig,
            boolean activate) throws UnifyException {
        addTaskMessage(taskMonitor, "Starting workflow category publication...");
        addTaskMessage(taskMonitor, "Category name: " + wfCategoryConfig.getName());
        addTaskMessage(taskMonitor, "Validating configuration...");
        List<UnifyError> errorList = WfCategoryConfigUtils.validate(taskMonitor, wfCategoryConfig);
        if (!errorList.isEmpty()) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_CATEGORY_PUBLICATION_ERROR,
                    wfCategoryConfig.getName());
        }

        addTaskMessage(taskMonitor, "Validation completed.");

        Long wfCategoryId = null;
        WfCategory oldWfCategory =
                db().find(
                        new WfCategoryQuery().name(wfCategoryConfig.getName()).version(wfCategoryConfig.getVersion()));
        String description = resolveApplicationMessage(wfCategoryConfig.getDescription());
        if (oldWfCategory == null) {
            addTaskMessage(taskMonitor, "Creating new category...");
            WfCategory wfCategory = new WfCategory();
            wfCategory.setName(wfCategoryConfig.getName());
            wfCategory.setDescription(description);
            wfCategory.setVersion(wfCategoryConfig.getVersion());
            wfCategory.setStatus(RecordStatus.INACTIVE);
            wfCategoryId = (Long) db().create(wfCategory);
        } else {
            addTaskMessage(taskMonitor, "Updating existing category...");
            oldWfCategory.setDescription(description);
            oldWfCategory.setVersion(wfCategoryConfig.getVersion());
            db().updateById(oldWfCategory);
            wfCategoryId = oldWfCategory.getId();
        }

        addTaskMessage(taskMonitor, "Publishing workflow category documents...");
        WfDocQuery wddQuery = new WfDocQuery();
        WfDoc wfDoc = new WfDoc();
        wfDoc.setWfCategoryId(wfCategoryId);
        for (WfDocumentConfig wfDocConfig : wfCategoryConfig.getWfDocumentsConfig().getWfDocumentConfigList()) {
            addTaskMessage(taskMonitor, "Document name: " + wfDocConfig.getName());
            String wfDocName = wfDocConfig.getName();
            wddQuery.clear();
            WfDoc oldWfDoc = db().find(wddQuery.wfCategoryId(wfCategoryId).name(wfDocName));
            description = resolveApplicationMessage(wfDocConfig.getDescription());
            if (oldWfDoc == null) {
                addTaskMessage(taskMonitor, "Creating new document...");
                wfDoc.setName(wfDocName);
                wfDoc.setDescription(description);
                wfDoc.setItemDescFormat(wfDocConfig.getItemDescFormat());
                populateChildList(wfDoc, wfDocConfig);
                db().create(wfDoc);
            } else {
                addTaskMessage(taskMonitor, "Updating existing document...");
                oldWfDoc.setDescription(description);
                oldWfDoc.setItemDescFormat(wfDocConfig.getItemDescFormat());
                populateChildList(oldWfDoc, wfDocConfig);
                db().updateById(oldWfDoc);
            }
        }
        addTaskMessage(taskMonitor, "Total of "
                + wfCategoryConfig.getWfDocumentsConfig().getWfDocumentConfigList().size() + " document(s) published.");

        addTaskMessage(taskMonitor, "Publishing workflow category messages...");
        if (wfCategoryConfig.getWfMessagesConfig() != null
                && DataUtils.isNotBlank(wfCategoryConfig.getWfMessagesConfig().getWfMessageConfigList())) {
            WfMessageQuery wmQuery = new WfMessageQuery();
            WfMessage wfMessage = new WfMessage();
            wfMessage.setWfCategoryId(wfCategoryId);
            for (WfMessageConfig wfMessageConfig : wfCategoryConfig.getWfMessagesConfig().getWfMessageConfigList()) {
                addTaskMessage(taskMonitor, "Message name: " + wfMessageConfig.getName());
                String wfMessageName = wfMessageConfig.getName();
                wmQuery.clear();
                WfMessage oldWfMessage = db().find(wmQuery.wfCategoryId(wfCategoryId).name(wfMessageName));
                description = resolveApplicationMessage(wfMessageConfig.getDescription());
                if (oldWfMessage == null) {
                    addTaskMessage(taskMonitor, "Creating new message...");
                    wfMessage.setName(wfMessageConfig.getName());
                    wfMessage.setDescription(description);
                    wfMessage.setWfDocName(wfMessageConfig.getDocument());
                    wfMessage.setSubject(wfMessageConfig.getSubject());
                    wfMessage.setTemplate(wfMessageConfig.getBody());
                    wfMessage.setMessageType(wfMessageConfig.getMessageType());
                    wfMessage.setActionLink(wfMessageConfig.getActionLink());
                    wfMessage.setHtmlFlag(wfMessageConfig.getHtml());
                    wfMessage.setAttachmentGenerator(wfMessageConfig.getAttachmentGenerator());
                    db().create(wfMessage);
                } else {
                    addTaskMessage(taskMonitor, "Updating existing message...");
                    oldWfMessage.setDescription(description);
                    oldWfMessage.setWfDocName(wfMessageConfig.getDocument());
                    oldWfMessage.setSubject(wfMessageConfig.getSubject());
                    oldWfMessage.setTemplate(wfMessageConfig.getBody());
                    oldWfMessage.setMessageType(wfMessageConfig.getMessageType());
                    oldWfMessage.setActionLink(wfMessageConfig.getActionLink());
                    oldWfMessage.setHtmlFlag(wfMessageConfig.getHtml());
                    oldWfMessage.setAttachmentGenerator(wfMessageConfig.getAttachmentGenerator());
                    db().updateById(oldWfMessage);
                }
            }

            addTaskMessage(taskMonitor,
                    "Total of " + wfCategoryConfig.getWfMessagesConfig().getWfMessageConfigList().size()
                            + " messages(s) published.");
        }

        addTaskMessage(taskMonitor, "Publishing workflow templates...");
        WfTemplateQuery wdQuery = new WfTemplateQuery();
        WfTemplate wfTemplate = new WfTemplate();
        wfTemplate.setWfCategoryId(wfCategoryId);
        for (WfTemplateConfig wfTemplateConfig : wfCategoryConfig.getWfTemplatesConfig().getWfTemplateConfigList()) {
            addTaskMessage(taskMonitor, "Template name: " + wfTemplateConfig.getName());
            String wfTemplateName = wfTemplateConfig.getName();
            wdQuery.clear();
            WfTemplate oldWfTemplate = db().find(wdQuery.wfCategoryId(wfCategoryId).name(wfTemplateName));
            description = resolveApplicationMessage(wfTemplateConfig.getDescription());
            if (oldWfTemplate == null) {
                addTaskMessage(taskMonitor, "Creating new template...");
                wfTemplate.setName(wfTemplateName);
                wfTemplate.setDescription(description);
                populateChildList(wfTemplate, wfTemplateConfig);
                db().create(wfTemplate);
            } else {
                addTaskMessage(taskMonitor, "Updating existing template...");
                oldWfTemplate.setDescription(description);
                populateChildList(oldWfTemplate, wfTemplateConfig);
                db().updateById(oldWfTemplate);
            }
        }
        addTaskMessage(taskMonitor, "Total of "
                + wfCategoryConfig.getWfTemplatesConfig().getWfTemplateConfigList().size() + " template(s) published.");

        if (activate) {
            addTaskMessage(taskMonitor, "Activating category with name[" + wfCategoryConfig.getName()
                    + "] and version [" + wfCategoryConfig.getVersion() + "]...");
            activateWfCategory(wfCategoryConfig.getName(), wfCategoryConfig.getVersion());
            addTaskMessage(taskMonitor, "...activation completed.");
        }
        return true;
    }

    @Override
    public void activateWfCategory(String wfCategoryName, String wfCategoryVersion) throws UnifyException {
        if (db().countAll(new WfCategoryQuery().name(wfCategoryName).version(wfCategoryVersion)) == 0) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_CATEGORY_NAME_VERSION_UNKNOWN,
                    wfCategoryName, wfCategoryVersion);
        }

        // Disable all versions
        db().updateAll(new WfCategoryQuery().name(wfCategoryName), new Update().add("status", RecordStatus.INACTIVE));

        // Update notification messages (must always do this during activation)
        List<WfMessage> wfMessageList =
                db().listAll(new WfMessageQuery().wfCategoryName(wfCategoryName).wfCategoryVersion(wfCategoryVersion));
        if (!wfMessageList.isEmpty()) {
            Long wfModuleId = systemService.getModuleId(WorkflowModuleNameConstants.WORKFLOW_MODULE);
            NotificationTemplate notificationTemplate = new NotificationTemplate();
            for (WfMessage wfMessage : wfMessageList) {
                String messageName =
                        WfNameUtils.getMessageGlobalName(wfMessage.getWfCategoryName(), wfMessage.getName());
                NotificationTemplate oldNotificationTemplate =
                        notificationService.findNotificationTemplate(WorkflowModuleNameConstants.WORKFLOW_MODULE,
                                messageName);
                String description = resolveApplicationMessage(wfMessage.getDescription());
                if (oldNotificationTemplate == null) {
                    notificationTemplate.setModuleId(wfModuleId);
                    notificationTemplate.setName(messageName);
                    notificationTemplate.setDescription(description);
                    notificationTemplate.setSubject(wfMessage.getSubject());
                    notificationTemplate.setTemplate(wfMessage.getTemplate());
                    notificationTemplate.setAttachmentGenerator(wfMessage.getAttachmentGenerator());
                    notificationTemplate.setMessageType(wfMessage.getMessageType());
                    notificationTemplate.setActionLink(wfMessage.getActionLink());
                    notificationTemplate.setHtmlFlag(wfMessage.getHtmlFlag());
                    notificationService.createNotificationTemplate(notificationTemplate);
                } else {
                    oldNotificationTemplate.setDescription(description);
                    oldNotificationTemplate.setSubject(wfMessage.getSubject());
                    oldNotificationTemplate.setTemplate(wfMessage.getTemplate());
                    oldNotificationTemplate.setAttachmentGenerator(wfMessage.getAttachmentGenerator());
                    oldNotificationTemplate.setMessageType(wfMessage.getMessageType());
                    oldNotificationTemplate.setActionLink(wfMessage.getActionLink());
                    oldNotificationTemplate.setHtmlFlag(wfMessage.getHtmlFlag());
                    notificationService.updateNotificationTemplate(oldNotificationTemplate);
                }
            }
        }

        // Enable version
        db().updateAll(new WfCategoryQuery().name(wfCategoryName).version(wfCategoryVersion),
                new Update().add("updateDt", db().getNow()).add("status", RecordStatus.ACTIVE));
    }

    @Taskable(
            name = WorkflowExecuteTransitionTaskConstants.TASK_NAME, description = "Workflow Item Transition Task",
            parameters = {
                    @Parameter(name = WorkflowExecuteTransitionTaskConstants.TRANSITIONUNIT_INDEX, type = int.class) },
            limit = TaskExecLimit.ALLOW_MULTIPLE)
    public int executeWorkflowItemTransition(TaskMonitor taskMonitor, int transitionUnitIndex) throws UnifyException {
        FlowingWfItemTransition flowingWfItemTransition = null;
        while ((flowingWfItemTransition =
                wfTransitionQueueManager.getNextFlowingWfItemTransition(transitionUnitIndex)) != null) {
            FlowingWfItem flowingWfItem = flowingWfItemTransition.getFlowingWfItem();
            WfStepDef targetWfStepDef = flowingWfItemTransition.getTargetWfStepDef();
            beginClusterLock(targetWfStepDef.getTemplateGlobalLockName());
            try {
                performFlowingWfItemTransition(targetWfStepDef, flowingWfItem);
            } catch (Exception e) {
                logError(e);
                try {
                    // Push workflow item to error step
                    flowingWfItem.setSourceWfStepDef(targetWfStepDef);
                    if (e instanceof UnifyException) {
                        flowingWfItem.setErrorCode(((UnifyException) e).getErrorCode());
                    }

                    flowingWfItem.setErrorMsg(getExceptionMessage(LocaleType.APPLICATION, e));
                    performFlowingWfItemTransition(flowingWfItem.getErrorWfStepDef(), flowingWfItem);
                } catch (Exception e1) {
                    logError(e1);
                }
            } finally {
                wfTransitionQueueManager.acknowledgeTransition(flowingWfItemTransition);
                endClusterLock(targetWfStepDef.getTemplateGlobalLockName());
            }
        }

        return 0;
    }

    @Transactional(TransactionAttribute.REQUIRES_NEW)
    public void performFlowingWfItemTransition(WfStepDef targetWfStepDef, FlowingWfItem flowingWfItem)
            throws UnifyException {
        doActualTransition(targetWfStepDef, flowingWfItem);
    }

    private List<WfAction> getWorkflowStepActions(WfStepDef wfStepDef) throws UnifyException {
        List<WfAction> actions = new ArrayList<WfAction>();
        for (WfUserActionDef wfUserActionDef : wfStepDef.getUserActionList()) {
            actions.add(new WfAction(wfUserActionDef.getName(), resolveSessionMessage(wfUserActionDef.getLabel()),
                    wfUserActionDef.getCommentReqType(), wfUserActionDef.isValidatePage()));
        }

        return actions;
    }

    private BadgeInfo getStatusBadge(WfItem wfItem) throws UnifyException {
        Date now = db().getNow();
        if (wfItem.getExpectedDt() != null && now.after(wfItem.getExpectedDt())) {
            return OVERDUE_BADGEINFO;
        }

        if (wfItem.getCriticalDt() != null && now.after(wfItem.getCriticalDt())) {
            return CRITICAL_BADGEINFO;
        }

        return PENDING_BADGEINFO;
    }

    private void populateChildList(WfDoc wfDoc, WfDocumentConfig wfDocConfig) throws UnifyException {
        // Fields
        List<WfDocField> fieldList = new ArrayList<WfDocField>();
        if (DataUtils.isNotBlank(wfDocConfig.getWfFieldsConfig().getWfFieldConfigList())) {
            for (WfFieldConfig wfDocFieldConfig : wfDocConfig.getWfFieldsConfig().getWfFieldConfigList()) {
                WfDocField wfDocField = new WfDocField();
                wfDocField.setName(wfDocFieldConfig.getName());
                wfDocField.setDescription(resolveApplicationMessage(wfDocFieldConfig.getDescription()));
                wfDocField.setDataType(wfDocFieldConfig.getDataType());
                wfDocField.setList(wfDocFieldConfig.isList());
                fieldList.add(wfDocField);
            }
        }

        // Complex fields
        if (DataUtils.isNotBlank(wfDocConfig.getWfFieldsConfig().getWfComplexFieldConfigList())) {
            for (WfComplexFieldConfig wfDocComplexFieldConfig : wfDocConfig.getWfFieldsConfig()
                    .getWfComplexFieldConfigList()) {
                WfDocField wfDocField = new WfDocField();
                wfDocField.setName(wfDocComplexFieldConfig.getName());
                wfDocField.setDescription(resolveApplicationMessage(wfDocComplexFieldConfig.getDescription()));
                wfDocField.setDataType(wfDocComplexFieldConfig.getDataType());
                wfDocField.setList(wfDocComplexFieldConfig.isList());
                fieldList.add(wfDocField);

                for (WfFieldConfig wfDocFieldConfig : wfDocComplexFieldConfig.getWfFieldConfigList()) {
                    wfDocField = new WfDocField();
                    wfDocField.setParentName(wfDocComplexFieldConfig.getName());
                    wfDocField.setName(wfDocFieldConfig.getName());
                    wfDocField.setDescription(resolveApplicationMessage(wfDocFieldConfig.getDescription()));
                    wfDocField.setDataType(wfDocFieldConfig.getDataType());
                    wfDocField.setList(wfDocFieldConfig.isList());
                    fieldList.add(wfDocField);
                }
            }
        }

        wfDoc.setFieldList(fieldList);

        // Classifiers
        List<WfDocClassifier> classifierList = null;
        if (wfDocConfig.getWfClassifiersConfig() != null
                && DataUtils.isNotBlank(wfDocConfig.getWfClassifiersConfig().getWfClassifierConfigList())) {
            classifierList = new ArrayList<WfDocClassifier>();
            for (WfClassifierConfig wfDocClassifierConfig : wfDocConfig.getWfClassifiersConfig()
                    .getWfClassifierConfigList()) {
                WfDocClassifier wfDocClassifier = new WfDocClassifier();
                wfDocClassifier.setName(wfDocClassifierConfig.getName());
                wfDocClassifier.setDescription(resolveApplicationMessage(wfDocClassifierConfig.getDescription()));
                wfDocClassifier.setFilterLogic(wfDocClassifierConfig.getFilterLogic());
                wfDocClassifier.setLogic(wfDocClassifierConfig.getLogic());

                if (DataUtils.isNotBlank(wfDocClassifierConfig.getWfClassifierFilterConfigList())) {
                    // Classifier filters
                    List<WfDocClassifierFilter> filterList = new ArrayList<WfDocClassifierFilter>();
                    for (WfClassifierFilterConfig wfDocClassifierFilterConfig : wfDocClassifierConfig
                            .getWfClassifierFilterConfigList()) {
                        WfDocClassifierFilter wfDocClassifierFilter = new WfDocClassifierFilter();
                        wfDocClassifierFilter.setFieldName(wfDocClassifierFilterConfig.getField());
                        wfDocClassifierFilter.setOp(wfDocClassifierFilterConfig.getOp());
                        wfDocClassifierFilter.setValue1(wfDocClassifierFilterConfig.getValue1());
                        wfDocClassifierFilter.setValue2(wfDocClassifierFilterConfig.getValue2());
                        wfDocClassifierFilter.setFieldOnly(wfDocClassifierFilterConfig.getFieldOnly());
                        filterList.add(wfDocClassifierFilter);
                    }

                    wfDocClassifier.setFilterList(filterList);
                }

                classifierList.add(wfDocClassifier);
            }
        }
        wfDoc.setClassifierList(classifierList);

        // Attachments
        List<WfDocAttachment> attachmentList = null;
        if (wfDocConfig.getWfAttachmentsConfig() != null
                && DataUtils.isNotBlank(wfDocConfig.getWfAttachmentsConfig().getWfAttachmentConfigList())) {
            attachmentList = new ArrayList<WfDocAttachment>();
            for (WfAttachmentConfig wfDocAttachmentConfig : wfDocConfig.getWfAttachmentsConfig()
                    .getWfAttachmentConfigList()) {
                WfDocAttachment wfDocAttachment = new WfDocAttachment();
                wfDocAttachment.setName(wfDocAttachmentConfig.getName());
                wfDocAttachment.setDescription(resolveApplicationMessage(wfDocAttachmentConfig.getDescription()));
                wfDocAttachment.setLabel(wfDocAttachmentConfig.getLabel());
                wfDocAttachment.setAttachmentType(wfDocAttachmentConfig.getType());
                attachmentList.add(wfDocAttachment);
            }
        }
        wfDoc.setAttachmentList(attachmentList);

        // Bean mappings
        List<WfDocBeanMapping> recordMappingList = null;
        if (wfDocConfig.getWfBeanMappingsConfig() != null
                && DataUtils.isNotBlank(wfDocConfig.getWfBeanMappingsConfig().getBeanMappingList())) {
            recordMappingList = new ArrayList<WfDocBeanMapping>();
            for (WfBeanMappingConfig wfDocBeanMappingConfig : wfDocConfig.getWfBeanMappingsConfig()
                    .getBeanMappingList()) {
                WfDocBeanMapping wfDocBeanMapping = new WfDocBeanMapping();
                wfDocBeanMapping.setName(wfDocBeanMappingConfig.getName());
                wfDocBeanMapping.setComplexFieldName(wfDocBeanMappingConfig.getComplexFieldName());
                wfDocBeanMapping.setDescription(resolveApplicationMessage(wfDocBeanMappingConfig.getDescription()));
                wfDocBeanMapping.setBeanType(wfDocBeanMappingConfig.getBeanType());

                List<WfDocFieldMapping> fieldMappingList = new ArrayList<WfDocFieldMapping>();
                for (WfFieldMappingConfig wfDocFieldMappingConfig : wfDocBeanMappingConfig.getFieldMappingList()) {
                    WfDocFieldMapping wfDocFieldMapping = new WfDocFieldMapping();
                    wfDocFieldMapping.setDocFieldName(wfDocFieldMappingConfig.getDocFieldName());
                    wfDocFieldMapping.setBeanFieldName(wfDocFieldMappingConfig.getBeanFieldName());
                    fieldMappingList.add(wfDocFieldMapping);
                }

                wfDocBeanMapping.setFieldMappingList(fieldMappingList);
                recordMappingList.add(wfDocBeanMapping);
            }
        }
        wfDoc.setBeanMappingList(recordMappingList);

        // Form
        WfForm wfForm = null;
        WfFormConfig wfFormConfig = wfDocConfig.getWfFormConfig();
        if (wfFormConfig != null) {
            wfForm = new WfForm();
            // Tabs
            List<WfFormTab> tabList = new ArrayList<WfFormTab>();
            List<WfFormSection> sectionList = new ArrayList<WfFormSection>();
            List<WfFormField> formFieldList = new ArrayList<WfFormField>();
            for (WfFormTabConfig wfFormTabConfig : wfFormConfig.getWfFormTabConfigList()) {
                WfFormTab wfFormTab = new WfFormTab();
                wfFormTab.setName(wfFormTabConfig.getName());
                wfFormTab.setDescription(resolveApplicationMessage(wfFormTabConfig.getDescription()));
                wfFormTab.setLabel(wfFormTabConfig.getLabel());
                wfFormTab.setPseudo(wfFormTabConfig.getPseudo());

                // Sections
                for (WfFormSectionConfig wfFormSectionConfig : wfFormTabConfig.getWfFormSectionConfigList()) {
                    WfFormSection wfFormSection = new WfFormSection();
                    wfFormSection.setName(wfFormSectionConfig.getName());
                    wfFormSection.setDescription(resolveApplicationMessage(wfFormSectionConfig.getDescription()));
                    wfFormSection.setLabel(wfFormSectionConfig.getLabel());
                    wfFormSection.setBinding(wfFormSectionConfig.getBinding());
                    wfFormSection.setTabName(wfFormTabConfig.getName());

                    // Fields
                    for (WfFormFieldConfig wfFormFieldConfig : wfFormSectionConfig.getWfFormFieldConfigList()) {
                        WfFormField wfFormField = new WfFormField();
                        wfFormField.setBinding(wfFormFieldConfig.getBinding());
                        wfFormField.setLabel(wfFormFieldConfig.getLabel());
                        wfFormField.setSectionName(wfFormSectionConfig.getName());
                        wfFormField.setEditorUpl(wfFormFieldConfig.getEditorUpl());
                        wfFormField.setRequired(wfFormFieldConfig.getRequired());
                        formFieldList.add(wfFormField);
                    }

                    sectionList.add(wfFormSection);
                }

                tabList.add(wfFormTab);
            }
            wfForm.setTabList(tabList);
            wfForm.setSectionList(sectionList);
            wfForm.setFieldList(formFieldList);
        }

        wfDoc.setWfForm(wfForm);
    }

    private void populateChildList(WfTemplate wfTemplate, WfTemplateConfig wfTemplateConfig) throws UnifyException {
        WfStep startWfStep = null;
        WfStep endWfStep = null;
        WfStep manualWfStep = null;

        // Documents
        List<WfTemplateDoc> templateDocList = new ArrayList<WfTemplateDoc>();
        for (WfTemplateDocConfig wfTemplateDocConfig : wfTemplateConfig.getWfTemplateDocsConfig()
                .getWfTemplateDocConfigList()) {
            WfTemplateDoc wfTemplateDoc = new WfTemplateDoc();
            wfTemplateDoc.setWfDocName(wfTemplateDocConfig.getName());
            wfTemplateDoc.setWfDocViewer(wfTemplateDocConfig.getViewer());
            wfTemplateDoc.setWorkAssigner(wfTemplateDocConfig.getAssigner());
            wfTemplateDoc.setManual(wfTemplateDocConfig.isManual());
            templateDocList.add(wfTemplateDoc);
        }
        wfTemplate.setTemplateDocList(templateDocList);

        // Steps
        List<WfStep> stepList = new ArrayList<WfStep>();
        for (WfStepConfig wfStepConfig : wfTemplateConfig.getWfStepsConfig().getWfStepConfigList()) {
            WfStep wfStep = new WfStep();
            wfStep.setName(wfStepConfig.getName());
            wfStep.setDescription(resolveApplicationMessage(wfStepConfig.getDescription()));
            wfStep.setLabel(wfStepConfig.getLabel());
            wfStep.setWorkAssigner(wfStepConfig.getAssigner());
            wfStep.setBranch(wfStepConfig.getBranch());
            wfStep.setOrigin(wfStepConfig.getOrigin());
            wfStep.setStepType(wfStepConfig.getType());
            wfStep.setPriorityLevel(wfStepConfig.getPriority());
            wfStep.setParticipantType(wfStepConfig.getParticipant());
            wfStep.setItemsPerSession(wfStepConfig.getItemsPerSession());
            wfStep.setCriticalMinutes(wfStepConfig.getCriticalMinutes());
            wfStep.setExpiryMinutes(wfStepConfig.getExpiryMinutes());
            wfStep.setIncludeForwarder(wfStepConfig.getIncludeForwarder());
            wfStep.setAudit(wfStepConfig.getAudit());
            wfStep.setBranchOnly(wfStepConfig.getBranchOnly());
            wfStep.setDepartmentOnly(wfStepConfig.getDepartmentOnly());

            if (wfStepConfig.getType().isStart()) {
                startWfStep = wfStep;
            } else if (wfStepConfig.getType().isEnd()) {
                endWfStep = wfStep;
            } else if (wfStepConfig.getType().isManual()) {
                manualWfStep = wfStep;
            }

            // Branches
            List<WfBranch> branchList = null;
            if (wfStepConfig.getWfBranchesConfig() != null
                    && DataUtils.isNotBlank(wfStepConfig.getWfBranchesConfig().getWfBranchConfigList())) {
                branchList = new ArrayList<WfBranch>();
                for (WfBranchConfig wfBranchConfig : wfStepConfig.getWfBranchesConfig().getWfBranchConfigList()) {
                    WfBranch wfBranch = new WfBranch();
                    wfBranch.setName(wfBranchConfig.getName());
                    wfBranch.setDescription(resolveApplicationMessage(wfBranchConfig.getDescription()));
                    wfBranch.setTarget(wfBranchConfig.getTarget());
                    List<WfMergeField> mergeFieldList = null;
                    if (wfBranchConfig.getWfMergesConfig() != null
                            && DataUtils.isNotBlank(wfBranchConfig.getWfMergesConfig().getWfMergeConfigList())) {
                        mergeFieldList = new ArrayList<WfMergeField>();
                        for (WfMergeConfig wfMergeConfig : wfBranchConfig.getWfMergesConfig().getWfMergeConfigList()) {
                            WfMergeField wfMergeField = new WfMergeField();
                            wfMergeField.setFieldName(wfMergeConfig.getFieldName());
                            mergeFieldList.add(wfMergeField);
                        }
                    }
                    wfBranch.setMergeFieldList(mergeFieldList);

                    branchList.add(wfBranch);
                }
            }
            wfStep.setBranchList(branchList);

            // Enrichments
            List<WfEnrichment> enrichmentList = null;
            if (wfStepConfig.getWfEnrichmentsConfig() != null
                    && DataUtils.isNotBlank(wfStepConfig.getWfEnrichmentsConfig().getWfEnrichmentConfigList())) {
                enrichmentList = new ArrayList<WfEnrichment>();
                for (WfEnrichmentConfig wfEnrichmentConfig : wfStepConfig.getWfEnrichmentsConfig()
                        .getWfEnrichmentConfigList()) {
                    WfEnrichment wfEnrichment = new WfEnrichment();
                    wfEnrichment.setName(wfEnrichmentConfig.getName());
                    wfEnrichment.setDescription(resolveApplicationMessage(wfEnrichmentConfig.getDescription()));
                    wfEnrichment.setDocName(wfEnrichmentConfig.getDocument());
                    wfEnrichment.setLogic(wfEnrichmentConfig.getLogic());
                    enrichmentList.add(wfEnrichment);
                }
            }
            wfStep.setEnrichmentList(enrichmentList);

            // Entity actions
            List<WfRecordAction> recordActionList = null;
            if (wfStepConfig.getWfRecordActionsConfig() != null
                    && DataUtils.isNotBlank(wfStepConfig.getWfRecordActionsConfig().getWfRecordActionConfigList())) {
                recordActionList = new ArrayList<WfRecordAction>();
                for (WfRecordActionConfig wfRecordActionConfig : wfStepConfig.getWfRecordActionsConfig()
                        .getWfRecordActionConfigList()) {
                    WfRecordAction wfRecordAction = new WfRecordAction();
                    wfRecordAction.setName(wfRecordActionConfig.getName());
                    wfRecordAction.setDescription(resolveApplicationMessage(wfRecordActionConfig.getDescription()));
                    wfRecordAction.setDocName(wfRecordActionConfig.getDocument());
                    wfRecordAction.setDocMappingName(wfRecordActionConfig.getDocMappingName());
                    wfRecordAction.setActionType(wfRecordActionConfig.getActionType());
                    recordActionList.add(wfRecordAction);
                }
            }
            wfStep.setRecordActionList(recordActionList);

            // Routings
            List<WfRouting> routingList = null;
            if (wfStepConfig.getWfRoutingsConfig() != null
                    && DataUtils.isNotBlank(wfStepConfig.getWfRoutingsConfig().getWfRoutingConfigList())) {
                routingList = new ArrayList<WfRouting>();
                for (WfRoutingConfig wfRoutingConfig : wfStepConfig.getWfRoutingsConfig().getWfRoutingConfigList()) {
                    WfRouting wfRouting = new WfRouting();
                    wfRouting.setName(wfRoutingConfig.getName());
                    wfRouting.setDescription(resolveApplicationMessage(wfRoutingConfig.getDescription()));
                    wfRouting.setDocName(wfRoutingConfig.getDocument());
                    wfRouting.setClassifierName(wfRoutingConfig.getClassifierName());
                    wfRouting.setTargetWfStepName(wfRoutingConfig.getTargetStepName());
                    routingList.add(wfRouting);
                }
            }
            wfStep.setRoutingList(routingList);

            // User actions
            List<WfUserAction> userActionList = null;
            if (wfStepConfig.getWfUserActionsConfig() != null
                    && DataUtils.isNotBlank(wfStepConfig.getWfUserActionsConfig().getWfUserActionConfigList())) {
                userActionList = new ArrayList<WfUserAction>();
                for (WfUserActionConfig wfUserActionConfig : wfStepConfig.getWfUserActionsConfig()
                        .getWfUserActionConfigList()) {
                    WfUserAction wfUserAction = new WfUserAction();
                    wfUserAction.setName(wfUserActionConfig.getName());
                    wfUserAction.setDescription(resolveApplicationMessage(wfUserActionConfig.getDescription()));
                    wfUserAction.setLabel(wfUserActionConfig.getLabel());
                    wfUserAction.setCommentReqType(wfUserActionConfig.getCommentRequirement());
                    wfUserAction.setTargetWfStepName(wfUserActionConfig.getTargetStepName());
                    wfUserAction.setValidatePage(wfUserActionConfig.getValidatePage());

                    if (DataUtils.isNotBlank(wfUserActionConfig.getAttachmentCheckConfigList())) {
                        List<WfAttachmentCheck> attachmentCheckList = new ArrayList<WfAttachmentCheck>();
                        for (WfAttachmentCheckConfig wfAttachmentCheckConfig : wfUserActionConfig
                                .getAttachmentCheckConfigList()) {
                            WfAttachmentCheck wfAttachmentCheck = new WfAttachmentCheck();
                            wfAttachmentCheck.setDocName(wfAttachmentCheckConfig.getDocument());
                            wfAttachmentCheck.setWfDocAttachmentName(wfAttachmentCheckConfig.getAttachmentName());
                            wfAttachmentCheck.setRequirementType(wfAttachmentCheckConfig.getRequirementType());
                            attachmentCheckList.add(wfAttachmentCheck);
                        }

                        wfUserAction.setAttachmentCheckList(attachmentCheckList);
                    }

                    userActionList.add(wfUserAction);
                }
            }
            wfStep.setUserActionList(userActionList);

            // Form privileges
            List<WfFormPrivilege> formPrivilegeList = null;
            if (wfStepConfig.getWfFormPrivilegesConfig() != null
                    && DataUtils.isNotBlank(wfStepConfig.getWfFormPrivilegesConfig().getWfFormPrivilegesConfigList())) {
                formPrivilegeList = new ArrayList<WfFormPrivilege>();
                for (WfFormPrivilegeConfig wfFormPrivilegeConfig : wfStepConfig.getWfFormPrivilegesConfig()
                        .getWfFormPrivilegesConfigList()) {
                    WfFormPrivilege wfFormPrivilege = new WfFormPrivilege();
                    wfFormPrivilege.setDocName(wfFormPrivilegeConfig.getDocument());
                    wfFormPrivilege.setType(wfFormPrivilegeConfig.getType());
                    wfFormPrivilege.setWfFormElementName(wfFormPrivilegeConfig.getName());
                    wfFormPrivilege.setDisabled(wfFormPrivilegeConfig.getDisabled());
                    wfFormPrivilege.setEditable(wfFormPrivilegeConfig.getEditable());
                    wfFormPrivilege.setRequired(wfFormPrivilegeConfig.getRequired());
                    wfFormPrivilege.setVisible(wfFormPrivilegeConfig.getVisible());
                    formPrivilegeList.add(wfFormPrivilege);
                }
            }
            wfStep.setFormPrivilegeList(formPrivilegeList);

            // Policies
            List<WfPolicy> policyList = null;
            if (wfStepConfig.getWfPoliciesConfig() != null
                    && DataUtils.isNotBlank(wfStepConfig.getWfPoliciesConfig().getWfPolicyConfigList())) {
                policyList = new ArrayList<WfPolicy>();
                for (WfPolicyConfig wfPolicyConfig : wfStepConfig.getWfPoliciesConfig().getWfPolicyConfigList()) {
                    WfPolicy wfPolicy = new WfPolicy();
                    wfPolicy.setName(wfPolicyConfig.getName());
                    wfPolicy.setDescription(resolveApplicationMessage(wfPolicyConfig.getDescription()));
                    wfPolicy.setLogic(wfPolicyConfig.getLogic());
                    policyList.add(wfPolicy);
                }
            }
            wfStep.setPolicyList(policyList);

            // Alerts
            List<WfAlert> alertList = null;
            if (wfStepConfig.getWfAlertsConfig() != null
                    && DataUtils.isNotBlank(wfStepConfig.getWfAlertsConfig().getWfAlertConfigList())) {
                alertList = new ArrayList<WfAlert>();
                for (WfAlertConfig wfAlertConfig : wfStepConfig.getWfAlertsConfig().getWfAlertConfigList()) {
                    WfAlert wfAlert = new WfAlert();
                    wfAlert.setName(wfAlertConfig.getName());
                    wfAlert.setDescription(resolveApplicationMessage(wfAlertConfig.getDescription()));
                    wfAlert.setDocName(wfAlertConfig.getDocument());
                    wfAlert.setFireOnPrevStepName(wfAlertConfig.getFireOnPrevStepName());
                    wfAlert.setType(wfAlertConfig.getType());
                    wfAlert.setParticipant(wfAlertConfig.getParticipant());
                    wfAlert.setChannel(wfAlertConfig.getChannel());
                    wfAlert.setNotificationTemplateCode(wfAlertConfig.getMessage());
                    wfAlert.setFilterName(wfAlertConfig.getFilter());
                    alertList.add(wfAlert);
                }
            }
            wfStep.setAlertList(alertList);

            stepList.add(wfStep);
        }

        wfTemplate.setManualOption(Boolean.FALSE);
        if (manualWfStep != null) {
            wfTemplate.setManualOption(Boolean.TRUE);
            // Add implicit user actions to manual step
            List<WfUserAction> userActionList = new ArrayList<WfUserAction>();
            WfUserAction wfUserAction = new WfUserAction();
            wfUserAction.setName("submit");
            wfUserAction.setDescription(getApplicationMessage("workflow.wftemplate.wfstep.manualsubmit"));
            wfUserAction.setLabel(getApplicationMessage("label.submit"));
            wfUserAction.setCommentReqType(RequirementType.NONE);
            wfUserAction.setTargetWfStepName(startWfStep.getName());
            wfUserAction.setValidatePage(Boolean.TRUE);
            userActionList.add(wfUserAction);

            wfUserAction = new WfUserAction();
            wfUserAction.setName("discard");
            wfUserAction.setDescription(getApplicationMessage("workflow.wftemplate.wfstep.manualdiscard"));
            wfUserAction.setLabel(getApplicationMessage("label.discard"));
            wfUserAction.setCommentReqType(RequirementType.NONE);
            wfUserAction.setTargetWfStepName(endWfStep.getName());
            wfUserAction.setValidatePage(Boolean.TRUE);
            userActionList.add(wfUserAction);

            manualWfStep.setUserActionList(userActionList);
        }

        wfTemplate.setStepList(stepList);
    }

    private FlowingWfItem createFlowingWfItem(WfItem wfItem) throws UnifyException {
        Long wfItemId = wfItem.getId();
        WfItemPackedDoc wfItemPackedDoc = db().find(WfItemPackedDoc.class, wfItemId);
        WfStepDef wfStepDef = wfSteps.get(wfItem.getStepGlobalName());
        WfProcessDef wfProcessDef = wfProcesses.get(wfItem.getProcessGlobalName());
        WfTemplateDef wfTemplateDef = wfProcessDef.getWfTemplateDef();
        WfTemplateDocDef wfTemplateDocDef = wfProcessDef.getWfTemplateDocDef();
        PackableDoc pd =
                PackableDoc.unpack(wfTemplateDocDef.getWfDocDef().getDocConfig(), wfItemPackedDoc.getPackedDoc(),
                        wfStepDef.isAudit());

        // Construct title
        String title =
                getSessionMessage("workflowitem.title.struct", wfTemplateDef.getDescription(),
                        wfTemplateDocDef.getWfDocDef().getDescription(), wfStepDef.getDescription(),
                        wfItem.getDescription());

        // Create work item
        FlowingWfItem flowingWfItem = new FlowingWfItem(wfProcessDef, wfStepDef, wfItem, wfItemId, title, pd);
        if (!StringUtils.isBlank(wfItem.getPrevWfStepName())) {
            flowingWfItem.setPrevWfStepDef(wfTemplateDef.getWfStepDef(wfItem.getPrevWfStepName()));
        }

        flowingWfItem.setErrorSource(wfItem.getSrcWfStepName());
        flowingWfItem.setErrorCode(wfItem.getErrorCode());
        flowingWfItem.setErrorMsg(wfItem.getErrorMsg());
        return flowingWfItem;
    }

    private Long submitToReceptacle(WfProcessDef wfProcessDef, WfStepDef trgWfStepDef, String branchCode,
            String departmentCode, PackableDoc packableDoc) throws UnifyException {
        if (!trgWfStepDef.isStart() && !trgWfStepDef.isManual()) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_SUBMIT_NONSTART_NONRECEPTACLE,
                    trgWfStepDef.getGlobalName());
        }

        // Create workflow item and submit to step
        return createWfItemAndSubmitToStep(new WfItem(), wfProcessDef, trgWfStepDef, branchCode, departmentCode,
                packableDoc);
    }

    private Long createWfItemAndSubmitToStep(WfItem wfItem, WfProcessDef wfProcessDef, WfStepDef trgWfStepDef,
            String branchCode, String departmentCode, PackableDoc packableDoc) throws UnifyException {
        Long submissionId = sequenceNumberService.getCachedBlockNextSequenceNumber(WORKFLOW_SUBMISSION_ID_SEQUENCE);
        wfItem.setSubmissionId(submissionId);
        wfItem.setStepGlobalName(trgWfStepDef.getGlobalName());
        wfItem.setBranchCode(branchCode);
        wfItem.setDepartmentCode(departmentCode);
        wfItem.setCreateDt(db().getNow());
        Long wfItemId = (Long) db().create(wfItem);

        WfItemPackedDoc wfItemPackedDoc = new WfItemPackedDoc();
        wfItemPackedDoc.setWfItemId(wfItemId);
        wfItemPackedDoc.setPackedDoc(packableDoc.pack());
        db().create(wfItemPackedDoc);
        packableDoc.clearUpdated();

        // Push to step
        // Asynchronous processing. Push transition to queue.
        WfTemplateDocDef wfTemplateDocDef = wfProcessDef.getWfTemplateDocDef();
        wfItem.setWfItemDesc(WfNameUtils.describe(wfTemplateDocDef.getWfDocDef().getItemDescFormat(), packableDoc));
        FlowingWfItem flowingWfItem =
                new FlowingWfItem(wfProcessDef, trgWfStepDef, wfItem, wfItemId, null, packableDoc);
        wfTransitionQueueManager.pushWfItemToTransitionQueue(submissionId, trgWfStepDef, flowingWfItem);
        return submissionId;
    }

    private void doActualTransition(final WfStepDef targetWfStepDef, final FlowingWfItem flowingWfItem)
            throws UnifyException {
        // Flow workflow item into target step
        if (!targetWfStepDef.isError()) {
            flowingWfItem.setPrevWfStepDef(flowingWfItem.getWfStepDef());
        }
        flowingWfItem.setWfStepDef(targetWfStepDef);

        // Create history
        final Date stepDt = db().getNow();
        Long wfItemHistId = flowingWfItem.getWfItemHistId();
        if (wfItemHistId == null) {
            WfItemHist wfHist = new WfItemHist();
            wfHist.setProcessGlobalName(flowingWfItem.getProcessGlobalName());
            wfHist.setDescription(flowingWfItem.getDescription());
            wfHist.setDocId(flowingWfItem.getDocId());
            wfHist.setInitiatedBy(getUserToken().getUserLoginId());
            wfItemHistId = (Long) db().create(wfHist);
        }

        Date criticalDt = null;
        if (targetWfStepDef.isCritical()) {
            criticalDt = CalendarUtils.getDateWithOffset(stepDt, targetWfStepDef.getCriticalMilliSec());
        }

        Date expectedDt = null;
        if (targetWfStepDef.isExpiry()) {
            expectedDt = CalendarUtils.getDateWithOffset(stepDt, targetWfStepDef.getExpiryMilliSec());
        }

        WfItemEvent wfHistEvent = new WfItemEvent();
        wfHistEvent.setWfItemHistId(wfItemHistId);
        wfHistEvent.setWfStepName(targetWfStepDef.getName());
        wfHistEvent.setStepDt(stepDt);
        wfHistEvent.setCriticalDt(criticalDt);
        wfHistEvent.setExpectedDt(expectedDt);
        String prevStepName = null;
        if (flowingWfItem.isWithPrevStep()) {
            prevStepName = flowingWfItem.getPrevWfStepDef().getName();
        }
        wfHistEvent.setPrevWfStepName(prevStepName);

        if (targetWfStepDef.isError()) {
            wfHistEvent.setSrcWfStepName(flowingWfItem.getSourceWfStepDef().getName());
            wfHistEvent.setErrorCode(flowingWfItem.getErrorCode());
            wfHistEvent.setErrorMsg(flowingWfItem.getErrorMsg());
        }

        Long wfHistEventId = (Long) db().create(wfHistEvent);

        // Update workflow item information.
        PackableDoc packableDoc = flowingWfItem.getPd();
        final Long wfItemId = flowingWfItem.getWfItemId();
        flowingWfItem.setWfItemHistId(wfItemHistId);
        flowingWfItem.setWfHistEventId(wfHistEventId);
        db().updateById(WfItem.class, wfItemId,
                new Update().add("wfHistEventId", wfHistEventId).add("stepGlobalName", targetWfStepDef.getGlobalName())
                        .add("stepDt", stepDt).add("criticalDt", criticalDt).add("expectedDt", expectedDt)
                        .add("participantType", targetWfStepDef.getParticipantType())
                        .add("forwardedBy", flowingWfItem.getHeldBy()));

        // Perform enrichment if any
        String docName = flowingWfItem.getDocName();
        for (WfEnrichmentDef wfEnrichmentDef : targetWfStepDef.getEnrichmentList()) {
            WfItemEnrichmentPolicy wfItemEnrichmentLogic =
                    (WfItemEnrichmentPolicy) getComponent(wfEnrichmentDef.getLogic());
            if (!wfEnrichmentDef.isDoc() || wfEnrichmentDef.getDocName().equals(docName)) {
                wfItemEnrichmentLogic.enrich(flowingWfItem.getReaderWriter());
            }
        }

        // Perform record actions if any
        if (DataUtils.isNotBlank(targetWfStepDef.getRecordActionList())) {
            for (WfRecordActionDef wfRecordActionDef : targetWfStepDef.getRecordActionList()) {
                if (wfRecordActionDef.getDocName().equals(docName)) {
                    switch (wfRecordActionDef.getActionType()) {
                        case CREATE: {
                            Document document = ReflectUtils.newInstance(wfRecordActionDef.getBeanClass());
                            packableDoc.writeTo(document);
                            Object id = genericService.create(document);
                            packableDoc.setId(id);
                            // Update document id in item history
                            db().updateById(WfItemHist.class, wfItemHistId, new Update().add("docId", id));
                        }
                            break;
                        case DELETE: {
                            genericService.delete(wfRecordActionDef.getBeanClass(), packableDoc.getId());
                        }
                            break;
                        case READ: {
                            Document document =
                                    genericService.find(wfRecordActionDef.getBeanClass(), packableDoc.getId());
                            packableDoc.readFrom(document);
                        }
                            break;
                        case UPDATE: {
                            Document document =
                                    genericService.find(wfRecordActionDef.getBeanClass(), packableDoc.getId());
                            packableDoc.writeTo(document);
                            genericService.update(document);
                        }
                            break;
                    }
                }
            }
        }

        // Apply policies
        FlowingWfItem.Reader flowingWfItemReader = flowingWfItem.getReader();
        for (WfPolicyDef wfPolicyDef : targetWfStepDef.getPolicyList()) {
            if (!wfPolicyDef.isDoc() || wfPolicyDef.getDocName().equals(docName)) {
                WfItemProcessPolicy wfItemProcessPolicy = (WfItemProcessPolicy) getComponent(wfPolicyDef.getLogic());
                wfItemProcessPolicy.execute(flowingWfItemReader);
            }
        }

        // Send pass-through alerts
        for (WfAlertDef wfAlertDef : targetWfStepDef.getAlertList()) {
            if (wfAlertDef.isPassThrough() && wfAlertDef.isFireAlertOn(docName, prevStepName)) {
                wfItemAlertLogic.sendAlert(flowingWfItemReader, wfAlertDef);
            }
        }

        if (targetWfStepDef.isEnd()) {
            // Termination. Delete workflow item.
            Long wfItemAttachmentRefId =
                    db().value(Long.class, "wfItemAttachmentRefId", new WfItemQuery().id(wfItemId));
            db().deleteAll(new WfItemAttachmentQuery().wfItemAttachmentRefId(wfItemAttachmentRefId));
            db().deleteAll(new WfItemPackedDocQuery().wfItemId(wfItemId));
            db().delete(WfItem.class, wfItemId);
        } else if (targetWfStepDef.isSplit()) {
            // Create split event
            WfItemSplitEvent wfItemSplitEvent = new WfItemSplitEvent();
            wfItemSplitEvent.setParentSplitEventId(flowingWfItem.getWfItemSplitEventId());
            wfItemSplitEvent.setSplitOriginName(targetWfStepDef.getName());
            wfItemSplitEvent.setSplitBranchName(flowingWfItem.getSplitBranchName());
            wfItemSplitEvent.setSplitCount(targetWfStepDef.branchCount());
            Long wfItemSplitEventId = (Long) db().create(wfItemSplitEvent);

            // Route copies of same item to all branches
            WfTemplateDef wfTemplateDef = flowingWfItem.getWfTemplateDef();
            WfItem wfItem = new WfItem();
            wfItem.setWfItemHistId(flowingWfItem.getWfItemHistId());
            wfItem.setWfItemSplitEventId(wfItemSplitEventId);
            wfItem.setWfItemAttachmentRefId(flowingWfItem.getWfItemAttachmentRefId());
            for (WfBranchDef wfBranchDef : targetWfStepDef.getBranchList()) {
                WfStepDef branchWfStepDef = wfTemplateDef.getWfStepDef(wfBranchDef.getTarget());
                wfItem.setSplitBranchName(wfBranchDef.getName());
                createWfItemAndSubmitToStep(wfItem, flowingWfItem.getWfProcessDef(), branchWfStepDef,
                        flowingWfItem.getBranchCode(), flowingWfItem.getDepartmentCode(), packableDoc);
            }

            // Delete original workflow item excluding attachments
            db().deleteAll(new WfItemPackedDocQuery().wfItemId(wfItemId));
            db().delete(WfItem.class, wfItemId);
        } else if (targetWfStepDef.isMerge()) {
            Long wfItemSplitEventId = flowingWfItem.getWfItemSplitEventId();
            WfItemSplitEvent wfItemSplitEvent = db().find(WfItemSplitEvent.class, wfItemSplitEventId);
            wfItemSplitEvent.setSplitCount(wfItemSplitEvent.getSplitCount() - 1);
            if (wfItemSplitEvent.getSplitCount() == 0) {
                // Perform merge
                List<Long> wfItemIdList = new ArrayList<Long>();
                WfItem mergedWfItem = new WfItem();
                mergedWfItem.setWfItemHistId(flowingWfItem.getWfItemHistId());
                mergedWfItem.setWfItemSplitEventId(wfItemSplitEvent.getParentSplitEventId());
                mergedWfItem.setWfItemAttachmentRefId(flowingWfItem.getWfItemAttachmentRefId());
                mergedWfItem.setSplitBranchName(wfItemSplitEvent.getSplitBranchName());
                mergedWfItem.setStepGlobalName(targetWfStepDef.getGlobalName());
                mergedWfItem.setBranchCode(flowingWfItem.getBranchCode());
                mergedWfItem.setDepartmentCode(flowingWfItem.getDepartmentCode());
                PackableDoc mergedPd = null;
                WfStepDef originWfStepDef = wfSteps.get(targetWfStepDef.getOriginGlobalName());
                for (WfItem wfItem : db().findAll(new WfItemQuery().wfItemSplitEventId(wfItemSplitEventId))) {
                    WfItemPackedDoc wfItemPackedDoc = db().find(WfItemPackedDoc.class, wfItemId);
                    PackableDoc pd =
                            PackableDoc.unpack(flowingWfItem.getWfTemplateDocDef().getWfDocDef().getDocConfig(),
                                    wfItemPackedDoc.getPackedDoc(), false);
                    if (mergedPd == null) {
                        mergedPd = pd;
                    } else {
                        WfBranchDef wfBranchDef = originWfStepDef.getWfBranchDef(wfItem.getSplitBranchName());
                        if (wfBranchDef.isWithMergeFields()) {
                            // Merge packable document
                            mergedPd.merge(pd, wfBranchDef.getMergeFields());
                        }
                    }
                    wfItemIdList.add(wfItem.getId());
                }

                // Get routing for merged item
                FlowingWfItem evalMergeFlowingWfItem =
                        new FlowingWfItem(flowingWfItem.getWfProcessDef(), targetWfStepDef, mergedWfItem, null, null,
                                mergedPd);
                WfStepDef routeToWfStep = resolveWorkflowRouting(targetWfStepDef, evalMergeFlowingWfItem);
                if (routeToWfStep == null) {
                    // Workflow item must be routed on merge
                    throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_MERGE_DOES_NOT_RESOLVE_TO_ROUTE,
                            targetWfStepDef.getGlobalName());
                }

                // Route merged item
                createWfItemAndSubmitToStep(mergedWfItem, evalMergeFlowingWfItem.getWfProcessDef(), routeToWfStep,
                        evalMergeFlowingWfItem.getBranchCode(), evalMergeFlowingWfItem.getDepartmentCode(), mergedPd);

                // Delete pre-merge data
                db().deleteAll(new WfItemPackedDocQuery().wfItemIdIn(wfItemIdList));
                db().deleteAll(new WfItemQuery().idIn(wfItemIdList));
                db().delete(WfItemSplitEvent.class, flowingWfItem.getWfItemSplitEventId());
            } else {
                // Pend merge workflow item
                db().updateById(wfItemSplitEvent);
            }
        } else {
            // Update packable document if changed
            if (packableDoc.isUpdated()) {
                db().updateAll(new WfItemPackedDocQuery().wfItemId(wfItemId),
                        new Update().add("packedDoc", packableDoc.pack()).add("updateDt", db().getNow()));
                packableDoc.clearUpdated();
            }

            // Route item if necessary
            WfStepDef routeToWfStep = resolveWorkflowRouting(targetWfStepDef, flowingWfItem);
            if (routeToWfStep != null) {
                commitTransactions();
                doActualTransition(routeToWfStep, flowingWfItem);
                return;
            }

            // Workflow item has settled in current step
            // Assign to human agent if user actions are associated with current step
            if (targetWfStepDef.isUserInteractive()) {
                assignWorkflowItem(flowingWfItem, targetWfStepDef, true);
            }
        }
    }

    private boolean assignWorkflowItem(FlowingWfItem flowingWfItem, WfStepDef targetWfStepDef,
            final boolean isTransition) throws UnifyException {
        final String origHeldBy = flowingWfItem.getHeldBy();
        String assignee = null;

        // Attempt to assign item to user. Step assigner has priority over document
        // assigner
        String workAssignerName = targetWfStepDef.getWorkAssignerName();
        if (StringUtils.isBlank(workAssignerName)) {
            workAssignerName = flowingWfItem.getWfTemplateDocDef().getWorkAssignerName();
        }

        if (!StringUtils.isBlank(workAssignerName)) {
            // Get step users and their current workload
            List<GroupAggregation> workloadList =
                    db().aggregateGroupMany(new Aggregate().add(AggregateType.COUNT, "heldBy"),
                            new WfItemQuery().stepGlobalName(targetWfStepDef.getGlobalName()).addGroupBy("heldBy"));

            // Fetch users available for step
            Collection<String> availableUsers =
                    wfStepUserInformationProvider.getEligibleUsersForParticipation(
                            flowingWfItem.getReader().getStepParticipant(),
                            flowingWfItem.getReader().getStepGlobalName(),
                            flowingWfItem.getReader().getRestrictions().getBranchCode(),
                            flowingWfItem.getReader().getRestrictions().getDepartmentCode());

            // Evaluate work item assignees
            List<WfItemAssigneeInfo> wfItemAssigneeInfoList = new ArrayList<WfItemAssigneeInfo>();
            for (GroupAggregation workload : workloadList) {
                String userLoginId = (String) workload.getGroupingList().get(0).getValue();
                if (availableUsers.remove(userLoginId)) {
                    wfItemAssigneeInfoList.add(new WfItemAssigneeInfo(userLoginId,
                            (Integer) workload.getAggregationList().get(0).getValue()));
                }
            }

            for (String userLoginId : availableUsers) {
                wfItemAssigneeInfoList.add(new WfItemAssigneeInfo(userLoginId, 0));
            }

            // Do assignment
            WfItemAssignmentPolicy wfItemAssignmentPolicy = (WfItemAssignmentPolicy) getComponent(workAssignerName);
            assignee = wfItemAssignmentPolicy.assignWorkItem(wfItemAssigneeInfoList, flowingWfItem.getReader());
        }

        db().updateAll(new WfItemQuery().id(flowingWfItem.getWfItemId()), new Update().add("heldBy", assignee));
        flowingWfItem.setHeldBy(assignee);

        // Alert user if transition or reassigned
        boolean reAssigned = !DataUtils.equals(origHeldBy, assignee);
        if (isTransition || reAssigned) {
            for (WfAlertDef wfAlertDef : targetWfStepDef.getAlertList()) {
                if (wfAlertDef.isUserInteract()
                        && wfAlertDef.isFireAlertOn(flowingWfItem.getDocName(), flowingWfItem.getPrevStepName())) {
                    wfItemAlertLogic.sendAlert(flowingWfItem.getReader(), wfAlertDef);
                }
            }
        }

        return reAssigned;
    }

    private WfStepDef resolveWorkflowRouting(WfStepDef currWfStepDef, FlowingWfItem flowingWfItem)
            throws UnifyException {
        if (DataUtils.isNotBlank(currWfStepDef.getRoutingList())) {
            for (WfRoutingDef wfRoutingDef : currWfStepDef.getRoutingList()) {
                if (!wfRoutingDef.isDoc() || wfRoutingDef.getDocName().equals(flowingWfItem.getDocName())) {
                    if (tryRoute(flowingWfItem.getReader(), wfRoutingDef)) {
                        WfStepDef routeToWfStep = wfSteps.get(wfRoutingDef.getTargetGlobalName());
                        if (routeToWfStep.isStart() || currWfStepDef.isManual()) {
                            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_ITEM_NOT_ROUTE_START,
                                    wfTemplates.get(currWfStepDef.getTemplateGlobalName()).getDescription(),
                                    currWfStepDef.getDescription());
                        }
                        return routeToWfStep;
                    }
                }
            }
        }
        return null;
    }

    private boolean tryRoute(FlowingWfItem.Reader flowingWfItemReader, WfRoutingDef wfRoutingDef)
            throws UnifyException {
        WfDocClassifierDef wfDocClassifierDef = wfRoutingDef.getClassifier();
        if (wfDocClassifierDef != null) {
            WfItemClassifierLogic workflowDocClassifierLogic =
                    (WfItemClassifierLogic) getComponent(wfDocClassifierDef.getLogic());
            return workflowDocClassifierLogic.match(flowingWfItemReader, wfDocClassifierDef);
        }

        return true;
    }

    private WfStepDef accessCurrentUserStep(String stepGlobalName) throws UnifyException {
        WfStepDef wfStepDef = wfSteps.get(stepGlobalName);
        if (!getUserToken().isReservedUser()) {
            Set<String> stepNames = getCurrentUserRoleStepCodes();
            if (!stepNames.contains(stepGlobalName)) {
                throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_CURRENT_USER_NOT_PARTICIPANT,
                        getUserToken().getUserName(), wfStepDef.getDescription());
            }
        }

        return wfStepDef;
    }

    private WfItemQuery getCurrentUserParticipationWfItemQuery(WfStepDef wfStepDef) throws UnifyException {
        WfItemQuery wfItemQuery = new WfItemQuery().stepGlobalName(wfStepDef.getGlobalName());
        UserToken userToken = getUserToken();
        if (!userToken.isReservedUser()) {
            Boolean supervisor = (Boolean) this.getSessionAttribute(JacklynSessionAttributeConstants.SUPERVISORFLAG);
            if (Boolean.TRUE.equals(supervisor)) {
                wfItemQuery.allOrParticipantType(WorkflowParticipantType.SUPERVISOR);
            } else {
                wfItemQuery.allOrParticipantType(WorkflowParticipantType.PERSONNEL);
            }

            if (wfStepDef.isBranchOnly()) {
                wfItemQuery.branchCode(userToken.getBranchCode());
            }

            if (wfStepDef.isDepartmentOnly()) {
                wfItemQuery.departmentCode(userToken.getDepartmentCode());
            }
        }

        return wfItemQuery;
    }

    private boolean checkWfTemplateStale(String templateGlobalName, long currentTimeStamp) throws UnifyException {
        boolean stale = false;
        try {
            TemplateNameParts templateNames = WfNameUtils.getTemplateNameParts(templateGlobalName);
            Date updateDt =
                    db().value(Date.class, "wfCategoryUpdateDt",
                            new WfTemplateQuery().wfCategoryName(templateNames.getCategoryName())
                                    .name(templateNames.getTemplateName()).wfCategoryStatus(RecordStatus.ACTIVE));
            stale = resolveUTC(updateDt) != currentTimeStamp;
        } catch (Exception e) {
            logError(e);
        }

        return stale;
    }

    private WfDocDef getWfItemWfDocDef(Long wfItemId) throws UnifyException {
        String processGlobalName = db().value(String.class, "processGlobalName", new WfItemQuery().id(wfItemId));
        ProcessNameParts processNameParts = WfNameUtils.getProcessNameParts(processGlobalName);
        return wfDocs.get(processNameParts.getDocGlobalName());
    }

    private class ComplexFieldConfigBuilderInfo {

        private PackableDocConfig.Builder cpdcb;

        private boolean list;

        public ComplexFieldConfigBuilderInfo(Builder cpdcb, boolean list) {
            this.cpdcb = cpdcb;
            this.list = list;
        }

        public PackableDocConfig.Builder getCpdcb() {
            return cpdcb;
        }

        public boolean isList() {
            return list;
        }
    }
}
