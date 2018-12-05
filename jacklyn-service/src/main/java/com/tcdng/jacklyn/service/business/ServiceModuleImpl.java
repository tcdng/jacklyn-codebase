/*
 * Copyright 2018 The Code Department
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tcdng.jacklyn.service.business;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessModule;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.service.constants.ServiceModuleErrorConstants;
import com.tcdng.jacklyn.service.constants.ServiceModuleNameConstants;
import com.tcdng.jacklyn.service.entities.ClientApp;
import com.tcdng.jacklyn.service.entities.ClientAppAsset;
import com.tcdng.jacklyn.service.entities.ClientAppAssetQuery;
import com.tcdng.jacklyn.service.entities.ClientAppLargeData;
import com.tcdng.jacklyn.service.entities.ClientAppQuery;
import com.tcdng.jacklyn.shared.service.ClientAppType;
import com.tcdng.jacklyn.shared.service.data.OSInstallationReqParams;
import com.tcdng.jacklyn.shared.service.data.OSInstallationReqResult;
import com.tcdng.jacklyn.shared.system.SystemAssetType;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.jacklyn.system.business.SystemModule;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.jacklyn.system.constants.SystemModuleSysParamConstants;
import com.tcdng.jacklyn.system.entities.SystemAssetQuery;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Broadcast;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.list.ListManager;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.util.WebUtils;

/**
 * Service business module implementation.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(ServiceModuleNameConstants.SERVICEBUSINESSMODULE)
public class ServiceModuleImpl extends AbstractJacklynBusinessModule
		implements ServiceModule {

	@Configurable(SystemModuleNameConstants.SYSTEMBUSINESSMODULE)
	private SystemModule systemModule;

	@Configurable(ApplicationComponents.APPLICATION_LISTMANAGER)
	private ListManager listManager;

	private Map<String, Set<String>> clientAppAccessFlags;

	public ServiceModuleImpl() {
		clientAppAccessFlags = new HashMap<String, Set<String>>();
	}

	@Override
	public Long createClientApp(ClientAppLargeData clientAppLargeData) throws UnifyException {
		Long clientAppId = (Long) db().create(clientAppLargeData.getData());
		updateClientAppAssets(clientAppId, clientAppLargeData.getSystemAssetIdList());
		return clientAppId;
	}

	@Override
	public ClientAppLargeData findClientApp(Long id) throws UnifyException {
		ClientApp clientApp = db().list(ClientApp.class, id);
		List<Long> clientAppAssetIdList = db().valueList(Long.class, "systemAssetId",
				new ClientAppAssetQuery().clientAppId(id));
		return new ClientAppLargeData(clientApp, clientAppAssetIdList);
	}

	@Override
	public List<ClientApp> findClientApps(ClientAppQuery query) throws UnifyException {
		return db().listAll(query);
	}

	@Override
	public int updateClientApp(ClientAppLargeData clientAppLargeData) throws UnifyException {
		int result = db().updateByIdVersion(clientAppLargeData.getData());
		updateClientAppAssets(clientAppLargeData.getId(),
				clientAppLargeData.getSystemAssetIdList());
		clearClientAppAssetAccess(clientAppLargeData.getData().getName());
		return result;
	}

	@Override
	public int deleteClientApp(Long id) throws UnifyException {
		db().deleteAll(new ClientAppAssetQuery().clientAppId(id));
		return db().delete(ClientApp.class, id);
	}

	@Override
	public boolean accessSystemAsset(String clientAppName, SystemAssetType systemAssetType,
			String assetName) throws UnifyException {
		Set<String> accessFlags = clientAppAccessFlags.get(clientAppName);
		if (accessFlags == null) {
			accessFlags = new HashSet<String>();
			clientAppAccessFlags.put(clientAppName, accessFlags);
		}

		String assetCheckKey = StringUtils.dotify(systemAssetType, assetName);
		if (!accessFlags.contains(assetCheckKey)) {
			if (db().countAll(new ClientAppQuery().name(clientAppName)) == 0) {
				throw new UnifyException(ServiceModuleErrorConstants.APPLICATION_UNKNOWN,
						clientAppName);
			}

			ClientAppAsset clientAppAsset = db().list(new ClientAppAssetQuery()
					.clientAppName(clientAppName).assetType(systemAssetType).assetName(assetName));
			if (clientAppAsset == null) {
				throw new UnifyException(ServiceModuleErrorConstants.APPLICATION_NO_SUCH_ASSET,
						clientAppName, assetName);
			}

			if (RecordStatus.INACTIVE.equals(clientAppAsset.getClientAppStatus())) {
				throw new UnifyException(ServiceModuleErrorConstants.APPLICATION_INACTIVE,
						clientAppName);
			}

			if (RecordStatus.INACTIVE.equals(clientAppAsset.getAssetStatus())) {
				throw new UnifyException(
						ServiceModuleErrorConstants.APPLICATION_ASSET_INACTIVE, clientAppName,
						assetName);
			}

			accessFlags.add(assetCheckKey);
		}

		return true;
	}

	@Override
	public OSInstallationReqResult processOSInstallationRequest(
			OSInstallationReqParams oSInstallationReqParams) throws UnifyException {
		logDebug("Processing OS installation request for [{0}]...",
				oSInstallationReqParams.getOsName());

		// Create application here
		boolean isAlreadyInstalled = true;
		Long clientAppId = null;
		ClientApp oldClientApp = db().list(new ClientAppQuery()
				.name(oSInstallationReqParams.getClientAppCode()).type(ClientAppType.OS));
		if (oldClientApp == null) {
			logDebug("Creating application of type OS...");
			ClientApp clientApp = new ClientApp();
			clientApp.setName(oSInstallationReqParams.getClientAppCode());
			clientApp.setDescription(oSInstallationReqParams.getOsName());
			clientApp.setType(ClientAppType.OS);
			clientAppId = (Long) db().create(clientApp);
			isAlreadyInstalled = false;
		} else {
			logDebug("...application [{0}] of type OS is already installed.",
					oSInstallationReqParams.getOsName());
			oldClientApp.setDescription(oSInstallationReqParams.getOsName());
			db().updateByIdVersion(oldClientApp);
			clientAppId = oldClientApp.getId();
		}

		// Grant OS access to all remote calls.
		List<Long> systemAssetIdList = systemModule
				.findSystemAssetIds(new SystemAssetQuery().type(SystemAssetType.REMOTECALLMETHOD));
		updateClientAppAssets(clientAppId, systemAssetIdList);

		// Return result
		logDebug("Preparing installation result...");
		OSInstallationReqResult airResult = new OSInstallationReqResult();
		airResult.setAppName(getApplicationName());
		airResult.setAppName(getApplicationName());
		String bannerFilename
				= WebUtils.expandThemeTag(systemModule.getSysParameterValue(String.class,
						SystemModuleSysParamConstants.SYSPARAM_APPLICATION_BANNER));
		byte[] icon = IOUtils.readFileResourceInputStream(bannerFilename);
		airResult.setAppIcon(icon);
		airResult.setAlreadyInstalled(isAlreadyInstalled);
		logDebug("OS installation for [{0}] completed.", oSInstallationReqParams.getOsName());
		return airResult;
	}

	@Override
	public void installFeatures(List<ModuleConfig> featureDefinitions) throws UnifyException {

	}

	@Broadcast
	public void clearClientAppAssetAccess(String... params) throws UnifyException {
		for (String clientAppCode : params) {
			clientAppAccessFlags.remove(clientAppCode);
		}
	}

	private void updateClientAppAssets(Long clientAppId, List<Long> systemAssetIdList)
			throws UnifyException {
		db().deleteAll(new ClientAppAssetQuery().clientAppId(clientAppId));
		ClientAppAsset clientAppAsset = new ClientAppAsset();
		clientAppAsset.setClientAppId(clientAppId);
		for (Long systemAssetId : systemAssetIdList) {
			clientAppAsset.setSystemAssetId(systemAssetId);
			db().create(clientAppAsset);
		}
	}

}
