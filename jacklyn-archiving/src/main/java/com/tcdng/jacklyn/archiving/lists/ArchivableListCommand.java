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
package com.tcdng.jacklyn.archiving.lists;

import java.util.List;
import java.util.Locale;

import com.tcdng.jacklyn.archiving.entities.ArchivableDefinitionQuery;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.list.StatusParams;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Archivable list command.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("archivabledeflist")
public class ArchivableListCommand extends AbstractArchivingListCommand<StatusParams> {

    public ArchivableListCommand() {
        super(StatusParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, StatusParams params) throws UnifyException {
        if (!StringUtils.isBlank(params.getStatus())) {
            return getArchivingBusinessModule()
                    .findArchivableDefinitions((ArchivableDefinitionQuery) new ArchivableDefinitionQuery()
                            .orderByDescription().status(RecordStatus.fromName(params.getStatus())));
        }

        return getArchivingBusinessModule()
                .findArchivableDefinitions((ArchivableDefinitionQuery) new ArchivableDefinitionQuery()
                        .orderByDescription().ignoreEmptyCriteria(true));
    }
}
