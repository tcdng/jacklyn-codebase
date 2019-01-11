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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tcdng.jacklyn.shared.xml.config.workflow.WfCategoryConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocumentConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfTemplateConfig;
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.UnifyException;
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
        WfCategoryValidationContext ctx = new WfCategoryValidationContext(taskMonitor, wfCategoryConfig.getName(),
                wfCategoryConfig.getVersion());
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
                ctx.addDocument(wfDocumentConfig);
                List<UnifyError> errorList = WfDocumentConfigUtils.validate(wfDocumentConfig);
                if (!errorList.isEmpty()) {
                    ctx.addError(WfCategoryErrorConstants.WFCATEGORY_DOCUMENT_ERRORS, ctx.getDocumentCounter());
                    ctx.addErrorList(errorList);
                }
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
                List<UnifyError> errorList = WfTemplateConfigUtils.validate(wfTemplateConfig);
                if (!errorList.isEmpty()) {
                    ctx.addError(WfCategoryErrorConstants.WFCATEGORY_TEMPLATE_ERRORS, ctx.getTemplateCounter());
                    ctx.addErrorList(errorList);
                }
            }
        }

        return ctx.getErrorList();
    }

    private static class WfCategoryValidationContext {

        private TaskMonitor taskMonitor;

        private List<UnifyError> errorList;

        private Set<String> wfDocumentConfigs;

        private Set<String> wfTemplateConfigs;

        private String namePrefix;

        private String version;

        private int documentCounter;

        private int templateCounter;

        public WfCategoryValidationContext(TaskMonitor taskMonitor, String categoryName, String version) {
            this.taskMonitor = taskMonitor;
            if (categoryName != null) {
                namePrefix = categoryName + '.';
            }

            this.version = version;
            errorList = new ArrayList<UnifyError>();
            wfDocumentConfigs = new HashSet<String>();
            wfTemplateConfigs = new HashSet<String>();
        }

        public void addError(String errorCode, Object... params) {
            UnifyError ue = new UnifyError(errorCode, params);
            if (taskMonitor != null) {
                taskMonitor.addErrorMessage(ue);
            }

            errorList.add(ue);
        }

        public void addDocument(WfDocumentConfig wfDocumentConfig) {
            String name = wfDocumentConfig.getName();
            if (!StringUtils.isBlank(name)) {
                if (namePrefix != null && !name.startsWith(namePrefix)) {
                    addError(WfCategoryErrorConstants.WFCATEGORY_DOCUMENT_NAME_NOT_COMPATIBLE, name);
                }

                if (wfDocumentConfigs.contains(name)) {
                    addError(WfCategoryErrorConstants.WFCATEGORY_DOCUMENT_EXISTS, name);
                } else {
                    wfDocumentConfigs.add(name);
                }
            }

            if (version != null && wfDocumentConfig.getVersion() != null) {
                if (!version.equals(wfDocumentConfig.getVersion())) {
                    addError(WfCategoryErrorConstants.WFCATEGORY_DOCUMENT_VERSION_NOT_COMPATIBLE, documentCounter,
                            version, wfDocumentConfig.getVersion());
                }
            }
            documentCounter++;
        }

        public void addTemplate(WfTemplateConfig wfTemplateConfig) {
            String name = wfTemplateConfig.getName();
            if (!StringUtils.isBlank(name)) {
                if (namePrefix != null && !name.startsWith(namePrefix)) {
                    addError(WfCategoryErrorConstants.WFCATEGORY_TEMPLATE_NAME_NOT_COMPATIBLE, name);
                }

                if (wfTemplateConfigs.contains(name)) {
                    addError(WfCategoryErrorConstants.WFCATEGORY_TEMPLATE_EXISTS, name);
                } else {
                    wfTemplateConfigs.add(name);
                }
            }

            if (version != null && wfTemplateConfig.getVersion() != null) {
                if (!version.equals(wfTemplateConfig.getVersion())) {
                    addError(WfCategoryErrorConstants.WFCATEGORY_TEMPLATE_VERSION_NOT_COMPATIBLE, templateCounter,
                            version, wfTemplateConfig.getVersion());
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
    }
}
