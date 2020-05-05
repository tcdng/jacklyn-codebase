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
package com.tcdng.jacklyn.system.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.system.constants.SystemModuleAuditConstants;
import com.tcdng.jacklyn.system.data.SystemControlState;
import com.tcdng.jacklyn.system.entities.SystemParameterQuery;
import com.tcdng.jacklyn.system.web.beans.SystemControlPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.logging.EventType;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * System control controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/systemcontrol")
@UplBinding("web/system/upl/systemcontrol.upl")
@ResultMappings({
        @ResultMapping(name = "refreshcontrolpanel", response = { "!refreshpanelresponse panels:$l{controlPanel}" }) })
public class SystemControlController extends AbstractSystemPageController<SystemControlPageBean> {

    public SystemControlController() {
        super(SystemControlPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String performToggleAction() throws UnifyException {
        SystemControlPageBean pageBean = getPageBean();
        SystemControlState systemControlState = pageBean.getSystemControlStateList().get(getRequestTarget(int.class));
        boolean newState = !systemControlState.isEnabled();
        getSystemService().setSysParameterValue(systemControlState.getName(), newState);
        updateControlStatus();
        if (newState) {
            logUserEvent(SystemModuleAuditConstants.ENABLE_SYS_CONTROL, systemControlState.getDescription());
            hintUser("$m{system.systemcontrol.state.enabled.hint}", systemControlState.getDescription());
        } else {
            logUserEvent(SystemModuleAuditConstants.DISABLE_SYS_CONTROL, systemControlState.getDescription());
            hintUser("$m{system.systemcontrol.state.disabled.hint}", systemControlState.getDescription());
        }
        return "refreshcontrolpanel";
    }

    public String getModeStyle() {
        return EventType.UPDATE.colorMode();
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        updateControlStatus();
    }

    private void updateControlStatus() throws UnifyException {
        SystemControlPageBean pageBean = getPageBean();
        List<SystemControlState> systemControlStateList =
                getSystemService().findSystemControlStates(
                        (SystemParameterQuery) new SystemParameterQuery().ignoreEmptyCriteria(true));
        pageBean.setSystemControlStateList(systemControlStateList);
    }
}
