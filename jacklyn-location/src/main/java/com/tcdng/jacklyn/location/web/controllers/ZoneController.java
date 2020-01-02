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
package com.tcdng.jacklyn.location.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.location.entities.Zone;
import com.tcdng.jacklyn.location.entities.ZoneQuery;
import com.tcdng.jacklyn.location.web.beans.ZonePageBean;
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
@Component("/location/zone")
@UplBinding("web/location/upl/managezone.upl")
public class ZoneController extends AbstractLocationFormController<ZonePageBean, Zone> {

    public ZoneController() {
        super(ZonePageBean.class, Zone.class, ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<Zone> find() throws UnifyException {
        ZonePageBean pageBean = getPageBean();
        ZoneQuery query = new ZoneQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchName())) {
            query.name(pageBean.getSearchName());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.addOrder("description").ignoreEmptyCriteria(true);
        return getLocationService().findZones(query);
    }

    @Override
    protected Zone find(Long id) throws UnifyException {
        return getLocationService().findZone(id);
    }

    @Override
    protected Zone prepareCreate() throws UnifyException {
        return new Zone();
    }

    @Override
    protected Object create(Zone zone) throws UnifyException {
        return getLocationService().createZone(zone);
    }

    @Override
    protected int update(Zone zone) throws UnifyException {
        return getLocationService().updateZone(zone);
    }

    @Override
    protected int delete(Zone zone) throws UnifyException {
        return getLocationService().deleteZone(zone.getId());
    }

}
