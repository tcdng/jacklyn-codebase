/*
 * Copyright 2014 The Code Department
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tcdng.jacklyn.datasource.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.datasource.constants.DataSourceTaskConstants;
import com.tcdng.jacklyn.datasource.constants.DataSourceTaskParamConstants;
import com.tcdng.jacklyn.datasource.entities.DataSource;
import com.tcdng.jacklyn.datasource.entities.DataSourceQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;

/**
 * Page controller for managing data sources.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/datasource/datasource")
@UplBinding("web/datasource/upl/managedatasource.upl")
public class ManageDataSourceController extends AbstractDataSourceCrudController<DataSource> {

    private String searchCode;

    private String searchDescription;

	private RecordStatus searchStatus;

	public ManageDataSourceController() {
		super(DataSource.class, "$m{datasource.datasource.hint}",
				ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.CLIPBOARD
						| ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
	}

	@Action
	public String testDataSource() throws UnifyException {
		TaskSetup taskSetup = TaskSetup.newBuilder().addTask(DataSourceTaskConstants.DATASOURCETESTTASK)
				.setParam(DataSourceTaskParamConstants.DATASOURCE, getRecord()).logMessages().build();
		return launchTaskWithMonitorBox(taskSetup, "$m{datasource.datasource.test}");
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

    public RecordStatus getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(RecordStatus searchStatus) {
		this.searchStatus = searchStatus;
	}

	@Override
	protected Object create(DataSource dataSource) throws UnifyException {
		return getDataSourceService().createDataSource(dataSource);
	}

	@Override
	protected int delete(DataSource dataSource) throws UnifyException {
		return getDataSourceService().deleteDataSource(dataSource.getId());
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

		if (searchStatus != null) {
			query.status(searchStatus);
		}

		query.order("description").ignoreEmptyCriteria(true);
		return getDataSourceService().findDataSources(query);
	}

	@Override
	protected DataSource find(Long dataSourceId) throws UnifyException {
		return getDataSourceService().findDataSource(dataSourceId);
	}

	@Override
	protected DataSource prepareCreate() throws UnifyException {
		return new DataSource();
	}

	@Override
	protected int update(DataSource dataSource) throws UnifyException {
		return getDataSourceService().updateDataSource(dataSource);
	}
}
