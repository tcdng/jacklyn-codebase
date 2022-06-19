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
package com.tcdng.jacklyn.archiving.web.lists;

import com.tcdng.jacklyn.archiving.business.ArchivingService;
import com.tcdng.jacklyn.common.web.lists.BaseListCommand;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.list.ListParam;

/**
 * Abstract base class for archiving service list commands.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractArchivingListCommand<T extends ListParam> extends BaseListCommand<T> {

    @Configurable
    private ArchivingService archivingService;

    public AbstractArchivingListCommand(Class<T> paramType) {
        super(paramType);
    }

    protected ArchivingService getArchivingService() {
        return archivingService;
    }

}
