package com.d2c.shop.b_api;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.d2c.shop.b_api.base.B_BaseController;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.logger.nosql.mongodb.service.UserVisitLogService;
import com.d2c.shop.modules.member.query.MemberShopQuery;
import com.d2c.shop.modules.member.service.MemberShopService;
import com.d2c.shop.modules.order.model.AllotItemDO;
import com.d2c.shop.modules.order.model.OrderDO;
import com.d2c.shop.modules.order.model.OrderItemDO;
import com.d2c.shop.modules.order.model.PurchItemDO;
import com.d2c.shop.modules.order.query.AllotItemQuery;
import com.d2c.shop.modules.order.query.OrderItemQuery;
import com.d2c.shop.modules.order.query.OrderQuery;
import com.d2c.shop.modules.order.query.PurchItemQuery;
import com.d2c.shop.modules.order.service.AllotItemService;
import com.d2c.shop.modules.order.service.OrderItemService;
import com.d2c.shop.modules.order.service.OrderService;
import com.d2c.shop.modules.order.service.PurchItemService;
import com.d2c.shop.modules.product.query.ProductQuery;
import com.d2c.shop.modules.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Cai
 */
@Api(description = "首页业务")
@RestController
@RequestMapping("/b_api/home")
public class B_HomeController extends B_BaseController {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MemberShopService memberShopService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private PurchItemService purchItemService;
    @Autowired
    private AllotItemService allotItemService;
    @Autowired
    private UserVisitLogService userVisitLogService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "数据面板")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public R info() {
        Long shopId = loginKeeperHolder.getLoginShopId();
        Object data = redisTemplate.opsForValue().get("HOME_DATA::" + shopId);
        if (data != null) {
            return Response.restResult(data, ResultCode.SUCCESS);
        }
        JSONObject result = new JSONObject();
        JSONObject manage = new JSONObject();
        JSONObject orders = new JSONObject();
        JSONObject daily = new JSONObject();
        // 商品总计
        ProductQuery pq = new ProductQuery();
        pq.setShopId(shopId);
        pq.setVirtual(0);
        int productTotal = productService.count(QueryUtil.buildWrapper(pq));
        manage.put("productTotal", productTotal);
        // 订单总计
        OrderQuery oq = new OrderQuery();
        oq.setShopId(shopId);
        int orderTotal = orderService.count(QueryUtil.buildWrapper(oq));
        manage.put("orderTotal", orderTotal);
        // 会员总计
        MemberShopQuery mq = new MemberShopQuery();
        mq.setShopId(shopId);
        int memberTotal = memberShopService.count(QueryUtil.buildWrapper(mq));
        manage.put("memberTotal", memberTotal);
        // 待付款
        oq.setStatus(OrderDO.StatusEnum.WAIT_PAY.name());
        int waitPayCount = orderService.count(QueryUtil.buildWrapper(oq));
        orders.put("waitPayCount", waitPayCount);
        // 待发货
        OrderItemQuery oiq = new OrderItemQuery();
        oiq.setShopId(shopId);
        oiq.setStatus(new String[]{OrderItemDO.StatusEnum.WAIT_DELIVER.name()});
        int waitDeliverCount = orderItemService.count(QueryUtil.buildWrapper(oiq));
        orders.put("waitDeliverCount", waitDeliverCount);
        // 待退款
        oiq.setStatus(new String[]{OrderItemDO.StatusEnum.WAIT_REFUND.name(), OrderItemDO.StatusEnum.AGREE_REFUND.name()});
        int waitRefundCount = orderItemService.count(QueryUtil.buildWrapper(oiq));
        orders.put("waitRefundCount", waitRefundCount);
        // 今日数据
        Date now = new Date();
        oq.setStatus(OrderDO.StatusEnum.PAID.name());
        oq.setCreateDateL(DateUtil.beginOfDay(now));
        oq.setCreateDateR(DateUtil.endOfDay(now));
        Map<String, Object> oMap = orderService.countDaily(oq);
        int orderCount = Integer.valueOf(oMap.get("orderCount").toString());
        int memberCount = Integer.valueOf(oMap.get("memberCount").toString());
        BigDecimal paidAmount = new BigDecimal(oMap.get("paidAmount").toString());
        daily.put("orderCount", orderCount);
        daily.put("memberCount", memberCount);
        daily.put("paidAmount", paidAmount);
        oiq.setStatus(new String[]{OrderItemDO.StatusEnum.PAID.name(), OrderItemDO.StatusEnum.WAIT_DELIVER.name()});
        oiq.setCreateDateL(DateUtil.beginOfDay(now));
        oiq.setCreateDateR(DateUtil.endOfDay(now));
        Map<String, Object> oiMap = orderItemService.countDaily(oiq);
        int quantityCount = Integer.valueOf(oiMap.get("quantityCount").toString());
        daily.put("quantityCount", quantityCount);
        mq.setCreateDateL(DateUtil.beginOfDay(now));
        mq.setCreateDateR(DateUtil.endOfDay(now));
        int newlyCount = memberShopService.count(QueryUtil.buildWrapper(mq));
        daily.put("newlyCount", newlyCount);
        int visitorCount = userVisitLogService.countDaily(shopId);
        daily.put("visitorCount", visitorCount);
        // 数据总览
        result.put("manage", manage);
        result.put("orders", orders);
        result.put("daily", daily);
        redisTemplate.opsForValue().set("HOME_DATA::" + shopId, result, 60, TimeUnit.SECONDS);
        return Response.restResult(result, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "进货面板")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public R count() {
        Long shopId = loginKeeperHolder.getLoginShopId();
        JSONObject result = new JSONObject();
        // 采购待发货
        PurchItemQuery query1 = new PurchItemQuery();
        query1.setToShopId(shopId);
        query1.setStatus(PurchItemDO.StatusEnum.PAID.name());
        int purchCount = purchItemService.count(QueryUtil.buildWrapper(query1));
        result.put("purchCount", purchCount);
        // 调拨待发货
        AllotItemQuery query2 = new AllotItemQuery();
        query2.setToShopId(shopId);
        query2.setStatus(AllotItemDO.StatusEnum.AGREE.name());
        int allotCount = allotItemService.count(QueryUtil.buildWrapper(query2));
        result.put("allotCount", allotCount);
        // 待调拨退回
        result.put("afterCount", 0);
        return Response.restResult(result, ResultCode.SUCCESS);
    }

}
