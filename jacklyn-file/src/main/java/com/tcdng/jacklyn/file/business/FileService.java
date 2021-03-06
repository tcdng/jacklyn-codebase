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
package com.tcdng.jacklyn.file.business;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tcdng.jacklyn.common.business.JacklynBusinessService;
import com.tcdng.jacklyn.file.data.BatchFileReadDefinitionLargeData;
import com.tcdng.jacklyn.file.data.BatchFileReadInputParameters;
import com.tcdng.jacklyn.file.entities.BatchFileDefinition;
import com.tcdng.jacklyn.file.entities.BatchFileDefinitionQuery;
import com.tcdng.jacklyn.file.entities.BatchFileFieldDefinition;
import com.tcdng.jacklyn.file.entities.BatchFileReadDefinition;
import com.tcdng.jacklyn.file.entities.BatchFileReadDefinitionQuery;
import com.tcdng.jacklyn.file.entities.FileInbox;
import com.tcdng.jacklyn.file.entities.FileInboxQuery;
import com.tcdng.jacklyn.file.entities.FileOutbox;
import com.tcdng.jacklyn.file.entities.FileOutboxQuery;
import com.tcdng.jacklyn.file.entities.FileTransferConfig;
import com.tcdng.jacklyn.file.entities.FileTransferConfigQuery;
import com.tcdng.jacklyn.shared.file.FileInboxReadStatus;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.batch.BatchFileReadConfig;

