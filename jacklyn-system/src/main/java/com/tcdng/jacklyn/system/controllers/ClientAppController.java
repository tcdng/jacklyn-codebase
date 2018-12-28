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
package com.tcdng.jacklyn.system.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.shared.system.ClientAppType;
import com.tcdng.jacklyn.system.entities.ClientApp;
import com.tcdng.jacklyn.system.entities.ClientAppLargeData;
import com.tcdng.jacklyn.system.entities.ClientAppQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.web.annotation.Action;

/**
 * Controller for managing service client application records.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/clientapp")
@UplBinding("web/system/upl/manageclientapp.upl")
public class ClientAppController extends AbstractSystemRecordController<ClientApp> {

    private String searchName;

    private String searchDescription;

    private RecordStatus searchStatus;

    private ClientAppLargeData largeData;

    private ClientAppLargeData clipboardLargeData;

    public ClientAppController() {
        super(ClientApp.class, "system.clientapp.hint", ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
        largeData = new ClientAppLargeData();
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public ClientAppLargeData getLargeData() {
        return largeData;
    }

    public void setLargeData(ClientAppLargeData largeData) {
        this.largeData = largeData;
    }

    @Override
    protected List<ClientApp> find() throws UnifyException {
        ClientAppQuery query = new ClientAppQuery();
        if (QueryUtils.isValidStringCriteria(searchName)) {
            query.nameLike(searchName);
        }
        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }
        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.order("description").ignoreEmptyCriteria(true);
        return getSystemService().findClientApps(query);
    }

    @Override
    protected ClientApp find(Long id) throws UnifyException {
        largeData = getSystemService().findClientApp(id);
        return largeData.getData();
    }

    @Override
    protected ClientApp prepareCreate() throws UnifyException {
        largeData = new ClientAppLargeData();
        largeData.getData().setType(ClientAppType.STANDARD);
        return largeData.getData();
    }

    @Override
    protected void onPrepareView(ClientApp clientAppData, boolean onPaste) throws UnifyException {
        if (onPaste) {
            largeData.setSystemAssetIdList(clipboardLargeData.getSystemAssetIdList());
        }
    }

    @Override
    @Action
    public String copyRecord() throws UnifyException {
        clipboardLargeData = ReflectUtils.shallowBeanCopy(largeData);
        return super.copyRecord();
    }

    @Override
    protected void onLoseView(ClientApp clientAppData) throws UnifyException {
        largeData = new ClientAppLargeData();
        clipboardLargeData = null;
    }

    @Override
    protected Object create(ClientApp clientAppData) throws UnifyException {
        return (Long) getSystemService().createClientApp(largeData);
    }

    @Override
    protected int update(ClientApp clientAppData) throws UnifyException {
        return getSystemService().updateClientApp(largeData);
    }

    @Override
    protected int delete(ClientApp applicationData) throws UnifyException {
        return getSystemService().deleteClientApp(applicationData.getId());
    }
}
