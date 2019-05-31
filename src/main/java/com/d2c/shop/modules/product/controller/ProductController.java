package com.d2c.shop.modules.product.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.api.base.BaseCtrl;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.product.model.*;
import com.d2c.shop.modules.product.query.ProductQuery;
import com.d2c.shop.modules.product.query.ProductSkuQuery;
import com.d2c.shop.modules.product.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author BaiCai
 */
@Api(description = "商品管理")
@RestController
@RequestMapping("/back/product")
public class ProductController extends BaseCtrl<ProductDO, ProductQuery> {

    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductClassifyService productClassifyService;

    @ApiOperation(value = "新增数据")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public R<ProductDO> insert(@RequestBody ProductDO entity) {
        Asserts.notNull(ResultCode.REQUEST_PARAM_NULL, entity);
        productService.doCreate(entity);
        return Response.restResult(entity, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "通过ID获取数据")
    @RequestMapping(value = "/select/{id}", method = RequestMethod.GET)
    public R<ProductDO> select(@PathVariable Long id) {
        ProductDO product = service.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, product);
        ProductSkuQuery query = new ProductSkuQuery();
        query.setProductId(product.getId());
        List<ProductSkuDO> skuList = productSkuService.list(QueryUtil.buildWrapper(query));
        product.setSkuList(skuList);
        BrandDO brand = brandService.getById(product.getBrandId());
        product.setBrand(brand);
        ProductCategoryDO category2 = productCategoryService.getById(product.getCategoryId());
        ProductCategoryDO category1 = productCategoryService.getById(category2.getParentId());
        if (category1 != null) {
            category1.getChildren().add(category2);
            product.setCategory(category1);
        } else {
            product.setCategory(category2);
        }
        ProductClassifyDO classify2 = productClassifyService.getById(product.getClassifyId());
        ProductClassifyDO classify1 = productClassifyService.getById(category2.getParentId());
        if (classify1 != null) {
            classify1.getChildren().add(classify2);
            product.setClassify(classify1);
        } else {
            product.setClassify(classify2);
        }
        return Response.restResult(product, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "通过ID更新数据")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R<ProductDO> update(@RequestBody ProductDO entity) {
        Asserts.notNull(ResultCode.REQUEST_PARAM_NULL, entity);
        if (entity.getSourceId() != null && entity.getCrowd() == 1) {
            throw new ApiException("拼团不能设置调拨类型的商品");
        }
        productService.doUpdate(entity);
        return Response.restResult(service.getById(entity.getId()), ResultCode.SUCCESS);
    }

    @ApiOperation(value = "更改状态")
    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public R status(Long id, Integer status) {
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, id, status);
        ProductDO product = productService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, product);
        ProductDO entity = new ProductDO();
        entity.setId(id);
        entity.setStatus(status);
        productService.updateById(entity);
        ProductSkuQuery query = new ProductSkuQuery();
        query.setProductId(id);
        ProductSkuDO sku = new ProductSkuDO();
        sku.setStatus(status);
        productSkuService.update(sku, QueryUtil.buildWrapper(query));
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "更改拼团")
    @RequestMapping(value = "/crowd", method = RequestMethod.POST)
    public R crowd(Long id, Integer crowd) {
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, id, crowd);
        ProductDO product = productService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, product);
        ProductDO entity = new ProductDO();
        entity.setId(id);
        entity.setCrowd(crowd);
        productService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}
