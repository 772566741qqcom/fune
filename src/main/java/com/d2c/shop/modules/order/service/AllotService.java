package com.d2c.shop.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.d2c.shop.modules.order.model.AllotDO;

/**
 * @author BaiCai
 */
public interface AllotService extends IService<AllotDO> {

    AllotDO doCreate(AllotDO allot);

    boolean doClose(AllotDO allot);

}
