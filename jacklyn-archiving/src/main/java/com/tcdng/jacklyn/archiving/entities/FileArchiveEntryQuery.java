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
package com.tcdng.jacklyn.archiving.entities;

import com.tcdng.jacklyn.common.entities.BaseEntityQuery;

/**
 * File archive entry query class.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class FileArchiveEntryQuery extends BaseEntityQuery<FileArchiveEntry> {

    public FileArchiveEntryQuery() {
        super(FileArchiveEntry.class);
    }

    public FileArchiveEntryQuery archivedItemId(Long archivedItemId) {
        return (FileArchiveEntryQuery) equals("archivedItemId", archivedItemId);
    }

    public FileArchiveEntryQuery recordName(String recordName) {
        return (FileArchiveEntryQuery) equals("recordName", recordName);
    }

    public FileArchiveEntryQuery fieldName(String fieldName) {
        return (FileArchiveEntryQuery) equals("fieldName", fieldName);
    }
}
