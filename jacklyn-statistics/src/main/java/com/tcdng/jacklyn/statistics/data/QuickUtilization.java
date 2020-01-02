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

package com.tcdng.jacklyn.statistics.data;

/**
 * Object with pre-calculated utilization.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class QuickUtilization {

    private QuickPercentage quickPercentage;

    private double freeValue;

    private byte[] presentation;

    public QuickUtilization(QuickPercentage quickPercentage, byte[] presentation) {
        this.quickPercentage = quickPercentage;
        this.presentation = presentation;
        freeValue = quickPercentage.getTotalValue() - quickPercentage.getValue();
    }

    public double getValue() {
        return quickPercentage.getValue();
    }

    public double getTotalValue() {
        return quickPercentage.getTotalValue();
    }

    public double getPercentage() {
        return quickPercentage.getPercentage();
    }

    public double getFraction() {
        return quickPercentage.getFraction();
    }

    public boolean isTotalValueZero() {
        return quickPercentage.isTotalValueZero();
    }

    public double getFreeValue() {
        return freeValue;
    }

    public byte[] getPresentation() {
        return presentation;
    }
    
}
