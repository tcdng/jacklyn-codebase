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

import com.tcdng.jacklyn.shared.xml.config.workflow.WfTemplateConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfUserActionConfig;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepType;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfAlertConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfEnrichmentConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormPrivilegeConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfMessageConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfPolicyConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfRecordActionConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfRoutingConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfStepConfig;
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.XMLConfigUtils;

/**
 * Workflow template XML configuration utilities.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public final class WfTemplateConfigUtils {

    private WfTemplateConfigUtils() {

    }

    public static WfTemplateConfig readWfTemplateConfig(File file) throws UnifyException {
        return XMLConfigUtils.readXmlConfig(WfTemplateConfig.class, file);
    }

    public static WfTemplateConfig readWfTemplateConfig(InputStream in) throws UnifyException {
        try {
            return XMLConfigUtils.readXmlConfig(WfTemplateConfig.class, in);
        } finally {
            IOUtils.close(in);
        }
    }

    public static WfTemplateConfig readWfTemplateConfig(Reader reader) throws UnifyException {
        try {
            return XMLConfigUtils.readXmlConfig(WfTemplateConfig.class, reader);
        } finally {
            IOUtils.close(reader);
        }
    }

    public static WfTemplateConfig readWfTemplateConfig(String resourceName) throws UnifyException {
        InputStream inputStream = null;
        try {
            inputStream = IOUtils.openClassLoaderResourceInputStream(resourceName);
            return XMLConfigUtils.readXmlConfig(WfTemplateConfig.class, inputStream);
        } finally {
            IOUtils.close(inputStream);
        }
    }

    public static List<UnifyError> validate(WfTemplateConfig wfTemplateConfig) throws UnifyException {
        return WfTemplateConfigUtils.validate(null, wfTemplateConfig);
    }

    public static List<UnifyError> validate(TaskMonitor taskMonitor, WfTemplateConfig wfTemplateConfig)
            throws UnifyException {
        WfTemplateValidationContext ctx = new WfTemplateValidationContext(taskMonitor);
        // Template name and description
        String name = wfTemplateConfig.getName();
        if (StringUtils.isBlank(name)) {
            ctx.addError(WfTemplateErrorConstants.WFTEMPLATE_NO_NAME);
        } else {
            String[] names = StringUtils.dotSplit(name);
            if (names.length != 2 || !WfNameUtils.isValidName(names[0]) || !WfNameUtils.isValidName(names[1])) {
                ctx.addError(WfTemplateErrorConstants.WFTEMPLATE_INVALID_NAME_FORMAT, name);
            }
        }

        if (StringUtils.isBlank(wfTemplateConfig.getDescription())) {
            ctx.addError(WfTemplateErrorConstants.WFTEMPLATE_NO_DESC);
        }

        if (StringUtils.isBlank(wfTemplateConfig.getDocument())) {
            ctx.addError(WfTemplateErrorConstants.WFTEMPLATE_NO_DOCUMENT);
        }

        if (StringUtils.isBlank(wfTemplateConfig.getVersion())) {
            ctx.addError(WfTemplateErrorConstants.WFTEMPLATE_NO_VERSION);
        }

        // Messages
        if (wfTemplateConfig.getWfMessagesConfig() != null
                && !DataUtils.isBlank(wfTemplateConfig.getWfMessagesConfig().getWfMessageConfigList())) {
            for (WfMessageConfig wfMessageConfig : wfTemplateConfig.getWfMessagesConfig().getWfMessageConfigList()) {
                ctx.addMessage(wfMessageConfig);
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

    public static class WfTemplateValidationContext {

        private TaskMonitor taskMonitor;

        private List<UnifyError> errorList;

        private Map<String, WfStepConfig> wfStepConfigs;

        private Set<String> wfMessageConfigs;

        private int startCount;

        private int manualCount;

        private int endCount;

        private int stepCounter;

        private int messageCounter;

        public WfTemplateValidationContext(TaskMonitor taskMonitor) {
            this.taskMonitor = taskMonitor;
            errorList = new ArrayList<UnifyError>();
            wfStepConfigs = new HashMap<String, WfStepConfig>();
            wfMessageConfigs = new HashSet<String>();
        }

        public void addError(String errorCode, Object... params) {
            UnifyError ue = new UnifyError(errorCode, params);
            if (taskMonitor != null) {
                taskMonitor.addErrorMessage(ue);
            }
            errorList.add(ue);
        }

        public void addMessage(WfMessageConfig wfMessageConfig) {
            String name = wfMessageConfig.getName();
            if (!StringUtils.isBlank(name)) {
                if (!WfNameUtils.isValidName(name)) {
                    addError(WfTemplateErrorConstants.WFTEMPLATE_MESSAGE_INVALID_NAME, messageCounter, name);
                }

                if (wfMessageConfigs.contains(name)) {
                    addError(WfTemplateErrorConstants.WFTEMPLATE_MESSAGE_EXIST, messageCounter, name);
                } else {
                    wfMessageConfigs.add(name);
                }
            } else {
                addError(WfTemplateErrorConstants.WFTEMPLATE_MESSAGE_NO_NAME, messageCounter);
            }

            if (StringUtils.isBlank(wfMessageConfig.getDescription())) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_MESSAGE_NO_DESC, messageCounter);
            }

            if (StringUtils.isBlank(wfMessageConfig.getSubject())) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_MESSAGE_NO_SUBJECT, messageCounter, name);
            }

            if (StringUtils.isBlank(wfMessageConfig.getBody())) {
                addError(WfTemplateErrorConstants.WFTEMPLATE_MESSAGE_NO_BODY, messageCounter, name);
            }

            messageCounter++;
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
                case RECEPTACLE:
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
                    String name = wfRoutingConfig.getName();
                    if (!StringUtils.isBlank(name)) {
                        if (!WfNameUtils.isValidName(name)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_INVALID_NAME, index,
                                    wfStepConfig.getName(), name);
                        }

                        if (names.contains(name)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_EXIST, index, wfStepConfig.getName(),
                                    name);
                        } else {
                            names.add(name);
                        }
                    } else {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_NO_NAME, index, wfStepConfig.getName());
                    }

                    if (StringUtils.isBlank(wfRoutingConfig.getDescription())) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_NO_DESC, index, wfStepConfig.getName());
                    }

                    String target = wfRoutingConfig.getTargetStepName();
                    if (StringUtils.isBlank(target)) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_NO_TARGET, index, wfStepConfig.getName(),
                                name);
                    } else {
                        WfStepConfig targetWfStepConfig = wfStepConfigs.get(target);
                        if (targetWfStepConfig == null) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_TARGET_UNKNOWN, index,
                                    wfStepConfig.getName(), name, target);
                        } else {
                            if (WorkflowStepType.START.equals(targetWfStepConfig.getType())) {
                                addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_TARGET_START, index,
                                        wfStepConfig.getName(), name, target);
                            }

                            if (target.equals(wfStepConfig.getName())) {
                                addError(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_TARGET_SELF, index,
                                        wfStepConfig.getName(), name, target);
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

                    if (wfUserActionConfig.getNoteRequirement() == null) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_USERACTION_NO_NOTES_TYPE, index,
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
                    String name = wfRecordActionConfig.getName();
                    if (!StringUtils.isBlank(name)) {
                        if (!WfNameUtils.isValidName(name)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_RECORDACTION_INVALID_NAME, index,
                                    wfStepConfig.getName(), name);
                        }

                        if (names.contains(name)) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_RECORDACTION_EXIST, index,
                                    wfStepConfig.getName(), name);
                        } else {
                            names.add(name);
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
                                wfStepConfig.getName(), name);
                    }

                    if (wfRecordActionConfig.getDocMappingName() == null) {
                        addError(WfTemplateErrorConstants.WFTEMPLATE_RECORDACTION_NO_MAPPING, index,
                                wfStepConfig.getName(), name);
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
                        if (!wfMessageConfigs.contains(wfAlertConfig.getMessage())) {
                            addError(WfTemplateErrorConstants.WFTEMPLATE_ALERT_MESSAGE_UNKNOWN, index,
                                    wfStepConfig.getName(), name, wfAlertConfig.getMessage());
                        }
                    }

                    index++;
                }
            }
        }

    }
}
