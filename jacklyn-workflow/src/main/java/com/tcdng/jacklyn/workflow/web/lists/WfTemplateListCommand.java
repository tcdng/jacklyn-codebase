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

package com.tcdng.jacklyn.workflow.web.lists;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.data.FilterParams;

/**
 * Workflow template list command.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("wftemplatelist")
public class WfTemplateListCommand extends AbstractWorkflowListCommand<FilterParams> {

    public WfTemplateListCommand() {
        super(FilterParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, FilterParams params) throws UnifyException {
        if (QueryUtils.isValidLongCriteria(params.getFilterId())) {
            return getWorkflowModule().findWfTemplates(params.getFilterId());
        }

        return Collections.emptyList();
    }

}
