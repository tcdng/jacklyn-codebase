/*
 * Copyright 2018 The Code Department
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
package com.tcdng.jacklyn.report.entities;

import java.util.Collection;

import com.tcdng.jacklyn.common.entities.BaseInstallEntityQuery;

/**
 * Query class for reportable fields.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ReportableFieldQuery extends BaseInstallEntityQuery<ReportableField> {

    public ReportableFieldQuery() {
        super(ReportableField.class);
    }

    public ReportableFieldQuery reportableId(Long reportableId) {
        return (ReportableFieldQuery) equals("reportableId", reportableId);
    }

    public ReportableFieldQuery parameterOnly(boolean parameterOnly) {
        return (ReportableFieldQuery) equals("parameterOnly", parameterOnly);
    }

    public ReportableFieldQuery name(String name) {
        return (ReportableFieldQuery) equals("name", name);
    }

    public ReportableFieldQuery nameIn(Collection<String> names) {
        return (ReportableFieldQuery) amongst("name", names);
    }

    public ReportableFieldQuery nameNotIn(Collection<String> names) {
        return (ReportableFieldQuery) notAmongst("name", names);
    }
}
