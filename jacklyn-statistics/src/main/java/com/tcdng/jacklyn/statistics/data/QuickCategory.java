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
import java.util.List;

import com.tcdng.unify.core.util.DataUtils;

/**
 * Quick categories data object.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class QuickCategory {

    private List<?> xValueList;

    private List<Category> categoryList;

    public QuickCategory(List<?> xValueList, List<Category> categoryList) {
        this.xValueList = DataUtils.unmodifiableList(xValueList);
        this.categoryList = DataUtils.unmodifiableList(categoryList);
    }

    public List<?> getXValueList() {
        return xValueList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public boolean isSanityCheck() {
        if (DataUtils.isBlank(xValueList) || DataUtils.isBlank(categoryList)) {
            return false;
        }
        
        for(Category category: categoryList) {
            if (DataUtils.isBlank(category.yValueList)) {
                return false;
            }
        }
        
        return true;
    }
    
    public static class Category {
        
        private String name;
        
        private List<? extends Number> yValueList;
        
        private Color color;

        public Category(String name, List<? extends Number> yValueList, Color color) {
            this.name = name;
            this.yValueList = yValueList;
            this.color = color;
        }

        public Category(String name, List<? extends Number> yValueList) {
            this.name = name;
            this.yValueList = yValueList;
        }

        public String getName() {
            return name;
        }

        public List<? extends Number> getYValueList() {
            return yValueList;
        }

        public Color getColor() {
            return color;
        }
    }
}
