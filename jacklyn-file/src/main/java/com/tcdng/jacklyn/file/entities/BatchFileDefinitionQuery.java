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

import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntityQuery;

/**
 * Bulk file definition record query class.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class BatchFileDefinitionQuery extends BaseVersionedStatusEntityQuery<BatchFileDefinition> {

    public BatchFileDefinitionQuery() {
        super(BatchFileDefinition.class);
    }

    public BatchFileDefinitionQuery name(String name) {
        return (BatchFileDefinitionQuery) equals("name", name);
    }

    public BatchFileDefinitionQuery nameLike(String name) {
        return (BatchFileDefinitionQuery) like("name", name);
    }

    public BatchFileDefinitionQuery descriptionLike(String description) {
        return (BatchFileDefinitionQuery) like("description", description);
    }
}
