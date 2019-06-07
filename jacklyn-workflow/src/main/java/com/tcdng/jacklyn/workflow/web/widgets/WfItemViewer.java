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
package com.tcdng.jacklyn.workflow.web.widgets;

import com.tcdng.jacklyn.workflow.data.ViewableWfItem;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.panel.StandalonePanel;

/**
 * Workflow item viewer.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface WfItemViewer extends StandalonePanel {

    /**
     * Sets viewer document mode based on privileges in viewable item's supplied
     * step.
     * 
     * @param viewableWfItem
     *            the viewable workflow item
     * @throws UnifyException
     *             if an error occurs
     */
    void setDocumentMode(ViewableWfItem viewableWfItem) throws UnifyException;
}
