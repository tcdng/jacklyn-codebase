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
package com.tcdng.jacklyn.file.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.file.entities.FileOutbox;
import com.tcdng.jacklyn.file.entities.FileOutboxQuery;
import com.tcdng.jacklyn.file.web.beans.FileOutboxPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing file outbox item record.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/file/fileoutbox")
@UplBinding("web/file/upl/managefileoutbox.upl")
public class FileOutboxController extends AbstractFileTransferBoxFormController<FileOutboxPageBean, FileOutbox> {

    public FileOutboxController() {
        super(FileOutboxPageBean.class, FileOutbox.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<FileOutbox> find() throws UnifyException {
        FileOutboxPageBean pageBean = getPageBean();
        FileOutboxQuery query = new FileOutboxQuery();
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchFileTransferConfigId())) {
            query.fileTransferConfigId(pageBean.getSearchFileTransferConfigId());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }

        query.createdOn(pageBean.getSearchCreateDt());
        return getFileService().findFileOutboxItems(query);
    }

    @Override
    protected FileOutbox find(Long id) throws UnifyException {
        return getFileService().findFileOutboxItem(id);
    }
}
