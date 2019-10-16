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
import com.tcdng.jacklyn.common.TestCustomerService;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.jacklyn.shared.workflow.WorkflowAlertType;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.shared.workflow.WorkflowRecordActionType;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepPriority;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepType;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfCategoryConfig;
import com.tcdng.jacklyn.shared.xml.util.WfCategoryConfigUtils;
import com.tcdng.jacklyn.workflow.TestOpenAccountProcessPolicy.OpenAccountDetails;
import com.tcdng.jacklyn.workflow.business.WorkflowService;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleErrorConstants;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem;
import com.tcdng.jacklyn.workflow.data.WfItemHistEvent;
import com.tcdng.jacklyn.workflow.data.WfItemHistory;
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
import com.tcdng.jacklyn.workflow.entities.WfMessage;
import com.tcdng.jacklyn.workflow.entities.WfPolicy;
import com.tcdng.jacklyn.workflow.entities.WfRecordAction;
import com.tcdng.jacklyn.workflow.entities.WfRouting;
import com.tcdng.jacklyn.workflow.entities.WfStep;
import com.tcdng.jacklyn.workflow.entities.WfTemplate;
import com.tcdng.jacklyn.workflow.entities.WfTemplateDoc;
import com.tcdng.jacklyn.workflow.entities.WfUserAction;
import com.tcdng.unify.core.UnifyCoreErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.criterion.FilterConditionType;
import com.tcdng.unify.core.data.PackableDoc;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.ThreadUtils;

