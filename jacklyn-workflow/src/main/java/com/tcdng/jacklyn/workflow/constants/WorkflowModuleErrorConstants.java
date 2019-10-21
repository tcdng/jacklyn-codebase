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
package com.tcdng.jacklyn.workflow.constants;

/**
 * Workflow module errors.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface WorkflowModuleErrorConstants {

    /**
     * Workflow template with name {0} is unknown.
     */
    String WORKFLOW_TEMPLATE_WITH_NAME_UNKNOWN = "WORKFLOW_0001";

    /**
     * Workflow {0} does not have associated document of type {1}.
     */
    String WORKFLOW_NO_ASSOCIATED_DOCUMENT = "WORKFLOW_0002";

    /**
     * Workflow {0} with step {1} is unknown.
     */
    String WORKFLOW_STEP_WITH_NAME_UNKNOWN = "WORKFLOW_0003";

    /**
     * Workflow items can not be routed to start step. Workflow = {0}, step = {1}.
     */
    String WORKFLOW_ITEM_NOT_ROUTE_START = "WORKFLOW_0004";

    /**
     * Workflow step {0} action {1} is unknown.
     */
    String WORKFLOW_STEP_ACTION_WITH_NAME_UNKNOWN = "WORKFLOW_0005";

    /**
     * Workflow item with ID {0} does not exist.
     */
    String WORKFLOW_ITEM_NOT_EXIST = "WORKFLOW_0006";

    /**
     * Workflow item {0} is already held by {1}.
     */
    String WORKFLOW_ITEM_ALREADY_HELD = "WORKFLOW_0007";

    /**
     * Attempt to apply action {0} on unheld workflow item {1}.
     */
    String WORKFLOW_APPLY_ACTION_UNHELD = "WORKFLOW_0008";

    /**
     * Classifier with name {0} referencing unknown field {1}.
     */
    String WORKFLOW_CLASSIFIER_UNKNOWN_FIELD = "WORKFLOW_0009";

    /**
     * Workflow with name {0} referencing unknown document {1}.
     */
    String WORKFLOW_UNKNOWN_DOCUMENT = "WORKFLOW_000A";

    /**
     * Workflow document {0} entry bean mapping for type {1} is unknown.
     */
    String WORKFLOW_DOCUMENT_ENTRY_BEANMAPPING_FOR_TYPE_UNKNOWN = "WORKFLOW_000B";

    /**
     * Current {0} is not a participant on step {1}.
     */
    String WORKFLOW_CURRENT_USER_NOT_PARTICIPANT = "WORKFLOW_000C";

    /**
     * Attempting to submit workflow document to non-start and non-receptacle step
     * {0}.
     */
    String WORKFLOW_SUBMIT_NONSTART_NONRECEPTACLE = "WORKFLOW_000D";

    /**
     * Workflow item definition {0} attachment {1} is unknown.
     */
    String WORKFLOW_ATTACHMENT_WITH_NAME_UNKNOWN = "WORKFLOW_000E";

    /**
     * Workflow item definition {0} attachment incompatible types. Defined = {1},
     * supplied = {2}.
     */
    String WORKFLOW_ATTACHMENT_INCOMPATIPLE_TYPE = "WORKFLOW_000F";

    /**
     * Workflow category {0} publication error.
     */
    String WORKFLOW_CATEGORY_PUBLICATION_ERROR = "WORKFLOW_0010";

    /**
     * Workflow template {0} has no manual initiation step.
     */
    String WORKFLOW_TEMPLATE_NO_MANUAL_INIT = "WORKFLOW_0011";

    /**
     * Workflow document with name {0} is unknown.
     */
    String WORKFLOW_DOCUMENT_WITH_NAME_UNKNOWN = "WORKFLOW_0012";

    /**
     * Workflow document with name {0} has no form.
     */
    String WORKFLOW_DOCUMENT_NO_FORM = "WORKFLOW_0013";

    /**
     * Workflow category with name {0} and version {1} is unknown.
     */
    String WORKFLOW_CATEGORY_NAME_VERSION_UNKNOWN = "WORKFLOW_0014";

    /**
     * Workflow tagged mapping with tag name {0} is unknown.
     */
    String WORKFLOW_TAGGEDMAPPING_WITH_TAGNAME_UNKNOWN = "WORKFLOW_0015";

    /**
     * Workflow template {0} has no manual initiation step for document {1}.
     */
    String WORKFLOW_TEMPLATE_NO_DOC_MANUAL_INIT = "WORKFLOW_0016";

    /**
     * Workflow {0} with document {1} is unknown.
     */
    String WORKFLOW_TEMPLATE_DOCUMENT_WITH_NAME_UNKNOWN = "WORKFLOW_0017";

    /**
     * Workflow merge step {0} does not resolve to a route.
     */
    String WORKFLOW_MERGE_DOES_NOT_RESOLVE_TO_ROUTE = "WORKFLOW_0018";
}
