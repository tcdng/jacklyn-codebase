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
import java.util.Set;

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
import com.tcdng.jacklyn.shared.workflow.data.ToolingPolicyLogicItem;
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
import com.tcdng.jacklyn.shared.xml.config.workflow.WfUserActionConfig;
import com.tcdng.jacklyn.shared.xml.util.WfCategoryConfigUtils;
import com.tcdng.jacklyn.shared.xml.util.WfNameUtils;
import com.tcdng.jacklyn.shared.xml.util.WfNameUtils.DocNameParts;
import com.tcdng.jacklyn.shared.xml.util.WfNameUtils.StepNameParts;
import com.tcdng.jacklyn.shared.xml.util.WfNameUtils.TemplateNameParts;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleErrorConstants;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.jacklyn.workflow.data.ManualWfItem;
import com.tcdng.jacklyn.workflow.data.WfAction;
import com.tcdng.jacklyn.workflow.data.WfAlertDef;
import com.tcdng.jacklyn.workflow.data.WfAttachmentCheckDef;
import com.tcdng.jacklyn.workflow.data.WfDocAttachmentDef;
import com.tcdng.jacklyn.workflow.data.WfDocBeanMappingDef;
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
import com.tcdng.jacklyn.workflow.data.WfItemHistObject;
import com.tcdng.jacklyn.workflow.data.WfItemObject;
import com.tcdng.jacklyn.workflow.data.WfItemObjects;
import com.tcdng.jacklyn.workflow.data.WfItemSummary;
import com.tcdng.jacklyn.workflow.data.WfManualInitDef;
import com.tcdng.jacklyn.workflow.data.WfPolicyDef;
import com.tcdng.jacklyn.workflow.data.WfRecordActionDef;
import com.tcdng.jacklyn.workflow.data.WfRoutingDef;
import com.tcdng.jacklyn.workflow.data.WfStepDef;
import com.tcdng.jacklyn.workflow.data.WfTemplateDef;
import com.tcdng.jacklyn.workflow.data.WfTemplateLargeData;
import com.tcdng.jacklyn.workflow.data.WfUserActionDef;
import com.tcdng.jacklyn.workflow.entities.WfAlert;
import com.tcdng.jacklyn.workflow.entities.WfAttachmentCheck;
import com.tcdng.jacklyn.workflow.entities.WfCategory;
import com.tcdng.jacklyn.workflow.entities.WfCategoryQuery;
import com.tcdng.jacklyn.workflow.entities.WfDoc;
import com.tcdng.jacklyn.workflow.entities.WfDocAttachment;
import com.tcdng.jacklyn.workflow.entities.WfDocBeanMapping;
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
import com.tcdng.jacklyn.workflow.entities.WfTemplateQuery;
import com.tcdng.jacklyn.workflow.entities.WfUserAction;
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Taskable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.business.GenericService;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.data.Document;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.PackableDoc;
import com.tcdng.unify.core.data.PackableDocConfig;
import com.tcdng.unify.core.data.PackableDocConfig.FieldConfig;
import com.tcdng.unify.core.data.PackableDocRWConfig;
import com.tcdng.unify.core.operation.Update;
import com.tcdng.unify.core.task.TaskExecLimit;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.upl.UplUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.StringUtils.StringToken;

