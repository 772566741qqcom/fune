package com.d2c.shop.modules.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.d2c.shop.modules.product.model.AllotSkuDO;
import com.d2c.shop.modules.product.model.support.AllotSkuBean;

import java.util.List;

/**
 * @author BaiCai
 */
public interface AllotSkuService extends IService<AllotSkuDO> {

    int doDeductStock(Long id, Integer quantity);

    List<AllotSkuBean> doListAllot(Long toShopId);

    List<AllotSkuBean> doSettleAllot(Long fromShopId, Long toShopId);

}
