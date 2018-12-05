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

package com.tcdng.jacklyn.system.entities;

import com.tcdng.jacklyn.common.entities.BaseStatusEntityQuery;

/**
 * Authentication query.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class AuthenticationQuery extends BaseStatusEntityQuery<Authentication> {

	public AuthenticationQuery() {
		super(Authentication.class);
	}

	public AuthenticationQuery name(String name) {
		return (AuthenticationQuery) equals("name", name);
	}

	public AuthenticationQuery nameLike(String name) {
		return (AuthenticationQuery) like("name", name);
	}

	public AuthenticationQuery descriptionLike(String description) {
		return (AuthenticationQuery) like("description", description);
	}
}
