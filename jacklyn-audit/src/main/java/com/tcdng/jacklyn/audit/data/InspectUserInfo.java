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

package com.tcdng.jacklyn.audit.data;

import java.util.List;

/**
 * Inspect user information object.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class InspectUserInfo {

	private String fullName;

	private String emailAddress;

	private byte[] photo;

	private List<InspectUserAuditItem> auditItems;

	public InspectUserInfo() {

	}

	public InspectUserInfo(String fullName, String emailAddress, byte[] photo,
			List<InspectUserAuditItem> auditItems) {
		this.fullName = fullName;
		this.emailAddress = emailAddress;
		this.photo = photo;
		this.auditItems = auditItems;
	}

	public String getFullName() {
		return fullName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public int getItemCount() {
		if (auditItems != null) {
			return auditItems.size();
		}

		return 0;
	}

	public List<InspectUserAuditItem> getAuditItems() {
		return auditItems;
	}

	public boolean isUser() {
		return fullName != null;
	}
}
