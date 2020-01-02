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

package com.tcdng.jacklyn.workflow.business;

import java.util.Collection;

import com.tcdng.jacklyn.notification.data.Message;
import com.tcdng.jacklyn.notification.data.NotificationContact;
import com.tcdng.jacklyn.notification.data.NotificationTemplateDef;
import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.jacklyn.system.constants.SystemModuleSysParamConstants;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleSysParamConstants;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem.Reader;
import com.tcdng.jacklyn.workflow.data.WfAlertDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.StringUtils.StringToken;

/**
 * Default workflow item alert policy implementation.
 * 
 * @author Lateef
 * @since 1.0
 */
@Component(
        name = WorkflowModuleNameConstants.DEFAULTWORKFLOWITEMALERTPOLICY,
        description = "Default Workflow Item Alert Policy")
public class WfItemAlertPolicyImpl extends AbstractWfItemAlertPolicy {

    @Override
    public void sendAlert(Reader flowingWfItemReader, WfAlertDef wfAlertDef) throws UnifyException {
        logDebug("Sending alert...");
        String senderName =
                getSystemService().getSysParameterValue(String.class,
                        SystemModuleSysParamConstants.SYSPARAM_SYSTEM_NAME);
        String senderContact = null;
        String channelName = null;
        Collection<NotificationContact> contactList = null;

        NotificationType channel = wfAlertDef.getChannel();
        switch (channel) {
            case EMAIL:
                senderContact =
                        getSystemService().getSysParameterValue(String.class,
                                SystemModuleSysParamConstants.SYSPARAM_SYSTEM_EMAIL);
                channelName =
                        getSystemService().getSysParameterValue(String.class,
                                WorkflowModuleSysParamConstants.SYSPARAM_WORKFLOW_EMAIL_CHANNEL);
                break;
            case SMS:
                senderContact =
                        getSystemService().getSysParameterValue(String.class,
                                SystemModuleSysParamConstants.SYSPARAM_SYSTEM_SMS_MOBILENO);
                channelName =
                        getSystemService().getSysParameterValue(String.class,
                                WorkflowModuleSysParamConstants.SYSPARAM_WORKFLOW_SMS_CHANNEL);
                break;
            case SYSTEM:
                senderContact = senderName;
                channelName =
                        getSystemService().getSysParameterValue(String.class,
                                WorkflowModuleSysParamConstants.SYSPARAM_WORKFLOW_SYSTEM_CHANNEL);
            default:
                break;
        }

        // Attempt to send only on valid channel name
        if (!StringUtils.isBlank(channelName)) {
            // Get contact list
            if (wfAlertDef.isPassThrough()) {
                // Alert all contacts in step
                contactList =
                        getEligibleContacts(channel, flowingWfItemReader, wfAlertDef.getParticipant(),
                                wfAlertDef.getStepGlobalName());
            } else if (wfAlertDef.isUserInteract()) {
                String heldBy = flowingWfItemReader.getItemHeldBy();
                if (!StringUtils.isBlank(heldBy)) {
                    // Alert specific user
                    contactList = getUserContacts(channel, heldBy);
                } else {
                    // Alert all contacts in step
                    contactList =
                            getEligibleContacts(channel, flowingWfItemReader, wfAlertDef.getParticipant(),
                                    wfAlertDef.getStepGlobalName());
                }
            }

            // Send only if there's at least one contact
            if (!DataUtils.isBlank(contactList)) {
                String templateGlobalName = wfAlertDef.getNotificationTemplateCode();
                NotificationTemplateDef notificationTemplateDef =
                        getNotificationService().getRuntimeNotificationTemplateDef(templateGlobalName);

                // Build message
                Message.Builder msgBuilder =
                        Message.newBuilder(templateGlobalName).fromSender(senderName, senderContact);

                // Populate contacts
                for (NotificationContact contact : contactList) {
                    msgBuilder.toRecipient(contact.getFullName(), contact.getContact());
                }

                // Populate message dictionary from workflow item
                for (StringToken token : notificationTemplateDef.getTokenList()) {
                    if (token.isParam()) {
                        String tokenName = token.getToken();
                        msgBuilder.usingDictionaryEntry(tokenName, flowingWfItemReader.read(tokenName));
                    }
                }

                // Set channel
                msgBuilder.sendVia(channelName);

                // Send notification
                getNotificationService().sendNotification(msgBuilder.build());
            }
        }
    }

}
