package com.d2c.shop.b_api.handler;

import com.d2c.shop.modules.order.model.AllotDO;

/**
 * @author Cai
 */
public interface AllotHandler {

    void operator(AllotDO allot, Object... conditions);

}
