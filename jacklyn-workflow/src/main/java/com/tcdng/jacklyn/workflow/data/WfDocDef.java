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

package com.tcdng.jacklyn.workflow.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.workflow.constants.WorkflowModuleErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.PackableDocConfig;
import com.tcdng.unify.core.util.StringUtils.StringToken;

/**
 * Workflow document definition.
 * 
 * @author Lateef
 * @since 1.0
 */
public class WfDocDef extends BaseWfDef {

    private Long wfDocId;

    private PackableDocConfig docConfig;

    private String wfCategoryName;

    private String globalName;

    private long timestamp;

    private WfFormDef wfFormDef;

    private List<StringToken> itemDescFormat;

    private Map<Class<?>, WfDocBeanMappingDef> receptacleBeanMappings;

    private Map<String, WfDocBeanMappingDef> beanMappings;

    private Map<String, WfDocAttachmentDef> attachments;

    private Map<String, WfDocClassifierDef> classifiers;

    public WfDocDef(Long wfDocId, String wfCategoryName, String globalName, String name, String description,
            PackableDocConfig docConfig, long timestamp, WfFormDef wfFormDef, List<StringToken> itemDescFormat,
            List<WfDocBeanMappingDef> beanMappingList, List<WfDocAttachmentDef> attachmentList,
            List<WfDocClassifierDef> classifierList) {
        super(name, description);
        this.wfDocId = wfDocId;
        this.wfCategoryName = wfCategoryName;
        this.globalName = globalName;
        this.docConfig = docConfig;
        this.timestamp = timestamp;
        this.wfFormDef = wfFormDef;
        this.itemDescFormat = itemDescFormat;

        if (beanMappingList != null) {
            receptacleBeanMappings = new HashMap<Class<?>, WfDocBeanMappingDef>();
            beanMappings = new HashMap<String, WfDocBeanMappingDef>();
            for (WfDocBeanMappingDef wfDocBeanMappingDef : beanMappingList) {
                if (wfDocBeanMappingDef.isReceptacleMapping()) {
                    receptacleBeanMappings.put(wfDocBeanMappingDef.getBeanType(), wfDocBeanMappingDef);
                }

                beanMappings.put(wfDocBeanMappingDef.getName(), wfDocBeanMappingDef);
            }
        } else {
            beanMappings = Collections.emptyMap();
            receptacleBeanMappings = Collections.emptyMap();
        }

        if (attachmentList != null) {
            attachments = new LinkedHashMap<String, WfDocAttachmentDef>();
            for (WfDocAttachmentDef wfDocAttachmentDef : attachmentList) {
                attachments.put(wfDocAttachmentDef.getName(), wfDocAttachmentDef);
            }

            attachments = Collections.unmodifiableMap(attachments);
        } else {
            attachments = Collections.emptyMap();
        }

        if (classifierList != null) {
            classifiers = new LinkedHashMap<String, WfDocClassifierDef>();
            for (WfDocClassifierDef wfDocClassifierDef : classifierList) {
                classifiers.put(wfDocClassifierDef.getName(), wfDocClassifierDef);
            }

            classifiers = Collections.unmodifiableMap(classifiers);
        } else {
            classifiers = Collections.emptyMap();
        }
    }

    public Long getWfDocId() {
        return wfDocId;
    }

    public PackableDocConfig getDocConfig() {
        return docConfig;
    }

    public String getGlobalName() {
        return globalName;
    }

    public String getWfCategoryName() {
        return wfCategoryName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public WfFormDef getWfFormDef() {
        return wfFormDef;
    }

    public List<StringToken> getItemDescFormat() {
        return itemDescFormat;
    }

    public Map<String, WfDocAttachmentDef> getAttachments() {
        return attachments;
    }

    public Map<String, WfDocClassifierDef> getClassifiers() {
        return classifiers;
    }

    public Set<String> getWfDocBeanMappingNames() {
        return beanMappings.keySet();
    }

    public WfDocBeanMappingDef getWfDocBeanMappingDef(String name) {
        return beanMappings.get(name);
    }

    public WfDocBeanMappingDef getReceptacleWfDocBeanMappingDef(Class<?> beanType) throws UnifyException {
        WfDocBeanMappingDef wfDocBeanMappingDef = receptacleBeanMappings.get(beanType);
        if (wfDocBeanMappingDef == null) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_DOCUMENT_ENTRY_BEANMAPPING_FOR_TYPE_UNKNOWN,
                    getDescription(), beanType);
        }

        return wfDocBeanMappingDef;
    }

    public Set<String> getWfDocAttachmentNames() {
        return attachments.keySet();
    }

    public WfDocAttachmentDef getWfDocAttachmentDef(String name) {
        return attachments.get(name);
    }

    public Set<String> getWfDocClassifierNames() {
        return classifiers.keySet();
    }

    public WfDocClassifierDef getWfDocClassifierDef(String name) {
        return classifiers.get(name);
    }

    public int getFieldCount() {
        return docConfig.getFieldCount();
    }

    public boolean isForm() {
        return wfFormDef != null;
    }
}
