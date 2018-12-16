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
package com.tcdng.jacklyn.workflow.widgets;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;

/**
 * User workflow item viewer panel.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("ui-userwfitempanel")
@UplBinding("web/workflow/upl/userwfitempanel.upl")
public class UserWfItemPanel extends AbstractWfItemPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        // Flip buttons
        int viewIndex = getValue(int.class, "viewIndex");
        int itemCount = getValue(int.class, "itemCount");
        setDisabled("firstFrmBtn", viewIndex == 0);
        setDisabled("prevFrmBtn", viewIndex == 0);
        setDisabled("nextFrmBtn", viewIndex >= (itemCount - 1));
        setDisabled("lastFrmBtn", viewIndex >= (itemCount - 1));
    }
}
