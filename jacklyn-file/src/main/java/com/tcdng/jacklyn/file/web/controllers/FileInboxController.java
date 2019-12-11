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
package com.tcdng.jacklyn.file.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.file.constants.FileModuleAuditConstants;
import com.tcdng.jacklyn.file.entities.FileInbox;
import com.tcdng.jacklyn.file.entities.FileInboxQuery;
import com.tcdng.jacklyn.file.web.beans.FileInboxPageBean;
import com.tcdng.jacklyn.shared.file.FileInboxReadStatus;
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
public class FileInboxController extends AbstractFileTransferBoxFormController<FileInboxPageBean, FileInbox> {

    public FileInboxController() {
        super(FileInboxPageBean.class, FileInbox.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
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
        FileInboxPageBean pageBean = getPageBean();
        FileInboxQuery query = new FileInboxQuery();
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchFileTransferConfigId())) {
            query.fileTransferConfigId(pageBean.getSearchFileTransferConfigId());
        }

        if (pageBean.getSearchReadStatus() != null) {
            query.readStatus(pageBean.getSearchReadStatus());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }

        query.createdOn(pageBean.getSearchCreateDt());
        return getFileService().findFileInboxItems(query);
    }

    @Override
    protected FileInbox find(Long id) throws UnifyException {
        return getFileService().findFileInboxItem(id);
    }
}
