/*
 * Copyright 2018-2020 The Code Department.
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
package com.tcdng.jacklyn.common.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Report file options.
 * 
 * @author Lateef
 * @since 1.0
 */
public class ReportFileOptions {

	private String fileName;
	
	private Map<String, Object> paramOptions;

	public ReportFileOptions(String fileName) {
		this.fileName = fileName;
	}

	public void addParamOption(String paramName, Object val) {
		if (paramOptions == null) {
			paramOptions = new HashMap<String, Object>();
		}
		
		paramOptions.put(paramName, val);
	}
	
	public String getFileName() {
		return fileName;
	}

	public boolean isParam(String paramName) {
		return getParamOptions().containsKey(paramName);
	}
	
	public Object getValue(String paramName) {
		return getParamOptions().get(paramName);
	}
	
	public Map<String, Object> getParamOptions() {
		if (paramOptions == null) {
			return Collections.emptyMap();
		}
		
		return paramOptions;
	}

	@Override
	public String toString() {
		return "ReportFileOptions [fileName=" + fileName + ", paramOptions=" + paramOptions + "]";
	}
}
