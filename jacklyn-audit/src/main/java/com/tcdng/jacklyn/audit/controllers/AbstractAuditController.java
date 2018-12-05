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

package com.tcdng.jacklyn.audit.controllers;

import com.tcdng.jacklyn.audit.business.AuditModule;
import com.tcdng.jacklyn.audit.constants.AuditModuleNameConstants;
import com.tcdng.jacklyn.common.controllers.BasePageController;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Abstract base page controller for audit module.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractAuditController extends BasePageController {

	@Configurable(AuditModuleNameConstants.AUDITBUSINESSMODULE)
	private AuditModule auditModule;

	public AbstractAuditController(boolean secured, boolean readOnly) {
		super(secured, readOnly);
	}

	protected AuditModule getAuditModule() {
		return auditModule;
	}

}
