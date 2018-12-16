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
package com.tcdng.jacklyn.service.entities;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.shared.system.SystemAssetType;
import com.tcdng.jacklyn.system.entities.SystemAsset;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.data.Describable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Client application asset.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "CLIENTAPPASSET", uniqueConstraints = { @UniqueConstraint({ "clientAppId", "systemAssetId" }) })
public class ClientAppAsset extends BaseEntity implements Describable {

    @ForeignKey(ClientApp.class)
    private Long clientAppId;

    @ForeignKey(SystemAsset.class)
    private Long systemAssetId;

    @ListOnly(key = "clientAppId", property = "name")
    private String clientAppName;

    @ListOnly(key = "clientAppId", property = "description")
    private String clientAppDesc;

    @ListOnly(key = "clientAppId", property = "status")
    private RecordStatus clientAppStatus;

    @ListOnly(key = "systemAssetId", property = "name")
    private String assetName;

    @ListOnly(key = "systemAssetId", property = "description")
    private String assetDesc;

    @ListOnly(key = "systemAssetId", property = "type")
    private SystemAssetType assetType;

    @ListOnly(key = "systemAssetId", property = "typeDesc")
    private String assetTypeDesc;

    @ListOnly(key = "systemAssetId", property = "status")
    private RecordStatus assetStatus;

    @Override
    public String getDescription() {
        return StringUtils.concatenate(clientAppDesc, " - ", assetTypeDesc, " - ", assetName);
    }

    public Long getClientAppId() {
        return clientAppId;
    }

    public void setClientAppId(Long clientAppId) {
        this.clientAppId = clientAppId;
    }

    public Long getSystemAssetId() {
        return systemAssetId;
    }

    public void setSystemAssetId(Long systemAssetId) {
        this.systemAssetId = systemAssetId;
    }

    public String getClientAppName() {
        return clientAppName;
    }

    public void setClientAppName(String clientAppName) {
        this.clientAppName = clientAppName;
    }

    public String getClientAppDesc() {
        return clientAppDesc;
    }

    public void setClientAppDesc(String clientAppDesc) {
        this.clientAppDesc = clientAppDesc;
    }

    public RecordStatus getClientAppStatus() {
        return clientAppStatus;
    }

    public void setClientAppStatus(RecordStatus clientAppStatus) {
        this.clientAppStatus = clientAppStatus;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetDesc() {
        return assetDesc;
    }

    public void setAssetDesc(String assetDesc) {
        this.assetDesc = assetDesc;
    }

    public SystemAssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(SystemAssetType assetType) {
        this.assetType = assetType;
    }

    public String getAssetTypeDesc() {
        return assetTypeDesc;
    }

    public void setAssetTypeDesc(String assetTypeDesc) {
        this.assetTypeDesc = assetTypeDesc;
    }

    public RecordStatus getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(RecordStatus assetStatus) {
        this.assetStatus = assetStatus;
    }

}
