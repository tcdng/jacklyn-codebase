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
package com.tcdng.jacklyn.system.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.system.constants.SystemDataSourceTaskConstants;
import com.tcdng.jacklyn.system.constants.SystemDataSourceTaskParamConstants;
import com.tcdng.jacklyn.system.entities.DataSource;
import com.tcdng.jacklyn.system.entities.DataSourceQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.constant.BooleanType;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;

/**
 * Page controller for managing data sources.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/datasource")
@UplBinding("web/system/upl/managedatasource.upl")
public class DataSourceController extends AbstractSystemCrudController<DataSource> {

    private String searchCode;

    private String searchDescription;

    private BooleanType searchAppReserved;
    
	private RecordStatus searchStatus;

	public DataSourceController() {
		super(DataSource.class, "$m{system.datasource.hint}",
				ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.CLIPBOARD
						| ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
	}

	@Action
	public String testDataSource() throws UnifyException {
		TaskSetup taskSetup = TaskSetup.newBuilder().addTask(SystemDataSourceTaskConstants.DATASOURCETESTTASK)
				.setParam(SystemDataSourceTaskParamConstants.DATASOURCE, getRecord()).logMessages().build();
		return launchTaskWithMonitorBox(taskSetup, "$m{system.datasource.test}");
	}

	public String getSearchCode() {
        return searchCode;
    }

    public void setSearchCode(String searchCode) {
        this.searchCode = searchCode;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public BooleanType getSearchAppReserved() {
        return searchAppReserved;
    }

    public void setSearchAppReserved(BooleanType searchAppReserved) {
        this.searchAppReserved = searchAppReserved;
    }

    public RecordStatus getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(RecordStatus searchStatus) {
		this.searchStatus = searchStatus;
	}

	@Override
	protected Object create(DataSource dataSource) throws UnifyException {
		return getSystemService().createDataSource(dataSource);
	}

	@Override
	protected int delete(DataSource dataSource) throws UnifyException {
		return getSystemService().deleteDataSource(dataSource.getId());
	}

	@Override
	protected List<DataSource> find() throws UnifyException {
		DataSourceQuery query = new DataSourceQuery();
		if (QueryUtils.isValidStringCriteria(getSearchCode())) {
			query.name(getSearchCode());
		}

		if (QueryUtils.isValidStringCriteria(getSearchDescription())) {
			query.descriptionLike(getSearchDescription());
		}

		if (searchAppReserved != null) {
		    query.appReserved(searchAppReserved);
		}

		if (searchStatus != null) {
			query.status(searchStatus);
		}

		query.order("description").ignoreEmptyCriteria(true);
		return getSystemService().findDataSources(query);
	}

	@Override
	protected DataSource find(Long dataSourceId) throws UnifyException {
		return getSystemService().findDataSource(dataSourceId);
	}

	@Override
	protected DataSource prepareCreate() throws UnifyException {
		return new DataSource();
	}

	@Override
	protected int update(DataSource dataSource) throws UnifyException {
		return getSystemService().updateDataSource(dataSource);
	}

    @Override
    protected void onPrepareCrudViewer(DataSource dataSource, int mode) throws UnifyException {
        setVisible("frmAppReserved", ManageRecordModifier.ADD != mode);
        if (ManageRecordModifier.isEditable(mode)) {
            setCrudViewerEditable(!BooleanType.TRUE.equals(dataSource.getAppReserved()));
        }
    }
}
