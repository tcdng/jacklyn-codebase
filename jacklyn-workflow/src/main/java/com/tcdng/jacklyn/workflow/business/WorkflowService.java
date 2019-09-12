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

import java.util.Collection;
import java.util.List;

import com.tcdng.jacklyn.common.business.JacklynBusinessService;
import com.tcdng.jacklyn.shared.workflow.data.ToolingEnrichmentLogicItem;
import com.tcdng.jacklyn.shared.workflow.data.ToolingItemClassifierLogicItem;
import com.tcdng.jacklyn.shared.workflow.data.ToolingPolicyLogicItem;
import com.tcdng.jacklyn.shared.workflow.data.ToolingWfDocUplGeneratorItem;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfCategoryConfig;
import com.tcdng.jacklyn.workflow.data.InteractWfItem;
import com.tcdng.jacklyn.workflow.data.InteractWfItems;
import com.tcdng.jacklyn.workflow.data.ManualInitInfo;
import com.tcdng.jacklyn.workflow.data.ManualWfItem;
import com.tcdng.jacklyn.workflow.data.WfFormDef;
import com.tcdng.jacklyn.workflow.data.WfItemAttachmentInfo;
import com.tcdng.jacklyn.workflow.data.WfItemHistory;
import com.tcdng.jacklyn.workflow.data.WfItemSummary;
import com.tcdng.jacklyn.workflow.data.WfProcessDef;
import com.tcdng.jacklyn.workflow.data.WfTemplateLargeData;
import com.tcdng.jacklyn.workflow.entities.WfCategory;
import com.tcdng.jacklyn.workflow.entities.WfCategoryQuery;
import com.tcdng.jacklyn.workflow.entities.WfDoc;
import com.tcdng.jacklyn.workflow.entities.WfDocQuery;
import com.tcdng.jacklyn.workflow.entities.WfMessage;
import com.tcdng.jacklyn.workflow.entities.WfMessageQuery;
import com.tcdng.jacklyn.workflow.entities.WfStep;
import com.tcdng.jacklyn.workflow.entities.WfStepQuery;
import com.tcdng.jacklyn.workflow.entities.WfTemplate;
import com.tcdng.jacklyn.workflow.entities.WfTemplateQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.Document;
import com.tcdng.unify.core.data.PackableDoc;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Workflow business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface WorkflowService extends JacklynBusinessService {

    /**
     * Starts a workflow category publication task.
     * 
     * @param wfCategoryConfigBin
     *            the category configuration binary
     * @param activate
     *            indicates if category should be activated after publication
     * @return the task monitor
     * @throws UnifyException
     *             if an error occurs
     */
    TaskMonitor startWorkflowCategoryPublication(byte[] wfCategoryConfigBin, boolean activate) throws UnifyException;

    /**
     * Starts a workflow category publication task.
     * 
     * @param wfCategoryConfig
     *            the category configuration
     * @param activate
     *            indicates if category should be activated after publication
     * @return the task monitor
     * @throws UnifyException
     *             if an error occurs
     */
    TaskMonitor startWorkflowCategoryPublication(WfCategoryConfig wfCategoryConfig, boolean activate)
            throws UnifyException;

    /**
     * Executes a workflow category publication.
     * 
     * @param taskMonitor
     *            optional task monitor
     * @param wfCategoryConfigBin
     *            binary to publish
     * @param activate
     *            indicates if category should be activated after publication
     * @return a true if value
     * @throws UnifyException
     *             if an error occurs
     */
    boolean executeWorkflowCategoryPublicationTask(TaskMonitor taskMonitor, byte[] wfCategoryConfigBin,
            boolean activate) throws UnifyException;

    /**
     * Executes a workflow category publication.
     * 
     * @param taskMonitor
     *            optional task monitor
     * @param wfCategoryConfig
     *            the category configuration
     * @param activate
     *            indicates if category should be activated after publication
     * @return a true if value
     * @throws UnifyException
     *             if an error occurs
     */
    boolean executeWorkflowCategoryPublicationTask(TaskMonitor taskMonitor, WfCategoryConfig wfCategoryConfig,
            boolean activate) throws UnifyException;

    /**
     * Activates a workflow category.
     * 
     * @param wfCategoryName
     *            the workflow category name
     * @param wfCategoryVersion
     *            the workflow category version
     * @throws UnifyException
     *             if an error occurs
     */
    void activateWfCategory(String wfCategoryName, String wfCategoryVersion) throws UnifyException;

    /**
     * Finds workflow category by ID.
     * 
     * @param wfCategoryId
     *            the workflow category ID
     * @return the workflow category data
     * @throws UnifyException
     *             if workflow category with ID is not found
     */
    WfCategory findWfCategory(Long wfCategoryId) throws UnifyException;

    /**
     * Finds workflow categories by query.
     * 
     * @param query
     *            the workflow category query
     * @return the list of workflow categories found
     * @throws UnifyException
     *             if an error occurs
     */
    List<WfCategory> findWfCategories(WfCategoryQuery query) throws UnifyException;

    /**
     * Updates a workflow category.
     * 
     * @param wfCategory
     *            the workflow category
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateWfCategory(WfCategory wfCategory) throws UnifyException;

    /**
     * Find workflow documents by ID.
     * 
     * @param wfDocId
     *            the workflow document ID
     * @return the workflow document
     * @throws UnifyException
     *             if document with ID is not found. if an error occurs
     */
    WfDoc findWfDoc(Long wfDocId) throws UnifyException;

    /**
     * Finds workflow documents by criteria.
     * 
     * @param query
     *            the the search criteria
     * @return list of workflow documents
     * @throws UnifyException
     *             if an error occurs
     */
    List<WfDoc> findWfDocs(WfDocQuery query) throws UnifyException;

    /**
     * Finds workflow documents by category.
     * 
     * @param wfCategoryId
     *            the category ID
     * @return list of workflow documents
     * @throws UnifyException
     *             if an error occurs
     */
    List<WfDoc> findWfDocs(Long wfCategoryId) throws UnifyException;

    /**
     * Finds workflow messages by criteria.
     * 
     * @param query
     *            the the search criteria
     * @return list of workflow messages
     * @throws UnifyException
     *             if an error occurs
     */
    List<WfMessage> findWfMessages(WfMessageQuery query) throws UnifyException;

    /**
     * Finds workflow messages by category.
     * 
     * @param wfCategoryId
     *            the category ID
     * @return list of workflow messages
     * @throws UnifyException
     *             if an error occurs
     */
    List<WfMessage> findWfMessages(Long wfCategoryId) throws UnifyException;

    /**
     * Finds workflow templates by criteria.
     * 
     * @param query
     *            the the search criteria
     * @return list of workflow templates
     * @throws UnifyException
     *             if an error occurs
     */
    List<WfTemplate> findWfTemplates(WfTemplateQuery query) throws UnifyException;

    /**
     * Finds workflow templates by category.
     * 
     * @param wfCategoryId
     *            the category ID
     * @return list of workflow templates
     * @throws UnifyException
     *             if an error occurs
     */
    List<WfTemplate> findWfTemplates(Long wfCategoryId) throws UnifyException;

    /**
     * Finds manual initiation information based on user role.
     * 
     * @return list of manual initiation information
     * @throws UnifyException
     *             if an error occurs
     */
    List<ManualInitInfo> findUserRoleManualInitInfos() throws UnifyException;

    /**
     * Find workflow template by ID.
     * 
     * @param wfTemplateId
     *            the workflow template ID
     * @return the workflow template
     * @throws UnifyException
     *             if template with ID is not found. if an error occurs
     */
    WfTemplate findWfTemplate(Long wfTemplateId) throws UnifyException;

    /**
     * Find large workflow template data by ID.
     * 
     * @param wfTemplateId
     *            the workflow template ID
     * @return the workflow template
     * @throws UnifyException
     *             if template with ID is not found. if an error occurs
     */
    WfTemplateLargeData findLargeWfTemplate(Long wfTemplateId) throws UnifyException;

    /**
     * Auto detects template for workflow category and document type.
     * 
     * @param categoryName
     *            the workflow category name
     * @param documentType
     *            the document type
     * @return the global template name
     * @throws UnifyException
     *             if an error occurs
     */
    String autoDetectTemplate(String categoryName, Class<? extends Document> documentType) throws UnifyException;

    /**
     * Find workflow steps using query.
     * 
     * @param query
     *            the query to use
     * @return list of workflow steps
     * @throws UnifyException
     *             if an error occurs
     */
    List<WfStep> findSteps(WfStepQuery query) throws UnifyException;

    /**
     * Find workflow step IDs for supplied template ID and step names.
     * 
     * @param wfTemplateId
     *            the template ID
     * @param stepNames
     *            the step names
     * @return step IDs
     * @throws UnifyException
     *             if an error occurs
     */
    List<Long> findStepIds(Long wfTemplateId, Collection<String> stepNames) throws UnifyException;

    /**
     * Gets the workflow form object.
     * 
     * @param docGlobalName
     *            the global document name
     * @return the runtime object
     * @throws UnifyException
     *             if document with global name is unknown. If document definition
     *             has no form
     */
    WfFormDef getRuntimeWfFormDef(String docGlobalName) throws UnifyException;

    /**
     * Gets workflow process definition.
     * 
     * @param processGlobalName
     *            the global process name
     * @return the runtime object
     * @throws UnifyException
     *             if an error occurs
     */
    WfProcessDef getRuntimeWfProcessDef(String processGlobalName) throws UnifyException;

    /**
     * Creates a manual initiation item for the supplied workflow process.
     * 
     * @param processGlobalName
     *            the name of workflow process to use
     * @return a new manual initiation item
     * @throws UnifyException
     *             if template is unknown. If process does not allow manual
     *             initiation. if an error occurs
     */
    ManualWfItem createManualInitItem(String processGlobalName) throws UnifyException;

    /**
     * Pends supplied manual initiation item.
     * 
     * @param manualInitItem
     *            the item to pend
     * @throws UnifyException
     *             if an error occurs
     */
    void pendManualInitItem(ManualWfItem manualInitItem) throws UnifyException;

    /**
     * Submits supplied manual initiation item to workflow.
     * 
     * @param manualInitItem
     *            the item to submit
     * @throws UnifyException
     *             if an error occurs
     */
    void submitManualInitItem(ManualWfItem manualInitItem) throws UnifyException;

    /**
     * Submits a packable document to workflow.
     * 
     * @param processGlobalName
     *            the workflow process name
     * @param branchCode
     *            optional item branch code
     * @param departmentCode
     *            optional item department code
     * @param packableDoc
     *            the packable document to push into workflow
     * @return the workflow item ID
     * @throws UnifyException
     *             if packable document doesn't match template. if template is
     *             unknown. if an error occurs
     */
    Long submitToWorkflow(String processGlobalName, String branchCode, String departmentCode, PackableDoc packableDoc)
            throws UnifyException;

    /**
     * Submits document to workflow.
     * 
     * @param processGlobalName
     *            the workflow global process name
     * @param document
     *            the document to submit
     * @return the workflow item ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long submitToWorkflow(String processGlobalName, Document document) throws UnifyException;

    /**
     * Submits one or more documents to workflow.
     * 
     * @param processGlobalName
     *            the workflow global process name
     * @param documents
     *            the documents to submit
     * @return list of workflow item IDs
     * @throws UnifyException
     *             if an error occurs
     */
    List<Long> submitToWorkflow(String processGlobalName, Document... documents) throws UnifyException;

    /**
     * Grabs work items for current user from specified step. Items grabbed include
     * old grabbed items and unheld items. Total number of items grabbed is limited
     * by maximum hold property of step.
     * 
     * @param stepGlobalName
     *            the global step name
     * @return the list of grabbed work item IDs.
     * @throws UnifyException
     *             if current user is not a participant in step
     */
    List<Long> grabCurrentUserWorkItems(String stepGlobalName) throws UnifyException;

    /**
     * Releases work items for current user from specified step.
     * 
     * @param stepGlobalName
     *            the global step name
     * @param wfItemIds
     *            the workflow item IDs
     * @return the total number of work items released.
     * @throws UnifyException
     *             if current user is not a participant in step if an error occurs
     */
    int releaseCurrentUserWorkItems(String stepGlobalName, List<Long> wfItemIds) throws UnifyException;

    /**
     * Returns the current user work item list for particular step.
     * 
     * @param stepGlobalName
     *            the global step name
     * @return the current session work items
     * @throws UnifyException
     *             if current user is not a participant in step
     */
    InteractWfItems getCurrentUserWorkItems(String stepGlobalName) throws UnifyException;

    /**
     * Returns the current user work item summary.
     * 
     * @return the summary list
     * @throws UnifyException
     *             if an error occurs
     */
    List<WfItemSummary> getCurrentUserWorkItemSummary() throws UnifyException;

    /**
     * Applies workflow action and releases workflow item.
     * 
     * @param workflowItem
     *            the workflow item
     * @param actionName
     *            the action name
     * @throws UnifyException
     *             if item is not held by current user. If action is unknown for
     *             current step.
     */
    void applyWorkflowAction(InteractWfItem workflowItem, String actionName) throws UnifyException;

    /**
     * Finds workflow item.
     * 
     * @param wfItemId
     *            the workflow item ID
     * @return the workflow item
     * @throws UnifyException
     *             if an error occurs
     */
    InteractWfItem findWorkflowItem(Long wfItemId) throws UnifyException;

    /**
     * Finds workflow TrailItem history.
     * 
     * @param wfItemHistId
     *            the worklfow history ID
     * @param commentsOnly
     *            indicates if to fetch only history events that have comments
     * @return the workflow item history
     * @throws UnifyException
     *             if an error occurs
     */
    WfItemHistory findWorkflowItemHistory(Long wfItemHistId, boolean commentsOnly) throws UnifyException;

    /**
     * Attaches item to workflow item.
     * 
     * @param wfItemId
     *            the workflow item ID
     * @param attachment
     *            the item to attach
     * @throws UnifyException
     *             if workflow item does not exist. if name is unknown. if there is
     *             a type mismatch. if an error occurs
     */
    void attachToWorkflowItem(Long wfItemId, WfItemAttachmentInfo attachment) throws UnifyException;

    /**
     * Fetches workflow item attachments.
     * 
     * @param wfItemId
     *            the workflow item ID
     * @param attributesOnly
     *            indicates attributes only and no data
     * @return the list of attachments including blank slots. Blank slots have no
     *         file name and no data.
     * @throws UnifyException
     *             if workflow item does not exist. if an error occurs
     */
    List<WfItemAttachmentInfo> fetchWorkflowItemAttachments(Long wfItemId, boolean attributesOnly)
            throws UnifyException;

    /**
     * Fetches workflow item attachments.
     * 
     * @param wfItemId
     *            the workflow item ID
     * @param name
     *            the attachment name
     * @return the attachment including attributes and data otherwise empty slot
     * @throws UnifyException
     *             if workflow item does not exist. if attachment with name is
     *             unknown if an error occurs
     */
    WfItemAttachmentInfo fetchWorkflowItemAttachment(Long wfItemId, String name) throws UnifyException;

    /**
     * Delete workflow item attachment.
     * 
     * @param wfItemId
     *            the workflow item ID
     * @param name
     *            the attachment name
     * @return the number of items deleted
     * @throws UnifyException
     *             if workflow item does not exist. if attachment with name is
     *             unknown if an error occurs
     */
    int deleteWorkflowItemAttachment(Long wfItemId, String name) throws UnifyException;

    /**
     * Finds all tooling item classifier logic types.
     * 
     * @return list of item classifier logic types
     * @throws UnifyException
     *             if an error occurs
     */
    List<ToolingItemClassifierLogicItem> findToolingItemClassifierLogicTypes() throws UnifyException;

    /**
     * Finds all tooling enrichment logic types.
     * 
     * @return list of enrichment logic types
     * @throws UnifyException
     *             if an error occurs
     */
    List<ToolingEnrichmentLogicItem> findToolingEnrichmentLogicTypes() throws UnifyException;

    /**
     * Finds all tooling policy logic types.
     * 
     * @return list of policy logic types
     * @throws UnifyException
     *             if an error occurs
     */
    List<ToolingPolicyLogicItem> findToolingPolicyLogicTypes() throws UnifyException;

    /**
     * Finds all tooling workflow document UPL generator types.
     * 
     * @return list of workflow document UPL generator types
     * @throws UnifyException
     *             if an error occurs
     */
    List<ToolingWfDocUplGeneratorItem> findToolingWfDocUplGeneratorTypes() throws UnifyException;
}
