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
package com.tcdng.jacklyn.workflow.web.widgets;

import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.AbstractValueListMultiControl;
import com.tcdng.unify.web.ui.Control;

/**
 * Workflow step action buttons.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("ui-wfactionbuttons")
@UplAttributes({ @UplAttribute(name = "buttonClass", type = String.class),
        @UplAttribute(name = "verticalLayout", type = boolean.class) })
public class WfActionButtons extends AbstractValueListMultiControl<ValueStore, Object> {

    private Control actionCtrl;

    @Override
    public void addPageAliases() throws UnifyException {
        addPageAlias(actionCtrl);
    }

    @Override
    public boolean isLayoutCaption() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<Object> getItemList() throws UnifyException {
        return (List<Object>) getValue();
    }

    @Override
    protected ValueStore newValue(Object item, int index) throws UnifyException {
        return createValueStore(item, index);
    }

    @Override
    protected void onCreateValueList(List<ValueStore> valueList) throws UnifyException {

    }

    public boolean isVerticalLayout() throws UnifyException {
        return getUplAttribute(boolean.class, "verticalLayout");
    }

    public Control getActionCtrl() {
        return actionCtrl;
    }

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        String buttonClass = getUplAttribute(String.class, "buttonClass");
        if (StringUtils.isBlank(buttonClass)) {
            actionCtrl = (Control) addInternalChildWidget("!ui-button captionBinding:label binding:name");
        } else {
            actionCtrl = (Control) addInternalChildWidget(
                    "!ui-button styleClass:$e{" + buttonClass + "} captionBinding:label binding:name");
        }

        actionCtrl.setGroupId(getId());
    }
}
