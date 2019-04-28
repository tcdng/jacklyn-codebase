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
package com.tcdng.jacklyn.integration.controllers;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.controllers.BaseRemoteCallController;
import com.tcdng.jacklyn.integration.business.IntegrationService;
import com.tcdng.jacklyn.integration.constants.IntegrationModuleNameConstants;
import com.tcdng.jacklyn.shared.integration.IntegrationRemoteCallNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.business.TaggedXmlMessageConsumer;
import com.tcdng.unify.core.data.TaggedXmlMessage;
import com.tcdng.unify.web.annotation.GatewayAction;
import com.tcdng.unify.web.data.TaggedXmlMessageParams;
import com.tcdng.unify.web.data.TaggedXmlMessageResult;

/**
 * Integration module remote call controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = IntegrationModuleNameConstants.INTEGRATION_MODULE)
@Component("/integration/gate")
public class IntegrationRemoteGateController extends BaseRemoteCallController {

    @Configurable
    private IntegrationService integrationService;
    
    @GatewayAction(
            name = IntegrationRemoteCallNameConstants.PUSH_TAGGEDXMLMESSAGE,
            description = "$m{integration.gate.remotecall.pushtaggedxmlmessage}")
    public TaggedXmlMessageResult pushTaggedXmlMessage(TaggedXmlMessageParams params) throws UnifyException {
        TaggedXmlMessage xmlMessage = params.getTaggedMessage();
        TaggedXmlMessageConsumer taggedMessageConsumer =
                (TaggedXmlMessageConsumer) getComponent(xmlMessage.getConsumer());
        taggedMessageConsumer.consume(xmlMessage);
        return TaggedXmlMessageResult.SUCCESS;
    }

}
