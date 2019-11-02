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

import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.workflow.entities.WfItem;
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

    private WfProcessDef wfProcessDef;

    private WfStepDef wfStepDef;

    private WfStepDef sourceWfStepDef;

    private Long wfItemId;

    private Long wfItemHistId;

    private Long wfHistEventId;

    private Long wfItemSplitEventId;

    private Long wfItemAttachmentRefId;

    private String wfItemBranchCode;

    private String wfItemDepartmentCode;

    private String description;

    private String title;

    private String label;

    private String comment;

    private String errorSource;

    private String errorCode;

    private String errorMsg;

    private String splitBranchName;

    private PackableDoc pd;

    private Reader reader;

    private ReaderWriter readerWriter;

    private Date createDt;

    private Date stepDt;

    private String heldBy;

    private List<WfAction> actionList;

    public FlowingWfItem(WfProcessDef wfProcessDef, WfStepDef wfStepDef, WfItem wfItem, Long wfItemId, String title,
            PackableDoc pd) {
        this.wfStepDef = wfStepDef;
        this.wfProcessDef = wfProcessDef;
        this.wfItemBranchCode = wfItem.getBranchCode();
        this.wfItemDepartmentCode = wfItem.getDepartmentCode();
        this.wfItemId = wfItemId;
        this.wfItemHistId = wfItem.getWfItemHistId();
        this.wfHistEventId = wfItem.getWfHistEventId();
        this.wfItemSplitEventId = wfItem.getWfItemSplitEventId();
        this.wfItemAttachmentRefId = wfItem.getWfItemAttachmentRefId();
        this.splitBranchName = wfItem.getSplitBranchName();
        this.description = wfItem.getDescription();
        this.title = title;
        this.createDt = wfItem.getCreateDt();
        this.stepDt = wfItem.getStepDt();
        this.heldBy = wfItem.getHeldBy();
        this.pd = pd;
    }
    
    public FlowingWfItem() {
        
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDocViewer() {
        return wfProcessDef.getWfTemplateDocDef().getWfDocUplGenerator().getDocViewer(pd);
    }

    @Override
    public String getBranchCode() {
        return wfItemBranchCode;
    }

    @Override
    public String getDepartmentCode() {
        return wfItemDepartmentCode;
    }

    @Override
    public String getProcessGlobalName() {
        return wfProcessDef.getGlobalName();
    }

    @Override
    public String getDocName() {
        return wfProcessDef.getWfTemplateDocDef().getDocName();
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
        return wfProcessDef.getWfTemplateDocDef();
    }

    public WfTemplateDef getWfTemplateDef() {
        return wfProcessDef.getWfTemplateDef();
    }

    public WfProcessDef getWfProcessDef() {
        return wfProcessDef;
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

    public WfStepDef getErrorWfStepDef() {
        return wfProcessDef.getWfTemplateDef().getErrorStep();
    }

    public WfStepDef getSourceWfStepDef() {
        return sourceWfStepDef;
    }

    public void setSourceWfStepDef(WfStepDef sourceWfStepDef) {
        this.sourceWfStepDef = sourceWfStepDef;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorSource() {
        return errorSource;
    }

    public void setErrorSource(String errorSource) {
        this.errorSource = errorSource;
    }

    public String getDescription() {
        return description;
    }

    public String getSplitBranchName() {
        return splitBranchName;
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

    public void setHeldBy(String heldBy) {
        this.heldBy = heldBy;
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

    public Long getWfItemSplitEventId() {
        return wfItemSplitEventId;
    }

    public Long getWfItemAttachmentRefId() {
        return wfItemAttachmentRefId;
    }

    public List<WfFormPrivilegeDef> getFormPrivilegeList() {
        return wfStepDef.getFormPrivilegeList();
    }

    public class Reader {

        private Restrictions restrictions;

        private Reader() {
            String branchCode = null;
            if (wfStepDef.isBranchOnly()) {
                branchCode = wfItemBranchCode;
            }

            String departmentCode = null;
            if (wfStepDef.isDepartmentOnly()) {
                departmentCode = wfItemDepartmentCode;
            }

            restrictions = new Restrictions(branchCode, departmentCode);
        }

        public Restrictions getRestrictions() {
            return restrictions;
        }

        public WorkflowParticipantType getStepParticipant() {
            return wfStepDef.getParticipantType();
        }

        public String getStepGlobalName() {
            return wfStepDef.getGlobalName();
        }

        public String getItemBranchCode() {
            return wfItemBranchCode;
        }

        public String getItemDepartmentCode() {
            return wfItemDepartmentCode;
        }

        public String getItemHeldBy() {
            return heldBy;
        }

        public String getConfigName() {
            return pd.getConfigName();
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

        public boolean isLimitItemToBranch() {
            return wfStepDef.isBranchOnly();
        }

        public boolean isLimitItemToDepartment() {
            return wfStepDef.isDepartmentOnly();
        }

        public Object read(String fieldName) throws UnifyException {
            return pd.read(fieldName);
        }

        public <T> T read(Class<T> type, String fieldName) throws UnifyException {
            return pd.read(type, fieldName);
        }

        public <T> List<T> readList(Class<T> type, String fieldName) throws UnifyException {
            return pd.readList(type, fieldName);
        }
    }

    public class ReaderWriter extends Reader {

        private ReaderWriter() {

        }

        public void write(String fieldName, Object value) throws UnifyException {
            pd.write(fieldName, value);
        }

        public void write(String fieldName, Object value, Formatter<?> formatter) throws UnifyException {
            pd.write(fieldName, value, formatter);
        }

    }

    public class Restrictions {

        String branchCode;

        String departmentCode;

        public Restrictions(String branchCode, String departmentCode) {
            this.branchCode = branchCode;
            this.departmentCode = departmentCode;
        }

        public String getBranchCode() {
            return branchCode;
        }

        public String getDepartmentCode() {
            return departmentCode;
        }
    }
}
