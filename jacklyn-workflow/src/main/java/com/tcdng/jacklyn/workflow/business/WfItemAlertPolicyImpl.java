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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    private static final String HELDBY_TOKEN = ":heldBy";

    private static final String FORWARDED_TOKEN = ":forwardedBy";

    private static final String WFITEMDESC_TOKEN = ":itemDesc";

    private static final String COMMENT_TOKEN = ":comment";

    private static final Set<String> RESERVED_WFITEM_TOKENS =
            Collections.unmodifiableSet(
                    new HashSet<String>(Arrays.asList(HELDBY_TOKEN, FORWARDED_TOKEN, WFITEMDESC_TOKEN, COMMENT_TOKEN)));

    @Override
    public void sendAlert(Reader flowingWfItemReader, WfAlertDef wfAlertDef) throws UnifyException {
        logDebug("Sending alert of type [{0}] through channel [{1}] for workflow item [{2}]...", wfAlertDef.getType(),
                wfAlertDef.getChannel(), flowingWfItemReader.getItemDesc());
        if (wfAlertDef.isWithFilter()) {
        	WfItemAlertFilter WfItemAlertFilter = (WfItemAlertFilter) getComponent(wfAlertDef.getFilter());
        	if(!WfItemAlertFilter.acceptAlert(flowingWfItemReader, wfAlertDef)) {
                logDebug("Alert discarded by filter [{0}]...", wfAlertDef.getFilter());
                return;
        	}
        }

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
        logDebug("Using specific channel with name [{0}]...", channelName);
        if (!StringUtils.isBlank(channelName)) {
            // Get contact list
            switch (wfAlertDef.getType()) {
                case CRITICAL_NOTIFICATION:
                case EXPIRATION_NOTIFICATION:
                    contactList =
                            getEligibleEscalationContacts(channel, flowingWfItemReader, wfAlertDef.getStepGlobalName());
                    break;
                case PASS_THROUGH:
                    contactList =
                            getEligibleParticipationContacts(channel, flowingWfItemReader, wfAlertDef.getParticipant(),
                                    wfAlertDef.getStepGlobalName());
                    break;
                case USER_INTERACT:
                    String heldBy = flowingWfItemReader.getItemHeldBy();
                    if (!StringUtils.isBlank(heldBy)) {
                        // Alert specific user
                        contactList = getUserContacts(channel, heldBy);
                    } else {
                        // Alert all contacts in step
                        contactList =
                                getEligibleParticipationContacts(channel, flowingWfItemReader,
                                        wfAlertDef.getParticipant(), wfAlertDef.getStepGlobalName());
                    }
                    break;
                default:
                    break;
            }

            // Send only if there's at least one contact
            if (!DataUtils.isBlank(contactList)) {
                logDebug("Targeting [{0}] contacts through channel...", contactList.size());
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
                        if (RESERVED_WFITEM_TOKENS.contains(tokenName)) {
                            if (HELDBY_TOKEN.equals(tokenName)) {
                                msgBuilder.usingDictionaryEntry(tokenName, flowingWfItemReader.getItemHeldBy());
                            } else if (FORWARDED_TOKEN.equals(tokenName)) {
                                msgBuilder.usingDictionaryEntry(tokenName, flowingWfItemReader.getItemForwardedBy());
                            } else if (COMMENT_TOKEN.equals(tokenName)) {
                                msgBuilder.usingDictionaryEntry(tokenName, flowingWfItemReader.getLastComment());
                            } else if (WFITEMDESC_TOKEN.equals(tokenName)) {
                                msgBuilder.usingDictionaryEntry(tokenName, flowingWfItemReader.getItemDesc());
                            }
                        } else {
                            msgBuilder.usingDictionaryEntry(tokenName, flowingWfItemReader.read(tokenName));
                        }
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
