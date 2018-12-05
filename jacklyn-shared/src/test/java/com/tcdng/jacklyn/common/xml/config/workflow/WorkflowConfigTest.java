/*
 * Copyright 2018 The Code Department
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

package com.tcdng.jacklyn.common.xml.config.workflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.jacklyn.shared.workflow.WorkflowBeanMappingType;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.shared.workflow.WorkflowRecordActionType;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepPriority;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepType;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfAlertConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfAlertsConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfCategoryConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocAttachmentConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocAttachmentsConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocBeanMappingConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocBeanMappingsConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocClassifierConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocClassifierFilterConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocClassifiersConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocFieldConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocFieldMappingConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocFieldsConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocsConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormFieldConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormSectionConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormSectionsConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormTabConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormTabsConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormsConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfMessageConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfMessagesConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfRecordActionConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfRecordActionsConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfRoutingConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfRoutingsConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfStepConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfTemplateConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfTemplatesConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfUserActionConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfUserActionsConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfXmlConfigUtils;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfXmlValidationErrorConstants;
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.operation.FilterConditionType;
import com.tcdng.unify.core.util.IOUtils;

/**
 * Workflow configuration tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WorkflowConfigTest {

	@Test
	public void testReadWfCategoryConfig() throws Exception {
		WfCategoryConfig wfCategoryConfig = readWfCategoryConfig("xml/wfcategory-customer.xml");
		assertNotNull(wfCategoryConfig);
		assertEquals("customerCategory", wfCategoryConfig.getName());
		assertEquals("Customer Category", wfCategoryConfig.getDescription());
	}

	@Test
	public void testReadWfDocsConfig() throws Exception {
		WfCategoryConfig wfCategoryConfig = readWfCategoryConfig("xml/wfcategory-customer.xml");

		WfDocsConfig wfDocsConfig = wfCategoryConfig.getWfDocsConfig();
		assertNotNull(wfDocsConfig);
		List<WfDocConfig> wfDocConfigList = wfDocsConfig.getWfDocConfigList();
		assertNotNull(wfDocConfigList);
		assertEquals(1, wfDocConfigList.size());

		WfDocConfig wfDocConfig = wfDocConfigList.get(0);
		assertNotNull(wfDocConfig);
		assertEquals("custInfo", wfDocConfig.getName());
		assertEquals("Customer Information", wfDocConfig.getDescription());

		// Fields
		WfDocFieldsConfig wfDocFieldsConfig = wfDocConfig.getWfDocFieldsConfig();
		assertNotNull(wfDocFieldsConfig);
		List<WfDocFieldConfig> wfDocFieldConfigList = wfDocFieldsConfig.getWfDocFieldConfigList();
		assertNotNull(wfDocFieldConfigList);
		assertEquals(5, wfDocFieldConfigList.size());

		WfDocFieldConfig wfDocFieldConfig = wfDocFieldConfigList.get(0);
		assertNotNull(wfDocFieldConfig);
		assertEquals("id", wfDocFieldConfig.getName());
		assertEquals("Customer ID", wfDocFieldConfig.getDescription());
		assertEquals(DataType.LONG, wfDocFieldConfig.getDataType());
		assertEquals(Boolean.FALSE, wfDocFieldConfig.getMultiple());

		wfDocFieldConfig = wfDocFieldConfigList.get(1);
		assertNotNull(wfDocFieldConfig);
		assertEquals("firstName", wfDocFieldConfig.getName());
		assertEquals("First Name", wfDocFieldConfig.getDescription());
		assertEquals(DataType.STRING, wfDocFieldConfig.getDataType());
		assertEquals(Boolean.FALSE, wfDocFieldConfig.getMultiple());

		wfDocFieldConfig = wfDocFieldConfigList.get(2);
		assertNotNull(wfDocFieldConfig);
		assertEquals("lastName", wfDocFieldConfig.getName());
		assertEquals("Last Name", wfDocFieldConfig.getDescription());
		assertEquals(DataType.STRING, wfDocFieldConfig.getDataType());
		assertEquals(Boolean.FALSE, wfDocFieldConfig.getMultiple());

		wfDocFieldConfig = wfDocFieldConfigList.get(3);
		assertNotNull(wfDocFieldConfig);
		assertEquals("age", wfDocFieldConfig.getName());
		assertEquals("Age", wfDocFieldConfig.getDescription());
		assertEquals(DataType.INTEGER, wfDocFieldConfig.getDataType());
		assertEquals(Boolean.FALSE, wfDocFieldConfig.getMultiple());

		wfDocFieldConfig = wfDocFieldConfigList.get(4);
		assertNotNull(wfDocFieldConfig);
		assertEquals("height", wfDocFieldConfig.getName());
		assertEquals("Height", wfDocFieldConfig.getDescription());
		assertEquals(DataType.DOUBLE, wfDocFieldConfig.getDataType());
		assertEquals(Boolean.FALSE, wfDocFieldConfig.getMultiple());

		// Classifiers
		WfDocClassifiersConfig wfDocClassifiersConfig = wfDocConfig.getWfDocClassifiersConfig();
		assertNotNull(wfDocClassifiersConfig);
		List<WfDocClassifierConfig> wfDocClassifierConfigList
				= wfDocClassifiersConfig.getWfDocClassifierConfigList();
		assertNotNull(wfDocClassifierConfigList);
		assertEquals(1, wfDocClassifierConfigList.size());

		WfDocClassifierConfig wfDocClassifierConfig = wfDocClassifierConfigList.get(0);
		assertNotNull(wfDocClassifierConfig);
		assertEquals("validAge", wfDocClassifierConfig.getName());
		assertEquals("Valid Age", wfDocClassifierConfig.getDescription());
		assertNull(wfDocClassifierConfig.getLogic());
		List<WfDocClassifierFilterConfig> wfDocClassifierFilterConfigList
				= wfDocClassifierConfig.getWfDocClassifierFilterConfigList();
		assertNotNull(wfDocClassifierFilterConfigList);
		assertEquals(1, wfDocClassifierFilterConfigList.size());
		WfDocClassifierFilterConfig wfDocClassifierFilterConfig
				= wfDocClassifierFilterConfigList.get(0);
		assertNotNull(wfDocClassifierFilterConfig);
		assertEquals("age", wfDocClassifierFilterConfig.getField());
		assertEquals("18", wfDocClassifierFilterConfig.getValue1());
		assertEquals("40", wfDocClassifierFilterConfig.getValue2());
		assertEquals(FilterConditionType.BETWEEN, wfDocClassifierFilterConfig.getOp());
		assertEquals(Boolean.FALSE, wfDocClassifierFilterConfig.getFieldOnly());

		// Attachments
		WfDocAttachmentsConfig wfDocAttachmentsConfig = wfDocConfig.getWfDocAttachmentsConfig();
		assertNotNull(wfDocAttachmentsConfig);
		List<WfDocAttachmentConfig> wfDocAttachmentConfigList
				= wfDocAttachmentsConfig.getWfDocAttachmentConfigList();
		assertNotNull(wfDocAttachmentConfigList);
		assertEquals(1, wfDocAttachmentConfigList.size());

		WfDocAttachmentConfig wfDocAttachmentConfig = wfDocAttachmentConfigList.get(0);
		assertNotNull(wfDocAttachmentConfig);
		assertEquals("birthCert", wfDocAttachmentConfig.getName());
		assertEquals("Birth Certificate", wfDocAttachmentConfig.getDescription());
		assertEquals("Certificate", wfDocAttachmentConfig.getLabel());
		assertEquals(FileAttachmentType.PDF, wfDocAttachmentConfig.getType());

		// Bean mappings
		WfDocBeanMappingsConfig wfDocBeanMappingsConfig = wfDocConfig.getWfDocBeanMappingsConfig();
		assertNotNull(wfDocBeanMappingsConfig);
		List<WfDocBeanMappingConfig> wfDocBeanMappingConfigList
				= wfDocBeanMappingsConfig.getBeanMappingList();
		assertNotNull(wfDocBeanMappingConfigList);
		assertEquals(1, wfDocBeanMappingConfigList.size());

		WfDocBeanMappingConfig wfDocBeanMappingConfig = wfDocBeanMappingConfigList.get(0);
		assertNotNull(wfDocBeanMappingConfig);
		assertEquals("custBeanMapping", wfDocBeanMappingConfig.getName());
		assertEquals("Customer Bean Mapping", wfDocBeanMappingConfig.getDescription());
		assertEquals("com.tcdng.jacklyn.test.TestCustomer", wfDocBeanMappingConfig.getBeanType());
		assertEquals(WorkflowBeanMappingType.PRIMARY, wfDocBeanMappingConfig.getType());

		List<WfDocFieldMappingConfig> fieldMappingList
				= wfDocBeanMappingConfig.getFieldMappingList();
		assertNotNull(fieldMappingList);
		assertEquals(4, fieldMappingList.size());
		WfDocFieldMappingConfig wfDocFieldMappingConfig = fieldMappingList.get(0);
		assertNotNull(wfDocFieldMappingConfig);
		assertEquals("firstName", wfDocFieldMappingConfig.getDocFieldName());
		assertEquals("firstName", wfDocFieldMappingConfig.getBeanFieldName());

		wfDocFieldMappingConfig = fieldMappingList.get(1);
		assertNotNull(wfDocFieldMappingConfig);
		assertEquals("lastName", wfDocFieldMappingConfig.getDocFieldName());
		assertEquals("lastName", wfDocFieldMappingConfig.getBeanFieldName());

		wfDocFieldMappingConfig = fieldMappingList.get(2);
		assertNotNull(wfDocFieldMappingConfig);
		assertEquals("age", wfDocFieldMappingConfig.getDocFieldName());
		assertEquals("age", wfDocFieldMappingConfig.getBeanFieldName());

		wfDocFieldMappingConfig = fieldMappingList.get(3);
		assertNotNull(wfDocFieldMappingConfig);
		assertEquals("height", wfDocFieldMappingConfig.getDocFieldName());
		assertEquals("height", wfDocFieldMappingConfig.getBeanFieldName());
	}

	@Test
	public void testReadWfFormsConfig() throws Exception {
		WfCategoryConfig wfCategoryConfig = readWfCategoryConfig("xml/wfcategory-customer.xml");

		WfFormsConfig wfFormsConfig = wfCategoryConfig.getWfFormsConfig();
		assertNotNull(wfFormsConfig);
		List<WfFormConfig> wfFormConfigList = wfFormsConfig.getWfFormConfigList();
		assertNotNull(wfFormConfigList);
		assertEquals(1, wfFormConfigList.size());

		WfFormConfig wfFormConfig = wfFormConfigList.get(0);
		assertNotNull(wfFormConfig);
		assertEquals("custForm", wfFormConfig.getName());
		assertEquals("Customer Form", wfFormConfig.getDescription());
		assertEquals("custInfo", wfFormConfig.getDocument());

		// Tabs
		WfFormTabsConfig wfFormTabsConfig = wfFormConfig.getWfFormTabsConfig();
		assertNotNull(wfFormTabsConfig);
		List<WfFormTabConfig> wfFormTabConfigList = wfFormTabsConfig.getWfFormTabConfigList();
		assertNotNull(wfFormTabConfigList);
		assertEquals(1, wfFormTabConfigList.size());

		// Sections
		WfFormTabConfig wfFormTabConfig = wfFormTabConfigList.get(0);
		assertNotNull(wfFormTabConfig);
		WfFormSectionsConfig wfFormSectionsConfig = wfFormTabConfig.getWfFormSectionsConfig();
		assertNotNull(wfFormSectionsConfig);
		List<WfFormSectionConfig> wfFormSectionConfigList
				= wfFormSectionsConfig.getWfFormSectionConfigList();
		assertNotNull(wfFormSectionConfigList);
		assertEquals(1, wfFormSectionConfigList.size());

		WfFormSectionConfig wfFormSectionConfig = wfFormSectionConfigList.get(0);
		assertNotNull(wfFormSectionConfig);
		assertEquals("basicDetails", wfFormSectionConfig.getName());
		assertEquals("Basic Details", wfFormSectionConfig.getDescription());
		assertNull(wfFormSectionConfig.getLabel());

		// Fields
		List<WfFormFieldConfig> wfFormFieldConfigList
				= wfFormSectionConfig.getWfFormFieldConfigList();
		assertNotNull(wfFormFieldConfigList);
		assertEquals(4, wfFormFieldConfigList.size());

		WfFormFieldConfig wfFormFieldConfig = wfFormFieldConfigList.get(0);
		assertEquals("firstName", wfFormFieldConfig.getBinding());
		assertEquals("!ui-text", wfFormFieldConfig.getEditorUpl());
		assertNull(wfFormFieldConfig.getLabel());
		assertEquals(Boolean.TRUE, wfFormFieldConfig.getRequired());

		wfFormFieldConfig = wfFormFieldConfigList.get(1);
		assertEquals("lastName", wfFormFieldConfig.getBinding());
		assertEquals("!ui-text", wfFormFieldConfig.getEditorUpl());
		assertNull(wfFormFieldConfig.getLabel());
		assertEquals(Boolean.TRUE, wfFormFieldConfig.getRequired());

		wfFormFieldConfig = wfFormFieldConfigList.get(2);
		assertEquals("age", wfFormFieldConfig.getBinding());
		assertEquals("!ui-integer", wfFormFieldConfig.getEditorUpl());
		assertNull(wfFormFieldConfig.getLabel());
		assertEquals(Boolean.TRUE, wfFormFieldConfig.getRequired());

		wfFormFieldConfig = wfFormFieldConfigList.get(3);
		assertEquals("height", wfFormFieldConfig.getBinding());
		assertEquals("!ui-decimal", wfFormFieldConfig.getEditorUpl());
		assertNull(wfFormFieldConfig.getLabel());
		assertEquals(Boolean.TRUE, wfFormFieldConfig.getRequired());
	}

	@Test
	public void testReadWfMessagesConfig() throws Exception {
		WfCategoryConfig wfCategoryConfig = readWfCategoryConfig("xml/wfcategory-customer.xml");

		WfMessagesConfig wfMessagesConfig = wfCategoryConfig.getWfMessagesConfig();
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
	}

	@Test
	public void testReadWfTemplatesConfig() throws Exception {
		WfCategoryConfig wfCategoryConfig = readWfCategoryConfig("xml/wfcategory-customer.xml");

		WfTemplatesConfig wfTemplatesConfig = wfCategoryConfig.getWfTemplatesConfig();
		assertNotNull(wfTemplatesConfig);
		List<WfTemplateConfig> wfTemplateConfigList = wfTemplatesConfig.getWfTemplateConfigList();
		assertNotNull(wfTemplateConfigList);
		assertEquals(1, wfTemplateConfigList.size());

		WfTemplateConfig wfTemplateConfig = wfTemplateConfigList.get(0);
		assertNotNull(wfTemplateConfig);
		assertEquals("custOnboarding", wfTemplateConfig.getName());
		assertEquals("Customer Onboarding", wfTemplateConfig.getDescription());
		assertEquals("custInfo", wfTemplateConfig.getWfDocName());
		assertEquals("Customer:{firstName} {lastName}", wfTemplateConfig.getItemDescFormat());

		// Steps
		List<WfStepConfig> wfStepConfigList = wfTemplateConfig.getWfStepConfigList();
		assertNotNull(wfStepConfigList);
		assertEquals(4, wfStepConfigList.size());

		// 1
		WfStepConfig wfStepConfig = wfStepConfigList.get(0);
		assertNotNull(wfStepConfig);
		assertEquals("start", wfStepConfig.getName());
		assertEquals("Start Step", wfStepConfig.getDescription());
		assertEquals(WorkflowStepType.START, wfStepConfig.getType());
		assertEquals(WorkflowParticipantType.NONE, wfStepConfig.getParticipant());
		assertEquals(WorkflowStepPriority.NORMAL, wfStepConfig.getPriority());
		assertEquals(Integer.valueOf(0), wfStepConfig.getItemsPerSession());
		assertEquals(Integer.valueOf(0), wfStepConfig.getExpiryHours());
		assertEquals(Boolean.FALSE, wfStepConfig.getAudit());
		assertEquals(Boolean.FALSE, wfStepConfig.getBranchOnly());
		assertEquals(Boolean.FALSE, wfStepConfig.getIncludeForwarder());
		
		WfRoutingsConfig wfRoutingsConfig = wfStepConfig.getWfRoutingsConfig();
		assertNotNull(wfRoutingsConfig);
		List<WfRoutingConfig> wfRoutingConfigList = wfRoutingsConfig.getWfRoutingConfigList();
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

		// 2
		wfStepConfig = wfStepConfigList.get(1);
		assertNotNull(wfStepConfig);
		assertEquals("createCust", wfStepConfig.getName());
		assertEquals("Create Customer", wfStepConfig.getDescription());
		assertEquals(WorkflowStepType.AUTOMATIC, wfStepConfig.getType());
		assertEquals(WorkflowParticipantType.NONE, wfStepConfig.getParticipant());
		assertEquals(WorkflowStepPriority.NORMAL, wfStepConfig.getPriority());
		assertEquals(Integer.valueOf(0), wfStepConfig.getItemsPerSession());
		assertEquals(Integer.valueOf(0), wfStepConfig.getExpiryHours());
		assertEquals(Boolean.FALSE, wfStepConfig.getAudit());
		assertEquals(Boolean.FALSE, wfStepConfig.getBranchOnly());
		assertEquals(Boolean.FALSE, wfStepConfig.getIncludeForwarder());
		
		WfRecordActionsConfig wfRecordActionsConfig = wfStepConfig.getWfRecordActionsConfig();
		assertNotNull(wfRecordActionsConfig);
		List<WfRecordActionConfig> wfRecordActionConfigList = wfRecordActionsConfig.getWfRecordActionConfigList();
		assertNotNull(wfRecordActionConfigList);
		assertEquals(1, wfRecordActionConfigList.size());
		
		WfRecordActionConfig wfRecordActionConfig = wfRecordActionConfigList.get(0);
		assertNotNull(wfRecordActionConfig);
		assertEquals("createCust", wfRecordActionConfig.getName());
		assertEquals("Create Customer", wfRecordActionConfig.getDescription());
		assertEquals(WorkflowRecordActionType.CREATE, wfRecordActionConfig.getActionType());
		assertEquals("custBeanMapping", wfRecordActionConfig.getDocMappingName());
		
		wfRoutingsConfig = wfStepConfig.getWfRoutingsConfig();
		assertNotNull(wfRoutingsConfig);
		wfRoutingConfigList = wfRoutingsConfig.getWfRoutingConfigList();
		assertNotNull(wfRoutingConfigList);
		assertEquals(1, wfRoutingConfigList.size());
		
		wfRoutingConfig = wfRoutingConfigList.get(0);
		assertNotNull(wfRoutingConfig);
		assertEquals("routToEnd", wfRoutingConfig.getName());
		assertEquals("Route to End", wfRoutingConfig.getDescription());
		assertNull(wfRoutingConfig.getClassifierName());
		assertEquals("end", wfRoutingConfig.getTargetStepName());
		
		// 3
		wfStepConfig = wfStepConfigList.get(2);
		assertNotNull(wfStepConfig);
		assertEquals("custApproval", wfStepConfig.getName());
		assertEquals("Customer Approval", wfStepConfig.getDescription());
		assertEquals(WorkflowStepType.INTERACTIVE, wfStepConfig.getType());
		assertEquals(WorkflowParticipantType.ALL, wfStepConfig.getParticipant());
		assertEquals(WorkflowStepPriority.NORMAL, wfStepConfig.getPriority());
		assertEquals(Integer.valueOf(0), wfStepConfig.getItemsPerSession());
		assertEquals(Integer.valueOf(0), wfStepConfig.getExpiryHours());
		assertEquals(Boolean.FALSE, wfStepConfig.getAudit());
		assertEquals(Boolean.FALSE, wfStepConfig.getBranchOnly());
		assertEquals(Boolean.FALSE, wfStepConfig.getIncludeForwarder());
		
		WfAlertsConfig wfAlertsConfig = wfStepConfig.getWfAlertsConfig();
		assertNotNull(wfAlertsConfig);
		List<WfAlertConfig> wfAlertConfigList = wfAlertsConfig.getWfAlertConfigList();
		assertNotNull(wfAlertConfigList);
		assertEquals(1, wfAlertConfigList.size());
		
		WfAlertConfig wfAlertConfig = wfAlertConfigList.get(0);
		assertNotNull(wfAlertConfig);
		assertEquals("awaitApproval", wfAlertConfig.getName());
		assertEquals("Awaiting Approval", wfAlertConfig.getDescription());
		assertEquals("awaitCustApproval", wfAlertConfig.getMessage());
		assertEquals(NotificationType.SYSTEM, wfAlertConfig.getType());
		
		WfUserActionsConfig wfUserActionsConfig = wfStepConfig.getWfUserActionsConfig();
		assertNotNull(wfUserActionsConfig);
		List<WfUserActionConfig> wfUserActionConfigList = wfUserActionsConfig.getWfUserActionConfigList();
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

		// 4
		wfStepConfig = wfStepConfigList.get(3);
		assertNotNull(wfStepConfig);
		assertEquals("end", wfStepConfig.getName());
		assertEquals("End Step", wfStepConfig.getDescription());
		assertEquals(WorkflowStepType.END, wfStepConfig.getType());
		assertEquals(WorkflowParticipantType.NONE, wfStepConfig.getParticipant());
		assertEquals(WorkflowStepPriority.NORMAL, wfStepConfig.getPriority());
		assertEquals(Integer.valueOf(0), wfStepConfig.getItemsPerSession());
		assertEquals(Integer.valueOf(0), wfStepConfig.getExpiryHours());
		assertEquals(Boolean.FALSE, wfStepConfig.getAudit());
		assertEquals(Boolean.FALSE, wfStepConfig.getBranchOnly());
		assertEquals(Boolean.FALSE, wfStepConfig.getIncludeForwarder());
	}

	@Test
	public void testValidateWfCategoryConfig() throws Exception {
		WfCategoryConfig wfCategoryConfig = readWfCategoryConfig("xml/wfcategory-customer.xml");
		List<UnifyError> errorList = WfXmlConfigUtils.validateWfCategoryConfig(wfCategoryConfig);
		assertNotNull(errorList);
		assertEquals(0, errorList.size());
	}

	@Test
	public void testValidateWfCategoryConfigWithDocError() throws Exception {
		WfCategoryConfig wfCategoryConfig = readWfCategoryConfig("xml/wfcategory-customer-err-doc.xml");
		List<UnifyError> errorList = WfXmlConfigUtils.validateWfCategoryConfig(wfCategoryConfig);
		assertNotNull(errorList);
		assertEquals(7, errorList.size());
		assertEquals(WfXmlValidationErrorConstants.WFXML_DOCFIELD_NO_NAME, errorList.get(0).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_DOCFIELD_NO_DESC, errorList.get(1).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_CLASSIFIER_NO_NAME, errorList.get(2).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_BEANMAPPING_NO_NAME, errorList.get(3).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_BEANMAPPING_NO_TYPE, errorList.get(4).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_ROUTING_CLASSIFIER_UNKNOWN, errorList.get(5).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_RECORDACTION_DOCMAPPING_UNKNOWN, errorList.get(6).getErrorCode());
	}

	@Test
	public void testValidateWfCategoryConfigWithFormError() throws Exception {
		WfCategoryConfig wfCategoryConfig = readWfCategoryConfig("xml/wfcategory-customer-err-form.xml");
		List<UnifyError> errorList = WfXmlConfigUtils.validateWfCategoryConfig(wfCategoryConfig);
		assertNotNull(errorList);
		assertEquals(7, errorList.size());
		assertEquals(WfXmlValidationErrorConstants.WFXML_FORM_NO_NAME, errorList.get(0).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_SECTION_NO_DESC, errorList.get(1).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_FIELD_NO_BINDING, errorList.get(2).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_TAB_NAME_EXIST, errorList.get(3).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_TAB_DESC_EXIST, errorList.get(4).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_TAB_NO_SECTIONS, errorList.get(5).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_STEP_FORM_UNKNOWN, errorList.get(6).getErrorCode());
	}

	@Test
	public void testValidateWfCategoryConfigWithMessageError() throws Exception {
		WfCategoryConfig wfCategoryConfig = readWfCategoryConfig("xml/wfcategory-customer-err-msg.xml");
		List<UnifyError> errorList = WfXmlConfigUtils.validateWfCategoryConfig(wfCategoryConfig);
		assertNotNull(errorList);
		assertEquals(5, errorList.size());
		assertEquals(WfXmlValidationErrorConstants.WFXML_MESSAGE_NO_NAME, errorList.get(0).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_MESSAGE_NO_DESC, errorList.get(1).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_MESSAGE_NO_SUBJECT, errorList.get(2).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_MESSAGE_NO_BODY, errorList.get(3).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_ALERT_MESSAGE_UNKNOWN, errorList.get(4).getErrorCode());
	}

	@Test
	public void testValidateWfCategoryConfigWithTemplateError() throws Exception {
		WfCategoryConfig wfCategoryConfig = readWfCategoryConfig("xml/wfcategory-customer-err-tmpl.xml");
		List<UnifyError> errorList = WfXmlConfigUtils.validateWfCategoryConfig(wfCategoryConfig);
		assertNotNull(errorList);
		assertEquals(13, errorList.size());
		assertEquals(WfXmlValidationErrorConstants.WFXML_TEMPLATE_NO_NAME, errorList.get(0).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_ROUTING_NO_TARGET, errorList.get(1).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_STEP_USERACTION_NOT_ALLOWED, errorList.get(2).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_RECORDACTION_NO_DOCMAPPING, errorList.get(3).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_USERACTION_TARGET_SELF, errorList.get(4).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_STEP_ROUTING_NOT_ALLOWED, errorList.get(5).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_STEP_FORM_REQUIRED, errorList.get(6).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_USERACTION_NO_NAME, errorList.get(7).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_USERACTION_NO_TARGET, errorList.get(8).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_USERACTION_TARGET_UNKNOWN, errorList.get(9).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_ALERT_NO_NAME, errorList.get(10).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_ALERT_MESSAGE_UNKNOWN, errorList.get(11).getErrorCode());
		assertEquals(WfXmlValidationErrorConstants.WFXML_TEMPLATE_MULTIPLE_END_STEPS, errorList.get(12).getErrorCode());
	}

	private WfCategoryConfig readWfCategoryConfig(String resourceName) throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = IOUtils.openClassLoaderResourceInputStream(resourceName);
			return WfXmlConfigUtils.readXmlWfCategoryConfig(inputStream);
		} finally {
			IOUtils.close(inputStream);
		}
	}
}
