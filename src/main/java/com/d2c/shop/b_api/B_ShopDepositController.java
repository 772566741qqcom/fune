package com.d2c.shop.b_api;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.d2c.shop.b_api.base.B_BaseController;
import com.d2c.shop.common.api.PageModel;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.core.model.ShopDepositDO;
import com.d2c.shop.modules.core.query.ShopDepositQuery;
import com.d2c.shop.modules.core.service.ShopDepositService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cai
 */
@Api(description = "店铺保证金业务")
@RestController
@RequestMapping("/b_api/shop_deposit")
public class B_ShopDepositController extends B_BaseController {

    @Autowired
    private ShopDepositService shopDepositService;

    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R<Page<ShopDepositDO>> list(PageModel page, ShopDepositQuery query) {
        query.setShopId(loginKeeperHolder.getLoginShopId());
        Page<ShopDepositDO> pager = (Page<ShopDepositDO>) shopDepositService.page(page, QueryUtil.buildWrapper(query));
        return Response.restResult(pager, ResultCode.SUCCESS);
    }

}
