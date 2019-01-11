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
package com.tcdng.jacklyn.file.business;

import java.util.Date;
import java.util.Set;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Interface for dynamically obtaining file transfer parameters.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface FileTransferPolicy extends UnifyComponent {

    /**
     * Returns the extended version of the remote path based on the supplied working
     * date.
     * 
     * @param remotePath
     *            the remote path
     * @param workingDtFormat
     *            the working date format
     * @param workingDt
     *            the working date
     * @return the extended remote path
     * @throws UnifyException
     *             if an error occurs
     */
    String getExtendedRemotePath(String remotePath, String workingDtFormat, Date workingDt) throws UnifyException;

    /**
     * Returns the extended version of the local path based on the supplied working
     * date.
     * 
     * @param localPath
     *            the local path
     * @param workingDtFormat
     *            the working date format
     * @param workingDt
     *            the working date
     * @return the extended local path
     * @throws UnifyException
     *             if an error occurs
     */
    String getExtendedLocalPath(String localPath, String workingDtFormat, Date workingDt) throws UnifyException;

    /**
     * Returns file prefixes based on supplied working date.
     * 
     * @param workingDt
     *            the working date
     * @return a list of file prefixes for filtering files to copy. A null means no
     *         filtering by prefix
     * @throws UnifyException
     *             if an error occurs
     */
    Set<String> getFilePrefixes(Date workingDt) throws UnifyException;

    /**
     * Returns file suffixes based on supplied working date.
     * 
     * @param workingDt
     *            the working date
     * @return a list of file suffixes for filtering files to copy. A null means no
     *         filtering by suffix
     * @throws UnifyException
     *             if an error occurs
     */
    Set<String> getFileSuffixes(Date workingDt) throws UnifyException;

    /**
     * Returns source semaphore suffix.
     * 
     * @return the return value is used to check for semaphore files before file
     *         transfer can occur. A null means no semaphore check
     * @throws UnifyException
     *             if an error occurs
     */
    String getSourceSemaphoreSuffix() throws UnifyException;

    /**
     * Returns a target semaphore suffix.
     * 
     * @return a non-null value indicates that semaphore file should be created in
     *         target directory for each transfered file
     * @throws UnifyException
     *             if an error occurs
     */
    String getTargetSemaphoreSuffix() throws UnifyException;
}
