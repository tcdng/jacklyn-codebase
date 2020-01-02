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
package com.tcdng.jacklyn.security.entities;

import com.tcdng.jacklyn.common.entities.BaseEntityQuery;
import com.tcdng.jacklyn.shared.system.SystemAssetType;

/**
 * Query object for client application asset records.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ClientAppAssetQuery extends BaseEntityQuery<ClientAppAsset> {

    public ClientAppAssetQuery() {
        super(ClientAppAsset.class);
    }

    public ClientAppAssetQuery clientAppId(Long clientAppId) {
        return (ClientAppAssetQuery) addEquals("clientAppId", clientAppId);
    }

    public ClientAppAssetQuery clientAppName(String clientAppName) {
        return (ClientAppAssetQuery) addEquals("clientAppName", clientAppName);
    }

    public ClientAppAssetQuery assetName(String assetName) {
        return (ClientAppAssetQuery) addEquals("assetName", assetName);
    }

    public ClientAppAssetQuery assetType(SystemAssetType assetType) {
        return (ClientAppAssetQuery) addEquals("assetType", assetType);
    }
}
