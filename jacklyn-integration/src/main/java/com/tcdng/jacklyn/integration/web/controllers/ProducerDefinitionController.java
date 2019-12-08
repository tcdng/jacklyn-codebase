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
package com.tcdng.jacklyn.integration.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.integration.entities.ProducerDefinition;
import com.tcdng.jacklyn.integration.entities.ProducerDefinitionQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing producer definitions.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/integration/producerdefinition")
@UplBinding("web/integration/upl/manageproducerdefinition.upl")
public class ProducerDefinitionController extends AbstractIntegrationCrudController<ProducerDefinition> {

    private String searchName;

    private String searchDescription;

    public ProducerDefinitionController() {
        super(ProducerDefinition.class, "$m{integration.producerdefinition.hint}",
                ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.CLIPBOARD
                        | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    @Override
    protected List<ProducerDefinition> find() throws UnifyException {
        ProducerDefinitionQuery query = new ProducerDefinitionQuery();
        if (QueryUtils.isValidStringCriteria(searchName)) {
            query.name(searchName);
        }

        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }

        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.addOrder("description").ignoreEmptyCriteria(true);
        return getIntegrationService().findProducerDefinitions(query);
    }

    @Override
    protected ProducerDefinition find(Long id) throws UnifyException {
        return getIntegrationService().findProducerDefinition(id);
    }

    @Override
    protected ProducerDefinition prepareCreate() throws UnifyException {
        return new ProducerDefinition();
    }

    @Override
    protected Object create(ProducerDefinition producerDefinition) throws UnifyException {
        return getIntegrationService().createProducerDefinition(producerDefinition);
    }

    @Override
    protected int update(ProducerDefinition producerDefinition) throws UnifyException {
        return getIntegrationService().updateProducerDefinition(producerDefinition);
    }

    @Override
    protected int delete(ProducerDefinition producerDefinition) throws UnifyException {
        return getIntegrationService().deleteProducerDefinition(producerDefinition.getId());
    }

}
