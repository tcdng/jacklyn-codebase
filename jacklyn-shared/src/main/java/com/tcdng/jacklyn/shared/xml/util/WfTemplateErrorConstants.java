/*
 * Copyright 2018-2019 The Code Department.
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
     * Workflow template routing with name already exists. Index = {0}, step = {1},
     * name = {2}.
     */
    String WFTEMPLATE_ROUTING_EXIST = "WFTMPL_001A";

    /**
     * Workflow template routing with invalid name. Index = {0}, step = {1}, name =
     * {2}.
     */
    String WFTEMPLATE_ROUTING_INVALID_NAME = "WFTMPL_001B";

    /**
     * Workflow template routing has no target. Index = {0}, step = {1}, name = {2}.
     */
    String WFTEMPLATE_ROUTING_NO_TARGET = "WFTMPL_001C";

    /**
     * Workflow template routing refers to unknown target. Index = {0}, step = {1},
     * name = {2}, target = {3}.
     */
    String WFTEMPLATE_ROUTING_TARGET_UNKNOWN = "WFTMPL_001D";

    /**
     * Workflow template routing target reference to start step not allowed. Index = {0}, step =
     * {1}, name = {2}, target = {3}.
     */
    String WFTEMPLATE_ROUTING_TARGET_START = "WFTMPL_001E";

    /**
     * Workflow template routing target reference to self step not allowed. Index = {0}, step =
     * {1}, name = {2}, target = {3}.
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
     * Workflow template enrichment with name already exists. Index = {0}, step =
     * {1}, name = {2}.
     */
    String WFTEMPLATE_ENRICHMENT_EXIST = "WFTMPL_0022";

    /**
     * Workflow template enrichment with invalid name. Index = {0}, step = {1}, name
     * = {2}.
     */
    String WFTEMPLATE_ENRICHMENT_INVALID_NAME = "WFTMPL_0023";

    /**
     * Workflow template enrichment has no logic. Index = {0}, step = {1}, name =
     * {2}.
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
     * Workflow template policy with name already exists. Index = {0}, step = {1},
     * name = {2}.
     */
    String WFTEMPLATE_POLICY_EXIST = "WFTMPL_0027";

    /**
     * Workflow template policy with invalid name. Index = {0}, step = {1}, name =
     * {2}.
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
     * Workflow template alert with name already exists. Index = {0}, step = {1},
     * name = {2}.
     */
    String WFTEMPLATE_ALERT_EXIST = "WFTMPL_002C";

    /**
     * Workflow template alert with invalid name. Index = {0}, step = {1}, name =
     * {2}.
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
     * Workflow template alert refers to unknown message. Index = {0}, step = {1},
     * name = {2}, message = {3}.
     */
    String WFTEMPLATE_ALERT_MESSAGE_UNKNOWN = "WFTMPL_0030";

    /**
     * Workflow template step has no user actions. Index = {0}, name = {1}, type =
     * {2}.
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
     * Workflow template user action with name already exists. Index = {0}, step =
     * {1}, name = {2}.
     */
    String WFTEMPLATE_USERACTION_EXIST = "WFTMPL_0034";

    /**
     * Workflow template user action with invalid name. Index = {0}, step = {1},
     * name = {2}.
     */
    String WFTEMPLATE_USERACTION_INVALID_NAME = "WFTMPL_0035";

    /**
     * Workflow template user action has no comment requirement type. Index = {0},
     * step = {1}, name = {2}.
     */
    String WFTEMPLATE_USERACTION_NO_COMMENT_TYPE = "WFTMPL_0036";

    /**
     * Workflow template user action has no target. Index = {0}, step = {1}, name =
     * {2}.
     */
    String WFTEMPLATE_USERACTION_NO_TARGET = "WFTMPL_0037";

    /**
     * Workflow template user action refers to unknown target. Index = {0}, step =
     * {1}, name = {2}, target = {3}.
     */
    String WFTEMPLATE_USERACTION_TARGET_UNKNOWN = "WFTMPL_0038";

    /**
     * Workflow template user action target refers to start step. Index = {0}, step
     * = {1}, name = {2}, target = {3}.
     */
    String WFTEMPLATE_USERACTION_TARGET_START = "WFTMPL_0039";

    /**
     * Workflow template user action target refers to self step. Index = {0}, step =
     * {1}, name = {2}, target = {3}.
     */
    String WFTEMPLATE_USERACTION_TARGET_SELF = "WFTMPL_003A";

    /**
     * Workflow template record action has no name. Index = {0}, step = {1}.
     */
    String WFTEMPLATE_RECORDACTION_NO_NAME = "WFTMPL_003B";

    /**
     * Workflow template record action has no description. Index = {0}, step = {1}.
     */
    String WFTEMPLATE_RECORDACTION_NO_DESC = "WFTMPL_003C";

    /**
     * Workflow template record action with name already exists. Index = {0}, step =
     * {1}, name = {2}.
     */
    String WFTEMPLATE_RECORDACTION_EXIST = "WFTMPL_003D";

    /**
     * Workflow template record action with invalid name. Index = {0}, step = {1},
     * name = {2}.
     */
    String WFTEMPLATE_RECORDACTION_INVALID_NAME = "WFTMPL_003E";

    /**
     * Workflow template record action has no type. Index = {0}, step = {1}, name =
     * {2}.
     */
    String WFTEMPLATE_RECORDACTION_NO_TYPE = "WFTMPL_003F";

    /**
     * Workflow template record action has no mapping. Index = {0}, step = {1}, name
     * = {2}.
     */
    String WFTEMPLATE_RECORDACTION_NO_MAPPING = "WFTMPL_0040";

    /**
     * Workflow template form privilege has no element name. Index = {0}, step =
     * {1}.
     */
    String WFTEMPLATE_FORMPRIVILEGE_NO_ELEMENT_NAME = "WFTMPL_0041";

    /**
     * Workflow template form privilege has no element type. Index = {0}, step =
     * {1}.
     */
    String WFTEMPLATE_FORMPRIVILEGE_NO_ELEMENT_TYPE = "WFTMPL_0042";

    /**
     * Workflow template step has routings. Index = {0}, step = {1}, type = {2}.
     */
    String WFTEMPLATE_STEP_ROUTINGS_EXIST = "WFTMPL_0043";

    /**
     * Workflow template step has user actions. Index = {0}, step = {1}, type = {2}.
     */
    String WFTEMPLATE_STEP_USERACTIONS_EXIST = "WFTMPL_0044";

    /**
     * Workflow template step has record actions. Index = {0}, step = {1}, type =
     * {2}.
     */
    String WFTEMPLATE_STEP_RECORDACTIONS_EXIST = "WFTMPL_0045";

    /**
     * Workflow template step has enrichments. Index = {0}, step = {1}, type = {2}.
     */
    String WFTEMPLATE_STEP_ENRICHMENTS_EXIST = "WFTMPL_0046";

    /**
     * Workflow template step has policies. Index = {0}, step = {1}, type = {2}.
     */
    String WFTEMPLATE_STEP_POLICIES_EXIST = "WFTMPL_0047";

    /**
     * Workflow template step has form privileges. Index = {0}, step = {1}, type =
     * {2}.
     */
    String WFTEMPLATE_STEP_FORMPRIVILEGES_EXIST = "WFTMPL_0048";

    /**
     * Workflow template has no version.
     */
    String WFTEMPLATE_NO_VERSION = "WFTMPL_0049";

    /**
     * Workflow template has multiple manual steps.
     */
    String WFTEMPLATE_STEP_MULTIPLE_MANUAL = "WFTMPL_004A";

    /**
     * Workflow template step has no actual participant. Index = {0}, name = {1}, type = {2}.
     */
    String WFTEMPLATE_STEP_NO_ACTUAL_PARTICIPANT = "WFTMPL_004B";

    /**
     * Workflow template alert refers to incompatible message. Index = {0}, step = {1},
     * name = {2}, message = {3}, document = {4}.
     */
    String WFTEMPLATE_ALERT_MESSAGE_INCOMPATIBLE = "WFTMPL_004C";

    /**
     * Workflow template document has no name. Index = {0}.
     */
    String WFTEMPLATE_DOC_NO_NAME = "WFTMPL_004D";

    /**
     * Workflow template document with name already exists. Index = {0}, name = {1}.
     */
    String WFTEMPLATE_DOC_EXIST = "WFTMPL_004E";

    /**
     * Workflow template document refers to unknown category document {0}.
     */
    String WFTEMPLATE_DOC_UNKNOWN_CATEGORY_DOC = "WFTMPL_004F";

    /**
     * Workflow template routing with classifier has no document reference. Index = {0}, step = {1}, classifier = {2}.
     */
    String WFTEMPLATE_ROUTING_WITH_CLASSIFIER_NO_DOC = "WFTMPL_0050";

    /**
     * Workflow template routing refers to unknown template document. Index = {0}, step = {1}, routing = {2}, document = {3}.
     */
    String WFTEMPLATE_ROUTING_UNKNOWN_TEMPLATE_DOC = "WFTMPL_0051";

    /**
     * Workflow template policy refers to unknown template document. Index = {0}, step = {1}, policy = {2}, document = {3}.
     */
    String WFTEMPLATE_POLICY_UNKNOWN_TEMPLATE_DOC = "WFTMPL_0052";

    /**
     * Workflow template enrichment refers to unknown template document. Index = {0}, step = {1}, enrichment = {2}, document = {3}.
     */
    String WFTEMPLATE_ENRICHMENT_UNKNOWN_TEMPLATE_DOC = "WFTMPL_0053";

    /**
     * Workflow template record action has no document reference. Index = {0}, step = {1}.
     */
    String WFTEMPLATE_RECORDACTION_WITH_NO_DOC = "WFTMPL_0054";

    /**
     * Workflow template record action refers to unknown template document. Index = {0}, step = {1}, record action = {2}, document = {3}.
     */
    String WFTEMPLATE_RECORDACTION_UNKNOWN_TEMPLATE_DOC = "WFTMPL_0055";

    /**
     * Workflow template record action refers to unknown category document mapping. Index = {0}, step = {1}, record action = {2}, document = {3}, mapping = {4}.
     */
    String WFTEMPLATE_RECORDACTION_UNKNOWN_CATEGORY_DOC_MAPPING = "WFTMPL_0056";

    /**
     * Workflow template routing refers to unknown category document classifier. Index = {0}, step = {1}, routing = {2}, document = {3}, classifier = {4}.
     */
    String WFTEMPLATE_ROUTING_UNKNOWN_CATEGORY_DOC_CLASSIFIER = "WFTMPL_0057";

    /**
     * Workflow template form privilege has no document reference. Index = {0}, step = {1}.
     */
    String WFTEMPLATE_FORMPRIVILEGE_WITH_NO_DOC = "WFTMPL_0058";

    /**
     * Workflow template form privilege refers to unknown template document. Index = {0}, step = {1}, record action = {2}, document = {3}.
     */
    String WFTEMPLATE_FORMPRIVILEGE_UNKNOWN_TEMPLATE_DOC = "WFTMPL_0059";

    /**
     * Workflow template alert refers to unknown template document. Index = {0}, step = {1}, alert = {2}, document = {3}.
     */
    String WFTEMPLATE_ALERT_UNKNOWN_TEMPLATE_DOC = "WFTMPL_005A";

    /**
     * Workflow template alert has no document reference. Index = {0}, step = {1}.
     */
    String WFTEMPLATE_ALERT_WITH_NO_DOC = "WFTMPL_005B";

    /**
     * Workflow template alert has no participant. Index = {0}, step = {1}, name = {2}.
     */
    String WFTEMPLATE_ALERT_NO_PARICIPANT = "WFTMPL_005C";

    /**
     * Workflow template alert has no notification channel. Index = {0}, step = {1}, name = {2}.
     */
    String WFTEMPLATE_ALERT_NO_CHANNEL = "WFTMPL_005D";

    /**
     * Workflow template has multiple error steps.
     */
    String WFTEMPLATE_STEP_MULTIPLE_ERROR = "WFTMPL_005E";

    /**
     * Workflow template has no error step.
     */
    String WFTEMPLATE_STEP_NO_ERROR = "WFTMPL_005F";

    /**
     * Workflow template routing target reference to error step not allowed. Index = {0}, step =
     * {1}, name = {2}, target = {3}.
     */
    String WFTEMPLATE_ROUTING_TARGET_ERROR = "WFTMPL_0060";

    /**
     * Workflow template user action target reference to error step not allowed. Index = {0}, step
     * = {1}, name = {2}, target = {3}.
     */
    String WFTEMPLATE_USERACTION_TARGET_ERROR = "WFTMPL_0061";

    /**
     * Workflow template step branch has no name. Index = {0}, step = {1}.
     */
    String WFTEMPLATE_STEP_BRANCH_NO_NAME = "WFTMPL_0062";

    /**
     * Workflow template step branch has no description. Index = {0}, step = {1}.
     */
    String WFTEMPLATE_STEP_BRANCH_NO_DESC = "WFTMPL_0063";

    /**
     * Workflow template step branch has no target. Index = {0}, step = {1}.
     */
    String WFTEMPLATE_STEP_BRANCH_NO_TARGET = "WFTMPL_0064";

    /**
     * Workflow template step branch with name already exists. Index = {0}, step = {1}, name = {2}.
     */
    String WFTEMPLATE_STEP_BRANCH_EXIST = "WFTMPL_0065";

    /**
     * Workflow template step branch with invalid name. Index = {0}, step = {1}, name = {2}.
     */
    String WFTEMPLATE_BRANCH_INVALID_NAME = "WFTMPL_0066";

    /**
     * Workflow template step must have at least one branch. Index = {0}, step = {1}, type = {2}.
     */
    String WFTEMPLATE_STEP_BRANCHES_NOT_EXIST = "WFTMPL_0067";

    /**
     * Workflow template step branches not allowed for type. Index = {0}, step = {1}, type = {2}.
     */
    String WFTEMPLATE_STEP_BRANCHES_EXIST = "WFTMPL_0068";

    /**
     * Workflow template step has alerts. Index = {0}, step = {1}, type = {2}.
     */
    String WFTEMPLATE_STEP_ALERTS_EXIST = "WFTMPL_0069";

    /**
     * Workflow template routing targets step in different branch. Index = {0}, step = {1}, name = {2}, target = {3}.
     */
    String WFTEMPLATE_ROUTING_TARGET_NOT_SAME_BRANCH = "WFTMPL_006A";

    /**
     * Workflow template branch with name already exists. Index = {0}, name = {1}.
     */
    String WFTEMPLATE_BRANCH_EXIST = "WFTMPL_006B";

    /**
     * Workflow template branch references an unknown target. Branch = {0}, target = {1}.
     */
    String WFTEMPLATE_BRANCH_UNKNOWN_TARGET = "WFTMPL_006C";

    /**
     * Workflow template branch references target in different branch. Branch = {0}, target = {1}, target branch = {2}.
     */
    String WFTEMPLATE_BRANCH_TARGET_DIFFERENT_BRANCH = "WFTMPL_006C";

    /**
     * Workflow template step has no origin. Index = {0}, name = {1}.
     */
    String WFTEMPLATE_STEP_NO_ORIGIN = "WFTMPL_006E";

    /**
     * Workflow template merge step origin not referring to a split step . Index = {0}, name = {1}.
     */
    String WFTEMPLATE_STEP_INVALID_ORIGIN = "WFTMPL_006F";

    /**
     * Workflow template multiple merge steps referring to the same origin split step. Index = {0}, step name = {1}, origin name = {2}.
     */
    String WFTEMPLATE_STEP_SAME_ORIGIN = "WFTMPL_0070";

    /**
     * Workflow template split step has no corresponding merge step. Split name = {0}.
     */
    String WFTEMPLATE_STEP_SPLIT_NO_MERGE = "WFTMPL_0071";

    /**
     * Workflow template routing targets step in different origin. Index = {0}, step = {1}, name = {2}, target = {3}.
     */
    String WFTEMPLATE_ROUTING_TARGET_NOT_SAME_ORIGIN = "WFTMPL_0072";
}
