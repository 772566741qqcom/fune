package com.d2c.shop.b_api.handler.impl;

import com.d2c.shop.b_api.handler.AllotHandler;
import com.d2c.shop.modules.order.model.AllotDO;
import com.d2c.shop.modules.order.model.AllotItemDO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Cai
 */
@Component
public class AllotPriceHandler implements AllotHandler {

    @Override
    public void operator(AllotDO allot, Object... conditions) {
        BigDecimal productAmount = BigDecimal.ZERO;
        for (AllotItemDO allotItem : allot.getAllotItemList()) {
            BigDecimal itemAmount = allotItem.getSupplyPrice().multiply(new BigDecimal(allotItem.getQuantity()));
            productAmount = productAmount.add(itemAmount);
            allotItem.setTotalAmount(itemAmount);
        }
        allot.setProductAmount(productAmount);
    }

}
