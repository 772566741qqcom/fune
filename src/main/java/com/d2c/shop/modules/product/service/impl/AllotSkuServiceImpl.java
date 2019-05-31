package com.d2c.shop.modules.product.service.impl;

import com.d2c.shop.common.api.base.BaseService;
import com.d2c.shop.modules.product.mapper.AllotSkuMapper;
import com.d2c.shop.modules.product.model.AllotSkuDO;
import com.d2c.shop.modules.product.model.support.AllotSkuBean;
import com.d2c.shop.modules.product.service.AllotSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author BaiCai
 */
@Service
public class AllotSkuServiceImpl extends BaseService<AllotSkuMapper, AllotSkuDO> implements AllotSkuService {

    @Autowired
    private AllotSkuMapper allotSkuMapper;

    @Override
    public int doDeductStock(Long id, Integer quantity) {
        return allotSkuMapper.doDeductStock(id, quantity);
    }

    @Override
    public List<AllotSkuBean> doListAllot(Long toShopId) {
        return allotSkuMapper.doListAllot(toShopId);
    }

    @Override
    public List<AllotSkuBean> doSettleAllot(Long fromShopId, Long toShopId) {
        return allotSkuMapper.doSettleAllot(fromShopId, toShopId);
    }

}