/**
 * File business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface FileService extends JacklynBusinessService {

    /**
     * Finds file transfer configurations by query.
     * 
     * @param query
     *            the search query
     * @return list of file transfer configurations that match query
     * @throws UnifyException
     *             if an error occurs
     */
    List<FileTransferConfig> findFileTransferConfigs(FileTransferConfigQuery query) throws UnifyException;

    /**
     * Finds file transfer configuration by Id.
     * 
     * @param id
     *            the file transfer configuration Id
     * @return the file transfer configuration record
     * @throws UnifyException
     *             if file transfer configuration with id not found
     */
    FileTransferConfig findFileTransferConfig(Long id) throws UnifyException;

    /**
     * Creates a file transfer configuration.
     * 
     * @param fileTransferConfig
     *            the file transfer configuration record
     * @return the Id of the created record
     * @throws UnifyException
     *             If file transfer configuration creation failed
     */
    Long createFileTransferConfig(FileTransferConfig fileTransferConfig) throws UnifyException;

    /**
     * Updates a file transfer configuration record by Id and version.
     * 
     * @param fileTransferConfig
     *            the file transfer configuration record
     * @return the update count
     * @throws UnifyException
     *             if file transfer configuration record with version and ID does
     *             not exist
     */
    int updateFileTransferConfig(FileTransferConfig fileTransferConfig) throws UnifyException;

    /**
     * Deletes file transfer configuration record by id.
     * 
     * @param id
     *            the file transfer configuration id
     * @return the delete count
     * @throws UnifyException
     *             if file transfer configuration with Id does not exists. If an
     *             error occurs
     */
    int deleteFileTransferConfig(Long id) throws UnifyException;

    /**
     * Gets a file transfer information for specified file transfer configuration
     * and working date.
     * 
     * @param fileTransferConfigId
     *            the file transfer configuration ID
     * @param workingDt
     *            the working date
     * @return the file transfer information
     * @throws UnifyException
     *             if an error occurs
     */
    /**
     * Reads a block from a remote file.
     * 
     * @param fileTransferConfigId
     *            the file transfer configuration ID
     * @param workingDt
     *            the working date
     * @param filename
     *            the remote file name
     * @param fileIndex
     *            the index to read from
     * @param length
     *            the block length to read in bytes
     * @return the block read otherwise a null is returned
     * @throws UnifyException
     *             if an error occurs
     */
    byte[] readRemoteBlock(Long fileTransferConfigId, Date workingDt, String filename, long fileIndex, int length)
            throws UnifyException;

    /**
     * Finds file inbox items by query.
     * 
     * @param query
     *            the search query
     * @return list of file inbox items that match query
     * @throws UnifyException
     *             if an error occurs
     */
    List<FileInbox> findFileInboxItems(FileInboxQuery query) throws UnifyException;

    /**
     * Finds a file inbox item by ID.
     * 
     * @param id
     *            the inbox item ID
     * @return the file inbox item
     * @throws UnifyException
     *             if item with ID is not found
     */
    FileInbox findFileInboxItem(Long id) throws UnifyException;

    /**
     * Updates file inbox item read statuses by query.
     * 
     * @param query
     *            the query
     * @param readStatus
     *            the read status to set
     * @return the number of items updated
     * @throws UnifyException
     *             if an error occurs
     */
    int updateFileInboxItemsReadStatus(FileInboxQuery query, FileInboxReadStatus readStatus) throws UnifyException;

    /**
     * Finds file outbox items by query.
     * 
     * @param query
     *            the search query
     * @return list of file outbox items that match query
     * @throws UnifyException
     *             ff an error occurs
     */
    List<FileOutbox> findFileOutboxItems(FileOutboxQuery query) throws UnifyException;

    /**
     * Finds a file outbox item by ID.
     * 
     * @param id
     *            the outbox item ID
     * @return the file outbox item
     * @throws UnifyException
     *             if item with ID is not found
     */
    FileOutbox findFileOutboxItem(Long id) throws UnifyException;

    /**
     * Finds batch file definitions by query.
     * 
     * @param query
     *            the search query
     * @return list of batch file definitions that match query
     * @throws UnifyException
     *             if an error occurs
     */
    List<BatchFileDefinition> findBatchFileDefinitions(BatchFileDefinitionQuery query) throws UnifyException;

    /**
     * Finds batch file definition by Id.
     * 
     * @param id
     *            the batch file definition Id
     * @return the batch file definition record
     * @throws UnifyException
     *             if batch file definition with id not found
     */
    BatchFileDefinition findBatchFileDefinition(Long id) throws UnifyException;

    /**
     * Creates a batch file definition.
     * 
     * @param record
     *            the batch file definition record
     * @return the Id of the created record
     * @throws UnifyException
     *             If batch file definition creation failed
     */
    Long createBatchFileDefinition(BatchFileDefinition record) throws UnifyException;

    /**
     * Updates a batch file definition record by Id and version.
     * 
     * @param record
     *            the batch file definition record
     * @return the update count
     * @throws UnifyException
     *             if batch file definition record with version and ID does not
     *             exist
     */
    int updateBatchFileDefinition(BatchFileDefinition record) throws UnifyException;

    /**
     * Deletes batch file definition record by id.
     * 
     * @param id
     *            the batch file definition id
     * @return the delete count
     * @throws UnifyException
     *             if batch file definition with Id does not exists. If an error
     *             occurs
     */
    int deleteBatchFileDefinition(Long id) throws UnifyException;

    /**
     * Finds batch file read definitions using query.
     * 
     * @param query
     *            the search query
     * @return list of batch file read definitions that match query
     * @throws UnifyException
     *             if an error occurs
     */
    List<BatchFileReadDefinition> findBatchFileReadDefinitions(BatchFileReadDefinitionQuery query)
            throws UnifyException;

    /**
     * Finds batch file read definition by Id.
     * 
     * @param id
     *            the batch file read definition Id
     * @return the batch file read configuration record
     * @throws UnifyException
     *             if batch file read configuration with id not found
     */
    BatchFileReadDefinition findBatchFileReadDefinition(Long id) throws UnifyException;

    /**
     * Finds batch file read definition document by ID
     * 
     * @param id
     *            the batch file read definition Id
     * @return the batch file read definition document
     * @throws UnifyException
     *             if batch file read definition with id not found
     */
    BatchFileReadDefinitionLargeData findBatchFileReadDefinitionDocument(Long id) throws UnifyException;

    /**
     * Creates a batch file read definition.
     * 
     * @param batchFileReadDefinition
     *            the batch file read definition record
     * @return the Id of the created record
     * @throws UnifyException
     *             If batch file read definition creation failed
     */
    Long createBatchFileReadDefinition(BatchFileReadDefinition batchFileReadDefinition) throws UnifyException;

    /**
     * Creates a batch file read definition.
     * 
     * @param document
     *            the batch file read definition document
     * @return the Id of the created record
     * @throws UnifyException
     *             If batch file read definition creation failed
     */
    Long createBatchFileReadDefinition(BatchFileReadDefinitionLargeData document) throws UnifyException;

    /**
     * Updates a batch file read definition record by Id and version.
     * 
     * @param batchFileReadDefinition
     *            the batch file read definition record
     * @return the update count
     * @throws UnifyException
     *             if batch file read definition record with version and ID does not
     *             exist
     */
    int updateBatchFileReadDefinition(BatchFileReadDefinition batchFileReadDefinition) throws UnifyException;

    /**
     * Updates a batch file read definition record by Id and version.
     * 
     * @param document
     *            the batch file read definition document
     * @return the update count
     * @throws UnifyException
     *             if batch file read definition record with version and ID does not
     *             exist
     */
    int updateBatchFileReadDefinition(BatchFileReadDefinitionLargeData document) throws UnifyException;

    /**
     * Deletes batch file read definition record by id.
     * 
     * @param id
     *            the batch file read definition id
     * @return the delete count
     * @throws UnifyException
     *             if batch file read definition with Id does not exists. If an
     *             error occurs
     */
    int deleteBatchFileReadDefinition(Long id) throws UnifyException;

    /**
     * Reloads parameter values for a batch file read configuration
     * 
     * @param document
     *            the scheduled task document
     * @return the task document
     * @throws UnifyException
     *             if an error occurs
     */
    BatchFileReadDefinitionLargeData loadBatchFileReadConfigDocumentValues(BatchFileReadDefinitionLargeData document)
            throws UnifyException;

    /**
     * Gets new batch file read input parameters using a batch file read definition.
     * 
     * @param batchFileReadDefinitionId
     *            the batch file read definition ID
     * @return a batch upload input parameters object
     * @throws UnifyException
     *             if an error occurs
     */
    BatchFileReadInputParameters getBatchFileReadInputParameters(Long batchFileReadDefinitionId) throws UnifyException;

    /**
     * Gets new batch file read configuration using a batch file read definition.
     * 
     * @param batchFileReadDefinitionName
     *            the batch file read definition name
     * @param parameters
     *            optional parameters
     * @return the batch file read configuration
     * @throws UnifyException
     *             if an error occurs
     */
    BatchFileReadConfig getBatchFileReadConfig(String batchFileReadDefinitionName, Map<String, Object> parameters)
            throws UnifyException;

    /**
     * Merges bean fields to file field definition mappings.
     * 
     * @param beanType
     *            the bean type
     * @param baseFieldDefList
     *            the base field definition list
     * @return new list of batch file field definitions
     * @throws UnifyException
     *             if an error occurs
     */
    List<BatchFileFieldDefinition> mergeBatchFileFieldMapping(String beanType,
            List<BatchFileFieldDefinition> baseFieldDefList) throws UnifyException;
}
