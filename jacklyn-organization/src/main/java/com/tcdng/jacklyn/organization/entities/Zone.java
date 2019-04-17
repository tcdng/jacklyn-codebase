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
package com.tcdng.jacklyn.organization.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.organization.constants.OrganizationModuleNameConstants;
import com.tcdng.jacklyn.system.entities.SupportedLocale;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Represents zone entity.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Managed(
        module = OrganizationModuleNameConstants.ORGANIZATION_MODULE, title = "Zone", reportable = true,
        auditable = true)
@Table(name = "JKZONE", uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }) })
public class Zone extends BaseVersionedStatusEntity {

    @ForeignKey(type = SupportedLocale.class, nullable = true)
    private Long supportedLocaleId;

    @Column(name = "ZONE_NM", length = 32)
    private String name;

    @Column(name = "ZONE_DESC", length = 64)
    private String description;

    @Column(name = "ZONE_TIMEZONE", length = 64, nullable = true)
    private String timeZone;

    @ListOnly(key = "supportedLocaleId", property = "description")
    private String supportedLocaleDesc;

    @ListOnly(key = "supportedLocaleId", property = "languageTag")
    private String languageTag;

    @Override
    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSupportedLocaleId() {
        return supportedLocaleId;
    }

    public void setSupportedLocaleId(Long supportedLocaleId) {
        this.supportedLocaleId = supportedLocaleId;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getSupportedLocaleDesc() {
        return supportedLocaleDesc;
    }

    public void setSupportedLocaleDesc(String supportedLocaleDesc) {
        this.supportedLocaleDesc = supportedLocaleDesc;
    }

    public String getLanguageTag() {
        return languageTag;
    }

    public void setLanguageTag(String languageTag) {
        this.languageTag = languageTag;
    }
}
