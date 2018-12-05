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

package com.tcdng.jacklyn.system.data;

/**
 * Represents the state of a boolean system parameter
 *
 * @author Lateef
 * @since 1.0
 */
public class SystemControlState {

	private int index;

	private String name;

	private String description;

	private boolean enabled;

	public SystemControlState(int index, String name, String description, boolean enabled) {
		this.index = index;
		this.name = name;
		this.description = description;
		this.enabled = enabled;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String toString() {
		return "SystemControlState [name=" + name + ", description=" + description + ", enabled="
				+ enabled + "]";
	}
}
