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
package com.tcdng.jacklyn.common.utils;

import java.io.File;
import java.util.Date;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.constants.CommonModuleErrorConstants;
import com.tcdng.jacklyn.shared.xml.config.module.ManagedConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.AnnotationUtils;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Jacklyn utilities.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public final class JacklynUtils {

	private JacklynUtils() {

	}

	public static String getExtendedFilePath(String path, String workingDtFormat, Date workingDt)
			throws UnifyException {
		StringBuilder sb = new StringBuilder();
		sb.append(path);
		if (!StringUtils.isBlank(workingDtFormat) && workingDt != null) {
			if (!path.endsWith("/") && !path.endsWith("\\")) {
				sb.append(File.separator);
			}
			sb.append(CalendarUtils.format(workingDtFormat, workingDt));
		}
		return sb.toString();
	}

	public static String generateManagedRecordTitle(Class<? extends Entity> managed) {
		String title
				= AnnotationUtils.getAnnotationString(managed.getAnnotation(Managed.class).title());
		if (title == null) {
			title = NameUtils.getDescription(managed);
		}
		return title;
	}

	public static String generateManagedRecordReportableName(Class<? extends Entity> reportable,
			String title) {
		return reportable.getAnnotation(Managed.class).module().toLowerCase() + '-'
				+ StringUtils.squeeze(title) + "-rpt";
	}

	public static ManagedConfig getManagedConfig(ModuleConfig moduleConfig, String type)
			throws UnifyException {
		ManagedConfig managedConfig = moduleConfig.getManagedConfig(type);
		if (managedConfig == null) {
			throw new UnifyException(CommonModuleErrorConstants.UNKNOWN_MANAGED_TYPE, type,
					moduleConfig.getName());
		}
		return managedConfig;
	}
}
