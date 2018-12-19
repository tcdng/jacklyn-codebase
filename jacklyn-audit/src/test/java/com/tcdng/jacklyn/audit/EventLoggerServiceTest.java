/*
 * Copyright 2018 The Code Department
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
package com.tcdng.jacklyn.audit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.tcdng.jacklyn.audit.business.EventLoggerService;
import com.tcdng.jacklyn.common.AbstractJacklynTest;
import com.tcdng.jacklyn.common.TestCustomer;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.logging.EventType;

/**
 * Event logger service test.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class EventLoggerServiceTest extends AbstractJacklynTest {

    @Test
    public void testLogEventWithDetails() throws Exception {
        EventLoggerService eventLoggerService = (EventLoggerService) this
                .getComponent(ApplicationComponents.APPLICATION_EVENTSLOGGER);
        assertTrue(eventLoggerService.logUserEvent("customer-search", "On a Sunday", "By age"));
    }

    @Test
    public void testLogEventWithRecordType() throws Exception {
        EventLoggerService eventLoggerService = (EventLoggerService) this
                .getComponent(ApplicationComponents.APPLICATION_EVENTSLOGGER);
        assertFalse(eventLoggerService.logUserEvent(EventType.SEARCH, TestCustomer.class));
    }

    @Test
    public void testLogEventWithRecord() throws Exception {
        EventLoggerService eventLoggerService = (EventLoggerService) this
                .getComponent(ApplicationComponents.APPLICATION_EVENTSLOGGER);
        assertTrue(eventLoggerService.logUserEvent(EventType.CREATE, new TestCustomer(), true));
        assertFalse(eventLoggerService.logUserEvent(EventType.VIEW, new TestCustomer(), false));
    }

    @Test(expected = UnifyException.class)
    public void testLogUnknownEvent() throws Exception {
        EventLoggerService eventLoggerService = (EventLoggerService) this
                .getComponent(ApplicationComponents.APPLICATION_EVENTSLOGGER);
        eventLoggerService.logUserEvent("create-user");
    }

    @Override
    protected void onSetup() throws Exception {

    }

    @Override
    protected void onTearDown() throws Exception {

    }
}
