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
package com.tcdng.jacklyn.archiving.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.entities.BaseTimestampedEntityQuery;

/**
 * File archive query class.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class FileArchiveQuery extends BaseTimestampedEntityQuery<FileArchive> {

    public FileArchiveQuery() {
        super(FileArchive.class);
    }

    public FileArchiveQuery archivableFieldId(Long archivableFieldId) {
        return (FileArchiveQuery) this.equals("archivableFieldId", archivableFieldId);
    }

    public FileArchiveQuery backedUp(Boolean backedUp) {
        return (FileArchiveQuery) this.equals("backedUp", backedUp);
    }

    public FileArchiveQuery archiveDt(Date archiveDt) {
        return (FileArchiveQuery) this.equals("archiveDt", archiveDt);
    }
}
