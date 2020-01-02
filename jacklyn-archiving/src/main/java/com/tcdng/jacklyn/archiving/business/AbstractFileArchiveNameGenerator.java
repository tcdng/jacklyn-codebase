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
package com.tcdng.jacklyn.archiving.business;

import java.util.Date;

import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.format.DateFormatter;
import com.tcdng.unify.core.system.SequenceNumberService;

/**
 * Abstract base class for a file archive name generator.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractFileArchiveNameGenerator extends AbstractUnifyComponent
        implements FileArchiveNameGenerator {

    @Configurable
    private SequenceNumberService sequenceNumberService;

    @Configurable("!fixeddatetimeformat pattern:$s{yyyyMMdd}")
    private String namingDateFormatter;

    @Configurable(".arch")
    private String fileExtension;

    @Configurable("4")
    private int minSequenceLength;

    private DateFormatter dateFormatter;

    @Override
    protected void onInitialize() throws UnifyException {
        dateFormatter = (DateFormatter) getUplComponent(getApplicationLocale(), namingDateFormatter, false);
    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    /**
     * Returns the file extension part.
     */
    protected String getFileExtensionPart() {
        return fileExtension;
    }

    /**
     * Returns the next sequence number part of archive file name based on supplied
     * archive configuration name, working date and configurable minimum sequence
     * length.
     * 
     * @param fileArchiveConfigName
     *            the file archive configuration name
     * @param workingDt
     *            the working date
     * @throws UnifyException
     *             if an error occurs
     */
    protected String getNextSequenceIdPart(String fileArchiveConfigName, Date workingDt) throws UnifyException {
        long nextSequenceNo = sequenceNumberService.getNextSequenceNumber(fileArchiveConfigName, workingDt);
        return String.format("%0" + minSequenceLength + "d", nextSequenceNo);
    }

    /**
     * Returns the date part of archive file name using supplied working date
     * 
     * @param workingDt
     *            the working date
     * @throws UnifyException
     *             if an error occurs
     */
    protected String getDatePart(Date workingDt) throws UnifyException {
        return dateFormatter.format(workingDt);
    }
}
