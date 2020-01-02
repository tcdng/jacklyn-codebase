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
package com.tcdng.jacklyn.file.entities;

import com.tcdng.jacklyn.common.annotation.Format;
import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.file.constants.FileModuleNameConstants;
import com.tcdng.jacklyn.shared.file.FileTransferDirection;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity for storing file transfer configuration information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = FileModuleNameConstants.FILE_MODULE, title = "File DataTransfer Configuration", reportable = true,
        auditable = true)
@Table(name = "JKFILETRANSFERCFG", uniqueConstraints = { @UniqueConstraint({ "name" }) })
public class FileTransferConfig extends BaseVersionedStatusEntity {

    @ForeignKey(name = "DIRECTION_TY")
    private FileTransferDirection direction;

    @Column(name = "FILETRANSFERCFG_NM")
    private String name;

    @Column(name = "FILETRANSFERCFG_DESC", length = 64)
    private String description;

    @Column(length = 48)
    private String fileTransferPolicy;

    @Column(length = 48)
    private String fileTransferServer;

    @Column(length = 64)
    private String remoteHost;

    @Column(nullable = true)
    private Integer remotePort;

    @Column(transformer = "lowercase-transformer", nullable = true)
    private String authenticationId;

    @Format(mask = true)
    @Column(length = 256, transformer = "twoway-stringcryptograph", nullable = true)
    private String authenticationPassword;

    @Column(length = 96)
    private String remotePath;

    @Column(length = 32, nullable = true)
    private String remoteDateFormat;

    @Column(length = 96)
    private String localPath;

    @Column(length = 32, nullable = true)
    private String localDateFormat;

    @Column
    private Integer maxTransferAttempts;

    @Column
    private Boolean deleteSourceOnTransfer;

    @Format(description = "$m{file.filetransferconfig.direction}")
    @ListOnly(key = "direction", property = "description")
    private String directionDesc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileTransferPolicy() {
        return fileTransferPolicy;
    }

    public void setFileTransferPolicy(String fileTransferPolicy) {
        this.fileTransferPolicy = fileTransferPolicy;
    }

    public String getFileTransferServer() {
        return fileTransferServer;
    }

    public void setFileTransferServer(String fileTransferServer) {
        this.fileTransferServer = fileTransferServer;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

    public String getAuthenticationId() {
        return authenticationId;
    }

    public void setAuthenticationId(String authenticationId) {
        this.authenticationId = authenticationId;
    }

    public String getAuthenticationPassword() {
        return authenticationPassword;
    }

    public void setAuthenticationPassword(String authenticationPassword) {
        this.authenticationPassword = authenticationPassword;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getRemoteDateFormat() {
        return remoteDateFormat;
    }

    public void setRemoteDateFormat(String remoteDateFormat) {
        this.remoteDateFormat = remoteDateFormat;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getLocalDateFormat() {
        return localDateFormat;
    }

    public void setLocalDateFormat(String localDateFormat) {
        this.localDateFormat = localDateFormat;
    }

    public Integer getMaxTransferAttempts() {
        return maxTransferAttempts;
    }

    public void setMaxTransferAttempts(Integer maxTransferAttempts) {
        this.maxTransferAttempts = maxTransferAttempts;
    }

    public FileTransferDirection getDirection() {
        return direction;
    }

    public void setDirection(FileTransferDirection direction) {
        this.direction = direction;
    }

    public String getDirectionDesc() {
        return directionDesc;
    }

    public void setDirectionDesc(String directionDesc) {
        this.directionDesc = directionDesc;
    }

    public Boolean getDeleteSourceOnTransfer() {
        return deleteSourceOnTransfer;
    }

    public void setDeleteSourceOnTransfer(Boolean deleteSourceOnTransfer) {
        this.deleteSourceOnTransfer = deleteSourceOnTransfer;
    }
}
