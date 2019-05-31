package com.d2c.shop.b_api;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.d2c.shop.b_api.base.B_BaseController;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.PageModel;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.api.constant.PrefixConstant;
import com.d2c.shop.common.sdk.pay.alipay.AliPayConfig;
import com.d2c.shop.common.sdk.pay.wxpay.WXPay;
import com.d2c.shop.common.sdk.pay.wxpay.config.WxJsPayConfig;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.core.model.ShopkeeperDO;
import com.d2c.shop.modules.order.model.OrderDO;
import com.d2c.shop.modules.order.model.OrderItemDO;
import com.d2c.shop.modules.order.model.PaymentDO;
import com.d2c.shop.modules.order.query.OrderItemQuery;
import com.d2c.shop.modules.order.query.OrderQuery;
import com.d2c.shop.modules.order.query.PaymentQuery;
import com.d2c.shop.modules.order.service.OrderItemService;
import com.d2c.shop.modules.order.service.OrderService;
import com.d2c.shop.modules.order.service.PaymentService;
import com.d2c.shop.modules.product.service.ProductSkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Cai
 */
@Api(description = "订单明细业务")
@RestController
@RequestMapping("/b_api/order_item")
public class B_OrderItemController extends B_BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private WxJsPayConfig wxJsPayConfig;

    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R<Page<OrderItemDO>> list(PageModel page, OrderItemQuery query) {
        query.setShopId(loginKeeperHolder.getLoginShopId());
        Page<OrderItemDO> pager = (Page<OrderItemDO>) orderItemService.page(page, QueryUtil.buildWrapper(query));
        return Response.restResult(pager, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "根据ID查询")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R<OrderDO> select(@PathVariable Long id) {
        OrderItemDO orderItem = orderItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, orderItem);
        OrderQuery query = new OrderQuery();
        query.setSn(orderItem.getOrderSn());
        OrderDO order = orderService.getOne(QueryUtil.buildWrapper(query));
        order.getOrderItemList().add(orderItem);
        return Response.restResult(order, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "订单明细发货")
    @RequestMapping(value = "/deliver", method = RequestMethod.POST)
    public R deliverItem(Long orderItemId, String logisticsCom, String logisticsNum) {
        OrderItemDO orderItem = orderItemService.getById(orderItemId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, orderItem);
        Asserts.eq(orderItem.getStatus(), OrderItemDO.StatusEnum.WAIT_DELIVER.name(), "订单明细状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(orderItem.getShopId(), keeper.getShopId(), "您不是本店店员");
        OrderItemDO entity = new OrderItemDO();
        entity.setId(orderItemId);
        entity.setLogisticsCom(logisticsCom);
        entity.setLogisticsNum(logisticsNum);
        entity.setStatus(OrderItemDO.StatusEnum.DELIVERED.name());
        orderItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "修改明细价格")
    @RequestMapping(value = "/update/amount", method = RequestMethod.POST)
    public R updateAmount(Long orderItemId, BigDecimal realPrice) {
        Asserts.notNull("修改金额不能为空", realPrice);
        OrderItemDO orderItem = orderItemService.getById(orderItemId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, orderItem);
        Asserts.eq(orderItem.getStatus(), OrderItemDO.StatusEnum.WAIT_PAY.name(), "只能修改未付款订单金额");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(orderItem.getShopId(), keeper.getShopId(), "您不是本店店员");
        // 修改后金额差值
        BigDecimal diffAmount = (orderItem.getRealPrice().subtract(realPrice)).multiply(new BigDecimal(orderItem.getQuantity()));
        Asserts.ge(orderItem.getPayAmount().subtract(diffAmount), BigDecimal.ZERO, "明细付款金额必须大于0");
        OrderQuery query = new OrderQuery();
        query.setSn(orderItem.getOrderSn());
        OrderDO order = orderService.getOne(QueryUtil.buildWrapper(query));
        Asserts.ge(order.getPayAmount().subtract(diffAmount), BigDecimal.ZERO, "订单付款金额必须大于0");
        Snowflake snowFlake = new Snowflake(2, 2);
        String newSn = PrefixConstant.ORDER_PREFIX + String.valueOf(snowFlake.nextId());
        // 修改订单变动金额
        OrderDO entity = new OrderDO();
        entity.setId(order.getId());
        entity.setSn(newSn);
        entity.setProductAmount(order.getProductAmount().subtract(diffAmount));
        entity.setPayAmount(order.getPayAmount().subtract(diffAmount));
        orderService.updateById(entity);
        // 修改明细变动金额
        OrderItemDO item = new OrderItemDO();
        item.setId(orderItemId);
        item.setOrderSn(newSn);
        item.setRealPrice(realPrice);
        item.setPayAmount(orderItem.getPayAmount().subtract(diffAmount));
        orderItemService.updateById(item);
        OrderItemDO entity2 = new OrderItemDO();
        entity2.setOrderSn(newSn);
        OrderItemQuery query2 = new OrderItemQuery();
        query2.setOrderSn(new String[]{order.getSn()});
        orderItemService.update(entity2, QueryUtil.buildWrapper(query2));
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "同意退款")
    @RequestMapping(value = "/agree/refund", method = RequestMethod.POST)
    public R agreeRefund(Long orderItemId) {
        OrderItemDO orderItem = orderItemService.getById(orderItemId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, orderItem);
        Asserts.eq(orderItem.getStatus(), OrderItemDO.StatusEnum.WAIT_REFUND.name(), "订单明细状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(orderItem.getShopId(), keeper.getShopId(), "您不是本店店员");
        OrderItemDO entity = new OrderItemDO();
        entity.setId(orderItemId);
        entity.setStatus(OrderItemDO.StatusEnum.AGREE_REFUND.name());
        orderItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "拒绝退款")
    @RequestMapping(value = "/refuse/refund", method = RequestMethod.POST)
    public R refuseRefund(Long orderItemId, String refuseMemo) {
        OrderItemDO orderItem = orderItemService.getById(orderItemId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, orderItem);
        Asserts.eq(orderItem.getStatus(), OrderItemDO.StatusEnum.WAIT_REFUND.name(), "订单明细状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(orderItem.getShopId(), keeper.getShopId(), "您不是本店店员");
        OrderItemDO entity = new OrderItemDO();
        entity.setId(orderItemId);
        entity.setRefuseMemo(refuseMemo);
        entity.setStatus(OrderItemDO.StatusEnum.REFUSE_REFUND.name());
        orderItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "同意退货退款")
    @RequestMapping(value = "/agree/reship", method = RequestMethod.POST)
    public R agreeReship(Long orderItemId, String reshipName, String reshipMobile, String reshipAddress) {
        OrderItemDO orderItem = orderItemService.getById(orderItemId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, orderItem);
        Asserts.eq(orderItem.getStatus(), OrderItemDO.StatusEnum.WAIT_RESHIP.name(), "订单明细状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(orderItem.getShopId(), keeper.getShopId(), "您不是本店店员");
        OrderItemDO entity = new OrderItemDO();
        entity.setId(orderItemId);
        entity.setReshipName(reshipName);
        entity.setReshipMobile(reshipMobile);
        entity.setReshipAddress(reshipAddress);
        entity.setStatus(OrderItemDO.StatusEnum.AGREE_RESHIP.name());
        orderItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "拒绝退货退款")
    @RequestMapping(value = "/refuse/reship", method = RequestMethod.POST)
    public R refuseReship(Long orderItemId, String refuseMemo) {
        OrderItemDO orderItem = orderItemService.getById(orderItemId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, orderItem);
        if (!orderItem.getStatus().equals(OrderItemDO.StatusEnum.WAIT_RESHIP.name())
                && !orderItem.getStatus().equals(OrderItemDO.StatusEnum.RESHIPED.name())) {
            throw new ApiException("订单明细状态异常");
        }
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(orderItem.getShopId(), keeper.getShopId(), "您不是本店店员");
        OrderItemDO entity = new OrderItemDO();
        entity.setId(orderItemId);
        entity.setRefuseMemo(refuseMemo);
        entity.setStatus(OrderItemDO.StatusEnum.REFUSE_RESHIP.name());
        orderItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "退货收货")
    @RequestMapping(value = "/receive/reship", method = RequestMethod.POST)
    public R receiveReship(Long orderItemId) {
        OrderItemDO orderItem = orderItemService.getById(orderItemId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, orderItem);
        Asserts.eq(orderItem.getStatus(), OrderItemDO.StatusEnum.RESHIPED.name(), "订单明细状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(orderItem.getShopId(), keeper.getShopId(), "您不是本店店员");
        OrderItemDO entity = new OrderItemDO();
        entity.setId(orderItemId);
        entity.setStatus(OrderItemDO.StatusEnum.AGREE_REFUND.name());
        orderItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "退款完成")
    @RequestMapping(value = "/success/refund", method = RequestMethod.POST)
    public R successRefund(Long orderItemId) throws Exception {
        OrderItemDO orderItem = orderItemService.getById(orderItemId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, orderItem);
        Asserts.eq(orderItem.getStatus(), OrderItemDO.StatusEnum.AGREE_REFUND.name(), "订单明细状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(orderItem.getShopId(), keeper.getShopId(), "您不是本店店员");
        PaymentQuery query = new PaymentQuery();
        query.setOrderSn(orderItem.getOrderSn());
        PaymentDO payment = paymentService.getOne(QueryUtil.buildWrapper(query));
        Asserts.notNull("预付单信息不完整", payment);
        if (payment.getPaymentType().equals(PaymentDO.PaymentTypeEnum.ALI_PAY.name())) {
            this.alipayRefund(orderItem, payment);
        } else if (payment.getPaymentType().equals(PaymentDO.PaymentTypeEnum.WX_PAY.name())) {
            this.wxPayRefund(orderItem);
        }
        orderItemService.doSuccessRefund(orderItem);
        // 返还库存
        productSkuService.doReturnStock(orderItem.getProductSkuId(), orderItem.getProductId(), orderItem.getAfterQuantity());
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    private void wxPayRefund(OrderItemDO orderItem) throws Exception {
        WXPay wxPay = new WXPay(wxJsPayConfig, WxJsPayConfig.REFUND_URL);
        Map<String, String> reqData = new HashMap<>();
        reqData.put("out_trade_no", orderItem.getOrderSn());
        reqData.put("out_refund_no", orderItem.getId().toString());
        DecimalFormat df = new DecimalFormat("0");
        OrderDO order = orderService.getById(orderItem.getOrderId());
        reqData.put("total_fee", df.format(order.getPayAmount().multiply(new BigDecimal(100))));
        reqData.put("refund_fee", df.format(orderItem.getAfterAmount().multiply(new BigDecimal(100))));
        Map<String, String> wxResponse = wxPay.refund(reqData);
        if (!wxResponse.get("return_code").equals("SUCCESS")) {
            throw new ApiException(wxResponse.toString());
        }
    }

    private void alipayRefund(OrderItemDO orderItem, PaymentDO payment) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.API_URL, AliPayConfig.APP_ID1, AliPayConfig.PRIVATE_KEY, AlipayConstants.FORMAT_JSON, AlipayConstants.CHARSET_GBK, AliPayConfig.PUBLIC_KEY, AlipayConstants.SIGN_TYPE_RSA2);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject json = new JSONObject();
        json.put("trade_no", payment.getPaymentSn());
        json.put("refund_amount", orderItem.getAfterAmount());
        json.put("out_request_no", orderItem.getId());
        request.setBizContent(json.toJSONString());
        AlipayTradeRefundResponse aliResponse = alipayClient.execute(request);
        if (!aliResponse.isSuccess()) {
            throw new ApiException(aliResponse.getBody());
        }
    }

}
