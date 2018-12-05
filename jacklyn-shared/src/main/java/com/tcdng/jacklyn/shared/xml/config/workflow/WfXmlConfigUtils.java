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

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepType;
import com.tcdng.jacklyn.shared.xml.config.BaseConfig;
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.XMLConfigUtils;

/**
 * Workflow XML configuration utilities.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public final class WfXmlConfigUtils {

	private WfXmlConfigUtils() {

	}

	public static WfCategoryConfig readXmlWfCategoryConfig(String xmlSrcObject)
			throws UnifyException {
		return XMLConfigUtils.readXmlConfig(WfCategoryConfig.class, xmlSrcObject);
	}

	public static WfCategoryConfig readXmlWfCategoryConfig(InputStream xmlSrcObject)
			throws UnifyException {
		return XMLConfigUtils.readXmlConfig(WfCategoryConfig.class, xmlSrcObject);
	}

	public static WfCategoryConfig readXmlWfCategoryConfig(Reader xmlSrcObject)
			throws UnifyException {
		return XMLConfigUtils.readXmlConfig(WfCategoryConfig.class, xmlSrcObject);
	}

	public static List<UnifyError> validateWfCategoryConfig(WfCategoryConfig wfCategoryConfig)
			throws UnifyException {
		return WfXmlConfigUtils.validateWfCategoryConfig(null, wfCategoryConfig);
	}

	public static List<UnifyError> validateWfCategoryConfig(TaskMonitor taskMonitor,
			WfCategoryConfig wfCategoryConfig) throws UnifyException {
		List<UnifyError> errorList = new ArrayList<UnifyError>();
		// Documents
		WfDocValidationContext wfDocValidationContext
				= new WfDocValidationContext(taskMonitor, wfCategoryConfig.getName());
		if (StringUtils.isBlank(wfCategoryConfig.getName())) {
			wfDocValidationContext.addError(WfXmlValidationErrorConstants.WFXML_CATEGORY_NO_NAME);
		}

		if (StringUtils.isBlank(wfCategoryConfig.getDescription())) {
			wfDocValidationContext.addError(WfXmlValidationErrorConstants.WFXML_CATEGORY_NO_DESC);
		}

		WfDocsConfig wfDocsConfig = wfCategoryConfig.getWfDocsConfig();
		if (wfDocsConfig == null || DataUtils.isBlank(wfDocsConfig.getWfDocConfigList())) {
			wfDocValidationContext.addError(WfXmlValidationErrorConstants.WFXML_CATEGORY_NO_DOCS,
					wfCategoryConfig.getName());
		} else {
			for (WfDocConfig wfDocConfig : wfDocsConfig.getWfDocConfigList()) {
				WfXmlConfigUtils.validateWfDocConfig(wfDocValidationContext, wfDocConfig);
			}
		}

		// Forms
		WfFormValidationContext wfFormValidationContext = new WfFormValidationContext(taskMonitor,
				wfCategoryConfig.getName(), wfDocValidationContext);
		if (wfCategoryConfig.getWfFormsConfig() != null
				&& !DataUtils.isBlank(wfCategoryConfig.getWfFormsConfig().getWfFormConfigList())) {
			for (WfFormConfig wfFormConfig : wfCategoryConfig.getWfFormsConfig()
					.getWfFormConfigList()) {
				WfXmlConfigUtils.validateWfFormConfig(wfFormValidationContext, wfFormConfig);
			}
		}

		// Messages
		WfMessageValidationContext wfMessageValidationContext
				= new WfMessageValidationContext(taskMonitor, wfCategoryConfig.getName());
		if (wfCategoryConfig.getWfMessagesConfig() != null && !DataUtils
				.isBlank(wfCategoryConfig.getWfMessagesConfig().getWfMessageConfigList())) {
			for (WfMessageConfig wfMessageConfig : wfCategoryConfig.getWfMessagesConfig()
					.getWfMessageConfigList()) {
				WfXmlConfigUtils.validateWfMessageConfig(wfMessageValidationContext,
						wfMessageConfig);
			}
		}

		// Templates
		WfTemplateValidationContext wfTemplateValidationContext
				= new WfTemplateValidationContext(taskMonitor, wfCategoryConfig.getName(),
						wfFormValidationContext, wfMessageValidationContext.getMessages());
		WfTemplatesConfig wfTemplatesConfig = wfCategoryConfig.getWfTemplatesConfig();
		if (wfTemplatesConfig == null
				|| DataUtils.isBlank(wfTemplatesConfig.getWfTemplateConfigList())) {
			wfDocValidationContext.addError(
					WfXmlValidationErrorConstants.WFXML_CATEGORY_NO_TEMPLATES,
					wfCategoryConfig.getName());
		} else {
			for (WfTemplateConfig wfTemplateConfig : wfTemplatesConfig.getWfTemplateConfigList()) {
				WfXmlConfigUtils.validateWfTemplateConfig(wfTemplateValidationContext,
						wfTemplateConfig);
			}
		}

		errorList.addAll(wfDocValidationContext.getErrorList());
		errorList.addAll(wfFormValidationContext.getErrorList());
		errorList.addAll(wfMessageValidationContext.getErrorList());
		errorList.addAll(wfTemplateValidationContext.getErrorList());

		return errorList;
	}

	private static void validateWfDocConfig(WfDocValidationContext wfDocValidationContext,
			WfDocConfig wfDocConfig) throws UnifyException {
		wfDocValidationContext.nextWfDocConfig(wfDocConfig);
		if (StringUtils.isBlank(wfDocConfig.getName())) {
			wfDocValidationContext
					.addDocError(WfXmlValidationErrorConstants.WFXML_DOCUMENT_NO_NAME);
		}

		if (StringUtils.isBlank(wfDocConfig.getDescription())) {
			wfDocValidationContext
					.addDocError(WfXmlValidationErrorConstants.WFXML_DOCUMENT_NO_DESC);
		}

		int match = wfDocValidationContext.matchWfDocConfig(wfDocConfig);
		if ((match & WfDocValidationContext.NAME_FLAG) != 0) {
			wfDocValidationContext.addDocError(
					WfXmlValidationErrorConstants.WFXML_DOCUMENT_NAME_EXIST, wfDocConfig.getName());
		}

		if ((match & WfDocValidationContext.DESC_FLAG) != 0) {
			wfDocValidationContext.addDocError(
					WfXmlValidationErrorConstants.WFXML_DOCUMENT_DESC_EXIST,
					wfDocConfig.getDescription());
		}

		// Fields
		if (wfDocConfig.getWfDocFieldsConfig() == null || DataUtils
				.isBlank(wfDocConfig.getWfDocFieldsConfig().getWfDocFieldConfigList())) {
			wfDocValidationContext
					.addDocError(WfXmlValidationErrorConstants.WFXML_DOCUMENT_NO_FIELD);
		} else {
			for (WfDocFieldConfig wfDocFieldConfig : wfDocConfig.getWfDocFieldsConfig()
					.getWfDocFieldConfigList()) {
				wfDocValidationContext.nextWfDocFieldConfig(wfDocFieldConfig);
				if (StringUtils.isBlank(wfDocFieldConfig.getName())) {
					wfDocValidationContext
							.addFieldError(WfXmlValidationErrorConstants.WFXML_DOCFIELD_NO_NAME);
				}

				if (StringUtils.isBlank(wfDocFieldConfig.getDescription())) {
					wfDocValidationContext
							.addFieldError(WfXmlValidationErrorConstants.WFXML_DOCFIELD_NO_DESC);
				}

				if (wfDocFieldConfig.getDataType() == null) {
					wfDocValidationContext
							.addFieldError(WfXmlValidationErrorConstants.WFXML_DOCFIELD_NO_TYPE);
				}

				match = wfDocValidationContext.matchWfDocFieldConfig(wfDocFieldConfig);
				if ((match & WfDocValidationContext.NAME_FLAG) != 0) {
					wfDocValidationContext.addFieldError(
							WfXmlValidationErrorConstants.WFXML_DOCFIELD_NAME_EXIST,
							wfDocFieldConfig.getName());
				}

				if ((match & WfDocValidationContext.DESC_FLAG) != 0) {
					wfDocValidationContext.addFieldError(
							WfXmlValidationErrorConstants.WFXML_DOCFIELD_DESC_EXIST,
							wfDocFieldConfig.getDescription());
				}

			}
		}

		// Classifiers
		WfDocClassifiersConfig wfDocClassifiersConfig = wfDocConfig.getWfDocClassifiersConfig();
		if (wfDocClassifiersConfig != null
				&& !DataUtils.isBlank(wfDocClassifiersConfig.getWfDocClassifierConfigList())) {
			for (WfDocClassifierConfig wfDocClassifierConfig : wfDocClassifiersConfig
					.getWfDocClassifierConfigList()) {
				wfDocValidationContext.nextWfDocClassifierConfig(wfDocClassifierConfig);
				if (StringUtils.isBlank(wfDocClassifierConfig.getName())) {
					wfDocValidationContext.addClassifierError(
							WfXmlValidationErrorConstants.WFXML_CLASSIFIER_NO_NAME);
				}

				if (StringUtils.isBlank(wfDocClassifierConfig.getDescription())) {
					wfDocValidationContext.addClassifierError(
							WfXmlValidationErrorConstants.WFXML_CLASSIFIER_NO_DESC);
				}

				match = wfDocValidationContext.matchWfDocClassifierConfig(wfDocClassifierConfig);
				if ((match & WfDocValidationContext.NAME_FLAG) != 0) {
					wfDocValidationContext.addClassifierError(
							WfXmlValidationErrorConstants.WFXML_CLASSIFIER_NAME_EXIST,
							wfDocClassifierConfig.getName());
				}

				if ((match & WfDocValidationContext.DESC_FLAG) != 0) {
					wfDocValidationContext.addClassifierError(
							WfXmlValidationErrorConstants.WFXML_CLASSIFIER_DESC_EXIST,
							wfDocClassifierConfig.getDescription());
				}

				List<WfDocClassifierFilterConfig> wfDocClassifierFilterConfigList
						= wfDocClassifierConfig.getWfDocClassifierFilterConfigList();
				if (StringUtils.isBlank(wfDocClassifierConfig.getLogic())
						&& DataUtils.isBlank(wfDocClassifierFilterConfigList)) {
					wfDocValidationContext.addClassifierError(
							WfXmlValidationErrorConstants.WFXML_CLASSIFIER_NO_LOGIC_FILTERS);
				}

				if (!DataUtils.isBlank(wfDocClassifierFilterConfigList)) {
					for (WfDocClassifierFilterConfig wfDocClassifierFilterConfig : wfDocClassifierFilterConfigList) {
						wfDocValidationContext
								.nextWfDocClassifierFilterConfig(wfDocClassifierFilterConfig);
						if (StringUtils.isBlank(wfDocClassifierFilterConfig.getField())) {
							wfDocValidationContext.addFilterError(
									WfXmlValidationErrorConstants.WFXML_FILTER_NO_FIELD);
						} else if (!wfDocValidationContext
								.matchField(wfDocClassifierFilterConfig.getField())) {
							wfDocValidationContext.addFilterError(
									WfXmlValidationErrorConstants.WFXML_FILTER_FIELD_UNKNOWN,
									wfDocClassifierFilterConfig.getField());
						}

						if (wfDocClassifierFilterConfig.getOp() == null) {
							wfDocValidationContext.addFilterError(
									WfXmlValidationErrorConstants.WFXML_FILTER_NO_OP);

						} else {
							switch (wfDocClassifierFilterConfig.getOp().operator().paramType()) {
								case BINARY:
									if (StringUtils
											.isBlank(wfDocClassifierFilterConfig.getValue2())) {
										wfDocValidationContext.addFilterError(
												WfXmlValidationErrorConstants.WFXML_FILTER_NO_VALUE2);
									} else if (Boolean.TRUE
											.equals(wfDocClassifierFilterConfig.getFieldOnly())
											&& !wfDocValidationContext.matchField(
													wfDocClassifierFilterConfig.getValue2())) {
										wfDocValidationContext.addFilterError(
												WfXmlValidationErrorConstants.WFXML_FILTER_FIELD_UNKNOWN,
												wfDocClassifierFilterConfig.getValue2());
									}
								case SINGLE:
									if (StringUtils
											.isBlank(wfDocClassifierFilterConfig.getValue1())) {
										wfDocValidationContext.addFilterError(
												WfXmlValidationErrorConstants.WFXML_FILTER_NO_VALUE1);
									} else if (Boolean.TRUE
											.equals(wfDocClassifierFilterConfig.getFieldOnly())
											&& !wfDocValidationContext.matchField(
													wfDocClassifierFilterConfig.getValue1())) {
										wfDocValidationContext.addFilterError(
												WfXmlValidationErrorConstants.WFXML_FILTER_FIELD_UNKNOWN,
												wfDocClassifierFilterConfig.getValue1());
									}
									break;
								case MULTIPLE:
									// TODO
									break;
								case VOID:
								default:
									break;
							}
						}

					}
				}
			}
		}

		// Attachments
		WfDocAttachmentsConfig wfDocAttachmentsConfig = wfDocConfig.getWfDocAttachmentsConfig();
		if (wfDocAttachmentsConfig != null
				&& !DataUtils.isBlank(wfDocAttachmentsConfig.getWfDocAttachmentConfigList())) {
			for (WfDocAttachmentConfig wfDocAttachmentConfig : wfDocAttachmentsConfig
					.getWfDocAttachmentConfigList()) {
				wfDocValidationContext.nextWfDocAttachmentConfig(wfDocAttachmentConfig);
				if (StringUtils.isBlank(wfDocAttachmentConfig.getName())) {
					wfDocValidationContext.addAttachmentError(
							WfXmlValidationErrorConstants.WFXML_ATTACHMENT_NO_NAME);
				}

				if (StringUtils.isBlank(wfDocAttachmentConfig.getDescription())) {
					wfDocValidationContext.addAttachmentError(
							WfXmlValidationErrorConstants.WFXML_ATTACHMENT_NO_DESC);
				}

				if (wfDocAttachmentConfig.getType() == null) {
					wfDocValidationContext.addAttachmentError(
							WfXmlValidationErrorConstants.WFXML_ATTACHMENT_NO_TYPE);
				}

				match = wfDocValidationContext.matchWfDocAttachmentConfig(wfDocAttachmentConfig);
				if ((match & WfDocValidationContext.NAME_FLAG) != 0) {
					wfDocValidationContext.addAttachmentError(
							WfXmlValidationErrorConstants.WFXML_ATTACHMENT_NAME_EXIST,
							wfDocAttachmentConfig.getName());
				}

				if ((match & WfDocValidationContext.DESC_FLAG) != 0) {
					wfDocValidationContext.addAttachmentError(
							WfXmlValidationErrorConstants.WFXML_ATTACHMENT_DESC_EXIST,
							wfDocAttachmentConfig.getDescription());
				}
			}
		}

		// Bean mappings
		WfDocBeanMappingsConfig wfDocBeanMappingsConfig = wfDocConfig.getWfDocBeanMappingsConfig();
		if (wfDocBeanMappingsConfig != null
				&& !DataUtils.isBlank(wfDocBeanMappingsConfig.getBeanMappingList())) {
			for (WfDocBeanMappingConfig wfDocBeanMappingConfig : wfDocBeanMappingsConfig
					.getBeanMappingList()) {
				wfDocValidationContext.nextWfDocBeanMappingConfig(wfDocBeanMappingConfig);
				if (StringUtils.isBlank(wfDocBeanMappingConfig.getName())) {
					wfDocValidationContext.addBeanMappingError(
							WfXmlValidationErrorConstants.WFXML_BEANMAPPING_NO_NAME);
				}

				if (StringUtils.isBlank(wfDocBeanMappingConfig.getDescription())) {
					wfDocValidationContext.addBeanMappingError(
							WfXmlValidationErrorConstants.WFXML_BEANMAPPING_NO_DESC);
				}

				if (wfDocBeanMappingConfig.getBeanType() == null) {
					wfDocValidationContext.addBeanMappingError(
							WfXmlValidationErrorConstants.WFXML_BEANMAPPING_NO_TYPE);
				}

				match = wfDocValidationContext.matchWfDocBeanMappingConfig(wfDocBeanMappingConfig);
				if ((match & WfDocValidationContext.NAME_FLAG) != 0) {
					wfDocValidationContext.addBeanMappingError(
							WfXmlValidationErrorConstants.WFXML_BEANMAPPING_NAME_EXIST,
							wfDocBeanMappingConfig.getName());
				}

				if ((match & WfDocValidationContext.DESC_FLAG) != 0) {
					wfDocValidationContext.addBeanMappingError(
							WfXmlValidationErrorConstants.WFXML_BEANMAPPING_DESC_EXIST,
							wfDocBeanMappingConfig.getDescription());
				}

				List<WfDocFieldMappingConfig> fieldMappingList
						= wfDocBeanMappingConfig.getFieldMappingList();
				if (DataUtils.isBlank(fieldMappingList)) {
					wfDocValidationContext.addBeanMappingError(
							WfXmlValidationErrorConstants.WFXML_BEANMAPPING_NO_FIELDMAPPINGS,
							wfDocBeanMappingConfig.getName());
				} else {
					for (WfDocFieldMappingConfig wfDocFieldMappingConfig : fieldMappingList) {
						wfDocValidationContext.nextWfDocFieldMappingConfig(wfDocFieldMappingConfig);
						if (StringUtils.isBlank(wfDocFieldMappingConfig.getDocFieldName())) {
							wfDocValidationContext.addFieldMappingError(
									WfXmlValidationErrorConstants.WFXML_FIELDMAPPING_NO_DOCFIELD);
						} else if (!wfDocValidationContext
								.matchField(wfDocFieldMappingConfig.getDocFieldName())) {
							wfDocValidationContext.addFieldMappingError(
									WfXmlValidationErrorConstants.WFXML_FIELDMAPPING_DOCFIELD_UNKNOWN,
									wfDocFieldMappingConfig.getDocFieldName());
						}

						if (wfDocValidationContext
								.matchWfDocFieldMappingConfig(wfDocFieldMappingConfig)) {
							wfDocValidationContext.addFieldMappingError(
									WfXmlValidationErrorConstants.WFXML_FIELDMAPPING_DOCFIELD_EXIST,
									wfDocFieldMappingConfig.getDocFieldName());
						}

						if (StringUtils.isBlank(wfDocFieldMappingConfig.getBeanFieldName())) {
							wfDocValidationContext.addFieldMappingError(
									WfXmlValidationErrorConstants.WFXML_FIELDMAPPING_NO_BEANFIELD);
						}
					}
				}
			}
		}
	}

	private static void validateWfFormConfig(WfFormValidationContext wfFormValidationContext,
			WfFormConfig wfFormConfig) {
		wfFormValidationContext.nextWfFormConfig(wfFormConfig);
		if (StringUtils.isBlank(wfFormConfig.getName())) {
			wfFormValidationContext.addFormError(WfXmlValidationErrorConstants.WFXML_FORM_NO_NAME);
		}

		if (StringUtils.isBlank(wfFormConfig.getDescription())) {
			wfFormValidationContext.addFormError(WfXmlValidationErrorConstants.WFXML_FORM_NO_DESC);
		}

		int match = wfFormValidationContext.matchWfFormConfig(wfFormConfig);
		if ((match & WfFormValidationContext.NAME_FLAG) != 0) {
			wfFormValidationContext.addFormError(
					WfXmlValidationErrorConstants.WFXML_FORM_NAME_EXIST, wfFormConfig.getName());
		}

		if ((match & WfFormValidationContext.DESC_FLAG) != 0) {
			wfFormValidationContext.addFormError(
					WfXmlValidationErrorConstants.WFXML_FORM_DESC_EXIST,
					wfFormConfig.getDescription());
		}

		if (StringUtils.isBlank(wfFormConfig.getDocument())) {
			wfFormValidationContext.addFormError(
					WfXmlValidationErrorConstants.WFXML_FORM_NO_DOCUMENT, wfFormConfig.getName());
		} else if (!wfFormValidationContext.isDocument(wfFormConfig.getDocument())) {
			wfFormValidationContext.addFormError(
					WfXmlValidationErrorConstants.WFXML_FORM_DOCUMENT_UNKNOWN,
					wfFormConfig.getName(), wfFormConfig.getDocument());
		}

		// Tabs
		WfFormTabsConfig wfFormTabsConfig = wfFormConfig.getWfFormTabsConfig();
		if (wfFormTabsConfig == null
				|| DataUtils.isBlank(wfFormTabsConfig.getWfFormTabConfigList())) {
			wfFormValidationContext.addFormError(WfXmlValidationErrorConstants.WFXML_FORM_NO_TAB,
					wfFormConfig.getName());
		} else {
			for (WfFormTabConfig wfFormTabConfig : wfFormTabsConfig.getWfFormTabConfigList()) {
				wfFormValidationContext.nextWfFormTabConfig(wfFormTabConfig);
				if (StringUtils.isBlank(wfFormTabConfig.getName())) {
					wfFormValidationContext
							.addTabError(WfXmlValidationErrorConstants.WFXML_TAB_NO_NAME);
				}

				if (StringUtils.isBlank(wfFormTabConfig.getDescription())) {
					wfFormValidationContext
							.addTabError(WfXmlValidationErrorConstants.WFXML_TAB_NO_DESC);
				}

				match = wfFormValidationContext.matchWfDocTabConfig(wfFormTabConfig);
				if ((match & WfDocValidationContext.NAME_FLAG) != 0) {
					wfFormValidationContext.addTabError(
							WfXmlValidationErrorConstants.WFXML_TAB_NAME_EXIST,
							wfFormTabConfig.getName());
				}

				if ((match & WfDocValidationContext.DESC_FLAG) != 0) {
					wfFormValidationContext.addTabError(
							WfXmlValidationErrorConstants.WFXML_TAB_DESC_EXIST,
							wfFormTabConfig.getDescription());
				}

				// Sections
				WfFormSectionsConfig wfDocSectionsConfig
						= wfFormTabConfig.getWfFormSectionsConfig();
				if (wfDocSectionsConfig == null
						|| DataUtils.isBlank(wfDocSectionsConfig.getWfFormSectionConfigList())) {
					wfFormValidationContext.addTabError(
							WfXmlValidationErrorConstants.WFXML_TAB_NO_SECTIONS,
							wfFormConfig.getName());
				} else {
					for (WfFormSectionConfig wfDocSectionConfig : wfDocSectionsConfig
							.getWfFormSectionConfigList()) {
						wfFormValidationContext.nextWfFormSectionConfig(wfDocSectionConfig);
						if (StringUtils.isBlank(wfDocSectionConfig.getName())) {
							wfFormValidationContext.addSectionError(
									WfXmlValidationErrorConstants.WFXML_SECTION_NO_NAME);
						}

						if (StringUtils.isBlank(wfDocSectionConfig.getDescription())) {
							wfFormValidationContext.addSectionError(
									WfXmlValidationErrorConstants.WFXML_SECTION_NO_DESC);
						}

						match = wfFormValidationContext.matchWfDocSectionConfig(wfDocSectionConfig);
						if ((match & WfDocValidationContext.NAME_FLAG) != 0) {
							wfFormValidationContext.addSectionError(
									WfXmlValidationErrorConstants.WFXML_SECTION_NAME_EXIST,
									wfDocSectionConfig.getName());
						}

						if ((match & WfDocValidationContext.DESC_FLAG) != 0) {
							wfFormValidationContext.addSectionError(
									WfXmlValidationErrorConstants.WFXML_SECTION_DESC_EXIST,
									wfDocSectionConfig.getDescription());
						}

						// Fields
						List<WfFormFieldConfig> wfDocFieldConfigList
								= wfDocSectionConfig.getWfFormFieldConfigList();
						if (DataUtils.isBlank(wfDocFieldConfigList)) {
							wfFormValidationContext.addSectionError(
									WfXmlValidationErrorConstants.WFXML_SECTION_NO_FIELDS);
						} else {
							for (WfFormFieldConfig wfFormFieldConfig : wfDocFieldConfigList) {
								wfFormValidationContext.nextWfFormFieldConfig(wfFormFieldConfig);
								if (StringUtils.isBlank(wfFormFieldConfig.getBinding())) {
									wfFormValidationContext.addFieldError(
											WfXmlValidationErrorConstants.WFXML_FIELD_NO_BINDING);
								}

								if (wfFormValidationContext
										.matchWfFormFieldConfig(wfFormFieldConfig)) {
									wfFormValidationContext.addFieldError(
											WfXmlValidationErrorConstants.WFXML_FIELD_BINDING_EXIST,
											wfFormFieldConfig.getBinding());
								}
							}
						}
					}
				}

			}
		}

	}

	private static void validateWfMessageConfig(
			WfMessageValidationContext wfMessageValidationContext,
			WfMessageConfig wfMessageConfig) {
		wfMessageValidationContext.nextWfMessageConfig(wfMessageConfig);
		if (StringUtils.isBlank(wfMessageConfig.getName())) {
			wfMessageValidationContext
					.addMessageError(WfXmlValidationErrorConstants.WFXML_MESSAGE_NO_NAME);
		}

		if (StringUtils.isBlank(wfMessageConfig.getDescription())) {
			wfMessageValidationContext
					.addMessageError(WfXmlValidationErrorConstants.WFXML_MESSAGE_NO_DESC);
		}

		int match = wfMessageValidationContext.matchWfMessageConfig(wfMessageConfig);
		if ((match & WfMessageValidationContext.NAME_FLAG) != 0) {
			wfMessageValidationContext.addMessageError(
					WfXmlValidationErrorConstants.WFXML_MESSAGE_NAME_EXIST,
					wfMessageConfig.getName());
		}

		if ((match & WfMessageValidationContext.DESC_FLAG) != 0) {
			wfMessageValidationContext.addMessageError(
					WfXmlValidationErrorConstants.WFXML_MESSAGE_DESC_EXIST,
					wfMessageConfig.getDescription());
		}

		if (StringUtils.isBlank(wfMessageConfig.getSubject())) {
			wfMessageValidationContext
					.addMessageError(WfXmlValidationErrorConstants.WFXML_MESSAGE_NO_SUBJECT);
		}

		if (StringUtils.isBlank(wfMessageConfig.getBody())) {
			wfMessageValidationContext
					.addMessageError(WfXmlValidationErrorConstants.WFXML_MESSAGE_NO_BODY);
		}
	}

	private static void validateWfTemplateConfig(
			WfTemplateValidationContext wfTemplateValidationContext,
			WfTemplateConfig wfTemplateConfig) throws UnifyException {
		wfTemplateValidationContext.nextWfTemplateConfig(wfTemplateConfig);
		if (StringUtils.isBlank(wfTemplateConfig.getName())) {
			wfTemplateValidationContext
					.addTemplateError(WfXmlValidationErrorConstants.WFXML_TEMPLATE_NO_NAME);
		}

		if (StringUtils.isBlank(wfTemplateConfig.getDescription())) {
			wfTemplateValidationContext
					.addTemplateError(WfXmlValidationErrorConstants.WFXML_TEMPLATE_NO_DESC);
		}

		if (StringUtils.isBlank(wfTemplateConfig.getItemDescFormat())) {
			wfTemplateValidationContext
					.addTemplateError(WfXmlValidationErrorConstants.WFXML_TEMPLATE_NO_ITEM_DESC);
		}

		int match = wfTemplateValidationContext.matchWfTemplateConfig(wfTemplateConfig);
		if ((match & WfTemplateValidationContext.NAME_FLAG) != 0) {
			wfTemplateValidationContext.addTemplateError(
					WfXmlValidationErrorConstants.WFXML_TEMPLATE_NAME_EXIST,
					wfTemplateConfig.getName());
		}

		if ((match & WfTemplateValidationContext.DESC_FLAG) != 0) {
			wfTemplateValidationContext.addTemplateError(
					WfXmlValidationErrorConstants.WFXML_TEMPLATE_DESC_EXIST,
					wfTemplateConfig.getDescription());
		}

		if (StringUtils.isBlank(wfTemplateConfig.getWfDocName())) {
			wfTemplateValidationContext.addTemplateError(
					WfXmlValidationErrorConstants.WFXML_TEMPLATE_NO_DOCUMENT,
					wfTemplateConfig.getName());
		} else if (!wfTemplateValidationContext.isDoc()) {
			wfTemplateValidationContext.addTemplateError(
					WfXmlValidationErrorConstants.WFXML_TEMPLATE_DOCUMENT_UNKNOWN,
					wfTemplateConfig.getName(), wfTemplateConfig.getWfDocName());
		}

		if (DataUtils.isBlank(wfTemplateConfig.getWfStepConfigList())) {
			wfTemplateValidationContext.addTemplateError(
					WfXmlValidationErrorConstants.WFXML_TEMPLATE_NO_STEPS,
					wfTemplateConfig.getName());
		} else {
			int startCount = 0;
			int manualCount = 0;
			int endCount = 0;
			Set<String> stepNames = new HashSet<String>();
			// Do quick pass to get all step names
			for (WfStepConfig wfStepConfig : wfTemplateConfig.getWfStepConfigList()) {
				if (!StringUtils.isBlank(wfStepConfig.getName())) {
					stepNames.add(wfStepConfig.getName());
				}
			}

			// Validate steps
			for (WfStepConfig wfStepConfig : wfTemplateConfig.getWfStepConfigList()) {
				wfTemplateValidationContext.nextWfStepConfig(wfStepConfig);
				if (StringUtils.isBlank(wfStepConfig.getName())) {
					wfTemplateValidationContext
							.addStepError(WfXmlValidationErrorConstants.WFXML_STEP_NO_NAME);
				}

				if (StringUtils.isBlank(wfStepConfig.getDescription())) {
					wfTemplateValidationContext
							.addStepError(WfXmlValidationErrorConstants.WFXML_STEP_NO_DESC);
				}

				match = wfTemplateValidationContext.matchWfStepConfig(wfStepConfig);
				if ((match & WfTemplateValidationContext.NAME_FLAG) != 0) {
					wfTemplateValidationContext.addStepError(
							WfXmlValidationErrorConstants.WFXML_STEP_NAME_EXIST,
							wfStepConfig.getName());
				}

				if ((match & WfTemplateValidationContext.DESC_FLAG) != 0) {
					wfTemplateValidationContext.addStepError(
							WfXmlValidationErrorConstants.WFXML_STEP_DESC_EXIST,
							wfStepConfig.getDescription());
				}

				if (wfStepConfig.getType() == null) {
					wfTemplateValidationContext.addStepError(
							WfXmlValidationErrorConstants.WFXML_STEP_NO_TYPE,
							wfStepConfig.getName());
				}

				if (wfStepConfig.getPriority() == null) {
					wfTemplateValidationContext.addStepError(
							WfXmlValidationErrorConstants.WFXML_STEP_NO_PRIORITY,
							wfStepConfig.getName());
				}

				if (WorkflowStepType.START.equals(wfStepConfig.getType())) {
					WfXmlConfigUtils.checkAutomatic(wfTemplateValidationContext, wfStepConfig);

					startCount++;
				} else if (WorkflowStepType.MANUAL.equals(wfStepConfig.getType())) {
					WfXmlConfigUtils.checkManual(wfTemplateValidationContext, wfStepConfig);

					manualCount++;
				} else if (WorkflowStepType.AUTOMATIC.equals(wfStepConfig.getType())) {
					WfXmlConfigUtils.checkAutomatic(wfTemplateValidationContext, wfStepConfig);
				} else if (WorkflowStepType.INTERACTIVE.equals(wfStepConfig.getType())) {
					WfXmlConfigUtils.checkManual(wfTemplateValidationContext, wfStepConfig);

					if (!WfXmlConfigUtils.isUserActioned(wfStepConfig)) {
						wfTemplateValidationContext.addStepError(
								WfXmlValidationErrorConstants.WFXML_STEP_USERACTION_REQUIRED,
								wfStepConfig.getName(), wfStepConfig.getType());
					}
				} else if (WorkflowStepType.END.equals(wfStepConfig.getType())) {
					if (WfXmlConfigUtils.isUserActioned(wfStepConfig)) {
						wfTemplateValidationContext.addStepError(
								WfXmlValidationErrorConstants.WFXML_STEP_USERACTION_NOT_ALLOWED,
								wfStepConfig.getName(), wfStepConfig.getType());
					}

					if (WfXmlConfigUtils.isFormPrivileged(wfStepConfig)) {
						wfTemplateValidationContext.addStepError(
								WfXmlValidationErrorConstants.WFXML_STEP_FORMPRIVILEGE_NOT_ALLOWED,
								wfStepConfig.getName(), wfStepConfig.getType());
					}

					if (WfXmlConfigUtils.isEnriched(wfStepConfig)) {
						wfTemplateValidationContext.addStepError(
								WfXmlValidationErrorConstants.WFXML_STEP_ENRICHMENT_NOT_ALLOWED,
								wfStepConfig.getName(), wfStepConfig.getType());
					}

					if (WfXmlConfigUtils.isPolicied(wfStepConfig)) {
						wfTemplateValidationContext.addStepError(
								WfXmlValidationErrorConstants.WFXML_STEP_POLICY_NOT_ALLOWED,
								wfStepConfig.getName(), wfStepConfig.getType());
					}

					if (WfXmlConfigUtils.isRecordActioned(wfStepConfig)) {
						wfTemplateValidationContext.addStepError(
								WfXmlValidationErrorConstants.WFXML_STEP_RECORDMAPPING_NOT_ALLOWED,
								wfStepConfig.getName(), wfStepConfig.getType());
					}

					endCount++;
				}

				// Enrichments
				if (wfStepConfig.getWfEnrichmentsConfig() != null && !DataUtils.isBlank(
						wfStepConfig.getWfEnrichmentsConfig().getWfEnrichmentConfigList())) {
					for (WfEnrichmentConfig wfEnrichmentConfig : wfStepConfig
							.getWfEnrichmentsConfig().getWfEnrichmentConfigList()) {
						wfTemplateValidationContext.nextWfEnrichmentConfig(wfEnrichmentConfig);
						if (StringUtils.isBlank(wfEnrichmentConfig.getName())) {
							wfTemplateValidationContext.addEnrichmentError(
									WfXmlValidationErrorConstants.WFXML_ENRICHMENT_NO_NAME);
						}

						if (StringUtils.isBlank(wfEnrichmentConfig.getDescription())) {
							wfTemplateValidationContext.addEnrichmentError(
									WfXmlValidationErrorConstants.WFXML_ENRICHMENT_NO_DESC);
						}

						match = wfTemplateValidationContext
								.matchWfEnrichmentConfig(wfEnrichmentConfig);
						if ((match & WfTemplateValidationContext.NAME_FLAG) != 0) {
							wfTemplateValidationContext.addEnrichmentError(
									WfXmlValidationErrorConstants.WFXML_ENRICHMENT_NAME_EXIST,
									wfEnrichmentConfig.getName());
						}

						if ((match & WfTemplateValidationContext.DESC_FLAG) != 0) {
							wfTemplateValidationContext.addEnrichmentError(
									WfXmlValidationErrorConstants.WFXML_ENRICHMENT_DESC_EXIST,
									wfEnrichmentConfig.getDescription());
						}

						if (StringUtils.isBlank(wfEnrichmentConfig.getLogic())) {
							wfTemplateValidationContext.addEnrichmentError(
									WfXmlValidationErrorConstants.WFXML_ENRICHMENT_NO_LOGIC);
						}
					}
				}

				// Routings
				if (WfXmlConfigUtils.isRouted(wfStepConfig)) {
					for (WfRoutingConfig wfRoutingConfig : wfStepConfig.getWfRoutingsConfig()
							.getWfRoutingConfigList()) {
						wfTemplateValidationContext.nextWfRoutingConfig(wfRoutingConfig);
						if (StringUtils.isBlank(wfRoutingConfig.getName())) {
							wfTemplateValidationContext.addRoutingError(
									WfXmlValidationErrorConstants.WFXML_ROUTING_NO_NAME);
						}

						if (StringUtils.isBlank(wfRoutingConfig.getDescription())) {
							wfTemplateValidationContext.addRoutingError(
									WfXmlValidationErrorConstants.WFXML_ROUTING_NO_DESC);
						}

						match = wfTemplateValidationContext.matchWfRoutingConfig(wfRoutingConfig);
						if ((match & WfTemplateValidationContext.NAME_FLAG) != 0) {
							wfTemplateValidationContext.addRoutingError(
									WfXmlValidationErrorConstants.WFXML_ROUTING_NAME_EXIST,
									wfRoutingConfig.getName());
						}

						if ((match & WfTemplateValidationContext.DESC_FLAG) != 0) {
							wfTemplateValidationContext.addRoutingError(
									WfXmlValidationErrorConstants.WFXML_ROUTING_DESC_EXIST,
									wfRoutingConfig.getDescription());
						}

						if (StringUtils.isBlank(wfRoutingConfig.getTargetStepName())) {
							wfTemplateValidationContext.addRoutingError(
									WfXmlValidationErrorConstants.WFXML_ROUTING_NO_TARGET);
						} else {
							if (!stepNames.contains(wfRoutingConfig.getTargetStepName())) {
								wfTemplateValidationContext.addRoutingError(
										WfXmlValidationErrorConstants.WFXML_ROUTING_TARGET_UNKNOWN,
										wfRoutingConfig.getTargetStepName());
							}

							if (wfRoutingConfig.getTargetStepName()
									.equals(wfStepConfig.getName())) {
								wfTemplateValidationContext.addRoutingError(
										WfXmlValidationErrorConstants.WFXML_ROUTING_TARGET_SELF,
										wfRoutingConfig.getTargetStepName());
							}
						}

						if (!StringUtils.isBlank(wfRoutingConfig.getClassifierName())) {
							if (!wfTemplateValidationContext
									.matchClassifier(wfRoutingConfig.getClassifierName())) {
								wfTemplateValidationContext.addRoutingError(
										WfXmlValidationErrorConstants.WFXML_ROUTING_CLASSIFIER_UNKNOWN,
										wfRoutingConfig.getName(),
										wfRoutingConfig.getClassifierName());
							}
						}
					}
				}

				// Entity actions
				if (wfStepConfig.getWfRecordActionsConfig() != null && !DataUtils.isBlank(
						wfStepConfig.getWfRecordActionsConfig().getWfRecordActionConfigList())) {
					for (WfRecordActionConfig wfRecordActionConfig : wfStepConfig
							.getWfRecordActionsConfig().getWfRecordActionConfigList()) {
						wfTemplateValidationContext.nextWfRecordActionConfig(wfRecordActionConfig);
						if (StringUtils.isBlank(wfRecordActionConfig.getName())) {
							wfTemplateValidationContext.addRecordActionError(
									WfXmlValidationErrorConstants.WFXML_RECORDACTION_NO_NAME);
						}

						if (StringUtils.isBlank(wfRecordActionConfig.getDescription())) {
							wfTemplateValidationContext.addRecordActionError(
									WfXmlValidationErrorConstants.WFXML_RECORDACTION_NO_DESC);
						}

						match = wfTemplateValidationContext
								.matchWfRecordActionConfig(wfRecordActionConfig);
						if ((match & WfTemplateValidationContext.NAME_FLAG) != 0) {
							wfTemplateValidationContext.addRecordActionError(
									WfXmlValidationErrorConstants.WFXML_RECORDACTION_NAME_EXIST,
									wfRecordActionConfig.getName());
						}

						if ((match & WfTemplateValidationContext.DESC_FLAG) != 0) {
							wfTemplateValidationContext.addRecordActionError(
									WfXmlValidationErrorConstants.WFXML_RECORDACTION_DESC_EXIST,
									wfRecordActionConfig.getDescription());
						}

						if (StringUtils.isBlank(wfRecordActionConfig.getDocMappingName())) {
							wfTemplateValidationContext.addRecordActionError(
									WfXmlValidationErrorConstants.WFXML_RECORDACTION_NO_DOCMAPPING);
						} else {
							if (!wfTemplateValidationContext
									.matchBeanMapping(wfRecordActionConfig.getDocMappingName())) {
								wfTemplateValidationContext.addRecordActionError(
										WfXmlValidationErrorConstants.WFXML_RECORDACTION_DOCMAPPING_UNKNOWN,
										wfRecordActionConfig.getDocMappingName());
							}
						}

						if (wfRecordActionConfig.getActionType() == null) {
							wfTemplateValidationContext.addRecordActionError(
									WfXmlValidationErrorConstants.WFXML_RECORDACTION_NO_TYPE);
						}
					}
				}

				// User actions
				if (WfXmlConfigUtils.isUserActioned(wfStepConfig)) {
					for (WfUserActionConfig wfUserActionConfig : wfStepConfig
							.getWfUserActionsConfig().getWfUserActionConfigList()) {
						wfTemplateValidationContext.nextWfUserActionConfig(wfUserActionConfig);
						if (StringUtils.isBlank(wfUserActionConfig.getName())) {
							wfTemplateValidationContext.addUserActionError(
									WfXmlValidationErrorConstants.WFXML_USERACTION_NO_NAME);
						}

						if (StringUtils.isBlank(wfUserActionConfig.getDescription())) {
							wfTemplateValidationContext.addUserActionError(
									WfXmlValidationErrorConstants.WFXML_USERACTION_NO_DESC);
						}

						match = wfTemplateValidationContext
								.matchWfUserActionConfig(wfUserActionConfig);
						if ((match & WfTemplateValidationContext.NAME_FLAG) != 0) {
							wfTemplateValidationContext.addUserActionError(
									WfXmlValidationErrorConstants.WFXML_USERACTION_NAME_EXIST,
									wfUserActionConfig.getName());
						}

						if ((match & WfTemplateValidationContext.DESC_FLAG) != 0) {
							wfTemplateValidationContext.addUserActionError(
									WfXmlValidationErrorConstants.WFXML_USERACTION_DESC_EXIST,
									wfUserActionConfig.getDescription());
						}

						if (StringUtils.isBlank(wfUserActionConfig.getTargetStepName())) {
							wfTemplateValidationContext.addUserActionError(
									WfXmlValidationErrorConstants.WFXML_USERACTION_NO_TARGET);
						} else {
							if (!stepNames.contains(wfUserActionConfig.getTargetStepName())) {
								wfTemplateValidationContext.addUserActionError(
										WfXmlValidationErrorConstants.WFXML_USERACTION_TARGET_UNKNOWN,
										wfUserActionConfig.getTargetStepName());
							}

							if (wfUserActionConfig.getTargetStepName()
									.equals(wfStepConfig.getName())) {
								wfTemplateValidationContext.addUserActionError(
										WfXmlValidationErrorConstants.WFXML_USERACTION_TARGET_SELF,
										wfUserActionConfig.getTargetStepName());
							}
						}

						if (wfUserActionConfig.getNoteRequirement() == null) {
							wfTemplateValidationContext.addUserActionError(
									WfXmlValidationErrorConstants.WFXML_USERACTION_NO_NOTEREQUIREMENT);
						}

						if (!DataUtils.isBlank(wfUserActionConfig.getAttachmentCheckConfigList())) {
							for (WfAttachmentCheckConfig wfAttachmentCheckConfig : wfUserActionConfig
									.getAttachmentCheckConfigList()) {
								wfTemplateValidationContext
										.nextWfAttachmentCheckConfig(wfAttachmentCheckConfig);
								if (StringUtils
										.isBlank(wfAttachmentCheckConfig.getAttachmentName())) {
									wfTemplateValidationContext.addAttachmentCheckError(
											WfXmlValidationErrorConstants.WFXML_ATTACHMENTCHECK_NO_ATTACHMENTNAME);
								} else if (!wfTemplateValidationContext.matchAttachment(
										wfAttachmentCheckConfig.getAttachmentName())) {
									wfTemplateValidationContext.addAttachmentCheckError(
											WfXmlValidationErrorConstants.WFXML_ATTACHMENTCHECK_ATTACHMENTNAME_UNKNOWN,
											wfAttachmentCheckConfig.getAttachmentName());
								}

								if (wfTemplateValidationContext
										.matchWfAttachmentCheckConfig(wfAttachmentCheckConfig)) {
									wfTemplateValidationContext.addAttachmentCheckError(
											WfXmlValidationErrorConstants.WFXML_ATTACHMENTCHECK_EXIST,
											wfAttachmentCheckConfig.getAttachmentName());
								}

								if (wfAttachmentCheckConfig.getRequirementType() == null) {
									wfTemplateValidationContext.addAttachmentCheckError(
											WfXmlValidationErrorConstants.WFXML_ATTACHMENTCHECK_NO_REQUIREMENTTYPE);
								}
							}
						}
					}
				}

				// Form privileges
				if (wfStepConfig.getWfFormPrivilegesConfig() != null && !DataUtils.isBlank(
						wfStepConfig.getWfFormPrivilegesConfig().getWfFormPrivilegesConfigList())) {
					for (WfFormPrivilegeConfig wfFormPrivilegeConfig : wfStepConfig
							.getWfFormPrivilegesConfig().getWfFormPrivilegesConfigList()) {
						wfTemplateValidationContext
								.nextWfFormPrivilegeConfig(wfFormPrivilegeConfig);

						if (wfFormPrivilegeConfig.getType() == null) {
							wfTemplateValidationContext.addFormPrivilegeError(
									WfXmlValidationErrorConstants.WFXML_FORMPRIVILEGE_NO_ELEMENT_TYPE);
						}

						if (StringUtils.isBlank(wfFormPrivilegeConfig.getName())) {
							wfTemplateValidationContext.addFormPrivilegeError(
									WfXmlValidationErrorConstants.WFXML_FORMPRIVILEGE_NO_ELEMENT_NAME);
						} else {
							if (wfFormPrivilegeConfig.getType() != null) {
								String form = wfStepConfig.getForm();
								switch (wfFormPrivilegeConfig.getType()) {
									case FIELD:
										if (!wfTemplateValidationContext.matchFormField(form,
												wfFormPrivilegeConfig.getName())) {
											wfTemplateValidationContext.addFormPrivilegeError(
													WfXmlValidationErrorConstants.WFXML_FORMPRIVILEGE_FIELD_UNKNOWN,
													wfFormPrivilegeConfig.getName());
										}
										break;
									case SECTION:
										if (!wfTemplateValidationContext.matchFormSection(form,
												wfFormPrivilegeConfig.getName())) {
											wfTemplateValidationContext.addFormPrivilegeError(
													WfXmlValidationErrorConstants.WFXML_FORMPRIVILEGE_SECTION_UNKNOWN,
													wfFormPrivilegeConfig.getName());
										}
										break;
									case TAB:
										if (!wfTemplateValidationContext.matchFormTab(form,
												wfFormPrivilegeConfig.getName())) {
											wfTemplateValidationContext.addFormPrivilegeError(
													WfXmlValidationErrorConstants.WFXML_FORMPRIVILEGE_TAB_UNKNOWN,
													wfFormPrivilegeConfig.getName());
										}
										break;
									default:
										break;

								}
							}
						}

						if (wfTemplateValidationContext
								.matchWfFormPrivilegeConfig(wfFormPrivilegeConfig)) {
							wfTemplateValidationContext.addFormPrivilegeError(
									WfXmlValidationErrorConstants.WFXML_FORMPRIVILEGE_NAME_EXIST,
									wfFormPrivilegeConfig.getName());
						}
					}
				}

				// Policies
				if (wfStepConfig.getWfPoliciesConfig() != null && !DataUtils
						.isBlank(wfStepConfig.getWfPoliciesConfig().getWfPolicyConfigList())) {
					for (WfPolicyConfig wfPolicyConfig : wfStepConfig.getWfPoliciesConfig()
							.getWfPolicyConfigList()) {
						wfTemplateValidationContext.nextWfPolicyConfig(wfPolicyConfig);
						if (StringUtils.isBlank(wfPolicyConfig.getName())) {
							wfTemplateValidationContext.addPolicyError(
									WfXmlValidationErrorConstants.WFXML_POLICY_NO_NAME);
						}

						if (StringUtils.isBlank(wfPolicyConfig.getDescription())) {
							wfTemplateValidationContext.addPolicyError(
									WfXmlValidationErrorConstants.WFXML_POLICY_NO_DESC);
						}

						match = wfTemplateValidationContext.matchWfPolicyConfig(wfPolicyConfig);
						if ((match & WfTemplateValidationContext.NAME_FLAG) != 0) {
							wfTemplateValidationContext.addPolicyError(
									WfXmlValidationErrorConstants.WFXML_POLICY_NAME_EXIST,
									wfPolicyConfig.getName());
						}

						if ((match & WfTemplateValidationContext.DESC_FLAG) != 0) {
							wfTemplateValidationContext.addPolicyError(
									WfXmlValidationErrorConstants.WFXML_POLICY_DESC_EXIST,
									wfPolicyConfig.getDescription());
						}

						if (StringUtils.isBlank(wfPolicyConfig.getLogic())) {
							wfTemplateValidationContext.addPolicyError(
									WfXmlValidationErrorConstants.WFXML_POLICY_NO_LOGIC);
						}
					}
				}

				// Alerts
				if (wfStepConfig.getWfAlertsConfig() != null && !DataUtils
						.isBlank(wfStepConfig.getWfAlertsConfig().getWfAlertConfigList())) {
					for (WfAlertConfig wfAlertConfig : wfStepConfig.getWfAlertsConfig()
							.getWfAlertConfigList()) {
						wfTemplateValidationContext.nextWfAlertConfig(wfAlertConfig);
						if (StringUtils.isBlank(wfAlertConfig.getName())) {
							wfTemplateValidationContext.addAlertError(
									WfXmlValidationErrorConstants.WFXML_ALERT_NO_NAME);
						}

						if (StringUtils.isBlank(wfAlertConfig.getDescription())) {
							wfTemplateValidationContext.addAlertError(
									WfXmlValidationErrorConstants.WFXML_ALERT_NO_DESC);
						}

						match = wfTemplateValidationContext.matchWfAlertConfig(wfAlertConfig);
						if ((match & WfTemplateValidationContext.NAME_FLAG) != 0) {
							wfTemplateValidationContext.addAlertError(
									WfXmlValidationErrorConstants.WFXML_ALERT_NAME_EXIST,
									wfAlertConfig.getName());
						}

						if ((match & WfTemplateValidationContext.DESC_FLAG) != 0) {
							wfTemplateValidationContext.addAlertError(
									WfXmlValidationErrorConstants.WFXML_ALERT_DESC_EXIST,
									wfAlertConfig.getDescription());
						}

						if (wfAlertConfig.getType() == null) {
							wfTemplateValidationContext.addAlertError(
									WfXmlValidationErrorConstants.WFXML_ALERT_NO_TYPE);
						}

						if (!wfTemplateValidationContext.matchMessage(wfAlertConfig.getMessage())) {
							wfTemplateValidationContext.addAlertError(
									WfXmlValidationErrorConstants.WFXML_ALERT_MESSAGE_UNKNOWN,
									wfAlertConfig.getMessage());
						}
					}
				}

			}

			if (startCount == 0) {
				wfTemplateValidationContext.addTemplateError(
						WfXmlValidationErrorConstants.WFXML_TEMPLATE_NO_START,
						wfTemplateConfig.getName());
			} else if (startCount > 1) {
				wfTemplateValidationContext.addTemplateError(
						WfXmlValidationErrorConstants.WFXML_TEMPLATE_MULTIPLE_START_STEPS,
						wfTemplateConfig.getName());
			}

			if (manualCount > 1) {
				wfTemplateValidationContext.addTemplateError(
						WfXmlValidationErrorConstants.WFXML_TEMPLATE_MULTIPLE_MANUAL_STEPS,
						wfTemplateConfig.getName());
			}

			if (endCount == 0) {
				wfTemplateValidationContext.addTemplateError(
						WfXmlValidationErrorConstants.WFXML_TEMPLATE_NO_END,
						wfTemplateConfig.getName());
			} else if (endCount > 1) {
				wfTemplateValidationContext.addTemplateError(
						WfXmlValidationErrorConstants.WFXML_TEMPLATE_MULTIPLE_END_STEPS,
						wfTemplateConfig.getName());
			}
		}
	}

	private static void checkAutomatic(WfTemplateValidationContext wfTemplateValidationContext,
			WfStepConfig wfStepConfig) {
		if (!WfXmlConfigUtils.isRouted(wfStepConfig)) {
			wfTemplateValidationContext.addStepError(
					WfXmlValidationErrorConstants.WFXML_STEP_ROUTING_REQUIRED,
					wfStepConfig.getName(), wfStepConfig.getType());
		}

		if (WfXmlConfigUtils.isUserActioned(wfStepConfig)) {
			wfTemplateValidationContext.addStepError(
					WfXmlValidationErrorConstants.WFXML_STEP_USERACTION_NOT_ALLOWED,
					wfStepConfig.getName(), wfStepConfig.getType());
		}

		if (WfXmlConfigUtils.isFormPrivileged(wfStepConfig)) {
			wfTemplateValidationContext.addStepError(
					WfXmlValidationErrorConstants.WFXML_STEP_FORMPRIVILEGE_NOT_ALLOWED,
					wfStepConfig.getName(), wfStepConfig.getType());
		}

		if (!WorkflowParticipantType.NONE.equals(wfStepConfig.getParticipant())) {
			wfTemplateValidationContext.addStepError(
					WfXmlValidationErrorConstants.WFXML_STEP_PARTICIPANT_NOT_ALLOWED,
					wfStepConfig.getName(), wfStepConfig.getType());
		}

	}

	private static void checkManual(WfTemplateValidationContext wfTemplateValidationContext,
			WfStepConfig wfStepConfig) {
		if (WfXmlConfigUtils.isRouted(wfStepConfig)) {
			wfTemplateValidationContext.addStepError(
					WfXmlValidationErrorConstants.WFXML_STEP_ROUTING_NOT_ALLOWED,
					wfStepConfig.getName(), wfStepConfig.getType());
		}

		if (WfXmlConfigUtils.isEnriched(wfStepConfig)) {
			wfTemplateValidationContext.addStepError(
					WfXmlValidationErrorConstants.WFXML_STEP_ENRICHMENT_NOT_ALLOWED,
					wfStepConfig.getName(), wfStepConfig.getType());
		}

		if (WfXmlConfigUtils.isPolicied(wfStepConfig)) {
			wfTemplateValidationContext.addStepError(
					WfXmlValidationErrorConstants.WFXML_STEP_POLICY_NOT_ALLOWED,
					wfStepConfig.getName(), wfStepConfig.getType());
		}

		if (WfXmlConfigUtils.isRecordActioned(wfStepConfig)) {
			wfTemplateValidationContext.addStepError(
					WfXmlValidationErrorConstants.WFXML_STEP_RECORDMAPPING_NOT_ALLOWED,
					wfStepConfig.getName(), wfStepConfig.getType());
		}

		if (StringUtils.isBlank(wfStepConfig.getForm())) {
			wfTemplateValidationContext.addStepError(
					WfXmlValidationErrorConstants.WFXML_STEP_FORM_REQUIRED, wfStepConfig.getName(),
					wfStepConfig.getType());
		} else if (!wfTemplateValidationContext.matchForm(wfStepConfig.getForm())) {
			wfTemplateValidationContext.addStepError(
					WfXmlValidationErrorConstants.WFXML_STEP_FORM_UNKNOWN, wfStepConfig.getName(),
					wfStepConfig.getForm());
		}

		if (wfStepConfig.getParticipant() == null
				|| WorkflowParticipantType.NONE.equals(wfStepConfig.getParticipant())) {
			wfTemplateValidationContext.addStepError(
					WfXmlValidationErrorConstants.WFXML_STEP_NO_PARTICIPANT, wfStepConfig.getName(),
					wfStepConfig.getType());
		}

	}

	private static boolean isRouted(WfStepConfig wfStepConfig) {
		return wfStepConfig.getWfRoutingsConfig() != null
				&& !DataUtils.isBlank(wfStepConfig.getWfRoutingsConfig().getWfRoutingConfigList());
	}

	private static boolean isUserActioned(WfStepConfig wfStepConfig) {
		return wfStepConfig.getWfUserActionsConfig() != null && !DataUtils
				.isBlank(wfStepConfig.getWfUserActionsConfig().getWfUserActionConfigList());
	}

	private static boolean isFormPrivileged(WfStepConfig wfStepConfig) {
		return wfStepConfig.getWfFormPrivilegesConfig() != null && !DataUtils
				.isBlank(wfStepConfig.getWfFormPrivilegesConfig().getWfFormPrivilegesConfigList());
	}

	private static boolean isEnriched(WfStepConfig wfStepConfig) {
		return wfStepConfig.getWfEnrichmentsConfig() != null && !DataUtils
				.isBlank(wfStepConfig.getWfEnrichmentsConfig().getWfEnrichmentConfigList());
	}

	private static boolean isPolicied(WfStepConfig wfStepConfig) {
		return wfStepConfig.getWfPoliciesConfig() != null
				&& !DataUtils.isBlank(wfStepConfig.getWfPoliciesConfig().getWfPolicyConfigList());
	}

	private static boolean isRecordActioned(WfStepConfig wfStepConfig) {
		return wfStepConfig.getWfRecordActionsConfig() != null && !DataUtils
				.isBlank(wfStepConfig.getWfRecordActionsConfig().getWfRecordActionConfigList());
	}

	private static abstract class AbstractValidationContext {

		public static final int NAME_FLAG = 0x00000001;

		public static final int DESC_FLAG = 0x00000002;

		protected String name;

		private Map<String, DocInfo> docs;

		private TaskMonitor taskMonitor;

		private List<UnifyError> errorList;

		public AbstractValidationContext(TaskMonitor taskMonitor, String name) {
			this(taskMonitor, name, null);
		}

		public AbstractValidationContext(TaskMonitor taskMonitor, String name,
				AbstractValidationContext refValidationContext) {
			this.taskMonitor = taskMonitor;
			this.name = name;
			if (refValidationContext != null) {
				docs = refValidationContext.docs;
			}
			errorList = new ArrayList<UnifyError>();
		}

		public List<UnifyError> getErrorList() {
			return errorList;
		}

		protected void addError(UnifyError ue) {
			if (taskMonitor != null) {
				taskMonitor.addErrorMessage(ue);
			}

			errorList.add(ue);
		}

		protected int matchConfig(List<? extends BaseConfig> configList, BaseConfig config) {
			int result = 0;
			boolean isNameMatch = false;
			boolean isDescMatch = false;
			for (int i = 0; i < (configList.size() - 1) && (!isNameMatch || !isDescMatch); i++) {
				BaseConfig eConfig = configList.get(i);
				if (!isNameMatch && config.getName() != null
						&& config.getName().equals(eConfig.getName())) {
					result |= NAME_FLAG;
					isNameMatch = true;
				}

				if (!isDescMatch && config.getDescription() != null
						&& config.getDescription().equals(eConfig.getDescription())) {
					result |= DESC_FLAG;
					isDescMatch = true;
				}
			}

			return result;
		}

		protected void addDocInfo(WfDocConfig wfDocConfig) {
			if (docs == null) {
				docs = new HashMap<String, DocInfo>();
			}

			if (!StringUtils.isBlank(wfDocConfig.getName())) {
				docs.put(wfDocConfig.getName(), new DocInfo());
			}
		}

		protected DocInfo getDocInfo(String docName) {
			if (!StringUtils.isBlank(docName)) {
				return docs.get(docName);
			}

			return null;
		}

		protected boolean isDocument(String docName) {
			if (!StringUtils.isBlank(docName)) {
				return docs.containsKey(docName);
			}

			return false;
		}
	}

	private static class WfDocValidationContext extends AbstractValidationContext {

		private List<WfDocConfig> docList;

		private List<WfDocFieldConfig> fieldList;

		private List<WfDocClassifierConfig> classifierList;

		private List<WfDocClassifierFilterConfig> classifierFilterList;

		private List<WfDocAttachmentConfig> attachmentList;

		private List<WfDocBeanMappingConfig> beanMappingList;

		private List<WfDocFieldMappingConfig> fieldMappingList;

		public WfDocValidationContext(TaskMonitor taskMonitor, String name) {
			super(taskMonitor, name);
			docList = new ArrayList<WfDocConfig>();
		}

		public void addError(String errorCode, Object... params) {
			addError(new UnifyError(errorCode, params));
		}

		public void addDocError(String errorCode, Object... params) {
			Object[] docParams = new Object[params.length + 2];
			docParams[0] = name;
			docParams[1] = docList.size() - 1;
			System.arraycopy(params, 0, docParams, 2, params.length);
			addError(new UnifyError(errorCode, docParams));
		}

		public void addFieldError(String errorCode, Object... params) {
			Object[] fldParams = new Object[params.length + 3];
			fldParams[0] = docList.size() - 1;
			fldParams[1] = docList.get(docList.size() - 1).getName();
			fldParams[2] = fieldList.size() - 1;
			System.arraycopy(params, 0, fldParams, 3, params.length);
			addError(new UnifyError(errorCode, fldParams));
		}

		public void addClassifierError(String errorCode, Object... params) {
			Object[] clsParams = new Object[params.length + 3];
			clsParams[0] = docList.size() - 1;
			clsParams[1] = docList.get(docList.size() - 1).getName();
			clsParams[2] = classifierList.size() - 1;
			System.arraycopy(params, 0, clsParams, 3, params.length);
			addError(new UnifyError(errorCode, clsParams));
		}

		public void addFilterError(String errorCode, Object... params) {
			Object[] filParams = new Object[params.length + 4];
			filParams[0] = docList.size() - 1;
			filParams[1] = docList.get(docList.size() - 1).getName();
			filParams[2] = classifierList.size() - 1;
			filParams[3] = classifierFilterList.size() - 1;
			System.arraycopy(params, 0, filParams, 4, params.length);
			addError(new UnifyError(errorCode, filParams));
		}

		public void addAttachmentError(String errorCode, Object... params) {
			Object[] clsParams = new Object[params.length + 3];
			clsParams[0] = docList.size() - 1;
			clsParams[1] = docList.get(docList.size() - 1).getName();
			clsParams[2] = attachmentList.size() - 1;
			System.arraycopy(params, 0, clsParams, 3, params.length);
			addError(new UnifyError(errorCode, clsParams));
		}

		public void addBeanMappingError(String errorCode, Object... params) {
			Object[] rMapParams = new Object[params.length + 3];
			rMapParams[0] = docList.size() - 1;
			rMapParams[1] = docList.get(docList.size() - 1).getName();
			rMapParams[2] = beanMappingList.size() - 1;
			System.arraycopy(params, 0, rMapParams, 3, params.length);
			addError(new UnifyError(errorCode, rMapParams));
		}

		public void addFieldMappingError(String errorCode, Object... params) {
			Object[] rMapParams = new Object[params.length + 4];
			rMapParams[0] = docList.size() - 1;
			rMapParams[1] = docList.get(docList.size() - 1).getName();
			rMapParams[2] = beanMappingList.size() - 1;
			rMapParams[3] = fieldMappingList.size() - 1;
			System.arraycopy(params, 0, rMapParams, 4, params.length);
			addError(new UnifyError(errorCode, rMapParams));
		}

		public void nextWfDocConfig(WfDocConfig wfDocConfig) {
			docList.add(wfDocConfig);
			fieldList = new ArrayList<WfDocFieldConfig>();
			classifierList = new ArrayList<WfDocClassifierConfig>();
			attachmentList = new ArrayList<WfDocAttachmentConfig>();
			beanMappingList = new ArrayList<WfDocBeanMappingConfig>();
			addDocInfo(wfDocConfig);
		}

		public void nextWfDocFieldConfig(WfDocFieldConfig wfDocFieldConfig) {
			fieldList.add(wfDocFieldConfig);

			if (!StringUtils.isBlank(wfDocFieldConfig.getName())) {
				DocInfo docInfo = getDocInfo();
				if (docInfo != null) {
					docInfo.addField(wfDocFieldConfig.getName());
				}
			}
		}

		public void nextWfDocClassifierConfig(WfDocClassifierConfig wfDocClassifierConfig) {
			classifierList.add(wfDocClassifierConfig);
			classifierFilterList = new ArrayList<WfDocClassifierFilterConfig>();

			if (!StringUtils.isBlank(wfDocClassifierConfig.getName())) {
				DocInfo docInfo = getDocInfo();
				if (docInfo != null) {
					docInfo.addClassifier(wfDocClassifierConfig.getName());
				}
			}
		}

		public void nextWfDocClassifierFilterConfig(
				WfDocClassifierFilterConfig wfDocClassifierFilterConfig) {
			classifierFilterList.add(wfDocClassifierFilterConfig);
		}

		public void nextWfDocAttachmentConfig(WfDocAttachmentConfig wfDocAttachmentConfig) {
			attachmentList.add(wfDocAttachmentConfig);

			if (!StringUtils.isBlank(wfDocAttachmentConfig.getName())) {
				DocInfo docInfo = getDocInfo();
				if (docInfo != null) {
					docInfo.addAttachment(wfDocAttachmentConfig.getName());
				}
			}
		}

		public void nextWfDocBeanMappingConfig(WfDocBeanMappingConfig wfDocBeanMappingConfig) {
			beanMappingList.add(wfDocBeanMappingConfig);
			fieldMappingList = new ArrayList<WfDocFieldMappingConfig>();

			if (!StringUtils.isBlank(wfDocBeanMappingConfig.getName())) {
				DocInfo docInfo = getDocInfo();
				if (docInfo != null) {
					docInfo.addBeanMapping(wfDocBeanMappingConfig.getName());
				}
			}
		}

		public void nextWfDocFieldMappingConfig(WfDocFieldMappingConfig wfDocFieldMappingConfig) {
			fieldMappingList.add(wfDocFieldMappingConfig);
		}

		public int matchWfDocConfig(WfDocConfig wfDocConfig) {
			return matchConfig(docList, wfDocConfig);
		}

		public int matchWfDocFieldConfig(WfDocFieldConfig wfDocFieldConfig) {
			return matchConfig(fieldList, wfDocFieldConfig);
		}

		public int matchWfDocClassifierConfig(WfDocClassifierConfig wfDocClassifierConfig) {
			return matchConfig(classifierList, wfDocClassifierConfig);
		}

		public int matchWfDocAttachmentConfig(WfDocAttachmentConfig wfDocAttachmentConfig) {
			return matchConfig(attachmentList, wfDocAttachmentConfig);
		}

		public int matchWfDocBeanMappingConfig(WfDocBeanMappingConfig wfDocBeanMappingConfig) {
			return matchConfig(beanMappingList, wfDocBeanMappingConfig);
		}

		public boolean matchWfDocFieldMappingConfig(
				WfDocFieldMappingConfig wfDocFieldMappingConfig) {
			for (WfDocFieldMappingConfig eWfDocFieldMappingConfig : fieldMappingList) {
				if (!wfDocFieldMappingConfig.equals(eWfDocFieldMappingConfig)) {
					if (wfDocFieldMappingConfig.getDocFieldName() != null && wfDocFieldMappingConfig
							.getDocFieldName().equals(eWfDocFieldMappingConfig.getDocFieldName())) {
						return true;
					}
				}
			}

			return false;
		}

		public boolean matchField(String fieldName) {
			for (WfDocFieldConfig wfDocFieldConfig : fieldList) {
				if (fieldName.equals(wfDocFieldConfig.getName())) {
					return true;
				}
			}

			return false;
		}

		private DocInfo getDocInfo() {
			WfDocConfig wfDocConfig = docList.get(docList.size() - 1);
			if (!StringUtils.isBlank(wfDocConfig.getName())) {
				return getDocInfo(wfDocConfig.getName());
			}

			return null;
		}
	}

	private static class WfFormValidationContext extends AbstractValidationContext {

		private List<WfFormConfig> formList;

		private List<WfFormTabConfig> tabList;

		private List<WfFormSectionConfig> sectionList;

		private List<WfFormFieldConfig> fieldList;

		public WfFormValidationContext(TaskMonitor taskMonitor, String name,
				WfDocValidationContext wfDocValidationContext) {
			super(taskMonitor, name, wfDocValidationContext);
			formList = new ArrayList<WfFormConfig>();
		}

		public void addFormError(String errorCode, Object... params) {
			Object[] formParams = new Object[params.length + 2];
			formParams[0] = name;
			formParams[1] = formList.size() - 1;
			System.arraycopy(params, 0, formParams, 2, params.length);
			addError(new UnifyError(errorCode, formParams));
		}

		public void addTabError(String errorCode, Object... params) {
			Object[] tabParams = new Object[params.length + 3];
			tabParams[0] = formList.size() - 1;
			tabParams[1] = formList.get(formList.size() - 1).getName();
			tabParams[2] = tabList.size() - 1;
			System.arraycopy(params, 0, tabParams, 3, params.length);
			addError(new UnifyError(errorCode, tabParams));
		}

		public void addSectionError(String errorCode, Object... params) {
			Object[] secParams = new Object[params.length + 3];
			secParams[0] = formList.size() - 1;
			secParams[1] = formList.get(formList.size() - 1).getName();
			secParams[2] = sectionList.size() - 1;
			System.arraycopy(params, 0, secParams, 3, params.length);
			addError(new UnifyError(errorCode, secParams));
		}

		public void addFieldError(String errorCode, Object... params) {
			Object[] fldParams = new Object[params.length + 3];
			fldParams[0] = formList.size() - 1;
			fldParams[1] = formList.get(formList.size() - 1).getName();
			fldParams[2] = fieldList.size() - 1;
			System.arraycopy(params, 0, fldParams, 3, params.length);
			addError(new UnifyError(errorCode, fldParams));
		}

		public void nextWfFormConfig(WfFormConfig wfFormConfig) {
			formList.add(wfFormConfig);
			tabList = new ArrayList<WfFormTabConfig>();
			sectionList = new ArrayList<WfFormSectionConfig>();
			fieldList = new ArrayList<WfFormFieldConfig>();
			DocInfo docInfo = getDocInfo(wfFormConfig.getDocument());
			if (docInfo != null) {
				docInfo.addFormInfo(wfFormConfig);
			}
		}

		public void nextWfFormTabConfig(WfFormTabConfig wfFormTabConfig) {
			tabList.add(wfFormTabConfig);

			if (!StringUtils.isBlank(wfFormTabConfig.getName())) {
				FormInfo formInfo = getFormInfo();
				if (formInfo != null) {
					formInfo.addTab(wfFormTabConfig.getName());
				}
			}
		}

		public void nextWfFormSectionConfig(WfFormSectionConfig wfFormSectionConfig) {
			sectionList.add(wfFormSectionConfig);

			if (!StringUtils.isBlank(wfFormSectionConfig.getName())) {
				FormInfo formInfo = getFormInfo();
				if (formInfo != null) {
					formInfo.addSection(wfFormSectionConfig.getName());
				}
			}
		}

		public void nextWfFormFieldConfig(WfFormFieldConfig wfFormFieldConfig) {
			fieldList.add(wfFormFieldConfig);

			if (!StringUtils.isBlank(wfFormFieldConfig.getBinding())) {
				FormInfo formInfo = getFormInfo();
				if (formInfo != null) {
					formInfo.addField(wfFormFieldConfig.getBinding());
				}
			}
		}

		public int matchWfFormConfig(WfFormConfig wfFormConfig) {
			return matchConfig(formList, wfFormConfig);
		}

		public int matchWfDocTabConfig(WfFormTabConfig wfDocTabConfig) {
			return matchConfig(tabList, wfDocTabConfig);
		}

		public int matchWfDocSectionConfig(WfFormSectionConfig wfDocSectionConfig) {
			return matchConfig(sectionList, wfDocSectionConfig);
		}

		public boolean matchWfFormFieldConfig(WfFormFieldConfig wfFormFieldConfig) {
			for (WfFormFieldConfig eWfDocFieldConfig : fieldList) {
				if (!wfFormFieldConfig.equals(eWfDocFieldConfig)) {
					if (wfFormFieldConfig.getBinding() != null && wfFormFieldConfig.getBinding()
							.equals(eWfDocFieldConfig.getBinding())) {
						return true;
					}
				}
			}

			return false;
		}

		private FormInfo getFormInfo() {
			WfFormConfig wfFormConfig = formList.get(formList.size() - 1);
			if (!StringUtils.isBlank(wfFormConfig.getName())) {
				DocInfo docInfo = getDocInfo(wfFormConfig.getDocument());
				if (docInfo != null) {
					return docInfo.getFormInfo(wfFormConfig.getName());
				}
			}

			return null;
		}
	}

	private static class WfMessageValidationContext extends AbstractValidationContext {

		private List<WfMessageConfig> messageList;

		private Set<String> messages;

		public WfMessageValidationContext(TaskMonitor taskMonitor, String name) {
			super(taskMonitor, name);
			messageList = new ArrayList<WfMessageConfig>();
			messages = new HashSet<String>();
		}

		public void addMessageError(String errorCode, Object... params) {
			Object[] messageParams = new Object[params.length + 2];
			messageParams[0] = name;
			messageParams[1] = messageList.size() - 1;
			System.arraycopy(params, 0, messageParams, 2, params.length);
			addError(new UnifyError(errorCode, messageParams));
		}

		public void nextWfMessageConfig(WfMessageConfig wfMessageConfig) {
			messageList.add(wfMessageConfig);
			messages.add(wfMessageConfig.getName());
		}

		public int matchWfMessageConfig(WfMessageConfig wfMessageConfig) {
			return matchConfig(messageList, wfMessageConfig);
		}

		public Set<String> getMessages() {
			return messages;
		}
	}

	private static class WfTemplateValidationContext extends AbstractValidationContext {

		private Set<String> messages;

		private List<WfTemplateConfig> templateList;

		private List<WfStepConfig> stepList;

		private List<WfEnrichmentConfig> enrichmentList;

		private List<WfPolicyConfig> policyList;

		private List<WfRoutingConfig> routingList;

		private List<WfUserActionConfig> userActionList;

		private List<WfRecordActionConfig> recordActionList;

		private List<WfAttachmentCheckConfig> attachmentCheckList;

		private List<WfFormPrivilegeConfig> formPrivilegeList;

		private List<WfAlertConfig> alertList;

		public WfTemplateValidationContext(TaskMonitor taskMonitor, String name,
				WfFormValidationContext wfFormValidationContext, Set<String> messages) {
			super(taskMonitor, name, wfFormValidationContext);
			this.messages = messages;
			templateList = new ArrayList<WfTemplateConfig>();
		}

		public void addTemplateError(String errorCode, Object... params) {
			Object[] tmplParams = new Object[params.length + 2];
			tmplParams[0] = name;
			tmplParams[1] = templateList.size() - 1;
			System.arraycopy(params, 0, tmplParams, 2, params.length);
			addError(new UnifyError(errorCode, tmplParams));
		}

		public void addStepError(String errorCode, Object... params) {
			Object[] stepParams = new Object[params.length + 3];
			stepParams[0] = templateList.size() - 1;
			stepParams[1] = templateList.get(templateList.size() - 1).getName();
			stepParams[2] = stepList.size() - 1;
			System.arraycopy(params, 0, stepParams, 3, params.length);
			addError(new UnifyError(errorCode, stepParams));
		}

		public void addEnrichmentError(String errorCode, Object... params) {
			Object[] enrichmentParams = new Object[params.length + 4];
			enrichmentParams[0] = templateList.size() - 1;
			enrichmentParams[1] = templateList.get(templateList.size() - 1).getName();
			enrichmentParams[2] = stepList.size() - 1;
			enrichmentParams[3] = enrichmentList.size() - 1;
			System.arraycopy(params, 0, enrichmentParams, 4, params.length);
			addError(new UnifyError(errorCode, enrichmentParams));
		}

		public void addRoutingError(String errorCode, Object... params) {
			Object[] routingParams = new Object[params.length + 4];
			routingParams[0] = templateList.size() - 1;
			routingParams[1] = templateList.get(templateList.size() - 1).getName();
			routingParams[2] = stepList.size() - 1;
			routingParams[3] = routingList.size() - 1;
			System.arraycopy(params, 0, routingParams, 4, params.length);
			addError(new UnifyError(errorCode, routingParams));
		}

		public void addRecordActionError(String errorCode, Object... params) {
			Object[] rActParams = new Object[params.length + 4];
			rActParams[0] = templateList.size() - 1;
			rActParams[1] = templateList.get(templateList.size() - 1).getName();
			rActParams[2] = stepList.size() - 1;
			rActParams[3] = recordActionList.size() - 1;
			System.arraycopy(params, 0, rActParams, 4, params.length);
			addError(new UnifyError(errorCode, rActParams));
		}

		public void addUserActionError(String errorCode, Object... params) {
			Object[] uActParams = new Object[params.length + 4];
			uActParams[0] = templateList.size() - 1;
			uActParams[1] = templateList.get(templateList.size() - 1).getName();
			uActParams[2] = stepList.size() - 1;
			uActParams[3] = userActionList.size() - 1;
			System.arraycopy(params, 0, uActParams, 4, params.length);
			addError(new UnifyError(errorCode, uActParams));
		}

		public void addPolicyError(String errorCode, Object... params) {
			Object[] policyParams = new Object[params.length + 4];
			policyParams[0] = templateList.size() - 1;
			policyParams[1] = templateList.get(templateList.size() - 1).getName();
			policyParams[2] = stepList.size() - 1;
			policyParams[3] = policyList.size() - 1;
			System.arraycopy(params, 0, policyParams, 4, params.length);
			addError(new UnifyError(errorCode, policyParams));
		}

		public void addAlertError(String errorCode, Object... params) {
			Object[] alertParams = new Object[params.length + 4];
			alertParams[0] = templateList.size() - 1;
			alertParams[1] = templateList.get(templateList.size() - 1).getName();
			alertParams[2] = stepList.size() - 1;
			alertParams[3] = alertList.size() - 1;
			System.arraycopy(params, 0, alertParams, 4, params.length);
			addError(new UnifyError(errorCode, alertParams));
		}

		public void addAttachmentCheckError(String errorCode, Object... params) {
			Object[] actionParams = new Object[params.length + 6];
			actionParams[0] = templateList.size() - 1;
			actionParams[1] = templateList.get(templateList.size() - 1).getName();
			actionParams[2] = stepList.size() - 1;
			actionParams[3] = userActionList.size() - 1;
			actionParams[4] = userActionList.get(userActionList.size() - 1).getName();
			actionParams[5] = attachmentCheckList.size() - 1;
			System.arraycopy(params, 0, actionParams, 6, params.length);
			addError(new UnifyError(errorCode, actionParams));
		}

		public void addFormPrivilegeError(String errorCode, Object... params) {
			Object[] fpParams = new Object[params.length + 4];
			fpParams[0] = templateList.size() - 1;
			fpParams[1] = templateList.get(templateList.size() - 1).getName();
			fpParams[2] = stepList.size() - 1;
			fpParams[3] = formPrivilegeList.size() - 1;
			System.arraycopy(params, 0, fpParams, 4, params.length);
			addError(new UnifyError(errorCode, fpParams));
		}

		public void nextWfTemplateConfig(WfTemplateConfig wfTemplateConfig) {
			templateList.add(wfTemplateConfig);
			stepList = new ArrayList<WfStepConfig>();
		}

		public void nextWfStepConfig(WfStepConfig wfStepConfig) {
			stepList.add(wfStepConfig);
			enrichmentList = new ArrayList<WfEnrichmentConfig>();
			routingList = new ArrayList<WfRoutingConfig>();
			userActionList = new ArrayList<WfUserActionConfig>();
			recordActionList = new ArrayList<WfRecordActionConfig>();
			formPrivilegeList = new ArrayList<WfFormPrivilegeConfig>();
			policyList = new ArrayList<WfPolicyConfig>();
			alertList = new ArrayList<WfAlertConfig>();
		}

		public void nextWfEnrichmentConfig(WfEnrichmentConfig wfEnrichmentConfig) {
			enrichmentList.add(wfEnrichmentConfig);
		}

		public void nextWfRoutingConfig(WfRoutingConfig wfRoutingConfig) {
			routingList.add(wfRoutingConfig);
		}

		public void nextWfPolicyConfig(WfPolicyConfig wfPolicyConfig) {
			policyList.add(wfPolicyConfig);
		}

		public void nextWfAlertConfig(WfAlertConfig wfAlertConfig) {
			alertList.add(wfAlertConfig);
		}

		public void nextWfUserActionConfig(WfUserActionConfig wfUserActionConfig) {
			userActionList.add(wfUserActionConfig);
			attachmentCheckList = new ArrayList<WfAttachmentCheckConfig>();
		}

		public void nextWfRecordActionConfig(WfRecordActionConfig wfRecordActionConfig) {
			recordActionList.add(wfRecordActionConfig);
		}

		public void nextWfAttachmentCheckConfig(WfAttachmentCheckConfig wfAttachmentCheckConfig) {
			attachmentCheckList.add(wfAttachmentCheckConfig);
		}

		public void nextWfFormPrivilegeConfig(WfFormPrivilegeConfig wfFormPrivilegeConfig) {
			formPrivilegeList.add(wfFormPrivilegeConfig);
		}

		public int matchWfTemplateConfig(WfTemplateConfig wfTemplateConfig) {
			return matchConfig(templateList, wfTemplateConfig);
		}

		public int matchWfStepConfig(WfStepConfig wfStepConfig) {
			return matchConfig(stepList, wfStepConfig);
		}

		public int matchWfEnrichmentConfig(WfEnrichmentConfig wfEnrichmentConfig) {
			return matchConfig(enrichmentList, wfEnrichmentConfig);
		}

		public int matchWfRoutingConfig(WfRoutingConfig wfRoutingConfig) {
			return matchConfig(routingList, wfRoutingConfig);
		}

		public int matchWfPolicyConfig(WfPolicyConfig wfPolicyConfig) {
			return matchConfig(policyList, wfPolicyConfig);
		}

		public int matchWfAlertConfig(WfAlertConfig wfAlertConfig) {
			return matchConfig(alertList, wfAlertConfig);
		}

		public int matchWfUserActionConfig(WfUserActionConfig wfUserActionConfig) {
			return matchConfig(userActionList, wfUserActionConfig);
		}

		public int matchWfRecordActionConfig(WfRecordActionConfig wfRecordActionConfig) {
			return matchConfig(recordActionList, wfRecordActionConfig);
		}

		public boolean matchWfAttachmentCheckConfig(
				WfAttachmentCheckConfig wfAttachmentCheckConfig) {
			if (!StringUtils.isBlank(wfAttachmentCheckConfig.getAttachmentName())) {
				for (WfAttachmentCheckConfig eWfAttachmentCheckConfig : attachmentCheckList) {
					if (wfAttachmentCheckConfig != eWfAttachmentCheckConfig
							&& wfAttachmentCheckConfig.getAttachmentName()
									.equals(eWfAttachmentCheckConfig.getAttachmentName())) {
						return true;
					}
				}
			}
			return false;
		}

		public boolean matchWfFormPrivilegeConfig(WfFormPrivilegeConfig wfFormPrivilegeConfig) {
			if (!StringUtils.isBlank(wfFormPrivilegeConfig.getName())) {
				for (WfFormPrivilegeConfig eWfFormPrivilegeConfig : formPrivilegeList) {
					if (wfFormPrivilegeConfig != eWfFormPrivilegeConfig && wfFormPrivilegeConfig
							.getName().equals(eWfFormPrivilegeConfig.getName())) {
						return true;
					}
				}
			}

			return false;
		}

		public boolean matchMessage(String name) {
			return messages.contains(name);
		}

		public boolean matchBeanMapping(String name) {
			DocInfo docInfo = getDocInfo();
			if (docInfo != null) {
				return docInfo.isBeanMapping(name);
			}

			return false;
		}

		public boolean matchClassifier(String name) {
			DocInfo docInfo = getDocInfo();
			if (docInfo != null) {
				return docInfo.isClassifier(name);
			}

			return false;
		}

		public boolean matchAttachment(String name) {
			DocInfo docInfo = getDocInfo();
			if (docInfo != null) {
				return docInfo.isAttachment(name);
			}

			return false;
		}

		public boolean matchForm(String formName) {
			return getDocInfo().isForm(formName);
		}

		public boolean matchFormField(String formName, String name) {
			FormInfo formInfo = getDocInfo().getFormInfo(formName);
			if (formInfo != null) {
				return formInfo.isField(name);
			}

			return false;
		}

		public boolean matchFormSection(String formName, String name) {
			FormInfo formInfo = getDocInfo().getFormInfo(formName);
			if (formInfo != null) {
				return formInfo.isSection(name);
			}

			return false;
		}

		public boolean matchFormTab(String formName, String name) {
			FormInfo formInfo = getDocInfo().getFormInfo(formName);
			if (formInfo != null) {
				return formInfo.isTab(name);
			}

			return false;
		}

		public boolean isDoc() {
			return getDocInfo() != null;
		}

		private DocInfo getDocInfo() {
			WfTemplateConfig wfTemplateConfig = templateList.get(templateList.size() - 1);
			if (!StringUtils.isBlank(wfTemplateConfig.getWfDocName())) {
				return getDocInfo(wfTemplateConfig.getWfDocName());
			}

			return null;
		}
	}

	private static class DocInfo {

		private Set<String> classifiers;

		private Set<String> attachments;

		private Set<String> beanMappings;

		private Set<String> fields;

		private Map<String, FormInfo> forms;

		public DocInfo() {
			classifiers = new HashSet<String>();
			attachments = new HashSet<String>();
			beanMappings = new HashSet<String>();
			fields = new HashSet<String>();
			forms = new HashMap<String, FormInfo>();
		}

		public void addAttachment(String name) {
			attachments.add(name);
		}

		public void addClassifier(String name) {
			classifiers.add(name);
		}

		public void addBeanMapping(String name) {
			beanMappings.add(name);
		}

		public void addField(String name) {
			fields.add(name);
		}

		public boolean isAttachment(String name) {
			return attachments.contains(name);
		}

		public boolean isClassifier(String name) {
			return classifiers.contains(name);
		}

		public boolean isBeanMapping(String name) {
			return beanMappings.contains(name);
		}
		//
		// public boolean isField(String name) {
		// return fields.contains(name);
		// }

		public void addFormInfo(WfFormConfig wfFormConfig) {
			if (forms == null) {
				forms = new HashMap<String, FormInfo>();
			}

			if (!StringUtils.isBlank(wfFormConfig.getName())) {
				forms.put(wfFormConfig.getName(), new FormInfo());
			}
		}

		public FormInfo getFormInfo(String formName) {
			if (!StringUtils.isBlank(formName)) {
				return forms.get(formName);
			}

			return null;
		}

		public boolean isForm(String formName) {
			if (!StringUtils.isBlank(formName)) {
				return forms.containsKey(formName);
			}

			return false;
		}

	}

	private static class FormInfo {

		private Set<String> fields;

		private Set<String> sections;

		private Set<String> tabs;

		public FormInfo() {
			fields = new HashSet<String>();
			sections = new HashSet<String>();
			tabs = new HashSet<String>();
		}

		public void addField(String name) {
			fields.add(name);
		}

		public void addSection(String name) {
			sections.add(name);
		}

		public void addTab(String name) {
			tabs.add(name);
		}

		public boolean isField(String name) {
			return fields.contains(name);
		}

		public boolean isSection(String name) {
			return sections.contains(name);
		}

		public boolean isTab(String name) {
			return tabs.contains(name);
		}
	}
}
