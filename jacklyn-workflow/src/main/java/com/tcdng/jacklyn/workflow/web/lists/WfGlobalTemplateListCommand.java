/*
 * Copyright 2018-2020 The Code Department.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.shared.xml.util.WfNameUtils;
import com.tcdng.jacklyn.workflow.entities.WfTemplate;
import com.tcdng.jacklyn.workflow.entities.WfTemplateQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.list.ZeroParams;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Workflow global template list command.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("wfglobaltemplatelist")
public class WfGlobalTemplateListCommand extends AbstractZeroParamsWorkflowListCommand {

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams params) throws UnifyException {
        List<WfTemplate> templateList =
                getWorkflowModule().findWfTemplates(new WfTemplateQuery().wfCategoryStatus(RecordStatus.ACTIVE));
        if (DataUtils.isNotBlank(templateList)) {
            List<ListData> list = new ArrayList<ListData>();
            for (WfTemplate wfTemplate : templateList) {
                list.add(new ListData(
                        WfNameUtils.getTemplateGlobalName(wfTemplate.getWfCategoryName(), wfTemplate.getName()),
                        wfTemplate.getDescription()));
            }
            return list;
        }

        return Collections.emptyList();
    }

}
