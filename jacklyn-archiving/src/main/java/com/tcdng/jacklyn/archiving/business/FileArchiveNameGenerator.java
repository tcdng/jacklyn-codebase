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
package com.tcdng.jacklyn.archiving.business;

import java.util.Date;

import com.tcdng.jacklyn.shared.archiving.FileArchiveType;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Component used for generating file archive names.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface FileArchiveNameGenerator extends UnifyComponent {

    /**
     * Generates a file archive name.
     * 
     * @param fileArchiveType
     *            the file archive type
     * @param fileArchiveConfigName
     *            the file archive configuration name
     * @param workingDt
     *            the working date
     * @return the generated file name
     * @throws UnifyException
     *             if an error occurs
     */
    String generateFileArchiveName(FileArchiveType fileArchiveType, String fileArchiveConfigName, Date workingDt)
            throws UnifyException;
}
