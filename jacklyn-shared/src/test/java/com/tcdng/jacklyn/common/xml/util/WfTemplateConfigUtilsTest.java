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

package com.tcdng.jacklyn.common.xml.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.jacklyn.shared.workflow.WorkflowFormElementType;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.shared.workflow.WorkflowRecordActionType;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepPriority;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepType;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfAlertConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfEnrichmentConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormPrivilegeConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfMessageConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfMessagesConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfPolicyConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfRecordActionConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfRoutingConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfStepConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfStepsConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfTemplateConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfUserActionConfig;
import com.tcdng.jacklyn.shared.xml.util.WfTemplateConfigUtils;
import com.tcdng.jacklyn.shared.xml.util.WfTemplateErrorConstants;
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.constant.RequirementType;

/**
 * Workflow template configuration tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfTemplateConfigUtilsTest {

    @Test
    public void testReadWfTemplateConfig() throws Exception {
        WfTemplateConfig wfTemplateConfig = WfTemplateConfigUtils
                .readWfTemplateConfig("xml/wfcustomer-tmpl-custonboarding.xml");
        assertNotNull(wfTemplateConfig);

        assertEquals("customerCategory.custOnboarding", wfTemplateConfig.getName());
        assertEquals("Customer Onboarding", wfTemplateConfig.getDescription());
        assertEquals("customerCategory.custInfo", wfTemplateConfig.getDocument());
        assertEquals("1.0", wfTemplateConfig.getVersion());

        // Messages
        WfMessagesConfig wfMessagesConfig = wfTemplateConfig.getWfMessagesConfig();
        assertNotNull(wfMessagesConfig);
        List<WfMessageConfig> wfMessageConfigList = wfMessagesConfig.getWfMessageConfigList();
        assertNotNull(wfMessageConfigList);
        assertEquals(1, wfMessageConfigList.size());

        WfMessageConfig wfMessageConfig = wfMessageConfigList.get(0);
        assertNotNull(wfMessageConfig);
        assertEquals("awaitCustApproval", wfMessageConfig.getName());
        assertEquals("Awaiting Customer Approval", wfMessageConfig.getDescription());
        assertEquals("default-messagegenerator", wfMessageConfig.getAttachmentGenerator());
        assertEquals("Awaiting Customer Approval", wfMessageConfig.getSubject());
        assertEquals("Awaiting approval for {firstName} {lastName}", wfMessageConfig.getBody());
        assertEquals(Boolean.FALSE, wfMessageConfig.getHtml());

        // Steps
        WfStepsConfig wfStepsConfig = wfTemplateConfig.getWfStepsConfig();
        assertNotNull(wfStepsConfig);
        List<WfStepConfig> wfStepConfigList = wfStepsConfig.getWfStepConfigList();
        assertNotNull(wfStepConfigList);
        assertEquals(4, wfStepConfigList.size());

        WfStepConfig wfStepConfig = wfStepConfigList.get(0);
        assertNotNull(wfStepConfig);
        assertEquals("start", wfStepConfig.getName());
        assertEquals("Start Step", wfStepConfig.getDescription());
        assertEquals(WorkflowStepType.START, wfStepConfig.getType());
        assertEquals(WorkflowParticipantType.NONE, wfStepConfig.getParticipant());
        assertEquals(WorkflowStepPriority.NORMAL, wfStepConfig.getPriority());
        assertNull(wfStepConfig.getWfAlertsConfig());
        assertNull(wfStepConfig.getWfUserActionsConfig());
        assertNull(wfStepConfig.getWfFormPrivilegesConfig());
        assertNull(wfStepConfig.getWfRecordActionsConfig());
        assertNull(wfStepConfig.getWfPoliciesConfig());

        assertNotNull(wfStepConfig.getWfRoutingsConfig());
        List<WfRoutingConfig> wfRoutingConfigList = wfStepConfig.getWfRoutingsConfig().getWfRoutingConfigList();
        assertNotNull(wfRoutingConfigList);
        assertEquals(2, wfRoutingConfigList.size());

        WfRoutingConfig wfRoutingConfig = wfRoutingConfigList.get(0);
        assertNotNull(wfRoutingConfig);
        assertEquals("routToCreate", wfRoutingConfig.getName());
        assertEquals("Route to Create", wfRoutingConfig.getDescription());
        assertEquals("validAge", wfRoutingConfig.getClassifierName());
        assertEquals("createCust", wfRoutingConfig.getTargetStepName());

        wfRoutingConfig = wfRoutingConfigList.get(1);
        assertNotNull(wfRoutingConfig);
        assertEquals("routToApproval", wfRoutingConfig.getName());
        assertEquals("Route to Approval", wfRoutingConfig.getDescription());
        assertNull(wfRoutingConfig.getClassifierName());
        assertEquals("custApproval", wfRoutingConfig.getTargetStepName());

        assertNotNull(wfStepConfig.getWfEnrichmentsConfig());
        List<WfEnrichmentConfig> wfEnrichmentConfigList = wfStepConfig.getWfEnrichmentsConfig()
                .getWfEnrichmentConfigList();
        assertNotNull(wfEnrichmentConfigList);
        assertEquals(1, wfEnrichmentConfigList.size());

        WfEnrichmentConfig wfEnrichmentConfig = wfEnrichmentConfigList.get(0);
        assertNotNull(wfEnrichmentConfig);
        assertEquals("testEnrichment", wfEnrichmentConfig.getName());
        assertEquals("Test Enrichment", wfEnrichmentConfig.getDescription());
        assertEquals("testcustomer-enrichmentlogic", wfEnrichmentConfig.getLogic());

        wfStepConfig = wfStepConfigList.get(1);
        assertNotNull(wfStepConfig);
        assertEquals("createCust", wfStepConfig.getName());
        assertEquals("Create Customer", wfStepConfig.getDescription());
        assertEquals(WorkflowStepType.AUTOMATIC, wfStepConfig.getType());
        assertEquals(WorkflowParticipantType.NONE, wfStepConfig.getParticipant());
        assertEquals(WorkflowStepPriority.NORMAL, wfStepConfig.getPriority());
        assertNull(wfStepConfig.getWfAlertsConfig());
        assertNull(wfStepConfig.getWfUserActionsConfig());
        assertNull(wfStepConfig.getWfFormPrivilegesConfig());
        assertNull(wfStepConfig.getWfEnrichmentsConfig());

        assertNotNull(wfStepConfig.getWfRecordActionsConfig());
        List<WfRecordActionConfig> wfRecordActionConfigList = wfStepConfig.getWfRecordActionsConfig()
                .getWfRecordActionConfigList();
        assertNotNull(wfRecordActionConfigList);
        assertEquals(1, wfRecordActionConfigList.size());

        WfRecordActionConfig wfRecordActionConfig = wfRecordActionConfigList.get(0);
        assertNotNull(wfRecordActionConfig);
        assertEquals("createCust", wfRecordActionConfig.getName());
        assertEquals("Create Customer", wfRecordActionConfig.getDescription());
        assertEquals("custBeanMapping", wfRecordActionConfig.getDocMappingName());
        assertEquals(WorkflowRecordActionType.CREATE, wfRecordActionConfig.getActionType());

        assertNotNull(wfStepConfig.getWfRoutingsConfig());
        wfRoutingConfigList = wfStepConfig.getWfRoutingsConfig().getWfRoutingConfigList();
        assertNotNull(wfRoutingConfigList);
        assertEquals(1, wfRoutingConfigList.size());

        wfRoutingConfig = wfRoutingConfigList.get(0);
        assertNotNull(wfRoutingConfig);
        assertEquals("routToEnd", wfRoutingConfig.getName());
        assertEquals("Route to End", wfRoutingConfig.getDescription());
        assertNull(wfRoutingConfig.getClassifierName());
        assertEquals("end", wfRoutingConfig.getTargetStepName());

        assertNotNull(wfStepConfig.getWfPoliciesConfig());
        List<WfPolicyConfig> wfPolicyConfigList = wfStepConfig.getWfPoliciesConfig().getWfPolicyConfigList();
        assertNotNull(wfPolicyConfigList);
        assertEquals(1, wfPolicyConfigList.size());

        WfPolicyConfig wfPolicyConfig = wfPolicyConfigList.get(0);
        assertNotNull(wfPolicyConfig);
        assertEquals("testOpenAccount", wfPolicyConfig.getName());
        assertEquals("Open Account", wfPolicyConfig.getDescription());
        assertEquals("testopenaccount-policylogic", wfPolicyConfig.getLogic());

        wfStepConfig = wfStepConfigList.get(2);
        assertNotNull(wfStepConfig);
        assertEquals("custApproval", wfStepConfig.getName());
        assertEquals("Customer Approval", wfStepConfig.getDescription());
        assertEquals(WorkflowStepType.INTERACTIVE, wfStepConfig.getType());
        assertEquals(WorkflowParticipantType.ALL, wfStepConfig.getParticipant());
        assertEquals(WorkflowStepPriority.HIGH, wfStepConfig.getPriority());
        assertNull(wfStepConfig.getWfRoutingsConfig());
        assertNull(wfStepConfig.getWfRecordActionsConfig());
        assertNull(wfStepConfig.getWfPoliciesConfig());
        assertNull(wfStepConfig.getWfEnrichmentsConfig());

        assertNotNull(wfStepConfig.getWfAlertsConfig());
        List<WfAlertConfig> wfAlertConfigList = wfStepConfig.getWfAlertsConfig().getWfAlertConfigList();
        assertNotNull(wfAlertConfigList);
        assertEquals(1, wfAlertConfigList.size());

        WfAlertConfig wfAlertConfig = wfAlertConfigList.get(0);
        assertNotNull(wfAlertConfig);
        assertEquals("awaitApproval", wfAlertConfig.getName());
        assertEquals("Awaiting Approval", wfAlertConfig.getDescription());
        assertEquals("awaitCustApproval", wfAlertConfig.getMessage());
        assertEquals(NotificationType.SYSTEM, wfAlertConfig.getType());

        assertNotNull(wfStepConfig.getWfUserActionsConfig());
        List<WfUserActionConfig> wfUserActionConfigList = wfStepConfig.getWfUserActionsConfig()
                .getWfUserActionConfigList();
        assertNotNull(wfUserActionConfigList);
        assertEquals(2, wfUserActionConfigList.size());

        WfUserActionConfig wfUserActionConfig = wfUserActionConfigList.get(0);
        assertNotNull(wfUserActionConfig);
        assertEquals("approveCust", wfUserActionConfig.getName());
        assertEquals("Approve Customer", wfUserActionConfig.getDescription());
        assertEquals("Approve", wfUserActionConfig.getLabel());
        assertEquals("createCust", wfUserActionConfig.getTargetStepName());
        assertEquals(RequirementType.OPTIONAL, wfUserActionConfig.getNoteRequirement());

        wfUserActionConfig = wfUserActionConfigList.get(1);
        assertNotNull(wfUserActionConfig);
        assertEquals("rejectCust", wfUserActionConfig.getName());
        assertEquals("Reject Customer", wfUserActionConfig.getDescription());
        assertEquals("Reject", wfUserActionConfig.getLabel());
        assertEquals("end", wfUserActionConfig.getTargetStepName());
        assertEquals(RequirementType.MANDATORY, wfUserActionConfig.getNoteRequirement());

        assertNotNull(wfStepConfig.getWfFormPrivilegesConfig());
        List<WfFormPrivilegeConfig> wfFormPrivilegeConfigList = wfStepConfig.getWfFormPrivilegesConfig()
                .getWfFormPrivilegesConfigList();
        assertNotNull(wfFormPrivilegeConfigList);
        assertEquals(1, wfFormPrivilegeConfigList.size());

        WfFormPrivilegeConfig wfFormPrivilegeConfig = wfFormPrivilegeConfigList.get(0);
        assertNotNull(wfFormPrivilegeConfig);
        assertEquals("licenseNo", wfFormPrivilegeConfig.getName());
        assertEquals(WorkflowFormElementType.FIELD, wfFormPrivilegeConfig.getType());

        wfStepConfig = wfStepConfigList.get(3);
        assertNotNull(wfStepConfig);
        assertEquals("end", wfStepConfig.getName());
        assertEquals("End Step", wfStepConfig.getDescription());
        assertEquals(WorkflowStepType.END, wfStepConfig.getType());
        assertEquals(WorkflowParticipantType.NONE, wfStepConfig.getParticipant());
        assertEquals(WorkflowStepPriority.NORMAL, wfStepConfig.getPriority());
        assertNull(wfStepConfig.getWfAlertsConfig());
        assertNull(wfStepConfig.getWfRoutingsConfig());
        assertNull(wfStepConfig.getWfRecordActionsConfig());
        assertNull(wfStepConfig.getWfPoliciesConfig());
        assertNull(wfStepConfig.getWfEnrichmentsConfig());
        assertNull(wfStepConfig.getWfUserActionsConfig());
        assertNull(wfStepConfig.getWfFormPrivilegesConfig());
    }

    @Test
    public void testValidateWfTemplateConfig() throws Exception {
        WfTemplateConfig wfTemplateConfig = WfTemplateConfigUtils
                .readWfTemplateConfig("xml/wfcustomer-tmpl-custonboarding.xml");
        List<UnifyError> errorList = WfTemplateConfigUtils.validate(wfTemplateConfig);
        assertNotNull(errorList);
        assertEquals(0, errorList.size());
    }

    @Test
    public void testValidateWfTemplateConfigWithErrors() throws Exception {
        WfTemplateConfig wfTemplateConfig = WfTemplateConfigUtils
                .readWfTemplateConfig("xml/wfcustomer-tmpl-custonboarding-err.xml");
        List<UnifyError> errorList = WfTemplateConfigUtils.validate(wfTemplateConfig);
        assertNotNull(errorList);
        assertEquals(14, errorList.size());

        assertEquals(WfTemplateErrorConstants.WFTEMPLATE_NO_DOCUMENT, errorList.get(0).getErrorCode());
        assertEquals(WfTemplateErrorConstants.WFTEMPLATE_MESSAGE_NO_DESC, errorList.get(1).getErrorCode());
        assertEquals(WfTemplateErrorConstants.WFTEMPLATE_MESSAGE_NO_SUBJECT, errorList.get(2).getErrorCode());
        assertEquals(WfTemplateErrorConstants.WFTEMPLATE_STEP_NO_NAME, errorList.get(3).getErrorCode());
        assertEquals(WfTemplateErrorConstants.WFTEMPLATE_STEP_EXIST, errorList.get(4).getErrorCode());
        assertEquals(WfTemplateErrorConstants.WFTEMPLATE_STEP_MULTIPLE_END, errorList.get(5).getErrorCode());
        assertEquals(WfTemplateErrorConstants.WFTEMPLATE_ENRICHMENT_NO_LOGIC, errorList.get(6).getErrorCode());
        assertEquals(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_EXIST, errorList.get(7).getErrorCode());
        assertEquals(WfTemplateErrorConstants.WFTEMPLATE_ROUTING_TARGET_UNKNOWN, errorList.get(8).getErrorCode());
        assertEquals(WfTemplateErrorConstants.WFTEMPLATE_POLICY_NO_NAME, errorList.get(9).getErrorCode());
        assertEquals(WfTemplateErrorConstants.WFTEMPLATE_RECORDACTION_NO_TYPE, errorList.get(10).getErrorCode());
        assertEquals(WfTemplateErrorConstants.WFTEMPLATE_ALERT_MESSAGE_UNKNOWN, errorList.get(11).getErrorCode());
        assertEquals(WfTemplateErrorConstants.WFTEMPLATE_USERACTION_TARGET_UNKNOWN, errorList.get(12).getErrorCode());
        assertEquals(WfTemplateErrorConstants.WFTEMPLATE_FORMPRIVILEGE_NO_ELEMENT_TYPE,
                errorList.get(13).getErrorCode());
    }
}
