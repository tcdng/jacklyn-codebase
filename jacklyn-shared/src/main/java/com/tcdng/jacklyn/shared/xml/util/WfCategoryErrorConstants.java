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

package com.tcdng.jacklyn.shared.xml.util;

/**
 * Workflow category XML validation errors.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface WfCategoryErrorConstants {

    /**
     * Workflow category has no name.
     */
    String WFCATEGORY_NO_NAME = "WFCAT_0001";

    /**
     * Workflow category has invalid name format. Name = {0}.
     */
    String WFCATEGORY_INVALID_NAME_FORMAT = "WFCAT_0002";

    /**
     * Workflow category has no version.
     */
    String WFCATEGORY_NO_VERSION = "WFCAT_0003";

    /**
     * Workflow category has no description.
     */
    String WFCATEGORY_NO_DESC = "WFCAT_0004";

    /**
     * Workflow category has no documents.
     */
    String WFCATEGORY_NO_DOCUMENTS = "WFCAT_0005";

    /**
     * Workflow category has no templates.
     */
    String WFCATEGORY_NO_TEMPLATES = "WFCAT_0006";

    /**
     * Workflow category document exists. Name = {0}.
     */
    String WFCATEGORY_DOCUMENT_EXISTS = "WFCAT_0007";

    /**
     * Workflow category template exists. Name = {0}.
     */
    String WFCATEGORY_TEMPLATE_EXISTS = "WFCAT_0008";

    /**
     * Workflow category has document errors. Document index = {0}.
     */
    String WFCATEGORY_DOCUMENT_ERRORS = "WFCAT_0009";

    /**
     * Workflow category has template errors. Template index = {0}.
     */
    String WFCATEGORY_TEMPLATE_ERRORS = "WFCAT_000A";

    /**
     * Workflow category document name not compatible. Name = {0}.
     */
    String WFCATEGORY_DOCUMENT_NAME_NOT_COMPATIBLE = "WFCAT_000B";

    /**
     * Workflow category template name not compatible. Name = {0}.
     */
    String WFCATEGORY_TEMPLATE_NAME_NOT_COMPATIBLE = "WFCAT_000C";

    /**
     * Workflow category document version not compatible. Document index = {0},
     * category version = {1}, document version = {2}.
     */
    String WFCATEGORY_DOCUMENT_VERSION_NOT_COMPATIBLE = "WFCAT_000D";

    /**
     * Workflow category template version not compatible. Template index = {0},
     * category version = {1}, template version = {2}.
     */
    String WFCATEGORY_TEMPLATE_VERSION_NOT_COMPATIBLE = "WFCAT_000E";
}
