/*
 * Copyright 2018-2020 The Code Department
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

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import com.tcdng.unify.core.util.DataUtils;

/**
 * Quick ratios data object.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class QuickRatio {

    private List<Ratio> ratios;

    public QuickRatio(List<Ratio> ratios) {
        this.ratios = Collections.unmodifiableList(ratios);
    }

    public List<Ratio> getRatios() {
        return ratios;
    }

    public boolean isSanityCheck() {
        if(DataUtils.isBlank(ratios)) {
            return false;
        }
        
        for(Ratio ratio: ratios) {
            if(ratio.value != 0.00) {
                return true;
            }
        }
        
        return false;
    }
    
    public static class Ratio {
        
        private String name;
        
        private Double value;
        
        private Color color;

        public Ratio(String name, Double value, Color color) {
            this.name = name;
            this.value = value;
            this.color = color;
        }

        public Ratio(String name, Double value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Double getValue() {
            return value;
        }

        public Color getColor() {
            return color;
        }
    }
}
