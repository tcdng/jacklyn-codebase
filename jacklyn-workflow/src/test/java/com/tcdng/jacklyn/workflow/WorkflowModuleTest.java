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
package com.tcdng.jacklyn.workflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Test;

import com.tcdng.jacklyn.common.AbstractJacklynTest;
import com.tcdng.jacklyn.common.TestCustomer;
import com.tcdng.jacklyn.common.TestCustomerModule;
import com.tcdng.jacklyn.notification.business.NotificationModule;
import com.tcdng.jacklyn.notification.constants.NotificationModuleNameConstants;
import com.tcdng.jacklyn.notification.entities.NotificationTemplate;
import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.jacklyn.shared.workflow.WorkflowBeanMappingType;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.shared.workflow.WorkflowRecordActionType;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepPriority;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepType;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfCategoryConfig;
import com.tcdng.jacklyn.shared.xml.util.WfDocumentConfigUtils;
import com.tcdng.jacklyn.workflow.TestOpenAccountPolicyLogic.OpenAccountDetails;
import com.tcdng.jacklyn.workflow.business.WorkflowModule;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleErrorConstants;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.jacklyn.workflow.data.WfItemHistEvent;
import com.tcdng.jacklyn.workflow.data.WfItemHistObject;
import com.tcdng.jacklyn.workflow.data.WfItemObject;
import com.tcdng.jacklyn.workflow.entities.WfAlert;
import com.tcdng.jacklyn.workflow.entities.WfCategory;
import com.tcdng.jacklyn.workflow.entities.WfCategoryQuery;
import com.tcdng.jacklyn.workflow.entities.WfDoc;
import com.tcdng.jacklyn.workflow.entities.WfDocAttachment;
import com.tcdng.jacklyn.workflow.entities.WfDocBeanMapping;
import com.tcdng.jacklyn.workflow.entities.WfDocClassifier;
import com.tcdng.jacklyn.workflow.entities.WfDocClassifierFilter;
import com.tcdng.jacklyn.workflow.entities.WfDocField;
import com.tcdng.jacklyn.workflow.entities.WfDocFieldMapping;
import com.tcdng.jacklyn.workflow.entities.WfEnrichment;
import com.tcdng.jacklyn.workflow.entities.WfForm;
import com.tcdng.jacklyn.workflow.entities.WfFormField;
import com.tcdng.jacklyn.workflow.entities.WfFormSection;
import com.tcdng.jacklyn.workflow.entities.WfFormTab;
import com.tcdng.jacklyn.workflow.entities.WfItem;
import com.tcdng.jacklyn.workflow.entities.WfItemAttachment;
import com.tcdng.jacklyn.workflow.entities.WfItemEvent;
import com.tcdng.jacklyn.workflow.entities.WfItemPackedDoc;
import com.tcdng.jacklyn.workflow.entities.WfPolicy;
import com.tcdng.jacklyn.workflow.entities.WfRecordAction;
import com.tcdng.jacklyn.workflow.entities.WfRouting;
import com.tcdng.jacklyn.workflow.entities.WfStep;
import com.tcdng.jacklyn.workflow.entities.WfTemplate;
import com.tcdng.jacklyn.workflow.entities.WfUserAction;
import com.tcdng.jacklyn.workflow.utils.WorkflowUtils;
import com.tcdng.unify.core.UnifyCoreErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.data.PackableDoc;
import com.tcdng.unify.core.operation.FilterConditionType;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.ThreadUtils;

