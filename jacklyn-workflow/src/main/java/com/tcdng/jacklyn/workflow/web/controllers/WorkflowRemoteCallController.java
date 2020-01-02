/*
 * Copyright 2018-2020 The Code Department.
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

package com.tcdng.jacklyn.workflow.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.web.controllers.BaseRemoteCallController;
import com.tcdng.jacklyn.shared.workflow.WorkflowRemoteCallNameConstants;
import com.tcdng.jacklyn.shared.workflow.data.GetToolingEnrichmentLogicParams;
import com.tcdng.jacklyn.shared.workflow.data.GetToolingEnrichmentLogicResult;
import com.tcdng.jacklyn.shared.workflow.data.GetToolingItemClassifierLogicParams;
import com.tcdng.jacklyn.shared.workflow.data.GetToolingItemClassifierLogicResult;
import com.tcdng.jacklyn.shared.workflow.data.GetToolingPolicyLogicParams;
import com.tcdng.jacklyn.shared.workflow.data.GetToolingPolicyLogicResult;
import com.tcdng.jacklyn.shared.workflow.data.GetToolingWfDocUplGeneratorParams;
import com.tcdng.jacklyn.shared.workflow.data.GetToolingWfDocUplGeneratorResult;
import com.tcdng.jacklyn.shared.workflow.data.PublishWfCategoryParams;
import com.tcdng.jacklyn.shared.workflow.data.PublishWfCategoryResult;
import com.tcdng.jacklyn.shared.workflow.data.ToolingEnrichmentLogicItem;
import com.tcdng.jacklyn.shared.workflow.data.ToolingItemClassifierLogicItem;
import com.tcdng.jacklyn.shared.workflow.data.ToolingPolicyLogicItem;
import com.tcdng.jacklyn.shared.workflow.data.ToolingWfDocUplGeneratorItem;
import com.tcdng.jacklyn.workflow.business.WorkflowService;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.web.annotation.RemoteAction;

/**
 * Workflow module remote call controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = WorkflowModuleNameConstants.WORKFLOW_MODULE)
@Component("/workflow/remotecall")
public class WorkflowRemoteCallController extends BaseRemoteCallController {

    @Configurable
    private WorkflowService workflowService;

    @RemoteAction(
            name = WorkflowRemoteCallNameConstants.PUBLISH_WORKFLOW_CATEGORY,
            description = "$m{workflow.remotecall.publishwfcategory}")
    public PublishWfCategoryResult publishWfCategory(PublishWfCategoryParams params) throws UnifyException {
        workflowService.executeWorkflowCategoryPublicationTask(null, params.getWfCategoryXml(), params.isActivate());
        return new PublishWfCategoryResult();
    }

    @RemoteAction(
            name = WorkflowRemoteCallNameConstants.GET_TOOLING_ITEMCLASSIFIER_LOGIC_LIST,
            description = "$m{workflow.remotecall.gettoolingclassifierlogic}")
    public GetToolingItemClassifierLogicResult getToolingItemClassifierLogicList(
            GetToolingItemClassifierLogicParams params) throws UnifyException {
        List<ToolingItemClassifierLogicItem> list = workflowService.findToolingItemClassifierLogicTypes();
        return new GetToolingItemClassifierLogicResult(list);
    }

    @RemoteAction(
            name = WorkflowRemoteCallNameConstants.GET_TOOLING_ENRICHMENT_LOGIC_LIST,
            description = "$m{workflow.remotecall.gettoolingenrichmentlogic}")
    public GetToolingEnrichmentLogicResult getToolingEnrichmentLogicList(GetToolingEnrichmentLogicParams params)
            throws UnifyException {
        List<ToolingEnrichmentLogicItem> list = workflowService.findToolingEnrichmentLogicTypes();
        return new GetToolingEnrichmentLogicResult(list);
    }

    @RemoteAction(
            name = WorkflowRemoteCallNameConstants.GET_TOOLING_POLICY_LOGIC_LIST,
            description = "$m{workflow.remotecall.gettoolingpolicylogic}")
    public GetToolingPolicyLogicResult getToolingPolicyLogicList(GetToolingPolicyLogicParams params)
            throws UnifyException {
        List<ToolingPolicyLogicItem> list = workflowService.findToolingPolicyLogicTypes();
        return new GetToolingPolicyLogicResult(list);
    }

    @RemoteAction(
            name = WorkflowRemoteCallNameConstants.GET_TOOLING_WFDOC_UPLGENERATOR_LIST,
            description = "$m{workflow.remotecall.gettoolingwfdocuplgenerator}")
    public GetToolingWfDocUplGeneratorResult getToolingWfDocUplGeneratorList(GetToolingWfDocUplGeneratorParams params)
            throws UnifyException {
        List<ToolingWfDocUplGeneratorItem> list = workflowService.findToolingWfDocUplGeneratorTypes();
        return new GetToolingWfDocUplGeneratorResult(list);
    }
}
