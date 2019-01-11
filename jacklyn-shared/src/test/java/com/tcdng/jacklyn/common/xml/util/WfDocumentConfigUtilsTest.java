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

package com.tcdng.jacklyn.common.xml.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import com.tcdng.jacklyn.shared.workflow.WorkflowBeanMappingType;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfAttachmentConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfAttachmentsConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfBeanMappingConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfBeanMappingsConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfClassifierConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfClassifierFilterConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfClassifiersConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfComplexFieldConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocumentConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFieldConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFieldMappingConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFieldsConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormFieldConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormSectionConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormTabConfig;
import com.tcdng.jacklyn.shared.xml.util.WfDocumentConfigUtils;
import com.tcdng.jacklyn.shared.xml.util.WfDocumentErrorConstants;
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.operation.FilterConditionType;

/**
 * Workflow document configuration tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfDocumentConfigUtilsTest {

    @Test
    public void testReadWfDocumentConfig() throws Exception {
        WfDocumentConfig wfDocumentConfig = WfDocumentConfigUtils
                .readWfDocumentConfig("xml/wfcustomer-doc-custinfo.xml");
        assertNotNull(wfDocumentConfig);

        assertEquals("customerCategory.custInfo", wfDocumentConfig.getName());
        assertEquals("Customer Information", wfDocumentConfig.getDescription());
        assertEquals("1.0", wfDocumentConfig.getVersion());

        // Fields
        WfFieldsConfig wfFieldsConfig = wfDocumentConfig.getWfFieldsConfig();
        assertNotNull(wfFieldsConfig);
        List<WfFieldConfig> wfFieldConfigList = wfFieldsConfig.getWfFieldConfigList();
        assertNotNull(wfFieldConfigList);
        assertEquals(5, wfFieldConfigList.size());

        WfFieldConfig wfFieldConfig = wfFieldConfigList.get(0);
        assertNotNull(wfFieldConfig);
        assertEquals(DataType.LONG, wfFieldConfig.getDataType());
        assertEquals("id", wfFieldConfig.getName());
        assertEquals("Customer ID", wfFieldConfig.getDescription());
        assertEquals(-1, wfFieldConfig.getRepeat());

        wfFieldConfig = wfFieldConfigList.get(1);
        assertNotNull(wfFieldConfig);
        assertEquals(DataType.STRING, wfFieldConfig.getDataType());
        assertEquals("firstName", wfFieldConfig.getName());
        assertEquals("First Name", wfFieldConfig.getDescription());
        assertEquals(-1, wfFieldConfig.getRepeat());

        wfFieldConfig = wfFieldConfigList.get(2);
        assertNotNull(wfFieldConfig);
        assertEquals(DataType.STRING, wfFieldConfig.getDataType());
        assertEquals("lastName", wfFieldConfig.getName());
        assertEquals("Last Name", wfFieldConfig.getDescription());
        assertEquals(-1, wfFieldConfig.getRepeat());

        wfFieldConfig = wfFieldConfigList.get(3);
        assertNotNull(wfFieldConfig);
        assertEquals(DataType.INTEGER, wfFieldConfig.getDataType());
        assertEquals("age", wfFieldConfig.getName());
        assertEquals("Age", wfFieldConfig.getDescription());
        assertEquals(-1, wfFieldConfig.getRepeat());

        wfFieldConfig = wfFieldConfigList.get(4);
        assertNotNull(wfFieldConfig);
        assertEquals(DataType.DOUBLE, wfFieldConfig.getDataType());
        assertEquals("height", wfFieldConfig.getName());
        assertEquals("Height", wfFieldConfig.getDescription());
        assertEquals(-1, wfFieldConfig.getRepeat());

        List<WfComplexFieldConfig> wfComplexFieldConfigList = wfFieldsConfig.getWfComplexFieldConfigList();
        assertNotNull(wfComplexFieldConfigList);
        assertEquals(1, wfComplexFieldConfigList.size());

        WfComplexFieldConfig wfComplexFieldConfig = wfComplexFieldConfigList.get(0);
        assertEquals(DataType.COMPLEX, wfComplexFieldConfig.getDataType());
        assertEquals("driversLicense", wfComplexFieldConfig.getName());
        assertEquals("Driver's License", wfComplexFieldConfig.getDescription());
        assertEquals(-1, wfComplexFieldConfig.getRepeat());

        wfFieldConfigList = wfComplexFieldConfig.getWfFieldConfigList();
        assertNotNull(wfFieldConfigList);
        assertEquals(3, wfFieldConfigList.size());

        wfFieldConfig = wfFieldConfigList.get(0);
        assertNotNull(wfFieldConfig);
        assertEquals(DataType.STRING, wfFieldConfig.getDataType());
        assertEquals("licenseNo", wfFieldConfig.getName());
        assertEquals("License No.", wfFieldConfig.getDescription());
        assertEquals(-1, wfFieldConfig.getRepeat());

        wfFieldConfig = wfFieldConfigList.get(1);
        assertNotNull(wfFieldConfig);
        assertEquals(DataType.DATE, wfFieldConfig.getDataType());
        assertEquals("issueDt", wfFieldConfig.getName());
        assertEquals("Issue Date", wfFieldConfig.getDescription());
        assertEquals(-1, wfFieldConfig.getRepeat());

        wfFieldConfig = wfFieldConfigList.get(2);
        assertNotNull(wfFieldConfig);
        assertEquals(DataType.DATE, wfFieldConfig.getDataType());
        assertEquals("expiryDt", wfFieldConfig.getName());
        assertEquals("Expiry Date", wfFieldConfig.getDescription());
        assertEquals(-1, wfFieldConfig.getRepeat());

        // Classifiers
        WfClassifiersConfig wfClassifiersConfig = wfDocumentConfig.getWfClassifiersConfig();
        assertNotNull(wfClassifiersConfig);
        List<WfClassifierConfig> wfClassifierConfigList = wfClassifiersConfig.getWfClassifierConfigList();
        assertNotNull(wfClassifierConfigList);
        assertEquals(1, wfClassifierConfigList.size());

        WfClassifierConfig wfClassifierConfig = wfClassifierConfigList.get(0);
        assertNotNull(wfClassifierConfig);
        assertEquals("validAge", wfClassifierConfig.getName());
        assertEquals("Valid Age", wfClassifierConfig.getDescription());
        assertNull(wfClassifierConfig.getLogic());

        List<WfClassifierFilterConfig> wfClassifierFilterConfigList = wfClassifierConfig
                .getWfClassifierFilterConfigList();
        assertNotNull(wfClassifierFilterConfigList);
        assertEquals(1, wfClassifierFilterConfigList.size());

        WfClassifierFilterConfig wfClassifierFilterConfig = wfClassifierFilterConfigList.get(0);
        assertNotNull(wfClassifierFilterConfig);
        assertEquals("age", wfClassifierFilterConfig.getField());
        assertEquals(FilterConditionType.BETWEEN, wfClassifierFilterConfig.getOp());
        assertEquals("18", wfClassifierFilterConfig.getValue1());
        assertEquals("40", wfClassifierFilterConfig.getValue2());
        assertEquals(Boolean.FALSE, wfClassifierFilterConfig.getFieldOnly());

        // Attachments
        WfAttachmentsConfig wfAttachmentsConfig = wfDocumentConfig.getWfAttachmentsConfig();
        assertNotNull(wfAttachmentsConfig);
        List<WfAttachmentConfig> wfAttachmentConfigList = wfAttachmentsConfig.getWfAttachmentConfigList();
        assertNotNull(wfAttachmentConfigList);
        assertEquals(1, wfAttachmentConfigList.size());

        WfAttachmentConfig wfAttachmentConfig = wfAttachmentConfigList.get(0);
        assertNotNull(wfAttachmentConfig);
        assertEquals("birthCert", wfAttachmentConfig.getName());
        assertEquals("Birth Certificate", wfAttachmentConfig.getDescription());
        assertEquals("Certificate", wfAttachmentConfig.getLabel());
        assertEquals(FileAttachmentType.PDF, wfAttachmentConfig.getType());

        // Bean mappings
        WfBeanMappingsConfig wfBeanMappingsConfig = wfDocumentConfig.getWfBeanMappingsConfig();
        assertNotNull(wfBeanMappingsConfig);
        List<WfBeanMappingConfig> wfBeanMappingConfigList = wfBeanMappingsConfig.getBeanMappingList();
        assertNotNull(wfBeanMappingConfigList);
        assertEquals(1, wfBeanMappingConfigList.size());

        WfBeanMappingConfig wfBeanMappingConfig = wfBeanMappingConfigList.get(0);
        assertNotNull(wfBeanMappingConfig);
        assertEquals("custBeanMapping", wfBeanMappingConfig.getName());
        assertEquals("Customer Bean Mapping", wfBeanMappingConfig.getDescription());
        assertEquals("com.tcdng.jacklyn.test.TestCustomer", wfBeanMappingConfig.getBeanType());
        assertEquals(WorkflowBeanMappingType.PRIMARY, wfBeanMappingConfig.getType());

        List<WfFieldMappingConfig> fieldMappingList = wfBeanMappingConfig.getFieldMappingList();
        assertNotNull(fieldMappingList);
        assertEquals(4, fieldMappingList.size());

        WfFieldMappingConfig wfFieldMappingConfig = fieldMappingList.get(0);
        assertNotNull(wfFieldMappingConfig);
        assertEquals("firstName", wfFieldMappingConfig.getDocFieldName());
        assertEquals("firstName", wfFieldMappingConfig.getBeanFieldName());

        wfFieldMappingConfig = fieldMappingList.get(1);
        assertNotNull(wfFieldMappingConfig);
        assertEquals("lastName", wfFieldMappingConfig.getDocFieldName());
        assertEquals("lastName", wfFieldMappingConfig.getBeanFieldName());

        wfFieldMappingConfig = fieldMappingList.get(2);
        assertNotNull(wfFieldMappingConfig);
        assertEquals("age", wfFieldMappingConfig.getDocFieldName());
        assertEquals("age", wfFieldMappingConfig.getBeanFieldName());

        wfFieldMappingConfig = fieldMappingList.get(3);
        assertNotNull(wfFieldMappingConfig);
        assertEquals("height", wfFieldMappingConfig.getDocFieldName());
        assertEquals("height", wfFieldMappingConfig.getBeanFieldName());

        // Forms
        WfFormConfig wfFormConfig = wfDocumentConfig.getWfFormConfig();
        assertNotNull(wfFormConfig);

        List<WfFormTabConfig> wfFormTabConfigList = wfFormConfig.getWfFormTabConfigList();
        assertNotNull(wfFormTabConfigList);
        assertEquals(1, wfFormTabConfigList.size());

        WfFormTabConfig wfFormTabConfig = wfFormTabConfigList.get(0);
        assertNotNull(wfFormTabConfig);
        assertEquals("main", wfFormTabConfig.getName());
        assertEquals("Main Tab", wfFormTabConfig.getDescription());
        assertEquals("Main", wfFormTabConfig.getLabel());
        assertEquals(Boolean.TRUE, wfFormTabConfig.getPseudo());

        List<WfFormSectionConfig> wfFormSectionConfigList = wfFormTabConfig.getWfFormSectionConfigList();
        assertNotNull(wfFormSectionConfigList);
        assertEquals(2, wfFormSectionConfigList.size());

        WfFormSectionConfig wfFormSectionConfig = wfFormSectionConfigList.get(0);
        assertNotNull(wfFormSectionConfig);
        assertEquals("basicDetails", wfFormSectionConfig.getName());
        assertEquals("Basic Details", wfFormSectionConfig.getDescription());
        assertNull(wfFormSectionConfig.getLabel());
        assertNull(wfFormSectionConfig.getBinding());

        List<WfFormFieldConfig> wfFormFieldConfigList = wfFormSectionConfig.getWfFormFieldConfigList();
        assertNotNull(wfFormFieldConfigList);
        assertEquals(4, wfFormFieldConfigList.size());

        WfFormFieldConfig wfFormFieldConfig = wfFormFieldConfigList.get(0);
        assertNotNull(wfFormFieldConfig);
        assertEquals("firstName", wfFormFieldConfig.getBinding());
        assertEquals("!ui-text", wfFormFieldConfig.getEditorUpl());
        assertNull(wfFormFieldConfig.getLabel());
        assertEquals(Boolean.TRUE, wfFormFieldConfig.getRequired());

        wfFormFieldConfig = wfFormFieldConfigList.get(1);
        assertNotNull(wfFormFieldConfig);
        assertEquals("lastName", wfFormFieldConfig.getBinding());
        assertEquals("!ui-text", wfFormFieldConfig.getEditorUpl());
        assertNull(wfFormFieldConfig.getLabel());
        assertEquals(Boolean.TRUE, wfFormFieldConfig.getRequired());

        wfFormFieldConfig = wfFormFieldConfigList.get(2);
        assertNotNull(wfFormFieldConfig);
        assertEquals("age", wfFormFieldConfig.getBinding());
        assertEquals("!ui-integer", wfFormFieldConfig.getEditorUpl());
        assertNull(wfFormFieldConfig.getLabel());
        assertEquals(Boolean.TRUE, wfFormFieldConfig.getRequired());

        wfFormFieldConfig = wfFormFieldConfigList.get(3);
        assertNotNull(wfFormFieldConfig);
        assertEquals("height", wfFormFieldConfig.getBinding());
        assertEquals("!ui-decimal", wfFormFieldConfig.getEditorUpl());
        assertNull(wfFormFieldConfig.getLabel());
        assertEquals(Boolean.TRUE, wfFormFieldConfig.getRequired());

        wfFormSectionConfig = wfFormSectionConfigList.get(1);
        assertNotNull(wfFormSectionConfig);
        assertEquals("licenseDetails", wfFormSectionConfig.getName());
        assertEquals("License Details", wfFormSectionConfig.getDescription());
        assertNull(wfFormSectionConfig.getLabel());
        assertEquals("driversLicense", wfFormSectionConfig.getBinding());

        wfFormFieldConfigList = wfFormSectionConfig.getWfFormFieldConfigList();
        assertNotNull(wfFormFieldConfigList);
        assertEquals(3, wfFormFieldConfigList.size());

        wfFormFieldConfig = wfFormFieldConfigList.get(0);
        assertNotNull(wfFormFieldConfig);
        assertEquals("licenseNo", wfFormFieldConfig.getBinding());
        assertEquals("!ui-text", wfFormFieldConfig.getEditorUpl());
        assertEquals("License No.", wfFormFieldConfig.getLabel());
        assertEquals(Boolean.TRUE, wfFormFieldConfig.getRequired());

        wfFormFieldConfig = wfFormFieldConfigList.get(1);
        assertNotNull(wfFormFieldConfig);
        assertEquals("issueDt", wfFormFieldConfig.getBinding());
        assertEquals("!ui-date", wfFormFieldConfig.getEditorUpl());
        assertEquals("Issue Date", wfFormFieldConfig.getLabel());
        assertEquals(Boolean.TRUE, wfFormFieldConfig.getRequired());

        wfFormFieldConfig = wfFormFieldConfigList.get(2);
        assertNotNull(wfFormFieldConfig);
        assertEquals("expiryDt", wfFormFieldConfig.getBinding());
        assertEquals("!ui-date", wfFormFieldConfig.getEditorUpl());
        assertEquals("Expiry Date", wfFormFieldConfig.getLabel());
        assertEquals(Boolean.TRUE, wfFormFieldConfig.getRequired());
    }

    @Test
    public void testValidateWfDocumentConfig() throws Exception {
        WfDocumentConfig wfDocumentConfig = WfDocumentConfigUtils
                .readWfDocumentConfig("xml/wfcustomer-doc-custinfo.xml");
        List<UnifyError> errorList = WfDocumentConfigUtils.validate(wfDocumentConfig);
        assertNotNull(errorList);
        assertEquals(0, errorList.size());
    }

    @Test
    public void testValidateWfDocumentConfigWithErrors() throws Exception {
        WfDocumentConfig wfDocumentConfig = WfDocumentConfigUtils
                .readWfDocumentConfig("xml/wfcustomer-doc-custinfo-err.xml");
        List<UnifyError> errorList = WfDocumentConfigUtils.validate(wfDocumentConfig);
        assertNotNull(errorList);
        assertEquals(21, errorList.size());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_NO_NAME, errorList.get(0).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_FIELD_NO_DESC, errorList.get(1).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_FIELD_NO_NAME, errorList.get(2).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_FIELD_NO_TYPE, errorList.get(3).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_FIELD_EXIST, errorList.get(4).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_FIELD_NO_TYPE, errorList.get(5).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIERFILTER_UNKNOWN_FIELD,
                errorList.get(6).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIER_EXIST, errorList.get(7).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIERFILTER_UNKNOWN_FIELD,
                errorList.get(8).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIERFILTER_NO_OP, errorList.get(9).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIER_NO_NAME, errorList.get(10).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIER_NO_FILTER_LOGIC, errorList.get(11).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_ATTACHMENT_EXIST, errorList.get(12).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_ATTACHMENT_NO_TYPE, errorList.get(13).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_ATTACHMENT_NO_NAME, errorList.get(14).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_ATTACHMENT_NO_TYPE, errorList.get(15).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_FIELDMAPPING_UNKNOWN_FIELD, errorList.get(16).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_FIELDMAPPING_UNKNOWN_FIELD, errorList.get(17).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_FORMFIELD_UNKNOWN_FIELD, errorList.get(18).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_FORMFIELD_NO_EDITOR, errorList.get(19).getErrorCode());
        assertEquals(WfDocumentErrorConstants.WFDOCUMENT_FORMFIELD_NOT_COMPLEX_FIELD, errorList.get(20).getErrorCode());
    }
}
