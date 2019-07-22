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
package com.tcdng.jacklyn.location.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.location.constants.LocationModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.Tooling;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Represents country entity.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Tooling(name = "country", description = "Country")
@Managed(module = LocationModuleNameConstants.LOCATION_MODULE, title = "Country", reportable = true, auditable = true)
@Table(
        name = "JKCOUNTRY",
        uniqueConstraints = { @UniqueConstraint({ "iso3Code" }), @UniqueConstraint({ "description" }) })
public class Country extends BaseVersionedStatusEntity {

    @Column(name = "COUNTRY_CD", length = 32)
    private String iso3Code;

    @Column(name = "COUNTRY_DESC", length = 64)
    private String description;

    public Country(String iso3Code, String description) {
        this.iso3Code = iso3Code;
        this.description = description;
    }

    public Country() {

    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIso3Code() {
        return iso3Code;
    }

    public void setIso3Code(String iso3Code) {
        this.iso3Code = iso3Code;
    }
}
