/*
 * Copyright 2018-2020 The Code Department.
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
package com.tcdng.jacklyn.shared.xml.config.module;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Report configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class ReportConfig extends BaseConfig {

    private String title;

    private String processor;

    private String reportable;

    private String template;

    private String layout;

    private ColumnsConfig columns;

    private ParametersConfig parameters;

    private FilterConfig filter;

    private boolean invertGroupColors;

    private boolean landscape;

    private boolean underlineRows;

    private boolean shadeOddRows;

    public String getTitle() {
        return title;
    }

    @XmlAttribute(required = true)
    public void setTitle(String title) {
        this.title = title;
    }

    public String getProcessor() {
        return processor;
    }

    @XmlAttribute
    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getReportable() {
        return reportable;
    }

    @XmlAttribute
    public void setReportable(String reportable) {
        this.reportable = reportable;
    }

    public ColumnsConfig getColumns() {
        return columns;
    }

    @XmlElement
    public void setColumns(ColumnsConfig columns) {
        this.columns = columns;
    }

    public ParametersConfig getParameters() {
        return parameters;
    }

    @XmlElement
    public void setParameters(ParametersConfig parameters) {
        this.parameters = parameters;
    }

    public FilterConfig getFilter() {
        return filter;
    }

    @XmlElement(name = "filter")
    public void setFilter(FilterConfig filter) {
        this.filter = filter;
    }

    public String getTemplate() {
        return template;
    }

    @XmlAttribute
    public void setTemplate(String template) {
        this.template = template;
    }

    public String getLayout() {
        return layout;
    }

    @XmlAttribute
    public void setLayout(String layout) {
        this.layout = layout;
    }

    public boolean isInvertGroupColors() {
        return invertGroupColors;
    }

    @XmlAttribute
    public void setInvertGroupColors(boolean invertGroupColors) {
        this.invertGroupColors = invertGroupColors;
    }

    public boolean isLandscape() {
        return landscape;
    }

    @XmlAttribute
    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }

    public boolean isUnderlineRows() {
        return underlineRows;
    }

    @XmlAttribute
    public void setUnderlineRows(boolean underlineRows) {
        this.underlineRows = underlineRows;
    }

    public boolean isShadeOddRows() {
        return shadeOddRows;
    }

    @XmlAttribute
    public void setShadeOddRows(boolean shadeOddRows) {
        this.shadeOddRows = shadeOddRows;
    }
}
