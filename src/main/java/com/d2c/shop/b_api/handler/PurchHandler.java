package com.d2c.shop.b_api.handler;

import com.d2c.shop.modules.order.model.PurchDO;

/**
 * @author Cai
 */
public interface PurchHandler {

    void operator(PurchDO purch, Object... conditions);

}