/**
 * Workflow business module tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WorkflowModuleTest extends AbstractJacklynTest {

	private static boolean published = false;

	@Test
	public void testPublishedWorkflowCategory() throws Exception {
		WorkflowModule wbModule = getWorkflowModule();
		/* Categories */
		List<WfCategory> wfCategoryList
				= wbModule.findWfCategories(new WfCategoryQuery().name("customerCategory"));
		assertNotNull(wfCategoryList);
		assertEquals(1, wfCategoryList.size());

		WfCategory wfCategoryData = wfCategoryList.get(0);
		assertNotNull(wfCategoryData);
		assertEquals("customerCategory", wfCategoryData.getName());
		assertEquals("Customer Category", wfCategoryData.getDescription());

		/* Documents */
		Long wfCategoryId = wfCategoryData.getId();
		List<WfDoc> wfDocList = wbModule.findWfDocs(wfCategoryId);
		assertNotNull(wfDocList);
		assertEquals(1, wfDocList.size());

		WfDoc wfDocData = wfDocList.get(0);
		assertNotNull(wfDocData);
		assertEquals("custInfo", wfDocData.getName());
		assertEquals("Customer Information", wfDocData.getDescription());
		assertNull(wfDocData.getFieldList());
		assertNull(wfDocData.getClassifierList());
		assertNull(wfDocData.getAttachmentList());
		assertNull(wfDocData.getBeanMappingList());

		wfDocData = wbModule.findWfDoc(wfDocData.getId());
		assertNotNull(wfDocData);
		assertEquals("custInfo", wfDocData.getName());
		assertEquals("Customer Information", wfDocData.getDescription());

		/* Document fields */
		List<WfDocField> docFieldList = wfDocData.getFieldList();
		assertNotNull(docFieldList);
		assertEquals(11, docFieldList.size());

		WfDocField wfDocFieldData = docFieldList.get(0);
		assertNotNull(wfDocFieldData);
		assertNull(wfDocFieldData.getParentName());
		assertEquals("id", wfDocFieldData.getName());
		assertEquals("Customer ID", wfDocFieldData.getDescription());
		assertEquals(DataType.LONG, wfDocFieldData.getDataType());
		assertFalse(wfDocFieldData.getArrayFlag());

		wfDocFieldData = docFieldList.get(1);
		assertNotNull(wfDocFieldData);
		assertNull(wfDocFieldData.getParentName());
		assertEquals("firstName", wfDocFieldData.getName());
		assertEquals("First Name", wfDocFieldData.getDescription());
		assertEquals(DataType.STRING, wfDocFieldData.getDataType());
		assertFalse(wfDocFieldData.getArrayFlag());

		wfDocFieldData = docFieldList.get(2);
		assertNotNull(wfDocFieldData);
		assertNull(wfDocFieldData.getParentName());
		assertEquals("lastName", wfDocFieldData.getName());
		assertEquals("Last Name", wfDocFieldData.getDescription());
		assertEquals(DataType.STRING, wfDocFieldData.getDataType());
		assertFalse(wfDocFieldData.getArrayFlag());

		wfDocFieldData = docFieldList.get(3);
		assertNotNull(wfDocFieldData);
		assertNull(wfDocFieldData.getParentName());
		assertEquals("age", wfDocFieldData.getName());
		assertEquals("Age", wfDocFieldData.getDescription());
		assertEquals(DataType.INTEGER, wfDocFieldData.getDataType());
		assertFalse(wfDocFieldData.getArrayFlag());

		wfDocFieldData = docFieldList.get(4);
		assertNotNull(wfDocFieldData);
		assertNull(wfDocFieldData.getParentName());
		assertEquals("height", wfDocFieldData.getName());
		assertEquals("Height", wfDocFieldData.getDescription());
		assertEquals(DataType.DOUBLE, wfDocFieldData.getDataType());
		assertFalse(wfDocFieldData.getArrayFlag());

		wfDocFieldData = docFieldList.get(5);
		assertNotNull(wfDocFieldData);
		assertNull(wfDocFieldData.getParentName());
		assertEquals("accountNo", wfDocFieldData.getName());
		assertEquals("Account Number", wfDocFieldData.getDescription());
		assertEquals(DataType.STRING, wfDocFieldData.getDataType());
		assertFalse(wfDocFieldData.getArrayFlag());

		wfDocFieldData = docFieldList.get(6);
		assertNotNull(wfDocFieldData);
		assertNull(wfDocFieldData.getParentName());
		assertEquals("fullName", wfDocFieldData.getName());
		assertEquals("Full Name", wfDocFieldData.getDescription());
		assertEquals(DataType.STRING, wfDocFieldData.getDataType());
		assertFalse(wfDocFieldData.getArrayFlag());

		wfDocFieldData = docFieldList.get(7);
		assertNotNull(wfDocFieldData);
		assertNull(wfDocFieldData.getParentName());
		assertEquals("driversLicense", wfDocFieldData.getName());
		assertEquals("Driver's License", wfDocFieldData.getDescription());
		assertEquals(DataType.COMPLEX, wfDocFieldData.getDataType());
		assertFalse(wfDocFieldData.getArrayFlag());

		wfDocFieldData = docFieldList.get(8);
		assertNotNull(wfDocFieldData);
		System.out.println(wfDocFieldData);
		assertEquals("driversLicense", wfDocFieldData.getParentName());
		assertEquals("licenseNo", wfDocFieldData.getName());
		assertEquals("License No.", wfDocFieldData.getDescription());
		assertEquals(DataType.STRING, wfDocFieldData.getDataType());
		assertFalse(wfDocFieldData.getArrayFlag());

		wfDocFieldData = docFieldList.get(9);
		assertNotNull(wfDocFieldData);
		assertEquals("driversLicense", wfDocFieldData.getParentName());
		assertEquals("issueDt", wfDocFieldData.getName());
		assertEquals("Issue Date", wfDocFieldData.getDescription());
		assertEquals(DataType.DATE, wfDocFieldData.getDataType());
		assertFalse(wfDocFieldData.getArrayFlag());

		wfDocFieldData = docFieldList.get(10);
		assertNotNull(wfDocFieldData);
		assertEquals("driversLicense", wfDocFieldData.getParentName());
		assertEquals("expiryDt", wfDocFieldData.getName());
		assertEquals("Expiry Date", wfDocFieldData.getDescription());
		assertEquals(DataType.DATE, wfDocFieldData.getDataType());
		assertFalse(wfDocFieldData.getArrayFlag());

		/* Classifier */
		List<WfDocClassifier> classifierList = wfDocData.getClassifierList();
		assertNotNull(classifierList);
		assertEquals(1, classifierList.size());

		WfDocClassifier wfDocClassifierData = classifierList.get(0);
		assertEquals("validAge", wfDocClassifierData.getName());
		assertEquals("Valid Age", wfDocClassifierData.getDescription());
		assertNull(wfDocClassifierData.getLogic());
		List<WfDocClassifierFilter> filterList = wfDocClassifierData.getFilterList();
		assertNotNull(filterList);
		assertEquals(1, filterList.size());
		WfDocClassifierFilter wfDocClassifierFilterData = filterList.get(0);
		assertNotNull(wfDocClassifierFilterData);
		assertEquals("age", wfDocClassifierFilterData.getFieldName());
		assertEquals(FilterConditionType.BETWEEN, wfDocClassifierFilterData.getOp());
		assertEquals("18", wfDocClassifierFilterData.getValue1());
		assertEquals("40", wfDocClassifierFilterData.getValue2());
		assertFalse(wfDocClassifierFilterData.getFieldOnly());

		/* Attachments */
		List<WfDocAttachment> attachmentList = wfDocData.getAttachmentList();
		assertNotNull(attachmentList);
		assertEquals(1, attachmentList.size());
		WfDocAttachment wfDocAttachmentData = attachmentList.get(0);
		assertNotNull(wfDocAttachmentData);
		assertEquals("birthCert", wfDocAttachmentData.getName());
		assertEquals("Birth Certificate", wfDocAttachmentData.getDescription());
		assertEquals("Certificate", wfDocAttachmentData.getLabel());
		assertEquals(FileAttachmentType.PDF, wfDocAttachmentData.getAttachmentType());

		/* Bean mappings */
		List<WfDocBeanMapping> beanMappingList = wfDocData.getBeanMappingList();
		assertNotNull(beanMappingList);
		assertEquals(1, beanMappingList.size());
		WfDocBeanMapping wfDocBeanMappingData = beanMappingList.get(0);
		assertNotNull(wfDocBeanMappingData);
		assertEquals("custBeanMapping", wfDocBeanMappingData.getName());
		assertEquals("Customer Bean Mapping", wfDocBeanMappingData.getDescription());
		assertEquals("com.tcdng.jacklyn.common.TestCustomer", wfDocBeanMappingData.getBeanType());
		assertEquals(WorkflowBeanMappingType.PRIMARY, wfDocBeanMappingData.getType());
		List<WfDocFieldMapping> fieldMappingList = wfDocBeanMappingData.getFieldMappingList();
		assertNotNull(fieldMappingList);
		assertEquals(5, fieldMappingList.size());

		WfDocFieldMapping wfDocFieldMappingData = fieldMappingList.get(0);
		assertNotNull(wfDocFieldMappingData);
		assertEquals("id", wfDocFieldMappingData.getDocFieldName());
		assertEquals("id", wfDocFieldMappingData.getBeanFieldName());

		wfDocFieldMappingData = fieldMappingList.get(1);
		assertNotNull(wfDocFieldMappingData);
		assertEquals("firstName", wfDocFieldMappingData.getDocFieldName());
		assertEquals("firstName", wfDocFieldMappingData.getBeanFieldName());

		wfDocFieldMappingData = fieldMappingList.get(2);
		assertNotNull(wfDocFieldMappingData);
		assertEquals("lastName", wfDocFieldMappingData.getDocFieldName());
		assertEquals("lastName", wfDocFieldMappingData.getBeanFieldName());

		wfDocFieldMappingData = fieldMappingList.get(3);
		assertNotNull(wfDocFieldMappingData);
		assertEquals("age", wfDocFieldMappingData.getDocFieldName());
		assertEquals("age", wfDocFieldMappingData.getBeanFieldName());

		wfDocFieldMappingData = fieldMappingList.get(4);
		assertNotNull(wfDocFieldMappingData);
		assertEquals("height", wfDocFieldMappingData.getDocFieldName());
		assertEquals("height", wfDocFieldMappingData.getBeanFieldName());

		/* Forms */
		List<WfForm> wfFormList = wbModule.findWfForms(wfCategoryId);
		assertNotNull(wfFormList);
		assertEquals(1, wfFormList.size());

		WfForm wfFormData = wfFormList.get(0);
		assertNotNull(wfFormData);
		assertEquals("custForm", wfFormData.getName());
		assertEquals("Customer Form", wfFormData.getDescription());
		assertNull(wfFormData.getTabList());
		assertNull(wfFormData.getSectionList());
		assertNull(wfFormData.getFieldList());

		wfFormData = wbModule.findWfForm(wfFormData.getId());
		assertNotNull(wfFormData);
		assertEquals("custForm", wfFormData.getName());
		assertEquals("Customer Form", wfFormData.getDescription());
		assertEquals("custInfo", wfFormData.getWfDocName());

		/* Tabs */
		List<WfFormTab> tabList = wfFormData.getTabList();
		assertNotNull(tabList);
		assertEquals(1, tabList.size());

		WfFormTab wfFormTabData = tabList.get(0);
		assertNotNull(wfFormTabData);
		assertEquals("main", wfFormTabData.getName());
		assertEquals("Main Tab", wfFormTabData.getDescription());
		assertEquals("Main", wfFormTabData.getLabel());
		assertEquals(Boolean.TRUE, wfFormTabData.getPseudo());

		/* Sections */
		List<WfFormSection> sectionList = wfFormData.getSectionList();
		assertNotNull(sectionList);
		assertEquals(2, sectionList.size());

		WfFormSection wfFormSectionData = sectionList.get(0);
		assertNotNull(wfFormSectionData);
		assertEquals("basicDetails", wfFormSectionData.getName());
		assertEquals("Basic Details", wfFormSectionData.getDescription());
		assertNull(wfFormSectionData.getBinding());
		assertNull(wfFormSectionData.getLabel());

		wfFormSectionData = sectionList.get(1);
		assertNotNull(wfFormSectionData);
		assertEquals("licenseDetails", wfFormSectionData.getName());
		assertEquals("License Details", wfFormSectionData.getDescription());
		assertEquals("driversLicense", wfFormSectionData.getBinding());
		assertNull(wfFormSectionData.getLabel());

		/* Form fields */
		List<WfFormField> formFieldList = wfFormData.getFieldList();
		assertNotNull(formFieldList);
		assertEquals(7, formFieldList.size());

		WfFormField wfFormFieldData = formFieldList.get(0);
		assertNotNull(wfFormFieldData);
		assertEquals("basicDetails", wfFormFieldData.getSectionName());
		assertEquals("firstName", wfFormFieldData.getBinding());
		assertEquals("!ui-text", wfFormFieldData.getEditorUpl());
		assertEquals("First Name", wfFormFieldData.getLabel());
		assertEquals(Boolean.TRUE, wfFormFieldData.getRequired());

		wfFormFieldData = formFieldList.get(1);
		assertNotNull(wfFormFieldData);
		assertEquals("basicDetails", wfFormFieldData.getSectionName());
		assertEquals("lastName", wfFormFieldData.getBinding());
		assertEquals("!ui-text", wfFormFieldData.getEditorUpl());
		assertEquals("Last Name", wfFormFieldData.getLabel());
		assertEquals(Boolean.TRUE, wfFormFieldData.getRequired());

		wfFormFieldData = formFieldList.get(2);
		assertNotNull(wfFormFieldData);
		assertEquals("basicDetails", wfFormFieldData.getSectionName());
		assertEquals("age", wfFormFieldData.getBinding());
		assertEquals("!ui-integer", wfFormFieldData.getEditorUpl());
		assertEquals("Age", wfFormFieldData.getLabel());
		assertEquals(Boolean.TRUE, wfFormFieldData.getRequired());

		wfFormFieldData = formFieldList.get(3);
		assertNotNull(wfFormFieldData);
		assertEquals("basicDetails", wfFormFieldData.getSectionName());
		assertEquals("height", wfFormFieldData.getBinding());
		assertEquals("!ui-decimal", wfFormFieldData.getEditorUpl());
		assertEquals("Height", wfFormFieldData.getLabel());
		assertEquals(Boolean.TRUE, wfFormFieldData.getRequired());

		wfFormFieldData = formFieldList.get(4);
		assertNotNull(wfFormFieldData);
		assertEquals("licenseDetails", wfFormFieldData.getSectionName());
		assertEquals("licenseNo", wfFormFieldData.getBinding());
		assertEquals("!ui-text", wfFormFieldData.getEditorUpl());
		assertEquals("License No.", wfFormFieldData.getLabel());
		assertEquals(Boolean.TRUE, wfFormFieldData.getRequired());

		wfFormFieldData = formFieldList.get(5);
		assertNotNull(wfFormFieldData);
		assertEquals("licenseDetails", wfFormFieldData.getSectionName());
		assertEquals("issueDt", wfFormFieldData.getBinding());
		assertEquals("!ui-date", wfFormFieldData.getEditorUpl());
		assertEquals("Issue Date", wfFormFieldData.getLabel());
		assertEquals(Boolean.TRUE, wfFormFieldData.getRequired());

		wfFormFieldData = formFieldList.get(6);
		assertNotNull(wfFormFieldData);
		assertEquals("licenseDetails", wfFormFieldData.getSectionName());
		assertEquals("expiryDt", wfFormFieldData.getBinding());
		assertEquals("!ui-date", wfFormFieldData.getEditorUpl());
		assertEquals("Expiry Date", wfFormFieldData.getLabel());
		assertEquals(Boolean.TRUE, wfFormFieldData.getRequired());

		/* Messages */
		NotificationModule nbModule = getNotificationModule();
		String messageName
				= WorkflowUtils.getGlobalMessageName("customerCategory", "awaitCustApproval");
		NotificationTemplate notificationTemplateData
				= nbModule.findNotificationTemplate("workflow", messageName);
		assertNotNull(notificationTemplateData);
		assertEquals(messageName, notificationTemplateData.getName());
		assertEquals("Awaiting Customer Approval", notificationTemplateData.getDescription());
		assertEquals("Awaiting Customer Approval", notificationTemplateData.getSubject());
		assertEquals("Awaiting approval for {firstName} {lastName}",
				notificationTemplateData.getTemplate());
		assertEquals("default-attachmentgenerator",
				notificationTemplateData.getAttachmentGenerator());
		assertEquals(Boolean.FALSE, notificationTemplateData.getHtmlFlag());

		/* Templates */
		List<WfTemplate> wfTemplateList = wbModule.findWfTemplates(wfCategoryId);
		assertNotNull(wfTemplateList);
		assertEquals(1, wfTemplateList.size());

		// Template 1
		WfTemplate wfTemplateData = wfTemplateList.get(0);
		assertNotNull(wfTemplateData);
		assertEquals("custOnboarding", wfTemplateData.getName());
		assertEquals("Customer Onboarding", wfTemplateData.getDescription());
		assertEquals("custInfo", wfTemplateData.getWfDocName());
		assertEquals("Customer:{firstName} {lastName}", wfTemplateData.getItemDescFormat());
		assertNull(wfTemplateData.getStepList());

		wfTemplateData = wbModule.findWfTemplate(wfTemplateData.getId());
		assertNotNull(wfTemplateData);
		assertEquals("custOnboarding", wfTemplateData.getName());
		assertEquals("Customer Onboarding", wfTemplateData.getDescription());
		assertEquals("custInfo", wfTemplateData.getWfDocName());
		assertEquals("Customer:{firstName} {lastName}", wfTemplateData.getItemDescFormat());
		List<WfStep> stepList = wfTemplateData.getStepList();
		assertNotNull(stepList);
		assertEquals(4, stepList.size());

		/* Steps */
		// 1
		WfStep wfStepData = stepList.get(0);
		assertNotNull(wfStepData);
		assertEquals("start", wfStepData.getName());
		assertEquals("Start Step", wfStepData.getDescription());
		assertNull(wfStepData.getLabel());
		assertNull(wfStepData.getWfFormName());
		assertEquals(WorkflowStepType.START, wfStepData.getStepType());
		assertEquals(WorkflowParticipantType.NONE, wfStepData.getParticipantType());
		assertEquals(WorkflowStepPriority.NORMAL, wfStepData.getPriorityLevel());
		assertEquals(Integer.valueOf(0), wfStepData.getItemsPerSession());
		assertEquals(Integer.valueOf(0), wfStepData.getExpiryHours());
		assertFalse(wfStepData.getAudit());
		assertFalse(wfStepData.getBranchOnly());
		assertFalse(wfStepData.getIncludeForwarder());
		assertTrue(DataUtils.isBlank(wfStepData.getFormPrivilegeList()));
		assertTrue(DataUtils.isBlank(wfStepData.getRecordActionList()));
		assertTrue(DataUtils.isBlank(wfStepData.getUserActionList()));
		assertTrue(DataUtils.isBlank(wfStepData.getPolicyList()));
		assertTrue(DataUtils.isBlank(wfStepData.getAlertList()));

		List<WfEnrichment> enrichmentList = wfStepData.getEnrichmentList();
		assertNotNull(enrichmentList);
		assertEquals(1, enrichmentList.size());

		WfEnrichment wfEnrichmentData = enrichmentList.get(0);
		assertNotNull(wfEnrichmentData);
		assertEquals("testEnrichment", wfEnrichmentData.getName());
		assertEquals("Test Enrichment", wfEnrichmentData.getDescription());
		assertEquals("testcustomer-enrichmentlogic", wfEnrichmentData.getLogic());

		List<WfRouting> routingList = wfStepData.getRoutingList();
		assertNotNull(routingList);
		assertEquals(2, routingList.size());

		WfRouting wfRoutingData = routingList.get(0);
		assertNotNull(wfRoutingData);
		assertEquals("routToCreate", wfRoutingData.getName());
		assertEquals("Route to Create", wfRoutingData.getDescription());
		assertEquals("validAge", wfRoutingData.getClassifierName());
		assertEquals("createCust", wfRoutingData.getTargetWfStepName());

		wfRoutingData = routingList.get(1);
		assertNotNull(wfRoutingData);
		assertEquals("routToApproval", wfRoutingData.getName());
		assertEquals("Route to Approval", wfRoutingData.getDescription());
		assertNull(wfRoutingData.getClassifierName());
		assertEquals("custApproval", wfRoutingData.getTargetWfStepName());

		// 2
		wfStepData = stepList.get(1);
		assertNotNull(wfStepData);
		assertEquals("createCust", wfStepData.getName());
		assertEquals("Create Customer", wfStepData.getDescription());
		assertNull(wfStepData.getLabel());
		assertNull(wfStepData.getWfFormName());
		assertEquals(WorkflowStepType.AUTOMATIC, wfStepData.getStepType());
		assertEquals(WorkflowParticipantType.NONE, wfStepData.getParticipantType());
		assertEquals(WorkflowStepPriority.NORMAL, wfStepData.getPriorityLevel());
		assertEquals(Integer.valueOf(0), wfStepData.getItemsPerSession());
		assertEquals(Integer.valueOf(0), wfStepData.getExpiryHours());
		assertFalse(wfStepData.getAudit());
		assertFalse(wfStepData.getBranchOnly());
		assertFalse(wfStepData.getIncludeForwarder());
		assertTrue(DataUtils.isBlank(wfStepData.getFormPrivilegeList()));
		assertTrue(DataUtils.isBlank(wfStepData.getUserActionList()));
		assertTrue(DataUtils.isBlank(wfStepData.getAlertList()));
		assertTrue(DataUtils.isBlank(wfStepData.getEnrichmentList()));
		assertTrue(DataUtils.isBlank(wfStepData.getAlertList()));

		List<WfPolicy> policyList = wfStepData.getPolicyList();
		assertNotNull(policyList);
		assertEquals(1, policyList.size());

		WfPolicy wfPolicyData = policyList.get(0);
		assertNotNull(wfPolicyData);
		assertEquals("testOpenAccount", wfPolicyData.getName());
		assertEquals("Open Account", wfPolicyData.getDescription());
		assertEquals("testopenaccount-policylogic", wfPolicyData.getLogic());

		List<WfRecordAction> recordActionList = wfStepData.getRecordActionList();
		assertNotNull(recordActionList);
		assertEquals(1, recordActionList.size());
		WfRecordAction wfRecordActionData = recordActionList.get(0);
		assertNotNull(wfRecordActionData);
		assertEquals("createCust", wfRecordActionData.getName());
		assertEquals("Create Customer", wfRecordActionData.getDescription());
		assertEquals(WorkflowRecordActionType.CREATE, wfRecordActionData.getActionType());
		assertEquals("custBeanMapping", wfRecordActionData.getDocMappingName());

		routingList = wfStepData.getRoutingList();
		assertNotNull(routingList);
		assertEquals(1, routingList.size());

		wfRoutingData = routingList.get(0);
		assertNotNull(wfRoutingData);
		assertEquals("routToEnd", wfRoutingData.getName());
		assertEquals("Route to End", wfRoutingData.getDescription());
		assertNull(wfRoutingData.getClassifierName());
		assertEquals("end", wfRoutingData.getTargetWfStepName());

		// 3
		wfStepData = stepList.get(2);
		assertNotNull(wfStepData);
		assertEquals("custApproval", wfStepData.getName());
		assertEquals("Customer Approval", wfStepData.getDescription());
		assertNull(wfStepData.getLabel());
		assertEquals("custForm", wfStepData.getWfFormName());
		assertEquals(WorkflowStepType.INTERACTIVE, wfStepData.getStepType());
		assertEquals(WorkflowParticipantType.ALL, wfStepData.getParticipantType());
		assertEquals(WorkflowStepPriority.HIGH, wfStepData.getPriorityLevel());
		assertEquals(Integer.valueOf(0), wfStepData.getItemsPerSession());
		assertEquals(Integer.valueOf(0), wfStepData.getExpiryHours());
		assertFalse(wfStepData.getAudit());
		assertFalse(wfStepData.getBranchOnly());
		assertFalse(wfStepData.getIncludeForwarder());
		assertTrue(DataUtils.isBlank(wfStepData.getRoutingList()));
		assertTrue(DataUtils.isBlank(wfStepData.getRecordActionList()));
		assertTrue(DataUtils.isBlank(wfStepData.getEnrichmentList()));
		assertTrue(DataUtils.isBlank(wfStepData.getPolicyList()));

		List<WfAlert> alertList = wfStepData.getAlertList();
		assertNotNull(alertList);
		assertEquals(1, alertList.size());

		WfAlert wfAlertData = alertList.get(0);
		assertNotNull(wfAlertData);
		assertEquals("awaitApproval", wfAlertData.getName());
		assertEquals("Awaiting Approval", wfAlertData.getDescription());
		assertEquals("awaitCustApproval", wfAlertData.getNotificationTemplateCode());
		assertEquals(NotificationType.SYSTEM, wfAlertData.getType());

		List<WfUserAction> userActionList = wfStepData.getUserActionList();
		assertNotNull(userActionList);
		assertEquals(2, userActionList.size());

		WfUserAction wfUserActionData = userActionList.get(0);
		assertNotNull(wfUserActionData);
		assertEquals("approveCust", wfUserActionData.getName());
		assertEquals("Approve Customer", wfUserActionData.getDescription());
		assertEquals("Approve", wfUserActionData.getLabel());
		assertEquals("createCust", wfUserActionData.getTargetWfStepName());
		assertEquals(RequirementType.OPTIONAL, wfUserActionData.getNoteReqType());
		assertTrue(DataUtils.isBlank(wfUserActionData.getAttachmentCheckList()));

		wfUserActionData = userActionList.get(1);
		assertNotNull(wfUserActionData);
		assertEquals("rejectCust", wfUserActionData.getName());
		assertEquals("Reject Customer", wfUserActionData.getDescription());
		assertEquals("Reject", wfUserActionData.getLabel());
		assertEquals("end", wfUserActionData.getTargetWfStepName());
		assertEquals(RequirementType.MANDATORY, wfUserActionData.getNoteReqType());
		assertTrue(DataUtils.isBlank(wfUserActionData.getAttachmentCheckList()));

		// 4
		wfStepData = stepList.get(3);
		assertNotNull(wfStepData);
		assertEquals("end", wfStepData.getName());
		assertEquals("End Step", wfStepData.getDescription());
		assertNull(wfStepData.getLabel());
		assertEquals(WorkflowStepType.END, wfStepData.getStepType());
		assertEquals(WorkflowParticipantType.NONE, wfStepData.getParticipantType());
		assertEquals(WorkflowStepPriority.NORMAL, wfStepData.getPriorityLevel());
		assertEquals(Integer.valueOf(0), wfStepData.getItemsPerSession());
		assertEquals(Integer.valueOf(0), wfStepData.getExpiryHours());
		assertFalse(wfStepData.getAudit());
		assertFalse(wfStepData.getBranchOnly());
		assertFalse(wfStepData.getIncludeForwarder());
		assertTrue(DataUtils.isBlank(wfStepData.getUserActionList()));
		assertTrue(DataUtils.isBlank(wfStepData.getFormPrivilegeList()));
		assertTrue(DataUtils.isBlank(wfStepData.getRoutingList()));
		assertTrue(DataUtils.isBlank(wfStepData.getRecordActionList()));
		assertTrue(DataUtils.isBlank(wfStepData.getAlertList()));
		assertTrue(DataUtils.isBlank(wfStepData.getEnrichmentList()));
		assertTrue(DataUtils.isBlank(wfStepData.getPolicyList()));
		assertTrue(DataUtils.isBlank(wfStepData.getAlertList()));
	}

	@Test
	public void testSubmitToWorkflow() throws Exception {
		WorkflowModule wbModule = getWorkflowModule();
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 50, 1.82);
		Long wfItemId = wbModule.submitToWorkflow("customerCategory.custOnboarding", testCustomer);
		assertNotNull(wfItemId);
	}

	@Test
	public void testFindWorkflowItem() throws Exception {
		WorkflowModule wbModule = getWorkflowModule();
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 50, 1.82); // Use invalid age
		Long wfItemId = wbModule.submitToWorkflow("customerCategory.custOnboarding", testCustomer);

		WfItemObject workflowItem = wbModule.findWorkflowItem(wfItemId);
		assertNotNull(workflowItem);
		assertEquals("customerCategory.custOnboarding.custApproval",
				workflowItem.getWfStepDef().getGlobalName());
		assertEquals("custApproval", workflowItem.getWfStepDef().getName());
		assertEquals("Customer:Tom Jones", workflowItem.getDescription());
		assertNotNull(workflowItem.getWfItemHistId());
		assertNotNull(workflowItem.getWfHistEventId());
		assertNotNull(workflowItem.getPd());
		assertNotNull(workflowItem.getCreateDt());
		assertNotNull(workflowItem.getStepDt());
		assertNull(workflowItem.getHeldBy());
	}

	@Test
	public void testFindWorkflowItemHistory() throws Exception {
		WorkflowModule wbModule = getWorkflowModule();
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 50, 1.82); // Use invalid age
		Long wfItemId = wbModule.submitToWorkflow("customerCategory.custOnboarding", testCustomer);

		WfItemObject workflowItem = wbModule.findWorkflowItem(wfItemId);
		WfItemHistObject workflowItemHist
				= wbModule.findWorkflowItemHistory(workflowItem.getWfItemHistId(), false);
		assertNotNull(workflowItemHist);
		assertNotNull(workflowItemHist.getId());
		assertNull(workflowItemHist.getDocumentId());
		assertEquals("customerCategory.custInfo", workflowItemHist.getGlobalDocName());
		assertEquals("customerCategory.custOnboarding", workflowItemHist.getGlobalTemplateName());
		assertEquals("Customer:Tom Jones", workflowItemHist.getDescription());

		List<WfItemHistEvent> eventList = workflowItemHist.getEventList();
		assertNotNull(eventList);
		assertEquals(2, eventList.size());

		WfItemHistEvent wihe = eventList.get(0);
		assertEquals("start", wihe.getWfStep());
		assertNotNull(wihe.getStepDt());
		assertNull(wihe.getWfAction());
		assertNull(wihe.getActionDt());
		assertNull(wihe.getActor());
		assertNull(wihe.getNotes());

		wihe = eventList.get(1);
		assertEquals("custApproval", wihe.getWfStep());
		assertNotNull(wihe.getStepDt());
		assertNull(wihe.getWfAction());
		assertNull(wihe.getActionDt());
		assertNull(wihe.getActor());
		assertNull(wihe.getNotes());
	}

	@Test
	public void testRouteWorkflowItemByClassifier() throws Exception {
		WorkflowModule wbModule = getWorkflowModule();
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 25, 1.82); // Use valid age
		Long wfItemId = wbModule.submitToWorkflow("customerCategory.custOnboarding", testCustomer);
		assertNull(wfItemId);
	}

	@Test
	public void testGrabWorkflowItem() throws Exception {
		WorkflowModule wbModule = getWorkflowModule();
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 10, 1.82); // Use invalid age
		wbModule.submitToWorkflow("customerCategory.custOnboarding", testCustomer);

		List<Long> grabItemIdList
				= wbModule.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval");
		assertNotNull(grabItemIdList);
		assertEquals(1, grabItemIdList.size());
		assertNotNull(grabItemIdList.get(0));
	}

	@Test
	public void testApplyWorkflowAction() throws Exception {
		WorkflowModule wbModule = getWorkflowModule();
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 10, 1.82); // Use invalid age
		wbModule.submitToWorkflow("customerCategory.custOnboarding", testCustomer);

		Long gWfItemId = wbModule
				.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval").get(0);

		WfItemObject workflowItem = wbModule.findWorkflowItem(gWfItemId);
		workflowItem.setNotes("Goody!");
		wbModule.applyWorkflowAction(workflowItem, "rejectCust");
	}

	@Test
	public void testApplyUnknownWorkflowAction() throws Exception {
		WorkflowModule wbModule = getWorkflowModule();
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 10, 1.82); // Use invalid age
		wbModule.submitToWorkflow("customerCategory.custOnboarding", testCustomer);

		Long gWfItemId = wbModule
				.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval").get(0);

		String errorName = null;
		try {
			WfItemObject workflowItem = wbModule.findWorkflowItem(gWfItemId);
			workflowItem.setNotes("Goody!");
			wbModule.applyWorkflowAction(workflowItem, "rejectMyCust"); // Unknown action
		} catch (UnifyException e) {
			errorName = e.getErrorCode();
		}

		assertEquals(WorkflowModuleErrorConstants.WORKFLOW_STEP_ACTION_WITH_NAME_UNKNOWN,
				errorName);
	}

	@Test
	public void testApplyWorkflowActionNotHeld() throws Exception {
		WorkflowModule wbModule = getWorkflowModule();
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 50, 1.82); // Use invalid age
		Long wfItemId = wbModule.submitToWorkflow("customerCategory.custOnboarding", testCustomer);

		WfItemObject workflowItem = wbModule.findWorkflowItem(wfItemId);
		String errorName = null;
		try {
			workflowItem.setNotes("Goody!");
			wbModule.applyWorkflowAction(workflowItem, "approveCust");
		} catch (UnifyException e) {
			errorName = e.getErrorCode();
		}

		assertEquals(WorkflowModuleErrorConstants.WORKFLOW_APPLY_ACTION_UNHELD, errorName);
	}

	@Test
	public void testApplyWorkflowActionHistory() throws Exception {
		WorkflowModule wbModule = getWorkflowModule();
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 8, 1.82); // Use invalid age
		wbModule.submitToWorkflow("customerCategory.custOnboarding", testCustomer);

		Long gWfItemId = wbModule
				.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval").get(0);

		WfItemObject workflowItem = wbModule.findWorkflowItem(gWfItemId);
		Long wfItemHistId = workflowItem.getWfItemHistId();
		workflowItem.setNotes("Goody!");
		wbModule.applyWorkflowAction(workflowItem, "approveCust");

		WfItemHistObject workflowItemHist = wbModule.findWorkflowItemHistory(wfItemHistId, false);
		assertNotNull(workflowItemHist);
		assertNotNull(workflowItemHist.getId());
		assertNotNull(workflowItemHist.getDocumentId());
		assertEquals("customerCategory.custInfo", workflowItemHist.getGlobalDocName());
		assertEquals("customerCategory.custOnboarding", workflowItemHist.getGlobalTemplateName());
		assertEquals("Customer:Tom Jones", workflowItemHist.getDescription());

		List<WfItemHistEvent> eventList = workflowItemHist.getEventList();
		assertNotNull(eventList);
		assertEquals(4, eventList.size());

		WfItemHistEvent wihe = eventList.get(0);
		assertEquals("start", wihe.getWfStep());
		assertNotNull(wihe.getStepDt());
		assertNull(wihe.getWfAction());
		assertNull(wihe.getActor());
		assertNull(wihe.getNotes());
		assertNull(wihe.getActionDt());

		wihe = eventList.get(1);
		assertEquals("custApproval", wihe.getWfStep());
		assertNotNull(wihe.getStepDt());
		assertEquals("approveCust", wihe.getWfAction());
		assertEquals("SYSTEM", wihe.getActor());
		assertEquals("Goody!", wihe.getNotes());
		assertNotNull(wihe.getActionDt());

		wihe = eventList.get(2);
		assertEquals("createCust", wihe.getWfStep());
		assertNotNull(wihe.getStepDt());
		assertNull(wihe.getWfAction());
		assertNull(wihe.getActor());
		assertNull(wihe.getNotes());
		assertNull(wihe.getActionDt());

		wihe = eventList.get(3);
		assertEquals("end", wihe.getWfStep());
		assertNotNull(wihe.getStepDt());
		assertNull(wihe.getWfAction());
		assertNull(wihe.getActor());
		assertNull(wihe.getNotes());
		assertNull(wihe.getActionDt());
	}

	@Test
	public void testRouteWorkflowItemByAction() throws Exception {
		WorkflowModule wbModule = getWorkflowModule();
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 8, 1.82); // Use invalid age
		wbModule.submitToWorkflow("customerCategory.custOnboarding", testCustomer);

		Long gWfItemId = wbModule
				.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval").get(0);

		WfItemObject workflowItem = wbModule.findWorkflowItem(gWfItemId);
		workflowItem.setNotes("Goody!");
		wbModule.applyWorkflowAction(workflowItem, "rejectCust");

		String errorName = null;
		try {
			workflowItem = wbModule.findWorkflowItem(gWfItemId);
		} catch (UnifyException e) {
			errorName = e.getErrorCode();
		}

		assertEquals(UnifyCoreErrorConstants.RECORD_NO_MATCHING_RECORD, errorName);
	}

	@Test
	public void testWorkflowItemDeletedOnEndStep() throws Exception {
		WorkflowModule wbModule = getWorkflowModule();
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 50, 1.82); // Use invalid age
		wbModule.submitToWorkflow("customerCategory.custOnboarding", testCustomer);

		Long gWfItemId = wbModule
				.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval").get(0);

		WfItemObject workflowItem = wbModule.findWorkflowItem(gWfItemId);
		wbModule.applyWorkflowAction(workflowItem, "rejectCust");

		String errorName = null;
		try {
			workflowItem = wbModule.findWorkflowItem(gWfItemId);
		} catch (UnifyException e) {
			errorName = e.getErrorCode();
		}

		assertEquals(UnifyCoreErrorConstants.RECORD_NO_MATCHING_RECORD, errorName);
	}

	@Test
	public void testApplyEnrichment() throws Exception {
		WorkflowModule wbModule = getWorkflowModule();
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 8, 1.82); // Invalid age
		wbModule.submitToWorkflow("customerCategory.custOnboarding", testCustomer);

		Long gWfItemId = wbModule
				.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval").get(0);

		WfItemObject workflowItem = wbModule.findWorkflowItem(gWfItemId);
		PackableDoc pd = workflowItem.getPd();
		assertEquals("Tom Jones", pd.readFieldValue(String.class, "fullName"));
		assertEquals("0123456789", pd.readFieldValue(String.class, "accountNo"));
	}

	@Test
	public void testApplyExtensionPolicy() throws Exception {
		TestOpenAccountPolicyLogic testOpenAccountPolicyLogic
				= (TestOpenAccountPolicyLogic) getComponent("testopenaccount-policylogic");
		testOpenAccountPolicyLogic.clear();

		WorkflowModule wbModule = getWorkflowModule();
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 20, 1.82); // Valid age
		wbModule.submitToWorkflow("customerCategory.custOnboarding", testCustomer);

		OpenAccountDetails openAccountDetails = testOpenAccountPolicyLogic.getOpenAccountDetails();
		assertNotNull(openAccountDetails);
		assertEquals("Tom Jones", openAccountDetails.getFullName());
		assertEquals("0123456789", openAccountDetails.getAccountNo());
	}

	@Test
	public void testRecordActionCreate() throws Exception {
		WorkflowModule wbModule = getWorkflowModule();
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 50, 1.82); // Use invalid age
		wbModule.submitToWorkflow("customerCategory.custOnboarding", testCustomer);

		TestCustomerModule tcbm = (TestCustomerModule) getComponent("test-customerservice");
		TestCustomer createdCustomer = tcbm.findCustomer("Tom");
		assertNull(createdCustomer);

		Long gWfItemId = wbModule
				.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval").get(0);

		WfItemObject workflowItem = wbModule.findWorkflowItem(gWfItemId);
		workflowItem.setNotes("Goody!");
		wbModule.applyWorkflowAction(workflowItem, "approveCust");

		createdCustomer = tcbm.findCustomer("Tom");
		assertNotNull(createdCustomer);
		assertEquals("Tom", createdCustomer.getFirstName());
		assertEquals("Jones", createdCustomer.getLastName());
		assertEquals(50, createdCustomer.getAge());
		assertEquals(Double.valueOf(1.82), Double.valueOf(createdCustomer.getHeight()));
	}

	@Test
	public void testRecordActionUpdate() throws Exception {
		TestCustomerModule tcbm = (TestCustomerModule) getComponent("test-customerservice");
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 65, 1.82); // Class 1
		Long custId = tcbm.createCustomer(testCustomer);

		WorkflowModule wbModule = getWorkflowModule();
		testCustomer.setLastName("Doe");
		testCustomer.setHeight(2.86);
		wbModule.submitToWorkflow("customerRecActionCategory.manageCust", testCustomer);

		testCustomer = tcbm.findCustomer(custId);
		assertEquals("Tom", testCustomer.getFirstName());
		assertEquals("Doe", testCustomer.getLastName());
		assertEquals(65, testCustomer.getAge());
		assertEquals(Double.valueOf(2.86), Double.valueOf(testCustomer.getHeight()));
	}

	@Test
	public void testRecordActionRead() throws Exception {
		TestCustomerModule tcbm = (TestCustomerModule) getComponent("test-customerservice");
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 24, 1.82);
		Long custId = tcbm.createCustomer(testCustomer);

		WorkflowModule wbModule = getWorkflowModule();
		TestCustomer mockCustomer = new TestCustomer();
		mockCustomer.setId(custId);
		mockCustomer.setFirstName("mockFirstName");
		mockCustomer.setAge(45); // Class 2
		wbModule.submitToWorkflow("customerRecActionCategory.manageCust", mockCustomer);
	}

	@Test
	public void testRecordActionDelete() throws Exception {
		TestCustomerModule tcbm = (TestCustomerModule) getComponent("test-customerservice");
		TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 24, 1.82); // No class
		tcbm.createCustomer(testCustomer);

		TestCustomer foundCustomer = tcbm.findCustomer("Tom");
		assertNotNull(foundCustomer);

		WorkflowModule wbModule = getWorkflowModule();
		wbModule.submitToWorkflow("customerRecActionCategory.manageCust", testCustomer);

		foundCustomer = tcbm.findCustomer("Tom");
		assertNull(foundCustomer);
	}

	@Override
	protected void onSetup() throws Exception {
		if (!published) {
			WfCategoryConfig custWfCategoryConfig
					= readWfCategoryConfig("xml/wfcategory-customer.xml");
			publish(custWfCategoryConfig);

			WfCategoryConfig custRecActionWfCategoryConfig
					= readWfCategoryConfig("xml/wfcategory-customer-recordaction.xml");
			publish(custRecActionWfCategoryConfig);
			published = true;
		}
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		published = false;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onTearDown() throws Exception {
		deleteAll(TestCustomer.class, WfItemAttachment.class, WfItemPackedDoc.class, WfItem.class,
				WfItemEvent.class);
	}

	private WorkflowModule getWorkflowModule() throws Exception {
		return (WorkflowModule) getComponent(WorkflowModuleNameConstants.WORKFLOWBUSINESSMODULE);
	}

	private NotificationModule getNotificationModule() throws Exception {
		return (NotificationModule) getComponent(
				NotificationModuleNameConstants.NOTIFICATIONBUSINESSMODULE);
	}

	private WfCategoryConfig readWfCategoryConfig(String resourceName) throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = IOUtils.openClassLoaderResourceInputStream(resourceName);
			return WfDocumentConfigUtils.readXmlWfCategoryConfig(inputStream);
		} finally {
			IOUtils.close(inputStream);
		}
	}

	private void publish(WfCategoryConfig wfCategoryConfig) throws Exception {
		TaskMonitor taskMonitor
				= getWorkflowModule().startWorkflowCategoryPublication(wfCategoryConfig);
		while (!taskMonitor.isDone()) {
			ThreadUtils.yield();
		}

		if (taskMonitor.getExceptions() != null && taskMonitor.getExceptions().length > 0) {
			throw taskMonitor.getExceptions()[0];
		}
	}
}
