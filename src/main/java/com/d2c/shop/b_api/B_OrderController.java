package com.d2c.shop.b_api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.d2c.shop.b_api.base.B_BaseController;
import com.d2c.shop.b_api.support.OrderRequestBeanB;
import com.d2c.shop.c_api.handler.impl.OrderPromotionHandler;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.PageModel;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.api.constant.PrefixConstant;
import com.d2c.shop.common.sdk.pay.alipay.AliPayConfig;
import com.d2c.shop.common.sdk.pay.wxpay.config.WxAppPayConfig;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.common.utils.ReflectUtil;
import com.d2c.shop.modules.core.model.ShopDO;
import com.d2c.shop.modules.core.model.ShopkeeperDO;
import com.d2c.shop.modules.core.service.ShopService;
import com.d2c.shop.modules.member.model.DraweeDO;
import com.d2c.shop.modules.member.service.DraweeService;
import com.d2c.shop.modules.order.model.*;
import com.d2c.shop.modules.order.query.OrderItemQuery;
import com.d2c.shop.modules.order.query.OrderQuery;
import com.d2c.shop.modules.order.query.PaymentQuery;
import com.d2c.shop.modules.order.service.*;
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
@Api(description = "订单业务")
@RestController
@RequestMapping("/b_api/order")
public class B_OrderController extends B_BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private DraweeService draweeService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private CrowdGroupService crowdGroupService;
    @Autowired
    private OrderPromotionHandler orderPromotionHandler;
    @Autowired
    private WxAppPayConfig wxAppPayConfig;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "立即结算")
    @RequestMapping(value = "/settle", method = RequestMethod.POST)
    public R<OrderDO> settle(@RequestBody OrderRequestBeanB orderRequest) {
        // 参数校验
        List<Long> packageIds = orderRequest.getPackageIds();
        Long skuId = orderRequest.getSkuId();
        Integer quantity = orderRequest.getQuantity();
        if (packageIds == null && skuId == null && quantity == null) {
            throw new ApiException(ResultCode.REQUEST_PARAM_NULL);
        }
        // 登录用户
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        // 构建订单
        OrderDO order = new OrderDO();
        order.setMemberId(keeper.getId());
        order.setMemberAccount(keeper.getAccount());
        order.setProductAmount(BigDecimal.ZERO);
        order.setCouponAmount(BigDecimal.ZERO);
        order.setPayAmount(BigDecimal.ZERO);
        // 构建订单明细
        List<OrderItemDO> orderItemList = this.buildOrderItemList(packageIds, skuId, quantity, keeper);
        order.setOrderItemList(orderItemList);
        // 按照顺序，处理订单促销
        orderPromotionHandler.operator(order);
        return Response.restResult(order, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "创建订单")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public R<OrderDO> create(@RequestBody OrderRequestBeanB orderRequest) {
        // 参数校验
        List<Long> packageIds = orderRequest.getPackageIds();
        Long skuId = orderRequest.getSkuId();
        Integer quantity = orderRequest.getQuantity();
        Long draweeId = orderRequest.getDraweeId();
        if (packageIds == null && skuId == null && quantity == null) {
            throw new ApiException(ResultCode.REQUEST_PARAM_NULL);
        }
        // 登录用户
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        // 防止重复提交
        try {
            if (!redisTemplate.opsForValue().setIfAbsent("C_CREATE_ORDER::" + keeper.getAccount(), 1)) {
                throw new ApiException("您尚有正在处理中的订单，请勿重复操作");
            }
            // 付款人信息
            DraweeDO drawee = draweeService.getById(draweeId);
            Asserts.notNull("付款人信息不能为空", drawee);
            // 构建订单
            Snowflake snowFlake = new Snowflake(1, 1);
            OrderDO order = new OrderDO();
            BeanUtil.copyProperties(drawee, order, ReflectUtil.clearPublicFields());
            order.setOffline(1);
            order.setMemberId(keeper.getId());
            order.setMemberAccount(keeper.getAccount());
            order.setSn(PrefixConstant.ORDER_PREFIX + String.valueOf(snowFlake.nextId()));
            order.setType(OrderDO.TypeEnum.NORMAL.name());
            order.setStatus(OrderDO.StatusEnum.WAIT_PAY.name());
            order.setProductAmount(BigDecimal.ZERO);
            order.setCouponAmount(BigDecimal.ZERO);
            order.setPayAmount(BigDecimal.ZERO);
            // 构建订单明细
            List<OrderItemDO> orderItemList = this.buildOrderItemList(packageIds, skuId, quantity, keeper);
            order.setOrderItemList(orderItemList);
            // 下单店铺
            ShopDO shop = shopService.getById(keeper.getShopId());
            Asserts.notNull("店铺不能为空", shop);
            order.setShopId(shop.getId());
            order.setShopName(shop.getName());
            // 按照顺序，处理订单促销
            orderPromotionHandler.operator(order);
            // 创建订单
            order = orderService.doCreate(order);
            // 清空购物车
            if (packageIds != null && packageIds.size() > 0) {
                packageService.removeByIds(packageIds);
            }
            return Response.restResult(order, ResultCode.SUCCESS);
        } finally {
            redisTemplate.delete("C_CREATE_ORDER::" + keeper.getAccount());
        }
    }

    // 构建订单明细
    private List<OrderItemDO> buildOrderItemList(List<Long> cartIds, Long skuId, Integer quantity, ShopkeeperDO keeper) {
        if (cartIds != null && cartIds.size() > 0) {
            // 从选货盒结算
            return this.buildPackageOrderItems(cartIds, keeper);
        } else if (skuId != null && quantity != null) {
            // 从立即选货结算
            return this.buildBuyNowOrderItems(skuId, quantity, keeper);
        } else {
            throw new ApiException(ResultCode.REQUEST_PARAM_NULL);
        }
    }

    // 从选货盒结算
    private List<OrderItemDO> buildPackageOrderItems(List<Long> packageIds, ShopkeeperDO keeper) {
        List<OrderItemDO> orderItemList = new ArrayList<>();
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
                Asserts.ge(sku.getStock(), map.get(sku.getId()).getQuantity(), sku.getId() + "的SKU库存不足");
                OrderItemDO orderItem = this.initOrderItem(map, keeper, sku);
                this.buildOrderItem(sku, orderItem);
                orderItemList.add(orderItem);
            }
        }
        return orderItemList;
    }

    // 从立即选货结算
    private List<OrderItemDO> buildBuyNowOrderItems(Long skuId, Integer quantity, ShopkeeperDO keeper) {
        List<OrderItemDO> orderItemList = new ArrayList<>();
        Asserts.gt(quantity, 0, "数量必须大于0");
        ProductSkuDO sku = productSkuService.getById(skuId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, sku);
        Asserts.ge(sku.getStock(), quantity, sku.getId() + "的SKU库存不足");
        ProductDO product = productService.getById(sku.getProductId());
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, product);
        OrderItemDO orderItem = this.initOrderItem(quantity, keeper, sku, product);
        this.buildOrderItem(sku, orderItem);
        orderItemList.add(orderItem);
        return orderItemList;
    }

    private OrderItemDO initOrderItem(Map<Long, PackageDO> map, ShopkeeperDO keeper, ProductSkuDO sku) {
        OrderItemDO orderItem = new OrderItemDO();
        PackageDO packageDO = map.get(sku.getId());
        BeanUtil.copyProperties(packageDO, orderItem, ReflectUtil.clearPublicFields());
        orderItem.setShopId(sku.getShopId());
        orderItem.setMemberId(keeper.getId());
        orderItem.setMemberAccount(keeper.getAccount());
        return orderItem;
    }

    private OrderItemDO initOrderItem(Integer quantity, ShopkeeperDO keeper, ProductSkuDO sku, ProductDO product) {
        OrderItemDO orderItem = new OrderItemDO();
        orderItem.setShopId(sku.getShopId());
        orderItem.setMemberId(keeper.getId());
        orderItem.setMemberAccount(keeper.getAccount());
        orderItem.setProductId(sku.getProductId());
        orderItem.setProductSkuId(sku.getId());
        orderItem.setQuantity(quantity);
        orderItem.setStandard(sku.getStandard());
        orderItem.setProductName(product.getName());
        orderItem.setProductPic(product.getFirstPic());
        return orderItem;
    }

    private void buildOrderItem(ProductSkuDO sku, OrderItemDO orderItem) {
        orderItem.setType(OrderDO.TypeEnum.NORMAL.name());
        orderItem.setStatus(OrderItemDO.StatusEnum.WAIT_PAY.name());
        orderItem.setVirtual(sku.getVirtual());
        orderItem.setProductPrice(sku.getSellPrice());
        orderItem.setRealPrice(sku.getSellPrice());
        orderItem.setPayAmount(BigDecimal.ZERO);
        orderItem.setCouponWeightingAmount(BigDecimal.ZERO);
    }

    @ApiOperation(value = "支付订单")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public R pay(Long id, String paymentType) {
        OrderDO order = orderService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, order);
        PaymentDO payment = new PaymentDO();
        payment.setStatus(0);
        payment.setOrderSn(order.getSn());
        payment.setAmount(order.getPayAmount());
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
        query.setOrderSn(order.getSn());
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
    public R<Page<OrderDO>> list(PageModel page, OrderQuery query) {
        query.setShopId(loginKeeperHolder.getLoginShopId());
        Page<OrderDO> pager = (Page<OrderDO>) orderService.page(page, QueryUtil.buildWrapper(query));
        List<String> orderSns = new ArrayList<>();
        Map<String, OrderDO> orderMap = new ConcurrentHashMap<>();
        for (OrderDO order : pager.getRecords()) {
            orderSns.add(order.getSn());
            orderMap.put(order.getSn(), order);
        }
        if (orderSns.size() == 0) return Response.restResult(pager, ResultCode.SUCCESS);
        OrderItemQuery itemQuery = new OrderItemQuery();
        itemQuery.setOrderSn(orderSns.toArray(new String[0]));
        List<OrderItemDO> orderItemList = orderItemService.list(QueryUtil.buildWrapper(itemQuery));
        for (OrderItemDO orderItem : orderItemList) {
            if (orderMap.get(orderItem.getOrderSn()) != null) {
                orderMap.get(orderItem.getOrderSn()).getOrderItemList().add(orderItem);
            }
        }
        return Response.restResult(pager, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "根据ID查询")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R<OrderDO> select(@PathVariable Long id) {
        OrderDO order = orderService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, order);
        OrderItemQuery itemQuery = new OrderItemQuery();
        itemQuery.setOrderSn(new String[]{order.getSn()});
        List<OrderItemDO> orderItemList = orderItemService.list(QueryUtil.buildWrapper(itemQuery));
        order.getOrderItemList().addAll(orderItemList);
        if (order.getType().equals(OrderDO.TypeEnum.CROWD.name())) {
            CrowdGroupDO crowdGroup = crowdGroupService.getById(order.getCrowdId());
            order.setCrowdGroup(crowdGroup);
        }
        return Response.restResult(order, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "修改收货地址")
    @RequestMapping(value = "/update/address", method = RequestMethod.POST)
    public R updateAddress(Long orderId, String province, String city, String district, String address, String name, String mobile) {
        OrderDO order = orderService.getById(orderId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, order);
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(order.getShopId(), keeper.getShopId(), "您不是本店店员");
        OrderDO entity = new OrderDO();
        entity.setId(orderId);
        entity.setProvince(province);
        entity.setCity(city);
        entity.setDistrict(district);
        entity.setAddress(address);
        entity.setName(name);
        entity.setMobile(mobile);
        orderService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}
