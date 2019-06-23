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
package com.tcdng.jacklyn.integration.business;

import java.util.List;

import com.tcdng.jacklyn.common.business.JacklynBusinessService;
import com.tcdng.jacklyn.integration.entities.ProducerDefinition;
import com.tcdng.jacklyn.integration.entities.ProducerDefinitionQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.PackableDoc;
import com.tcdng.unify.core.data.TaggedBinaryMessage;
import com.tcdng.unify.core.data.TaggedXmlMessage;

/**
 * Integration business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface IntegrationService extends JacklynBusinessService {

    /**
     * Creates a new producer definition.
     * 
     * @param producerDefinition
     *            definition the producer definition
     * @return the created producer definition ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createProducerDefinition(ProducerDefinition producerDefinition) throws UnifyException;

    /**
     * Finds producer definition by ID.
     * 
     * @param producerDefinitionId
     *            the producer definition ID
     * @return the producer definition data
     * @throws UnifyException
     *             if producer definition with ID is not found
     */
    ProducerDefinition findProducerDefinition(Long producerDefinitionId) throws UnifyException;

    /**
     * Finds producer definitions by query.
     * 
     * @param query
     *            the producer definition query
     * @return the list of producer definitions found
     * @throws UnifyException
     *             if an error occurs
     */
    List<ProducerDefinition> findProducerDefinitions(ProducerDefinitionQuery query) throws UnifyException;

    /**
     * Updates a producer definition.
     * 
     * @param producerDefinition
     *            definition the producer definition
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateProducerDefinition(ProducerDefinition producerDefinition) throws UnifyException;

    /**
     * Deletes a producer definition.
     * 
     * @param id
     *            the producer definition ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteProducerDefinition(Long id) throws UnifyException;

    /**
     * Produces an integration message for supplied producer definition and a
     * packable document. Messages produced is consumed by
     * {@link #consumeMessage(String)}.
     * 
     * @param producerDefinitionName
     *            the producer definition name
     * @param branchCode
     *            optional target branch code
     * @param departmentCode
     *            optional target department code
     * @param packableDoc
     *            the packable document
     * @throws UnifyException
     *             if producer definition is unknown or is inactive. If an error
     *             occurs
     */
    void produceMessage(String producerDefinitionName, String branchCode, String departmentCode,
            PackableDoc packableDoc) throws UnifyException;

    /**
     * Produces an integration message for supplied producer definition and binary
     * message. Messages produced is consumed by {@link #consumeMessage(String)}.
     * 
     * @param producerDefinitionName
     *            the producer definition name
     * @param branchCode
     *            optional target branch code
     * @param departmentCode
     *            optional target department code
     * @param msg
     *            the message
     * @throws UnifyException
     *             if producer definition is unknown or is inactive. If an error
     *             occurs
     */
    void produceMessage(String producerDefinitionName, String branchCode, String departmentCode, byte[] msg)
            throws UnifyException;

    /**
     * Consumes next available message in producer's queue.
     * 
     * @param producerDefinitionName
     *            the producer definition name
     * @return the next available tagged binary message otherwise null.
     * @throws UnifyException
     *             if an error occurs
     */
    TaggedBinaryMessage consumeMessage(String producerDefinitionName) throws UnifyException;

    /**
     * Processes a tagged XML message received from an integration point.
     * 
     * @param taggedXmlMessage
     *            the tagged XML message
     * @throws UnifyException
     *             if an error occurs
     */
    void processTaggedXmlMessage(TaggedXmlMessage taggedXmlMessage) throws UnifyException;
}
