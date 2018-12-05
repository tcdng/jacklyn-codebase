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
package com.tcdng.jacklyn.security.entities;

import com.tcdng.jacklyn.common.entities.BaseVersionedTimestampedEntity;
import com.tcdng.jacklyn.shared.security.BiometricCategory;
import com.tcdng.jacklyn.shared.security.BiometricType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Biometric entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table("BIOMETRIC")
public class Biometric extends BaseVersionedTimestampedEntity {

	@ForeignKey
	private BiometricType type;

	@Column
	private BiometricCategory category;

	@Column(nullable = true)
	private byte[] biometric;

	@ListOnly(key = "type", property = "description")
	private String typeDesc;

	@Override
	public String getDescription() {
		return StringUtils.concatenate("Biometric category=", category, ", type = ", type);
	}

	public BiometricCategory getCategory() {
		return category;
	}

	public void setCategory(BiometricCategory category) {
		this.category = category;
	}

	public BiometricType getType() {
		return type;
	}

	public void setType(BiometricType type) {
		this.type = type;
	}

	public byte[] getBiometric() {
		return biometric;
	}

	public void setBiometric(byte[] biometric) {
		this.biometric = biometric;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
}
