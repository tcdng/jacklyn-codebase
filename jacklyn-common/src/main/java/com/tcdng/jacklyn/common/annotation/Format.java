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
package com.tcdng.jacklyn.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tcdng.unify.core.constant.AnnotationConstants;
import com.tcdng.unify.core.constant.HAlignType;

/**
 * Annotation used for specifying managed field format information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Format {

	/** The field description */
	String description() default AnnotationConstants.NONE;

	/** The field formatter UPL descriptor */
	String formatter() default AnnotationConstants.NONE;

	/** The component name of associated list command */
	String list() default AnnotationConstants.NONE;

	/** Field horizontal alignment */
	HAlignType halign() default HAlignType.LEFT;

	/** The multi-part width ratio for field */
	int widthRatio() default -1;

	/** Indicates if field is masked */
	boolean mask() default false;
}
