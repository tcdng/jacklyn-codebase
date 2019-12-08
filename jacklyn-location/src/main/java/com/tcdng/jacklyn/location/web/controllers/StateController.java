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
package com.tcdng.jacklyn.location.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.location.entities.State;
import com.tcdng.jacklyn.location.entities.StateQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing states.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/location/state")
@UplBinding("web/location/upl/managestate.upl")
public class StateController extends AbstractLocationCrudController<State> {

    private Long searchCountryId;
    
    private String searchCode;

    private String searchDescription;

    public StateController() {
        super(State.class, "$m{location.state.hint}", ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    public Long getSearchCountryId() {
        return searchCountryId;
    }

    public void setSearchCountryId(Long searchCountryId) {
        this.searchCountryId = searchCountryId;
    }

    public String getSearchCode() {
        return searchCode;
    }

    public void setSearchCode(String searchCode) {
        this.searchCode = searchCode;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    @Override
    protected List<State> find() throws UnifyException {
        StateQuery query = new StateQuery();
        if (QueryUtils.isValidLongCriteria(searchCountryId)) {
            query.countryId(searchCountryId);
        }
        
        if (QueryUtils.isValidStringCriteria(searchCode)) {
            query.code(searchCode);
        }

        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }

        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.addOrder("description").ignoreEmptyCriteria(true);
        return getLocationService().findStates(query);
    }

    @Override
    protected State find(Long id) throws UnifyException {
        return getLocationService().findState(id);
    }

    @Override
    protected State prepareCreate() throws UnifyException {
        return new State();
    }

    @Override
    protected Object create(State state) throws UnifyException {
        return getLocationService().createState(state);
    }

    @Override
    protected int update(State state) throws UnifyException {
        return getLocationService().updateState(state);
    }

    @Override
    protected int delete(State state) throws UnifyException {
        return getLocationService().deleteState(state.getId());
    }

}
