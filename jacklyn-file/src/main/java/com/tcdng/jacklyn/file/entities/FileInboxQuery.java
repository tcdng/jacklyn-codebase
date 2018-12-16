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
package com.tcdng.jacklyn.file.entities;

import com.tcdng.jacklyn.shared.file.FileInboxReadStatus;
import com.tcdng.jacklyn.shared.file.FileInboxStatus;

/**
 * Query class for file inbox items.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class FileInboxQuery extends AbstractFileBoxDataQuery<FileInbox> {

    public FileInboxQuery() {
        super(FileInbox.class);
    }

    public FileInboxQuery readStatus(FileInboxReadStatus readStatus) {
        return (FileInboxQuery) equals("readStatus", readStatus);
    }

    public FileInboxQuery readStatusNot(FileInboxReadStatus readStatus) {
        return (FileInboxQuery) equals("readStatus", readStatus);
    }

    public FileInboxQuery status(FileInboxStatus status) {
        return (FileInboxQuery) equals("status", status);
    }

    public FileInboxQuery statusNot(FileInboxStatus status) {
        return (FileInboxQuery) notEqual("status", status);
    }
}
