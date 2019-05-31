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
import com.d2c.shop.modules.product.model.BrandDO;
import com.d2c.shop.modules.product.query.BrandQuery;
import com.d2c.shop.modules.product.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Cai
 */
@Api(description = "品牌业务")
@RestController
@RequestMapping("/b_api/brand")
public class B_BrandController extends B_BaseController {

    @Autowired
    private BrandService brandService;

    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R<Page<BrandDO>> list(PageModel page) {
        BrandQuery query = new BrandQuery();
        query.setShopId(loginKeeperHolder.getLoginShopId());
        Page<BrandDO> pager = (Page<BrandDO>) brandService.page(page, QueryUtil.buildWrapper(query));
        return Response.restResult(pager, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "根据ID查询")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R<BrandDO> select(@PathVariable Long id) {
        BrandDO brand = brandService.getById(id);
        Asserts.eq(brand.getShopId(), loginKeeperHolder.getLoginShopId(), "您不是本店店员");
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, brand);
        return Response.restResult(brand, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "新增")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public R<BrandDO> insert(@RequestBody BrandDO brand) {
        brand.setShopId(loginKeeperHolder.getLoginShopId());
        brandService.save(brand);
        return Response.restResult(brand, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R<BrandDO> update(@RequestBody BrandDO brand) {
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(brand.getShopId(), keeper.getShopId(), "您不是本店店员");
        brandService.updateById(brand);
        return Response.restResult(brandService.getById(brand.getId()), ResultCode.SUCCESS);
    }

}
