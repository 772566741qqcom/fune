package com.d2c.shop.modules.core.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.api.base.extension.BaseExcelCtrl;
import com.d2c.shop.modules.core.model.ShopDO;
import com.d2c.shop.modules.core.query.ShopQuery;
import com.d2c.shop.modules.core.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author BaiCai
 */
@Api(description = "店铺管理")
@RestController
@RequestMapping("/back/shop")
public class ShopController extends BaseExcelCtrl<ShopDO, ShopQuery> {

    @Autowired
    private ShopService shopService;

    @ApiOperation(value = "保证金充值")
    @RequestMapping(value = "/update/deposit", method = RequestMethod.POST)
    public R deposit(Long id, BigDecimal deposit, BigDecimal recharge) {
        Asserts.gt(deposit, BigDecimal.ZERO, "保证金金额不能小于等于0");
        Asserts.gt(recharge, BigDecimal.ZERO, "实际充值金额不能小于等于0");
        shopService.updateDeposit(id, deposit, recharge);
        return Response.restResult(service.getById(id), ResultCode.SUCCESS);
    }

}
