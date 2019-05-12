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

package com.tcdng.jacklyn.notification.utils;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Notification utilities.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public final class NotificationUtils {

    private static FactoryMap<String, TemplateNameParts> templateNames;

    static {
        templateNames = new FactoryMap<String, TemplateNameParts>() {

            @Override
            protected TemplateNameParts create(String globalName, Object... params) throws Exception {
                String[] names = StringUtils.dotSplit(globalName);
                if (names.length > 2) {                    
                    return new TemplateNameParts(names[0], StringUtils.dotify(names[1], names[2]));
                }
                
                return new TemplateNameParts(names[0], names[1]);
            }

        };
    }

    private NotificationUtils() {

    }

    public static String getTemplateGlobalName(String moduleName, String templateName) {
        return StringUtils.dotify(moduleName, templateName);
    }

    public static TemplateNameParts getTemplateNameParts(String templateGlobalName) throws UnifyException {
        return templateNames.get(templateGlobalName);
    }

    public static class TemplateNameParts {

        private String moduleName;

        private String templateName;

        public TemplateNameParts(String moduleName, String templateName) {
            this.moduleName = moduleName;
            this.templateName = templateName;
        }

        public String getModuleName() {
            return moduleName;
        }

        public String getTemplateName() {
            return templateName;
        }
    }
}
