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
package com.tcdng.jacklyn.notification.entities;

import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntityQuery;

/**
 * Message template query class;
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class NotificationTemplateQuery extends BaseVersionedStatusEntityQuery<NotificationTemplate> {

    public NotificationTemplateQuery() {
        super(NotificationTemplate.class);
    }

    public NotificationTemplateQuery moduleId(Long moduleId) {
        return (NotificationTemplateQuery) equals("moduleId", moduleId);
    }

    public NotificationTemplateQuery moduleName(String moduleName) {
        return (NotificationTemplateQuery) equals("moduleName", moduleName);
    }

    public NotificationTemplateQuery name(String name) {
        return (NotificationTemplateQuery) equals("name", name);
    }

    public NotificationTemplateQuery nameLike(String name) {
        return (NotificationTemplateQuery) like("name", name);
    }

    public NotificationTemplateQuery descriptionLike(String description) {
        return (NotificationTemplateQuery) like("description", description);
    }
}
