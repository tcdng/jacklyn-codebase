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
package com.tcdng.jacklyn.audit;

import java.math.BigDecimal;

import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Id;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.database.Entity;

/**
 * Test record.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table
public class TestRecord implements Entity {

    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private BigDecimal balance;

    public TestRecord(Long id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public TestRecord() {

    }

    @Override
    public Entity getExt() {
        return null;
    }

    @Override
    public void setExt(Entity entity) {
        
    }

    @Override
    public String getListKey() {
        return String.valueOf(id);
    }

    @Override
    public String getListDescription() {
        return name;
    }

    @Override
    public String getDescription() {
        return name;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public boolean isReserved() {
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
