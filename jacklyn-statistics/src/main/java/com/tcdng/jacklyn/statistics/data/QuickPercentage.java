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
 * Object with pre-calculated percentage.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class QuickPercentage {

    private double value;

    private double totalValue;

    private double percentage;

    private double fraction;

    public QuickPercentage(double value, double totalValue) {
        this.value = value;
        this.totalValue = totalValue;
        if (!isTotalValueZero()) {
            this.percentage = (value * 100) / totalValue;
            this.fraction = value / totalValue;
        }
    }

    public double getValue() {
        return value;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public double getPercentage() {
        return percentage;
    }

    public double getFraction() {
        return fraction;
    }

    public boolean isTotalValueZero() {
        return totalValue == 0.0;
    }
}
