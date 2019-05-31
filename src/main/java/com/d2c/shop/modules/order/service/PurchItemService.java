package com.d2c.shop.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.d2c.shop.modules.order.model.PurchItemDO;

/**
 * @author BaiCai
 */
public interface PurchItemService extends IService<PurchItemDO> {

    Long doReceive(PurchItemDO purchItem);

    boolean doHandleDifference(PurchItemDO purchItem);

    boolean doCancelDifference(PurchItemDO purchItem);

}
