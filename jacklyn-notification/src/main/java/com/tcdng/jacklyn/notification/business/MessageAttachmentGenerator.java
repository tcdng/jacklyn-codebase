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

package com.tcdng.jacklyn.notification.business;

import java.util.List;

import com.tcdng.jacklyn.notification.data.MessageDictionary;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.FileAttachment;

/**
 * Message attachment generator. Used to generate attachments on the fly just
 * before sending notification.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface MessageAttachmentGenerator extends UnifyComponent {

    /**
     * Generates file attachments using supplied message dictionary.
     * 
     * @param messageDictionary
     *            the dictionary to use
     * @return list of file attachments
     * @throws UnifyException
     *             if an error occurs
     */
    List<FileAttachment> generateAttachments(MessageDictionary messageDictionary) throws UnifyException;
}
