package com.d2c.shop.modules.core.controller;

import com.d2c.shop.common.api.base.BaseCtrl;
import com.d2c.shop.modules.core.model.ShopDepositDO;
import com.d2c.shop.modules.core.query.ShopDepositQuery;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BaiCai
 */
@Api(description = "店铺保证金管理")
@RestController
@RequestMapping("/back/shop_deposit")
public class ShopDepositController extends BaseCtrl<ShopDepositDO, ShopDepositQuery> {

}
