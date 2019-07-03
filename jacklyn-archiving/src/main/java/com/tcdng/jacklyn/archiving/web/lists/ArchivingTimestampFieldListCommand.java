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
package com.tcdng.jacklyn.archiving.web.lists;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.tcdng.jacklyn.archiving.entities.ArchivingFieldQuery;
import com.tcdng.jacklyn.shared.archiving.ArchivingFieldType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Archiving timestamp field list command.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("archivingtimestampfieldlist")
public class ArchivingTimestampFieldListCommand extends AbstractArchivingListCommand<ArchivingParams> {

    public ArchivingTimestampFieldListCommand() {
        super(ArchivingParams.class);
    }

    private static final ArchivingFieldType[] TIME = { ArchivingFieldType.DATE, ArchivingFieldType.TIMESTAMP_UTC };

    @Override
    public List<? extends Listable> execute(Locale locale, ArchivingParams params) throws UnifyException {
        if (QueryUtils.isValidLongCriteria(params.getArchivableDefId())) {
            ArchivingFieldQuery query = new ArchivingFieldQuery();
            query.archivableDefId(params.getArchivableDefId());
            query.fieldTypeIn(TIME);
            query.installed(Boolean.TRUE);
            query.orderByDescription();
            return getArchivingService().findArchivingFields(query);
        }

        return Collections.emptyList();
    }

}
