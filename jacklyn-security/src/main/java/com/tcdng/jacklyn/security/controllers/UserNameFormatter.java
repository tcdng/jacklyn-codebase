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
package com.tcdng.jacklyn.security.controllers;

import com.tcdng.jacklyn.security.business.SecurityService;
import com.tcdng.jacklyn.security.entities.User;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.format.AbstractFormatter;

/**
 * User name from ID formatter.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component(name = "usernameformat", description = "$m{security.format.username}")
public class UserNameFormatter extends AbstractFormatter<String> {

    @Configurable
    private SecurityService securityModule;

    public UserNameFormatter() {
        super(String.class);
    }

    @Override
    public String format(String value) throws UnifyException {
        User user = getSecurityModule().findUser(value);
        return user.getFullName();
    }

    @Override
    public String parse(String string) throws UnifyException {
        return null;
    }

    @Override
    public String getPattern() throws UnifyException {
        return null;
    }

    protected SecurityService getSecurityModule() throws UnifyException {
        return securityModule;
    }
}
