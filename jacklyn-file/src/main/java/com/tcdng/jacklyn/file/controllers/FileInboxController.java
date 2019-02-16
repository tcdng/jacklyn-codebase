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
package com.tcdng.jacklyn.file.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.file.constants.FileModuleAuditConstants;
import com.tcdng.jacklyn.file.entities.FileInbox;
import com.tcdng.jacklyn.file.entities.FileInboxQuery;
import com.tcdng.jacklyn.shared.file.FileInboxReadStatus;
import com.tcdng.jacklyn.shared.file.FileInboxStatus;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;

/**
 * Controller for managing file inbox item record.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/file/fileinbox")
@UplBinding("web/file/upl/managefileinbox.upl")
public class FileInboxController extends AbstractFileTransferBoxController<FileInbox> {

    private FileInboxReadStatus searchReadStatus;

    private FileInboxStatus searchStatus;

    public FileInboxController() {
        super(FileInbox.class, "$m{file.fileinbox.hint}",
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    public FileInboxReadStatus getSearchReadStatus() {
        return searchReadStatus;
    }

    public void setSearchReadStatus(FileInboxReadStatus searchReadStatus) {
        this.searchReadStatus = searchReadStatus;
    }

    public FileInboxStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(FileInboxStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    @Action
    public String markRead() throws UnifyException {
        List<Long> fileInboxIds = getSelectedIds();
        if (!fileInboxIds.isEmpty()) {
            getFileService().updateFileInboxItemsReadStatus((FileInboxQuery) new FileInboxQuery().idIn(fileInboxIds),
                    FileInboxReadStatus.READ);
            logUserEvent(FileModuleAuditConstants.FILEINBOX_MARKREAD, getSelectedDescription());
            hintUser("$m{hint.records.markedread}");
        }
        return findRecords();
    }

    @Action
    public String markUnread() throws UnifyException {
        List<Long> fileInboxIds = getSelectedIds();
        if (!fileInboxIds.isEmpty()) {
            getFileService().updateFileInboxItemsReadStatus((FileInboxQuery) new FileInboxQuery().idIn(fileInboxIds),
                    FileInboxReadStatus.NOT_READ);
            logUserEvent(FileModuleAuditConstants.FILEINBOX_MARKUNREAD, getSelectedDescription());
            hintUser("$m{hint.records.markedunread}");
        }
        return findRecords();
    }

    @Override
    protected List<FileInbox> find() throws UnifyException {
        FileInboxQuery query = new FileInboxQuery();
        if (QueryUtils.isValidLongCriteria(getSearchFileTransferConfigId())) {
            query.fileTransferConfigId(getSearchFileTransferConfigId());
        }

        if (getSearchReadStatus() != null) {
            query.readStatus(getSearchReadStatus());
        }

        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }

        query.createdOn(getSearchCreateDt());
        return getFileService().findFileInboxItems(query);
    }

    @Override
    protected FileInbox find(Long id) throws UnifyException {
        return getFileService().findFileInboxItem(id);
    }
}
