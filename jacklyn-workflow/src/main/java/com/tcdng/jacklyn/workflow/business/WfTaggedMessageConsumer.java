/*
 * Copyright 2018-2019 The Code Department
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

import com.tcdng.jacklyn.workflow.data.WfTaggedMappingDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Tooling;
import com.tcdng.unify.core.business.AbstractTaggedMessageConsumer;
import com.tcdng.unify.core.data.PackableDoc;
import com.tcdng.unify.core.data.TaggedMessage;

/**
 * Workflow tagged message consumer.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Tooling(description = "Workflow Tagged Message Consumer")
@Component("workflow-taggedmessageconsumer")
public class WfTaggedMessageConsumer extends AbstractTaggedMessageConsumer {

    @Configurable
    private WorkflowService workflowService;

    @Override
    public void consume(TaggedMessage taggedMessage) throws UnifyException {
        WfTaggedMappingDef wfTaggedMappingDef = workflowService.getRuntimeWfTaggedMappingDef(taggedMessage.getTag());
        PackableDoc packableDoc =
                PackableDoc.unpack(wfTaggedMappingDef.getWfDocDef().getDocConfig(), taggedMessage.getMessage());
        workflowService.submitToWorkflow(wfTaggedMappingDef.getWfTemplateDef().getGlobalName(), packableDoc);
    }

}
