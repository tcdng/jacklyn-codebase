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
package com.tcdng.jacklyn.workflow.data;

import com.tcdng.unify.core.constant.FileAttachmentType;

/**
 * Workflow item attachment data object.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfItemAttachmentInfo {

    private String name;

    private String label;

    private String filename;

    private FileAttachmentType type;

    private byte[] data;

    public WfItemAttachmentInfo(String name, String label, String filename, FileAttachmentType type) {
        this(name, label, filename, type, null);
    }

    public WfItemAttachmentInfo(String name, String label, String filename, FileAttachmentType type, byte[] data) {
        this.name = name;
        this.label = label;
        this.filename = filename;
        this.type = type;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getFilename() {
        return filename;
    }

    public FileAttachmentType getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }
}
