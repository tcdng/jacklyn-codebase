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
package com.tcdng.jacklyn.workflow.business;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.common.constants.JacklynSessionAttributeConstants;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.notification.business.NotificationService;
import com.tcdng.jacklyn.notification.entities.NotificationTemplate;
import com.tcdng.jacklyn.notification.utils.NotificationUtils;
import com.tcdng.jacklyn.shared.workflow.WorkflowApplyActionTaskConstants;
import com.tcdng.jacklyn.shared.workflow.WorkflowCategoryBinaryPublicationTaskConstants;
import com.tcdng.jacklyn.shared.workflow.WorkflowCategoryPublicationTaskConstants;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.shared.workflow.data.ToolingEnrichmentLogicItem;
import com.tcdng.jacklyn.shared.workflow.data.ToolingItemClassifierLogicItem;
import com.tcdng.jacklyn.shared.workflow.data.ToolingPolicyLogicItem;
import com.tcdng.jacklyn.shared.workflow.data.ToolingWfDocUplGeneratorItem;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfAlertConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfAttachmentCheckConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfAttachmentConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfBeanMappingConfig;
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
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleErrorConstants;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem;
import com.tcdng.jacklyn.workflow.data.InteractWfItems;
import com.tcdng.jacklyn.workflow.data.ManualInitInfo;
import com.tcdng.jacklyn.workflow.data.ManualWfItem;
import com.tcdng.jacklyn.workflow.data.WfAction;
import com.tcdng.jacklyn.workflow.data.WfAlertDef;
import com.tcdng.jacklyn.workflow.data.WfAttachmentCheckDef;
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
import com.tcdng.jacklyn.workflow.data.WfItemAttachmentInfo;
import com.tcdng.jacklyn.workflow.data.WfItemHistEvent;
import com.tcdng.jacklyn.workflow.data.WfItemHistory;
import com.tcdng.jacklyn.workflow.data.WfItemSummary;
import com.tcdng.jacklyn.workflow.data.WfManualInitDef;
import com.tcdng.jacklyn.workflow.data.WfPolicyDef;
import com.tcdng.jacklyn.workflow.data.WfProcessDef;
import com.tcdng.jacklyn.workflow.data.WfRecordActionDef;
import com.tcdng.jacklyn.workflow.data.WfRoutingDef;
import com.tcdng.jacklyn.workflow.data.WfStepDef;
import com.tcdng.jacklyn.workflow.data.WfTemplateDef;
import com.tcdng.jacklyn.workflow.data.WfTemplateDocDef;
import com.tcdng.jacklyn.workflow.data.WfTemplateLargeData;
import com.tcdng.jacklyn.workflow.data.WfUserActionDef;
import com.tcdng.jacklyn.workflow.entities.WfAlert;
import com.tcdng.jacklyn.workflow.entities.WfAttachmentCheck;
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
import com.tcdng.unify.core.annotation.Taskable;
import com.tcdng.unify.core.annotation.TransactionAttribute;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.business.GenericService;
import com.tcdng.unify.core.constant.FrequencyUnit;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.BeanMappingConfig;
import com.tcdng.unify.core.data.Document;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.PackableDoc;
import com.tcdng.unify.core.data.PackableDocConfig;
import com.tcdng.unify.core.data.PackableDocConfig.Builder;
import com.tcdng.unify.core.system.SequenceNumberService;
import com.tcdng.unify.core.task.TaskExecLimit;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.StringUtils.StringToken;
import com.tcdng.unify.core.util.ThreadUtils;

