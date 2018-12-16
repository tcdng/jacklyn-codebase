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
 * Workflow document XML validation errors.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface WfDocumentErrorConstants {

	/**
	 * Workflow document has no name.
	 */
	String WFDOCUMENT_NO_NAME = "WFDOC_0001";

	/**
	 * Workflow document has invalid name format. Name = {0}.
	 */
	String WFDOCUMENT_INVALID_NAME_FORMAT = "WFDOC_0002";

	/**
	 * Workflow document has no description.
	 */
	String WFDOCUMENT_NO_DESC = "WFDOC_0003";

	/**
	 * Workflow document has no fields.
	 */
	String WFDOCUMENT_NO_FIELDS = "WFDOC_0004";

	/**
	 * Workflow document field has no name. Index = {0}.
	 */
	String WFDOCUMENT_FIELD_NO_NAME = "WFDOC_0005";

	/**
	 * Workflow document field has no description. Index = {0}.
	 */
	String WFDOCUMENT_FIELD_NO_DESC = "WFDOC_0006";

	/**
	 * Workflow document field with name already exists. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_FIELD_EXIST = "WFDOC_0007";

	/**
	 * Workflow document field with invalid name. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_FIELD_INVALID_NAME = "WFDOC_0008";

	/**
	 * Workflow document complex field has no sub fields. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_COMPLEX_NO_FIELDS = "WFDOC_0009";

	/**
	 * Workflow document field has no data type. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_FIELD_NO_TYPE = "WFDOC_000A";

	/**
	 * Workflow document field with invalid data type. Index = {0}, name = {1}, type = {2}
	 */
	String WFDOCUMENT_FIELD_INVALID_DATATYPE = "WFDOC_000B";

	/**
	 * Workflow document classifier has no name. Index = {0}.
	 */
	String WFDOCUMENT_CLASSIFIER_NO_NAME = "WFDOC_000C";

	/**
	 * Workflow document classifier has no description. Index = {0}.
	 */
	String WFDOCUMENT_CLASSIFIER_NO_DESC = "WFDOC_000D";

	/**
	 * Workflow document classifier with name already exists. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_CLASSIFIER_EXIST = "WFDOC_000E";

	/**
	 * Workflow document classifier with invalid name. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_CLASSIFIER_INVALID_NAME = "WFDOC_000F";

	/**
	 * Workflow document classifier has no filter or logic. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_CLASSIFIER_NO_FILTER_LOGIC = "WFDOC_0010";

	/**
	 * Workflow document classifier has filter and logic. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_CLASSIFIER_HAS_FILTER_LOGIC = "WFDOC_0011";

	/**
	 * Workflow document classifier filter has no field. Index = {0}, classifier =  {1}.
	 */
	String WFDOCUMENT_CLASSIFIERFILTER_NO_FIELD = "WFDOC_0012";

	/**
	 * Workflow document classifier filter has no operation. Index = {0}, classifier = {1}.
	 */
	String WFDOCUMENT_CLASSIFIERFILTER_NO_OP = "WFDOC_0013";

	/**
	 * Workflow document classifier filter refers to unknown field. Index = {0}, classifier = {1}, field = {2}.
	 */
	String WFDOCUMENT_CLASSIFIERFILTER_UNKNOWN_FIELD = "WFDOC_0014";

	/**
	 * Workflow document classifier filter refers to complex field. Index = {0}, classifier = {1}, field = {2}.
	 */
	String WFDOCUMENT_CLASSIFIERFILTER_COMPLEX_FIELD = "WFDOC_0015	";

	/**
	 * Workflow document attachment has no name. Index = {0}.
	 */
	String WFDOCUMENT_ATTACHMENT_NO_NAME = "WFDOC_0016";

	/**
	 * Workflow document attachment has no description. Index = {0}.
	 */
	String WFDOCUMENT_ATTACHMENT_NO_DESC = "WFDOC_0017";

	/**
	 * Workflow document attachment with name already exists. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_ATTACHMENT_EXIST = "WFDOC_0018";

	/**
	 * Workflow document attachment with invalid name. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_ATTACHMENT_INVALID_NAME = "WFDOC_0019";

	/**
	 * Workflow document attachment has no type. Index = {0}, name = {1}.
	 */
	String WFDOCUMENT_ATTACHMENT_NO_TYPE = "WFDOC_001A";

	/**
	 * Workflow document bean mapping has no name. Index = {0}.
	 */
	String WFDOCUMENT_BEANMAPPING_NO_NAME = "WFDOC_001B";

	/**
	 * Workflow document bean mapping has no description. Index = {0}.
	 */
	String WFDOCUMENT_BEANMAPPING_NO_DESC = "WFDOC_001C";

	/**
	 * Workflow document bean mapping with name already exists. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_BEANMAPPING_EXIST = "WFDOC_001D";

	/**
	 * Workflow document bean mapping with invalid name. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_BEANMAPPING_INVALID_NAME = "WFDOC_001E";

	/**
	 * Workflow document bean mapping has no type. Index = {0}, name = {1}.
	 */
	String WFDOCUMENT_BEANMAPPING_NO_TYPE = "WFDOC_001F";

	/**
	 * Workflow document bean mapping has no bean type. Index = {0}, name = {1}.
	 */
	String WFDOCUMENT_BEANMAPPING_NO_BEANTYPE = "WFDOC_0020";

	/**
	 * Workflow document bean mapping has no field mappings. Index = {0}, name = {1}.
	 */
	String WFDOCUMENT_BEANMAPPING_NO_MAPPINGS = "WFDOC_0021";

	/**
	 * Workflow document field mapping has no document field. Index = {0}, bean mapping = {1}.
	 */
	String WFDOCUMENT_FIELDMAPPING_NO_DOCFIELD = "WFDOC_0022";

	/**
	 * Workflow document field mapping has no bean field. Index = {0}, bean mapping = {1}.
	 */
	String WFDOCUMENT_FIELDMAPPING_NO_BEANFIELD = "WFDOC_0023";

	/**
	 * Workflow document field mapping refers to unknown document field. Index = {0}, bean mapping = {1}, field name = {2}.
	 */
	String WFDOCUMENT_FIELDMAPPING_UNKNOWN_FIELD = "WFDOC_0024";

	/**
	 * Workflow document form has no tabs..
	 */
	String WFDOCUMENT_FORM_NO_TABS = "WFDOC_0025";

	/**
	 * Workflow document tab has no name. Index = {0}.
	 */
	String WFDOCUMENT_TAB_NO_NAME = "WFDOC_0026";

	/**
	 * Workflow document tab has no description. Index = {0}.
	 */
	String WFDOCUMENT_TAB_NO_DESC = "WFDOC_0027";

	/**
	 * Workflow document tab with name already exists. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_TAB_EXIST = "WFDOC_0028";

	/**
	 * Workflow document tab with invalid name. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_TAB_INVALID_NAME = "WFDOC_0029";

	/**
	 * Workflow document tab with no sections. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_TAB_NO_SECTIONS = "WFDOC_002A";

	/**
	 * Workflow document section has no name. Index = {0}.
	 */
	String WFDOCUMENT_SECTION_NO_NAME = "WFDOC_002B";

	/**
	 * Workflow document section has no description. Index = {0}.
	 */
	String WFDOCUMENT_SECTION_NO_DESC = "WFDOC_002C";

	/**
	 * Workflow document section with name already exists. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_SECTION_EXIST = "WFDOC_002D";

	/**
	 * Workflow document section with invalid name. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_SECTION_INVALID_NAME = "WFDOC_002E";

	/**
	 * Workflow document section with no fields. Index = {0}, name = {1}
	 */
	String WFDOCUMENT_SECTION_NO_FIELDS = "WFDOC_002F";

	/**
	 * Workflow document section unknown complex binding. Index = {0}, name = {1}, binding = {2}
	 */
	String WFDOCUMENT_SECTION_UNKNOWN_COMPLEX = "WFDOC_0030";
	
	/**
	 * Workflow document section field binding already exists. Index = {0}, sectionName = {1}, binding = {2}
	 */
	String WFDOCUMENT_FORMFIELD_MULTIPLE_BINDING = "WFDOC_0031";
	
	/**
	 * Workflow document section field has no binding. Index = {0}, sectionName = {1}
	 */
	String WFDOCUMENT_FORMFIELD_NO_BINDING = "WFDOC_0032";
	
	/**
	 * Workflow document section field binding refers to unknown field. Index = {0}, sectionName = {1}, binding = {2}
	 */
	String WFDOCUMENT_FORMFIELD_UNKNOWN_FIELD = "WFDOC_0033";
	
	/**
	 * Workflow document section field binding refers to non-complex subfield. Index = {0}, sectionName = {1}, binding = {2}
	 */
	String WFDOCUMENT_FORMFIELD_NOT_COMPLEX_FIELD = "WFDOC_0034";
	
	/**
	 * Workflow document section field binding has no UPL editor. Index = {0}, sectionName = {1}, binding = {2}
	 */
	String WFDOCUMENT_FORMFIELD_NO_EDITOR = "WFDOC_0035"; 

	/**
	 * Workflow document has no version.
	 */
	String WFDOCUMENT_NO_VERSION = "WFDOC_0036";
}
