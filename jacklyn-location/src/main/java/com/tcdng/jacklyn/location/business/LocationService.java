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

import com.tcdng.jacklyn.common.business.JacklynBusinessService;
import com.tcdng.jacklyn.location.entities.Country;
import com.tcdng.jacklyn.location.entities.CountryQuery;
import com.tcdng.jacklyn.location.entities.State;
import com.tcdng.jacklyn.location.entities.StateQuery;
import com.tcdng.jacklyn.location.entities.Zone;
import com.tcdng.jacklyn.location.entities.ZoneQuery;
import com.tcdng.unify.core.UnifyException;

/**
 * Location business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface LocationService extends JacklynBusinessService {

    /**
     * Creates a new zone.
     * 
     * @param zone
     *            the zone data
     * @return the created zone ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createZone(Zone zone) throws UnifyException;

    /**
     * Finds zone by ID.
     * 
     * @param zoneId
     *            the zone ID
     * @return the zone data
     * @throws UnifyException
     *             if zone with ID is not found
     */
    Zone findZone(Long zoneId) throws UnifyException;

    /**
     * Finds zone by query.
     * 
     * @param query
     *            the zone query
     * @return the zone data if found otherwise null
     * @throws UnifyException
     *             if multiple records matched by query. if an error occurs
     */
    Zone findZone(ZoneQuery query) throws UnifyException;

    /**
     * Finds zones by query.
     * 
     * @param query
     *            the zone query
     * @return the list of zones found
     * @throws UnifyException
     *             if an error occurs
     */
    List<Zone> findZones(ZoneQuery query) throws UnifyException;

    /**
     * Updates a zone.
     * 
     * @param zone
     *            the zone data
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateZone(Zone zone) throws UnifyException;

    /**
     * Deletes a zone.
     * 
     * @param id
     *            the zone ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteZone(Long id) throws UnifyException;

    /**
     * Creates a new country.
     * 
     * @param country
     *            the country data
     * @return the created country ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createCountry(Country country) throws UnifyException;

    /**
     * Finds country by ID.
     * 
     * @param countryId
     *            the country ID
     * @return the country data
     * @throws UnifyException
     *             if country with ID is not found
     */
    Country findCountry(Long countryId) throws UnifyException;

    /**
     * Finds country by query.
     * 
     * @param query
     *            the country query
     * @return the country record if found otherwise null
     * @throws UnifyException
     *             if multiple records matched by query. if an error occurs
     */
    Country findCountry(CountryQuery query) throws UnifyException;

    /**
     * Finds countries by query.
     * 
     * @param query
     *            the country query
     * @return the list of countries found
     * @throws UnifyException
     *             if an error occurs
     */
    List<Country> findCountries(CountryQuery query) throws UnifyException;

    /**
     * Updates a country.
     * 
     * @param country
     *            the country data
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateCountry(Country country) throws UnifyException;

    /**
     * Deletes a country.
     * 
     * @param id
     *            the country ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteCountry(Long id) throws UnifyException;

    /**
     * Creates a new state.
     * 
     * @param state
     *            the state data
     * @return the created state ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createState(State state) throws UnifyException;

    /**
     * Finds state by ID.
     * 
     * @param stateId
     *            the state ID
     * @return the state data
     * @throws UnifyException
     *             if state with ID is not found
     */
    State findState(Long stateId) throws UnifyException;

    /**
     * Finds state by query.
     * 
     * @param query
     *            the state query
     * @return the state record if found otherwise null
     * @throws UnifyException
     *             if multiple records matched by query. if an error occurs
     */
    State findState(StateQuery query) throws UnifyException;

    /**
     * Finds states by query.
     * 
     * @param query
     *            the state query
     * @return the list of states found
     * @throws UnifyException
     *             if an error occurs
     */
    List<State> findStates(StateQuery query) throws UnifyException;

    /**
     * Updates a state.
     * 
     * @param state
     *            the state data
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateState(State state) throws UnifyException;

    /**
     * Deletes a state.
     * 
     * @param id
     *            the state ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteState(Long id) throws UnifyException;
}
