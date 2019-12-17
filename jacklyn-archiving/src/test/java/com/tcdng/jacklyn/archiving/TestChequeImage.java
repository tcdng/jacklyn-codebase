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
package com.tcdng.jacklyn.archiving;

import com.tcdng.jacklyn.archiving.constants.ArchivingModuleNameConstants;
import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseTimestampedEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.Table;

/**
 * Test cheque image data.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = ArchivingModuleNameConstants.ARCHIVING_MODULE, title = "TestChequeImage", archivable = true)
@Table("TEST_CHEQUE_IMAGE")
public class TestChequeImage extends BaseTimestampedEntity {

    @Column(nullable = true)
    private byte[] image;

    @Column(type = ColumnType.CLOB, nullable = true)
    private String template;

    @Column
    private Boolean archivedFlag;
    
    @Override
    public String getDescription() {
        return this.template;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Boolean getArchivedFlag() {
        return archivedFlag;
    }

    public void setArchivedFlag(Boolean archivedFlag) {
        this.archivedFlag = archivedFlag;
    }
}
