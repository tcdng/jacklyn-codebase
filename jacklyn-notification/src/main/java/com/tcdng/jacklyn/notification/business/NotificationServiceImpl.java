/*
 * Copyright 2018 The Code Department
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
package com.tcdng.jacklyn.notification.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.notification.constants.NotificationModuleErrorConstants;
import com.tcdng.jacklyn.notification.constants.NotificationModuleNameConstants;
import com.tcdng.jacklyn.notification.constants.NotificationModuleSysParamConstants;
import com.tcdng.jacklyn.notification.data.Message;
import com.tcdng.jacklyn.notification.data.MessageDictionary;
import com.tcdng.jacklyn.notification.data.MessageTemplateDef;
import com.tcdng.jacklyn.notification.data.MessagingChannelDef;
import com.tcdng.jacklyn.notification.entities.Notification;
import com.tcdng.jacklyn.notification.entities.NotificationAttachment;
import com.tcdng.jacklyn.notification.entities.NotificationAttachmentQuery;
import com.tcdng.jacklyn.notification.entities.NotificationChannel;
import com.tcdng.jacklyn.notification.entities.NotificationChannelQuery;
import com.tcdng.jacklyn.notification.entities.NotificationQuery;
import com.tcdng.jacklyn.notification.entities.NotificationRecipient;
import com.tcdng.jacklyn.notification.entities.NotificationRecipientQuery;
import com.tcdng.jacklyn.notification.entities.NotificationTemplate;
import com.tcdng.jacklyn.notification.entities.NotificationTemplateQuery;
import com.tcdng.jacklyn.notification.utils.NotificationUtils;
import com.tcdng.jacklyn.notification.utils.NotificationUtils.TemplateNameParts;
import com.tcdng.jacklyn.shared.notification.NotificationStatus;
import com.tcdng.jacklyn.shared.notification.data.ToolingAttachmentGenItem;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.jacklyn.shared.xml.config.module.NotificationTemplateConfig;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.system.entities.Authentication;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Periodic;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.FrequencyUnit;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.FileAttachment;
import com.tcdng.unify.core.operation.Update;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default notification business service implementation.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(NotificationModuleNameConstants.NOTIFICATIONSERVICE)
public class NotificationServiceImpl extends AbstractJacklynBusinessService implements NotificationService {

    @Configurable
    private SystemService systemMService;

    @Configurable(NotificationModuleNameConstants.EMAILMESSAGINGCHANNEL)
    private MessagingChannel emailNotificationChannel;

    @Configurable(NotificationModuleNameConstants.SYSTEMMESSAGINGCHANNEL)
    private MessagingChannel systemNotificationChannel;

    private FactoryMap<String, MessageTemplateDef> templates;

    private FactoryMap<String, MessagingChannelDef> channels;

    public NotificationServiceImpl() {
        templates = new FactoryMap<String, MessageTemplateDef>(true) {

            @Override
            protected boolean stale(String globalTemplateName, MessageTemplateDef notificationTemplateDef)
                    throws Exception {
                boolean stale = false;
                try {
                    TemplateNameParts templateNames = NotificationUtils.getTemplateNameParts(globalTemplateName);
                    long versionNo = db().value(long.class, "versionNo", new NotificationTemplateQuery()
                            .moduleName(templateNames.getModuleName()).name(templateNames.getTemplateName()));
                    stale = versionNo != notificationTemplateDef.getVersionNo();
                } catch (Exception e) {
                    logError(e);
                }

                return stale;
            }

            @Override
            protected MessageTemplateDef create(String globalTemplateName, Object... params) throws Exception {
                TemplateNameParts templateNames = NotificationUtils.getTemplateNameParts(globalTemplateName);
                NotificationTemplate notificationTemplate = db().list(new NotificationTemplateQuery()
                        .moduleName(templateNames.getModuleName()).name(templateNames.getTemplateName()));
                if (notificationTemplate == null) {
                    throw new UnifyException(NotificationModuleErrorConstants.MESSAGE_TEMPLATE_WITH_NAME_UNKNOWN,
                            globalTemplateName);
                }

                return new MessageTemplateDef(notificationTemplate.getId(),
                        resolveApplicationMessage(notificationTemplate.getSubject()),
                        StringUtils.breakdownParameterizedString(notificationTemplate.getTemplate()),
                        notificationTemplate.getHtmlFlag(), notificationTemplate.getVersionNo());
            }

        };

        channels = new FactoryMap<String, MessagingChannelDef>(true) {

            @Override
            protected boolean stale(String name, MessagingChannelDef notificationChannelDef) throws Exception {
                boolean stale = false;
                try {
                    long versionNo = db().value(long.class, "versionNo", new NotificationChannelQuery().name(name));
                    stale = versionNo != notificationChannelDef.getVersionNo();
                } catch (Exception e) {
                    logError(e);
                }

                return stale;
            }

            @Override
            protected MessagingChannelDef create(String name, Object... params) throws Exception {
                NotificationChannel notificationChannel = db().list(new NotificationChannelQuery().name(name));
                if (notificationChannel == null) {
                    throw new UnifyException(NotificationModuleErrorConstants.MESSAGE_CHANNEL_WITH_NAME_UNKNOWN, name);
                }

                String userName = null;
                String password = null;
                if (notificationChannel.getAuthenticationId() != null) {
                    Authentication authentication = systemMService
                            .findAuthentication(notificationChannel.getAuthenticationId()).getData();
                    userName = authentication.getUserName();
                    password = authentication.getPassword();
                }

                return new MessagingChannelDef(notificationChannel.getId(), notificationChannel.getName(),
                        notificationChannel.getNotificationType(), notificationChannel.getHostAddress(),
                        notificationChannel.getHostPort(), notificationChannel.getSecurityType(), userName, password,
                        notificationChannel.getVersionNo());
            }

        };
    }

    @Override
    public Long createNotificationTemplate(NotificationTemplate notificationTemplate) throws UnifyException {
        return (Long) db().create(notificationTemplate);
    }

    @Override
    public NotificationTemplate findNotificationTemplate(Long notificationTemplateId) throws UnifyException {
        return db().find(NotificationTemplate.class, notificationTemplateId);
    }

    @Override
    public NotificationTemplate findNotificationTemplate(String moduleName, String name) throws UnifyException {
        return db().list(new NotificationTemplateQuery().moduleName(moduleName).name(name));
    }

    @Override
    public List<NotificationTemplate> findNotificationTemplates(NotificationTemplateQuery query) throws UnifyException {
        return db().listAll(query.select("id", "name", "description", "subject", "htmlFlag", "moduleId", "moduleName",
                "moduleDescription", "status", "statusDesc"));
    }

    @Override
    public int updateNotificationTemplate(NotificationTemplate notificationTemplate) throws UnifyException {
        return db().updateByIdVersion(notificationTemplate);
    }

    @Override
    public int deleteNotificationTemplate(Long id) throws UnifyException {
        return db().delete(NotificationTemplate.class, id);
    }

    @Override
    public Long createNotificationChannel(NotificationChannel notificationChannel) throws UnifyException {
        return (Long) db().create(notificationChannel);
    }

    @Override
    public NotificationChannel findNotificationChannel(Long notificationChannelId) throws UnifyException {
        return db().find(NotificationChannel.class, notificationChannelId);
    }

    @Override
    public List<NotificationChannel> findNotificationChannels(NotificationChannelQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateNotificationChannel(NotificationChannel notificationChannel) throws UnifyException {
        return db().updateByIdVersion(notificationChannel);
    }

    @Override
    public int deleteNotificationChannel(Long id) throws UnifyException {
        return db().delete(NotificationChannel.class, id);
    }

    @Override
    public void sendNotification(Message message) throws UnifyException {
        // Put notification in communication system
        Notification notification = new Notification();
        Long notificationTemplateId = templates.get(message.getGlobalTemplateName()).getNotificationTemplateId();
        notification.setNotificationTemplateId(notificationTemplateId);

        Long notificationChannelId = channels.get(message.getNotificationChannelName()).getNotificationChannelId();
        notification.setNotificationChannelId(notificationChannelId);

        byte[] dictionary = IOUtils.streamToBytes(message.getDictionary());
        notification.setSenderName(message.getSenderName());
        notification.setSenderContact(message.getSenderContact());
        notification.setDictionary(dictionary);
        notification.setDueDt(new Date());
        notification.setAttempts(Integer.valueOf(0));
        notification.setStatus(NotificationStatus.NOT_SENT);
        Long notificationId = (Long) db().create(notification);

        NotificationRecipient notifRecipient = new NotificationRecipient();
        notifRecipient.setNotificationId(notificationId);
        for (com.tcdng.jacklyn.notification.data.Message.Recipient recipient : message.getRecipients()) {
            notifRecipient.setRecipientName(recipient.getName());
            notifRecipient.setRecipientContact(recipient.getContact());
            db().create(notifRecipient);
        }

        if (!message.getAttachments().isEmpty()) {
            NotificationAttachment notifAttachment = new NotificationAttachment();
            notifAttachment.setNotificationId(notificationId);
            for (com.tcdng.jacklyn.notification.data.Message.Attachment attachment : message.getAttachments()) {
                notifAttachment.setType(attachment.getType());
                notifAttachment.setFileName(attachment.getFileName());
                notifAttachment.setData(attachment.getData());
                db().create(notifAttachment);
            }
        }
    }

    @Override
    public List<Notification> findNotifications(NotificationQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public Notification findNotification(Long notificationId) throws UnifyException {
        return db().list(Notification.class, notificationId);
    }

    @Override
    public int setNotificationStatus(List<Long> notificationIds, NotificationStatus status) throws UnifyException {
        return db().updateAll(new NotificationQuery().idIn(notificationIds), new Update().add("status", status));
    }

    @Override
    public List<ToolingAttachmentGenItem> findToolingAttachmentGenTypes() throws UnifyException {
        return getToolingTypes(ToolingAttachmentGenItem.class, MessageAttachmentGenerator.class);
    }

    @Periodic(PeriodicType.SLOWER)
    public void sendNotifications(TaskMonitor taskMonitor) throws UnifyException {
        if (grabClusterMasterLock()) {
            if (systemMService.getSysParameterValue(boolean.class,
                    NotificationModuleSysParamConstants.NOTIFICATION_ENABLED)) {
                int maxBatchSize = systemMService.getSysParameterValue(int.class,
                        NotificationModuleSysParamConstants.NOTIFICATION_MAX_BATCH_SIZE);
                int maxAttempts = systemMService.getSysParameterValue(int.class,
                        NotificationModuleSysParamConstants.NOTIFICATION_MAXIMUM_TRIES);
                int retryMinutes = systemMService.getSysParameterValue(int.class,
                        NotificationModuleSysParamConstants.NOTIFICATION_RETRY_MINUTES);

                List<Notification> notificationList = db().listAll(new NotificationQuery().due()
                        .status(NotificationStatus.NOT_SENT).orderById().limit(maxBatchSize));
                for (Notification notification : notificationList) {
                    int attempts = notification.getAttempts() + 1;
                    notification.setAttempts(attempts);
                    MessageDictionary messageDictionary = IOUtils.streamFromBytes(MessageDictionary.class,
                            notification.getDictionary());
                    if (sendNotification(notification, messageDictionary)) {
                        // Update to SENT status
                        notification.setSentDt(new Date());
                        notification.setStatus(NotificationStatus.SENT);
                    } else {
                        if (attempts >= maxAttempts) {
                            notification.setStatus(NotificationStatus.ABORTED);
                        } else {
                            // Shift and update due date by retry minutes
                            Date dueDt = CalendarUtils.getNowWithFrequencyOffset(FrequencyUnit.MINUTE, retryMinutes);
                            notification.setDueDt(dueDt);
                        }
                    }

                    db().updateById(notification);
                    commit();
                }
            }
        }
    }

    @Override
    public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {
        // Install new and update old
        NotificationTemplate notificationTemplate = new NotificationTemplate();
        for (ModuleConfig moduleConfig : moduleConfigList) {
            Long moduleId = systemMService.getModuleId(moduleConfig.getName());
            if (moduleConfig.getNotificationTemplates() != null) {
                logDebug("Installing message type definitions for module [{0}]...",
                        resolveApplicationMessage(moduleConfig.getDescription()));
                NotificationTemplateQuery mtQuery = new NotificationTemplateQuery();
                for (NotificationTemplateConfig notificationTemplateConfig : moduleConfig.getNotificationTemplates()
                        .getNotificationTemplateList()) {
                    mtQuery.clear();
                    NotificationTemplate oldNotificationTemplate = db()
                            .find(mtQuery.moduleId(moduleId).name(notificationTemplateConfig.getName()));
                    String description = resolveApplicationMessage(notificationTemplateConfig.getDescription());
                    if (oldNotificationTemplate == null) {
                        notificationTemplate.setModuleId(moduleId);
                        notificationTemplate.setName(notificationTemplateConfig.getName());
                        notificationTemplate.setDescription(description);
                        notificationTemplate.setSubject(notificationTemplateConfig.getSubject());
                        notificationTemplate.setTemplate(notificationTemplateConfig.getTemplate());
                        notificationTemplate.setHtmlFlag(notificationTemplateConfig.isHtml());
                        db().create(notificationTemplate);
                    } else {
                        oldNotificationTemplate.setDescription(description);
                        oldNotificationTemplate.setSubject(notificationTemplateConfig.getSubject());
                        oldNotificationTemplate.setTemplate(notificationTemplateConfig.getTemplate());
                        oldNotificationTemplate.setHtmlFlag(notificationTemplateConfig.isHtml());
                        db().updateByIdVersion(oldNotificationTemplate);
                    }
                }
            }
        }
    }

    @Override
    public void clearCache() throws UnifyException {
        templates.clear();
        channels.clear();
    }

    private boolean sendNotification(Notification notification, MessageDictionary messageDictionary) {
        try {
            MessageTemplateDef notificationTemplateDef = templates.get(NotificationUtils
                    .getGlobalTemplateName(notification.getModuleName(), notification.getNotificationTemplateName()));
            MessagingChannelDef notificationChannelDef = channels.get(notification.getNotificationChannelName());
            MessagingChannel notificationChannel = null;
            switch (notificationChannelDef.getNotificationType()) {
            case SYSTEM:
                notificationChannel = systemNotificationChannel;
                break;
            case SMS:
                // TODO
                break;
            case EMAIL:
            default:
                notificationChannel = emailNotificationChannel;
                break;
            }

            String subject = resolveApplicationMessage(notification.getSubject());
            String senderContact = resolveApplicationMessage(notification.getSenderContact());
            messageDictionary.addEntry("subject", subject);
            messageDictionary.addEntry("senderContact", senderContact);
            messageDictionary.addEntry("senderName", resolveApplicationMessage(notification.getSenderName()));
            String messageBody = StringUtils.buildParameterizedString(notificationTemplateDef.getTokenList(),
                    messageDictionary.getDictionary());

            // Recipients
            List<String> recipientContactList = db().valueList(String.class, "recipientContact",
                    new NotificationRecipientQuery().notificationId(notification.getId()));

            // Attachments. Combine generated and database attachments.
            List<FileAttachment> fileAttachmentList = Collections.emptyList();
            boolean isAttachment = false;
            String attachmentGenerator = notification.getAttachmentGenerator();
            if (!StringUtils.isBlank(attachmentGenerator)) {
                // Generated attachments
                fileAttachmentList = new ArrayList<FileAttachment>();
                MessageAttachmentGenerator notificationAttachmentGenerator = (MessageAttachmentGenerator) getComponent(
                        attachmentGenerator);
                fileAttachmentList.addAll(notificationAttachmentGenerator.generateAttachments(messageDictionary));
                isAttachment = true;
            }

            // Database attachments
            List<NotificationAttachment> attachmentList = db()
                    .listAll(new NotificationAttachmentQuery().notificationId(notification.getId()));
            if (!attachmentList.isEmpty()) {
                if (!isAttachment) {
                    fileAttachmentList = new ArrayList<FileAttachment>();
                }

                for (NotificationAttachment notificationAttachment : attachmentList) {
                    fileAttachmentList.add(new FileAttachment(notificationAttachment.getType(),
                            notificationAttachment.getFileName(), notificationAttachment.getData()));
                }
            }

            // Send
            return notificationChannel.sendMessage(notificationChannelDef, subject, senderContact, recipientContactList,
                    messageBody, notificationTemplateDef.isHtml(), fileAttachmentList);
        } catch (UnifyException e) {
            logError(e);
        }
        return false;
    }
}
