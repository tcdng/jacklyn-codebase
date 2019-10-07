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
package com.tcdng.jacklyn.common;

import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.batch.BatchItemRecord;
import com.tcdng.unify.core.database.AbstractTestTableEntity;

/**
 * Test bank record.
 *
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "TEST_BANK", uniqueConstraints = { @UniqueConstraint({ "name" }) })
public class TestBank extends AbstractTestTableEntity implements BatchItemRecord {

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String shortName;

    @Column
    private String hoRoutingNo;

    @Column
    private String country;

    @Override
    public Object getBatchId() {
        return null;
    }

    @Override
    public void setBatchId(Object id) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getHoRoutingNo() {
        return hoRoutingNo;
    }

    public void setHoRoutingNo(String hoRoutingNo) {
        this.hoRoutingNo = hoRoutingNo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
