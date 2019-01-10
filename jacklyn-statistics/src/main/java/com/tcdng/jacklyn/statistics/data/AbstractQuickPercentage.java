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

package com.tcdng.jacklyn.statistics.data;

/**
 * Abstract quick percentage.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class AbstractQuickPercentage<T extends Number> implements QuickPercentage<T>{

    private T value;

    private T totalValue;

    private Double percentage;

    private Double fraction;

    public AbstractQuickPercentage(T value, T totalValue) {
        this.value = value;
        this.totalValue = totalValue;
        if (!isTotalValueZero()) {
            this.percentage = (value.doubleValue() * 100) / totalValue.doubleValue();
            this.fraction =  value.doubleValue() / totalValue.doubleValue();
        }
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public T getTotalValue() {
        return totalValue;
    }

    @Override
    public Double getPercentage() {
        return percentage;
    }

    @Override
    public Double getFraction() {
        return fraction;
    }

    public boolean isTotalValueZero() {
        return totalValue == null || totalValue.doubleValue() == 0.0;
    }
}
