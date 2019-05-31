package com.d2c.shop.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.d2c.shop.modules.order.model.AllotItemDO;

/**
 * @author BaiCai
 */
public interface AllotItemService extends IService<AllotItemDO> {

    Long doReceive(AllotItemDO allotItem);

    boolean doHandleDifference(AllotItemDO allotItem);

    boolean doCancelDifference(AllotItemDO allotItem);

}
