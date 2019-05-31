package com.d2c.shop.b_api;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.d2c.shop.b_api.base.B_BaseController;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.PageModel;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.core.model.ShopkeeperDO;
import com.d2c.shop.modules.product.model.FreightDO;
import com.d2c.shop.modules.product.query.FreightQuery;
import com.d2c.shop.modules.product.service.FreightService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Cai
 */
@Api(description = "运费业务")
@RestController
@RequestMapping("/b_api/freight")
public class B_FreightController extends B_BaseController {

    @Autowired
    private FreightService freightService;

    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R<Page<FreightDO>> list(PageModel page) {
        FreightQuery query = new FreightQuery();
        query.setShopId(loginKeeperHolder.getLoginShopId());
        Page<FreightDO> pager = (Page<FreightDO>) freightService.page(page, QueryUtil.buildWrapper(query));
        return Response.restResult(pager, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "根据ID查询")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R<FreightDO> select(@PathVariable Long id) {
        FreightDO freight = freightService.getById(id);
        Asserts.eq(freight.getShopId(), loginKeeperHolder.getLoginShopId(), "您不是本店店员");
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, freight);
        return Response.restResult(freight, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "新增")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public R<FreightDO> insert(@RequestBody FreightDO freight) {
        freight.setShopId(loginKeeperHolder.getLoginShopId());
        freightService.save(freight);
        return Response.restResult(freight, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R<FreightDO> update(@RequestBody FreightDO freight) {
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(freight.getShopId(), keeper.getShopId(), "您不是本店店员");
        freightService.updateById(freight);
        return Response.restResult(freightService.getById(freight.getId()), ResultCode.SUCCESS);
    }

}
