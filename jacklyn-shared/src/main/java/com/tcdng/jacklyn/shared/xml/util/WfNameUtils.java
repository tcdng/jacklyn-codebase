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

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Workflow name utilities.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public final class WfNameUtils {

	private static FactoryMap<String, DocNameParts> docNames;

	private static FactoryMap<String, TemplateNameParts> templateNames;

	private static FactoryMap<String, StepNameParts> stepNames;

	static {
		docNames = new FactoryMap<String, DocNameParts>() {

			@Override
			protected DocNameParts create(String globalName, Object... params) throws Exception {
				String[] names = StringUtils.dotSplit(globalName);
				return new DocNameParts(names[0], names[1]);
			}

		};

		templateNames = new FactoryMap<String, TemplateNameParts>() {

			@Override
			protected TemplateNameParts create(String globalName, Object... params) throws Exception {
				String[] names = StringUtils.dotSplit(globalName);
				return new TemplateNameParts(names[0], names[1]);
			}

		};

		stepNames = new FactoryMap<String, StepNameParts>() {

			@Override
			protected StepNameParts create(String globalName, Object... params) throws Exception {
				String[] names = StringUtils.dotSplit(globalName);
				return new StepNameParts(StringUtils.dotify(names[0], names[1]), names[2]);
			}

		};
	}

	private WfNameUtils() {

	}

	public static boolean isValidName(String name) {
		return !StringUtils.isBlank(name) && !StringUtils.containsWhitespace(name);
	}

	public static String getGlobalDocName(String categoryName, String docName) {
		return StringUtils.dotify(categoryName, docName);
	}

	public static String getGlobalTemplateName(String categoryName, String templateName) {
		return StringUtils.dotify(categoryName, templateName);
	}

	public static String getGlobalMessageName(String categoryName, String templateName, String messageName) {
		return StringUtils.dotify(categoryName, templateName, messageName);
	}

	public static String getGlobalStepName(String categoryName, String templateName, String stepName) {
		return StringUtils.dotify(categoryName, templateName, stepName);
	}

	public static DocNameParts getDocNameParts(String globalName) throws UnifyException {
		return docNames.get(globalName);
	}

	public static TemplateNameParts getTemplateNameParts(String globalName) throws UnifyException {
		return templateNames.get(globalName);
	}

	public static StepNameParts getStepNameParts(String globalName) throws UnifyException {
		return stepNames.get(globalName);
	}

	public static class DocNameParts {

		private String categoryName;

		private String docName;

		public DocNameParts(String categoryName, String docName) {
			this.categoryName = categoryName;
			this.docName = docName;
		}

		public String getCategoryName() {
			return categoryName;
		}

		public String getDocName() {
			return docName;
		}

	}

	public static class FormNameParts {

		private String categoryName;

		private String formName;

		public FormNameParts(String categoryName, String formName) {
			this.categoryName = categoryName;
			this.formName = formName;
		}

		public String getCategoryName() {
			return categoryName;
		}

		public String getFormName() {
			return formName;
		}

	}

	public static class TemplateNameParts {

		private String categoryName;

		private String templateName;

		public TemplateNameParts(String categoryName, String templateName) {
			this.categoryName = categoryName;
			this.templateName = templateName;
		}

		public String getCategoryName() {
			return categoryName;
		}

		public String getTemplateName() {
			return templateName;
		}
	}

	public static class StepNameParts {

		private String globalTemplateName;

		private String stepName;

		public StepNameParts(String globalTemplateName, String stepName) {
			this.globalTemplateName = globalTemplateName;
			this.stepName = stepName;
		}

		public String getGlobalTemplateName() {
			return globalTemplateName;
		}

		public String getStepName() {
			return stepName;
		}

	}
}
