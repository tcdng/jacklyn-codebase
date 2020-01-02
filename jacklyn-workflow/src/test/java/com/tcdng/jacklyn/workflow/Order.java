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

package com.tcdng.jacklyn.workflow;

import java.math.BigDecimal;

import com.tcdng.unify.core.data.AbstractDocument;

/**
 * Test order object.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class Order extends AbstractDocument {

	private String name;
	
	private String description;
	
	private String address;
	
	private String status;
	
	private Integer quantity;
	
	private BigDecimal unitPrice;
	
	private BigDecimal amount;
	
	private BigDecimal vat;
	
	private BigDecimal totalAmount;
	
	public Order(String name, String description, Integer quantity, BigDecimal unitPrice) {
		this.name = name;
		this.description = description;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
	}

	@Override
	public String getBranchCode() {
		return null;
	}

	@Override
	public String getDepartmentCode() {
		return null;
	}

	@Override
	public Object getId() {
		return null;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getVat() {
		return vat;
	}

	public void setVat(BigDecimal vat) {
		this.vat = vat;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
