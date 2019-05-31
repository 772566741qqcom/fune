package com.d2c.shop.b_api;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.d2c.shop.b_api.base.B_BaseController;
import com.d2c.shop.b_api.handler.impl.AllotPriceHandler;
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
import com.d2c.shop.modules.order.model.*;
import com.d2c.shop.modules.order.query.AllotItemQuery;
import com.d2c.shop.modules.order.query.AllotQuery;
import com.d2c.shop.modules.order.query.PaymentQuery;
import com.d2c.shop.modules.order.query.SettlementQuery;
import com.d2c.shop.modules.order.service.*;
import com.d2c.shop.modules.product.model.ProductDO;
import com.d2c.shop.modules.product.model.ProductSkuDO;
import com.d2c.shop.modules.product.model.support.AllotSkuBean;
import com.d2c.shop.modules.product.service.AllotSkuService;
import com.d2c.shop.modules.product.service.ProductService;
import com.d2c.shop.modules.product.service.ProductSkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Cai
 */
@Api(description = "调拨单业务")
@RestController
@RequestMapping("/b_api/allot")
public class B_AllotController extends B_BaseController {

    @Autowired
    private AllotService allotService;
    @Autowired
    private AllotItemService allotItemService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private AllotSkuService allotSkuService;
    @Autowired
    private SettlementService settlementService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private AllotPriceHandler allotPriceHandler;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "免费拿货")
    @RequestMapping(value = "/settle", method = RequestMethod.POST)
    public R<AllotDO> settle(@RequestBody OrderRequestBeanB orderRequest) {
        // 参数校验
        List<Long> packageIds = orderRequest.getPackageIds();
        Long skuId = orderRequest.getSkuId();
        Integer quantity = orderRequest.getQuantity();
        if (packageIds == null && skuId == null && quantity == null) {
            throw new ApiException(ResultCode.REQUEST_PARAM_NULL);
        }
        // 登录用户
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        // 构建调拨单
        AllotDO allot = new AllotDO();
        allot.setShopKeeperId(keeper.getId());
        allot.setShopKeeperAccount(keeper.getAccount());
        allot.setStatus(AllotDO.StatusEnum.APPLY.name());
        allot.setProductAmount(BigDecimal.ZERO);
        // 收货方店铺
        ShopDO shop = shopService.getById(keeper.getShopId());
        Asserts.notNull("收货方店铺不能为空", shop);
        allot.setToShopId(shop.getId());
        allot.setToName(shop.getName());
        allot.setToMobile(shop.getTelephone());
        allot.setToAddress(shop.getAddress());
        // 构建调拨明细
        List<AllotItemDO> allotItemList = this.buildAllotItemList(packageIds, skuId, quantity, keeper);
        allot.setAllotItemList(allotItemList);
        // 发货方店铺
        ShopDO shop2 = shopService.getById(allotItemList.get(0).getFromShopId());
        Asserts.notNull("发货方店铺不能为空", shop2);
        allot.setFromShopId(shop2.getId());
        allot.setFromName(shop2.getName());
        allot.setFromMobile(shop2.getTelephone());
        allot.setFromAddress(shop2.getAddress());
        // 处理调拨单金额
        allotPriceHandler.operator(allot);
        Asserts.ge(shop.getDeposit(), allot.getProductAmount(), "店铺保证金余额不足");
        return Response.restResult(allot, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "创建调拨单")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public R<AllotDO> create(@RequestBody OrderRequestBeanB orderRequest) {
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
            if (!redisTemplate.opsForValue().setIfAbsent("C_CREATE_ALLOT::" + keeper.getAccount(), 1)) {
                throw new ApiException("您尚有正在处理中的调拨单，请勿重复操作");
            }
            // 构建调拨单
            Snowflake snowFlake = new Snowflake(1, 1);
            AllotDO allot = new AllotDO();
            allot.setShopKeeperId(keeper.getId());
            allot.setShopKeeperAccount(keeper.getAccount());
            allot.setSn(PrefixConstant.ALLOT_PREFIX + String.valueOf(snowFlake.nextId()));
            allot.setStatus(AllotDO.StatusEnum.APPLY.name());
            allot.setProductAmount(BigDecimal.ZERO);
            // 收货方店铺
            ShopDO shop = shopService.getById(keeper.getShopId());
            Asserts.notNull("收货方店铺不能为空", shop);
            allot.setToShopId(shop.getId());
            allot.setToName(shop.getName());
            allot.setToMobile(shop.getTelephone());
            allot.setToAddress(shop.getAddress());
            // 构建调拨明细
            List<AllotItemDO> allotItemList = this.buildAllotItemList(packageIds, skuId, quantity, keeper);
            allot.setAllotItemList(allotItemList);
            // 发货方店铺
            ShopDO shop2 = shopService.getById(allotItemList.get(0).getFromShopId());
            Asserts.notNull("发货方店铺不能为空", shop2);
            allot.setFromShopId(shop2.getId());
            allot.setFromName(shop2.getName());
            allot.setFromMobile(shop2.getTelephone());
            allot.setFromAddress(shop2.getAddress());
            // 处理调拨单金额
            allotPriceHandler.operator(allot);
            Asserts.ge(shop.getDeposit(), allot.getProductAmount(), "店铺保证金余额不足");
            // 创建调拨单
            allot = allotService.doCreate(allot);
            // 清空选货盒
            if (packageIds != null && packageIds.size() > 0) {
                packageService.removeByIds(packageIds);
            }
            return Response.restResult(allot, ResultCode.SUCCESS);
        } finally {
            redisTemplate.delete("C_CREATE_ALLOT::" + keeper.getAccount());
        }
    }

    // 构建调拨单明细
    private List<AllotItemDO> buildAllotItemList(List<Long> packageIds, Long skuId, Integer quantity, ShopkeeperDO keeper) {
        if (packageIds != null && packageIds.size() > 0) {
            // 从选货盒结算
            return this.buildPackageAllotItemList(packageIds, keeper);
        } else if (skuId != null && quantity != null) {
            // 从立即选货结算
            return this.buildBuyNowAllotItemList(skuId, quantity, keeper);
        } else {
            throw new ApiException(ResultCode.REQUEST_PARAM_NULL);
        }
    }

    // 从选货盒结算
    private List<AllotItemDO> buildPackageAllotItemList(List<Long> packageIds, ShopkeeperDO keeper) {
        List<AllotItemDO> allotItemList = new ArrayList<>();
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
                AllotItemDO allotItem = new AllotItemDO();
                allotItem.setStatus(AllotItemDO.StatusEnum.APPLY.name());
                allotItem.setTotalAmount(BigDecimal.ZERO);
                allotItem.setToShopId(keeper.getShopId());
                allotItem.setFromShopId(sku.getShopId());
                allotItem.setProductId(packageDO.getProductId());
                allotItem.setProductSkuId(packageDO.getProductSkuId());
                allotItem.setQuantity(packageDO.getQuantity());
                allotItem.setStandard(packageDO.getStandard());
                allotItem.setProductName(packageDO.getProductName());
                allotItem.setProductPic(packageDO.getProductPic());
                allotItem.setProductPrice(sku.getSellPrice());
                allotItem.setSupplyPrice(sku.getSupplyPrice());
                allotItemList.add(allotItem);
            }
        }
        return allotItemList;
    }

    // 从立即选货结算
    private List<AllotItemDO> buildBuyNowAllotItemList(Long skuId, Integer quantity, ShopkeeperDO keeper) {
        List<AllotItemDO> allotItemList = new ArrayList<>();
        Asserts.gt(quantity, 0, "数量必须大于0");
        ProductSkuDO sku = productSkuService.getById(skuId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, sku);
        Asserts.ge(sku.getStock(), quantity, sku.getId() + "的SKU库存不足");
        ProductDO product = productService.getById(sku.getProductId());
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, product);
        Asserts.gt(sku.getSupplyPrice(), BigDecimal.ZERO, "商品供货价信息不完整");
        AllotItemDO allotItem = new AllotItemDO();
        allotItem.setStatus(AllotItemDO.StatusEnum.APPLY.name());
        allotItem.setTotalAmount(BigDecimal.ZERO);
        allotItem.setToShopId(keeper.getShopId());
        allotItem.setFromShopId(sku.getShopId());
        allotItem.setProductId(sku.getProductId());
        allotItem.setProductSkuId(sku.getId());
        allotItem.setQuantity(quantity);
        allotItem.setStandard(sku.getStandard());
        allotItem.setProductName(product.getName());
        allotItem.setProductPic(product.getFirstPic());
        allotItem.setProductPrice(sku.getSellPrice());
        allotItem.setSupplyPrice(sku.getSupplyPrice());
        allotItemList.add(allotItem);
        return allotItemList;
    }

    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R<Page<AllotDO>> list(PageModel page, AllotQuery query) {
        query.setToShopId(loginKeeperHolder.getLoginShopId());
        Page<AllotDO> pager = (Page<AllotDO>) allotService.page(page, QueryUtil.buildWrapper(query));
        List<String> allotSns = new ArrayList<>();
        Map<String, AllotDO> allotMap = new ConcurrentHashMap<>();
        for (AllotDO allot : pager.getRecords()) {
            allotSns.add(allot.getSn());
            allotMap.put(allot.getSn(), allot);
        }
        if (allotSns.size() == 0) return Response.restResult(pager, ResultCode.SUCCESS);
        AllotItemQuery itemQuery = new AllotItemQuery();
        itemQuery.setAllotSn(allotSns.toArray(new String[0]));
        List<AllotItemDO> allotItemList = allotItemService.list(QueryUtil.buildWrapper(itemQuery));
        for (AllotItemDO allotItem : allotItemList) {
            if (allotMap.get(allotItem.getAllotSn()) != null) {
                allotMap.get(allotItem.getAllotSn()).getAllotItemList().add(allotItem);
            }
        }
        return Response.restResult(pager, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "根据ID查询")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R<AllotDO> select(@PathVariable Long id) {
        AllotDO allot = allotService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, allot);
        AllotItemQuery itemQuery = new AllotItemQuery();
        itemQuery.setAllotSn(new String[]{allot.getSn()});
        List<AllotItemDO> allotItemList = allotItemService.list(QueryUtil.buildWrapper(itemQuery));
        allot.getAllotItemList().addAll(allotItemList);
        return Response.restResult(allot, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "同意调拨")
    @RequestMapping(value = "/agree", method = RequestMethod.POST)
    public R agree(Long id) {
        AllotDO allot = allotService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, allot);
        Asserts.eq(allot.getStatus(), AllotDO.StatusEnum.APPLY.name(), "调拨单状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(allot.getFromShopId(), keeper.getShopId(), "您不是本店店员");
        AllotDO entity = new AllotDO();
        entity.setId(id);
        entity.setStatus(AllotDO.StatusEnum.AGREE.name());
        allotService.updateById(entity);
        AllotItemDO allotItem = new AllotItemDO();
        allotItem.setStatus(AllotItemDO.StatusEnum.AGREE.name());
        AllotItemQuery itemQuery = new AllotItemQuery();
        itemQuery.setAllotId(id);
        allotItemService.update(allotItem, QueryUtil.buildWrapper(itemQuery));
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "拒绝调拨")
    @RequestMapping(value = "/refuse", method = RequestMethod.POST)
    public R refuse(Long id) {
        AllotDO allot = allotService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, allot);
        Asserts.eq(allot.getStatus(), AllotDO.StatusEnum.APPLY.name(), "调拨单状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(allot.getFromShopId(), keeper.getShopId(), "您不是本店店员");
        allotService.doClose(allot);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "取消调拨")
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public R cancel(Long id) {
        AllotDO allot = allotService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, allot);
        Asserts.eq(allot.getStatus(), AllotDO.StatusEnum.APPLY.name(), "调拨单状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(allot.getToShopId(), keeper.getShopId(), "您不是本店店员");
        allotService.doClose(allot);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "货品结算")
    @RequestMapping(value = "/statement", method = RequestMethod.POST)
    public R<List<AllotSkuBean>> statement() {
        List<AllotSkuBean> list = allotSkuService.doListAllot(loginKeeperHolder.getLoginShopId());
        List<Long> shopIds = new ArrayList<>();
        List<Long> productIds = new ArrayList<>();
        for (AllotSkuBean bean : list) {
            shopIds.add(bean.getFromShopId());
            productIds.add(bean.getProductId());
        }
        if (shopIds.size() == 0 || productIds.size() == 0) {
            throw new ApiException("您目前没有需要结算的货品");
        }
        List<ShopDO> shops = (List<ShopDO>) shopService.listByIds(shopIds);
        List<ProductDO> products = (List<ProductDO>) productService.listByIds(productIds);
        Map<Long, ShopDO> shopMap = new HashMap<>();
        for (ShopDO item : shops) {
            shopMap.put(item.getId(), item);
        }
        Map<Long, ProductDO> productMap = new HashMap<>();
        for (ProductDO item : products) {
            productMap.put(item.getId(), item);
        }
        for (AllotSkuBean bean : list) {
            if (shopMap.get(bean.getShopId()) != null) {
                bean.setShop(shopMap.get(bean.getShopId()));
            }
            if (productMap.get(bean.getProductId()) != null) {
                bean.setProduct(productMap.get(bean.getProductId()));
            }
        }
        return Response.restResult(list, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "创建货品结算单")
    @RequestMapping(value = "/statement/create", method = RequestMethod.POST)
    public R<SettlementDO> createStatement(Long shopId) {
        Asserts.notNull(ResultCode.REQUEST_PARAM_NULL, shopId);
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        SettlementQuery query = new SettlementQuery();
        query.setShopId(keeper.getShopId());
        query.setStatus(SettlementDO.StatusEnum.WAIT_PAY.name());
        SettlementDO old = settlementService.getOne(QueryUtil.buildWrapper(query));
        if (old != null) {
            old.setGoods(null);
            Response.restResult(old, ResultCode.SUCCESS);
        }
        List<AllotSkuBean> list = allotSkuService.doSettleAllot(shopId, keeper.getShopId());
        JSONArray array = new JSONArray();
        BigDecimal payAmount = BigDecimal.ZERO;
        for (AllotSkuBean bean : list) {
            JSONObject obj = new JSONObject();
            obj.put("id", bean.getAllotSkuId());
            obj.put("count", bean.getSettleStock());
            array.add(obj);
            payAmount = payAmount.add(bean.getSupplyPrice().multiply(new BigDecimal(bean.getSettleStock())));
        }
        SettlementDO settlement = new SettlementDO();
        settlement.setFromShopId(shopId);
        settlement.setShopId(keeper.getShopId());
        settlement.setShopKeeperId(keeper.getId());
        settlement.setShopKeeperAccount(keeper.getAccount());
        Snowflake snowFlake = new Snowflake(1, 1);
        settlement.setSn(PrefixConstant.SETTLE_PREFIX + String.valueOf(snowFlake.nextId()));
        settlement.setStatus(SettlementDO.StatusEnum.WAIT_PAY.name());
        settlement.setGoods(array.toJSONString());
        settlement.setPayAmount(payAmount);
        settlement.setExpireDate(DateUtil.offsetMinute(new Date(), 120));
        settlementService.save(settlement);
        settlement.setGoods(null);
        return Response.restResult(settlement, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "支付货品结算单")
    @RequestMapping(value = "/statement/pay", method = RequestMethod.POST)
    public R payStatement(Long id, String paymentType) {
        SettlementDO settlement = settlementService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, settlement);
        PaymentDO payment = new PaymentDO();
        payment.setStatus(0);
        payment.setOrderSn(settlement.getSn());
        payment.setAmount(settlement.getPayAmount());
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
        query.setOrderSn(settlement.getSn());
        PaymentDO old = paymentService.getOne(QueryUtil.buildWrapper(query));
        if (old == null) {
            paymentService.save(payment);
        } else {
            payment.setId(old.getId());
            paymentService.updateById(payment);
        }
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}
