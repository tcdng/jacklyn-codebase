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
package com.tcdng.jacklyn.common.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcdng.jacklyn.common.annotation.CrudPanelList;
import com.tcdng.jacklyn.common.annotation.SessionAttr;
import com.tcdng.jacklyn.common.annotation.SessionLoading;
import com.tcdng.jacklyn.common.constants.CommonModuleNameConstants;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.web.PageController;
import com.tcdng.unify.web.ui.panel.TableCrudPanel;

/**
 * Page controller session utilities.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component(CommonModuleNameConstants.PAGECONTROLLERSESSIONUTILS)
public class PageControllerSessionUtilsImpl extends AbstractUnifyComponent
		implements PageControllerSessionUtils {

	private static final SessionLoadingConfig BLANK_CONFIG = new SessionLoadingConfig();

	private FactoryMap<Class<? extends PageController>, SessionLoadingConfig> sessionLoadingConfigs;

	public PageControllerSessionUtilsImpl() {
		sessionLoadingConfigs
				= new FactoryMap<Class<? extends PageController>, SessionLoadingConfig>() {

					@Override
					protected SessionLoadingConfig create(Class<? extends PageController> type,
							Object... params) throws Exception {
						SessionLoadingConfig sessionLoadingConfig = BLANK_CONFIG;
						SessionLoading slsa = type.getAnnotation(SessionLoading.class);
						if (slsa != null) {
							Map<String, String> setAttributeConfigs = new HashMap<String, String>();
							for (SessionAttr saa : slsa.sessionAttributes()) {
								setAttributeConfigs.put(saa.name(), saa.field());
							}

							Map<String, String> crudPanelConfigs = new HashMap<String, String>();
							for (CrudPanelList cpl : slsa.crudPanelLists()) {
								crudPanelConfigs.put(cpl.panel(), cpl.field());
							}

							sessionLoadingConfig = new SessionLoadingConfig(setAttributeConfigs,
									crudPanelConfigs);
						}
						return sessionLoadingConfig;
					}
				};
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> void loadSession(PageController pageController)
			throws UnifyException {
		SessionLoadingConfig slc = sessionLoadingConfigs.get(pageController.getClass());
		for (Map.Entry<String, String> entry : slc.getSetAttributeConfigs().entrySet()) {
			setSessionAttribute(entry.getKey(),
					ReflectUtils.getNestedBeanProperty(pageController, entry.getValue()));
		}

		for (Map.Entry<String, String> entry : slc.getCrudPanelConfigs().entrySet()) {
			((TableCrudPanel<T>) pageController.getPage().getWidgetByShortName(entry.getKey()))
					.setRecordList((List<T>) ReflectUtils.getNestedBeanProperty(pageController,
							entry.getValue()));
		}
	}

	@Override
	public void unloadSession(PageController pageController) throws UnifyException {
		SessionLoadingConfig slc = sessionLoadingConfigs.get(pageController.getClass());
		removeSessionAttributes(slc.getSetAttributeConfigs().keySet().toArray(new String[0]));
	}

	@Override
	protected void onInitialize() throws UnifyException {

	}

	@Override
	protected void onTerminate() throws UnifyException {

	}

	private static class SessionLoadingConfig {

		private Map<String, String> setAttributeConfigs;

		private Map<String, String> crudPanelConfigs;

		public SessionLoadingConfig() {
			setAttributeConfigs = Collections.emptyMap();
			crudPanelConfigs = Collections.emptyMap();
		}

		public SessionLoadingConfig(Map<String, String> setAttributeConfigs,
				Map<String, String> crudPanelConfigs) {
			this.setAttributeConfigs = setAttributeConfigs;
			this.crudPanelConfigs = crudPanelConfigs;
		}

		public Map<String, String> getSetAttributeConfigs() {
			return setAttributeConfigs;
		}

		public Map<String, String> getCrudPanelConfigs() {
			return crudPanelConfigs;
		}
	}
}
