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
package com.tcdng.jacklyn.location.business;

import java.util.List;

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.location.constants.LocationModuleNameConstants;
import com.tcdng.jacklyn.location.entities.Country;
import com.tcdng.jacklyn.location.entities.CountryQuery;
import com.tcdng.jacklyn.location.entities.State;
import com.tcdng.jacklyn.location.entities.StateQuery;
import com.tcdng.jacklyn.location.entities.Zone;
import com.tcdng.jacklyn.location.entities.ZoneQuery;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;

/**
 * Default implementation of location business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(LocationModuleNameConstants.LOCATIONSERVICE)
public class LocationServiceImpl extends AbstractJacklynBusinessService implements LocationService {

    @Override
    public Long createZone(Zone zone) throws UnifyException {
        return (Long) db().create(zone);
    }

    @Override
    public Zone findZone(Long zoneId) throws UnifyException {
        return db().find(Zone.class, zoneId);
    }

    @Override
    public List<Zone> findZones(ZoneQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateZone(Zone zone) throws UnifyException {
        return db().updateByIdVersion(zone);
    }

    @Override
    public int deleteZone(Long id) throws UnifyException {
        return db().delete(Zone.class, id);
    }

    @Override
    public Long createCountry(Country country) throws UnifyException {
        return (Long) db().create(country);
    }

    @Override
    public Country findCountry(Long countryId) throws UnifyException {
        return db().find(Country.class, countryId);
    }

    @Override
    public List<Country> findCountries(CountryQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateCountry(Country country) throws UnifyException {
        return db().updateByIdVersion(country);
    }

    @Override
    public int deleteCountry(Long id) throws UnifyException {
        return db().delete(Country.class, id);
    }

    @Override
    public Long createState(State state) throws UnifyException {
        return (Long) db().create(state);
    }

    @Override
    public State findState(Long stateId) throws UnifyException {
        return db().find(State.class, stateId);
    }

    @Override
    public List<State> findStates(StateQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateState(State state) throws UnifyException {
        return db().updateByIdVersion(state);
    }

    @Override
    public int deleteState(Long id) throws UnifyException {
        return db().delete(State.class, id);
    }

    @Override
    public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {

    }
}
