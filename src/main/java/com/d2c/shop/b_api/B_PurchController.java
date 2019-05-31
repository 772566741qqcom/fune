package com.d2c.shop.b_api;

import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.d2c.shop.b_api.base.B_BaseController;
import com.d2c.shop.b_api.handler.impl.PurchPriceHandler;
import com.d2c.shop.b_api.support.OrderRequestBeanB;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.PageModel;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.api.constant.PrefixConstant;
import com.d2c.shop.common.sdk.pay.alipay.AliPayConfig;
import com.d2c.shop.common.sdk.pay.wxpay.config.WxAppPayConfig;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.core.model.ShopDO;
import com.d2c.shop.modules.core.model.ShopkeeperDO;
import com.d2c.shop.modules.core.service.ShopService;
import com.d2c.shop.modules.order.model.PackageDO;
import com.d2c.shop.modules.order.model.PaymentDO;
import com.d2c.shop.modules.order.model.PurchDO;
import com.d2c.shop.modules.order.model.PurchItemDO;
import com.d2c.shop.modules.order.query.PaymentQuery;
import com.d2c.shop.modules.order.query.PurchItemQuery;
import com.d2c.shop.modules.order.query.PurchQuery;
import com.d2c.shop.modules.order.service.PackageService;
import com.d2c.shop.modules.order.service.PaymentService;
import com.d2c.shop.modules.order.service.PurchItemService;
import com.d2c.shop.modules.order.service.PurchService;
import com.d2c.shop.modules.product.model.ProductDO;
import com.d2c.shop.modules.product.model.ProductSkuDO;
import com.d2c.shop.modules.product.service.ProductService;
import com.d2c.shop.modules.product.service.ProductSkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Cai
 */
@Api(description = "采购单业务")
@RestController
@RequestMapping("/b_api/purch")
public class B_PurchController extends B_BaseController {

