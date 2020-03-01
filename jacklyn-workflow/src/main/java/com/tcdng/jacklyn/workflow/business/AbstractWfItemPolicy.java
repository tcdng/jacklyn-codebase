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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.tcdng.jacklyn.notification.data.NotificationContact;
import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem.Reader;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for workflow item policies.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractWfItemPolicy extends AbstractUnifyComponent implements WfItemPolicy {

    @Configurable
    private WfStepUserInformationProvider wfStepUserInformationProvider;

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected WfStepUserInformationProvider getWfStepUserInfoProvider() {
        return wfStepUserInformationProvider;
    }

    protected Collection<String> getEligibleEscalationRecipients(Reader flowingWfItemReader) throws UnifyException {
        FlowingWfItem.Restrictions restrictions = flowingWfItemReader.getRestrictions();
        return wfStepUserInformationProvider.getEligibleUsersForEscalation(flowingWfItemReader.getStepGlobalName(),
                restrictions.getBranchCode(), restrictions.getDepartmentCode());
    }

    protected Collection<NotificationContact> getEligibleEscalationContacts(NotificationType type,
            Reader flowingWfItemReader, String globalStepName) throws UnifyException {
        if (type != null) {
            FlowingWfItem.Restrictions restrictions = flowingWfItemReader.getRestrictions();
            switch (type) {
                case EMAIL:
                    return wfStepUserInformationProvider.getEligibleEmailContactsForEscalation(globalStepName,
                            restrictions.getBranchCode(), restrictions.getDepartmentCode());
                case SMS:
                    break;
                case SYSTEM:
                    Collection<String> elligibleUsers = getEligibleEscalationRecipients(flowingWfItemReader);
                    if (!DataUtils.isBlank(elligibleUsers)) {
                        List<NotificationContact> contacts = new ArrayList<NotificationContact>();
                        for (String userLoginId : elligibleUsers) {
                            contacts.add(new NotificationContact(userLoginId, userLoginId));
                        }

                        return contacts;
                    }
                default:
                    break;
            }
        }

        return Collections.emptyList();
    }

    protected Collection<String> getEligibleParticipants(Reader flowingWfItemReader) throws UnifyException {
        FlowingWfItem.Restrictions restrictions = flowingWfItemReader.getRestrictions();
        return wfStepUserInformationProvider.getEligibleUsersForParticipation(flowingWfItemReader.getStepParticipant(),
                flowingWfItemReader.getStepGlobalName(), restrictions.getBranchCode(),
                restrictions.getDepartmentCode());
    }

    protected Collection<NotificationContact> getEligibleParticipationContacts(NotificationType type,
            Reader flowingWfItemReader, WorkflowParticipantType participant, String globalStepName)
            throws UnifyException {
        if (type != null) {
            FlowingWfItem.Restrictions restrictions = flowingWfItemReader.getRestrictions();
            switch (type) {
                case EMAIL:
                    return wfStepUserInformationProvider.getEligibleEmailContactsForParticipation(participant,
                            globalStepName, restrictions.getBranchCode(), restrictions.getDepartmentCode());
                case SMS:
                    return wfStepUserInformationProvider.getEligibleMobilePhoneContactsForParticipation(participant,
                            globalStepName, restrictions.getBranchCode(), restrictions.getDepartmentCode());
                case SYSTEM:
                    Collection<String> elligibleUsers = getEligibleParticipants(flowingWfItemReader);
                    if (!DataUtils.isBlank(elligibleUsers)) {
                        List<NotificationContact> contacts = new ArrayList<NotificationContact>();
                        for (String userLoginId : elligibleUsers) {
                            contacts.add(new NotificationContact(userLoginId, userLoginId));
                        }

                        return contacts;
                    }
                default:
                    break;
            }
        }

        return Collections.emptyList();
    }

    protected Collection<NotificationContact> getUserContacts(NotificationType type, String userLoginId)
            throws UnifyException {
        if (type != null) {
            switch (type) {
                case EMAIL:
                    return wfStepUserInformationProvider.getEmailContactsForUser(userLoginId);
                case SMS:
                    return wfStepUserInformationProvider.getMobilePhoneContactsForUser(userLoginId);
                case SYSTEM:
                    return Arrays.asList(new NotificationContact(userLoginId, userLoginId));
                default:
                    break;

            }
        }

        return Collections.emptyList();
    }
}
