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
package com.tcdng.jacklyn.organization.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.organization.entities.Zone;
import com.tcdng.jacklyn.organization.entities.ZoneQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing zones.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/organization/zone")
@UplBinding("web/organization/upl/managezone.upl")
public class ZoneController extends AbstractOrganizationCrudController<Zone> {

    private String searchName;

    private String searchDescription;

    public ZoneController() {
        super(Zone.class, "$m{organization.zone.hint}", ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
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
    protected List<Zone> find() throws UnifyException {
        ZoneQuery query = new ZoneQuery();
        if (QueryUtils.isValidStringCriteria(searchName)) {
            query.name(searchName);
        }

        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }

        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.order("description").ignoreEmptyCriteria(true);
        return getOrganizationService().findZones(query);
    }

    @Override
    protected Zone find(Long id) throws UnifyException {
        return getOrganizationService().findZone(id);
    }

    @Override
    protected Zone prepareCreate() throws UnifyException {
        return new Zone();
    }

    @Override
    protected Object create(Zone zoneData) throws UnifyException {
        return getOrganizationService().createZone(zoneData);
    }

    @Override
    protected int update(Zone zoneData) throws UnifyException {
        return getOrganizationService().updateZone(zoneData);
    }

    @Override
    protected int delete(Zone zoneData) throws UnifyException {
        return getOrganizationService().deleteZone(zoneData.getId());
    }

}
