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
package com.tcdng.jacklyn.integration.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseTimestampedEntity;
import com.tcdng.jacklyn.integration.constants.IntegrationModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Producer queue entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = IntegrationModuleNameConstants.INTEGRATION_MODULE, title = "Producer Queue")
@Table("JKINTPRODUCERQUEUE")
public class ProducerQueue extends BaseTimestampedEntity {

    @ForeignKey(type = ProducerDefinition.class)
    private Long producerDefinitionId;

    @Column(nullable = true)
    private String branchCode;

    @Column(nullable = true)
    private String departmentCode;

    @Column
    private byte[] message;

    @ListOnly(key = "producerDefinitionId", property = "name")
    private String producerDefinitionName;

    @ListOnly(key = "producerDefinitionId", property = "messageTag")
    private String messageTag;

    @ListOnly(key = "producerDefinitionId", property = "preferredConsumer")
    private String preferredConsumer;

    public ProducerQueue(Long producerDefinitionId, String branchCode, String departmentCode, byte[] message) {
        this.producerDefinitionId = producerDefinitionId;
        this.branchCode = branchCode;
        this.departmentCode = departmentCode;
        this.message = message;
    }

    public ProducerQueue() {

    }

    @Override
    public String getBranchCode() {
        return branchCode;
    }

    @Override
    public String getDepartmentCode() {
        return departmentCode;
    }

    @Override
    public String getDescription() {
        return producerDefinitionName;
    }

    public Long getProducerDefinitionId() {
        return producerDefinitionId;
    }

    public void setProducerDefinitionId(Long producerDefinitionId) {
        this.producerDefinitionId = producerDefinitionId;
    }

    public String getMessageTag() {
        return messageTag;
    }

    public void setMessageTag(String messageTag) {
        this.messageTag = messageTag;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getPreferredConsumer() {
        return preferredConsumer;
    }

    public void setPreferredConsumer(String preferredConsumer) {
        this.preferredConsumer = preferredConsumer;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public String getProducerDefinitionName() {
        return producerDefinitionName;
    }

    public void setProducerDefinitionName(String producerDefinitionName) {
        this.producerDefinitionName = producerDefinitionName;
    }
}