/**
 * Default workflow business service implementation.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(WorkflowModuleNameConstants.WORKFLOWSERVICE)
public class WorkflowServiceImpl extends AbstractJacklynBusinessService implements WorkflowService {

    @Configurable
    private SystemService systemService;

    @Configurable
    private NotificationService notificationService;

    @Configurable
    private GenericService genericService;

    @Configurable(WorkflowModuleNameConstants.DEFAULTWORKFLOWITEMALERTLOGIC)
    private WfItemAlertLogic wfItemAlertLogic;

    private FactoryMap<String, WfDocDef> wfDocs;

    private FactoryMap<String, WfTemplateDef> wfTemplates;

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
                    stale = !updateDt.equals(wfDocDef.getTimestamp());
                } catch (Exception e) {
                    logError(e);
                }

                return stale;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected WfDocDef create(String globalDocName, Object... params) throws Exception {
                DocNameParts docNames = WfNameUtils.getDocNameParts(globalDocName);
                WfDoc wfDoc =
                        db().list(new WfDocQuery().wfCategoryName(docNames.getCategoryName())
                                .name(docNames.getDocName()).wfCategoryStatus(RecordStatus.ACTIVE));
                if (wfDoc == null) {
                    throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_DOCUMENT_WITH_NAME_UNKNOWN,
                            globalDocName);
                }

                Long wfDocId = wfDoc.getId();
                // Fields
                List<FieldConfig> fieldConfigList = new ArrayList<FieldConfig>();
                int size = wfDoc.getFieldList().size();
                for (int i = 0; i < size; i++) {
                    WfDocField wfDocField = wfDoc.getFieldList().get(i);
                    DataType dataType = wfDocField.getDataType();
                    if (DataType.COMPLEX.equals(dataType)) {
                        List<FieldConfig> subFieldConfigList = new ArrayList<FieldConfig>();
                        for (i++; i < size; i++) {
                            WfDocField subWfDocField = wfDoc.getFieldList().get(i);
                            if (subWfDocField.getParentName() == null) {
                                i--;
                                break;
                            }

                            subFieldConfigList.add(new FieldConfig(subWfDocField.getName(), subWfDocField.getDataType(),
                                    subWfDocField.getRepeat()));
                        }

                        fieldConfigList
                                .add(new FieldConfig(wfDocField.getName(), wfDocField.getRepeat(), subFieldConfigList));
                    } else {
                        fieldConfigList.add(new FieldConfig(wfDocField.getName(), dataType, wfDocField.getRepeat()));
                    }
                }

                // Bean mappings
                List<WfDocBeanMappingDef> beanMappingList = new ArrayList<WfDocBeanMappingDef>();
                for (WfDocBeanMapping wfDocBeanMapping : wfDoc.getBeanMappingList()) {
                    PackableDocRWConfig.FieldMapping[] fieldMappings =
                            new PackableDocRWConfig.FieldMapping[wfDocBeanMapping.getFieldMappingList().size()];
                    for (int i = 0; i < fieldMappings.length; i++) {
                        WfDocFieldMapping wfDocFieldMapping = wfDocBeanMapping.getFieldMappingList().get(i);
                        fieldMappings[i] =
                                new PackableDocRWConfig.FieldMapping(wfDocFieldMapping.getDocFieldName(),
                                        wfDocFieldMapping.getBeanFieldName());
                    }

                    Class<? extends Document> beanType =
                            (Class<? extends Document>) ReflectUtils.getClassForName(wfDocBeanMapping.getBeanType());
                    beanMappingList
                            .add(new WfDocBeanMappingDef(wfDocBeanMapping.getName(), wfDocBeanMapping.getDescription(),
                                    new PackableDocRWConfig(beanType, fieldMappings), wfDocBeanMapping.getType()));
                }

                // Attachments
                List<WfDocAttachmentDef> attachmentList = null;
                if (!DataUtils.isBlank(wfDoc.getAttachmentList())) {
                    attachmentList = new ArrayList<WfDocAttachmentDef>();
                    for (WfDocAttachment wfDocAttachment : wfDoc.getAttachmentList()) {
                        attachmentList
                                .add(new WfDocAttachmentDef(wfDocAttachment.getName(), wfDocAttachment.getDescription(),
                                        wfDocAttachment.getLabel(), wfDocAttachment.getAttachmentType()));
                    }
                }

                // Classifiers
                List<WfDocClassifierDef> classifierList = null;
                if (!DataUtils.isBlank(wfDoc.getClassifierList())) {
                    classifierList = new ArrayList<WfDocClassifierDef>();
                    for (WfDocClassifier wfDocClassifier : wfDoc.getClassifierList()) {
                        List<WfDocClassifierFilterDef> filterList = null;
                        if (!DataUtils.isBlank(wfDocClassifier.getFilterList())) {
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
                return new WfDocDef(wfDocId, globalDocName, wfDoc.getName(), wfDoc.getDescription(),
                        new PackableDocConfig(globalDocName, fieldConfigList), wfDoc.getWfCategoryUpdateDt(), wfFormDef,
                        itemDescFormat, beanMappingList, attachmentList, classifierList);
            }

        };

        wfTemplates = new FactoryMap<String, WfTemplateDef>(true) {

            @Override
            protected boolean stale(String globalName, WfTemplateDef wfTemplateDef) throws Exception {
                boolean stale = false;
                try {
                    TemplateNameParts templateNames = WfNameUtils.getTemplateNameParts(globalName);
                    Date updateDt =
                            db().value(Date.class, "wfCategoryUpdateDt",
                                    new WfTemplateQuery().wfCategoryName(templateNames.getCategoryName())
                                            .name(templateNames.getTemplateName())
                                            .wfCategoryStatus(RecordStatus.ACTIVE));
                    stale = !updateDt.equals(wfTemplateDef.getTimestamp());
                } catch (Exception e) {
                    logError(e);
                }

                return stale;
            }

            @Override
            protected WfTemplateDef create(String globalTemplateName, Object... params) throws Exception {
                TemplateNameParts templateNames = WfNameUtils.getTemplateNameParts(globalTemplateName);
                WfTemplate wfTemplate =
                        db().list(new WfTemplateQuery().wfCategoryName(templateNames.getCategoryName())
                                .name(templateNames.getTemplateName()).wfCategoryStatus(RecordStatus.ACTIVE)
                                .select("id", "wfDocName", "name", "description", "wfCategoryUpdateDt", "stepList"));
                if (wfTemplate == null) {
                    throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_TEMPLATE_WITH_NAME_UNKNOWN,
                            globalTemplateName);
                }

                Long wfTemplateId = wfTemplate.getId();

                // Workflow document
                String globalDocName =
                        WfNameUtils.getGlobalDocName(templateNames.getCategoryName(), wfTemplate.getWfDocName());
                WfDocDef wfDocDef = wfDocs.get(globalDocName);

                // For now always use single form viewer generator
                String globalDocViewer =
                        UplUtils.generateUplGeneratorTargetName("wfsingleformdocviewer-generator", globalDocName);

                // Steps
                List<WfStepDef> stepList = new ArrayList<WfStepDef>();
                for (WfStep wfStep : wfTemplate.getStepList()) {
                    // Enrichment information
                    List<WfEnrichmentDef> enrichmentList = null;
                    if (!DataUtils.isBlank(wfStep.getEnrichmentList())) {
                        enrichmentList = new ArrayList<WfEnrichmentDef>();
                        for (WfEnrichment wfEnrichment : wfStep.getEnrichmentList()) {
                            enrichmentList.add(new WfEnrichmentDef(wfEnrichment.getName(),
                                    wfEnrichment.getDescription(), wfEnrichment.getLogic()));
                        }
                    }

                    // Routing information
                    List<WfRoutingDef> routingList = null;
                    if (!DataUtils.isBlank(wfStep.getRoutingList())) {
                        routingList = new ArrayList<WfRoutingDef>();
                        for (WfRouting wfRouting : wfStep.getRoutingList()) {
                            WfDocClassifierDef wfDocClassifierDef = null;
                            if (wfRouting.getClassifierName() != null) {
                                wfDocClassifierDef = wfDocDef.getWfDocClassifierDef(wfRouting.getClassifierName());
                            }

                            routingList.add(new WfRoutingDef(wfRouting.getName(), wfRouting.getDescription(),
                                    WfNameUtils.getGlobalStepName(templateNames.getCategoryName(),
                                            templateNames.getTemplateName(), wfRouting.getTargetWfStepName()),
                                    wfDocClassifierDef));
                        }
                    }

                    // Entity action information
                    List<WfRecordActionDef> recordActionList = null;
                    if (!DataUtils.isBlank(wfStep.getRecordActionList())) {
                        recordActionList = new ArrayList<WfRecordActionDef>();
                        for (WfRecordAction wfRecordAction : wfStep.getRecordActionList()) {
                            recordActionList.add(new WfRecordActionDef(wfRecordAction.getName(),
                                    wfRecordAction.getDescription(), wfRecordAction.getActionType(),
                                    wfDocDef.getWfDocBeanMappingDef(wfRecordAction.getDocMappingName())));
                        }
                    }

                    // User action information
                    List<WfUserActionDef> userActionList = null;
                    if (!DataUtils.isBlank(wfStep.getUserActionList())) {
                        userActionList = new ArrayList<WfUserActionDef>();
                        for (WfUserAction wfUserAction : wfStep.getUserActionList()) {
                            List<WfAttachmentCheckDef> attachmentCheckList = null;
                            if (!DataUtils.isBlank(wfUserAction.getAttachmentCheckList())) {
                                attachmentCheckList = new ArrayList<WfAttachmentCheckDef>();
                                for (WfAttachmentCheck wfAttachmentCheckData : wfUserAction.getAttachmentCheckList()) {
                                    attachmentCheckList.add(new WfAttachmentCheckDef(
                                            wfAttachmentCheckData.getRequirementType(), wfDocDef.getWfDocAttachmentDef(
                                                    wfAttachmentCheckData.getWfDocAttachmentName())));
                                }
                            }

                            userActionList.add(new WfUserActionDef(wfUserAction.getName(),
                                    wfUserAction.getDescription(), wfUserAction.getLabel(),
                                    wfUserAction.getNoteReqType(),
                                    WfNameUtils.getGlobalStepName(templateNames.getCategoryName(),
                                            templateNames.getTemplateName(), wfUserAction.getTargetWfStepName()),
                                    wfUserAction.getValidatePage(), attachmentCheckList));
                        }
                    }

                    // Form privilege information
                    List<WfFormPrivilegeDef> formPrivilegeList = null;
                    if (!DataUtils.isBlank(wfStep.getFormPrivilegeList())) {
                        formPrivilegeList = new ArrayList<WfFormPrivilegeDef>();
                        for (WfFormPrivilege wfFormPrivilege : wfStep.getFormPrivilegeList()) {
                            formPrivilegeList.add(new WfFormPrivilegeDef(wfFormPrivilege.getType(),
                                    wfFormPrivilege.getWfFormElementName(), wfFormPrivilege.getVisible(),
                                    wfFormPrivilege.getEditable(), wfFormPrivilege.getDisabled(),
                                    wfFormPrivilege.getRequired()));
                        }
                    }

                    // Alert information
                    List<WfAlertDef> alertList = null;
                    if (!DataUtils.isBlank(wfStep.getAlertList())) {
                        alertList = new ArrayList<WfAlertDef>();
                        for (WfAlert wfAlert : wfStep.getAlertList()) {
                            String globalTemplateCode =
                                    NotificationUtils.getGlobalTemplateName(WorkflowModuleNameConstants.WORKFLOW_MODULE,
                                            WfNameUtils.getGlobalMessageName(templateNames.getCategoryName(),
                                                    wfAlert.getNotificationTemplateCode()));
                            alertList.add(new WfAlertDef(wfAlert.getName(), wfAlert.getDescription(), wfAlert.getType(),
                                    globalTemplateCode));
                        }
                    }

                    // Policy information
                    List<WfPolicyDef> policyList = null;
                    if (!DataUtils.isBlank(wfStep.getPolicyList())) {
                        policyList = new ArrayList<WfPolicyDef>();
                        for (WfPolicy wfPolicy : wfStep.getPolicyList()) {
                            policyList.add(new WfPolicyDef(wfPolicy.getName(), wfPolicy.getDescription(),
                                    wfPolicy.getLogic()));
                        }
                    }

                    // Step
                    String docViewer = null;
                    if (wfStep.getStepType().isUserInteractive()) {
                        docViewer = globalDocViewer;
                    }
                    stepList.add(new WfStepDef(wfTemplateId, globalTemplateName, globalDocName,
                            WfNameUtils.getGlobalStepName(templateNames.getCategoryName(),
                                    templateNames.getTemplateName(), wfStep.getName()),
                            docViewer, wfStep.getName(), wfStep.getDescription(), wfStep.getLabel(),
                            wfStep.getStepType(), wfStep.getParticipantType(), enrichmentList, routingList,
                            recordActionList, userActionList, formPrivilegeList, alertList, policyList,
                            wfStep.getItemsPerSession(), wfStep.getExpiryHours(), wfStep.getAudit(),
                            wfStep.getBranchOnly(), wfStep.getIncludeForwarder()));
                }

                return new WfTemplateDef(wfTemplateId, globalTemplateName, wfTemplate.getName(),
                        wfTemplate.getDescription(), wfTemplate.getWfCategoryUpdateDt(), wfDocDef, stepList);
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
    public List<WfTemplate> findUserRoleManualInitWfTemplates() throws UnifyException {
        WfTemplateQuery query = new WfTemplateQuery();
        if (!getUserToken().isReservedUser()) {
            Set<String> steps = getCurrentUserRoleStepCodes();
            if (steps.isEmpty()) {
                return Collections.emptyList();
            }

            Set<String> categoryNames = new HashSet<String>();
            Set<String> templateNames = new HashSet<String>();
            for (String globalStepName : steps) {
                StepNameParts stepNameParts = WfNameUtils.getStepNameParts(globalStepName);
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
        return db().listAll(query);
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
    public WfFormDef getRuntimeWfFormDef(String globalDocName) throws UnifyException {
        WfDocDef wfDocDef = wfDocs.get(globalDocName);
        if (!wfDocDef.isForm()) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_DOCUMENT_NO_FORM, globalDocName);
        }

        return wfDocDef.getWfFormDef();
    }

    @Override
    public ManualWfItem createManualInitItem(String globalTemplateName) throws UnifyException {
        WfTemplateDef wfTemplateDef = wfTemplates.get(globalTemplateName);
        WfManualInitDef manualInitDef = wfTemplateDef.getManualInitDef();
        return new ManualWfItem(manualInitDef.getWfStepDef(),
                new PackableDoc(manualInitDef.getWfDocDef().getDocConfig()).preset());
    }

    @Override
    public void pendManualInitItem(ManualWfItem manualInitItem) throws UnifyException {
        WfStepDef manualWfStepDef = manualInitItem.getWfStepDef();
        WfTemplateDef wfTemplateDef = wfTemplates.get(manualWfStepDef.getGlobalTemplateName());
        submitToReceptacle(wfTemplateDef, manualWfStepDef, null, getUserBranchId(), manualInitItem.getPd());
    }

    @Override
    public void submitManualInitItem(ManualWfItem manualInitItem) throws UnifyException {
        submitToWorkflow(manualInitItem.getWfStepDef().getGlobalTemplateName(), manualInitItem.getPd());
    }

    @Override
    public Long submitToWorkflow(String globalTemplateName, PackableDoc packableDoc) throws UnifyException {
        WfTemplateDef wfTemplateDef = wfTemplates.get(globalTemplateName);
        return submitToReceptacle(wfTemplateDef, wfTemplateDef.getStartStep(), null, getUserBranchId(), packableDoc);
    }

    @Override
    public Long submitToWorkflow(String globalTemplateName, Document... documents) throws UnifyException {
        WfTemplateDef wfTemplateDef = wfTemplates.get(globalTemplateName);
        WfStepDef wfStepDef = wfTemplateDef.getStartStep();

        // Create and populate packable document
        Document pDocument = documents[0];
        WfDocDef wfDocDef = wfTemplateDef.getWfDocDef();
        PackableDoc packableDoc = new PackableDoc(wfDocDef.getDocConfig(), wfStepDef.isAudit());
        for (Document document : documents) {
            packableDoc.readFrom(wfDocDef.getEntryWfDocBeanMappingDef(document.getClass()).getRwConfig(), document);
        }

        return submitToReceptacle(wfTemplateDef, wfStepDef, (Long) pDocument.getId(), (Long) pDocument.getOwnerId(),
                packableDoc);
    }

    @Override
    public List<Long> grabCurrentUserWorkItems(String globalStepName) throws UnifyException {
        WfStepDef wfStepDef = accessCurrentUserStep(globalStepName);
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
        wfItemQuery.globalTemplateName(wfStepDef.getGlobalTemplateName());
        wfItemQuery.wfStepName(wfStepDef.getName());
        wfItemQuery.heldBy(userLoginID);
        return db().valueList(Long.class, "id", wfItemQuery);
    }

    @Override
    public int releaseCurrentUserWorkItems(String globalStepName, List<Long> wfItemIds) throws UnifyException {
        WfStepDef wfStepDef = accessCurrentUserStep(globalStepName);
        WfItemQuery wfItemQuery = getCurrentUserParticipationWfItemQuery(wfStepDef);
        wfItemQuery.idIn(wfItemIds);
        wfItemQuery.heldBy(getUserToken().getUserLoginId());
        return db().updateAll(wfItemQuery, new Update().add("heldBy", null));
    }

    @Override
    public WfItemObjects getCurrentUserWorkItems(String globalStepName) throws UnifyException {
        WfStepDef wfStepDef = accessCurrentUserStep(globalStepName);
        String useLoginID = getUserToken().getUserLoginId();
        List<WfItem> wfItemList =
                db().listAll(new WfItemQuery().globalTemplateName(wfStepDef.getGlobalTemplateName())
                        .wfStepName(wfStepDef.getName()).heldBy(useLoginID));

        List<WfAction> actions = new ArrayList<WfAction>();
        for (WfUserActionDef wfUserActionDef : wfStepDef.getUserActionList()) {
            actions.add(new WfAction(wfUserActionDef.getName(), resolveSessionMessage(wfUserActionDef.getLabel()),
                    wfUserActionDef.getNoteReqType(), wfUserActionDef.isValidatePage()));
        }

        return new WfItemObjects(globalStepName, wfItemList, actions);
    }

    @Override
    public List<WfItemSummary> getCurrentUserWorkItemSummary() throws UnifyException {
        Set<String> stepNames = getCurrentUserRoleStepCodes();
        if (!stepNames.isEmpty()) {
            List<WfItemSummary> summaryList = new ArrayList<WfItemSummary>();
            for (String globalStepName : stepNames) {
                WfStepDef wfStepDef = getWfStepDef(globalStepName);
                WfItemQuery wfItemQuery = getCurrentUserParticipationWfItemQuery(wfStepDef);
                int itemCount = db().countAll(wfItemQuery);
                if (itemCount > 0) {
                    String stepDesc = wfStepDef.getDescription();
                    int holdCount = db().countAll(wfItemQuery.isHeld());
                    String description = getSessionMessage("workflowitem.summary", stepDesc, itemCount, holdCount);
                    summaryList.add(new WfItemSummary(description, globalStepName, stepDesc, itemCount, holdCount));
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
    public void applyWorkflowAction(WfItemObject workflowItem, String actionName) throws UnifyException {
        WfUserActionDef wfUserActionDef = workflowItem.getWfStepDef().getWfUserActionDef(actionName);
        WfStepDef trgWfStep = getWfStepDef(wfUserActionDef.getTargetGlobalName());

        // Check if current user holds workflow item
        Long wfItemId = workflowItem.getWfItemId();
        String userLoginId = getUserToken().getUserLoginId();
        WfItemQuery wfItemQuery = ((WfItemQuery) new WfItemQuery().id(wfItemId)).heldBy(userLoginId);
        if (db().countAll(wfItemQuery) > 0) {
            // Log history
            Date actionDt = db().getNow();
            db().updateById(WfItemEvent.class, workflowItem.getWfHistEventId(), new Update().add("actionDt", actionDt)
                    .add("actor", userLoginId).add("wfAction", actionName).add("notes", workflowItem.getNotes()));

            // Route to target step
            WfStepDef settleQ = pushIntoStep(trgWfStep, workflowItem);
            if (!settleQ.isEnd()) {
                // Release workflow item
                db().updateAll(wfItemQuery, new Update().add("heldBy", null));
            }
        } else {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_APPLY_ACTION_UNHELD, actionName,
                    workflowItem.getDescription());
        }
    }

    @Override
    public WfItemObject findWorkflowItem(Long wfItemId) throws UnifyException {
        WfItem wfItem = db().list(WfItem.class, wfItemId);
        WfStepDef wfStepDef = getWfStepDef(wfItem);
        WfTemplateDef wfTemplateDef = getWfTemplateDef(wfStepDef);

        WfItemPackedDoc wfItemPackedDoc = db().find(WfItemPackedDoc.class, wfItemId);

        PackableDoc pd =
                PackableDoc.unpack(wfTemplateDef.getWfDocDef().getDocConfig(), wfItemPackedDoc.getPackedDoc(),
                        wfStepDef.isAudit());

        WfItemObject workflowItem =
                new WfItemObject(wfStepDef, wfItem.getDocumentId(), wfItemId, wfItem.getWfItemHistId(),
                        wfItem.getWfHistEventId(), wfItem.getDescription(), wfItem.getCreateDt(), wfItem.getStepDt(),
                        wfItem.getHeldBy(), pd);

        return workflowItem;
    }

    @Override
    public WfItemHistObject findWorkflowItemHistory(Long wfItemHistId, boolean notesOnly) throws UnifyException {
        WfItemHist wfHist = db().list(WfItemHist.class, wfItemHistId);
        WfItemEventQuery query = new WfItemEventQuery();
        if (notesOnly) {
            query.notesOnly();
        }

        query.wfItemHistId(wfItemHistId);
        query.orderById();
        List<WfItemEvent> wfHistEventList = db().listAll(query);

        String globalDocName = WfNameUtils.getGlobalDocName(wfHist.getWfCategoryName(), wfHist.getWfDocName());
        String globalTemplateName =
                WfNameUtils.getGlobalTemplateName(wfHist.getWfCategoryName(), wfHist.getWfTemplateName());
        WfTemplateDef wfTemplateDef = wfTemplates.get(globalTemplateName);
        List<WfItemHistEvent> eventList = new ArrayList<WfItemHistEvent>();
        for (WfItemEvent wfHistEvent : wfHistEventList) {
            WfStepDef wfStepDef = wfTemplateDef.getWfStepDef(wfHistEvent.getWfStepName());
            String actionDesc = null;
            if (wfHistEvent.getWfAction() != null) {
                actionDesc = resolveSessionMessage(wfStepDef.getWfUserActionDef(wfHistEvent.getWfAction()).getLabel());
            }

            eventList.add(new WfItemHistEvent(wfHistEvent.getId(), wfHistEvent.getWfStepName(), wfHistEvent.getStepDt(),
                    wfHistEvent.getActionDt(), wfHistEvent.getActor(), wfHistEvent.getWfAction(), actionDesc,
                    wfHistEvent.getNotes()));
        }

        return new WfItemHistObject(wfHist.getId(), wfHist.getDocumentId(), globalDocName, globalTemplateName,
                wfHist.getDescription(), Collections.unmodifiableList(eventList));
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
    public List<ToolingEnrichmentLogicItem> findToolingEnrichmentLogicTypes() throws UnifyException {
        return getToolingTypes(ToolingEnrichmentLogicItem.class, WfItemEnrichmentLogic.class);
    }

    @Override
    public List<ToolingPolicyLogicItem> findToolingPolicyLogicTypes() throws UnifyException {
        return getToolingTypes(ToolingPolicyLogicItem.class, WfItemPolicyLogic.class);
    }

    @Taskable(
            name = WorkflowApplyActionTaskConstants.TASK_NAME,
            description = "Apply Action to Multiple Workflow Items Task",
            parameters = { @Parameter(name = WorkflowApplyActionTaskConstants.WFITEMS_IDLIST, type = List.class),
                    @Parameter(WorkflowApplyActionTaskConstants.WFACTION_NAME),
                    @Parameter(WorkflowApplyActionTaskConstants.NOTES) },
            limit = TaskExecLimit.ALLOW_MULTIPLE)
    public int executeApplyActionToMultipleWorkflowItems(TaskMonitor taskMonitor, List<Long> wfItemIdList,
            String actionName, String notes) throws UnifyException {
        for (Long wfItemId : wfItemIdList) {
            WfItemObject workflowItem = findWorkflowItem(wfItemId);
            addTaskMonitorSessionMessage(taskMonitor, "workflow.taskmonitor.itemgrabbed",
                    workflowItem.getDescription());
            workflowItem.setNotes(notes);
            applyWorkflowAction(workflowItem, actionName);
            addTaskMessage(taskMonitor, "workflow.taskmonitor.actionapplied");
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
                && !DataUtils.isBlank(wfCategoryConfig.getWfMessagesConfig().getWfMessageConfigList())) {
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
            String wfDocName = wfTemplateConfig.getDocument();
            String wfTemplateName = wfTemplateConfig.getName();
            wdQuery.clear();
            WfTemplate oldWfTemplate = db().find(wdQuery.wfCategoryId(wfCategoryId).name(wfTemplateName));
            description = resolveApplicationMessage(wfTemplateConfig.getDescription());
            if (oldWfTemplate == null) {
                addTaskMessage(taskMonitor, "Creating new template...");
                wfTemplate.setName(wfTemplateName);
                wfTemplate.setDescription(description);
                wfTemplate.setWfDocName(wfDocName);
                populateChildList(wfTemplate, wfTemplateConfig);
                db().create(wfTemplate);
            } else {
                addTaskMessage(taskMonitor, "Updating existing template...");
                oldWfTemplate.setDescription(description);
                oldWfTemplate.setWfDocName(wfDocName);
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
                        WfNameUtils.getGlobalMessageName(wfMessage.getWfCategoryName(), wfMessage.getName());
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

    private void populateChildList(WfDoc wfDoc, WfDocumentConfig wfDocConfig) throws UnifyException {
        // Fields
        List<WfDocField> fieldList = new ArrayList<WfDocField>();
        if (!DataUtils.isBlank(wfDocConfig.getWfFieldsConfig().getWfFieldConfigList())) {
            for (WfFieldConfig wfDocFieldConfig : wfDocConfig.getWfFieldsConfig().getWfFieldConfigList()) {
                WfDocField wfDocField = new WfDocField();
                wfDocField.setName(wfDocFieldConfig.getName());
                wfDocField.setDescription(resolveApplicationMessage(wfDocFieldConfig.getDescription()));
                wfDocField.setDataType(wfDocFieldConfig.getDataType());
                wfDocField.setRepeat(wfDocFieldConfig.getRepeat());
                fieldList.add(wfDocField);
            }
        }

        if (!DataUtils.isBlank(wfDocConfig.getWfFieldsConfig().getWfComplexFieldConfigList())) {
            for (WfComplexFieldConfig wfDocComplexFieldConfig : wfDocConfig.getWfFieldsConfig()
                    .getWfComplexFieldConfigList()) {
                WfDocField wfDocField = new WfDocField();
                wfDocField.setName(wfDocComplexFieldConfig.getName());
                wfDocField.setDescription(resolveApplicationMessage(wfDocComplexFieldConfig.getDescription()));
                wfDocField.setDataType(wfDocComplexFieldConfig.getDataType());
                wfDocField.setRepeat(wfDocComplexFieldConfig.getRepeat());
                fieldList.add(wfDocField);

                for (WfFieldConfig wfDocFieldConfig : wfDocComplexFieldConfig.getWfFieldConfigList()) {
                    wfDocField = new WfDocField();
                    wfDocField.setParentName(wfDocComplexFieldConfig.getName());
                    wfDocField.setName(wfDocFieldConfig.getName());
                    wfDocField.setDescription(resolveApplicationMessage(wfDocFieldConfig.getDescription()));
                    wfDocField.setDataType(wfDocFieldConfig.getDataType());
                    wfDocField.setRepeat(wfDocFieldConfig.getRepeat());
                    fieldList.add(wfDocField);
                }
            }
        }

        wfDoc.setFieldList(fieldList);

        // Classifiers
        List<WfDocClassifier> classifierList = null;
        if (wfDocConfig.getWfClassifiersConfig() != null
                && !DataUtils.isBlank(wfDocConfig.getWfClassifiersConfig().getWfClassifierConfigList())) {
            classifierList = new ArrayList<WfDocClassifier>();
            for (WfClassifierConfig wfDocClassifierConfig : wfDocConfig.getWfClassifiersConfig()
                    .getWfClassifierConfigList()) {
                WfDocClassifier wfDocClassifier = new WfDocClassifier();
                wfDocClassifier.setName(wfDocClassifierConfig.getName());
                wfDocClassifier.setDescription(resolveApplicationMessage(wfDocClassifierConfig.getDescription()));
                wfDocClassifier.setFilterLogic(wfDocClassifierConfig.getFilterLogic());
                wfDocClassifier.setLogic(wfDocClassifierConfig.getLogic());

                if (!DataUtils.isBlank(wfDocClassifierConfig.getWfClassifierFilterConfigList())) {
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
                && !DataUtils.isBlank(wfDocConfig.getWfAttachmentsConfig().getWfAttachmentConfigList())) {
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
                && !DataUtils.isBlank(wfDocConfig.getWfBeanMappingsConfig().getBeanMappingList())) {
            recordMappingList = new ArrayList<WfDocBeanMapping>();
            for (WfBeanMappingConfig wfDocBeanMappingConfig : wfDocConfig.getWfBeanMappingsConfig()
                    .getBeanMappingList()) {
                WfDocBeanMapping wfDocBeanMapping = new WfDocBeanMapping();
                wfDocBeanMapping.setName(wfDocBeanMappingConfig.getName());
                wfDocBeanMapping.setDescription(resolveApplicationMessage(wfDocBeanMappingConfig.getDescription()));
                wfDocBeanMapping.setBeanType(wfDocBeanMappingConfig.getBeanType());
                wfDocBeanMapping.setType(wfDocBeanMappingConfig.getType());

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
                    && !DataUtils.isBlank(wfStepConfig.getWfEnrichmentsConfig().getWfEnrichmentConfigList())) {
                enrichmentList = new ArrayList<WfEnrichment>();
                for (WfEnrichmentConfig wfEnrichmentConfig : wfStepConfig.getWfEnrichmentsConfig()
                        .getWfEnrichmentConfigList()) {
                    WfEnrichment wfEnrichment = new WfEnrichment();
                    wfEnrichment.setName(wfEnrichmentConfig.getName());
                    wfEnrichment.setDescription(resolveApplicationMessage(wfEnrichmentConfig.getDescription()));
                    wfEnrichment.setLogic(wfEnrichmentConfig.getLogic());
                    enrichmentList.add(wfEnrichment);
                }
            }
            wfStep.setEnrichmentList(enrichmentList);

            // Entity actions
            List<WfRecordAction> recordActionList = null;
            if (wfStepConfig.getWfRecordActionsConfig() != null
                    && !DataUtils.isBlank(wfStepConfig.getWfRecordActionsConfig().getWfRecordActionConfigList())) {
                recordActionList = new ArrayList<WfRecordAction>();
                for (WfRecordActionConfig wfRecordActionConfig : wfStepConfig.getWfRecordActionsConfig()
                        .getWfRecordActionConfigList()) {
                    WfRecordAction wfRecordAction = new WfRecordAction();
                    wfRecordAction.setName(wfRecordActionConfig.getName());
                    wfRecordAction.setDescription(resolveApplicationMessage(wfRecordActionConfig.getDescription()));
                    wfRecordAction.setDocMappingName(wfRecordActionConfig.getDocMappingName());
                    wfRecordAction.setActionType(wfRecordActionConfig.getActionType());
                    recordActionList.add(wfRecordAction);
                }
            }
            wfStep.setRecordActionList(recordActionList);

            // Routings
            List<WfRouting> routingList = null;
            if (wfStepConfig.getWfRoutingsConfig() != null
                    && !DataUtils.isBlank(wfStepConfig.getWfRoutingsConfig().getWfRoutingConfigList())) {
                routingList = new ArrayList<WfRouting>();
                for (WfRoutingConfig wfRoutingConfig : wfStepConfig.getWfRoutingsConfig().getWfRoutingConfigList()) {
                    WfRouting wfRouting = new WfRouting();
                    wfRouting.setName(wfRoutingConfig.getName());
                    wfRouting.setDescription(resolveApplicationMessage(wfRoutingConfig.getDescription()));
                    wfRouting.setClassifierName(wfRoutingConfig.getClassifierName());
                    wfRouting.setTargetWfStepName(wfRoutingConfig.getTargetStepName());
                    routingList.add(wfRouting);
                }
            }
            wfStep.setRoutingList(routingList);

            // User actions
            List<WfUserAction> userActionList = null;
            if (wfStepConfig.getWfUserActionsConfig() != null
                    && !DataUtils.isBlank(wfStepConfig.getWfUserActionsConfig().getWfUserActionConfigList())) {
                userActionList = new ArrayList<WfUserAction>();
                for (WfUserActionConfig wfUserActionConfig : wfStepConfig.getWfUserActionsConfig()
                        .getWfUserActionConfigList()) {
                    WfUserAction wfUserAction = new WfUserAction();
                    wfUserAction.setName(wfUserActionConfig.getName());
                    wfUserAction.setDescription(resolveApplicationMessage(wfUserActionConfig.getDescription()));
                    wfUserAction.setLabel(wfUserActionConfig.getLabel());
                    wfUserAction.setNoteReqType(wfUserActionConfig.getNoteRequirement());
                    wfUserAction.setTargetWfStepName(wfUserActionConfig.getTargetStepName());
                    wfUserAction.setValidatePage(wfUserActionConfig.getValidatePage());

                    if (!DataUtils.isBlank(wfUserActionConfig.getAttachmentCheckConfigList())) {
                        List<WfAttachmentCheck> attachmentCheckList = new ArrayList<WfAttachmentCheck>();
                        for (WfAttachmentCheckConfig wfAttachmentCheckConfig : wfUserActionConfig
                                .getAttachmentCheckConfigList()) {
                            WfAttachmentCheck wfAttachmentCheck = new WfAttachmentCheck();
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
                    && !DataUtils.isBlank(wfStepConfig.getWfFormPrivilegesConfig().getWfFormPrivilegesConfigList())) {
                formPrivilegeList = new ArrayList<WfFormPrivilege>();
                for (WfFormPrivilegeConfig wfFormPrivilegeConfig : wfStepConfig.getWfFormPrivilegesConfig()
                        .getWfFormPrivilegesConfigList()) {
                    WfFormPrivilege wfFormPrivilege = new WfFormPrivilege();
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
                    && !DataUtils.isBlank(wfStepConfig.getWfPoliciesConfig().getWfPolicyConfigList())) {
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
                    && !DataUtils.isBlank(wfStepConfig.getWfAlertsConfig().getWfAlertConfigList())) {
                alertList = new ArrayList<WfAlert>();
                for (WfAlertConfig wfAlertConfig : wfStepConfig.getWfAlertsConfig().getWfAlertConfigList()) {
                    WfAlert wfAlert = new WfAlert();
                    wfAlert.setName(wfAlertConfig.getName());
                    wfAlert.setDescription(resolveApplicationMessage(wfAlertConfig.getDescription()));
                    wfAlert.setType(wfAlertConfig.getType());
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
            wfUserAction.setNoteReqType(RequirementType.NONE);
            wfUserAction.setTargetWfStepName(startWfStep.getName());
            wfUserAction.setValidatePage(Boolean.TRUE);
            userActionList.add(wfUserAction);

            wfUserAction = new WfUserAction();
            wfUserAction.setName("discard");
            wfUserAction.setDescription(getApplicationMessage("workflow.wftemplate.wfstep.manualdiscard"));
            wfUserAction.setLabel(getApplicationMessage("label.discard"));
            wfUserAction.setNoteReqType(RequirementType.NONE);
            wfUserAction.setTargetWfStepName(endWfStep.getName());
            wfUserAction.setValidatePage(Boolean.TRUE);
            userActionList.add(wfUserAction);

            manualWfStep.setUserActionList(userActionList);
        }

        wfTemplate.setStepList(stepList);
    }

    private Long submitToReceptacle(WfTemplateDef wfTemplateDef, WfStepDef trgWfStepDef, Long id, Long ownerId,
            PackableDoc packableDoc) throws UnifyException {
        if (!trgWfStepDef.isStart() && !trgWfStepDef.isManual() && !trgWfStepDef.isReceptacle()) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_SUBMIT_NONSTART_NONRECEPTACLE,
                    trgWfStepDef.getGlobalName());
        }

        // Create workflow item
        WfItem wfItem = new WfItem();
        wfItem.setGlobalTemplateName(wfTemplateDef.getGlobalName());
        wfItem.setOwnerId(ownerId);
        wfItem.setDescription(packableDoc.describe(wfTemplateDef.getWfDocDef().getItemDescFormat()));
        wfItem.setCreateDt(db().getNow());
        Long wfItemId = (Long) db().create(wfItem);

        WfItemPackedDoc wfItemPackedDoc = new WfItemPackedDoc();
        wfItemPackedDoc.setWfItemId(wfItemId);
        wfItemPackedDoc.setPackedDoc(packableDoc.pack());
        db().create(wfItemPackedDoc);
        packableDoc.setChanged(false);

        // Push to step
        WfItemObject workflowItem =
                new WfItemObject(trgWfStepDef, id, wfItemId, null, null, wfItem.getDescription(), wfItem.getCreateDt(),
                        wfItem.getStepDt(), wfItem.getHeldBy(), packableDoc);

        WfStepDef settleStep = pushIntoStep(trgWfStepDef, workflowItem);

        // Return null if item settles in end step
        if (settleStep.isEnd()) {
            return null;
        }

        return wfItemId;
    }

    private WfStepDef accessCurrentUserStep(String globalName) throws UnifyException {
        WfStepDef wfStepDef = getWfStepDef(globalName);
        if (!getUserToken().isReservedUser()) {
            Set<String> stepNames = getCurrentUserRoleStepCodes();
            if (!stepNames.contains(globalName)) {
                throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_CURRENT_USER_NOT_PARTICIPANT,
                        getUserToken().getUserName(), wfStepDef.getDescription());
            }
        }

        return wfStepDef;
    }

    private WfItemQuery getCurrentUserParticipationWfItemQuery(WfStepDef wfStepDef) throws UnifyException {
        WfItemQuery wfItemQuery =
                new WfItemQuery().globalTemplateName(wfStepDef.getGlobalTemplateName()).wfStepName(wfStepDef.getName());
        if (!getUserToken().isReservedUser()) {
            Boolean supervisor = (Boolean) this.getSessionAttribute(JacklynSessionAttributeConstants.SUPERVISORFLAG);
            if (Boolean.TRUE.equals(supervisor)) {
                wfItemQuery.allOrParticipantType(WorkflowParticipantType.SUPERVISOR);
            } else {
                wfItemQuery.allOrParticipantType(WorkflowParticipantType.PERSONNEL);
            }

            if (wfStepDef.isBranchOnly()) {
                wfItemQuery.ownerId(getUserBranchId());
            }
        }

        return wfItemQuery;
    }

    private WfStepDef pushIntoStep(WfStepDef wfStepDef, WfItemObject wfItem) throws UnifyException {
        // Create history
        WfTemplateDef wfTemplateDef = getWfTemplateDef(wfStepDef);
        WfDocDef wfDocDef = wfTemplateDef.getWfDocDef();
        Date stepDt = db().getNow();
        Long wfItemHistId = wfItem.getWfItemHistId();
        if (wfItemHistId == null && (wfStepDef.isStart() || wfStepDef.isManual() || wfStepDef.isReceptacle())) {
            WfItemHist wfHist = new WfItemHist();
            wfHist.setWfTemplateId(wfTemplateDef.getWfTemplateId());
            wfHist.setWfDocId(wfDocDef.getWfDocId());
            wfHist.setDescription(wfItem.getDescription());
            wfHist.setDocumentId((Long) wfItem.getDocId());
            wfItemHistId = (Long) db().create(wfHist);
        }

        WfItemEvent wfHistEvent = new WfItemEvent();
        wfHistEvent.setWfItemHistId(wfItemHistId);
        wfHistEvent.setWfStepName(wfStepDef.getName());
        wfHistEvent.setStepDt(stepDt);
        Long wfHistEventId = (Long) db().create(wfHistEvent);

        // Update workflow item information.
        UserToken userToken = getUserToken();
        String forwardedBy = null;
        if (!userToken.isReservedUser()) {
            forwardedBy = userToken.getUserLoginId();
        }

        PackableDoc packableDoc = wfItem.getPd();
        Long wfItemId = wfItem.getWfItemId();
        wfItem.setWfItemHistId(wfItemHistId);
        wfItem.setWfHistEventId(wfHistEventId);
        wfItem.setWfStepDef(wfStepDef);
        db().updateById(WfItem.class, wfItemId,
                new Update().add("wfHistEventId", wfHistEventId).add("wfStepName", wfStepDef.getName())
                        .add("stepDt", stepDt).add("participantType", wfStepDef.getParticipantType())
                        .add("forwardedBy", forwardedBy));

        // Perform enrichment if any
        for (WfEnrichmentDef wfEnrichmentDef : wfStepDef.getEnrichmentList()) {
            WfItemEnrichmentLogic wfItemEnrichmentLogic =
                    (WfItemEnrichmentLogic) getComponent(wfEnrichmentDef.getLogic());
            wfItemEnrichmentLogic.enrich(wfItem.getReaderWriter());
        }

        // Perform record actions if any
        if (!DataUtils.isBlank(wfStepDef.getRecordActionList())) {
            for (WfRecordActionDef wfRecordActionDef : wfStepDef.getRecordActionList()) {
                WfDocBeanMappingDef wfDocBeanMappingDef = wfRecordActionDef.getBeanMapping();
                PackableDocRWConfig rwConfig = wfDocBeanMappingDef.getRwConfig();
                String docIdName = rwConfig.getMappedDocField("id");
                switch (wfRecordActionDef.getActionType()) {
                case CREATE: {
                    Document document = ReflectUtils.newInstance(wfDocBeanMappingDef.getBeanType());
                    packableDoc.writeTo(rwConfig, document);
                    Object id = genericService.create(document);
                    packableDoc.writeFieldValue(docIdName, id);

                    if (wfDocBeanMappingDef.isPrimaryMapping()) {
                        // Update document id in item history
                        db().updateById(WfItemHist.class, wfItemHistId, new Update().add("documentId", id));
                    }
                }
                    break;
                case DELETE: {
                    Object id = packableDoc.readFieldValue(docIdName);
                    genericService.delete(wfDocBeanMappingDef.getBeanType(), id);
                }
                    break;
                case READ: {
                    Object id = packableDoc.readFieldValue(docIdName);
                    Document document = genericService.find(wfDocBeanMappingDef.getBeanType(), id);
                    packableDoc.readFrom(wfDocBeanMappingDef.getRwConfig(), document);
                }
                    break;
                case UPDATE: {
                    Object id = packableDoc.readFieldValue(docIdName);
                    Update update = new Update();
                    for (PackableDocRWConfig.FieldMapping fieldMapping : wfDocBeanMappingDef.getRwConfig()
                            .getFieldMappings()) {
                        if (docIdName.equals(fieldMapping.getDocFieldName())) {
                            // Skip. Do not update ID.
                            continue;
                        }

                        update.add(fieldMapping.getBeanFieldName(),
                                packableDoc.readFieldValue(fieldMapping.getDocFieldName()));
                    }

                    genericService.updateById(wfDocBeanMappingDef.getBeanType(), id, update);
                }
                    break;
                }
            }
        }

        // Apply policies
        WfItemReader wfItemReader = wfItem.getReader();
        for (WfPolicyDef wfPolicyDef : wfStepDef.getPolicyList()) {
            WfItemPolicyLogic wfItemPolicyLogic = (WfItemPolicyLogic) getComponent(wfPolicyDef.getLogic());
            wfItemPolicyLogic.executePolicy(wfItemReader);
        }

        // Send alerts
        for (WfAlertDef wfAlertDef : wfStepDef.getAlertList()) {
            wfItemAlertLogic.sendAlert(wfItemReader, wfAlertDef);
        }

        // Commit this transition?
        // TODO
        // commitPackableDoc(packableDoc, wfItemId);
        // commit();

        // Check for termination
        if (wfStepDef.isEnd()) {
            deleteWorkflowItem(wfItemId);
        } else {
            // Route item if necessary
            if (!DataUtils.isBlank(wfStepDef.getRoutingList())) {
                for (WfRoutingDef wfRoutingDef : wfStepDef.getRoutingList()) {
                    if (tryRoute(wfItemReader, wfRoutingDef)) {
                        WfStepDef trgStep = getWfStepDef(wfRoutingDef.getTargetGlobalName());
                        if (trgStep.isStart() || wfStepDef.isManual()) {
                            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_ITEM_NOT_ROUTE_START,
                                    wfTemplateDef.getDescription(), wfStepDef.getDescription());
                        }

                        return pushIntoStep(trgStep, wfItem);
                    }
                }
            }

            // Commit if packable document has changed
            commitPackableDoc(packableDoc, wfItemId);
        }

        return wfStepDef;
    }

    private boolean tryRoute(WfItemReader wfItemReader, WfRoutingDef wfRoutingDef) throws UnifyException {
        WfDocClassifierDef wfDocClassifierDef = wfRoutingDef.getClassifier();
        if (wfDocClassifierDef != null) {
            WfItemClassifierLogic workflowDocClassifierLogic =
                    (WfItemClassifierLogic) getComponent(wfDocClassifierDef.getLogic());
            return workflowDocClassifierLogic.match(wfItemReader, wfDocClassifierDef);
        }

        return true;
    }

    private WfDocDef getWfItemWfDocDef(Long wfItemId) throws UnifyException {
        String globalTemplateName = db().value(String.class, "globalTemplateName", new WfItemQuery().id(wfItemId));
        return wfTemplates.get(globalTemplateName).getWfDocDef();
    }

    private WfStepDef getWfStepDef(String globalName) throws UnifyException {
        StepNameParts stepNameParts = WfNameUtils.getStepNameParts(globalName);
        return wfTemplates.get(stepNameParts.getGlobalTemplateName()).getWfStepDef(stepNameParts.getStepName());
    }

    private WfStepDef getWfStepDef(WfItem wfItem) throws UnifyException {
        return wfTemplates.get(wfItem.getGlobalTemplateName()).getWfStepDef(wfItem.getWfStepName());
    }

    private WfTemplateDef getWfTemplateDef(WfStepDef wfStepDef) throws UnifyException {
        return wfTemplates.get(wfStepDef.getGlobalTemplateName());
    }

    private boolean commitPackableDoc(PackableDoc packableDoc, Long wfItemId) throws UnifyException {
        if (packableDoc.isChanged()) {
            db().updateAll(new WfItemPackedDocQuery().wfItemId(wfItemId),
                    new Update().add("packedDoc", packableDoc.pack()).add("updateDt", db().getNow()));
            packableDoc.setChanged(false);
            return true;
        }

        return false;
    }

    private void deleteWorkflowItem(Long wfItemId) throws UnifyException {
        db().deleteAll(new WfItemPackedDocQuery().wfItemId(wfItemId));
        db().deleteAll(new WfItemAttachmentQuery().wfItemId(wfItemId));
        db().delete(WfItem.class, wfItemId);
    }
}
