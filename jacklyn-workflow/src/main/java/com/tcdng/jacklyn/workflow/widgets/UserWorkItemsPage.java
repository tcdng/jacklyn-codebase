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

package com.tcdng.jacklyn.workflow.widgets;

import java.util.Set;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.web.ui.container.BasicPage;

/**
 * Specialized page for managing user workflow items.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("ui-userworkitemspage")
public class UserWorkItemsPage extends BasicPage {

    private Set<String> pageValidationActions;

    public void setPageValidationActions(Set<String> pageValidationActions) {
        this.pageValidationActions = pageValidationActions;
    }

    @Override
    public boolean isValidationEnabled() throws UnifyException {
        String actionName = this.getRequestTarget(String.class);
        if (actionName != null) {
            return this.pageValidationActions != null && this.pageValidationActions.contains(actionName);
        }

        return false;
    }

}
