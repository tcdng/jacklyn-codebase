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
package com.tcdng.jacklyn.common;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.Tooling;

/**
 * TestCustomer workflow document.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Tooling("Test Customer")
@Managed(module = "customer", title = "Test Customer", auditable = true)
@Table(name = "TESTCUSTOMER")
public class TestCustomer extends BaseEntity {

	@Column
	private String firstName;

	@Column
	private String lastName;

	@Column
	private int age;

	@Column
	private double height;

	public TestCustomer(String firstName, String lastName, int age, double height) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.height = height;
	}

	public TestCustomer() {

	}

	public String getDescription() {
		return this.firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

}
