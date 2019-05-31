package com.d2c.shop.b_api;

import com.baomidou.mybatisplus.extension.api.R;
import com.d2c.shop.b_api.base.B_BaseController;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.core.model.ShopDO;
import com.d2c.shop.modules.core.model.ShopkeeperDO;
import com.d2c.shop.modules.core.service.ShopService;
import com.d2c.shop.modules.order.model.PackageDO;
import com.d2c.shop.modules.order.query.PackageQuery;
import com.d2c.shop.modules.order.service.PackageService;
import com.d2c.shop.modules.product.model.ProductDO;
import com.d2c.shop.modules.product.model.ProductSkuDO;
import com.d2c.shop.modules.product.service.ProductService;
import com.d2c.shop.modules.product.service.ProductSkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Cai
 */
@Api(description = "选货盒业务")
@RestController
@RequestMapping("/b_api/package")
public class B_PackageController extends B_BaseController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductSkuService productSkuService;

    @ApiOperation(value = "数量")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public R count() {
        Long keeperId = loginKeeperHolder.getLoginKeeperId();
        PackageQuery query = new PackageQuery();
        query.setShopKeeperId(keeperId);
        int count = packageService.count(QueryUtil.buildWrapper(query));
        return Response.restResult(count, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R<List<PackageDO>> list(Long shopId) {
        PackageQuery query = new PackageQuery();
        query.setShopId(shopId);
        query.setShopKeeperId(loginKeeperHolder.getLoginKeeperId());
        List<PackageDO> list = (List<PackageDO>) packageService.list(QueryUtil.buildWrapper(query));
        List<Long> skuIds = new ArrayList<>();
        List<Long> shopIds = new ArrayList<>();
        Map<Long, PackageDO> map = new ConcurrentHashMap<>();
        for (PackageDO packageItem : list) {
            skuIds.add(packageItem.getProductSkuId());
            shopIds.add(packageItem.getShopId());
            map.put(packageItem.getProductSkuId(), packageItem);
        }
        if (skuIds.size() == 0) return Response.restResult(list, ResultCode.SUCCESS);
        List<ProductSkuDO> skuList = (List<ProductSkuDO>) productSkuService.listByIds(skuIds);
        List<ShopDO> shopList = (List<ShopDO>) shopService.listByIds(shopIds);
        Map<Long, ShopDO> map2 = new ConcurrentHashMap<>();
        for (ShopDO shop : shopList) {
            map2.put(shop.getId(), shop);
        }
        for (ProductSkuDO productSku : skuList) {
            if (map.get(productSku.getId()) != null) {
                PackageDO packageItem = map.get(productSku.getId());
                packageItem.setRealPrice(productSku.getSupplyPrice());
                packageItem.setStock(productSku.getStock());
                packageItem.setStatus(productSku.getStatus());
                if (map2.get(packageItem.getShopId()) != null) {
                    packageItem.setShop(map2.get(packageItem.getShopId()));
                }
            }
        }
        return Response.restResult(list, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "新增")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public R insert(Long skuId, Integer quantity) {
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.gt(quantity, 0, "数量必须大于0");
        ProductSkuDO sku = productSkuService.getById(skuId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, sku);
        PackageQuery query = new PackageQuery();
        query.setShopKeeperId(keeper.getId());
        query.setShopId(sku.getShopId());
        query.setProductSkuId(skuId);
        PackageDO packageItem = packageService.getOne(QueryUtil.buildWrapper(query));
        if (packageItem == null) {
            Asserts.ge(sku.getStock(), quantity, sku.getId() + "的SKU库存不足");
            ProductDO product = productService.getById(sku.getProductId());
            PackageDO entity = new PackageDO();
            entity.setShopId(sku.getShopId());
            entity.setShopKeeperId(keeper.getId());
            entity.setShopKeeperAccount(keeper.getAccount());
            entity.setProductId(sku.getProductId());
            entity.setProductSkuId(sku.getId());
            entity.setQuantity(quantity);
            entity.setStandard(sku.getStandard());
            entity.setProductName(product.getName());
            entity.setProductPic(product.getFirstPic());
            entity.setProductPrice(product.getPrice());
            entity.setBuyout(product.getBuyout());
            entity.setAllot(product.getAllot());
            packageService.save(entity);
        } else {
            Asserts.ge(sku.getStock(), packageItem.getQuantity() + quantity, sku.getId() + "的SKU库存不足");
            PackageDO entity = new PackageDO();
            entity.setQuantity(packageItem.getQuantity() + quantity);
            entity.setId(packageItem.getId());
            packageService.updateById(entity);
        }
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R update(Long id, Integer quantity) {
        Asserts.gt(quantity, 0, "数量必须大于0");
        PackageDO packageItem = packageService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, packageItem);
        Asserts.eq(packageItem.getShopKeeperId(), loginKeeperHolder.getLoginKeeperId(), "您不是本人");
        ProductSkuDO sku = productSkuService.getById(packageItem.getProductSkuId());
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, sku);
        Asserts.ge(sku.getStock(), quantity, sku.getId() + "的SKU库存不足");
        PackageDO entity = new PackageDO();
        entity.setId(packageItem.getId());
        entity.setQuantity(quantity);
        packageService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public R delete(Long[] ids) {
        PackageDO packageItem = packageService.getById(ids[0]);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, packageItem);
        Asserts.eq(packageItem.getShopKeeperId(), loginKeeperHolder.getLoginKeeperId(), "您不是本人");
        packageService.removeByIds(Arrays.asList(ids));
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}

