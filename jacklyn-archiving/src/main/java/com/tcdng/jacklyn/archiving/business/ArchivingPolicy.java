/*
 * Copyright 2018-2019 The Code Department
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

import java.util.List;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Archiving policy component.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface ArchivingPolicy extends UnifyComponent {

    /**
     * Gets the LOB object to be archived for archive item.
     * 
     * @param archiveItemId
     *            the archive item ID
     * @return the LOB object to be archived
     * @throws UnifyException
     *             if an error occurs
     */
    byte[] getLobToArchive(Long archiveItemId) throws UnifyException;

    /**
     * Deletes LOB columns for archived item is
     * 
     * @param archiveItemIdList
     *            the archive item ID list
     * @return the number of records updated
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteLobFromAchivable(List<Long> archiveItemIdList) throws UnifyException;
}
