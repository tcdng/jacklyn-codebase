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
package com.tcdng.jacklyn.file.entities;

import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntityQuery;
import com.tcdng.jacklyn.shared.file.FileTransferDirection;

/**
 * Query class for file transfer configurations.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class FileTransferConfigQuery extends BaseVersionedStatusEntityQuery<FileTransferConfig> {

    public FileTransferConfigQuery() {
        super(FileTransferConfig.class);
    }

    public FileTransferConfigQuery name(String name) {
        return (FileTransferConfigQuery) equals("name", name);
    }

    public FileTransferConfigQuery nameLike(String name) {
        return (FileTransferConfigQuery) like("name", name);
    }

    public FileTransferConfigQuery descriptionLike(String description) {
        return (FileTransferConfigQuery) like("description", description);
    }

    public FileTransferConfigQuery direction(FileTransferDirection direction) {
        return (FileTransferConfigQuery) equals("direction", direction);
    }
}
