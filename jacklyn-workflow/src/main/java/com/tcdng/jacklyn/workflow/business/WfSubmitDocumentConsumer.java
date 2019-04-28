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

import com.tcdng.jacklyn.workflow.data.WfTemplateDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Tooling;
import com.tcdng.unify.core.business.AbstractTaggedBinaryMessageConsumer;
import com.tcdng.unify.core.data.PackableDoc;
import com.tcdng.unify.core.data.TaggedBinaryMessage;

/**
 * Workflow submit document consumer.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Tooling(description = "Workflow Submit Document Consumer")
@Component("workflow-submitdocumentconsumer")
public class WfSubmitDocumentConsumer extends AbstractTaggedBinaryMessageConsumer {

    @Configurable
    private WorkflowService workflowService;

    @Override
    public void consume(TaggedBinaryMessage binaryMessage) throws UnifyException {
        WfTemplateDef wfTemplateDef = workflowService.getRuntimeWfTemplateDef(binaryMessage.getTag());
        PackableDoc packableDoc =
                PackableDoc.unpack(wfTemplateDef.getWfDocDef().getDocConfig(), binaryMessage.getMessage());
        workflowService.submitToWorkflow(wfTemplateDef.getGlobalName(), packableDoc);
    }

}
