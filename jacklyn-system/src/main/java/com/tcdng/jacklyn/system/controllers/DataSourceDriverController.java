/*
 * Copyright 2014 The Code Department
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
import com.tcdng.jacklyn.system.entities.DataSourceDriver;
import com.tcdng.jacklyn.system.entities.DataSourceDriverQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Page controller for managing data source drivers.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/datasourcedriver")
@UplBinding("web/system/upl/managedatasourcedriver.upl")
public class DataSourceDriverController extends AbstractSystemCrudController<DataSourceDriver> {

    private String searchCode;

    private String searchDescription;

    private RecordStatus searchStatus;

	public DataSourceDriverController() {
		super(DataSourceDriver.class, "$m{system.datasourcedriver.hint}",
				ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.CLIPBOARD
						| ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
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
	protected Object create(DataSourceDriver datasourceDriver) throws UnifyException {
		return getSystemService().createDataSourceDriver(datasourceDriver);
	}

	@Override
	protected int delete(DataSourceDriver datasourceDriver) throws UnifyException {
		return getSystemService().deleteDataSourceDriver(datasourceDriver.getId());
	}

	@Override
	protected List<DataSourceDriver> find() throws UnifyException {
		DataSourceDriverQuery query = new DataSourceDriverQuery();
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
		return getSystemService().findDataSourceDrivers(query);
	}

	@Override
	protected DataSourceDriver find(Long dataSourceDriverId) throws UnifyException {
		return getSystemService().findDataSourceDriver(dataSourceDriverId);
	}

	@Override
	protected DataSourceDriver prepareCreate() throws UnifyException {
		return new DataSourceDriver();
	}

	@Override
	protected int update(DataSourceDriver datasourceDriver) throws UnifyException {
		return getSystemService().updateDataSourceDriver(datasourceDriver);
	}
}
