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
package com.tcdng.jacklyn.security.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.security.data.ClientAppLargeData;
import com.tcdng.jacklyn.security.entities.ClientApp;
import com.tcdng.jacklyn.security.entities.ClientAppQuery;
import com.tcdng.jacklyn.security.web.beans.ClientAppPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.web.annotation.Action;

/**
 * Controller for managing client application records.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/security/clientapp")
@UplBinding("web/security/upl/manageclientapp.upl")
public class ClientAppController extends AbstractSecurityFormController<ClientAppPageBean, ClientApp> {

    public ClientAppController() {
        super(ClientAppPageBean.class, ClientApp.class, ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<ClientApp> find() throws UnifyException {
        ClientAppPageBean pageBean = getPageBean();
        ClientAppQuery query = new ClientAppQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchName())) {
            query.nameLike(pageBean.getSearchName());
        }
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }
        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.addOrder("description").ignoreEmptyCriteria(true);
        return getSecurityService().findClientApps(query);
    }

    @Override
    protected ClientApp find(Long id) throws UnifyException {
        ClientAppLargeData largeData = getSecurityService().findClientApp(id);
        ClientAppPageBean pageBean = getPageBean();
        pageBean.setLargeData(largeData);
        return largeData.getData();
    }

    @Override
    protected void onPrepareCreate(ClientApp clientApp) throws UnifyException {
        ClientAppPageBean pageBean = getPageBean();
        pageBean.setLargeData(new ClientAppLargeData(clientApp));
    }

    @Override
    protected void onPrepareView(ClientApp clientAppData, boolean onPaste) throws UnifyException {
        ClientAppPageBean pageBean = getPageBean();
        ClientAppLargeData largeData = pageBean.getLargeData();
        if (onPaste) {
            largeData.setSystemAssetIdList(pageBean.getClipboardLargeData().getSystemAssetIdList());
        }
    }

    @Override
    @Action
    public String copyRecord() throws UnifyException {
        ClientAppPageBean pageBean = getPageBean();
        ClientAppLargeData clipboardLargeData = ReflectUtils.shallowBeanCopy(pageBean.getLargeData());
        pageBean.setClipboardLargeData(clipboardLargeData);
        
        return super.copyRecord();
    }

    @Override
    protected void onLoseView(ClientApp clientAppData) throws UnifyException {
        ClientAppPageBean pageBean = getPageBean();
        pageBean.setLargeData(new ClientAppLargeData());
        pageBean.setClipboardLargeData(null);
        ;
    }

    @Override
    protected Object create(ClientApp clientAppData) throws UnifyException {
        ClientAppPageBean pageBean = getPageBean();
        return (Long) getSecurityService().createClientApp(pageBean.getLargeData());
    }

    @Override
    protected int update(ClientApp clientAppData) throws UnifyException {
        ClientAppPageBean pageBean = getPageBean();
        return getSecurityService().updateClientApp(pageBean.getLargeData());
    }

    @Override
    protected int delete(ClientApp applicationData) throws UnifyException {
        return getSecurityService().deleteClientApp(applicationData.getId());
    }
}
