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
package com.tcdng.jacklyn.file.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.file.entities.FileOutbox;
import com.tcdng.jacklyn.file.entities.FileOutboxQuery;
import com.tcdng.jacklyn.shared.file.FileOutboxStatus;
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
public class FileOutboxController extends AbstractFileTransferBoxController<FileOutbox> {

    private FileOutboxStatus searchStatus;

    public FileOutboxController() {
        super(FileOutbox.class, "file.fileoutbox.hint",
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    public FileOutboxStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(FileOutboxStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    @Override
    protected List<FileOutbox> find() throws UnifyException {
        FileOutboxQuery query = new FileOutboxQuery();
        if (QueryUtils.isValidLongCriteria(getSearchFileTransferConfigId())) {
            query.fileTransferConfigId(getSearchFileTransferConfigId());
        }

        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }

        query.createdOn(getSearchCreateDt());
        return getFileModule().findFileOutboxItems(query);
    }

    @Override
    protected FileOutbox find(Long id) throws UnifyException {
        return getFileModule().findFileOutboxItem(id);
    }
}
