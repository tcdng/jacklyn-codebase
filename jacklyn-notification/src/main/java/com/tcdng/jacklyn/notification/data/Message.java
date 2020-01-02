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
package com.tcdng.jacklyn.notification.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Message object.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class Message {

    private String notificationChannelName;

    private String templateGlobalName;

    private String senderName;

    private String senderContact;

    private String reference;

    private List<Recipient> recipients;

    private List<Attachment> attachments;

    private Map<String, Object> dictionary;

    private Message(String notificationChannelName, String templateGlobalName, String senderName, String senderContact,
            String reference, List<Recipient> recipients, List<Attachment> attachments,
            Map<String, Object> dictionary) {
        this.templateGlobalName = templateGlobalName;
        this.notificationChannelName = notificationChannelName;
        this.senderName = senderName;
        this.senderContact = senderContact;
        this.reference = reference;
        this.recipients = recipients;
        this.attachments = attachments;
        this.dictionary = dictionary;
    }

    public String getNotificationChannelName() {
        return notificationChannelName;
    }

    public String getTemplateGlobalName() {
        return templateGlobalName;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderContact() {
        return senderContact;
    }

    public String getReference() {
        return reference;
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public Map<String, Object> getDictionary() {
        return dictionary;
    }

    public boolean isDictionary() {
        return !dictionary.isEmpty();
    }

    public static Builder newBuilder(String templateGlobalName) {
        return new Builder(templateGlobalName);
    }

    public static class Builder {

        private String templateGlobalName;

        private String senderName;

        private String senderContact;

        private String reference;

        private List<Recipient> recipients;

        private List<Attachment> attachments;

        private Map<String, Object> dictionary;

        private String notificationChannelName;

        private Builder(String templateGlobalName) {
            this.templateGlobalName = templateGlobalName;
            recipients = new ArrayList<Recipient>();
        }

        public Builder fromSender(String senderName, String senderContact) {
            this.senderName = senderName;
            this.senderContact = senderContact;
            return this;
        }

        public Builder toRecipient(String name, String contact) {
            recipients.add(new Recipient(name, contact));
            return this;
        }

        public Builder forReference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder usingDictionaryEntry(String entry, Object value) {
            getDictionary().put(entry, value);
            return this;
        }

        public Builder withAttachment(FileAttachmentType type, String fileName, byte[] data) {
            getAttachments().add(new Attachment(type, fileName, data));
            return this;
        }

        public Builder withAttachment(FileAttachmentType type, byte[] data) {
            getAttachments().add(new Attachment(type, data));
            return this;
        }

        public Builder sendVia(String notificationChannelName) {
            this.notificationChannelName = notificationChannelName;
            return this;
        }

        public Message build() {
            return new Message(notificationChannelName, templateGlobalName, senderName, senderContact, reference,
                DataUtils.unmodifiableList(recipients), DataUtils.unmodifiableList(attachments), dictionary);
        }

        private List<Attachment> getAttachments() {
            if (attachments == null) {
                attachments = new ArrayList<Attachment>();
            }

            return attachments;
        }

        private Map<String, Object> getDictionary() {
            if (dictionary == null) {
                dictionary = new HashMap<String, Object>();
            }

            return dictionary;
        }
    }

    public static class Recipient {
        private String name;

        private String contact;

        private Recipient(String name, String contact) {
            this.name = name;
            this.contact = contact;
        }

        public String getName() {
            return name;
        }

        public String getContact() {
            return contact;
        }
    }

    public static class Attachment {

        private FileAttachmentType type;

        private String fileName;

        private byte[] data;

        public Attachment(FileAttachmentType type, String fileName, byte[] data) {
            this.type = type;
            this.fileName = fileName;
            this.data = data;
        }

        public Attachment(FileAttachmentType type, byte[] data) {
            this.type = type;
            this.data = data;
        }

        public FileAttachmentType getType() {
            return type;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getData() {
            return data;
        }
    }
}
