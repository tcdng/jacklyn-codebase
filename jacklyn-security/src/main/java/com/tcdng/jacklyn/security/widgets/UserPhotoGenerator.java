/*
 * Copyright 2018 The Code Department
 * 
 * http://www.tcdng.com/licenses/Jacklyn-Tools-1.0.txt
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tcdng.jacklyn.security.widgets;

import java.io.IOException;
import java.io.OutputStream;

import com.tcdng.jacklyn.common.constants.JacklynSessionAttributeConstants;
import com.tcdng.jacklyn.security.business.SecurityService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.resource.AbstractImageGenerator;
import com.tcdng.unify.core.resource.ImageFormat;

/**
 * User photo generator.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("userphoto-generator")
public class UserPhotoGenerator extends AbstractImageGenerator {

    @Configurable
    private SecurityService securityModule;

    private byte[] photo;

    @Override
    public ImageFormat generate(OutputStream outputStream) throws UnifyException {
        try {
            outputStream.write(photo);
            outputStream.flush();
        } catch (IOException e) {
            throwOperationErrorException(e);
        } finally {
            photo = null;
        }

        return ImageFormat.WILDCARD;
    }

    @Override
    public boolean isReady() throws UnifyException {
        Long userId = getUserId();
        if (userId != null && userId > 0) {
            photo = securityModule.findUserPhotograph(userId);
            return photo != null;
        }

        return false;
    }

    private Long getUserId() throws UnifyException {
        return (Long) getSessionAttribute(JacklynSessionAttributeConstants.USERID);
    }
}
