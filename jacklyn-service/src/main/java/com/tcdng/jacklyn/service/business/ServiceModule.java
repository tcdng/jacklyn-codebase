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

import java.util.List;

import com.tcdng.jacklyn.common.business.JacklynBusinessModule;
import com.tcdng.jacklyn.common.business.RemoteCallSystemAssetProvider;
import com.tcdng.jacklyn.service.entities.ClientApp;
import com.tcdng.jacklyn.service.entities.ClientAppLargeData;
import com.tcdng.jacklyn.service.entities.ClientAppQuery;
import com.tcdng.jacklyn.shared.service.data.OSInstallationReqParams;
import com.tcdng.jacklyn.shared.service.data.OSInstallationReqResult;
import com.tcdng.unify.core.UnifyException;

/**
 * Service business module interface.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface ServiceModule extends JacklynBusinessModule, RemoteCallSystemAssetProvider {

	/**
	 * Creates a new client application.
	 * 
	 * @param clientAppLargeData
	 *            the client application data
	 * @return the created client application ID
	 * @throws UnifyException
	 *             if an error occurs
	 */
	Long createClientApp(ClientAppLargeData clientAppLargeData) throws UnifyException;

	/**
	 * Finds an client application by ID.
	 * 
	 * @param id
	 *            the client application ID
	 * @return the client application data
	 * @throws UnifyException
	 *             if client application with ID is not found
	 */
	ClientAppLargeData findClientApp(Long id) throws UnifyException;

	/**
	 * Finds client applications by query.
	 * 
	 * @param query
	 *            the client application query
	 * @return the list of applications found
	 * @throws UnifyException
	 *             if an error occurs
	 */
	List<ClientApp> findClientApps(ClientAppQuery query) throws UnifyException;

	/**
	 * Updates an client application.
	 * 
	 * @param clientAppLargeData
	 *            the client application data
	 * @return the update count
	 * @throws UnifyException
	 *             if an error occurs
	 */
	int updateClientApp(ClientAppLargeData clientAppLargeData) throws UnifyException;

	/**
	 * Deletes an client application.
	 * 
	 * @param id
	 *            the client application ID
	 * @return the delete count
	 * @throws UnifyException
	 *             if an error occurs
	 */
	int deleteClientApp(Long id) throws UnifyException;

	/**
	 * Processes an OS installation request.
	 * 
	 * @param oSInstallationReqParams
	 *            the OS request parameters
	 * @return the request result
	 * @throws UnifyException
	 *             if an error occurs
	 */
	OSInstallationReqResult processOSInstallationRequest(
			OSInstallationReqParams oSInstallationReqParams) throws UnifyException;
}
