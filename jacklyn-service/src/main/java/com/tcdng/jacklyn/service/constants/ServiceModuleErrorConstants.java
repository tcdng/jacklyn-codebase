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
package com.tcdng.jacklyn.service.constants;

/**
 * Service module error constants.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface ServiceModuleErrorConstants {

	/** Application is unknown. Application name = {0} */
	String APPLICATION_UNKNOWN = "SERVICE_0001";

	/** Application as no such asset. Application name = {0}, Asset name = {1} */
	String APPLICATION_NO_SUCH_ASSET = "SERVICE_0002";

	/** Application is inactive. Application name = {0} */
	String APPLICATION_INACTIVE = "SERVICE_0003";

	/** Application asset is inactive. Application name = {0}, Asset name = {1} */
	String APPLICATION_ASSET_INACTIVE = "SERVICE_0004";
}
