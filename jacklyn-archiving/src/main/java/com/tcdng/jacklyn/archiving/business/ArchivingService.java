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
package com.tcdng.jacklyn.archiving.business;

import java.util.List;

import com.tcdng.jacklyn.archiving.entities.ArchivableDefinition;
import com.tcdng.jacklyn.archiving.entities.ArchivableDefinitionQuery;
import com.tcdng.jacklyn.archiving.entities.ArchivingField;
import com.tcdng.jacklyn.archiving.entities.ArchivingFieldQuery;
import com.tcdng.jacklyn.archiving.entities.FileArchiveConfig;
import com.tcdng.jacklyn.archiving.entities.FileArchiveConfigQuery;
import com.tcdng.jacklyn.common.business.JacklynBusinessService;
import com.tcdng.unify.core.UnifyException;

/**
 * Archiving business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface ArchivingService extends JacklynBusinessService {

    /**
     * Finds archivable definitions by query.
     * 
     * @param query
     *            the search query
     * @return list of archivable definition that match query
     * @throws UnifyException
     *             if an error occurs
     */
    List<ArchivableDefinition> findArchivableDefinitions(ArchivableDefinitionQuery query) throws UnifyException;

    /**
     * Finds archivable definition by ID
     * 
     * @param archivableDefId
     *            the
     * @return archivable definition data
     * @throws UnifyException
     *             if record with id is not found
     */
    ArchivableDefinition findArchivableDefinition(Long archivableDefId) throws UnifyException;

    /**
     * Finds archiving fields by query.
     * 
     * @param query
     *            the search query
     * @return list of archive fields that match query
     * @throws UnifyException
     *             if an error occurs
     */
    List<ArchivingField> findArchivingFields(ArchivingFieldQuery query) throws UnifyException;

    /**
     * Finds archiving field by ID
     * 
     * @param archivingFieldId
     *            the
     * @return archiving field data
     * @throws UnifyException
     *             if record with id is not found
     */
    ArchivingField findArchivingField(Long archivingFieldId) throws UnifyException;

    /**
     * Finds archiving field by query
     * 
     * @param query
     *            the
     * @return archiving field data if found otherwise null
     * @throws UnifyException
     *             if multiple records found. If an error occurs
     */
    ArchivingField findArchivingField(ArchivingFieldQuery query) throws UnifyException;

    /**
     * Creates a new file archive config.
     * 
     * @param fileArchiveConfig
     *            the file archive config
     * @return the created file archive config ID otherwise a null value if no items
     *         found to archive
     * @throws UnifyException
     *             if an error occurs
     */
    Long createFileArchiveConfig(FileArchiveConfig fileArchiveConfig) throws UnifyException;

    /**
     * Finds file archive config by ID.
     * 
     * @param fileArchiveConfigId
     *            the file archive config ID
     * @return the file archive config data
     * @throws UnifyException
     *             if file archive config with ID is not found
     */
    FileArchiveConfig findFileArchiveConfig(Long fileArchiveConfigId) throws UnifyException;

    /**
     * Finds file archive configs by query.
     * 
     * @param query
     *            the file archive config query
     * @return the list of file archive configs found
     * @throws UnifyException
     *             if an error occurs
     */
    List<FileArchiveConfig> findFileArchiveConfigs(FileArchiveConfigQuery query) throws UnifyException;

    /**
     * Updates a file archive config.
     * 
     * @param fileArchiveConfig
     *            the file archive config
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateFileArchiveConfig(FileArchiveConfig fileArchiveConfig) throws UnifyException;

    /**
     * Deletes a file archive config.
     * 
     * @param id
     *            the file archive config ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteFileArchiveConfig(Long id) throws UnifyException;

    /**
     * Retrieves an archived BLOB.
     * 
     * @param recordName
     *            the record type canonical name
     * @param fieldName
     *            the record field name
     * @param archivedItemId
     *            the original archived item ID
     * @return the retrieved BLOB if archive entry exists otherwise null
     * @throws UnifyException
     *             if an error occurs
     */
    byte[] retriveArchivedBlob(String recordName, String fieldName, Long archivedItemId) throws UnifyException;

    /**
     * Retrieves an archived CLOB.
     * 
     * @param recordName
     *            the record type canonical name
     * @param fieldName
     *            the record field name
     * @param archivedItemId
     *            the original archived item ID
     * @return the retrieved CLOB if archive entry exists otherwise null
     * @throws UnifyException
     *             if an error occurs
     */
    String retriveArchivedClob(String recordName, String fieldName, Long archivedItemId) throws UnifyException;
}
