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
package com.tcdng.jacklyn.common.business;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcdng.jacklyn.common.data.ReportMultiFileOptions;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.Input;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient base class for report multi-file options generator,
 * 
 * @author Lateef
 * @since 1.0
 */
public abstract class AbstractReportMultiFileOptionsGenerator extends AbstractUnifyComponent
		implements ReportMultiFileOptionsGenerator {

	@Override
	public ReportMultiFileOptions generate(String path, List<Input<?>> inputList) throws UnifyException {
		Map<String, Object> params = Collections.emptyMap();
		if (!DataUtils.isBlank(inputList)) {
			params = new HashMap<String, Object>();
			for (Input<?> input : inputList) {
				params.put(input.getName(), input.getTypeValue());
			}
		}

		return doGenerate(path, params);
	}

	protected abstract ReportMultiFileOptions doGenerate(String path, Map<String, Object> params) throws UnifyException;

	@Override
	protected void onInitialize() throws UnifyException {

	}

	@Override
	protected void onTerminate() throws UnifyException {

	}
}
