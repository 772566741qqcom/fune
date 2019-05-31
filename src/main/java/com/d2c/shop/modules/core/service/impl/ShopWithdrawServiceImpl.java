package com.d2c.shop.modules.core.service.impl;

import com.d2c.shop.common.api.base.BaseService;
import com.d2c.shop.modules.core.mapper.ShopWithdrawMapper;
import com.d2c.shop.modules.core.model.ShopFlowDO;
import com.d2c.shop.modules.core.model.ShopWithdrawDO;
import com.d2c.shop.modules.core.service.ShopFlowService;
import com.d2c.shop.modules.core.service.ShopService;
import com.d2c.shop.modules.core.service.ShopWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author BaiCai
 */
@Service
public class ShopWithdrawServiceImpl extends BaseService<ShopWithdrawMapper, ShopWithdrawDO> implements ShopWithdrawService {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopFlowService shopFlowService;

    @Override
    @Transactional
    public int doApplyWithdraw(ShopWithdrawDO shopWithdraw) {
        this.save(shopWithdraw);
        return shopService.updateBalance(shopWithdraw.getShopId(), shopWithdraw.getAmount().negate(), shopWithdraw.getAmount());
    }

    @Override
    @Transactional
    public int doRefuseWithdraw(ShopWithdrawDO shopWithdraw) {
        ShopWithdrawDO entity = new ShopWithdrawDO();
        entity.setId(shopWithdraw.getId());
        entity.setStatus(ShopWithdrawDO.StatusEnum.CLOSE.name());
        this.updateById(entity);
        return shopService.updateBalance(shopWithdraw.getShopId(), shopWithdraw.getAmount(), shopWithdraw.getAmount().negate());
    }

    @Override
    @Transactional
    public int doSuccessWithdraw(ShopWithdrawDO shopWithdraw) {
        ShopFlowDO sf = new ShopFlowDO();
        sf.setStatus(1);
        sf.setType(ShopFlowDO.TypeEnum.WITHDRAW.name());
        sf.setShopId(shopWithdraw.getShopId());
        sf.setOrderSn(String.valueOf(shopWithdraw.getId()));
        sf.setPaymentType(shopWithdraw.getPayType());
        sf.setPaymentSn(shopWithdraw.getPaySn());
        sf.setAmount(shopWithdraw.getAmount().negate());
        shopWithdraw.setStatus(ShopWithdrawDO.StatusEnum.SUCCESS.name());
        this.updateById(shopWithdraw);
        return shopFlowService.doFlowing(sf, BigDecimal.ZERO, shopWithdraw.getAmount().negate());
    }

}
