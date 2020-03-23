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
package com.tcdng.jacklyn.archiving.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.archiving.entities.FileArchiveConfig;
import com.tcdng.jacklyn.archiving.entities.FileArchiveConfigQuery;
import com.tcdng.jacklyn.archiving.web.beans.FileArchiveConfigPageBean;
import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing file archive configuration record.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/archiving/filearchiveconfig")
@UplBinding("web/archiving/upl/managefilearchiveconfig.upl")
public class FileArchiveConfigController
        extends AbstractArchivingFormController<FileArchiveConfigPageBean, FileArchiveConfig> {

    public FileArchiveConfigController() {
        super(FileArchiveConfigPageBean.class, FileArchiveConfig.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.CLIPBOARD
                        | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<FileArchiveConfig> find() throws UnifyException {
        FileArchiveConfigPageBean pageBean = getPageBean();
        FileArchiveConfigQuery query = new FileArchiveConfigQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.ignoreEmptyCriteria(true);
        return getArchivingService().findFileArchiveConfigs(query);
    }

    @Override
    protected FileArchiveConfig find(Long id) throws UnifyException {
        return getArchivingService().findFileArchiveConfig(id);
    }

    @Override
    protected Object create(FileArchiveConfig fileArchiveConfigData) throws UnifyException {
        return getArchivingService().createFileArchiveConfig(fileArchiveConfigData);
    }

    @Override
    protected int update(FileArchiveConfig fileArchiveConfigData) throws UnifyException {
        return getArchivingService().updateFileArchiveConfig(fileArchiveConfigData);
    }

    @Override
    protected int delete(FileArchiveConfig fileArchiveConfigData) throws UnifyException {
        return getArchivingService().deleteFileArchiveConfig(fileArchiveConfigData.getId());
    }
}
