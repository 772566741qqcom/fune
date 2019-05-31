package com.d2c.shop.c_api;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.d2c.shop.c_api.base.C_BaseController;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.PageModel;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.member.model.MemberDO;
import com.d2c.shop.modules.order.model.CrowdGroupDO;
import com.d2c.shop.modules.order.model.OrderDO;
import com.d2c.shop.modules.order.model.OrderItemDO;
import com.d2c.shop.modules.order.query.OrderItemQuery;
import com.d2c.shop.modules.order.query.OrderQuery;
import com.d2c.shop.modules.order.service.CrowdGroupService;
import com.d2c.shop.modules.order.service.OrderItemService;
import com.d2c.shop.modules.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Cai
 */
@Api(description = "订单明细业务")
@RestController
@RequestMapping("/c_api/order_item")
public class C_OrderItemController extends C_BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private CrowdGroupService crowdGroupService;

    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R<Page<OrderItemDO>> list(PageModel page, OrderItemQuery query) {
        query.setMemberId(loginMemberHolder.getLoginId());
        Page<OrderItemDO> pager = (Page<OrderItemDO>) orderItemService.page(page, QueryUtil.buildWrapper(query));
        if (query.getType() != null && query.getType().equals(OrderDO.TypeEnum.CROWD.name())) {
            Set<Long> crowdIds = new HashSet<>();
            pager.getRecords().forEach(item -> crowdIds.add(item.getCrowdId()));
            if (crowdIds.size() == 0) return Response.restResult(pager, ResultCode.SUCCESS);
            List<CrowdGroupDO> groupList = (List<CrowdGroupDO>) crowdGroupService.listByIds(crowdIds);
            Map<Long, CrowdGroupDO> groupMap = new ConcurrentHashMap<>();
            groupList.forEach(item -> groupMap.put(item.getId(), item));
            for (OrderItemDO oi : pager.getRecords()) {
                oi.setCrowdGroup(groupMap.get(oi.getCrowdId()));
            }
        }
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

    @ApiOperation(value = "订单明细签收")
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public R receiveItem(Long orderItemId) {
        OrderItemDO orderItem = orderItemService.getById(orderItemId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, orderItem);
        Asserts.eq(orderItem.getStatus(), OrderItemDO.StatusEnum.DELIVERED.name(), "订单明细状态异常");
        MemberDO member = loginMemberHolder.getLoginMember();
        Asserts.eq(orderItem.getMemberId(), member.getId(), "订单不属于本人");
        OrderItemDO entity = new OrderItemDO();
        entity.setId(orderItemId);
        entity.setStatus(OrderItemDO.StatusEnum.RECEIVED.name());
        orderItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "申请退款")
    @RequestMapping(value = "/apply/refund", method = RequestMethod.POST)
    public R applyRefund(Long orderItemId, BigDecimal afterAmount, String afterMemo, String afterPic) {
        Asserts.notNull(ResultCode.REQUEST_PARAM_NULL, afterAmount, afterMemo);
        OrderItemDO orderItem = orderItemService.getById(orderItemId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, orderItem);
        if (!orderItem.getStatus().equals(OrderItemDO.StatusEnum.PAID.name())
                && !orderItem.getStatus().equals(OrderItemDO.StatusEnum.WAIT_DELIVER.name())) {
            throw new ApiException("订单明细状态异常");
        }
        MemberDO member = loginMemberHolder.getLoginMember();
        Asserts.eq(orderItem.getMemberId(), member.getId(), "订单不属于本人");
        OrderItemDO entity = new OrderItemDO();
        entity.setId(orderItemId);
        entity.setAfterType(1);
        entity.setAfterDate(new Date());
        entity.setAfterQuantity(orderItem.getQuantity());
        entity.setAfterAmount(afterAmount);
        entity.setAfterMemo(afterMemo);
        entity.setAfterPic(afterPic);
        entity.setStatus(OrderItemDO.StatusEnum.WAIT_REFUND.name());
        entity.setOldStatus(orderItem.getStatus());
        orderItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "取消退款")
    @RequestMapping(value = "/cancel/refund", method = RequestMethod.POST)
    public R cancelRefund(Long orderItemId) {
        OrderItemDO orderItem = orderItemService.getById(orderItemId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, orderItem);
        if (!orderItem.getStatus().equals(OrderItemDO.StatusEnum.WAIT_REFUND.name())
                && !orderItem.getStatus().equals(OrderItemDO.StatusEnum.REFUSE_REFUND.name())) {
            throw new ApiException("订单明细状态异常");
        }
        MemberDO member = loginMemberHolder.getLoginMember();
        Asserts.eq(orderItem.getMemberId(), member.getId(), "订单不属于本人");
        OrderItemDO entity = new OrderItemDO();
        entity.setId(orderItemId);
        entity.setStatus(orderItem.getOldStatus());
        entity.setAfterType(0);
        orderItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "申请退货退款")
    @RequestMapping(value = "/apply/reship", method = RequestMethod.POST)
    public R applyReship(Long orderItemId, Integer afterQuantity, String afterMemo, String afterPic) {
        Asserts.notNull(ResultCode.REQUEST_PARAM_NULL, afterQuantity, afterMemo);
        OrderItemDO orderItem = orderItemService.getById(orderItemId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, orderItem);
        Asserts.eq(orderItem.getStatus(), OrderItemDO.StatusEnum.RECEIVED.name(), "订单明细状态异常");
        MemberDO member = loginMemberHolder.getLoginMember();
        Asserts.eq(orderItem.getMemberId(), member.getId(), "订单不属于本人");
        OrderItemDO entity = new OrderItemDO();
        entity.setId(orderItemId);
        entity.setAfterType(2);
        entity.setAfterDate(new Date());
        entity.setAfterQuantity(afterQuantity);
        entity.setAfterAmount(orderItem.getPayAmount().multiply(new BigDecimal(afterQuantity)).divide(new BigDecimal(orderItem.getQuantity()), 2, BigDecimal.ROUND_HALF_UP));
        entity.setAfterMemo(afterMemo);
        entity.setAfterPic(afterPic);
        entity.setStatus(OrderItemDO.StatusEnum.WAIT_RESHIP.name());
        entity.setOldStatus(orderItem.getStatus());
        orderItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "取消退货退款")
    @RequestMapping(value = "/cancel/reship", method = RequestMethod.POST)
    public R cancelReship(Long orderItemId) {
        OrderItemDO orderItem = orderItemService.getById(orderItemId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, orderItem);
        if (!orderItem.getStatus().equals(OrderItemDO.StatusEnum.WAIT_RESHIP.name())
                && !orderItem.getStatus().equals(OrderItemDO.StatusEnum.REFUSE_RESHIP.name())
                && !orderItem.getStatus().equals(OrderItemDO.StatusEnum.AGREE_RESHIP.name())) {
            throw new ApiException("订单明细状态异常");
        }
        MemberDO member = loginMemberHolder.getLoginMember();
        Asserts.eq(orderItem.getMemberId(), member.getId(), "订单不属于本人");
        OrderItemDO entity = new OrderItemDO();
        entity.setId(orderItemId);
        entity.setStatus(orderItem.getOldStatus());
        entity.setAfterType(0);
        orderItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "退货寄回")
    @RequestMapping(value = "/deliver/reship", method = RequestMethod.POST)
    public R deliverReship(Long orderItemId, String reshipLogisticsCom, String reshipLogisticsNum) {
        Asserts.notNull(ResultCode.REQUEST_PARAM_NULL, reshipLogisticsCom, reshipLogisticsNum);
        OrderItemDO orderItem = orderItemService.getById(orderItemId);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, orderItem);
        Asserts.eq(orderItem.getStatus(), OrderItemDO.StatusEnum.AGREE_RESHIP.name(), "订单明细状态异常");
        MemberDO member = loginMemberHolder.getLoginMember();
        Asserts.eq(orderItem.getMemberId(), member.getId(), "订单不属于本人");
        OrderItemDO entity = new OrderItemDO();
        entity.setId(orderItemId);
        entity.setReshipLogisticsCom(reshipLogisticsCom);
        entity.setReshipLogisticsNum(reshipLogisticsNum);
        entity.setStatus(OrderItemDO.StatusEnum.RESHIPED.name());
        orderItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}