/**
 * Default workflow business service implementation.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(WorkflowModuleNameConstants.WORKFLOWSERVICE)
public class WorkflowServiceImpl extends AbstractJacklynBusinessService implements WorkflowService {

    private static final String DEFAULT_WFDOCGENERATOR = "wfsingleformviewer-generator";

    private static final String WORKFLOW_SUBMISSION_ID_SEQUENCE = "wfsubmissionid-sequence";

    @Configurable
    private SystemService systemService;

    @Configurable
    private NotificationService notificationService;

    @Configurable
    private GenericService genericService;

    @Configurable
    private SequenceNumberService sequenceNumberService;

    @Configurable(WorkflowModuleNameConstants.DEFAULTWORKFLOWITEMALERTLOGIC)
    private WfItemAlertLogic defWfItemAlertLogic;

    @Configurable(WorkflowModuleNameConstants.DEFAULTWORKFLOWUSERASSIGNMENTPOLICY)
    private WfItemUserAssignmentPolicy defWfItemUserAssignmentPolicy;

    private FactoryMap<String, WfDocDef> wfDocs;

    private FactoryMap<String, WfTemplateDef> wfTemplates;

    private FactoryMap<String, WfStepDef> wfSteps;

    private FactoryMap<String, WfProcessDef> wfProcesses;

    private Queue<FlowingWfItemTransition> pendingWfItemTransitionQueue;

    private Set<Long> pendingSubmissionIds;

    public WorkflowServiceImpl() {
        pendingWfItemTransitionQueue = new ConcurrentLinkedQueue<FlowingWfItemTransition>();
        pendingSubmissionIds = ConcurrentHashMap.newKeySet();

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
                            (Class<? extends Document>) ReflectUtils.getClassForName(wfDocBeanMapping.getBeanType());
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
                            wfTemplateDoc.getAssignmentPolicy(), wfTemplateDoc.getManual()));
                }

                // Steps
                List<WfStepDef> stepList = new ArrayList<WfStepDef>();
                for (WfStep wfStep : wfTemplate.getStepList()) {
                    String stepGlobalName =
                            WfNameUtils.getStepGlobalName(templateNames.getCategoryName(),
                                    templateNames.getTemplateName(), wfStep.getName());

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
                            alertList.add(new WfAlertDef(wfAlert.getDocName(), stepGlobalName, wfAlert.getName(),
                                    wfAlert.getDescription(), wfAlert.getType(), wfAlert.getParticipant(),
                                    wfAlert.getChannel(), notifTemplateGlobalName));
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
                    long expiryMilliSec =
                            CalendarUtils.getMilliSecondsByFrequency(FrequencyUnit.HOUR, wfStep.getExpiryHours());
                    stepList.add(new WfStepDef(wfTemplateId, templateGlobalName, stepGlobalName, wfStep.getName(),
                            wfStep.getDescription(), wfStep.getLabel(), wfStep.getStepType(),
                            wfStep.getParticipantType(), enrichmentList, routingList, recordActionList, userActionList,
                            formPrivilegeList, alertList, policyList, wfStep.getItemsPerSession(), expiryMilliSec,
                            wfStep.getAudit(), wfStep.getBranchOnly(), wfStep.getDepartmentOnly(),
                            wfStep.getIncludeForwarder(), templateTimestamp));
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
        return db().listAll(new WfTemplateQuery().wfCategoryId(wfCategoryId).order("description"));
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

        query.select("id", "name", "description", "wfCategoryName", "wfCategoryDesc");
        query.wfCategoryStatus(RecordStatus.ACTIVE);
        query.manualOption(Boolean.TRUE);
        query.order("wfCategoryDesc", "description");
        List<WfTemplate> templateList = db().listAll(query);

        if (!templateList.isEmpty()) {
            List<ManualInitInfo> resultList = new ArrayList<ManualInitInfo>();
            for (WfTemplate wfTemplate : templateList) {
                for (WfTemplateDoc wfTemplateDoc : db()
                        .findAll(new WfTemplateDocQuery().wfTemplateId(wfTemplate.getId()).manual(Boolean.TRUE))) {
                    String processGlobalName =
                            WfNameUtils.getProcessGlobalName(wfTemplate.getWfCategoryName(), wfTemplate.getName(),
                                    wfTemplateDoc.getWfDocName());
                    String processDesc =
                            String.format("%s::%s", wfTemplate.getDescription(), wfTemplateDoc.getWfDocName());
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

        List<WfEnrichment> enrichmentList = new ArrayList<WfEnrichment>();
        List<WfRouting> routingList = new ArrayList<WfRouting>();
        List<WfRecordAction> recordActionList = new ArrayList<WfRecordAction>();
        List<WfUserAction> userActionList = new ArrayList<WfUserAction>();
        List<WfFormPrivilege> formPrivilegeList = new ArrayList<WfFormPrivilege>();
        List<WfPolicy> policyList = new ArrayList<WfPolicy>();
        List<WfAlert> alertList = new ArrayList<WfAlert>();
        for (WfStep wfStep : stepList) {
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

        return new WfTemplateLargeData(wfTemplate, stepList, enrichmentList, routingList, recordActionList,
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
        boolean present;
        do {
            present = false;
            for (Long id : submissionId) {
                present = pendingSubmissionIds.contains(id);
                if (present) {
                    break;
                }
            }

            if (present) {
                ThreadUtils.sleep(250);
            }
        } while (present);
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
            wfItemQuery.limit(wfStepDef.getItemsPerSession());
        }
        db().updateAll(wfItemQuery, new Update().add("heldBy", userLoginID));

        wfItemQuery.clear();
        wfItemQuery.stepGlobalName(wfStepDef.getGlobalName());
        wfItemQuery.heldBy(userLoginID);
        return db().valueList(Long.class, "id", wfItemQuery);
    }

    @Override
    public int releaseCurrentUserWorkItems(String stepGlobalName, List<Long> wfItemIds) throws UnifyException {
        WfStepDef wfStepDef = accessCurrentUserStep(stepGlobalName);
        WfItemQuery wfItemQuery = getCurrentUserParticipationWfItemQuery(wfStepDef);
        wfItemQuery.idIn(wfItemIds);
        wfItemQuery.heldBy(getUserToken().getUserLoginId());
        return db().updateAll(wfItemQuery, new Update().add("heldBy", null));
    }

    @Override
    public InteractWfItems getCurrentUserWorkItems(String stepGlobalName) throws UnifyException {
        WfStepDef wfStepDef = accessCurrentUserStep(stepGlobalName);
        String useLoginID = getUserToken().getUserLoginId();
        List<WfItem> wfItemList =
                db().listAll(new WfItemQuery().stepGlobalName(wfStepDef.getGlobalName()).heldBy(useLoginID));

        List<WfAction> actions = new ArrayList<WfAction>();
        for (WfUserActionDef wfUserActionDef : wfStepDef.getUserActionList()) {
            actions.add(new WfAction(wfUserActionDef.getName(), resolveSessionMessage(wfUserActionDef.getLabel()),
                    wfUserActionDef.getCommentReqType(), wfUserActionDef.isValidatePage()));
        }

        return new InteractWfItems(stepGlobalName, wfItemList, actions);
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
                    int holdCount = db().countAll(wfItemQuery.isHeld());
                    String description = getSessionMessage("workflowitem.summary", stepDesc, itemCount, holdCount);
                    summaryList.add(new WfItemSummary(description, stepGlobalName, stepDesc, itemCount, holdCount));
                }
            }

            if (!summaryList.isEmpty()) {
                DataUtils.sort(summaryList, WfItemSummary.class, "stepDesc", true);
                return summaryList;
            }
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

            // Push to transition queue
            Long submissionId = sequenceNumberService.getCachedBlockNextSequenceNumber(WORKFLOW_SUBMISSION_ID_SEQUENCE);
            pushIntoWfItemTransitionQueue(submissionId, trgWfStep, flowingWfItem);
            return submissionId;
        }

        throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_APPLY_ACTION_UNHELD, actionName,
                flowingWfItem.getDescription());
    }

    @Override
    public FlowingWfItem findWorkflowItem(Long wfItemId) throws UnifyException {
        WfItem wfItem = db().list(WfItem.class, wfItemId);
        return findFlowingWfItem(wfItem);
    }

    @Override
    public FlowingWfItem findWorkflowItemBySubmission(Long submissionId) throws UnifyException {
        WfItem wfItem = db().list(new WfItemQuery().submissionId(submissionId));
        if (wfItem != null) {
            return findFlowingWfItem(wfItem);
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

            eventList.add(new WfItemHistEvent(wfHistEvent.getId(), wfHistEvent.getWfStepName(), wfHistEvent.getStepDt(),
                    wfHistEvent.getActionDt(), wfHistEvent.getActor(), wfHistEvent.getWfAction(), actionDesc,
                    wfHistEvent.getComment()));
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

        WfItemAttachment wfItemAttachment =
                db().find(new WfItemAttachmentQuery().wfItemId(wfItemId).name(attachment.getName()).select("wfItemId",
                        "name"));
        if (wfItemAttachment == null) {
            wfItemAttachment = new WfItemAttachment();
            wfItemAttachment.setWfItemId(wfItemId);
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
            WfItemAttachmentQuery query = new WfItemAttachmentQuery();
            query.wfItemId(wfItemId);
            if (attributesOnly) {
                query.select("name", "fileName");
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
        WfItemAttachment wfItemAttachment = db().find(new WfItemAttachmentQuery().wfItemId(wfItemId).name(name));
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
        return db().deleteAll(new WfItemAttachmentQuery().wfItemId(wfItemId).name(name));
    }

    @Override
    public List<ToolingItemClassifierLogicItem> findToolingItemClassifierLogicTypes() throws UnifyException {
        return getToolingTypes(ToolingItemClassifierLogicItem.class, WfItemClassifierLogic.class);
    }

    @Override
    public List<ToolingEnrichmentLogicItem> findToolingEnrichmentLogicTypes() throws UnifyException {
        return getToolingTypes(ToolingEnrichmentLogicItem.class, WfItemEnrichmentLogic.class);
    }

    @Override
    public List<ToolingPolicyLogicItem> findToolingPolicyLogicTypes() throws UnifyException {
        return getToolingTypes(ToolingPolicyLogicItem.class, WfItemProcessPolicy.class);
    }

    @Override
    public List<ToolingWfDocUplGeneratorItem> findToolingWfDocUplGeneratorTypes() throws UnifyException {
        return getToolingTypes(ToolingWfDocUplGeneratorItem.class, WfDocUplGenerator.class);
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

    @Override
    public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {

    }

    @Override
    public void clearCache() throws UnifyException {
        wfDocs.clear();
        wfTemplates.clear();
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

    @Periodic(PeriodicType.EXTREME)
    public void performFlowingWfItemTransitions(TaskMonitor taskMonitor) throws UnifyException {
        FlowingWfItemTransition wfItemTransition = null;
        while ((wfItemTransition = pendingWfItemTransitionQueue.poll()) != null) {
            FlowingWfItem flowingWfItem = wfItemTransition.getFlowingWfItem();
            WfStepDef targetWfStepDef = wfItemTransition.getTargetWfStepDef();
            try {
                performFlowingWfItemTransition(targetWfStepDef, flowingWfItem);
            } catch (Exception e) {
                try {
                    // Push workflow item to error step
                    String errorMsg = getExceptionMessage(LocaleType.APPLICATION, e);
                    flowingWfItem.setSourceWfStepDef(targetWfStepDef);
                    flowingWfItem.setErrorMsg(errorMsg);
                    performFlowingWfItemTransition(flowingWfItem.getErrorWfStepDef(), flowingWfItem);
                } catch (Exception e1) {
                    logError(e1);
                }
            } finally {
                pendingSubmissionIds.remove(wfItemTransition.getSubmissionId());
            }
        }
    }

    @Transactional(TransactionAttribute.REQUIRES_NEW)
    public void performFlowingWfItemTransition(WfStepDef targetWfStepDef, FlowingWfItem flowingWfItem)
            throws UnifyException {
        doActualTransition(targetWfStepDef, flowingWfItem);
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
            wfTemplateDoc.setAssignmentPolicy(wfTemplateDocConfig.getAssigner());
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
            wfStep.setStepType(wfStepConfig.getType());
            wfStep.setPriorityLevel(wfStepConfig.getPriority());
            wfStep.setParticipantType(wfStepConfig.getParticipant());
            wfStep.setItemsPerSession(wfStepConfig.getItemsPerSession());
            wfStep.setExpiryHours(wfStepConfig.getExpiryHours());
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
                    wfAlert.setType(wfAlertConfig.getType());
                    wfAlert.setParticipant(wfAlertConfig.getParticipant());
                    wfAlert.setChannel(wfAlertConfig.getChannel());
                    wfAlert.setNotificationTemplateCode(wfAlertConfig.getMessage());
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

    private FlowingWfItem findFlowingWfItem(WfItem wfItem) throws UnifyException {
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
                getSessionMessage("workflowitem.title.struct", wfTemplateDef.getName(), wfStepDef.getName(),
                        wfTemplateDocDef.getWfDocDef().getName(), wfItem.getDescription());

        // Create work item
        FlowingWfItem flowingWfItem =
                new FlowingWfItem(wfStepDef, wfTemplateDef.getErrorStep(), wfTemplateDocDef,
                        wfItem.getProcessGlobalName(), wfItem.getBranchCode(), wfItem.getDepartmentCode(), wfItemId,
                        wfItem.getWfItemHistId(), wfItem.getWfHistEventId(), wfItem.getDescription(), title,
                        wfItem.getCreateDt(), wfItem.getStepDt(), wfItem.getHeldBy(), pd);

        return flowingWfItem;
    }

    private Long submitToReceptacle(WfProcessDef wfProcessDef, WfStepDef trgWfStepDef, String branchCode,
            String departmentCode, PackableDoc packableDoc) throws UnifyException {
        if (!trgWfStepDef.isStart() && !trgWfStepDef.isManual()) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_SUBMIT_NONSTART_NONRECEPTACLE,
                    trgWfStepDef.getGlobalName());
        }

        // Create workflow item
        Long submissionId = sequenceNumberService.getCachedBlockNextSequenceNumber(WORKFLOW_SUBMISSION_ID_SEQUENCE);
        WfItem wfItem = new WfItem();
        wfItem.setSubmissionId(submissionId);
        wfItem.setStepGlobalName(trgWfStepDef.getGlobalName());
        wfItem.setBranchCode(branchCode);
        wfItem.setDepartmentCode(departmentCode);
        wfItem.setInitiatedBy(getUserToken().getUserLoginId());
        wfItem.setCreateDt(db().getNow());
        Long wfItemId = (Long) db().create(wfItem);

        WfItemPackedDoc wfItemPackedDoc = new WfItemPackedDoc();
        wfItemPackedDoc.setWfItemId(wfItemId);
        wfItemPackedDoc.setPackedDoc(packableDoc.pack());
        db().create(wfItemPackedDoc);
        packableDoc.clearUpdated();

        // Push to step
        WfTemplateDocDef wfTemplateDocDef = wfProcessDef.getWfTemplateDocDef();
        String description = WfNameUtils.describe(wfTemplateDocDef.getWfDocDef().getItemDescFormat(), packableDoc);
        FlowingWfItem flowingWfItem =
                new FlowingWfItem(trgWfStepDef, wfProcessDef.getWfTemplateDef().getErrorStep(), wfTemplateDocDef,
                        wfProcessDef.getGlobalName(), branchCode, departmentCode, wfItemId, null, null, description,
                        null, wfItem.getCreateDt(), wfItem.getStepDt(), wfItem.getHeldBy(), packableDoc);

        // Asynchronous processing. Push transition to queue.
        pushIntoWfItemTransitionQueue(submissionId, trgWfStepDef, flowingWfItem);
        return submissionId;
    }

    private void pushIntoWfItemTransitionQueue(Long submissionId, WfStepDef targetWfStepDef,
            FlowingWfItem flowingWfItem) throws UnifyException {
        pendingSubmissionIds.add(submissionId);
        pendingWfItemTransitionQueue.offer(new FlowingWfItemTransition(submissionId, targetWfStepDef, flowingWfItem));
    }

    private void doActualTransition(final WfStepDef targetWfStepDef, final FlowingWfItem flowingWfItem)
            throws UnifyException {
        // Flow workflow item into target step
        flowingWfItem.setWfStepDef(targetWfStepDef);

        // Create history
        Date stepDt = db().getNow();
        Long wfItemHistId = flowingWfItem.getWfItemHistId();
        if (wfItemHistId == null && (targetWfStepDef.isStart() || targetWfStepDef.isManual())) {
            WfItemHist wfHist = new WfItemHist();
            wfHist.setProcessGlobalName(flowingWfItem.getProcessGlobalName());
            wfHist.setDescription(flowingWfItem.getDescription());
            wfHist.setDocId(flowingWfItem.getDocId());
            wfItemHistId = (Long) db().create(wfHist);
        }

        Date expectedDt = null;
        if (targetWfStepDef.isExpiry()) {
            expectedDt = CalendarUtils.getDateWithOffset(stepDt, targetWfStepDef.getExpiryMilliSec());
        }

        WfItemEvent wfHistEvent = new WfItemEvent();
        wfHistEvent.setWfItemHistId(wfItemHistId);
        wfHistEvent.setWfStepName(targetWfStepDef.getName());
        wfHistEvent.setStepDt(stepDt);
        wfHistEvent.setExpectedDt(expectedDt);
        Long wfHistEventId = (Long) db().create(wfHistEvent);

        // Update workflow item information.
        UserToken userToken = getUserToken();
        String forwardedBy = null;
        if (!userToken.isReservedUser()) {
            forwardedBy = userToken.getUserLoginId();
        }

        PackableDoc packableDoc = flowingWfItem.getPd();
        final Long wfItemId = flowingWfItem.getWfItemId();
        flowingWfItem.setWfItemHistId(wfItemHistId);
        flowingWfItem.setWfHistEventId(wfHistEventId);
        db().updateById(WfItem.class, wfItemId,
                new Update().add("wfHistEventId", wfHistEventId).add("stepGlobalName", targetWfStepDef.getGlobalName())
                        .add("stepDt", stepDt).add("expectedDt", expectedDt)
                        .add("participantType", targetWfStepDef.getParticipantType()).add("forwardedBy", forwardedBy));

        // Perform enrichment if any
        String docName = flowingWfItem.getDocName();
        for (WfEnrichmentDef wfEnrichmentDef : targetWfStepDef.getEnrichmentList()) {
            WfItemEnrichmentLogic wfItemEnrichmentLogic =
                    (WfItemEnrichmentLogic) getComponent(wfEnrichmentDef.getLogic());
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
            if (wfAlertDef.isPassThrough() && wfAlertDef.getDocName().equals(docName)) {
                defWfItemAlertLogic.sendAlert(flowingWfItemReader, wfAlertDef);
            }
        }

        // Check for termination
        if (targetWfStepDef.isEnd()) {
            // Delete workflow item
            db().deleteAll(new WfItemPackedDocQuery().wfItemId(wfItemId));
            db().deleteAll(new WfItemAttachmentQuery().wfItemId(wfItemId));
            db().deleteAll(new WfItemQuery().id(wfItemId));
        } else {
            // Route item if necessary
            if (DataUtils.isNotBlank(targetWfStepDef.getRoutingList())) {
                for (WfRoutingDef wfRoutingDef : targetWfStepDef.getRoutingList()) {
                    if (!wfRoutingDef.isDoc() || wfRoutingDef.getDocName().equals(docName)) {
                        if (tryRoute(flowingWfItemReader, wfRoutingDef)) {
                            WfStepDef trgStep = wfSteps.get(wfRoutingDef.getTargetGlobalName());
                            if (trgStep.isStart() || targetWfStepDef.isManual()) {
                                throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_ITEM_NOT_ROUTE_START,
                                        wfTemplates.get(targetWfStepDef.getTemplateGlobalName()).getDescription(),
                                        targetWfStepDef.getDescription());
                            }

                            doActualTransition(trgStep, flowingWfItem);
                            return;
                        }
                    }
                }
            }

            // Workflow item has settled in current step
            // Commit if packable document has changed
            if (packableDoc.isUpdated()) {
                db().updateAll(new WfItemPackedDocQuery().wfItemId(wfItemId),
                        new Update().add("packedDoc", packableDoc.pack()).add("updateDt", db().getNow()));
                packableDoc.clearUpdated();
            }

            // Assign to human agent if user actions are associated with current step
            if (targetWfStepDef.isUserInteractive()) {
                WfItemUserAssignmentPolicy wfItemUserAssignmentPolicy = defWfItemUserAssignmentPolicy;
                if (flowingWfItem.getWfTemplateDocDef().isWithAssignmentPolicy()) {
                    wfItemUserAssignmentPolicy =
                            (WfItemUserAssignmentPolicy) getComponent(
                                    flowingWfItem.getWfTemplateDocDef().getAssignmentPolicyName());
                }

                String heldBy = wfItemUserAssignmentPolicy.execute(flowingWfItemReader);
                db().updateAll(new WfItemQuery().id(wfItemId), new Update().add("heldBy", heldBy));

                // Alert user
                flowingWfItem.setHeldBy(heldBy);
                for (WfAlertDef wfAlertDef : targetWfStepDef.getAlertList()) {
                    if (wfAlertDef.isUserInteract() && wfAlertDef.getDocName().equals(docName)) {
                        defWfItemAlertLogic.sendAlert(flowingWfItemReader, wfAlertDef);
                    }
                }
            }

        }
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

    private class FlowingWfItemTransition {

        private Long submissionId;

        private WfStepDef targetWfStepDef;

        private FlowingWfItem flowingWfItem;

        public FlowingWfItemTransition(Long submissionId, WfStepDef targetWfStepDef, FlowingWfItem flowingWfItem) {
            this.submissionId = submissionId;
            this.targetWfStepDef = targetWfStepDef;
            this.flowingWfItem = flowingWfItem;
        }

        public Long getSubmissionId() {
            return submissionId;
        }

        public WfStepDef getTargetWfStepDef() {
            return targetWfStepDef;
        }

        public FlowingWfItem getFlowingWfItem() {
            return flowingWfItem;
        }
    }
}
