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

package com.tcdng.jacklyn.notification.data;

import java.util.List;

import com.tcdng.unify.core.util.StringUtils.StringToken;

/**
 * Message template definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class MessageTemplateDef {

    private Long notificationTemplateId;

    private String subject;

    private String link;

    private List<StringToken> tokenList;

    private boolean html;

    private long versionNo;

    public MessageTemplateDef(Long notificationTemplateId, String subject, String link, List<StringToken> tokenList,
            boolean html, long versionNo) {
        this.notificationTemplateId = notificationTemplateId;
        this.subject = subject;
        this.link = link;
        this.tokenList = tokenList;
        this.html = html;
        this.versionNo = versionNo;
    }

    public Long getNotificationTemplateId() {
        return notificationTemplateId;
    }

    public String getSubject() {
        return subject;
    }

    public String getLink() {
        return link;
    }

    public List<StringToken> getTokenList() {
        return tokenList;
    }

    public boolean isHtml() {
        return html;
    }

    public long getVersionNo() {
        return versionNo;
    }
}
