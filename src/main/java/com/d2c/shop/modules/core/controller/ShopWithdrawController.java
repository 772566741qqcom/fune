package com.d2c.shop.modules.core.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.api.base.BaseCtrl;
import com.d2c.shop.modules.core.model.ShopWithdrawDO;
import com.d2c.shop.modules.core.query.ShopWithdrawQuery;
import com.d2c.shop.modules.core.service.ShopWithdrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author BaiCai
 */
@Api(description = "店铺提现管理")
@RestController
@RequestMapping("/back/shop_withdraw")
public class ShopWithdrawController extends BaseCtrl<ShopWithdrawDO, ShopWithdrawQuery> {

    @Autowired
    private ShopWithdrawService shopWithdrawService;

    @ApiOperation(value = "同意提现")
    @RequestMapping(value = "/agree", method = RequestMethod.POST)
    public R agree(Long id, String payType, String paySn, Date payDate, String payAccount) {
        ShopWithdrawDO shopWithdraw = shopWithdrawService.getById(id);
        Asserts.notNull("提现单不能为空", shopWithdraw);
        shopWithdraw.setPayType(payType);
        shopWithdraw.setPaySn(paySn);
        shopWithdraw.setPayDate(payDate);
        shopWithdraw.setPayAccount(payAccount);
        shopWithdrawService.doSuccessWithdraw(shopWithdraw);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "拒绝提现")
    @RequestMapping(value = "/refuse", method = RequestMethod.POST)
    public R refuse(Long id) {
        ShopWithdrawDO shopWithdraw = shopWithdrawService.getById(id);
        Asserts.notNull("提现单不能为空", shopWithdraw);
        shopWithdrawService.doRefuseWithdraw(shopWithdraw);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}
