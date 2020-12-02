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

import java.util.List;

import com.tcdng.jacklyn.common.data.ReportMultiFileOptions;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.Input;

/**
 * Report multi-file options generator,
 * 
 * @author Lateef
 * @since 1.0
 */
public interface ReportMultiFileOptionsGenerator extends UnifyComponent {

	/**
	 * Generates report multi-file options
	 * 
	 * @param path  base output path
	 * @param inputList the report input
	 * @return the generated report options
	 * @throws UnifyException if an error occurs
	 */
	ReportMultiFileOptions generate(String path, List<Input<?>> inputList) throws UnifyException;
}
