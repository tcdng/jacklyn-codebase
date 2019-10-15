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

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.PackableDoc;
import com.tcdng.unify.core.format.Formatter;

/**
 * Flowing workflow item.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class FlowingWfItem implements ViewableWfItem {

    private WfStepDef wfStepDef;

    private WfTemplateDocDef wfTemplateDocDef;

    private String processGlobalName;

    private String branchCode;

    private String departmentCode;

    private Long wfItemId;

    private Long wfItemHistId;

    private Long wfHistEventId;

    private String description;

    private String title;

    private String label;

    private String comment;

    private PackableDoc pd;

    private Reader reader;

    private ReaderWriter readerWriter;

    private Date createDt;

    private Date stepDt;

    private String heldBy;

    private List<WfAction> actionList;

    public FlowingWfItem(WfStepDef wfStepDef, WfTemplateDocDef wfTemplateDocDef, String processGlobalName,
            String branchCode, String departmentCode, Long wfItemId, Long wfItemHistId, Long wfHistEventId,
            String description, String title, Date createDt, Date stepDt, String heldBy, PackableDoc pd) {
        this.wfStepDef = wfStepDef;
        this.wfTemplateDocDef = wfTemplateDocDef;
        this.processGlobalName = processGlobalName;
        this.branchCode = branchCode;
        this.departmentCode = departmentCode;
        this.wfItemId = wfItemId;
        this.wfItemHistId = wfItemHistId;
        this.wfHistEventId = wfHistEventId;
        this.description = description;
        this.title = title;
        this.createDt = createDt;
        this.stepDt = stepDt;
        this.heldBy = heldBy;
        this.pd = pd;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDocViewer() {
        return wfTemplateDocDef.getWfDocUplGenerator().getDocViewer(pd);
    }

    @Override
    public PackableDoc getPd() {
        return pd;
    }

    @Override
    public WfStepDef getWfStepDef() {
        return wfStepDef;
    }

    @Override
    public WfTemplateDocDef getWfTemplateDocDef() {
        return wfTemplateDocDef;
    }

    public Reader getReader() {
        if (reader == null) {
            reader = new Reader();
        }

        return reader;
    }

    public ReaderWriter getReaderWriter() {
        if (readerWriter == null) {
            readerWriter = new ReaderWriter();
        }

        return readerWriter;
    }

    public void setWfStepDef(WfStepDef wfStepDef) {
        this.wfStepDef = wfStepDef;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getBranchCode() {
        return branchCode;
    }

    @Override
    public String getDepartmentCode() {
        return departmentCode;
    }

    @Override
    public String getProcessGlobalName() {
        return processGlobalName;
    }

    @Override
    public String getDocName() {
        return wfTemplateDocDef.getDocName();
    }

    public Long getWfItemId() {
        return wfItemId;
    }

    public Long getDocId() {
        return (Long) pd.getId();
    }

    public Long getWfItemHistId() {
        return wfItemHistId;
    }

    public void setWfItemHistId(Long wfItemHistId) {
        this.wfItemHistId = wfItemHistId;
    }

    public Long getWfHistEventId() {
        return wfHistEventId;
    }

    public void setWfHistEventId(Long wfHistEventId) {
        this.wfHistEventId = wfHistEventId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public Date getStepDt() {
        return stepDt;
    }

    public String getHeldBy() {
        return heldBy;
    }

    public List<WfAction> getActionList() {
        return actionList;
    }

    public void setActionList(List<WfAction> actionList) {
        this.actionList = actionList;
    }

    public List<WfFormPrivilegeDef> getFormPrivilegeList() {
        return wfStepDef.getFormPrivilegeList();
    }
    
    public class Reader {

        private Reader() {
            
        }

        public String getBranchCode() {
            return branchCode;
        }

        public String getDepartmentCode() {
            return departmentCode;
        }

        public Set<String> getFieldNames() {
            return pd.getConfig().getFieldNames();
        }

        public Class<?> getFieldType(String fieldName) throws UnifyException {
            return pd.getConfig().getFieldConfig(fieldName).getDataType();
        }

        public boolean isList(String fieldName) throws UnifyException {
            return pd.getConfig().getFieldConfig(fieldName).isList();
        }

        public boolean isComplex(String fieldName) throws UnifyException {
            return pd.getConfig().getFieldConfig(fieldName).isComplex();
        }
        
        public Object readField(String fieldName) throws UnifyException {
            return pd.read(fieldName);
        }

        public <T> T readField(Class<T> type, String fieldName) throws UnifyException {
            return pd.read(type, fieldName);
        }

        public <T> List<T> readListField(Class<T> type, String fieldName) throws UnifyException {
            return pd.readList(type, fieldName);
        }
    }
    
    public class ReaderWriter extends Reader {

        private ReaderWriter() {

        }

        public void writeField(String fieldName, Object value) throws UnifyException {
            pd.write(fieldName, value);
        }

        public void writeField(String fieldName, Object value, Formatter<?> formatter) throws UnifyException {
            pd.write(fieldName, value, formatter);
        }

    }
    
}
