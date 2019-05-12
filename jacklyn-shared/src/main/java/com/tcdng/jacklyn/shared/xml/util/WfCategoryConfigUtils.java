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

package com.tcdng.jacklyn.shared.xml.util;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.shared.workflow.WorkflowStepType;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfAlertConfig;
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
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.XMLConfigUtils;

/**
 * Workflow category XML configuration utilities.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public final class WfCategoryConfigUtils {

    private WfCategoryConfigUtils() {

    }

    public static WfCategoryConfig readWfCategoryConfig(File file) throws UnifyException {
        return XMLConfigUtils.readXmlConfig(WfCategoryConfig.class, file);
    }

    public static WfCategoryConfig readWfCategoryConfig(InputStream in) throws UnifyException {
        try {
            return XMLConfigUtils.readXmlConfig(WfCategoryConfig.class, in);
        } finally {
            IOUtils.close(in);
        }
    }

    public static WfCategoryConfig readWfCategoryConfig(Reader reader) throws UnifyException {
        try {
            return XMLConfigUtils.readXmlConfig(WfCategoryConfig.class, reader);
        } finally {
            IOUtils.close(reader);
        }
    }

    public static WfCategoryConfig readWfCategoryConfig(String resourceName) throws UnifyException {
        InputStream inputStream = null;
        try {
            inputStream = IOUtils.openClassLoaderResourceInputStream(resourceName);
            return XMLConfigUtils.readXmlConfig(WfCategoryConfig.class, inputStream);
        } finally {
            IOUtils.close(inputStream);
        }
    }

    public static List<UnifyError> validate(WfCategoryConfig wfCategoryConfig) throws UnifyException {
        return WfCategoryConfigUtils.validate(null, wfCategoryConfig);
    }

    public static List<UnifyError> validate(TaskMonitor taskMonitor, WfCategoryConfig wfCategoryConfig)
            throws UnifyException {
        WfCategoryValidationContext ctx = new WfCategoryValidationContext(taskMonitor);
        // Category name and description
        String name = wfCategoryConfig.getName();
        if (StringUtils.isBlank(name)) {
            ctx.addError(WfCategoryErrorConstants.WFCATEGORY_NO_NAME);
        } else {
            if (!WfNameUtils.isValidName(name)) {
                ctx.addError(WfCategoryErrorConstants.WFCATEGORY_INVALID_NAME_FORMAT, name);
            }
        }

        if (StringUtils.isBlank(wfCategoryConfig.getDescription())) {
            ctx.addError(WfCategoryErrorConstants.WFCATEGORY_NO_DESC);
        }

        String version = wfCategoryConfig.getVersion();
        if (StringUtils.isBlank(version)) {
            ctx.addError(WfCategoryErrorConstants.WFCATEGORY_NO_VERSION);
        }

        // Documents
        if (wfCategoryConfig.getWfDocumentsConfig() == null
                || DataUtils.isBlank(wfCategoryConfig.getWfDocumentsConfig().getWfDocumentConfigList())) {
            ctx.addError(WfCategoryErrorConstants.WFCATEGORY_NO_DOCUMENTS);
        } else {
            for (WfDocumentConfig wfDocumentConfig : wfCategoryConfig.getWfDocumentsConfig()
                    .getWfDocumentConfigList()) {
                if (!StringUtils.isBlank(wfDocumentConfig.getName()) && ctx.isDocument(wfDocumentConfig.getName())) {
                    ctx.addError(WfCategoryErrorConstants.WFCATEGORY_DOCUMENT_EXISTS, wfDocumentConfig.getName());
                }

                WfDocumentValidationContext wfDocCtx = WfCategoryConfigUtils.validate(taskMonitor, wfDocumentConfig);
                ctx.addDocument(wfDocCtx);
                List<UnifyError> errorList = wfDocCtx.getErrorList();
                if (!errorList.isEmpty()) {
                    ctx.addError(WfCategoryErrorConstants.WFCATEGORY_DOCUMENT_ERRORS, ctx.getDocumentCounter());
                    ctx.addErrorList(errorList);
                }
            }
        }

        // Messages
        if (wfCategoryConfig.getWfMessagesConfig() != null
                && !DataUtils.isBlank(wfCategoryConfig.getWfMessagesConfig().getWfMessageConfigList())) {
            for (WfMessageConfig wfMessageConfig : wfCategoryConfig.getWfMessagesConfig().getWfMessageConfigList()) {
                ctx.addMessage(wfMessageConfig);
            }
        }

        // Templates
        if (wfCategoryConfig.getWfTemplatesConfig() == null
                || DataUtils.isBlank(wfCategoryConfig.getWfTemplatesConfig().getWfTemplateConfigList())) {
            ctx.addError(WfCategoryErrorConstants.WFCATEGORY_NO_TEMPLATES);
        } else {
            for (WfTemplateConfig wfTemplateConfig : wfCategoryConfig.getWfTemplatesConfig()
                    .getWfTemplateConfigList()) {
                ctx.addTemplate(wfTemplateConfig);
                List<UnifyError> errorList = WfCategoryConfigUtils.validate(ctx, wfTemplateConfig);
                if (!errorList.isEmpty()) {
                    ctx.addError(WfCategoryErrorConstants.WFCATEGORY_TEMPLATE_ERRORS, ctx.getTemplateCounter());
                    ctx.addErrorList(errorList);
                }
            }
        }

        return ctx.getErrorList();
    }

    private static WfDocumentValidationContext validate(TaskMonitor taskMonitor, WfDocumentConfig wfDocumentConfig)
            throws UnifyException {
        WfDocumentValidationContext ctx = new WfDocumentValidationContext(wfDocumentConfig.getName(), taskMonitor);
        // Document name and description
        String name = wfDocumentConfig.getName();
        if (StringUtils.isBlank(name)) {
            ctx.addError(WfDocumentErrorConstants.WFDOCUMENT_NO_NAME);
        }

        if (StringUtils.isBlank(wfDocumentConfig.getDescription())) {
            ctx.addError(WfDocumentErrorConstants.WFDOCUMENT_NO_DESC);
        }

        // Fields
        if (wfDocumentConfig.getWfFieldsConfig() == null
                || (DataUtils.isBlank(wfDocumentConfig.getWfFieldsConfig().getWfFieldConfigList())
                        && DataUtils.isBlank(wfDocumentConfig.getWfFieldsConfig().getWfComplexFieldConfigList()))) {
            ctx.addError(WfDocumentErrorConstants.WFDOCUMENT_NO_FIELDS);
        } else {
            if (!DataUtils.isBlank(wfDocumentConfig.getWfFieldsConfig().getWfFieldConfigList())) {
                for (WfFieldConfig wfFieldConfig : wfDocumentConfig.getWfFieldsConfig().getWfFieldConfigList()) {
                    ctx.addField(wfFieldConfig, null);
                }
            }

            if (!DataUtils.isBlank(wfDocumentConfig.getWfFieldsConfig().getWfComplexFieldConfigList())) {
                for (WfComplexFieldConfig wfComplexFieldConfig : wfDocumentConfig.getWfFieldsConfig()
                        .getWfComplexFieldConfigList()) {
                    ctx.addField(wfComplexFieldConfig, null);
                    if (DataUtils.isBlank(wfComplexFieldConfig.getWfFieldConfigList())) {
                        ctx.addError(WfDocumentErrorConstants.WFDOCUMENT_COMPLEX_NO_FIELDS, ctx.getFieldCounter(),
                                wfComplexFieldConfig.getName());
                    } else {
                        for (WfFieldConfig wfFieldConfig : wfComplexFieldConfig.getWfFieldConfigList()) {
                            ctx.addField(wfFieldConfig, wfComplexFieldConfig.getName());
                        }
                    }
                }
            }
        }

        // Classifiers
        if (wfDocumentConfig.getWfClassifiersConfig() != null
                && !DataUtils.isBlank(wfDocumentConfig.getWfClassifiersConfig().getWfClassifierConfigList())) {
            for (WfClassifierConfig wfClassifierConfig : wfDocumentConfig.getWfClassifiersConfig()
                    .getWfClassifierConfigList()) {
                ctx.addClassifier(wfClassifierConfig);
            }
        }

        // Attachments
        if (wfDocumentConfig.getWfAttachmentsConfig() != null
                && !DataUtils.isBlank(wfDocumentConfig.getWfAttachmentsConfig().getWfAttachmentConfigList())) {
            for (WfAttachmentConfig wfAttachmentConfig : wfDocumentConfig.getWfAttachmentsConfig()
                    .getWfAttachmentConfigList()) {
                ctx.addAttachment(wfAttachmentConfig);
            }
        }

        // Bean mappings
        if (wfDocumentConfig.getWfBeanMappingsConfig() != null
                && !DataUtils.isBlank(wfDocumentConfig.getWfBeanMappingsConfig().getBeanMappingList())) {
            for (WfBeanMappingConfig wfBeanMappingConfig : wfDocumentConfig.getWfBeanMappingsConfig()
                    .getBeanMappingList()) {
                ctx.addBeanMapping(wfBeanMappingConfig);
            }
        }

        // Form
        if (wfDocumentConfig.getWfFormConfig() != null) {
            if (DataUtils.isBlank(wfDocumentConfig.getWfFormConfig().getWfFormTabConfigList())) {
                ctx.addError(WfDocumentErrorConstants.WFDOCUMENT_FORM_NO_TABS);
            } else {
                for (WfFormTabConfig wfFormTabConfig : wfDocumentConfig.getWfFormConfig().getWfFormTabConfigList()) {
                    ctx.addTab(wfFormTabConfig);
                }
            }
        }

        return ctx;
    }

    private static List<UnifyError> validate(WfCategoryValidationContext wfCatCtx, WfTemplateConfig wfTemplateConfig)
            throws UnifyException {
        WfTemplateValidationContext ctx = new WfTemplateValidationContext(wfCatCtx);
        // Template name and description
        String name = wfTemplateConfig.getName();
        if (StringUtils.isBlank(name)) {
            ctx.addError(WfTemplateErrorConstants.WFTEMPLATE_NO_NAME);
        }

        if (StringUtils.isBlank(wfTemplateConfig.getDescription())) {
            ctx.addError(WfTemplateErrorConstants.WFTEMPLATE_NO_DESC);
        }

        // Documents
        if (wfTemplateConfig.getWfTemplateDocsConfig() == null
                || DataUtils.isBlank(wfTemplateConfig.getWfTemplateDocsConfig().getWfTemplateDocConfigList())) {
            ctx.addError(WfTemplateErrorConstants.WFTEMPLATE_NO_DOCUMENT);
        } else {
            for (WfTemplateDocConfig wfTemplateDocConfig : wfTemplateConfig.getWfTemplateDocsConfig()
                    .getWfTemplateDocConfigList()) {
                if (ctx.addTemplateDoc(wfTemplateDocConfig)) {
                    if (!wfCatCtx.isDocument(wfTemplateDocConfig.getName())) {
                        ctx.addError(WfTemplateErrorConstants.WFTEMPLATE_DOC_UNKNOWN_CATEGORY_DOC,
                                wfTemplateDocConfig.getName());
                    }
                }
            }
        }

        // Steps
        if (wfTemplateConfig.getWfStepsConfig() == null
                || DataUtils.isBlank(wfTemplateConfig.getWfStepsConfig().getWfStepConfigList())) {
            ctx.addError(WfTemplateErrorConstants.WFTEMPLATE_NO_STEPS);
        } else {
            for (WfStepConfig wfStepConfig : wfTemplateConfig.getWfStepsConfig().getWfStepConfigList()) {
                ctx.addStepFirstPass(wfStepConfig);
            }
            ctx.evaluateFirstPass();

            for (WfStepConfig wfStepConfig : wfTemplateConfig.getWfStepsConfig().getWfStepConfigList()) {
                ctx.addStepSecondPass(wfStepConfig);
            }
        }

        return ctx.getErrorList();
    }

    private static class WfCategoryValidationContext {

        private TaskMonitor taskMonitor;

        private List<UnifyError> errorList;

        private Map<String, WfDocumentValidationContext> wfDocumentConfigs;

        private Map<String, WfMessageConfig> wfMessageConfigs;

        private Set<String> wfTemplateConfigs;

        private int documentCounter;

        private int messageCounter;

        private int templateCounter;

        public WfCategoryValidationContext(TaskMonitor taskMonitor) {
            this.taskMonitor = taskMonitor;
            errorList = new ArrayList<UnifyError>();
            wfDocumentConfigs = new HashMap<String, WfDocumentValidationContext>();
            wfMessageConfigs = new HashMap<String, WfMessageConfig>();
            wfTemplateConfigs = new HashSet<String>();
        }

        public void addError(String errorCode, Object... params) {
            UnifyError ue = new UnifyError(errorCode, params);
            if (taskMonitor != null) {
                taskMonitor.addErrorMessage(ue);
            }

            errorList.add(ue);
        }

        public void addDocument(WfDocumentValidationContext wfDocCtx) {
            String docName = wfDocCtx.getDocName();
            if (!StringUtils.isBlank(docName)) {
                if (!wfDocumentConfigs.containsKey(docName)) {
                    wfDocumentConfigs.put(docName, wfDocCtx);
                }
            }
            documentCounter++;
        }

        public void addMessage(WfMessageConfig wfMessageConfig) {
            String name = wfMessageConfig.getName();
            if (!StringUtils.isBlank(name)) {
                if (!WfNameUtils.isValidName(name)) {
                    addError(WfCategoryErrorConstants.WFCATEGORY_MESSAGE_INVALID_NAME, messageCounter, name);
                }

                if (wfMessageConfigs.containsKey(name)) {
                    addError(WfCategoryErrorConstants.WFCATEGORY_MESSAGE_EXIST, messageCounter, name);
                } else {
                    wfMessageConfigs.put(name, wfMessageConfig);
                }
            } else {
                addError(WfCategoryErrorConstants.WFCATEGORY_MESSAGE_NO_NAME, messageCounter);
            }

            if (StringUtils.isBlank(wfMessageConfig.getDescription())) {
                addError(WfCategoryErrorConstants.WFCATEGORY_MESSAGE_NO_DESC, messageCounter);
            }

            if (StringUtils.isBlank(wfMessageConfig.getSubject())) {
                addError(WfCategoryErrorConstants.WFCATEGORY_MESSAGE_NO_SUBJECT, messageCounter, name);
            }

            if (StringUtils.isBlank(wfMessageConfig.getBody())) {
                addError(WfCategoryErrorConstants.WFCATEGORY_MESSAGE_NO_BODY, messageCounter, name);
            }

            if (StringUtils.isBlank(wfMessageConfig.getDescription())) {
                addError(WfCategoryErrorConstants.WFCATEGORY_MESSAGE_NO_DOCUMENT, messageCounter, name);
            }

            messageCounter++;
        }

        public void addTemplate(WfTemplateConfig wfTemplateConfig) {
            String name = wfTemplateConfig.getName();
            if (!StringUtils.isBlank(name)) {
                if (wfTemplateConfigs.contains(name)) {
                    addError(WfCategoryErrorConstants.WFCATEGORY_TEMPLATE_EXISTS, name);
                } else {
                    wfTemplateConfigs.add(name);
                }
            }

            templateCounter++;
        }

        public void addErrorList(List<UnifyError> errorList) {
            this.errorList.addAll(errorList);
        }

        public List<UnifyError> getErrorList() {
            return errorList;
        }

        public int getDocumentCounter() {
            return documentCounter;
        }

        public int getTemplateCounter() {
            return templateCounter;
        }

        public WfMessageConfig getMessage(String name) {
            return wfMessageConfigs.get(name);
        }

        public WfDocumentValidationContext getDocument(String docName) {
            return wfDocumentConfigs.get(docName);
        }

        public boolean isDocument(String docName) {
            return wfDocumentConfigs.containsKey(docName);
        }
    }

    private static class WfDocumentValidationContext {

        private String docName;

        private TaskMonitor taskMonitor;

        private List<UnifyError> errorList;

        private Map<String, WfFieldConfigInfo> wfFieldConfigs;

        private Set<String> wfClassifierConfigs;

        private Set<String> wfAttachmentConfigs;

        private Set<String> wfBeanMappingConfigs;

        private Set<String> wfTabConfigs;

        private Set<String> wfSectionConfigs;

        private int fieldCounter;

        private int classifierCounter;

        private int attachmentCounter;

        private int beanMappingCounter;

        private int tabCounter;

        private int sectionCounter;

        public WfDocumentValidationContext(String docName, TaskMonitor taskMonitor) {
            this.docName = docName;
            this.taskMonitor = taskMonitor;
            errorList = new ArrayList<UnifyError>();
            wfFieldConfigs = new HashMap<String, WfFieldConfigInfo>();
            wfClassifierConfigs = new HashSet<String>();
            wfAttachmentConfigs = new HashSet<String>();
            wfBeanMappingConfigs = new HashSet<String>();
            wfTabConfigs = new HashSet<String>();
            wfSectionConfigs = new HashSet<String>();
        }

        public String getDocName() {
            return docName;
        }

        public void addError(String errorCode, Object... params) {
            UnifyError ue = new UnifyError(errorCode, params);
            if (taskMonitor != null) {
                taskMonitor.addErrorMessage(ue);
            }

            errorList.add(ue);
        }

        public void addField(WfFieldConfig wfFieldConfig, String parentName) {
            String name = wfFieldConfig.getName();
            if (!StringUtils.isBlank(name)) {
                if (!WfNameUtils.isValidName(name)) {
                    addError(WfDocumentErrorConstants.WFDOCUMENT_FIELD_INVALID_NAME, fieldCounter, name);
                }

                if (wfFieldConfigs.containsKey(name)) {
                    addError(WfDocumentErrorConstants.WFDOCUMENT_FIELD_EXIST, fieldCounter, name);
                } else {
                    wfFieldConfigs.put(name, new WfFieldConfigInfo(wfFieldConfig, parentName));
                }
            } else {
                addError(WfDocumentErrorConstants.WFDOCUMENT_FIELD_NO_NAME, fieldCounter);
            }

            if (StringUtils.isBlank(wfFieldConfig.getDescription())) {
                addError(WfDocumentErrorConstants.WFDOCUMENT_FIELD_NO_DESC, fieldCounter);
            }

            if (wfFieldConfig.getDataType() == null) {
                addError(WfDocumentErrorConstants.WFDOCUMENT_FIELD_NO_TYPE, fieldCounter, name);
            } else if (DataType.COMPLEX.equals(wfFieldConfig.getDataType())) {
                if (!(wfFieldConfig instanceof WfComplexFieldConfig)) {
                    addError(WfDocumentErrorConstants.WFDOCUMENT_FIELD_INVALID_DATATYPE, fieldCounter, name,
                            wfFieldConfig.getDataType());
                }
            }

            fieldCounter++;
        }

        public void addClassifier(WfClassifierConfig wfClassifierConfig) {
            String name = wfClassifierConfig.getName();
            if (!StringUtils.isBlank(name)) {
                if (!WfNameUtils.isValidName(name)) {
                    addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIER_INVALID_NAME, classifierCounter, name);
                }

                if (wfClassifierConfigs.contains(name)) {
                    addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIER_EXIST, classifierCounter, name);
                } else {
                    wfClassifierConfigs.add(name);
                }
            } else {
                addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIER_NO_NAME, classifierCounter);
            }

            if (StringUtils.isBlank(wfClassifierConfig.getDescription())) {
                addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIER_NO_DESC, classifierCounter);
            }

            if (StringUtils.isBlank(wfClassifierConfig.getLogic())
                    && DataUtils.isBlank(wfClassifierConfig.getWfClassifierFilterConfigList())) {
                addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIER_NO_FILTER_LOGIC, classifierCounter, name);
            }

            if (!StringUtils.isBlank(wfClassifierConfig.getLogic())
                    && !DataUtils.isBlank(wfClassifierConfig.getWfClassifierFilterConfigList())) {
                addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIER_HAS_FILTER_LOGIC, classifierCounter, name);
            }

            if (!DataUtils.isBlank(wfClassifierConfig.getWfClassifierFilterConfigList())) {
                int index = 0;
                for (WfClassifierFilterConfig wfClassifierFilterConfig : wfClassifierConfig
                        .getWfClassifierFilterConfigList()) {
                    if (StringUtils.isBlank(wfClassifierFilterConfig.getField())) {
                        addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIERFILTER_NO_FIELD, index, name);
                    } else {
                        validateClassifierFilterFieldRef(wfClassifierFilterConfig.getField(), name, index);
                    }

                    if (wfClassifierFilterConfig.getOp() == null) {
                        addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIERFILTER_NO_OP, index, name);
                    }

                    if (Boolean.TRUE.equals(wfClassifierFilterConfig.getFieldOnly())) {
                        validateClassifierFilterFieldRef(wfClassifierFilterConfig.getValue1(), name, index);
                        validateClassifierFilterFieldRef(wfClassifierFilterConfig.getValue2(), name, index);
                    }
                    index++;
                }
            }
            classifierCounter++;
        }

        public void addAttachment(WfAttachmentConfig wfAttachmentConfig) {
            String name = wfAttachmentConfig.getName();
            if (!StringUtils.isBlank(name)) {
                if (!WfNameUtils.isValidName(name)) {
                    addError(WfDocumentErrorConstants.WFDOCUMENT_ATTACHMENT_INVALID_NAME, attachmentCounter, name);
                }

                if (wfAttachmentConfigs.contains(name)) {
                    addError(WfDocumentErrorConstants.WFDOCUMENT_ATTACHMENT_EXIST, attachmentCounter, name);
                } else {
                    wfAttachmentConfigs.add(name);
                }
            } else {
                addError(WfDocumentErrorConstants.WFDOCUMENT_ATTACHMENT_NO_NAME, attachmentCounter);
            }

            if (StringUtils.isBlank(wfAttachmentConfig.getDescription())) {
                addError(WfDocumentErrorConstants.WFDOCUMENT_ATTACHMENT_NO_DESC, attachmentCounter);
            }

            if (wfAttachmentConfig.getType() == null) {
                addError(WfDocumentErrorConstants.WFDOCUMENT_ATTACHMENT_NO_TYPE, attachmentCounter, name);
            }

            attachmentCounter++;
        }

        public void addBeanMapping(WfBeanMappingConfig wfBeanMappingConfig) {
            String name = wfBeanMappingConfig.getName();
            if (!StringUtils.isBlank(name)) {
                if (!WfNameUtils.isValidName(name)) {
                    addError(WfDocumentErrorConstants.WFDOCUMENT_BEANMAPPING_INVALID_NAME, beanMappingCounter, name);
                }

                if (wfBeanMappingConfigs.contains(name)) {
                    addError(WfDocumentErrorConstants.WFDOCUMENT_BEANMAPPING_EXIST, beanMappingCounter, name);
                } else {
                    wfBeanMappingConfigs.add(name);
                }
            } else {
                addError(WfDocumentErrorConstants.WFDOCUMENT_BEANMAPPING_NO_NAME, beanMappingCounter);
            }

            if (StringUtils.isBlank(wfBeanMappingConfig.getDescription())) {
                addError(WfDocumentErrorConstants.WFDOCUMENT_BEANMAPPING_NO_DESC, beanMappingCounter);
            }

            // if (wfBeanMappingConfig.getType() == null) {
            // addError(WfDocumentErrorConstants.WFDOCUMENT_BEANMAPPING_NO_TYPE,
            // beanMappingCounter, name);
            // }

            if (wfBeanMappingConfig.getBeanType() == null) {
                addError(WfDocumentErrorConstants.WFDOCUMENT_BEANMAPPING_NO_BEANTYPE, beanMappingCounter, name);
            }

            if (DataUtils.isBlank(wfBeanMappingConfig.getFieldMappingList())) {
                addError(WfDocumentErrorConstants.WFDOCUMENT_BEANMAPPING_NO_MAPPINGS, beanMappingCounter, name);
            } else {
                int index = 0;
                for (WfFieldMappingConfig wfFieldMappingConfig : wfBeanMappingConfig.getFieldMappingList()) {
                    if (StringUtils.isBlank(wfFieldMappingConfig.getDocFieldName())) {
                        addError(WfDocumentErrorConstants.WFDOCUMENT_FIELDMAPPING_NO_DOCFIELD, index, name);
                    } else {
                        if (!wfFieldConfigs.containsKey(wfFieldMappingConfig.getDocFieldName())) {
                            addError(WfDocumentErrorConstants.WFDOCUMENT_FIELDMAPPING_UNKNOWN_FIELD, index, name,
                                    wfFieldMappingConfig.getDocFieldName());
                        }
                    }

                    if (StringUtils.isBlank(wfFieldMappingConfig.getBeanFieldName())) {
                        addError(WfDocumentErrorConstants.WFDOCUMENT_FIELDMAPPING_NO_BEANFIELD, index, name);
                    }

                    index++;
                }
            }
            beanMappingCounter++;
        }

        public void addTab(WfFormTabConfig wfFormTabConfig) {
            String name = wfFormTabConfig.getName();
            if (!StringUtils.isBlank(name)) {
                if (!WfNameUtils.isValidName(name)) {
                    addError(WfDocumentErrorConstants.WFDOCUMENT_TAB_INVALID_NAME, tabCounter, name);
                }

                if (wfTabConfigs.contains(name)) {
                    addError(WfDocumentErrorConstants.WFDOCUMENT_TAB_EXIST, tabCounter, name);
                } else {
                    wfTabConfigs.add(name);
                }
            } else {
                addError(WfDocumentErrorConstants.WFDOCUMENT_TAB_NO_NAME, tabCounter);
            }

            if (StringUtils.isBlank(wfFormTabConfig.getDescription())) {
                addError(WfDocumentErrorConstants.WFDOCUMENT_TAB_NO_DESC, tabCounter);
            }

            if (DataUtils.isBlank(wfFormTabConfig.getWfFormSectionConfigList())) {
                addError(WfDocumentErrorConstants.WFDOCUMENT_TAB_NO_SECTIONS, tabCounter, name);
            } else {
                for (WfFormSectionConfig wfFormSectionConfig : wfFormTabConfig.getWfFormSectionConfigList()) {
                    String sectionName = wfFormSectionConfig.getName();
                    if (!StringUtils.isBlank(sectionName)) {
                        if (!WfNameUtils.isValidName(sectionName)) {
                            addError(WfDocumentErrorConstants.WFDOCUMENT_SECTION_INVALID_NAME, sectionCounter,
                                    sectionName);
                        }

                        if (wfSectionConfigs.contains(sectionName)) {
                            addError(WfDocumentErrorConstants.WFDOCUMENT_SECTION_EXIST, sectionCounter, sectionName);
                        } else {
                            wfSectionConfigs.add(sectionName);
                        }
                    } else {
                        addError(WfDocumentErrorConstants.WFDOCUMENT_SECTION_NO_NAME, sectionCounter);
                    }

                    if (StringUtils.isBlank(wfFormSectionConfig.getDescription())) {
                        addError(WfDocumentErrorConstants.WFDOCUMENT_SECTION_NO_DESC, sectionCounter);
                    }

                    boolean isComplexBinding = !StringUtils.isBlank(wfFormSectionConfig.getBinding());
                    if (isComplexBinding) {
                        WfFieldConfigInfo wfFieldConfigInfo = wfFieldConfigs.get(wfFormSectionConfig.getBinding());
                        if (wfFieldConfigInfo == null || !wfFieldConfigInfo.isComplex()) {
                            addError(WfDocumentErrorConstants.WFDOCUMENT_SECTION_UNKNOWN_COMPLEX, sectionCounter,
                                    sectionName, wfFormSectionConfig.getBinding());
                        }
                    }

                    Set<String> bindings = new HashSet<String>();
                    if (DataUtils.isBlank(wfFormSectionConfig.getWfFormFieldConfigList())) {
                        addError(WfDocumentErrorConstants.WFDOCUMENT_SECTION_NO_FIELDS, sectionCounter, sectionName);
                    } else {
                        int index = 0;
                        for (WfFormFieldConfig wfFormFieldConfig : wfFormSectionConfig.getWfFormFieldConfigList()) {
                            String binding = wfFormFieldConfig.getBinding();
                            if (bindings.contains(binding)) {
                                addError(WfDocumentErrorConstants.WFDOCUMENT_FORMFIELD_MULTIPLE_BINDING, index,
                                        sectionName, binding);
                            } else {
                                bindings.add(binding);
                            }

                            if (StringUtils.isBlank(binding)) {
                                addError(WfDocumentErrorConstants.WFDOCUMENT_FORMFIELD_NO_BINDING, index, sectionName);
                            } else {
                                WfFieldConfigInfo wfFieldConfigInfo = wfFieldConfigs.get(binding);
                                if (wfFieldConfigInfo == null) {
                                    addError(WfDocumentErrorConstants.WFDOCUMENT_FORMFIELD_UNKNOWN_FIELD, index,
                                            sectionName, binding);
                                } else {
                                    if (isComplexBinding) {
                                        if (!wfFormSectionConfig.getBinding()
                                                .equals(wfFieldConfigInfo.getParentName())) {
                                            addError(WfDocumentErrorConstants.WFDOCUMENT_FORMFIELD_NOT_COMPLEX_FIELD,
                                                    index, sectionName, binding);
                                        }
                                    }
                                }
                            }

                            if (StringUtils.isBlank(wfFormFieldConfig.getEditorUpl())) {
                                addError(WfDocumentErrorConstants.WFDOCUMENT_FORMFIELD_NO_EDITOR, index, sectionName,
                                        binding);
                            }
                        }
                    }
                    sectionCounter++;
                }
            }

            tabCounter++;
        }

        private void validateClassifierFilterFieldRef(String fieldName, String name, int index) {
            WfFieldConfigInfo wfFieldConfigInfo = wfFieldConfigs.get(fieldName);
            if (wfFieldConfigInfo == null) {
                addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIERFILTER_UNKNOWN_FIELD, index, name, fieldName);
            } else if (DataType.COMPLEX.equals(wfFieldConfigInfo.getWfFieldConfig().getDataType())) {
                addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIERFILTER_COMPLEX_FIELD, index, name, fieldName);
            }
        }

        public List<UnifyError> getErrorList() {
            return errorList;
        }

        public int getFieldCounter() {
            return fieldCounter;
        }

        public boolean isClassifier(String classifierName) {
            return wfClassifierConfigs.contains(classifierName);
        }

        public boolean isBeanMapping(String beanMappingName) {
            return wfBeanMappingConfigs.contains(beanMappingName);
        }
    }

    private static class WfTemplateValidationContext {

        private WfCategoryValidationContext wfCatCtx;

        private List<UnifyError> errorList;

        private Map<String, WfTemplateDocConfig> wfTemplateDocConfigs;

        private Map<String, WfStepConfig> wfStepConfigs;

        private int startCount;

        private int manualCount;

        private int endCount;

        private int templateDocCounter;

        private int stepCounter;

        public WfTemplateValidationContext(WfCategoryValidationContext wfCatCtx) {
            this.wfCatCtx = wfCatCtx;
            errorList = new ArrayList<UnifyError>();
            wfStepConfigs = new HashMap<String, WfStepConfig>();
            wfTemplateDocConfigs = new HashMap<String, WfTemplateDocConfig>();
        }

        public void addError(String errorCode, Object... params) {
            UnifyError ue = new UnifyError(errorCode, params);
            if (wfCatCtx.taskMonitor != null) {
                wfCatCtx.taskMonitor.addErrorMessage(ue);
            }
            errorList.add(ue);
        }

        public boolean addTemplateDoc(WfTemplateDocConfig wfTemplateDocConfig) {
            String docName = wfTemplateDocConfig.getName();
            if (!StringUtils.isBlank(docName)) {
                if (wfTemplateDocConfigs.containsKey(docName)) {
                    addError(WfTemplateErrorConstants.WFTEMPLATE_DOC_EXIST, templateDocCounter, docName);
                } else {
                    wfTemplateDocConfigs.put(docName, wfTemplateDocConfig);
                }

                return true;
            } else {
                addError(WfTemplateErrorConstants.WFTEMPLATE_DOC_NO_NAME, templateDocCounter);
            }

            return false;
        }

        public void addStepFirstPass(WfStepConfig wfStepConfig) {
            String name = wfStepConfig.getName();
            if (!StringUtils.isBlank(name)) {
                if (!WfNameUtils.isValidName(name)) {
                    addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_INVALID_NAME, stepCounter, name);
                }

                if (wfStepConfigs.containsKey(name)) {
                    addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_EXIST, stepCounter, name);
                } else {
                    wfStepConfigs.put(name, wfStepConfig);
                }
            } else {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_NO_NAME, stepCounter);
            }

            if (StringUtils.isBlank(wfStepConfig.getDescription())) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_NO_DESC, stepCounter);
            }

            if (wfStepConfig.getType() == null) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_NO_TYPE, stepCounter, name);
            }

            if (wfStepConfig.getParticipant() == null) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_NO_PARTICIPANT, stepCounter, name);
            } else {
                if (!wfStepConfig.getParticipant().isParticipant()) {
                    if (wfStepConfig.getType() != null && wfStepConfig.getType().isUserInteractive()) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_NO_ACTUAL_PARTICIPANT, stepCounter, name,
                                wfStepConfig.getType());
                    }
                }
            }

            if (wfStepConfig.getPriority() == null) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_NO_PRIORITY, stepCounter, name);
            }

            if (WorkflowStepType.START.equals(wfStepConfig.getType())) {
                startCount++;
            }

            if (WorkflowStepType.MANUAL.equals(wfStepConfig.getType())) {
                manualCount++;
            }

            if (WorkflowStepType.END.equals(wfStepConfig.getType())) {
                endCount++;
            }

            stepCounter++;
        }

        public void addStepSecondPass(WfStepConfig wfStepConfig) {
            WorkflowStepType type = wfStepConfig.getType();
            if (type != null) {
                switch (type) {
                    case AUTOMATIC:
                        validateEnrichments(wfStepConfig);
                        validatePolicies(wfStepConfig);
                        validateRecordActions(wfStepConfig);
                        validateAlerts(wfStepConfig);
                        validateRoutings(wfStepConfig);
                        invalidateUserActions(wfStepConfig);
                        invalidateFormPrivileges(wfStepConfig);
                        break;
                    case END:
                        validateAlerts(wfStepConfig);
                        invalidateEnrichments(wfStepConfig);
                        invalidateRoutings(wfStepConfig);
                        invalidatePolicies(wfStepConfig);
                        invalidateRecordActions(wfStepConfig);
                        invalidateUserActions(wfStepConfig);
                        invalidateFormPrivileges(wfStepConfig);
                        break;
                    case INTERACTIVE:
                        validateAlerts(wfStepConfig);
                        validateUserActions(wfStepConfig);
                        validateFormPrivileges(wfStepConfig);
                        invalidateEnrichments(wfStepConfig);
                        invalidatePolicies(wfStepConfig);
                        invalidateRoutings(wfStepConfig);
                        invalidateRecordActions(wfStepConfig);
                        break;
                    case MANUAL:
                        validateAlerts(wfStepConfig);
                        validateFormPrivileges(wfStepConfig);
                        invalidateEnrichments(wfStepConfig);
                        invalidateRoutings(wfStepConfig);
                        invalidatePolicies(wfStepConfig);
                        invalidateRecordActions(wfStepConfig);
                        invalidateUserActions(wfStepConfig);
                        break;
                    case START:
                        validateEnrichments(wfStepConfig);
                        validateAlerts(wfStepConfig);
                        validateRoutings(wfStepConfig);
                        invalidatePolicies(wfStepConfig);
                        invalidateRecordActions(wfStepConfig);
                        invalidateUserActions(wfStepConfig);
                        invalidateFormPrivileges(wfStepConfig);
                        break;
                    default:
                        break;
                }
            }

            stepCounter++;
        }

        public List<UnifyError> getErrorList() {
            return errorList;
        }

        public void evaluateFirstPass() {
            if (startCount == 0) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_NO_START);
            } else if (startCount > 1) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_MULTIPLE_START);
            }

            if (manualCount > 1) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_MULTIPLE_MANUAL);
            }

            if (endCount == 0) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_NO_END);
            } else if (endCount > 1) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_MULTIPLE_END);
            }

            stepCounter = 0;
        }

        private void validateRoutings(WfStepConfig wfStepConfig) {
            if (wfStepConfig.getWfRoutingsConfig() == null
                    || DataUtils.isBlank(wfStepConfig.getWfRoutingsConfig().getWfRoutingConfigList())) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_NO_ROUTINGS, stepCounter, wfStepConfig.getName(),
                        wfStepConfig.getType());
            } else {
                Set<String> names = new HashSet<String>();
                int index = 0;
                for (WfRoutingConfig wfRoutingConfig : wfStepConfig.getWfRoutingsConfig().getWfRoutingConfigList()) {
                    String routingName = wfRoutingConfig.getName();
                    if (!StringUtils.isBlank(routingName)) {
                        if (!WfNameUtils.isValidName(routingName)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_INVALID_NAME, index,
                                    wfStepConfig.getName(), routingName);
                        }

                        if (names.contains(routingName)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_EXIST, index, wfStepConfig.getName(),
                                    routingName);
                        } else {
                            names.add(routingName);
                        }
                    } else {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_NO_NAME, index, wfStepConfig.getName());
                    }

                    if (StringUtils.isBlank(wfRoutingConfig.getDescription())) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_NO_DESC, index, wfStepConfig.getName());
                    }

                    if (!StringUtils.isBlank(wfRoutingConfig.getClassifierName())) {
                        if (StringUtils.isBlank(wfRoutingConfig.getDocument())) {//
                            addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_WITH_CLASSIFIER_NO_DOC, index,
                                    wfStepConfig.getName(), wfRoutingConfig.getClassifierName());
                        } else {
                            if (!wfTemplateDocConfigs.containsKey(wfRoutingConfig.getDocument())) {
                                addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_UNKNOWN_TEMPLATE_DOC, index,
                                        wfStepConfig.getName(), routingName, wfRoutingConfig.getDocument());
                            } else {
                                WfDocumentValidationContext wfDocCtx =
                                        wfCatCtx.getDocument(wfRoutingConfig.getDocument());
                                if (wfDocCtx != null && !wfDocCtx.isClassifier(wfRoutingConfig.getClassifierName())) {
                                    addError(
                                            WfTemplateErrorConstants.WFTEMPLATE_ROUTING_UNKNOWN_CATEGORY_DOC_CLASSIFIER,
                                            index, wfStepConfig.getName(), routingName, wfRoutingConfig.getDocument(),
                                            wfRoutingConfig.getClassifierName());
                                }
                            }

                        }
                    }

                    String target = wfRoutingConfig.getTargetStepName();
                    if (StringUtils.isBlank(target)) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_NO_TARGET, index, wfStepConfig.getName(),
                                routingName);
                    } else {
                        WfStepConfig targetWfStepConfig = wfStepConfigs.get(target);
                        if (targetWfStepConfig == null) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_TARGET_UNKNOWN, index,
                                    wfStepConfig.getName(), routingName, target);
                        } else {
                            if (WorkflowStepType.START.equals(targetWfStepConfig.getType())) {
                                addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_TARGET_START, index,
                                        wfStepConfig.getName(), routingName, target);
                            }

                            if (target.equals(wfStepConfig.getName())) {
                                addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_TARGET_SELF, index,
                                        wfStepConfig.getName(), routingName, target);
                            }
                        }
                    }

                    index++;
                }
            }
        }

        private void invalidateRoutings(WfStepConfig wfStepConfig) {
            if (wfStepConfig.getWfRoutingsConfig() != null
                    && !DataUtils.isBlank(wfStepConfig.getWfRoutingsConfig().getWfRoutingConfigList())) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_ROUTINGS_EXIST, stepCounter, wfStepConfig.getName(),
                        wfStepConfig.getType());
            }
        }

        private void validateUserActions(WfStepConfig wfStepConfig) {
            if (wfStepConfig.getWfUserActionsConfig() == null
                    || DataUtils.isBlank(wfStepConfig.getWfUserActionsConfig().getWfUserActionConfigList())) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_NO_USERACTIONS, stepCounter, wfStepConfig.getName(),
                        wfStepConfig.getType());
            } else {
                Set<String> names = new HashSet<String>();
                int index = 0;
                for (WfUserActionConfig wfUserActionConfig : wfStepConfig.getWfUserActionsConfig()
                        .getWfUserActionConfigList()) {
                    String name = wfUserActionConfig.getName();
                    if (!StringUtils.isBlank(name)) {
                        if (!WfNameUtils.isValidName(name)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_USERACTION_INVALID_NAME, index,
                                    wfStepConfig.getName(), name);
                        }

                        if (names.contains(name)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_USERACTION_EXIST, index,
                                    wfStepConfig.getName(), name);
                        } else {
                            names.add(name);
                        }
                    } else {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_USERACTION_NO_NAME, index, wfStepConfig.getName());
                    }

                    if (StringUtils.isBlank(wfUserActionConfig.getDescription())) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_USERACTION_NO_DESC, index, wfStepConfig.getName());
                    }

                    if (wfUserActionConfig.getCommentRequirement() == null) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_USERACTION_NO_COMMENT_TYPE, index,
                                wfStepConfig.getName(), name);
                    }

                    String target = wfUserActionConfig.getTargetStepName();
                    if (StringUtils.isBlank(target)) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_USERACTION_NO_TARGET, index,
                                wfStepConfig.getName(), name);
                    } else {
                        WfStepConfig targetWfStepConfig = wfStepConfigs.get(target);
                        if (targetWfStepConfig == null) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_USERACTION_TARGET_UNKNOWN, index,
                                    wfStepConfig.getName(), name, target);
                        } else {
                            if (WorkflowStepType.START.equals(targetWfStepConfig.getType())) {
                                addError(WfTemplateErrorConstants.WFTEMPLATE_USERACTION_TARGET_START, index,
                                        wfStepConfig.getName(), name, target);
                            }

                            if (target.equals(wfStepConfig.getName())) {
                                addError(WfTemplateErrorConstants.WFTEMPLATE_USERACTION_TARGET_SELF, index,
                                        wfStepConfig.getName(), name, target);
                            }
                        }
                    }

                    index++;
                }
            }
        }

        private void invalidateUserActions(WfStepConfig wfStepConfig) {
            if (wfStepConfig.getWfUserActionsConfig() != null
                    && !DataUtils.isBlank(wfStepConfig.getWfUserActionsConfig().getWfUserActionConfigList())) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_USERACTIONS_EXIST, stepCounter,
                        wfStepConfig.getName(), wfStepConfig.getType());
            }
        }

        private void validateRecordActions(WfStepConfig wfStepConfig) {
            if (wfStepConfig.getWfRecordActionsConfig() != null
                    && !DataUtils.isBlank(wfStepConfig.getWfRecordActionsConfig().getWfRecordActionConfigList())) {
                Set<String> names = new HashSet<String>();
                int index = 0;
                for (WfRecordActionConfig wfRecordActionConfig : wfStepConfig.getWfRecordActionsConfig()
                        .getWfRecordActionConfigList()) {
                    String recordActionName = wfRecordActionConfig.getName();
                    if (!StringUtils.isBlank(recordActionName)) {
                        if (!WfNameUtils.isValidName(recordActionName)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_RECORDACTION_INVALID_NAME, index,
                                    wfStepConfig.getName(), recordActionName);
                        }

                        if (names.contains(recordActionName)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_RECORDACTION_EXIST, index,
                                    wfStepConfig.getName(), recordActionName);
                        } else {
                            names.add(recordActionName);
                        }
                    } else {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_RECORDACTION_NO_NAME, index,
                                wfStepConfig.getName());
                    }

                    if (StringUtils.isBlank(wfRecordActionConfig.getDescription())) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_RECORDACTION_NO_DESC, index,
                                wfStepConfig.getName());
                    }

                    if (wfRecordActionConfig.getActionType() == null) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_RECORDACTION_NO_TYPE, index,
                                wfStepConfig.getName(), recordActionName);
                    }

                    if (StringUtils.isBlank(wfRecordActionConfig.getDocMappingName())) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_RECORDACTION_NO_MAPPING, index,
                                wfStepConfig.getName(), recordActionName);
                    } else {
                        if (StringUtils.isBlank(wfRecordActionConfig.getDocument())) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_RECORDACTION_WITH_NO_DOC, index,
                                    wfStepConfig.getName());
                        } else {
                            if (!wfTemplateDocConfigs.containsKey(wfRecordActionConfig.getDocument())) {
                                addError(WfTemplateErrorConstants.WFTEMPLATE_RECORDACTION_UNKNOWN_TEMPLATE_DOC, index,
                                        wfStepConfig.getName(), recordActionName, wfRecordActionConfig.getDocument());
                            } else {
                                WfDocumentValidationContext wfDocCtx =
                                        wfCatCtx.getDocument(wfRecordActionConfig.getDocument());
                                if (wfDocCtx != null
                                        && !wfDocCtx.isBeanMapping(wfRecordActionConfig.getDocMappingName())) {
                                    addError(
                                            WfTemplateErrorConstants.WFTEMPLATE_RECORDACTION_UNKNOWN_CATEGORY_DOC_MAPPING,
                                            index, wfStepConfig.getName(), recordActionName,
                                            wfRecordActionConfig.getDocument(),
                                            wfRecordActionConfig.getDocMappingName());
                                }
                            }
                        }
                    }

                    index++;
                }
            }
        }

        private void invalidateRecordActions(WfStepConfig wfStepConfig) {
            if (wfStepConfig.getWfRecordActionsConfig() != null
                    && !DataUtils.isBlank(wfStepConfig.getWfRecordActionsConfig().getWfRecordActionConfigList())) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_RECORDACTIONS_EXIST, stepCounter,
                        wfStepConfig.getName(), wfStepConfig.getType());
            }
        }

        private void validateFormPrivileges(WfStepConfig wfStepConfig) {
            if (wfStepConfig.getWfFormPrivilegesConfig() != null
                    && !DataUtils.isBlank(wfStepConfig.getWfFormPrivilegesConfig().getWfFormPrivilegesConfigList())) {
                int index = 0;
                for (WfFormPrivilegeConfig wfFormPrivilegeConfig : wfStepConfig.getWfFormPrivilegesConfig()
                        .getWfFormPrivilegesConfigList()) {
                    if (StringUtils.isBlank(wfFormPrivilegeConfig.getDocument())) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_FORMPRIVILEGE_WITH_NO_DOC, index,
                                wfStepConfig.getName());
                    } else {
                        if (!wfTemplateDocConfigs.containsKey(wfFormPrivilegeConfig.getDocument())) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_FORMPRIVILEGE_UNKNOWN_TEMPLATE_DOC, index,
                                    wfStepConfig.getName(), wfFormPrivilegeConfig.getName(),
                                    wfFormPrivilegeConfig.getDocument());
                        }
                    }

                    if (StringUtils.isBlank(wfFormPrivilegeConfig.getName())) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_FORMPRIVILEGE_NO_ELEMENT_NAME, index,
                                wfStepConfig.getName());
                    }

                    if (wfFormPrivilegeConfig.getType() == null) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_FORMPRIVILEGE_NO_ELEMENT_TYPE, index,
                                wfStepConfig.getName());
                    }

                    index++;
                }
            }
        }

        private void invalidateFormPrivileges(WfStepConfig wfStepConfig) {
            if (wfStepConfig.getWfFormPrivilegesConfig() != null
                    && !DataUtils.isBlank(wfStepConfig.getWfFormPrivilegesConfig().getWfFormPrivilegesConfigList())) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_FORMPRIVILEGES_EXIST, stepCounter,
                        wfStepConfig.getName(), wfStepConfig.getType());
            }
        }

        private void validateEnrichments(WfStepConfig wfStepConfig) {
            if (wfStepConfig.getWfEnrichmentsConfig() != null
                    && !DataUtils.isBlank(wfStepConfig.getWfEnrichmentsConfig().getWfEnrichmentConfigList())) {
                Set<String> names = new HashSet<String>();
                int index = 0;
                for (WfEnrichmentConfig wfEnrichmentConfig : wfStepConfig.getWfEnrichmentsConfig()
                        .getWfEnrichmentConfigList()) {
                    String name = wfEnrichmentConfig.getName();
                    if (!StringUtils.isBlank(name)) {
                        if (!WfNameUtils.isValidName(name)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_ENRICHMENT_INVALID_NAME, index,
                                    wfStepConfig.getName(), name);
                        }

                        if (names.contains(name)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_ENRICHMENT_EXIST, index,
                                    wfStepConfig.getName(), name);
                        } else {
                            names.add(name);
                        }
                    } else {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_ENRICHMENT_NO_NAME, index, wfStepConfig.getName());
                    }

                    if (StringUtils.isBlank(wfEnrichmentConfig.getDescription())) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_ENRICHMENT_NO_DESC, index, wfStepConfig.getName());
                    }

                    if (!StringUtils.isBlank(wfEnrichmentConfig.getDocument())
                            && !wfTemplateDocConfigs.containsKey(wfEnrichmentConfig.getDocument())) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_ENRICHMENT_UNKNOWN_TEMPLATE_DOC, index,
                                wfStepConfig.getName(), wfEnrichmentConfig.getName(), wfEnrichmentConfig.getDocument());
                    }

                    if (StringUtils.isBlank(wfEnrichmentConfig.getLogic())) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_ENRICHMENT_NO_LOGIC, index, wfStepConfig.getName(),
                                name);
                    }

                    index++;
                }
            }
        }

        private void invalidateEnrichments(WfStepConfig wfStepConfig) {
            if (wfStepConfig.getWfEnrichmentsConfig() != null
                    && !DataUtils.isBlank(wfStepConfig.getWfEnrichmentsConfig().getWfEnrichmentConfigList())) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_ENRICHMENTS_EXIST, stepCounter,
                        wfStepConfig.getName(), wfStepConfig.getType());
            }
        }

        private void validatePolicies(WfStepConfig wfStepConfig) {
            if (wfStepConfig.getWfPoliciesConfig() != null
                    && !DataUtils.isBlank(wfStepConfig.getWfPoliciesConfig().getWfPolicyConfigList())) {
                Set<String> names = new HashSet<String>();
                int index = 0;
                for (WfPolicyConfig wfPolicyConfig : wfStepConfig.getWfPoliciesConfig().getWfPolicyConfigList()) {
                    String name = wfPolicyConfig.getName();
                    if (!StringUtils.isBlank(name)) {
                        if (!WfNameUtils.isValidName(name)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_POLICY_INVALID_NAME, index,
                                    wfStepConfig.getName(), name);
                        }

                        if (names.contains(name)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_POLICY_EXIST, index, wfStepConfig.getName(),
                                    name);
                        } else {
                            names.add(name);
                        }
                    } else {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_POLICY_NO_NAME, index, wfStepConfig.getName());
                    }

                    if (StringUtils.isBlank(wfPolicyConfig.getDescription())) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_POLICY_NO_DESC, index, wfStepConfig.getName());
                    }

                    if (!StringUtils.isBlank(wfPolicyConfig.getDocument())
                            && !wfTemplateDocConfigs.containsKey(wfPolicyConfig.getDocument())) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_POLICY_UNKNOWN_TEMPLATE_DOC, index,
                                wfStepConfig.getName(), wfPolicyConfig.getName(), wfPolicyConfig.getDocument());
                    }

                    if (StringUtils.isBlank(wfPolicyConfig.getLogic())) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_POLICY_NO_LOGIC, index, wfStepConfig.getName(),
                                name);
                    }

                    index++;
                }
            }
        }

        private void invalidatePolicies(WfStepConfig wfStepConfig) {
            if (wfStepConfig.getWfPoliciesConfig() != null
                    && !DataUtils.isBlank(wfStepConfig.getWfPoliciesConfig().getWfPolicyConfigList())) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_STEP_POLICIES_EXIST, stepCounter, wfStepConfig.getName(),
                        wfStepConfig.getType());
            }
        }

        private void validateAlerts(WfStepConfig wfStepConfig) {
            if (wfStepConfig.getWfAlertsConfig() != null
                    && !DataUtils.isBlank(wfStepConfig.getWfAlertsConfig().getWfAlertConfigList())) {
                Set<String> names = new HashSet<String>();
                int index = 0;
                for (WfAlertConfig wfAlertConfig : wfStepConfig.getWfAlertsConfig().getWfAlertConfigList()) {
                    String name = wfAlertConfig.getName();
                    if (!StringUtils.isBlank(name)) {
                        if (!WfNameUtils.isValidName(name)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_ALERT_INVALID_NAME, index,
                                    wfStepConfig.getName(), name);
                        }

                        if (names.contains(name)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_ALERT_EXIST, index, wfStepConfig.getName(),
                                    name);
                        } else {
                            names.add(name);
                        }
                    } else {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_ALERT_NO_NAME, index, wfStepConfig.getName());
                    }

                    if (StringUtils.isBlank(wfAlertConfig.getDescription())) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_ALERT_NO_DESC, index, wfStepConfig.getName());
                    }

                    if (wfAlertConfig.getType() == null) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_ALERT_NO_TYPE, index, wfStepConfig.getName(),
                                name);
                    }

                    if (StringUtils.isBlank(wfAlertConfig.getMessage())) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_ALERT_NO_MESSAGE, index, wfStepConfig.getName(),
                                name);
                    } else {
                        WfMessageConfig wfMessageConfig = wfCatCtx.getMessage(wfAlertConfig.getMessage());
                        if (wfMessageConfig == null) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_ALERT_MESSAGE_UNKNOWN, index,
                                    wfStepConfig.getName(), name, wfAlertConfig.getMessage());
                        } else if (!wfTemplateDocConfigs.containsKey(wfMessageConfig.getDocument())) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_ALERT_MESSAGE_INCOMPATIBLE, index,
                                    wfStepConfig.getName(), name, wfAlertConfig.getMessage(),
                                    wfMessageConfig.getDocument());
                        }
                    }

                    index++;
                }
            }
        }

    }

    private static class WfFieldConfigInfo {

        private WfFieldConfig wfFieldConfig;

        private String parentName;

        public WfFieldConfigInfo(WfFieldConfig wfFieldConfig, String parentName) {
            this.wfFieldConfig = wfFieldConfig;
            this.parentName = parentName;
        }

        public WfFieldConfig getWfFieldConfig() {
            return wfFieldConfig;
        }

        public String getParentName() {
            return parentName;
        }

        public boolean isComplex() {
            return DataType.COMPLEX.equals(wfFieldConfig.getDataType());
        }
    }
}
