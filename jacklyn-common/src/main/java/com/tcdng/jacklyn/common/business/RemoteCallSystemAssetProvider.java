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

package com.tcdng.jacklyn.common.business;

import com.tcdng.jacklyn.shared.system.SystemAssetType;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Remote call system asset provider.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface RemoteCallSystemAssetProvider extends UnifyComponent {

	/**
	 * Symbolic system asset access check. Should be called before granting a client
	 * application access to a system asset.
	 * 
	 * @param clientAppName
	 *            the client application code
	 * @param systemAssetType
	 *            the system asset type code
	 * @param assetName
	 *            the asset code
	 * @throws UnifyException
	 *             if asset is unknown. If client application is deactivated. If
	 *             asset is deactivated. If an error occurs
	 */
	boolean accessSystemAsset(String clientAppName, SystemAssetType systemAssetType,
			String assetName) throws UnifyException;

}
