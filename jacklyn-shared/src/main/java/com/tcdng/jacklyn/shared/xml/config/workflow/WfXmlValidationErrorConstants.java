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

package com.tcdng.jacklyn.shared.xml.config.workflow;

/**
 * Workflow XML validation errors.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface WfXmlValidationErrorConstants {

	/**
	 * Workflow category has no name.
	 */
	String WFXML_CATEGORY_NO_NAME = "WF_XML_VALIDATION_0001";

	/**
	 * Workflow category has no description.
	 */
	String WFXML_CATEGORY_NO_DESC = "WF_XML_VALIDATION_0002";

	/**
	 * Workflow category must have at least one document definition. Category = {0}.
	 */
	String WFXML_CATEGORY_NO_DOCS = "WF_XML_VALIDATION_0003";

	/**
	 * Workflow category must have at least one template definition. Category = {0}.
	 */
	String WFXML_CATEGORY_NO_TEMPLATES = "WF_XML_VALIDATION_0004";

	/**
	 * Workflow document has no name. Category = {0}, docIndex = {1}.
	 */
	String WFXML_DOCUMENT_NO_NAME = "WF_XML_VALIDATION_0005";

	/**
	 * Workflow document has no description. Category = {0}, docIndex = {1}.
	 */
	String WFXML_DOCUMENT_NO_DESC = "WF_XML_VALIDATION_0006";

	/**
	 * Workflow document must have at least one field definition. Category = {0}, docIndex = {1}, docName= {2}.
	 */
	String WFXML_DOCUMENT_NO_FIELD = "WF_XML_VALIDATION_0007";

	/**
	 * Workflow document with name already exists. Category = {0}, docIndex = {1}, docName= {2}.
	 */
	String WFXML_DOCUMENT_NAME_EXIST = "WF_XML_VALIDATION_0008";

	/**
	 * Workflow document with description already exists. Category = {0}, docIndex = {1}, docDesc= {2}.
	 */
	String WFXML_DOCUMENT_DESC_EXIST = "WF_XML_VALIDATION_0009";

	/**
	 * Workflow form section has no name. FormIndex = {0}, formName = {1}, sectionIndex = {2}.
	 */
	String WFXML_SECTION_NO_NAME = "WF_XML_VALIDATION_000A";

	/**
	 * Workflow form section has no description. FormIndex = {0}, formName = {1}, sectionIndex = {2}.
	 */
	String WFXML_SECTION_NO_DESC = "WF_XML_VALIDATION_000B";

	/**
	 * Workflow form section has no fields. FormIndex = {0}, formName = {1}, sectionIndex = {2}.
	 */
	String WFXML_SECTION_NO_FIELDS = "WF_XML_VALIDATION_000C";

	/**
	 * Workflow form section with name already exists. FormIndex = {0}, formName = {1}, sectionIndex = {2}, sectionName = {3}.
	 */
	String WFXML_SECTION_NAME_EXIST = "WF_XML_VALIDATION_000D";

	/**
	 * Workflow form section with description already exists. FormIndex = {0}, formName = {1}, sectionIndex = {2}, sectionDesc = {3}.
	 */
	String WFXML_SECTION_DESC_EXIST = "WF_XML_VALIDATION_000E";

	/**
	 * Workflow form field has no binding. FormIndex = {0}, formName = {1}, sectionIndex = {2}, fieldIndex = {3}.
	 */
	String WFXML_FIELD_NO_BINDING = "WF_XML_VALIDATION_000F";

	/**
	 * Workflow form field has no description. FormIndex = {0}, formName = {1}, sectionIndex = {2}, fieldIndex = {3}.
	 */
	String WFXML_FIELD_NO_DESC = "WF_XML_VALIDATION_0010";

	/**
	 * Workflow form field with binding already exists. FormIndex = {0}, formName = {1}, sectionIndex = {2}, fieldIndex = {3}, fieldName = {4}.
	 */
	String WFXML_FIELD_BINDING_EXIST = "WF_XML_VALIDATION_0011";

	/**
	 * Workflow form field with description already exists. FormIndex = {0}, formName = {1}, sectionIndex = {2}, fieldIndex = {3}, sectionDesc = {4}.
	 */
	String WFXML_FIELD_DESC_EXIST = "WF_XML_VALIDATION_0012";

	/**
	 * Workflow form field has no editor descriptor. FormIndex = {0}, formName = {1}, sectionIndex = {2}, fieldIndex = {3}.
	 */
	String WFXML_FIELD_NO_EDITOR = "WF_XML_VALIDATION_0013";

	/**
	 * Workflow document classifier has no name. DocIndex = {0}, docName = {1}, classifierIndex = {2}.
	 */
	String WFXML_CLASSIFIER_NO_NAME = "WF_XML_VALIDATION_0014";

	/**
	 * Workflow document classifier has no description. DocIndex = {0}, docName = {1}, classifierIndex = {2}.
	 */
	String WFXML_CLASSIFIER_NO_DESC = "WF_XML_VALIDATION_0015";

	/**
	 * Workflow document classifier with name already exists. DocIndex = {0}, docName = {1}, classifierIndex = {2}, classifierName = {3}.
	 */
	String WFXML_CLASSIFIER_NAME_EXIST = "WF_XML_VALIDATION_0016";

	/**
	 * Workflow document classifier with description already exists. DocIndex = {0}, docName = {1}, classifierIndex = {2}, classifierDesc = {3}.
	 */
	String WFXML_CLASSIFIER_DESC_EXIST = "WF_XML_VALIDATION_0017";

	/**
	 * Workflow document classifier has no logic and classification filters. DocIndex = {0}, docName = {1}, classifierIndex = {2}.
	 */
	String WFXML_CLASSIFIER_NO_LOGIC_FILTERS = "WF_XML_VALIDATION_0018";

	/**
	 * Workflow document filter requires a field. DocIndex = {0}, docName = {1}, classifierIndex = {2}, filterIndex = {3}.
	 */
	String WFXML_FILTER_NO_FIELD = "WF_XML_VALIDATION_0019";

	/**
	 * Workflow document filter referred field is unknown. DocIndex = {0}, docName = {1}, classifierIndex = {2}, filterIndex = {3}, fieldName {4}.
	 */
	String WFXML_FILTER_FIELD_UNKNOWN = "WF_XML_VALIDATION_001A";

	/**
	 * Workflow document filter requires an operation. DocIndex = {0}, docName = {1}, classifierIndex = {2}, filterIndex = {3}.
	 */
	String WFXML_FILTER_NO_OP = "WF_XML_VALIDATION_001B";
	
	/**
	 * Workflow document filter operation requires value 1. DocIndex = {0}, docName = {1}, classifierIndex = {2}, filterIndex = {3}.
	 */
	String WFXML_FILTER_NO_VALUE1 = "WF_XML_VALIDATION_001C";
	
	/**
	 * Workflow document filter operation requires value 2. DocIndex = {0}, docName = {1}, classifierIndex = {2}, filterIndex = {3}.
	 */
	String WFXML_FILTER_NO_VALUE2 = "WF_XML_VALIDATION_001D";

	/**
	 * Workflow document attachment has no name. DocIndex = {0}, docName = {1}, attachmentIndex = {2}.
	 */
	String WFXML_ATTACHMENT_NO_NAME = "WF_XML_VALIDATION_001E";

	/**
	 * Workflow document attachment has no description. DocIndex = {0}, docName = {1}, attachmentIndex = {2}.
	 */
	String WFXML_ATTACHMENT_NO_DESC = "WF_XML_VALIDATION_001F";

	/**
	 * Workflow document attachment has no type. DocIndex = {0}, docName = {1}, attachmentIndex = {2}.
	 */
	String WFXML_ATTACHMENT_NO_TYPE = "WF_XML_VALIDATION_0020";

	/**
	 * Workflow document attachment with name already exists. DocIndex = {0}, docName = {1}, attachmentIndex = {2}, attachmentName = {3}.
	 */
	String WFXML_ATTACHMENT_NAME_EXIST = "WF_XML_VALIDATION_0021";

	/**
	 * Workflow document attachment with description already exists. DocIndex = {0}, docName = {1}, attachmentIndex = {2}, attachmentDesc = {3}.
	 */
	String WFXML_ATTACHMENT_DESC_EXIST = "WF_XML_VALIDATION_0022";

	/**
	 * Workflow template has no name. Category = {0}, templateIndex = {1}.
	 */
	String WFXML_TEMPLATE_NO_NAME = "WF_XML_VALIDATION_0023";

	/**
	 * Workflow template has no description. Category = {0}, templateIndex = {1}.
	 */
	String WFXML_TEMPLATE_NO_DESC = "WF_XML_VALIDATION_0024";

	/**
	 * Workflow template must have step definitions. Category = {0}, templateIndex = {1}, templateName= {2}.
	 */
	String WFXML_TEMPLATE_NO_STEPS = "WF_XML_VALIDATION_0025";

	/**
	 * Workflow template with name already exists. Category = {0}, templateIndex = {1}, templateName= {2}.
	 */
	String WFXML_TEMPLATE_NAME_EXIST = "WF_XML_VALIDATION_0026";

	/**
	 * Workflow template with description already exists. Category = {0}, templateIndex = {1}, templateDesc= {2}.
	 */
	String WFXML_TEMPLATE_DESC_EXIST = "WF_XML_VALIDATION_0027";

	/**
	 * Workflow template must refer to a document. Category = {0}, templateIndex = {1}, templateName= {2}.
	 */
	String WFXML_TEMPLATE_NO_DOCUMENT = "WF_XML_VALIDATION_0028";

	/**
	 * Workflow template document is unknown. Category = {0}, templateIndex = {1}, templateName= {2}, document= {3}.
	 */
	String WFXML_TEMPLATE_DOCUMENT_UNKNOWN = "WF_XML_VALIDATION_0029";

	/**
	 * Workflow template has no start step. Category = {0}, templateIndex = {1}, templateName= {2}.
	 */
	String WFXML_TEMPLATE_NO_START = "WF_XML_VALIDATION_002A";

	/**
	 * Workflow template has multiple start steps. Category = {0}, templateIndex = {1}, templateName= {2}.
	 */
	String WFXML_TEMPLATE_MULTIPLE_START_STEPS = "WF_XML_VALIDATION_002B";

	/**
	 * Workflow template has no end step. Category = {0}, templateIndex = {1}, templateName= {2}.
	 */
	String WFXML_TEMPLATE_NO_END = "WF_XML_VALIDATION_002C";

	/**
	 * Workflow template step has no name. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}.
	 */
	String WFXML_STEP_NO_NAME = "WF_XML_VALIDATION_002D";

	/**
	 * Workflow template step has no description. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}.
	 */
	String WFXML_STEP_NO_DESC = "WF_XML_VALIDATION_002E";

	/**
	 * Workflow template step has no type. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}.
	 */
	String WFXML_STEP_NO_TYPE = "WF_XML_VALIDATION_002F";

	/**
	 * Workflow template step has no participant. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepType = {3}.
	 */
	String WFXML_STEP_NO_PARTICIPANT = "WF_XML_VALIDATION_0030";

	/**
	 * Workflow template step has no priority. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}.
	 */
	String WFXML_STEP_NO_PRIORITY = "WF_XML_VALIDATION_0031";

	/**
	 * Workflow template step has no form mode. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}.
	 */
	String WFXML_STEP_NO_FORMMODE = "WF_XML_VALIDATION_0032";

	/**
	 * Workflow template step with name already exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}.
	 */
	String WFXML_STEP_NAME_EXIST = "WF_XML_VALIDATION_0033";

	/**
	 * Workflow template step with description already exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepDesc = {3}.
	 */
	String WFXML_STEP_DESC_EXIST = "WF_XML_VALIDATION_0034";

	/**
	 * Workflow template start step must have at least one routing or one action. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}.
	 */
	String WFXML_STEP_START_NO_ROUTE_ACTION = "WF_XML_VALIDATION_0035";

	/**
	 * Workflow template normal step must have at least one routing or one action. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}.
	 */
	String WFXML_STEP_NORMAL_NO_ROUTE_ACTION = "WF_XML_VALIDATION_0036";

	/**
	 * Workflow template end step must have no routings or actions. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}.
	 */
	String WFXML_STEP_END_ROUTE_ACTION = "WF_XML_VALIDATION_0037";

	/**
	 * Workflow template step with form privileged must have actions. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}.
	 */
	String WFXML_STEP_FORMPRIVILEGE_ACTION_REQUIRED = "WF_XML_VALIDATION_0038";

	/**
	 * Workflow template routing has no name. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, routingIndex = {3}.
	 */
	String WFXML_ROUTING_NO_NAME = "WF_XML_VALIDATION_0039";

	/**
	 * Workflow template routing has no description. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, routingIndex = {3}.
	 */
	String WFXML_ROUTING_NO_DESC = "WF_XML_VALIDATION_003A";

	/**
	 * Workflow template routing with name already exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, routingIndex = {3}, routingName = {4}.
	 */
	String WFXML_ROUTING_NAME_EXIST = "WF_XML_VALIDATION_003B";

	/**
	 * Workflow template routing with description already exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, routingIndex = {3}, routingDesc = {4}.
	 */
	String WFXML_ROUTING_DESC_EXIST = "WF_XML_VALIDATION_003C";

	/**
	 * Workflow template routing has no target. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, routingIndex = {3}.
	 */
	String WFXML_ROUTING_NO_TARGET = "WF_XML_VALIDATION_003D";

	/**
	 * Workflow template routing has unknown target step. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, routingIndex = {3}, targetStepName = {4}.
	 */
	String WFXML_ROUTING_TARGET_UNKNOWN = "WF_XML_VALIDATION_003E";

	/**
	 * Workflow template routing has unknown classifier TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, routingIndex = {3}, routingName = {4}, classifierName = {5}.
	 */
	String WFXML_ROUTING_CLASSIFIER_UNKNOWN = "WF_XML_VALIDATION_003F";

	/**
	 * Workflow template user action has no name. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}.
	 */
	String WFXML_USERACTION_NO_NAME = "WF_XML_VALIDATION_0040";

	/**
	 * Workflow template user action has no description. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}.
	 */
	String WFXML_USERACTION_NO_DESC = "WF_XML_VALIDATION_0041";

	/**
	 * Workflow template user action with name already exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}, actionName = {4}.
	 */
	String WFXML_USERACTION_NAME_EXIST = "WF_XML_VALIDATION_0042";

	/**
	 * Workflow template user action with description already exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}, actionDesc = {4}.
	 */
	String WFXML_USERACTION_DESC_EXIST = "WF_XML_VALIDATION_0043";

	/**
	 * Workflow template user action has no target. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}.
	 */
	String WFXML_USERACTION_NO_TARGET = "WF_XML_VALIDATION_0044";

	/**
	 * Workflow template user action has unknown target step. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}, targetStepName = {4}.
	 */
	String WFXML_USERACTION_TARGET_UNKNOWN = "WF_XML_VALIDATION_0045";

	/**
	 * Workflow template user action has no notes requirement type. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}.
	 */
	String WFXML_USERACTION_NO_NOTEREQUIREMENT = "WF_XML_VALIDATION_0046";

	/**
	 * Workflow template attachment check has no attachment name. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}, actionName = {4}, attachmentCheckIndex = {5}.
	 */
	String WFXML_ATTACHMENTCHECK_NO_ATTACHMENTNAME= "WF_XML_VALIDATION_0047";

	/**
	 * Workflow template attachment check attachment name is unknown. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}, actionName = {4}, attachmentCheckIndex = {5}, attachmentName = {6}.
	 */
	String WFXML_ATTACHMENTCHECK_ATTACHMENTNAME_UNKNOWN= "WF_XML_VALIDATION_0048";
	
	/**
	 * Workflow template attachment check has no attachment name. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}, actionName = {4}, attachmentCheckIndex = {5}.
	 */
	String WFXML_ATTACHMENTCHECK_NO_REQUIREMENTTYPE= "WF_XML_VALIDATION_0049";
	
	/**
	 * Workflow template attachment check exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}, actionName = {4}, attachmentCheckIndex = {5}, attachmentName = {6}.
	 */
	String WFXML_ATTACHMENTCHECK_EXIST= "WF_XML_VALIDATION_004A";

	/**
	 * Workflow template form privilege has no element name. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, formPrivilegeIndex = {3}.
	 */
	String WFXML_FORMPRIVILEGE_NO_ELEMENT_NAME = "WF_XML_VALIDATION_004B";

	/**
	 * Workflow template form privilege has unknown field. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, formPrivilegeIndex = {3}, fieldName = {4}.
	 */
	String WFXML_FORMPRIVILEGE_FIELD_UNKNOWN = "WF_XML_VALIDATION_004C";

	/**
	 * Workflow template form privilege with name already exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, formPrivilegeIndex = {3}, fieldName = {4}.
	 */
	String WFXML_FORMPRIVILEGE_NAME_EXIST = "WF_XML_VALIDATION_004D";

	/**
	 * Workflow template routing can not target its own step. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, routingIndex = {3}, targetStepName = {4}.
	 */
	String WFXML_ROUTING_TARGET_SELF = "WF_XML_VALIDATION_004E";

	/**
	 * Workflow template user action can not target its own step. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}, targetStepName = {4}.
	 */
	String WFXML_USERACTION_TARGET_SELF = "WF_XML_VALIDATION_004F";

	/**
	 * Workflow form tab has no name. FormIndex = {0}, formName = {1}, tabIndex = {2}.
	 */
	String WFXML_TAB_NO_NAME = "WF_XML_VALIDATION_0050";

	/**
	 * Workflow form tab has no description. FormIndex = {0}, formName = {1}, tabIndex = {2}.
	 */
	String WFXML_TAB_NO_DESC = "WF_XML_VALIDATION_0051";

	/**
	 * Workflow form tab with name already exists. FormIndex = {0}, formName = {1}, tabIndex = {2}, tabName = {3}.
	 */
	String WFXML_TAB_NAME_EXIST = "WF_XML_VALIDATION_0052";

	/**
	 * Workflow form tab with description already exists. FormIndex = {0}, formName = {1}, tabIndex = {2}, tabDesc = {3}.
	 */
	String WFXML_TAB_DESC_EXIST = "WF_XML_VALIDATION_0053";

	/**
	 * Workflow form section tab name is unknown. FormIndex = {0}, formName = {1}, sectionIndex = {2}, tabName = {3}.
	 */
	String WFXML_SECTION_TAB_UNKNOWN = "WF_XML_VALIDATION_0054";

	/**
	 * Workflow document bean mapping has no name. DocIndex = {0}, docName = {1}, recordMappingIndex = {2}.
	 */
	String WFXML_BEANMAPPING_NO_NAME = "WF_XML_VALIDATION_0055";

	/**
	 * Workflow document bean mapping has no description. DocIndex = {0}, docName = {1}, recordMappingIndex = {2}.
	 */
	String WFXML_BEANMAPPING_NO_DESC = "WF_XML_VALIDATION_0056";

	/**
	 * Workflow document bean mapping has no type. DocIndex = {0}, docName = {1}, recordMappingIndex = {2}.
	 */
	String WFXML_BEANMAPPING_NO_TYPE = "WF_XML_VALIDATION_0057";

	/**
	 * Workflow document bean mapping has no document Id field. DocIndex = {0}, docName = {1}, recordMappingIndex = {2}.
	 */
	String WFXML_BEANMAPPING_NO_DOCIDFIELD = "WF_XML_VALIDATION_0058";

	/**
	 * Workflow document bean mapping with name already exists. DocIndex = {0}, docName = {1}, recordMappingIndex = {2}, recordMappingName = {3}.
	 */
	String WFXML_BEANMAPPING_NAME_EXIST = "WF_XML_VALIDATION_0059";

	/**
	 * Workflow document bean mapping with description already exists. DocIndex = {0}, docName = {1}, recordMappingIndex = {2}, recordMappingDesc = {3}.
	 */
	String WFXML_BEANMAPPING_DESC_EXIST = "WF_XML_VALIDATION_005A";

	/**
	 * Workflow document bean mapping has no field mappings. DocIndex = {0}, docName = {1}, recordMappingIndex = {2}, recordMappingName = {3}.
	 */
	String WFXML_BEANMAPPING_NO_FIELDMAPPINGS = "WF_XML_VALIDATION_005B";

	/**
	 * Workflow document field mapping has no document field. DocIndex = {0}, docName = {1}, recordMappingIndex = {2}, fieldMappingIndex = {3}.
	 */
	String WFXML_FIELDMAPPING_NO_DOCFIELD = "WF_XML_VALIDATION_005C";

	/**
	 * Workflow document field mapping has no bean field. DocIndex = {0}, docName = {1}, recordMappingIndex = {2}, fieldMappingIndex = {3}.
	 */
	String WFXML_FIELDMAPPING_NO_BEANFIELD = "WF_XML_VALIDATION_005D";

	/**
	 * Workflow document field mapping document field is unknown. DocIndex = {0}, docName = {1}, recordMappingIndex = {2}, fieldMappingIndex = {3}, docFieldName = {4}.
	 */
	String WFXML_FIELDMAPPING_DOCFIELD_UNKNOWN = "WF_XML_VALIDATION_005E";

	/**
	 * Workflow document field mapping with document field exists. DocIndex = {0}, docName = {1}, recordMappingIndex = {2}, fieldMappingIndex = {3}, docFieldName = {4}.
	 */
	String WFXML_FIELDMAPPING_DOCFIELD_EXIST = "WF_XML_VALIDATION_005F";

	/**
	 * Workflow template record action has no name. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}.
	 */
	String WFXML_RECORDACTION_NO_NAME = "WF_XML_VALIDATION_0060";

	/**
	 * Workflow template record action has no description. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}.
	 */
	String WFXML_RECORDACTION_NO_DESC = "WF_XML_VALIDATION_0061";

	/**
	 * Workflow template record action with name already exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}, actionName = {4}.
	 */
	String WFXML_RECORDACTION_NAME_EXIST = "WF_XML_VALIDATION_0062";

	/**
	 * Workflow template record action with description already exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}, actionDesc = {4}.
	 */
	String WFXML_RECORDACTION_DESC_EXIST = "WF_XML_VALIDATION_0063";

	/**
	 * Workflow template record action has no type. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}.
	 */
	String WFXML_RECORDACTION_NO_TYPE = "WF_XML_VALIDATION_0064";

	/**
	 * Workflow template record action has no bean mapping. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}.
	 */
	String WFXML_RECORDACTION_NO_DOCMAPPING = "WF_XML_VALIDATION_0065";

	/**
	 * Workflow template record action has unknown bean mapping. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, actionIndex = {3}, recordMappingName = {4}.
	 */
	String WFXML_RECORDACTION_DOCMAPPING_UNKNOWN = "WF_XML_VALIDATION_0066";

	/**
	 * Workflow document must have at least one bean mapping definition. Category = {0}, docIndex = {1}, docName= {2}.
	 */
	String WFXML_DOCUMENT_NO_BEANMAPPING = "WF_XML_VALIDATION_0067";

	/**
	 * Workflow document must have at least one entry bean mapping definition. Category = {0}, docIndex = {1}, docName= {2}.
	 */
	String WFXML_DOCUMENT_NO_ENTRY_BEANMAPPING = "WF_XML_VALIDATION_0068";

	/**
	 * Workflow template form privilege has no element type. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, formPrivilegeIndex = {3}.
	 */
	String WFXML_FORMPRIVILEGE_NO_ELEMENT_TYPE = "WF_XML_VALIDATION_0069";
	
	/**
	 * Workflow template form privilege has unknown tab. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, formPrivilegeIndex = {3}, fieldName = {4}.
	 */
	String WFXML_FORMPRIVILEGE_TAB_UNKNOWN = "WF_XML_VALIDATION_006A";

	/**
	 * Workflow template form privilege has unknown section. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, formPrivilegeIndex = {3}, fieldName = {4}.
	 */
	String WFXML_FORMPRIVILEGE_SECTION_UNKNOWN = "WF_XML_VALIDATION_006B";

	/**
	 * Workflow form must have at least one tab definition. Category = {0}, formIndex = {1}, formName= {2}.
	 */
	String WFXML_FORM_NO_TAB = "WF_XML_VALIDATION_006C";
	
	/**
	 * Workflow template has multiple manual steps. Category = {0}, templateIndex = {1}, templateName= {2}.
	 */
	String WFXML_TEMPLATE_MULTIPLE_MANUAL_STEPS = "WF_XML_VALIDATION_006D";

	/**
	 * Workflow category has no module code.
	 */
	String WFXML_CATEGORY_NO_MODULE = "WF_XML_VALIDATION_006E";

	/**
	 * Workflow template has no item description element. Category = {0}, templateIndex = {1}.
	 */
	String WFXML_TEMPLATE_NO_ITEM_DESC = "WF_XML_VALIDATION_006F";
	
	/**
	 * Workflow template has multiple end steps. Category = {0}, templateIndex = {1}, templateName= {2}.
	 */
	String WFXML_TEMPLATE_MULTIPLE_END_STEPS = "WF_XML_VALIDATION_0070";
	
	/**
	 * Workflow message has no name. Category = {0}, messageIndex = {1}.
	 */
	String WFXML_MESSAGE_NO_NAME = "WF_XML_VALIDATION_0071";

	/**
	 * Workflow message has no description. Category = {0}, messageIndex = {1}.
	 */
	String WFXML_MESSAGE_NO_DESC = "WF_XML_VALIDATION_0072";

	/**
	 * Workflow message has no subject. Category = {0}, messageIndex = {1}.
	 */
	String WFXML_MESSAGE_NO_SUBJECT = "WF_XML_VALIDATION_0073";

	/**
	 * Workflow message has no body. Category = {0}, messageIndex = {1}.
	 */
	String WFXML_MESSAGE_NO_BODY = "WF_XML_VALIDATION_0074";
	
	/**
	 * Workflow message with name exists. Category = {0}, messageIndex = {1}, name = {2}.
	 */
	String WFXML_MESSAGE_NAME_EXIST = "WF_XML_VALIDATION_0075";

	/**
	 * Workflow message with description exists. Category = {0}, messageIndex = {1}, description = {2}.
	 */
	String WFXML_MESSAGE_DESC_EXIST = "WF_XML_VALIDATION_0076";

	/**
	 * Workflow template enrichment has no name. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, enrichmentIndex = {3}.
	 */
	String WFXML_ENRICHMENT_NO_NAME = "WF_XML_VALIDATION_0077";

	/**
	 * Workflow template enrichment has no description. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, enrichmentIndex = {3}.
	 */
	String WFXML_ENRICHMENT_NO_DESC = "WF_XML_VALIDATION_0078";

	/**
	 * Workflow template enrichment with name already exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, enrichmentIndex = {3}, enrichmentName = {4}.
	 */
	String WFXML_ENRICHMENT_NAME_EXIST = "WF_XML_VALIDATION_0079";

	/**
	 * Workflow template enrichment with description already exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, enrichmentIndex = {3}, enrichmentDesc = {4}.
	 */
	String WFXML_ENRICHMENT_DESC_EXIST = "WF_XML_VALIDATION_007A";

	/**
	 * Workflow template enrichment has no logic. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, enrichmentIndex = {3}.
	 */
	String WFXML_ENRICHMENT_NO_LOGIC = "WF_XML_VALIDATION_007B";

	/**
	 * Workflow template policy has no name. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, policyIndex = {3}.
	 */
	String WFXML_POLICY_NO_NAME = "WF_XML_VALIDATION_007C";

	/**
	 * Workflow template policy has no description. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, policyIndex = {3}.
	 */
	String WFXML_POLICY_NO_DESC = "WF_XML_VALIDATION_007D";

	/**
	 * Workflow template policy with name already exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, policyIndex = {3}, policyName = {4}.
	 */
	String WFXML_POLICY_NAME_EXIST = "WF_XML_VALIDATION_007E";

	/**
	 * Workflow template policy with description already exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, policyIndex = {3}, policyDesc = {4}.
	 */
	String WFXML_POLICY_DESC_EXIST = "WF_XML_VALIDATION_007F";

	/**
	 * Workflow template policy has no logic. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, policyIndex = {3}.
	 */
	String WFXML_POLICY_NO_LOGIC = "WF_XML_VALIDATION_0080";

	/**
	 * Workflow template alert has no name. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, alertIndex = {3}.
	 */
	String WFXML_ALERT_NO_NAME = "WF_XML_VALIDATION_0081";

	/**
	 * Workflow template alert has no description. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, alertIndex = {3}.
	 */
	String WFXML_ALERT_NO_DESC = "WF_XML_VALIDATION_0082";

	/**
	 * Workflow template alert with name already exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, alertIndex = {3}, alertName = {4}.
	 */
	String WFXML_ALERT_NAME_EXIST = "WF_XML_VALIDATION_0083";

	/**
	 * Workflow template alert with description already exists. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, alertIndex = {3}, alertDesc = {4}.
	 */
	String WFXML_ALERT_DESC_EXIST = "WF_XML_VALIDATION_0084";

	/**
	 * Workflow template alert has no notification type. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, alertIndex = {3}.
	 */
	String WFXML_ALERT_NO_TYPE = "WF_XML_VALIDATION_0085";

	/**
	 * Workflow template alert message is unknown. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, alertIndex = {3}, message = {4}.
	 */
	String WFXML_ALERT_MESSAGE_UNKNOWN = "WF_XML_VALIDATION_0086";

	/**
	 * Workflow document field has no name. DocIndex = {0}, docName = {1}, fieldIndex = {2}.
	 */
	String WFXML_DOCFIELD_NO_NAME = "WF_XML_VALIDATION_0087";

	/**
	 * Workflow document field has no description. DocIndex = {0}, docName = {1}, fieldIndex = {2}.
	 */
	String WFXML_DOCFIELD_NO_DESC = "WF_XML_VALIDATION_0088";

	/**
	 * Workflow document field with name already exists. DocIndex = {0}, docName = {1}, fieldIndex = {2}, fieldName = {3}.
	 */
	String WFXML_DOCFIELD_NAME_EXIST = "WF_XML_VALIDATION_0089";

	/**
	 * Workflow document field with description already exists. DocIndex = {0}, docName = {1}, fieldIndex = {2}, fieldDescription = {3}.
	 */
	String WFXML_DOCFIELD_DESC_EXIST = "WF_XML_VALIDATION_008A";

	/**
	 * Workflow template step can not have enrichments. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}, stepType = {4}.
	 */
	String WFXML_STEP_ENRICHMENT_NOT_ALLOWED = "WF_XML_VALIDATION_008B";

	/**
	 * Workflow template step can not have record mappings. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}, stepType = {4}.
	 */
	String WFXML_STEP_RECORDMAPPING_NOT_ALLOWED = "WF_XML_VALIDATION_008C";

	/**
	 * Workflow template step can not have policies. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}, stepType = {4}.
	 */
	String WFXML_STEP_POLICY_NOT_ALLOWED = "WF_XML_VALIDATION_008D";

	/**
	 * Workflow template step requires at least one routing. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}, stepType = {4}.
	 */
	String WFXML_STEP_ROUTING_REQUIRED = "WF_XML_VALIDATION_008E";

	/**
	 * Workflow template step can not have routings. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}, stepType = {4}.
	 */
	String WFXML_STEP_ROUTING_NOT_ALLOWED = "WF_XML_VALIDATION_008F";

	/**
	 * Workflow template step requires at least one user action. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}, stepType = {4}.
	 */
	String WFXML_STEP_USERACTION_REQUIRED = "WF_XML_VALIDATION_0090";

	/**
	 * Workflow template step can not have user actions. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}, stepType = {4}.
	 */
	String WFXML_STEP_USERACTION_NOT_ALLOWED = "WF_XML_VALIDATION_0091";

	/**
	 * Workflow template step can not have form privileges. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}, stepType = {4}.
	 */
	String WFXML_STEP_FORMPRIVILEGE_NOT_ALLOWED = "WF_XML_VALIDATION_0092";

	/**
	 * Workflow template step requires an interaction form. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}, stepType = {4}.
	 */
	String WFXML_STEP_FORM_REQUIRED = "WF_XML_VALIDATION_0093";

	/**
	 * Workflow template step form is unknown at template level. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepName = {3}, form = {4}.
	 */
	String WFXML_STEP_FORM_UNKNOWN = "WF_XML_VALIDATION_0094";

	/**
	 * Workflow form has no name. Category = {0}, formIndex = {1}.
	 */
	String WFXML_FORM_NO_NAME = "WF_XML_VALIDATION_0095";

	/**
	 * Workflow form has no description. Category = {0}, formIndex = {1}.
	 */
	String WFXML_FORM_NO_DESC = "WF_XML_VALIDATION_0096";

	/**
	 * Workflow form with name already exists. Category = {0}, formIndex = {1}, formName= {2}.
	 */
	String WFXML_FORM_NAME_EXIST = "WF_XML_VALIDATION_0097";

	/**
	 * Workflow form with description already exists. Category = {0}, formIndex = {1}, formDesc= {2}.
	 */
	String WFXML_FORM_DESC_EXIST = "WF_XML_VALIDATION_0098";

	/**
	 * Workflow form must refer to a document. Category = {0}, formIndex = {1}, formName= {2}.
	 */
	String WFXML_FORM_NO_DOCUMENT = "WF_XML_VALIDATION_0099";

	/**
	 * Workflow form document is unknown. Category = {0}, formIndex = {1}, formName= {2}, document= {3}.
	 */
	String WFXML_FORM_DOCUMENT_UNKNOWN = "WF_XML_VALIDATION_009A";

	/**
	 * Workflow form tab has no sections. FormIndex = {0}, formName = {1}, tabIndex = {2}, sectionIndex = {3}.
	 */
	String WFXML_TAB_NO_SECTIONS = "WF_XML_VALIDATION_009B";

	/**
	 * Workflow document field has no type. DocIndex = {0}, docName = {1}, fieldIndex = {2}.
	 */
	String WFXML_DOCFIELD_NO_TYPE = "WF_XML_VALIDATION_009C";

	/**
	 * Workflow template step participant not allowed. TemplateIndex = {0}, templateName = {1}, stepIndex = {2}, stepType = {3}.
	 */
	String WFXML_STEP_PARTICIPANT_NOT_ALLOWED = "WF_XML_VALIDATION_009D";
}
