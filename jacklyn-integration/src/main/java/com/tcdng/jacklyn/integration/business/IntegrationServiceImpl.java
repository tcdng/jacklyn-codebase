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

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.integration.constants.IntegrationModuleNameConstants;
import com.tcdng.jacklyn.integration.entities.ProducerDefinition;
import com.tcdng.jacklyn.integration.entities.ProducerDefinitionQuery;
import com.tcdng.jacklyn.integration.entities.ProducerQueue;
import com.tcdng.jacklyn.integration.entities.ProducerQueueQuery;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.PackableDoc;
import com.tcdng.unify.core.data.TaggedBinaryMessage;
import com.tcdng.unify.core.data.TaggedXmlMessage;
import com.tcdng.unify.core.data.TaggedXmlMessageConsumer;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default implementation of integration business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(IntegrationModuleNameConstants.INTEGRATIONSERVICE)
public class IntegrationServiceImpl extends AbstractJacklynBusinessService implements IntegrationService {

    private FactoryMap<String, String> producerLockKeys;

    public IntegrationServiceImpl() {
        producerLockKeys = new FactoryMap<String, String>() {

            @Override
            protected String create(String producerDefinitionName, Object... params) throws Exception {
                return StringUtils.dotify("intProducerLock", producerDefinitionName);
            }

        };
    }

    @Override
    public Long createProducerDefinition(ProducerDefinition producerDefinition) throws UnifyException {
        return (Long) db().create(producerDefinition);
    }

    @Override
    public ProducerDefinition findProducerDefinition(Long producerDefinitionId) throws UnifyException {
        return db().find(ProducerDefinition.class, producerDefinitionId);
    }

    @Override
    public List<ProducerDefinition> findProducerDefinitions(ProducerDefinitionQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateProducerDefinition(ProducerDefinition producerDefinition) throws UnifyException {
        return db().updateByIdVersion(producerDefinition);
    }

    @Override
    public int deleteProducerDefinition(Long id) throws UnifyException {
        return db().delete(ProducerDefinition.class, id);
    }

    @Override
    public void produceMessage(String producerDefinitionName, String branchCode, String departmentCode,
            PackableDoc packableDoc) throws UnifyException {
        produceMessage(producerDefinitionName, branchCode, departmentCode, packableDoc.pack());
    }

    @Override
    public void produceMessage(String producerDefinitionName, String branchCode, String departmentCode, byte[] msg)
            throws UnifyException {
        beginClusterLock(producerLockKeys.get(producerDefinitionName));
        try {
            Long producerDefinitionId =
                    db().value(Long.class, "id",
                            new ProducerDefinitionQuery().name(producerDefinitionName).status(RecordStatus.ACTIVE));
            db().create(new ProducerQueue(producerDefinitionId, branchCode, departmentCode, msg));
        } finally {
            endClusterLock(producerLockKeys.get(producerDefinitionName));
        }
    }

    @Override
    public TaggedBinaryMessage consumeMessage(String producerDefinitionName) throws UnifyException {
        beginClusterLock(producerLockKeys.get(producerDefinitionName));
        try {
            // Get message on top of queue. We do this by getting lowest ID value
            Long producerMessageId =
                    db().min(Long.class, "id", new ProducerQueueQuery().producerDefinitionName(producerDefinitionName));
            if (producerMessageId != null) {
                ProducerQueue msg = db().list(ProducerQueue.class, producerMessageId);
                db().delete(ProducerQueue.class, producerMessageId);
                return new TaggedBinaryMessage(msg.getMessageTag(), msg.getBranchCode(), msg.getDepartmentCode(),
                        msg.getPreferredConsumer(), msg.getMessage());
            }
        } finally {
            endClusterLock(producerLockKeys.get(producerDefinitionName));
        }
        return null;
    }

    @Override
    public void processTaggedXmlMessage(TaggedXmlMessage taggedXmlMessage) throws UnifyException {
        TaggedXmlMessageConsumer taggedMessageConsumer =
                (TaggedXmlMessageConsumer) getComponent(taggedXmlMessage.getConsumer());
        taggedMessageConsumer.consume(taggedXmlMessage);
    }

    @Override
    public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {

    }
}
