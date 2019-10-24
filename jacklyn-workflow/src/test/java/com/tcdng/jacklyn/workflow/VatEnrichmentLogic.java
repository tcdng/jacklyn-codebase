/*
 * Copyright 2018-2019 The Code Department.
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

import com.tcdng.jacklyn.workflow.business.AbstractWfItemEnrichmentLogic;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem.ReaderWriter;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Test VAT enrichment logic.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("vat-enrichmentlogic")
public class VatEnrichmentLogic extends AbstractWfItemEnrichmentLogic {

    @Override
    public void enrich(ReaderWriter rw) throws UnifyException {
    	BigDecimal unitPrice = rw.read(BigDecimal.class, "unitPrice");
    	Integer quantity =  rw.read(Integer.class, "quantity");
    	
    	BigDecimal amount = unitPrice.multiply(BigDecimal.valueOf(quantity.longValue()).setScale(2));
    	BigDecimal vat = amount.multiply(BigDecimal.valueOf(0.05)).setScale(2);
    	BigDecimal totalAmount = amount.add(vat).setScale(2);
    	
        rw.write("amount", amount);
        rw.write("vat", vat);
        rw.write("totalAmount", totalAmount);
    }

}
