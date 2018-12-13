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

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.shared.xml.config.workflow.WfAttachmentConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfBeanMappingConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfClassifierConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfClassifierFilterConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfComplexFieldConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfDocumentConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFieldConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFieldMappingConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormFieldConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormSectionConfig;
import com.tcdng.jacklyn.shared.xml.config.workflow.WfFormTabConfig;
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.XMLConfigUtils;

/**
 * Workflow document XML configuration utilities.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public final class WfDocumentConfigUtils {

	private WfDocumentConfigUtils() {

	}

	public static WfDocumentConfig readWfDocumentConfig(File file) throws Exception {
		return XMLConfigUtils.readXmlConfig(WfDocumentConfig.class, file);
	}

	public static WfDocumentConfig readWfDocumentConfig(InputStream in) throws Exception {
		try {
			return XMLConfigUtils.readXmlConfig(WfDocumentConfig.class, in);
		} finally {
			IOUtils.close(in);
		}
	}

	public static WfDocumentConfig readWfDocumentConfig(Reader reader) throws Exception {
		try {
			return XMLConfigUtils.readXmlConfig(WfDocumentConfig.class, reader);
		} finally {
			IOUtils.close(reader);
		}
	}

	public static WfDocumentConfig readWfDocumentConfig(String resourceName) throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = IOUtils.openClassLoaderResourceInputStream(resourceName);
			return XMLConfigUtils.readXmlConfig(WfDocumentConfig.class, inputStream);
		} finally {
			IOUtils.close(inputStream);
		}
	}

	public static List<UnifyError> validate(WfDocumentConfig wfDocumentConfig) throws UnifyException {
		WfDocumentValidationContext ctx = new WfDocumentValidationContext();
		// Document name and description
		String name = wfDocumentConfig.getName();
		if (StringUtils.isBlank(name)) {
			ctx.addError(WfDocumentErrorConstants.WFDOCUMENT_NO_NAME);
		} else {
			String[] names = StringUtils.dotSplit(name);
			if (names.length != 2 || !WfNameUtils.isValidName(names[0]) || !WfNameUtils.isValidName(names[1])) {
				ctx.addError(WfDocumentErrorConstants.WFDOCUMENT_INVALID_NAME_FORMAT, name);
			}
		}

		if (StringUtils.isBlank(wfDocumentConfig.getDescription())) {
			ctx.addError(WfDocumentErrorConstants.WFDOCUMENT_NO_DESC);
		}

		// Fields
		if (wfDocumentConfig.getWfFieldsConfig() == null
				|| (DataUtils.isBlank(wfDocumentConfig.getWfFieldsConfig().getWfFieldConfigList())
						&& DataUtils.isBlank(wfDocumentConfig.getWfFieldsConfig().getWfComplexFieldConfigList()))) {
			ctx.addError(WfDocumentErrorConstants.WFDOCUMENT_NO_FIELDS);
		} else {
			if (!DataUtils.isBlank(wfDocumentConfig.getWfFieldsConfig().getWfFieldConfigList())) {
				for (WfFieldConfig wfFieldConfig : wfDocumentConfig.getWfFieldsConfig().getWfFieldConfigList()) {
					ctx.addField(wfFieldConfig, null);
				}
			}

			if (!DataUtils.isBlank(wfDocumentConfig.getWfFieldsConfig().getWfComplexFieldConfigList())) {
				for (WfComplexFieldConfig wfComplexFieldConfig : wfDocumentConfig.getWfFieldsConfig()
						.getWfComplexFieldConfigList()) {
					ctx.addField(wfComplexFieldConfig, null);
					if (DataUtils.isBlank(wfComplexFieldConfig.getWfFieldConfigList())) {
						ctx.addError(WfDocumentErrorConstants.WFDOCUMENT_COMPLEX_NO_FIELDS, ctx.getFieldCounter(),
								wfComplexFieldConfig.getName());
					} else {
						for (WfFieldConfig wfFieldConfig : wfComplexFieldConfig.getWfFieldConfigList()) {
							ctx.addField(wfFieldConfig, wfComplexFieldConfig.getName());
						}
					}
				}
			}
		}

		// Classifiers
		if (wfDocumentConfig.getWfClassifiersConfig() != null
				&& !DataUtils.isBlank(wfDocumentConfig.getWfClassifiersConfig().getWfClassifierConfigList())) {
			for (WfClassifierConfig wfClassifierConfig : wfDocumentConfig.getWfClassifiersConfig()
					.getWfClassifierConfigList()) {
				ctx.addClassifier(wfClassifierConfig);
			}
		}

		// Attachments
		if (wfDocumentConfig.getWfAttachmentsConfig() != null
				&& !DataUtils.isBlank(wfDocumentConfig.getWfAttachmentsConfig().getWfAttachmentConfigList())) {
			for (WfAttachmentConfig wfAttachmentConfig : wfDocumentConfig.getWfAttachmentsConfig()
					.getWfAttachmentConfigList()) {
				ctx.addAttachment(wfAttachmentConfig);
			}
		}

		// Bean mappings
		if (wfDocumentConfig.getWfBeanMappingsConfig() != null
				&& !DataUtils.isBlank(wfDocumentConfig.getWfBeanMappingsConfig().getBeanMappingList())) {
			for (WfBeanMappingConfig wfBeanMappingConfig : wfDocumentConfig.getWfBeanMappingsConfig()
					.getBeanMappingList()) {
				ctx.addBeanMapping(wfBeanMappingConfig);
			}
		}

		// Form
		if (wfDocumentConfig.getWfFormConfig() != null) {
			if (DataUtils.isBlank(wfDocumentConfig.getWfFormConfig().getWfFormTabConfigList())) {
				ctx.addError(WfDocumentErrorConstants.WFDOCUMENT_FORM_NO_TABS);
			} else {
				for (WfFormTabConfig wfFormTabConfig : wfDocumentConfig.getWfFormConfig().getWfFormTabConfigList()) {
					ctx.addTab(wfFormTabConfig);
				}
			}
		}
		return ctx.getErrorList();
	}

	private static class WfDocumentValidationContext {

		private List<UnifyError> errorList;

		private Map<String, WfFieldConfigInfo> wfFieldConfigs;

		private Set<String> wfClassifierConfigs;

		private Set<String> wfAttachmentConfigs;

		private Set<String> wfBeanMappingConfigs;

		private Set<String> wfTabConfigs;

		private Set<String> wfSectionConfigs;

		private int fieldCounter;

		private int classifierCounter;

		private int attachmentCounter;

		private int beanMappingCounter;

		private int tabCounter;

		private int sectionCounter;

		public WfDocumentValidationContext() {
			errorList = new ArrayList<UnifyError>();
			wfFieldConfigs = new HashMap<String, WfFieldConfigInfo>();
			wfClassifierConfigs = new HashSet<String>();
			wfAttachmentConfigs = new HashSet<String>();
			wfBeanMappingConfigs = new HashSet<String>();
			wfTabConfigs = new HashSet<String>();
			wfSectionConfigs = new HashSet<String>();
		}

		public void addError(String errorCode, Object... params) {
			errorList.add(new UnifyError(errorCode, params));
		}

		public void addField(WfFieldConfig wfFieldConfig, String parentName) {
			String name = wfFieldConfig.getName();
			if (!StringUtils.isBlank(name)) {
				if (!WfNameUtils.isValidName(name)) {
					addError(WfDocumentErrorConstants.WFDOCUMENT_FIELD_INVALID_NAME, fieldCounter, name);
				}

				if (wfFieldConfigs.containsKey(name)) {
					addError(WfDocumentErrorConstants.WFDOCUMENT_FIELD_EXIST, fieldCounter, name);
				} else {
					wfFieldConfigs.put(name, new WfFieldConfigInfo(wfFieldConfig, parentName));
				}
			} else {
				addError(WfDocumentErrorConstants.WFDOCUMENT_FIELD_NO_NAME, fieldCounter);
			}

			if (StringUtils.isBlank(wfFieldConfig.getDescription())) {
				addError(WfDocumentErrorConstants.WFDOCUMENT_FIELD_NO_DESC, fieldCounter);
			}

			if (wfFieldConfig.getDataType() == null) {
				addError(WfDocumentErrorConstants.WFDOCUMENT_FIELD_NO_TYPE, fieldCounter, name);
			} else if (DataType.COMPLEX.equals(wfFieldConfig.getDataType())) {
				if (!(wfFieldConfig instanceof WfComplexFieldConfig)) {
					addError(WfDocumentErrorConstants.WFDOCUMENT_FIELD_INVALID_DATATYPE, fieldCounter, name,
							wfFieldConfig.getDataType());
				}
			}

			fieldCounter++;
		}

		public void addClassifier(WfClassifierConfig wfClassifierConfig) {
			String name = wfClassifierConfig.getName();
			if (!StringUtils.isBlank(name)) {
				if (!WfNameUtils.isValidName(name)) {
					addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIER_INVALID_NAME, classifierCounter, name);
				}

				if (wfClassifierConfigs.contains(name)) {
					addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIER_EXIST, classifierCounter, name);
				} else {
					wfClassifierConfigs.add(name);
				}
			} else {
				addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIER_NO_NAME, classifierCounter);
			}

			if (StringUtils.isBlank(wfClassifierConfig.getDescription())) {
				addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIER_NO_DESC, classifierCounter);
			}

			if (StringUtils.isBlank(wfClassifierConfig.getLogic())
					&& DataUtils.isBlank(wfClassifierConfig.getWfClassifierFilterConfigList())) {
				addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIER_NO_FILTER_LOGIC, classifierCounter, name);
			}

			if (!StringUtils.isBlank(wfClassifierConfig.getLogic())
					&& !DataUtils.isBlank(wfClassifierConfig.getWfClassifierFilterConfigList())) {
				addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIER_HAS_FILTER_LOGIC, classifierCounter, name);
			}

			if (!DataUtils.isBlank(wfClassifierConfig.getWfClassifierFilterConfigList())) {
				int index = 0;
				for (WfClassifierFilterConfig wfClassifierFilterConfig : wfClassifierConfig
						.getWfClassifierFilterConfigList()) {
					if (StringUtils.isBlank(wfClassifierFilterConfig.getField())) {
						addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIERFILTER_NO_FIELD, index, name);
					} else {
						validateClassifierFilterFieldRef(wfClassifierFilterConfig.getField(), name, index);
					}

					if (wfClassifierFilterConfig.getOp() == null) {
						addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIERFILTER_NO_OP, index, name);
					}

					if (Boolean.TRUE.equals(wfClassifierFilterConfig.getFieldOnly())) {
						validateClassifierFilterFieldRef(wfClassifierFilterConfig.getValue1(), name, index);
						validateClassifierFilterFieldRef(wfClassifierFilterConfig.getValue2(), name, index);
					}
					index++;
				}
			}
			classifierCounter++;
		}

		public void addAttachment(WfAttachmentConfig wfAttachmentConfig) {
			String name = wfAttachmentConfig.getName();
			if (!StringUtils.isBlank(name)) {
				if (!WfNameUtils.isValidName(name)) {
					addError(WfDocumentErrorConstants.WFDOCUMENT_ATTACHMENT_INVALID_NAME, attachmentCounter, name);
				}

				if (wfAttachmentConfigs.contains(name)) {
					addError(WfDocumentErrorConstants.WFDOCUMENT_ATTACHMENT_EXIST, attachmentCounter, name);
				} else {
					wfAttachmentConfigs.add(name);
				}
			} else {
				addError(WfDocumentErrorConstants.WFDOCUMENT_ATTACHMENT_NO_NAME, attachmentCounter);
			}

			if (StringUtils.isBlank(wfAttachmentConfig.getDescription())) {
				addError(WfDocumentErrorConstants.WFDOCUMENT_ATTACHMENT_NO_DESC, attachmentCounter);
			}

			if (wfAttachmentConfig.getType() == null) {
				addError(WfDocumentErrorConstants.WFDOCUMENT_ATTACHMENT_NO_TYPE, attachmentCounter, name);
			}

			attachmentCounter++;
		}

		public void addBeanMapping(WfBeanMappingConfig wfBeanMappingConfig) {
			String name = wfBeanMappingConfig.getName();
			if (!StringUtils.isBlank(name)) {
				if (!WfNameUtils.isValidName(name)) {
					addError(WfDocumentErrorConstants.WFDOCUMENT_BEANMAPPING_INVALID_NAME, beanMappingCounter, name);
				}

				if (wfBeanMappingConfigs.contains(name)) {
					addError(WfDocumentErrorConstants.WFDOCUMENT_BEANMAPPING_EXIST, beanMappingCounter, name);
				} else {
					wfBeanMappingConfigs.add(name);
				}
			} else {
				addError(WfDocumentErrorConstants.WFDOCUMENT_BEANMAPPING_NO_NAME, beanMappingCounter);
			}

			if (StringUtils.isBlank(wfBeanMappingConfig.getDescription())) {
				addError(WfDocumentErrorConstants.WFDOCUMENT_BEANMAPPING_NO_DESC, beanMappingCounter);
			}

			if (wfBeanMappingConfig.getType() == null) {
				addError(WfDocumentErrorConstants.WFDOCUMENT_BEANMAPPING_NO_TYPE, beanMappingCounter, name);
			}

			if (wfBeanMappingConfig.getBeanType() == null) {
				addError(WfDocumentErrorConstants.WFDOCUMENT_BEANMAPPING_NO_BEANTYPE, beanMappingCounter, name);
			}

			if (DataUtils.isBlank(wfBeanMappingConfig.getFieldMappingList())) {
				addError(WfDocumentErrorConstants.WFDOCUMENT_BEANMAPPING_NO_MAPPINGS, beanMappingCounter, name);
			} else {
				int index = 0;
				for (WfFieldMappingConfig wfFieldMappingConfig : wfBeanMappingConfig.getFieldMappingList()) {
					if (StringUtils.isBlank(wfFieldMappingConfig.getDocFieldName())) {
						addError(WfDocumentErrorConstants.WFDOCUMENT_FIELDMAPPING_NO_DOCFIELD, index, name);
					} else {
						if (!wfFieldConfigs.containsKey(wfFieldMappingConfig.getDocFieldName())) {
							addError(WfDocumentErrorConstants.WFDOCUMENT_FIELDMAPPING_UNKNOWN_FIELD, index, name,
									wfFieldMappingConfig.getDocFieldName());
						}
					}

					if (StringUtils.isBlank(wfFieldMappingConfig.getBeanFieldName())) {
						addError(WfDocumentErrorConstants.WFDOCUMENT_FIELDMAPPING_NO_BEANFIELD, index, name);
					}

					index++;
				}
			}
			beanMappingCounter++;
		}

		public void addTab(WfFormTabConfig wfFormTabConfig) {
			String name = wfFormTabConfig.getName();
			if (!StringUtils.isBlank(name)) {
				if (!WfNameUtils.isValidName(name)) {
					addError(WfDocumentErrorConstants.WFDOCUMENT_TAB_INVALID_NAME, tabCounter, name);
				}

				if (wfTabConfigs.contains(name)) {
					addError(WfDocumentErrorConstants.WFDOCUMENT_TAB_EXIST, tabCounter, name);
				} else {
					wfTabConfigs.add(name);
				}
			} else {
				addError(WfDocumentErrorConstants.WFDOCUMENT_TAB_NO_NAME, tabCounter);
			}

			if (StringUtils.isBlank(wfFormTabConfig.getDescription())) {
				addError(WfDocumentErrorConstants.WFDOCUMENT_TAB_NO_DESC, tabCounter);
			}

			if (DataUtils.isBlank(wfFormTabConfig.getWfFormSectionConfigList())) {
				addError(WfDocumentErrorConstants.WFDOCUMENT_TAB_NO_SECTIONS, tabCounter, name);
			} else {
				for (WfFormSectionConfig wfFormSectionConfig : wfFormTabConfig.getWfFormSectionConfigList()) {
					String sectionName = wfFormSectionConfig.getName();
					if (!StringUtils.isBlank(sectionName)) {
						if (!WfNameUtils.isValidName(sectionName)) {
							addError(WfDocumentErrorConstants.WFDOCUMENT_SECTION_INVALID_NAME, sectionCounter,
									sectionName);
						}

						if (wfSectionConfigs.contains(sectionName)) {
							addError(WfDocumentErrorConstants.WFDOCUMENT_SECTION_EXIST, sectionCounter, sectionName);
						} else {
							wfSectionConfigs.add(sectionName);
						}
					} else {
						addError(WfDocumentErrorConstants.WFDOCUMENT_SECTION_NO_NAME, sectionCounter);
					}

					if (StringUtils.isBlank(wfFormSectionConfig.getDescription())) {
						addError(WfDocumentErrorConstants.WFDOCUMENT_SECTION_NO_DESC, sectionCounter);
					}

					boolean isComplexBinding = !StringUtils.isBlank(wfFormSectionConfig.getBinding());
					if (isComplexBinding) {
						WfFieldConfigInfo wfFieldConfigInfo = wfFieldConfigs.get(wfFormSectionConfig.getBinding());
						if (wfFieldConfigInfo == null || !wfFieldConfigInfo.isComplex()) {
							addError(WfDocumentErrorConstants.WFDOCUMENT_SECTION_UNKNOWN_COMPLEX, sectionCounter,
									sectionName, wfFormSectionConfig.getBinding());
						}
					}

					Set<String> bindings = new HashSet<String>();
					if (DataUtils.isBlank(wfFormSectionConfig.getWfFormFieldConfigList())) {
						addError(WfDocumentErrorConstants.WFDOCUMENT_SECTION_NO_FIELDS, sectionCounter, sectionName);
					} else {
						int index = 0;
						for (WfFormFieldConfig wfFormFieldConfig : wfFormSectionConfig.getWfFormFieldConfigList()) {
							String binding = wfFormFieldConfig.getBinding();
							if (bindings.contains(binding)) {
								addError(WfDocumentErrorConstants.WFDOCUMENT_FORMFIELD_MULTIPLE_BINDING, index,
										sectionName, binding);
							} else {
								bindings.add(binding);
							}

							if (StringUtils.isBlank(binding)) {
								addError(WfDocumentErrorConstants.WFDOCUMENT_FORMFIELD_NO_BINDING, index, sectionName);
							} else {
								WfFieldConfigInfo wfFieldConfigInfo = wfFieldConfigs.get(binding);
								if (wfFieldConfigInfo == null) {
									addError(WfDocumentErrorConstants.WFDOCUMENT_FORMFIELD_UNKNOWN_FIELD, index,
											sectionName, binding);
								} else {
									if (isComplexBinding) {
										if (!wfFormSectionConfig.getBinding()
												.equals(wfFieldConfigInfo.getParentName())) {
											addError(WfDocumentErrorConstants.WFDOCUMENT_FORMFIELD_NOT_COMPLEX_FIELD,
													index, sectionName, binding);
										}
									}
								}
							}

							if (StringUtils.isBlank(wfFormFieldConfig.getEditorUpl())) {
								addError(WfDocumentErrorConstants.WFDOCUMENT_FORMFIELD_NO_EDITOR, index, sectionName,
										binding);
							}
						}
					}
					sectionCounter++;
				}
			}

			tabCounter++;
		}

		private void validateClassifierFilterFieldRef(String fieldName, String name, int index) {
			WfFieldConfigInfo wfFieldConfigInfo = wfFieldConfigs.get(fieldName);
			if (wfFieldConfigInfo == null) {
				addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIERFILTER_UNKNOWN_FIELD, index, name, fieldName);
			} else if (DataType.COMPLEX.equals(wfFieldConfigInfo.getWfFieldConfig().getDataType())) {
				addError(WfDocumentErrorConstants.WFDOCUMENT_CLASSIFIERFILTER_COMPLEX_FIELD, index, name, fieldName);
			}
		}

		public List<UnifyError> getErrorList() {
			return errorList;
		}

		public int getFieldCounter() {
			return fieldCounter;
		}
	}

	private static class WfFieldConfigInfo {

		private WfFieldConfig wfFieldConfig;

		private String parentName;

		public WfFieldConfigInfo(WfFieldConfig wfFieldConfig, String parentName) {
			this.wfFieldConfig = wfFieldConfig;
			this.parentName = parentName;
		}

		public WfFieldConfig getWfFieldConfig() {
			return wfFieldConfig;
		}

		public String getParentName() {
			return parentName;
		}

		public boolean isComplex() {
			return DataType.COMPLEX.equals(wfFieldConfig.getDataType());
		}
	}
}
