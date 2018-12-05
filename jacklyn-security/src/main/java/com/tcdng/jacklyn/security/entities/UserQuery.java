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

import java.util.Collection;

import com.tcdng.jacklyn.common.entities.BaseVersionedTimestampedStatusEntityQuery;

/**
 * Query class for user records.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class UserQuery extends BaseVersionedTimestampedStatusEntityQuery<User> {

	public UserQuery() {
		super(User.class);
	}

	@Override
	public UserQuery id(Long id) {
		return (UserQuery) super.id(id);
	}

	@Override
	public UserQuery idIn(Collection<Long> id) {
		return (UserQuery) super.idIn(id);
	}

	@Override
	public UserQuery idNotIn(Collection<Long> id) {
		return (UserQuery) super.idNotIn(id);
	}

	public UserQuery fullNameLike(String fullName) {
		return (UserQuery) like("fullName", fullName);
	}

	public UserQuery loginId(String loginId) {
		return (UserQuery) equals("loginId", loginId);
	}

	public UserQuery password(String password) {
		return (UserQuery) equals("password", password);
	}
}
