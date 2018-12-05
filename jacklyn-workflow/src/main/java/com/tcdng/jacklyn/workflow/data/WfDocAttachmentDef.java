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

package com.tcdng.jacklyn.workflow.data;

import com.tcdng.unify.core.constant.FileAttachmentType;

/**
 * Workflow document attachment definition.
 * 
 * @author Lateef
 * @since 1.0
 */
public class WfDocAttachmentDef extends BaseLabelWfDef {

	private static final long serialVersionUID = -2716941379368718045L;

	private FileAttachmentType attachmentType;

	public WfDocAttachmentDef(String name, String description, String label,
			FileAttachmentType attachmentType) {
		super(name, description, label);
		this.attachmentType = attachmentType;
	}

	public FileAttachmentType getAttachmentType() {
		return attachmentType;
	}

}
