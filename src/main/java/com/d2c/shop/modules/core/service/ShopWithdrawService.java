package com.d2c.shop.modules.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.d2c.shop.modules.core.model.ShopWithdrawDO;

/**
 * @author BaiCai
 */
public interface ShopWithdrawService extends IService<ShopWithdrawDO> {

    int doApplyWithdraw(ShopWithdrawDO shopWithdraw);

    int doRefuseWithdraw(ShopWithdrawDO shopWithdraw);

    int doSuccessWithdraw(ShopWithdrawDO shopWithdraw);

}