    @Autowired
    private PurchService purchService;
    @Autowired
    private PurchItemService purchItemService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private PurchPriceHandler purchPriceHandler;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "立即买断")
    @RequestMapping(value = "/settle", method = RequestMethod.POST)
    public R<PurchDO> settle(@RequestBody OrderRequestBeanB orderRequest) {
        // 参数校验
        List<Long> packageIds = orderRequest.getPackageIds();
        Long skuId = orderRequest.getSkuId();
        Integer quantity = orderRequest.getQuantity();
        if (packageIds == null && skuId == null && quantity == null) {
            throw new ApiException(ResultCode.REQUEST_PARAM_NULL);
        }
        // 登录用户
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        // 构建采购单
        PurchDO purch = new PurchDO();
        purch.setShopKeeperId(keeper.getId());
        purch.setShopKeeperAccount(keeper.getAccount());
        purch.setStatus(PurchDO.StatusEnum.WAIT_PAY.name());
        purch.setProductAmount(BigDecimal.ZERO);
        // 收货方店铺
        ShopDO shop = shopService.getById(keeper.getShopId());
        Asserts.notNull("收货方店铺不能为空", shop);
        purch.setToShopId(shop.getId());
        purch.setToName(shop.getName());
        purch.setToMobile(shop.getTelephone());
        purch.setToAddress(shop.getAddress());
        // 构建采购明细
        List<PurchItemDO> purchItemList = this.buildPurchItemList(packageIds, skuId, quantity, keeper);
        purch.setPurchItemList(purchItemList);
        // 发货方店铺
        ShopDO shop2 = shopService.getById(purchItemList.get(0).getFromShopId());
        Asserts.notNull("发货方店铺不能为空", shop2);
        purch.setFromShopId(shop2.getId());
        purch.setFromName(shop2.getName());
        purch.setFromMobile(shop2.getTelephone());
        purch.setFromAddress(shop2.getAddress());
        // 处理采购单金额
        purchPriceHandler.operator(purch);
        return Response.restResult(purch, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "创建采购单")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public R<PurchDO> create(@RequestBody OrderRequestBeanB orderRequest) {
        // 参数校验
        List<Long> packageIds = orderRequest.getPackageIds();
        Long skuId = orderRequest.getSkuId();
        Integer quantity = orderRequest.getQuantity();
        if (packageIds == null && skuId == null && quantity == null) {
            throw new ApiException(ResultCode.REQUEST_PARAM_NULL);
        }
        // 登录用户
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        // 防止重复提交
        try {
            if (!redisTemplate.opsForValue().setIfAbsent("C_CREATE_PURCH::" + keeper.getAccount(), 1)) {
                throw new ApiException("您尚有正在处理中的采购单，请勿重复操作");
            }
            // 构建采购单
            Snowflake snowFlake = new Snowflake(1, 1);
            PurchDO purch = new PurchDO();
            purch.setShopKeeperId(keeper.getId());
            purch.setShopKeeperAccount(keeper.getAccount());
            purch.setSn(PrefixConstant.PURCH_PREFIX + String.valueOf(snowFlake.nextId()));
            purch.setStatus(PurchDO.StatusEnum.WAIT_PAY.name());
            purch.setProductAmount(BigDecimal.ZERO);
            // 收货方店铺
            ShopDO shop = shopService.getById(keeper.getShopId());
            Asserts.notNull("收货方店铺不能为空", shop);
            purch.setToShopId(shop.getId());
            purch.setToName(shop.getName());
            purch.setToMobile(shop.getTelephone());
            purch.setToAddress(shop.getAddress());
            // 构建采购明细
            List<PurchItemDO> purchItemList = this.buildPurchItemList(packageIds, skuId, quantity, keeper);
            purch.setPurchItemList(purchItemList);
            // 发货方店铺
            ShopDO shop2 = shopService.getById(purchItemList.get(0).getFromShopId());
            Asserts.notNull("发货方店铺不能为空", shop2);
            purch.setFromShopId(shop2.getId());
            purch.setFromName(shop2.getName());
            purch.setFromMobile(shop2.getTelephone());
            purch.setFromAddress(shop2.getAddress());
            // 处理采购单金额
            purchPriceHandler.operator(purch);
            // 创建采购单
            purch = purchService.doCreate(purch);
            // 清空选货盒
            if (packageIds != null && packageIds.size() > 0) {
                packageService.removeByIds(packageIds);
            }
            return Response.restResult(purch, ResultCode.SUCCESS);
        } finally {
            redisTemplate.delete("C_CREATE_PURCH::" + keeper.getAccount());
        }
    }

    // 构建采购单明细
    private List<PurchItemDO> buildPurchItemList(List<Long> packageIds, Long skuId, Integer quantity, ShopkeeperDO keeper) {
        if (packageIds != null && packageIds.size() > 0) {
            // 从选货盒结算
            return this.buildPackagePurchItemList(packageIds, keeper);
        } else if (skuId != null && quantity != null) {
            // 从立即选货结算
            return this.buildBuyNowPurchItemList(skuId, quantity, keeper);
        } else {
            throw new ApiException(ResultCode.REQUEST_PARAM_NULL);
        }
    }

    // 从选货盒结算
    private List<PurchItemDO> buildPackagePurchItemList(List<Long> packageIds, ShopkeeperDO keeper) {
        List<PurchItemDO> purchItemList = new ArrayList<>();
        List<PackageDO> list = (List<PackageDO>) packageService.listByIds(packageIds);
        List<Long> skuIds = new ArrayList<>();
        Map<Long, PackageDO> map = new ConcurrentHashMap<>();
        for (PackageDO packageDO : list) {
            skuIds.add(packageDO.getProductSkuId());
            map.put(packageDO.getProductSkuId(), packageDO);
        }
        Asserts.gt(skuIds.size(), 0, "选货盒数据异常");
        List<ProductSkuDO> skuList = (List<ProductSkuDO>) productSkuService.listByIds(skuIds);
        for (ProductSkuDO sku : skuList) {
            if (map.get(sku.getId()) != null) {
                PackageDO packageDO = map.get(sku.getId());
                Asserts.ge(sku.getStock(), map.get(sku.getId()).getQuantity(), sku.getId() + "的SKU库存不足");
                Asserts.gt(sku.getSupplyPrice(), BigDecimal.ZERO, "商品供货价信息不完整");
                PurchItemDO purchItem = new PurchItemDO();
                purchItem.setStatus(PurchItemDO.StatusEnum.WAIT_PAY.name());
                purchItem.setTotalAmount(BigDecimal.ZERO);
                purchItem.setToShopId(keeper.getShopId());
                purchItem.setFromShopId(sku.getShopId());
                purchItem.setProductId(packageDO.getProductId());
                purchItem.setProductSkuId(packageDO.getProductSkuId());
                purchItem.setQuantity(packageDO.getQuantity());
                purchItem.setStandard(packageDO.getStandard());
                purchItem.setProductName(packageDO.getProductName());
                purchItem.setProductPic(packageDO.getProductPic());
                purchItem.setProductPrice(sku.getSellPrice());
                purchItem.setSupplyPrice(sku.getSupplyPrice());
                purchItemList.add(purchItem);
            }
        }
        return purchItemList;
    }

    // 从立即选货结算
    private List<PurchItemDO> buildBuyNowPurchItemList(Long skuId, Integer quantity, ShopkeeperDO keeper) {
        List<PurchItemDO> purchItemList = new ArrayList<>();
        Asserts.gt(quantity, 0, "数量必须大于0");
        ProductSkuDO sku = productSkuService.getById(skuId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, sku);
        Asserts.ge(sku.getStock(), quantity, sku.getId() + "的SKU库存不足");
        ProductDO product = productService.getById(sku.getProductId());
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, product);
        Asserts.gt(sku.getSupplyPrice(), BigDecimal.ZERO, "商品供货价信息不完整");
        PurchItemDO purchItem = new PurchItemDO();
        purchItem.setStatus(PurchItemDO.StatusEnum.WAIT_PAY.name());
        purchItem.setTotalAmount(BigDecimal.ZERO);
        purchItem.setToShopId(keeper.getShopId());
        purchItem.setFromShopId(sku.getShopId());
        purchItem.setProductId(sku.getProductId());
        purchItem.setProductSkuId(sku.getId());
        purchItem.setQuantity(quantity);
        purchItem.setStandard(sku.getStandard());
        purchItem.setProductName(product.getName());
        purchItem.setProductPic(product.getFirstPic());
        purchItem.setProductPrice(sku.getSellPrice());
        purchItem.setSupplyPrice(sku.getSupplyPrice());
        purchItemList.add(purchItem);
        return purchItemList;
    }

    @ApiOperation(value = "支付订单")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public R pay(Long id, String paymentType) {
        PurchDO purch = purchService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, purch);
        PaymentDO payment = new PaymentDO();
        payment.setStatus(0);
        payment.setOrderSn(purch.getSn());
        payment.setAmount(purch.getProductAmount());
        PaymentDO.PaymentTypeEnum paymentTypeEnum = PaymentDO.PaymentTypeEnum.valueOf(paymentType);
        switch (paymentTypeEnum) {
            case WX_PAY:
                payment.setPaymentType(PaymentDO.PaymentTypeEnum.WX_PAY.name());
                payment.setTradeType("APP");
                payment.setPrepayId("0");
                payment.setAppId(WxAppPayConfig.APP_ID);
                payment.setMchId(WxAppPayConfig.MCH_ID);
                break;
            case ALI_PAY:
                payment.setPaymentType(PaymentDO.PaymentTypeEnum.ALI_PAY.name());
                payment.setTradeType("QUICK_MSECURITY_PAY");
                payment.setPrepayId("0");
                payment.setAppId(AliPayConfig.APP_ID2);
                payment.setMchId("0");
                break;
            default:
                return Response.restResult(null, ResultCode.REQUEST_PARAM_NULL);
        }
        PaymentQuery query = new PaymentQuery();
        query.setOrderSn(purch.getSn());
        PaymentDO old = paymentService.getOne(QueryUtil.buildWrapper(query));
        if (old == null) {
            paymentService.save(payment);
        } else {
            payment.setId(old.getId());
            paymentService.updateById(payment);
        }
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R<Page<PurchDO>> list(PageModel page, PurchQuery query) {
        query.setToShopId(loginKeeperHolder.getLoginShopId());
        Page<PurchDO> pager = (Page<PurchDO>) purchService.page(page, QueryUtil.buildWrapper(query));
        List<String> purchSns = new ArrayList<>();
        Map<String, PurchDO> purchMap = new ConcurrentHashMap<>();
        for (PurchDO purch : pager.getRecords()) {
            purchSns.add(purch.getSn());
            purchMap.put(purch.getSn(), purch);
        }
        if (purchSns.size() == 0) return Response.restResult(pager, ResultCode.SUCCESS);
        PurchItemQuery itemQuery = new PurchItemQuery();
        itemQuery.setPurchSn(purchSns.toArray(new String[0]));
        List<PurchItemDO> purchItemList = purchItemService.list(QueryUtil.buildWrapper(itemQuery));
        for (PurchItemDO purchItem : purchItemList) {
            if (purchMap.get(purchItem.getPurchSn()) != null) {
                purchMap.get(purchItem.getPurchSn()).getPurchItemList().add(purchItem);
            }
        }
        return Response.restResult(pager, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "根据ID查询")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R<PurchDO> select(@PathVariable Long id) {
        PurchDO purch = purchService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, purch);
        PurchItemQuery itemQuery = new PurchItemQuery();
        itemQuery.setPurchSn(new String[]{purch.getSn()});
        List<PurchItemDO> purchItemList = purchItemService.list(QueryUtil.buildWrapper(itemQuery));
        purch.getPurchItemList().addAll(purchItemList);
        return Response.restResult(purch, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "取消采购")
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public R cancel(Long id) {
        PurchDO purch = purchService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, purch);
        Asserts.eq(purch.getStatus(), PurchDO.StatusEnum.WAIT_PAY.name(), "采购单状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(purch.getToShopId(), keeper.getShopId(), "您不是本店店员");
        purchService.doClose(purch);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}
