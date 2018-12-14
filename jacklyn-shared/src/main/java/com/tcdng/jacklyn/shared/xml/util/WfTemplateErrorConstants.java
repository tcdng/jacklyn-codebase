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
 * Workflow template XML validation errors.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface WfTemplateErrorConstants {

	/**
	 * Workflow template has no name.
	 */
	String WFTEMPLATE_NO_NAME = "WFTMPL_0001";

	/**
	 * Workflow template has invalid name format. Name = {0}.
	 */
	String WFTEMPLATE_INVALID_NAME_FORMAT = "WFTMPL_0002";

	/**
	 * Workflow template has no description.
	 */
	String WFTEMPLATE_NO_DESC = "WFTMPL_0003";

	/**
	 * Workflow template has no document reference.
	 */
	String WFTEMPLATE_NO_DOCUMENT = "WFTMPL_0004";

	/**
	 * Workflow template message has no name. Index = {0}.
	 */
	String WFTEMPLATE_MESSAGE_NO_NAME = "WFTMPL_0005";

	/**
	 * Workflow template message has no description. Index = {0}.
	 */
	String WFTEMPLATE_MESSAGE_NO_DESC = "WFTMPL_0006";

	/**
	 * Workflow template message with name already exists. Index = {0}, name = {1}.
	 */
	String WFTEMPLATE_MESSAGE_EXIST = "WFTMPL_0007";

	/**
	 * Workflow template message with invalid name. Index = {0}, name = {1}.
	 */
	String WFTEMPLATE_MESSAGE_INVALID_NAME = "WFTMPL_0008";

	/**
	 * Workflow template message has no subject. Index = {0}, name = {1}.
	 */
	String WFTEMPLATE_MESSAGE_NO_SUBJECT = "WFTMPL_0009";

	/**
	 * Workflow template message has no body. Index = {0}, name = {1}.
	 */
	String WFTEMPLATE_MESSAGE_NO_BODY = "WFTMPL_000A";

	/**
	 * Workflow template has no steps.
	 */
	String WFTEMPLATE_NO_STEPS = "WFTMPL_000B";

	/**
	 * Workflow template step has no name. Index = {0}.
	 */
	String WFTEMPLATE_STEP_NO_NAME = "WFTMPL_000C";

	/**
	 * Workflow template step has no description. Index = {0}.
	 */
	String WFTEMPLATE_STEP_NO_DESC = "WFTMPL_000D";

	/**
	 * Workflow template step with name already exists. Index = {0}, name = {1}.
	 */
	String WFTEMPLATE_STEP_EXIST = "WFTMPL_000E";

	/**
	 * Workflow template step with invalid name. Index = {0}, name = {1}.
	 */
	String WFTEMPLATE_STEP_INVALID_NAME = "WFTMPL_000F";

	/**
	 * Workflow template step has no type. Index = {0}, name = {1}.
	 */
	String WFTEMPLATE_STEP_NO_TYPE = "WFTMPL_0010";

	/**
	 * Workflow template step has no participant. Index = {0}, name = {1}.
	 */
	String WFTEMPLATE_STEP_NO_PARTICIPANT = "WFTMPL_0011";

	/**
	 * Workflow template step has no priority. Index = {0}, name = {1}.
	 */
	String WFTEMPLATE_STEP_NO_PRIORITY = "WFTMPL_0012";

	/**
	 * Workflow template has multiple start steps.
	 */
	String WFTEMPLATE_STEP_MULTIPLE_START = "WFTMPL_0013";

	/**
	 * Workflow template has multiple end steps.
	 */
	String WFTEMPLATE_STEP_MULTIPLE_END = "WFTMPL_0014";

	/**
	 * Workflow template has no start step.
	 */
	String WFTEMPLATE_STEP_NO_START = "WFTMPL_0015";

	/**
	 * Workflow template has no end step.
	 */
	String WFTEMPLATE_STEP_NO_END = "WFTMPL_0016";

	/**
	 * Workflow template step has no routings. Index = {0}, name = {1}, type = {2}.
	 */
	String WFTEMPLATE_STEP_NO_ROUTINGS = "WFTMPL_0017";

	/**
	 * Workflow template routing has no name. Index = {0}, step = {1}.
	 */
	String WFTEMPLATE_ROUTING_NO_NAME = "WFTMPL_0018";

	/**
	 * Workflow template routing has no description. Index = {0}, step = {1}.
	 */
	String WFTEMPLATE_ROUTING_NO_DESC = "WFTMPL_0019";

	/**
	 * Workflow template routing with name already exists. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_ROUTING_EXIST = "WFTMPL_001A";

	/**
	 * Workflow template routing with invalid name. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_ROUTING_INVALID_NAME = "WFTMPL_001B";

	/**
	 * Workflow template routing has no target. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_ROUTING_NO_TARGET = "WFTMPL_001C";

	/**
	 * Workflow template routing refers to unknown target. Index = {0}, step = {1}, name = {2}, target = {3}.
	 */
	String WFTEMPLATE_ROUTING_TARGET_UNKNOWN = "WFTMPL_001D";

	/**
	 * Workflow template routing target refers to start step. Index = {0}, step = {1}, name = {2}, target = {3}.
	 */
	String WFTEMPLATE_ROUTING_TARGET_START = "WFTMPL_001E";

	/**
	 * Workflow template routing target refers to self step. Index = {0}, step = {1}, name = {2}, target = {3}.
	 */
	String WFTEMPLATE_ROUTING_TARGET_SELF = "WFTMPL_001F";

	/**
	 * Workflow template enrichment has no name. Index = {0}, step = {1}.
	 */
	String WFTEMPLATE_ENRICHMENT_NO_NAME = "WFTMPL_0020";

	/**
	 * Workflow template enrichment has no description. Index = {0}, step = {1}.
	 */
	String WFTEMPLATE_ENRICHMENT_NO_DESC = "WFTMPL_0021";

	/**
	 * Workflow template enrichment with name already exists. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_ENRICHMENT_EXIST = "WFTMPL_0022";

	/**
	 * Workflow template enrichment with invalid name. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_ENRICHMENT_INVALID_NAME = "WFTMPL_0023";

	/**
	 * Workflow template enrichment has no logic. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_ENRICHMENT_NO_LOGIC = "WFTMPL_0024";

	/**
	 * Workflow template policy has no name. Index = {0}, step = {1}.
	 */
	String WFTEMPLATE_POLICY_NO_NAME = "WFTMPL_0025";

	/**
	 * Workflow template policy has no description. Index = {0}, step = {1}.
	 */
	String WFTEMPLATE_POLICY_NO_DESC = "WFTMPL_0026";

	/**
	 * Workflow template policy with name already exists. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_POLICY_EXIST = "WFTMPL_0027";

	/**
	 * Workflow template policy with invalid name. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_POLICY_INVALID_NAME = "WFTMPL_0028";

	/**
	 * Workflow template policy has no logic. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_POLICY_NO_LOGIC = "WFTMPL_0029";

	/**
	 * Workflow template alert has no name. Index = {0}, step = {1}.
	 */
	String WFTEMPLATE_ALERT_NO_NAME = "WFTMPL_002A";

	/**
	 * Workflow template alert has no description. Index = {0}, step = {1}.
	 */
	String WFTEMPLATE_ALERT_NO_DESC = "WFTMPL_002B";

	/**
	 * Workflow template alert with name already exists. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_ALERT_EXIST = "WFTMPL_002C";

	/**
	 * Workflow template alert with invalid name. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_ALERT_INVALID_NAME = "WFTMPL_002D";

	/**
	 * Workflow template alert has no type. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_ALERT_NO_TYPE = "WFTMPL_002E";

	/**
	 * Workflow template alert has no logic. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_ALERT_NO_MESSAGE = "WFTMPL_002F";

	/**
	 * Workflow template alert refers to unknown message. Index = {0}, step = {1}, name = {2}, message = {3}.
	 */
	String WFTEMPLATE_ALERT_MESSAGE_UNKNOWN = "WFTMPL_0030";

	/**
	 * Workflow template step has no user actions. Index = {0}, name = {1}, type = {2}.
	 */
	String WFTEMPLATE_STEP_NO_USERACTIONS = "WFTMPL_0031";

	/**
	 * Workflow template user action has no name. Index = {0}, step = {1}.
	 */
	String WFTEMPLATE_USERACTION_NO_NAME = "WFTMPL_0032";

	/**
	 * Workflow template user action has no description. Index = {0}, step = {1}.
	 */
	String WFTEMPLATE_USERACTION_NO_DESC = "WFTMPL_0033";

	/**
	 * Workflow template user action with name already exists. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_USERACTION_EXIST = "WFTMPL_0034";

	/**
	 * Workflow template user action with invalid name. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_USERACTION_INVALID_NAME = "WFTMPL_0035";

	/**
	 * Workflow template user action has no notes requirement type. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_USERACTION_NO_NOTES_TYPE = "WFTMPL_0036";

	/**
	 * Workflow template user action has no target. Index = {0}, step = {1}, name = {2}.
	 */
	String WFTEMPLATE_USERACTION_NO_TARGET = "WFTMPL_0037";

	/**
	 * Workflow template user action refers to unknown target. Index = {0}, step = {1}, name = {2}, target = {3}.
	 */
	String WFTEMPLATE_USERACTION_TARGET_UNKNOWN = "WFTMPL_0038";

	/**
	 * Workflow template user action target refers to start step. Index = {0}, step = {1}, name = {2}, target = {3}.
	 */
	String WFTEMPLATE_USERACTION_TARGET_START = "WFTMPL_0039";

	/**
	 * Workflow template user action target refers to self step. Index = {0}, step = {1}, name = {2}, target = {3}.
	 */
	String WFTEMPLATE_USERACTION_TARGET_SELF = "WFTMPL_003A";
}
