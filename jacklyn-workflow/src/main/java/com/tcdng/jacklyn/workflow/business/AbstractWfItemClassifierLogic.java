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
package com.tcdng.jacklyn.workflow.business;

import java.util.Date;

import com.tcdng.jacklyn.workflow.data.FlowingWfItem.Reader;
import com.tcdng.jacklyn.workflow.data.WfDocClassifierFilterDef;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for workflow item classifier.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractWfItemClassifierLogic extends AbstractUnifyComponent implements WfItemClassifierLogic {

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    /**
     * Applies filter to workflow item.
     * 
     * @param flowingWfItemReader
     *            the workflow item reader
     * @param filter
     *            to filter to apply
     * @return a true value if filtered otherwise false
     * @throws UnifyException
     *             if an error occurs
     */
    protected boolean applyFilter(Reader flowingWfItemReader, WfDocClassifierFilterDef filter) throws UnifyException {
        if (flowingWfItemReader != null) {
            Object fieldValue = flowingWfItemReader.read(filter.getFieldName());
            Object limVal1 = resolveValue(flowingWfItemReader, filter, filter.getValue1());
            switch (filter.getOp()) {
                case BETWEEN:
                    if (fieldValue != null) {
                        double dblFieldVal = getDouble(fieldValue);
                        return dblFieldVal >= getDouble(limVal1)
                                && dblFieldVal <= getDouble(resolveValue(flowingWfItemReader, filter, filter.getValue2()));
                    }
                    break;
                case EQUALS:
                    return limVal1.equals(fieldValue);
                case GREATER:
                    if (fieldValue != null) {
                        return getDouble(fieldValue) > getDouble(limVal1);
                    }
                    break;
                case GREATER_OR_EQUAL:
                    if (fieldValue != null) {
                        return getDouble(fieldValue) >= getDouble(limVal1);
                    }
                    break;
                case IS_NOT_NULL:
                    return fieldValue != null;
                case IS_NULL:
                    return fieldValue == null;
                case LESS_OR_EQUAL:
                    if (fieldValue != null) {
                        return getDouble(fieldValue) <= getDouble(limVal1);
                    }
                    break;
                case LESS:
                    if (fieldValue != null) {
                        return getDouble(fieldValue) < getDouble(limVal1);
                    }
                    break;
                case LIKE:
                    if (fieldValue != null) {
                        return ((String) fieldValue).indexOf((String) limVal1) >= 0;
                    }
                    break;
                case BEGIN_WITH:
                    if (fieldValue != null) {
                        return ((String) fieldValue).startsWith((String) limVal1);
                    }
                    break;
                case END_WITH:
                    if (fieldValue != null) {
                        return ((String) fieldValue).endsWith((String) limVal1);
                    }
                    break;
                case NOT_BETWEEN:
                    if (fieldValue != null) {
                        double dblFieldVal = getDouble(fieldValue);
                        return dblFieldVal < getDouble(limVal1)
                                || dblFieldVal > getDouble(resolveValue(flowingWfItemReader, filter, filter.getValue2()));
                    }
                    break;
                case NOT_EQUAL:
                    return !limVal1.equals(fieldValue);
                case NOT_LIKE:
                    if (fieldValue != null) {
                        return ((String) fieldValue).indexOf((String) limVal1) < 0;
                    }
                    break;
                case NOT_BEGIN_WITH:
                    if (fieldValue != null) {
                        return !((String) fieldValue).startsWith((String) limVal1);
                    }
                    break;
                case NOT_END_WITH:
                    if (fieldValue != null) {
                        return !((String) fieldValue).endsWith((String) limVal1);
                    }
                    break;
                default:
                    break;
            }
        }

        return false;
    }

    private Object resolveValue(Reader flowingWfItemReader, WfDocClassifierFilterDef filter, String value)
            throws UnifyException {
        Object val = null;
        if (filter.isFieldOnly()) {
            val = flowingWfItemReader.read(value);
        } else {
            Class<?> fieldType = flowingWfItemReader.getFieldType(filter.getFieldName());
            val = DataUtils.convert(fieldType, value, null);
        }

        if (val instanceof Date) {
            val = ((Date) val).getTime();
        }

        return val;
    }

    private double getDouble(Object val) {
        if (val instanceof Number) {
            return ((Number) val).doubleValue();
        }

        if (val instanceof Date) {
            return ((Date) val).getTime();
        }

        if (val instanceof String) {
            return ((String) val).length();
        }

        return 0;
    }

}
