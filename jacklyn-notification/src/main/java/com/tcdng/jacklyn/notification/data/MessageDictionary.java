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
package com.tcdng.jacklyn.notification.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Message dictionary.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class MessageDictionary implements Serializable {

    private static final long serialVersionUID = 1680429097663631478L;

    private Map<String, Object> dictionary;

    public MessageDictionary(Map<String, Object> dictionary) {
        this.dictionary = dictionary;
    }

    public MessageDictionary() {
        dictionary = new HashMap<String, Object>();
    }

    public void addEntry(String entryName, Object value) {
        dictionary.put(entryName, value);
    }

    public Set<String> getEntryNames() {
        return dictionary.keySet();
    }

    public Object getEntry(String entryName) {
        return dictionary.get(entryName);
    }

    public Map<String, Object> getDictionary() {
        return dictionary;
    }
}
