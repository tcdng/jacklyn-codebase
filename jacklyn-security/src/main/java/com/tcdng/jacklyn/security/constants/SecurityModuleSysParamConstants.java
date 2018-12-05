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
package com.tcdng.jacklyn.security.constants;

/**
 * Security module system parameter constants.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public interface SecurityModuleSysParamConstants {

	String ENABLE_USER_THEMES = "SEC-0001";

	String ENABLE_ROLE_THEMES = "SEC-0002";

	String ENABLE_PASSWORD_HISTORY = "SEC-0003";

	String PASSWORD_HISTORY_LENGTH = "SEC-0004";

	String ENABLE_PASSWORD_EXPIRY = "SEC-0005";

	String PASSWORD_EXPIRY_DAYS = "SEC-0006";

	String ENABLE_ACCOUNT_LOCKING = "SEC-0007";

	String MAXIMUM_LOGIN_TRIES = "SEC-0008";

	String USER_PASSWORD_GENERATOR = "SEC-0009";

	String USER_PASSWORD_LENGTH = "SEC-000A";

	String USER_PASSWORD_SEND_EMAIL = "SEC-000B";

	String USER_DEFAULT_APPLICATION = "SEC-000C";

	String ENABLE_TWOFACTOR_AUTHENTICATION = "SEC-000D";

	String ENABLE_SYSTEMWIDE_MULTILOGIN_RULE = "SEC-000E";

	String SYSTEMWIDE_MULTILOGIN = "SEC-000F";

	String APPLICATION_SECURITY_KEY = "SEC-0010";

	String NEW_PASSWORD_MESSAGE_TEMPLATE = "SEC-0011";

	String RESET_PASSWORD_MESSAGE_TEMPLATE = "SEC-0012";

	String SECURITY_EMAIL_CHANNEL = "SEC-0013";

	String ADMINISTRATOR_EMAIL = "SEC-0014";

	String ADMINISTRATOR_NAME = "SEC-0015";
}
