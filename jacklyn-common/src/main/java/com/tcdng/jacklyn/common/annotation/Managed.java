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
package com.tcdng.jacklyn.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tcdng.unify.core.constant.AnnotationConstants;

/**
 * Annotation for indicating managed record type.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Managed {

    /** Entity type module */
    String module();

    /** Entity type title */
    String title() default AnnotationConstants.NONE;

    /** Indicates record type is reportable */
    boolean reportable() default false;

    /** Indicates record type is auditable */
    boolean auditable() default false;

    /** Indicates record type is archivable */
    boolean archivable() default false;

    /** Comma-separated list of fields to exclude on report */
    String excludeOnReport() default AnnotationConstants.NONE;

    /** Comma-separated list of fields to exclude on audit */
    String excludeOnAudit() default AnnotationConstants.NONE;

    /** Comma-separated list of fields to exclude on archive */
    String excludeOnArchive() default AnnotationConstants.NONE;
}