/**
 * Workflow business service tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WorkflowServiceTest extends AbstractJacklynTest {

    private static boolean published = false;

    @Test
    public void testPublishedWorkflowCategory() throws Exception {
        WorkflowService wfService = getWorkflowService();
        /* Categories */
        List<WfCategory> wfCategoryList = wfService.findWfCategories(new WfCategoryQuery().name("customerCategory"));
        assertNotNull(wfCategoryList);
        assertEquals(1, wfCategoryList.size());

        WfCategory wfCategory = wfCategoryList.get(0);
        assertNotNull(wfCategory);
        assertEquals("customerCategory", wfCategory.getName());
        assertEquals("Customer Workflow Category", wfCategory.getDescription());
        assertEquals("1.0", wfCategory.getVersion());
        assertEquals(RecordStatus.ACTIVE, wfCategory.getStatus());

        /* Documents */
        Long wfCategoryId = wfCategory.getId();
        List<WfDoc> wfDocList = wfService.findWfDocs(wfCategoryId);
        assertNotNull(wfDocList);
        assertEquals(1, wfDocList.size());

        WfDoc wfDoc = wfDocList.get(0);
        assertNotNull(wfDoc);
        assertEquals("custDoc", wfDoc.getName());
        assertEquals("Customer Information", wfDoc.getDescription());
        assertNull(wfDoc.getFieldList());
        assertNull(wfDoc.getClassifierList());
        assertNull(wfDoc.getAttachmentList());
        assertNull(wfDoc.getBeanMappingList());

        wfDoc = wfService.findWfDoc(wfDoc.getId());
        assertNotNull(wfDoc);
        assertEquals("custDoc", wfDoc.getName());
        assertEquals("Customer Information", wfDoc.getDescription());

        /* Document fields */
        List<WfDocField> docFieldList = wfDoc.getFieldList();
        assertNotNull(docFieldList);
        assertEquals(10, docFieldList.size());

        WfDocField wfDocField = docFieldList.get(0);
        assertNotNull(wfDocField);
        assertNull(wfDocField.getParentName());
        assertEquals("firstName", wfDocField.getName());
        assertEquals("First Name", wfDocField.getDescription());
        assertEquals(DataType.STRING, wfDocField.getDataType());

        wfDocField = docFieldList.get(1);
        assertNotNull(wfDocField);
        assertNull(wfDocField.getParentName());
        assertEquals("lastName", wfDocField.getName());
        assertEquals("Last Name", wfDocField.getDescription());
        assertEquals(DataType.STRING, wfDocField.getDataType());

        wfDocField = docFieldList.get(2);
        assertNotNull(wfDocField);
        assertNull(wfDocField.getParentName());
        assertEquals("age", wfDocField.getName());
        assertEquals("Age", wfDocField.getDescription());
        assertEquals(DataType.INTEGER, wfDocField.getDataType());

        wfDocField = docFieldList.get(3);
        assertNotNull(wfDocField);
        assertNull(wfDocField.getParentName());
        assertEquals("height", wfDocField.getName());
        assertEquals("Height", wfDocField.getDescription());
        assertEquals(DataType.DOUBLE, wfDocField.getDataType());

        wfDocField = docFieldList.get(4);
        assertNotNull(wfDocField);
        assertNull(wfDocField.getParentName());
        assertEquals("accountNo", wfDocField.getName());
        assertEquals("Account Number", wfDocField.getDescription());
        assertEquals(DataType.STRING, wfDocField.getDataType());

        wfDocField = docFieldList.get(5);
        assertNotNull(wfDocField);
        assertNull(wfDocField.getParentName());
        assertEquals("fullName", wfDocField.getName());
        assertEquals("Full Name", wfDocField.getDescription());
        assertEquals(DataType.STRING, wfDocField.getDataType());

        wfDocField = docFieldList.get(6);
        assertNotNull(wfDocField);
        assertNull(wfDocField.getParentName());
        assertEquals("driversLicense", wfDocField.getName());
        assertEquals("Driver's License", wfDocField.getDescription());
        assertNull(wfDocField.getDataType());

        wfDocField = docFieldList.get(7);
        assertNotNull(wfDocField);
        assertEquals("driversLicense", wfDocField.getParentName());
        assertEquals("licenseNo", wfDocField.getName());
        assertEquals("License No.", wfDocField.getDescription());
        assertEquals(DataType.STRING, wfDocField.getDataType());

        wfDocField = docFieldList.get(8);
        assertNotNull(wfDocField);
        assertEquals("driversLicense", wfDocField.getParentName());
        assertEquals("issueDt", wfDocField.getName());
        assertEquals("Issue Date", wfDocField.getDescription());
        assertEquals(DataType.DATE, wfDocField.getDataType());

        wfDocField = docFieldList.get(9);
        assertNotNull(wfDocField);
        assertEquals("driversLicense", wfDocField.getParentName());
        assertEquals("expiryDt", wfDocField.getName());
        assertEquals("Expiry Date", wfDocField.getDescription());
        assertEquals(DataType.DATE, wfDocField.getDataType());

        /* Classifier */
        List<WfDocClassifier> classifierList = wfDoc.getClassifierList();
        assertNotNull(classifierList);
        assertEquals(1, classifierList.size());

        WfDocClassifier wfDocClassifier = classifierList.get(0);
        assertEquals("validAge", wfDocClassifier.getName());
        assertEquals("Valid Age", wfDocClassifier.getDescription());
        assertNull(wfDocClassifier.getLogic());
        List<WfDocClassifierFilter> filterList = wfDocClassifier.getFilterList();
        assertNotNull(filterList);
        assertEquals(1, filterList.size());
        WfDocClassifierFilter wfDocClassifierFilter = filterList.get(0);
        assertNotNull(wfDocClassifierFilter);
        assertEquals("age", wfDocClassifierFilter.getFieldName());
        assertEquals(FilterConditionType.BETWEEN, wfDocClassifierFilter.getOp());
        assertEquals("18", wfDocClassifierFilter.getValue1());
        assertEquals("40", wfDocClassifierFilter.getValue2());
        assertFalse(wfDocClassifierFilter.getFieldOnly());

        /* Attachments */
        List<WfDocAttachment> attachmentList = wfDoc.getAttachmentList();
        assertNotNull(attachmentList);
        assertEquals(1, attachmentList.size());
        WfDocAttachment wfDocAttachment = attachmentList.get(0);
        assertNotNull(wfDocAttachment);
        assertEquals("birthCert", wfDocAttachment.getName());
        assertEquals("Birth Certificate", wfDocAttachment.getDescription());
        assertEquals("Certificate", wfDocAttachment.getLabel());
        assertEquals(FileAttachmentType.PDF, wfDocAttachment.getAttachmentType());

        /* Bean mappings */
        List<WfDocBeanMapping> beanMappingList = wfDoc.getBeanMappingList();
        assertNotNull(beanMappingList);
        assertEquals(1, beanMappingList.size());
        WfDocBeanMapping wfDocBeanMapping = beanMappingList.get(0);
        assertNotNull(wfDocBeanMapping);
        assertEquals("custBeanMapping", wfDocBeanMapping.getName());
        assertEquals("Customer Bean Mapping", wfDocBeanMapping.getDescription());
        assertEquals("com.tcdng.jacklyn.common.TestCustomer", wfDocBeanMapping.getBeanType());
        List<WfDocFieldMapping> fieldMappingList = wfDocBeanMapping.getFieldMappingList();
        assertNotNull(fieldMappingList);
        assertEquals(4, fieldMappingList.size());

        WfDocFieldMapping wfDocFieldMapping = fieldMappingList.get(0);
        assertNotNull(wfDocFieldMapping);
        assertEquals("firstName", wfDocFieldMapping.getDocFieldName());
        assertEquals("firstName", wfDocFieldMapping.getBeanFieldName());

        wfDocFieldMapping = fieldMappingList.get(1);
        assertNotNull(wfDocFieldMapping);
        assertEquals("lastName", wfDocFieldMapping.getDocFieldName());
        assertEquals("lastName", wfDocFieldMapping.getBeanFieldName());

        wfDocFieldMapping = fieldMappingList.get(2);
        assertNotNull(wfDocFieldMapping);
        assertEquals("age", wfDocFieldMapping.getDocFieldName());
        assertEquals("age", wfDocFieldMapping.getBeanFieldName());

        wfDocFieldMapping = fieldMappingList.get(3);
        assertNotNull(wfDocFieldMapping);
        assertEquals("height", wfDocFieldMapping.getDocFieldName());
        assertEquals("height", wfDocFieldMapping.getBeanFieldName());

        /* Forms */
        WfForm wfForm = wfDoc.getWfForm();
        assertNotNull(wfForm);
        assertNotNull(wfForm.getTabList());
        assertNotNull(wfForm.getSectionList());
        assertNotNull(wfForm.getFieldList());

        /* Tabs */
        List<WfFormTab> tabList = wfForm.getTabList();
        assertNotNull(tabList);
        assertEquals(1, tabList.size());

        WfFormTab wfFormTab = tabList.get(0);
        assertNotNull(wfFormTab);
        assertEquals("main", wfFormTab.getName());
        assertEquals("Main Tab", wfFormTab.getDescription());
        assertEquals("Main", wfFormTab.getLabel());
        assertEquals(Boolean.TRUE, wfFormTab.getPseudo());

        /* Sections */
        List<WfFormSection> sectionList = wfForm.getSectionList();
        assertNotNull(sectionList);
        assertEquals(2, sectionList.size());

        WfFormSection wfFormSection = sectionList.get(0);
        assertNotNull(wfFormSection);
        assertEquals("basicDetails", wfFormSection.getName());
        assertEquals("Basic Details", wfFormSection.getDescription());
        assertNull(wfFormSection.getBinding());
        assertNull(wfFormSection.getLabel());

        wfFormSection = sectionList.get(1);
        assertNotNull(wfFormSection);
        assertEquals("licenseDetails", wfFormSection.getName());
        assertEquals("License Details", wfFormSection.getDescription());
        assertEquals("driversLicense", wfFormSection.getBinding());
        assertNull(wfFormSection.getLabel());

        /* Form fields */
        List<WfFormField> formFieldList = wfForm.getFieldList();
        assertNotNull(formFieldList);
        assertEquals(7, formFieldList.size());

        WfFormField wfFormField = formFieldList.get(0);
        assertNotNull(wfFormField);
        assertEquals("basicDetails", wfFormField.getSectionName());
        assertEquals("firstName", wfFormField.getBinding());
        assertEquals("!ui-text", wfFormField.getEditorUpl());
        assertEquals("First Name", wfFormField.getLabel());
        assertEquals(Boolean.TRUE, wfFormField.getRequired());

        wfFormField = formFieldList.get(1);
        assertNotNull(wfFormField);
        assertEquals("basicDetails", wfFormField.getSectionName());
        assertEquals("lastName", wfFormField.getBinding());
        assertEquals("!ui-text", wfFormField.getEditorUpl());
        assertEquals("Last Name", wfFormField.getLabel());
        assertEquals(Boolean.TRUE, wfFormField.getRequired());

        wfFormField = formFieldList.get(2);
        assertNotNull(wfFormField);
        assertEquals("basicDetails", wfFormField.getSectionName());
        assertEquals("age", wfFormField.getBinding());
        assertEquals("!ui-integer", wfFormField.getEditorUpl());
        assertEquals("Age", wfFormField.getLabel());
        assertEquals(Boolean.TRUE, wfFormField.getRequired());

        wfFormField = formFieldList.get(3);
        assertNotNull(wfFormField);
        assertEquals("basicDetails", wfFormField.getSectionName());
        assertEquals("height", wfFormField.getBinding());
        assertEquals("!ui-decimal", wfFormField.getEditorUpl());
        assertEquals("Height", wfFormField.getLabel());
        assertEquals(Boolean.TRUE, wfFormField.getRequired());

        wfFormField = formFieldList.get(4);
        assertNotNull(wfFormField);
        assertEquals("licenseDetails", wfFormField.getSectionName());
        assertEquals("licenseNo", wfFormField.getBinding());
        assertEquals("!ui-text", wfFormField.getEditorUpl());
        assertEquals("License No.", wfFormField.getLabel());
        assertEquals(Boolean.TRUE, wfFormField.getRequired());

        wfFormField = formFieldList.get(5);
        assertNotNull(wfFormField);
        assertEquals("licenseDetails", wfFormField.getSectionName());
        assertEquals("issueDt", wfFormField.getBinding());
        assertEquals("!ui-date", wfFormField.getEditorUpl());
        assertEquals("Issue Date", wfFormField.getLabel());
        assertEquals(Boolean.TRUE, wfFormField.getRequired());

        wfFormField = formFieldList.get(6);
        assertNotNull(wfFormField);
        assertEquals("licenseDetails", wfFormField.getSectionName());
        assertEquals("expiryDt", wfFormField.getBinding());
        assertEquals("!ui-date", wfFormField.getEditorUpl());
        assertEquals("Expiry Date", wfFormField.getLabel());
        assertEquals(Boolean.TRUE, wfFormField.getRequired());

        /* Messages */
        List<WfMessage> messageList = wfService.findWfMessages(wfCategoryId);
        assertNotNull(messageList);
        assertEquals(1, messageList.size());

        WfMessage wfMessage = messageList.get(0);
        assertNotNull(wfMessage);
        assertEquals("awaitCustApproval", wfMessage.getName());
        assertEquals("Awaiting Customer Approval", wfMessage.getDescription());
        assertEquals("Awaiting Customer Approval", wfMessage.getSubject());
        assertEquals("Awaiting approval for {firstName} {lastName}", wfMessage.getTemplate());
        assertEquals("default-attachmentgenerator", wfMessage.getAttachmentGenerator());
        assertEquals(Boolean.FALSE, wfMessage.getHtmlFlag());
        
        /* Templates */
        List<WfTemplate> wfTemplateList = wfService.findWfTemplates(wfCategoryId);
        assertNotNull(wfTemplateList);
        assertEquals(1, wfTemplateList.size());

        // Template 1
        WfTemplate wfTemplate = wfTemplateList.get(0);
        assertNotNull(wfTemplate);
        assertEquals("custOnboarding", wfTemplate.getName());
        assertEquals("Customer Onboarding", wfTemplate.getDescription());
        assertNull(wfTemplate.getStepList());

        wfTemplate = wfService.findWfTemplate(wfTemplate.getId());
        assertNotNull(wfTemplate);
        assertEquals("custOnboarding", wfTemplate.getName());
        assertEquals("Customer Onboarding", wfTemplate.getDescription());

        /* Documents */
        List<WfTemplateDoc> templateDocList = wfTemplate.getTemplateDocList();
        assertNotNull(templateDocList);
        assertEquals(1, templateDocList.size());
       
        WfTemplateDoc wfTemplateDoc = templateDocList.get(0);
        assertNotNull(wfTemplateDoc);
        assertEquals("custDoc", wfTemplateDoc.getWfDocName());
        assertEquals(Boolean.TRUE, wfTemplateDoc.getManual());
        assertNull(wfTemplateDoc.getWfDocViewer());

        /* Steps */
        List<WfStep> stepList = wfTemplate.getStepList();
        assertNotNull(stepList);
        assertEquals(5, stepList.size());
        // 0
        WfStep wfStep = stepList.get(0);
        assertNotNull(wfStep);
        assertEquals("manual", wfStep.getName());
        assertEquals("Manual Create Customer", wfStep.getDescription());
        assertNull(wfStep.getLabel());
        assertEquals(WorkflowStepType.MANUAL, wfStep.getStepType());
        assertEquals(WorkflowParticipantType.PERSONNEL, wfStep.getParticipantType());
        assertEquals(WorkflowStepPriority.NORMAL, wfStep.getPriorityLevel());
        assertEquals(Integer.valueOf(0), wfStep.getItemsPerSession());
        assertEquals(Integer.valueOf(0), wfStep.getExpiryHours());
        assertFalse(wfStep.getAudit());
        assertFalse(wfStep.getBranchOnly());
        assertFalse(wfStep.getIncludeForwarder());
        assertTrue(DataUtils.isBlank(wfStep.getFormPrivilegeList()));
        assertTrue(DataUtils.isBlank(wfStep.getRecordActionList()));
        assertTrue(DataUtils.isBlank(wfStep.getRoutingList()));
        assertTrue(DataUtils.isBlank(wfStep.getPolicyList()));
        assertTrue(DataUtils.isBlank(wfStep.getEnrichmentList()));
        assertTrue(DataUtils.isBlank(wfStep.getAlertList()));
        
        // Implicit user actions for manual step
        List<WfUserAction> userActionList = wfStep.getUserActionList();
        assertNotNull(userActionList);
        assertEquals(2, userActionList.size());

        WfUserAction wfUserAction = userActionList.get(0);
        assertNotNull(wfUserAction);
        assertEquals("submit", wfUserAction.getName());
        assertEquals("Manual Submit", wfUserAction.getDescription());
        assertEquals("Submit", wfUserAction.getLabel());
        assertEquals("start", wfUserAction.getTargetWfStepName());
        assertEquals(RequirementType.NONE, wfUserAction.getCommentReqType());
        assertTrue(DataUtils.isBlank(wfUserAction.getAttachmentCheckList()));

        wfUserAction = userActionList.get(1);
        assertNotNull(wfUserAction);
        assertEquals("discard", wfUserAction.getName());
        assertEquals("Manual Discard", wfUserAction.getDescription());
        assertEquals("Discard", wfUserAction.getLabel());
        assertEquals("end", wfUserAction.getTargetWfStepName());
        assertEquals(RequirementType.NONE, wfUserAction.getCommentReqType());
        assertTrue(DataUtils.isBlank(wfUserAction.getAttachmentCheckList()));
        
        // 1
        wfStep = stepList.get(1);
        assertNotNull(wfStep);
        assertEquals("start", wfStep.getName());
        assertEquals("Start Step", wfStep.getDescription());
        assertNull(wfStep.getLabel());
        assertEquals(WorkflowStepType.START, wfStep.getStepType());
        assertEquals(WorkflowParticipantType.NONE, wfStep.getParticipantType());
        assertEquals(WorkflowStepPriority.NORMAL, wfStep.getPriorityLevel());
        assertEquals(Integer.valueOf(0), wfStep.getItemsPerSession());
        assertEquals(Integer.valueOf(0), wfStep.getExpiryHours());
        assertFalse(wfStep.getAudit());
        assertFalse(wfStep.getBranchOnly());
        assertFalse(wfStep.getIncludeForwarder());
        assertTrue(DataUtils.isBlank(wfStep.getFormPrivilegeList()));
        assertTrue(DataUtils.isBlank(wfStep.getRecordActionList()));
        assertTrue(DataUtils.isBlank(wfStep.getUserActionList()));
        assertTrue(DataUtils.isBlank(wfStep.getPolicyList()));
        assertTrue(DataUtils.isBlank(wfStep.getAlertList()));

        List<WfEnrichment> enrichmentList = wfStep.getEnrichmentList();
        assertNotNull(enrichmentList);
        assertEquals(1, enrichmentList.size());

        WfEnrichment wfEnrichment = enrichmentList.get(0);
        assertNotNull(wfEnrichment);
        assertEquals("testEnrichment", wfEnrichment.getName());
        assertEquals("Test Enrichment", wfEnrichment.getDescription());
        assertNull(wfEnrichment.getDocName());
        assertEquals("testcustomer-enrichmentlogic", wfEnrichment.getLogic());

        List<WfRouting> routingList = wfStep.getRoutingList();
        assertNotNull(routingList);
        assertEquals(2, routingList.size());

        WfRouting wfRouting = routingList.get(0);
        assertNotNull(wfRouting);
        assertEquals("routToCreate", wfRouting.getName());
        assertEquals("Route to Create", wfRouting.getDescription());
        assertEquals("custDoc", wfRouting.getDocName());
        assertEquals("validAge", wfRouting.getClassifierName());
        assertEquals("createCust", wfRouting.getTargetWfStepName());

        wfRouting = routingList.get(1);
        assertNotNull(wfRouting);
        assertEquals("routToApproval", wfRouting.getName());
        assertEquals("Route to Approval", wfRouting.getDescription());
        assertNull(wfRouting.getDocName());
        assertNull(wfRouting.getClassifierName());
        assertEquals("custApproval", wfRouting.getTargetWfStepName());

        // 2
        wfStep = stepList.get(2);
        assertNotNull(wfStep);
        assertEquals("createCust", wfStep.getName());
        assertEquals("Create Customer", wfStep.getDescription());
        assertNull(wfStep.getLabel());
        assertEquals(WorkflowStepType.AUTOMATIC, wfStep.getStepType());
        assertEquals(WorkflowParticipantType.NONE, wfStep.getParticipantType());
        assertEquals(WorkflowStepPriority.NORMAL, wfStep.getPriorityLevel());
        assertEquals(Integer.valueOf(0), wfStep.getItemsPerSession());
        assertEquals(Integer.valueOf(0), wfStep.getExpiryHours());
        assertFalse(wfStep.getAudit());
        assertFalse(wfStep.getBranchOnly());
        assertFalse(wfStep.getIncludeForwarder());
        assertTrue(DataUtils.isBlank(wfStep.getFormPrivilegeList()));
        assertTrue(DataUtils.isBlank(wfStep.getUserActionList()));
        assertTrue(DataUtils.isBlank(wfStep.getAlertList()));
        assertTrue(DataUtils.isBlank(wfStep.getEnrichmentList()));
        assertTrue(DataUtils.isBlank(wfStep.getAlertList()));

        List<WfPolicy> policyList = wfStep.getPolicyList();
        assertNotNull(policyList);
        assertEquals(1, policyList.size());

        WfPolicy wfPolicy = policyList.get(0);
        assertNotNull(wfPolicy);
        assertEquals("testOpenAccount", wfPolicy.getName());
        assertEquals("Open Account", wfPolicy.getDescription());
        assertNull(wfPolicy.getDocName());
        assertEquals("testopenaccount-processpolicy", wfPolicy.getLogic());

        List<WfRecordAction> recordActionList = wfStep.getRecordActionList();
        assertNotNull(recordActionList);
        assertEquals(1, recordActionList.size());
        WfRecordAction wfRecordAction = recordActionList.get(0);
        assertNotNull(wfRecordAction);
        assertEquals("createCust", wfRecordAction.getName());
        assertEquals("Create Customer", wfRecordAction.getDescription());
        assertEquals(WorkflowRecordActionType.CREATE, wfRecordAction.getActionType());
        assertEquals("custDoc", wfRecordAction.getDocName());
        assertEquals("custBeanMapping", wfRecordAction.getDocMappingName());

        routingList = wfStep.getRoutingList();
        assertNotNull(routingList);
        assertEquals(1, routingList.size());

        wfRouting = routingList.get(0);
        assertNotNull(wfRouting);
        assertEquals("routToEnd", wfRouting.getName());
        assertEquals("Route to End", wfRouting.getDescription());
        assertNull(wfRouting.getDocName());
        assertNull(wfRouting.getClassifierName());
        assertEquals("end", wfRouting.getTargetWfStepName());

        // 3
        wfStep = stepList.get(3);
        assertNotNull(wfStep);
        assertEquals("custApproval", wfStep.getName());
        assertEquals("Customer Approval", wfStep.getDescription());
        assertNull(wfStep.getLabel());
        assertEquals(WorkflowStepType.INTERACTIVE, wfStep.getStepType());
        assertEquals(WorkflowParticipantType.ALL, wfStep.getParticipantType());
        assertEquals(WorkflowStepPriority.HIGH, wfStep.getPriorityLevel());
        assertEquals(Integer.valueOf(0), wfStep.getItemsPerSession());
        assertEquals(Integer.valueOf(0), wfStep.getExpiryHours());
        assertFalse(wfStep.getAudit());
        assertFalse(wfStep.getBranchOnly());
        assertFalse(wfStep.getIncludeForwarder());
        assertTrue(DataUtils.isBlank(wfStep.getRoutingList()));
        assertTrue(DataUtils.isBlank(wfStep.getRecordActionList()));
        assertTrue(DataUtils.isBlank(wfStep.getEnrichmentList()));
        assertTrue(DataUtils.isBlank(wfStep.getPolicyList()));

        List<WfAlert> alertList = wfStep.getAlertList();
        assertNotNull(alertList);
        assertEquals(1, alertList.size());

        WfAlert wfAlert = alertList.get(0);
        assertNotNull(wfAlert);
        assertEquals("awaitApproval", wfAlert.getName());
        assertEquals("Awaiting Approval", wfAlert.getDescription());
        assertEquals("awaitCustApproval", wfAlert.getNotificationTemplateCode());
        assertEquals(WorkflowAlertType.USER_INTERACT, wfAlert.getType());
        assertEquals(NotificationType.SYSTEM, wfAlert.getChannel());

        userActionList = wfStep.getUserActionList();
        assertNotNull(userActionList);
        assertEquals(2, userActionList.size());

        wfUserAction = userActionList.get(0);
        assertNotNull(wfUserAction);
        assertEquals("approveCust", wfUserAction.getName());
        assertEquals("Approve Customer", wfUserAction.getDescription());
        assertEquals("Approve", wfUserAction.getLabel());
        assertEquals("createCust", wfUserAction.getTargetWfStepName());
        assertEquals(RequirementType.OPTIONAL, wfUserAction.getCommentReqType());
        assertTrue(DataUtils.isBlank(wfUserAction.getAttachmentCheckList()));

        wfUserAction = userActionList.get(1);
        assertNotNull(wfUserAction);
        assertEquals("rejectCust", wfUserAction.getName());
        assertEquals("Reject Customer", wfUserAction.getDescription());
        assertEquals("Reject", wfUserAction.getLabel());
        assertEquals("end", wfUserAction.getTargetWfStepName());
        assertEquals(RequirementType.MANDATORY, wfUserAction.getCommentReqType());
        assertTrue(DataUtils.isBlank(wfUserAction.getAttachmentCheckList()));

        // 4
        wfStep = stepList.get(4);
        assertNotNull(wfStep);
        assertEquals("end", wfStep.getName());
        assertEquals("End Step", wfStep.getDescription());
        assertNull(wfStep.getLabel());
        assertEquals(WorkflowStepType.END, wfStep.getStepType());
        assertEquals(WorkflowParticipantType.NONE, wfStep.getParticipantType());
        assertEquals(WorkflowStepPriority.NORMAL, wfStep.getPriorityLevel());
        assertEquals(Integer.valueOf(0), wfStep.getItemsPerSession());
        assertEquals(Integer.valueOf(0), wfStep.getExpiryHours());
        assertFalse(wfStep.getAudit());
        assertFalse(wfStep.getBranchOnly());
        assertFalse(wfStep.getIncludeForwarder());
        assertTrue(DataUtils.isBlank(wfStep.getUserActionList()));
        assertTrue(DataUtils.isBlank(wfStep.getFormPrivilegeList()));
        assertTrue(DataUtils.isBlank(wfStep.getRoutingList()));
        assertTrue(DataUtils.isBlank(wfStep.getRecordActionList()));
        assertTrue(DataUtils.isBlank(wfStep.getAlertList()));
        assertTrue(DataUtils.isBlank(wfStep.getEnrichmentList()));
        assertTrue(DataUtils.isBlank(wfStep.getPolicyList()));
        assertTrue(DataUtils.isBlank(wfStep.getAlertList()));
    }

    @Test
    public void testSubmitToWorkflow() throws Exception {
        WorkflowService wfService = getWorkflowService();
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 50, 1.82);
        Long submissionId = wfService.submitToWorkflow("customerCategory.custOnboarding.custDoc", testCustomer);
        assertNotNull(submissionId);
    }

    @Test
    public void testFindWorkflowItemBySubmission() throws Exception {
        WorkflowService wfService = getWorkflowService();
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 50, 1.82); // Use invalid age
        Long submissionId = wfService.submitToWorkflow("customerCategory.custOnboarding.custDoc", testCustomer);
        wfService.ensureSubmissionsProcessed(submissionId);
        
        FlowingWfItem flowingWfItem = wfService.findWorkflowItemBySubmission(submissionId);
        assertNotNull(flowingWfItem);
        assertNotNull(flowingWfItem.getWfStepDef());
        assertNotNull(flowingWfItem.getWfTemplateDocDef());
        assertEquals("customerCategory.custOnboarding.custApproval", flowingWfItem.getWfStepDef().getGlobalName());
        assertEquals("custApproval", flowingWfItem.getWfStepDef().getName());
        assertEquals("Customer:Tom Jones", flowingWfItem.getDescription());
        assertEquals("custDoc", flowingWfItem.getWfTemplateDocDef().getDocName());
        assertNotNull(flowingWfItem.getWfTemplateDocDef().getWfDocDef());
        assertNotNull(flowingWfItem.getWfTemplateDocDef().getWfDocUplGenerator());
        assertNotNull(flowingWfItem.getWfItemHistId());
        assertNotNull(flowingWfItem.getWfHistEventId());
        assertNotNull(flowingWfItem.getPd());
        assertNotNull(flowingWfItem.getCreateDt());
        assertNotNull(flowingWfItem.getStepDt());
        assertNull(flowingWfItem.getHeldBy());
        assertEquals("wfsingleformviewer-generator>g>customerCategory.custDoc", flowingWfItem.getDocViewer());
    }

    @Test
    public void testFindWorkflowItemHistory() throws Exception {
        WorkflowService wfService = getWorkflowService();
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 50, 1.82); // Use invalid age
        Long submissionId = wfService.submitToWorkflow("customerCategory.custOnboarding.custDoc", testCustomer);
        wfService.ensureSubmissionsProcessed(submissionId);

        FlowingWfItem flowingWfItem = wfService.findWorkflowItemBySubmission(submissionId);
        WfItemHistory workflowItemHist = wfService.findWorkflowItemHistory(flowingWfItem.getWfItemHistId(), false);
        assertNotNull(workflowItemHist);
        assertNotNull(workflowItemHist.getId());
        assertNull(workflowItemHist.getDocId());
        assertEquals("customerCategory.custDoc", workflowItemHist.getDocGlobalName());
        assertEquals("customerCategory.custOnboarding", workflowItemHist.getTemplateGlobalName());
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
        assertNull(wihe.getComments());

        wihe = eventList.get(1);
        assertEquals("custApproval", wihe.getWfStep());
        assertNotNull(wihe.getStepDt());
        assertNull(wihe.getWfAction());
        assertNull(wihe.getActionDt());
        assertNull(wihe.getActor());
        assertNull(wihe.getComments());
    }

    @Test
    public void testRouteWorkflowItemByClassifier() throws Exception {
        WorkflowService wfService = getWorkflowService();
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 25, 1.82); // Use valid age
        wfService.submitToWorkflow("customerCategory.custOnboarding.custDoc", testCustomer);
    }

    @Test
    public void testGrabWorkflowItem() throws Exception {
        WorkflowService wfService = getWorkflowService();
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 10, 1.82); // Use invalid age
        Long submissionId = wfService.submitToWorkflow("customerCategory.custOnboarding.custDoc", testCustomer);
        wfService.ensureSubmissionsProcessed(submissionId);

        List<Long> grabItemIdList = wfService.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval");
        assertNotNull(grabItemIdList);
        assertEquals(1, grabItemIdList.size());
        assertNotNull(grabItemIdList.get(0));
    }

    @Test
    public void testApplyWorkflowAction() throws Exception {
        WorkflowService wfService = getWorkflowService();
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 10, 1.82); // Use invalid age
        Long submissionId = wfService.submitToWorkflow("customerCategory.custOnboarding.custDoc", testCustomer);
        wfService.ensureSubmissionsProcessed(submissionId);

        Long gWfItemId = wfService.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval").get(0);

        FlowingWfItem flowingWfItem = wfService.findWorkflowItem(gWfItemId);
        flowingWfItem.setComment("Goody!");
        wfService.applyWorkflowAction(flowingWfItem, "rejectCust");
    }

    @Test
    public void testApplyUnknownWorkflowAction() throws Exception {
        WorkflowService wfService = getWorkflowService();
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 10, 1.82); // Use invalid age
        Long submissionId = wfService.submitToWorkflow("customerCategory.custOnboarding.custDoc", testCustomer);
        wfService.ensureSubmissionsProcessed(submissionId);

        Long gWfItemId = wfService.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval").get(0);

        String errorName = null;
        try {
            FlowingWfItem flowingWfItem = wfService.findWorkflowItem(gWfItemId);
            flowingWfItem.setComment("Goody!");
            wfService.applyWorkflowAction(flowingWfItem, "rejectMyCust"); // Unknown action
        } catch (UnifyException e) {
            errorName = e.getErrorCode();
        }

        assertEquals(WorkflowModuleErrorConstants.WORKFLOW_STEP_ACTION_WITH_NAME_UNKNOWN, errorName);
    }

    @Test
    public void testApplyWorkflowActionNotHeld() throws Exception {
        WorkflowService wfService = getWorkflowService();
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 50, 1.82); // Use invalid age
        Long submissionId = wfService.submitToWorkflow("customerCategory.custOnboarding.custDoc", testCustomer);
        wfService.ensureSubmissionsProcessed(submissionId);

        FlowingWfItem flowingWfItem = wfService.findWorkflowItemBySubmission(submissionId);
        String errorName = null;
        try {
            flowingWfItem.setComment("Goody!");
            wfService.applyWorkflowAction(flowingWfItem, "approveCust");
        } catch (UnifyException e) {
            errorName = e.getErrorCode();
        }

        assertEquals(WorkflowModuleErrorConstants.WORKFLOW_APPLY_ACTION_UNHELD, errorName);
    }

    @Test
    public void testApplyWorkflowActionHistory() throws Exception {
        WorkflowService wfService = getWorkflowService();
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 8, 1.82); // Use invalid age
        Long submissionId = wfService.submitToWorkflow("customerCategory.custOnboarding.custDoc", testCustomer);
        wfService.ensureSubmissionsProcessed(submissionId);

        Long gWfItemId = wfService.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval").get(0);

        FlowingWfItem flowingWfItem = wfService.findWorkflowItem(gWfItemId);
        Long wfItemHistId = flowingWfItem.getWfItemHistId();
        flowingWfItem.setComment("Goody!");
        submissionId = wfService.applyWorkflowAction(flowingWfItem, "approveCust");
        wfService.ensureSubmissionsProcessed(submissionId);

        WfItemHistory workflowItemHist = wfService.findWorkflowItemHistory(wfItemHistId, false);
        assertNotNull(workflowItemHist);
        assertNotNull(workflowItemHist.getId());
        assertNotNull(workflowItemHist.getDocId());
        assertEquals("customerCategory.custDoc", workflowItemHist.getDocGlobalName());
        assertEquals("customerCategory.custOnboarding", workflowItemHist.getTemplateGlobalName());
        assertEquals("Customer:Tom Jones", workflowItemHist.getDescription());

        List<WfItemHistEvent> eventList = workflowItemHist.getEventList();
        assertNotNull(eventList);
        assertEquals(4, eventList.size());

        WfItemHistEvent wihe = eventList.get(0);
        assertEquals("start", wihe.getWfStep());
        assertNotNull(wihe.getStepDt());
        assertNull(wihe.getWfAction());
        assertNull(wihe.getActor());
        assertNull(wihe.getComments());
        assertNull(wihe.getActionDt());

        wihe = eventList.get(1);
        assertEquals("custApproval", wihe.getWfStep());
        assertNotNull(wihe.getStepDt());
        assertEquals("approveCust", wihe.getWfAction());
        assertEquals("SYSTEM", wihe.getActor());
        assertEquals("Goody!", wihe.getComments());
        assertNotNull(wihe.getActionDt());

        wihe = eventList.get(2);
        assertEquals("createCust", wihe.getWfStep());
        assertNotNull(wihe.getStepDt());
        assertNull(wihe.getWfAction());
        assertNull(wihe.getActor());
        assertNull(wihe.getComments());
        assertNull(wihe.getActionDt());

        wihe = eventList.get(3);
        assertEquals("end", wihe.getWfStep());
        assertNotNull(wihe.getStepDt());
        assertNull(wihe.getWfAction());
        assertNull(wihe.getActor());
        assertNull(wihe.getComments());
        assertNull(wihe.getActionDt());
    }

    @Test
    public void testRouteWorkflowItemByAction() throws Exception {
        WorkflowService wfService = getWorkflowService();
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 8, 1.82); // Use invalid age
        Long submissionId = wfService.submitToWorkflow("customerCategory.custOnboarding.custDoc", testCustomer);
        wfService.ensureSubmissionsProcessed(submissionId);

        Long gWfItemId = wfService.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval").get(0);

        FlowingWfItem flowingWfItem = wfService.findWorkflowItem(gWfItemId);
        flowingWfItem.setComment("Goody!");
        submissionId = wfService.applyWorkflowAction(flowingWfItem, "rejectCust");
        wfService.ensureSubmissionsProcessed(submissionId);

        String errorName = null;
        try {
            flowingWfItem = wfService.findWorkflowItem(gWfItemId);
        } catch (UnifyException e) {
            errorName = e.getErrorCode();
        }

        assertEquals(UnifyCoreErrorConstants.RECORD_NO_MATCHING_RECORD, errorName);
    }

    @Test
    public void testWorkflowItemDeletedOnEndStep() throws Exception {
        WorkflowService wfService = getWorkflowService();
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 50, 1.82); // Use invalid age
        Long submissionId = wfService.submitToWorkflow("customerCategory.custOnboarding.custDoc", testCustomer);
        wfService.ensureSubmissionsProcessed(submissionId);

        Long gWfItemId = wfService.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval").get(0);

        FlowingWfItem flowingWfItem = wfService.findWorkflowItem(gWfItemId);
        submissionId = wfService.applyWorkflowAction(flowingWfItem, "rejectCust");
        wfService.ensureSubmissionsProcessed(submissionId);

        String errorName = null;
        try {
            flowingWfItem = wfService.findWorkflowItem(gWfItemId);
        } catch (UnifyException e) {
            errorName = e.getErrorCode();
        }

        assertEquals(UnifyCoreErrorConstants.RECORD_NO_MATCHING_RECORD, errorName);
    }

    @Test
    public void testApplyEnrichment() throws Exception {
        WorkflowService wfService = getWorkflowService();
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 8, 1.82); // Invalid age
        Long submissionId = wfService.submitToWorkflow("customerCategory.custOnboarding.custDoc", testCustomer);
        wfService.ensureSubmissionsProcessed(submissionId);

        Long gWfItemId = wfService.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval").get(0);

        FlowingWfItem flowingWfItem = wfService.findWorkflowItem(gWfItemId);
        PackableDoc pd = flowingWfItem.getPd();
        assertEquals("Tom Jones", pd.read(String.class, "fullName"));
        assertEquals("0123456789", pd.read(String.class, "accountNo"));
    }

    @Test
    public void testApplyExtensionPolicy() throws Exception {
        TestOpenAccountProcessPolicy testOpenAccountPolicyLogic = (TestOpenAccountProcessPolicy) getComponent(
                "testopenaccount-processpolicy");
        testOpenAccountPolicyLogic.clear();

        WorkflowService wfService = getWorkflowService();
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 20, 1.82); // Valid age
        Long submissionId = wfService.submitToWorkflow("customerCategory.custOnboarding.custDoc", testCustomer);
        wfService.ensureSubmissionsProcessed(submissionId);
        
        OpenAccountDetails openAccountDetails = testOpenAccountPolicyLogic.getOpenAccountDetails();
        assertNotNull(openAccountDetails);
        assertEquals("Tom Jones", openAccountDetails.getFullName());
        assertEquals("0123456789", openAccountDetails.getAccountNo());
    }

    @Test
    public void testRecordActionCreate() throws Exception {
        WorkflowService wfService = getWorkflowService();
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 50, 1.82); // Use invalid age
        Long submissionId = wfService.submitToWorkflow("customerCategory.custOnboarding.custDoc", testCustomer);
        wfService.ensureSubmissionsProcessed(submissionId);

        TestCustomerService tcbm = (TestCustomerService) getComponent("test-customerservice");
        TestCustomer createdCustomer = tcbm.findCustomer("Tom");
        assertNull(createdCustomer);

        Long gWfItemId = wfService.grabCurrentUserWorkItems("customerCategory.custOnboarding.custApproval").get(0);

        FlowingWfItem flowingWfItem = wfService.findWorkflowItem(gWfItemId);
        flowingWfItem.setComment("Goody!");
        submissionId = wfService.applyWorkflowAction(flowingWfItem, "approveCust");
        wfService.ensureSubmissionsProcessed(submissionId);

        createdCustomer = tcbm.findCustomer("Tom");
        assertNotNull(createdCustomer);
        assertEquals("Tom", createdCustomer.getFirstName());
        assertEquals("Jones", createdCustomer.getLastName());
        assertEquals(50, createdCustomer.getAge());
        assertEquals(Double.valueOf(1.82), Double.valueOf(createdCustomer.getHeight()));
    }

    @Test
    public void testRecordActionUpdate() throws Exception {
        TestCustomerService tcbm = (TestCustomerService) getComponent("test-customerservice");
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 65, 1.82); // Class 1
        Long custId = tcbm.createCustomer(testCustomer);

        WorkflowService wfService = getWorkflowService();
        testCustomer.setLastName("Doe");
        testCustomer.setHeight(2.86);
        Long submissionId = wfService.submitToWorkflow("customerRecActionCategory.manageCust.custDoc", testCustomer);
        wfService.ensureSubmissionsProcessed(submissionId);
        
        testCustomer = tcbm.findCustomer(custId);
        assertEquals("Tom", testCustomer.getFirstName());
        assertEquals("Doe", testCustomer.getLastName());
        assertEquals(65, testCustomer.getAge());
        assertEquals(Double.valueOf(2.86), Double.valueOf(testCustomer.getHeight()));
    }

    @Test
    public void testRecordActionRead() throws Exception {
        TestCustomerService tcbm = (TestCustomerService) getComponent("test-customerservice");
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 24, 1.82);
        Long custId = tcbm.createCustomer(testCustomer);

        WorkflowService wfService = getWorkflowService();
        TestCustomer mockCustomer = new TestCustomer();
        mockCustomer.setId(custId);
        mockCustomer.setFirstName("mockFirstName");
        mockCustomer.setAge(45); // Class 2
        Long submissionId = wfService.submitToWorkflow("customerRecActionCategory.manageCust.custDoc", mockCustomer);
        wfService.ensureSubmissionsProcessed(submissionId);
        
        //  Read should have updated packable document fields with details from database
        FlowingWfItem flowingWfItem = wfService.findWorkflowItemBySubmission(submissionId);
        PackableDoc pd = flowingWfItem.getPd();
        assertEquals("Tom", pd.read("firstName"));
        assertEquals("Jones", pd.read("lastName"));
        assertEquals(Integer.valueOf(24), pd.read("age"));
        assertEquals(Double.valueOf(1.82), pd.read("height"));
    }

    @Test
    public void testRecordActionDelete() throws Exception {
        TestCustomerService tcbm = (TestCustomerService) getComponent("test-customerservice");
        TestCustomer testCustomer = new TestCustomer("Tom", "Jones", 24, 1.82); // No class
        tcbm.createCustomer(testCustomer);

        TestCustomer foundCustomer = tcbm.findCustomer("Tom");
        assertNotNull(foundCustomer);

        WorkflowService wfService = getWorkflowService();
        Long submissionId = wfService.submitToWorkflow("customerRecActionCategory.manageCust.custDoc", testCustomer);
        wfService.ensureSubmissionsProcessed(submissionId);
        
        foundCustomer = tcbm.findCustomer("Tom");
        assertNull(foundCustomer);
    }

    @Override
    protected void onSetup() throws Exception {
        if (!published) {
            WfCategoryConfig custWfCategoryConfig = readWfCategoryConfig("xml/wfcustomer.xml");
            publish(custWfCategoryConfig);

            WfCategoryConfig custRecActionWfCategoryConfig = readWfCategoryConfig("xml/wfcustomer-recordaction.xml");
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
        deleteAll(TestCustomer.class, WfItemAttachment.class, WfItemPackedDoc.class, WfItem.class, WfItemEvent.class);
    }

    private WorkflowService getWorkflowService() throws Exception {
        return (WorkflowService) getComponent(WorkflowModuleNameConstants.WORKFLOWSERVICE);
    }

    private WfCategoryConfig readWfCategoryConfig(String resourceName) throws Exception {
        InputStream inputStream = null;
        try {
            inputStream = IOUtils.openClassLoaderResourceInputStream(resourceName);
            return WfCategoryConfigUtils.readWfCategoryConfig(inputStream);
        } finally {
            IOUtils.close(inputStream);
        }
    }

    private void publish(WfCategoryConfig wfCategoryConfig) throws Exception {
        TaskMonitor taskMonitor = getWorkflowService().startWorkflowCategoryPublication(wfCategoryConfig, true);
        while (!taskMonitor.isDone()) {
            ThreadUtils.yield();
        }

        if (taskMonitor.getExceptions() != null && taskMonitor.getExceptions().length > 0) {
            throw taskMonitor.getExceptions()[0];
        }
    }
}
