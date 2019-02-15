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

package com.tcdng.jacklyn.notification.web;

import java.util.Collections;
import java.util.List;

import com.tcdng.jacklyn.notification.business.AbstractMessageAttachmentGenerator;
import com.tcdng.jacklyn.notification.data.MessageDictionary;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Tooling;
import com.tcdng.unify.core.data.FileAttachment;

/**
 * Test message attachment generator.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Tooling("Test Message Attachment Generator")
@Component("test-messageattachmentgenerator")
public class TestMessageAttachmentGenerator extends AbstractMessageAttachmentGenerator {

    @Override
    public List<FileAttachment> generateAttachments(MessageDictionary messageDictionary) throws UnifyException {
        return Collections.emptyList();
    }

}
