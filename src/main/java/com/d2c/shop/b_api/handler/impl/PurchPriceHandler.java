package com.d2c.shop.b_api.handler.impl;

import com.d2c.shop.b_api.handler.PurchHandler;
import com.d2c.shop.modules.order.model.PurchDO;
import com.d2c.shop.modules.order.model.PurchItemDO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Cai
 */
@Component
public class PurchPriceHandler implements PurchHandler {

    @Override
    public void operator(PurchDO purch, Object... conditions) {
        BigDecimal productAmount = BigDecimal.ZERO;
        for (PurchItemDO purchItem : purch.getPurchItemList()) {
            BigDecimal itemAmount = purchItem.getSupplyPrice().multiply(new BigDecimal(purchItem.getQuantity()));
            productAmount = productAmount.add(itemAmount);
            purchItem.setTotalAmount(itemAmount);
        }
        purch.setProductAmount(productAmount);
        purch.setPayAmount(productAmount);
    }

}
