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
package com.tcdng.jacklyn.archiving.controllers;

import java.util.List;

import com.tcdng.jacklyn.archiving.entities.ArchivableDefinition;
import com.tcdng.jacklyn.archiving.entities.ArchivableDefinitionQuery;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing archivable definition record.
 *
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/archiving/archivabledefinition")
@UplBinding("web/archiving/upl/managearchivabledefinitions.upl")
public class ArchivableDefinitionController extends AbstractArchivingRecordController<ArchivableDefinition> {

    private Long searchModuleId;

    private RecordStatus searchStatus;

    public ArchivableDefinitionController() {
        super(ArchivableDefinition.class, "archiving.archivabledefinition.hint",
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    public Long getSearchModuleId() {
        return searchModuleId;
    }

    public void setSearchModuleId(Long searchModuleId) {
        this.searchModuleId = searchModuleId;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    @Override
    protected List<ArchivableDefinition> find() throws UnifyException {
        ArchivableDefinitionQuery query = new ArchivableDefinitionQuery();
        if (QueryUtils.isValidLongCriteria(searchModuleId)) {
            query.moduleId(searchModuleId);
        }
        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.ignoreEmptyCriteria(true);
        return getArchivingService().findArchivableDefinitions(query);
    }

    @Override
    protected ArchivableDefinition find(Long id) throws UnifyException {
        return getArchivingService().findArchivableDefinition(id);
    }

    @Override
    protected ArchivableDefinition prepareCreate() throws UnifyException {
        return null;
    }

    @Override
    protected Object create(ArchivableDefinition record) throws UnifyException {
        return null;
    }

    @Override
    protected int update(ArchivableDefinition record) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(ArchivableDefinition record) throws UnifyException {
        return 0;
    }

}
