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
package com.tcdng.jacklyn.shared.security;

import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Biometric category constants.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@StaticList("biometriccategorylist")
public enum BiometricCategory implements EnumConst {

	USERS("U");

	private final String code;

	private BiometricCategory(String code) {
		this.code = code;
	}

	@Override
	public String code() {
		return this.code;
	}

	public static BiometricCategory fromCode(String code) {
		return EnumUtils.fromCode(BiometricCategory.class, code);
	}

	public static BiometricCategory fromName(String name) {
		return EnumUtils.fromName(BiometricCategory.class, name);
	}
}
