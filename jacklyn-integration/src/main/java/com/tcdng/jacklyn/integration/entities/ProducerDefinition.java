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
package com.tcdng.jacklyn.integration.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.integration.constants.IntegrationModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Producer definition entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(
        module = IntegrationModuleNameConstants.INTEGRATION_MODULE, title = "Producer Definition", reportable = true,
        auditable = true)
@Table(
        name = "JKINTPRODUCERDEF",
        uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }) })
public class ProducerDefinition extends BaseVersionedStatusEntity {

    @Column(name = "PRODUCERDEF_NM", length = 32)
    private String name;

    @Column(name = "PRODUCERDEF_DESC", length = 64)
    private String description;

    @Column(length = 96)
    private String messageTag;

    @Column(length = 64)
    private String preferredConsumer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessageTag() {
        return messageTag;
    }

    public void setMessageTag(String messageTag) {
        this.messageTag = messageTag;
    }

    public String getPreferredConsumer() {
        return preferredConsumer;
    }

    public void setPreferredConsumer(String preferredConsumer) {
        this.preferredConsumer = preferredConsumer;
    }
}
