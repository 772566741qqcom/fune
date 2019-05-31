package com.d2c.shop.e_api;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.e_api.base.E_BaseController;
import com.d2c.shop.modules.order.model.OrderDO;
import com.d2c.shop.modules.order.model.OrderItemDO;
import com.d2c.shop.modules.order.query.OrderItemQuery;
import com.d2c.shop.modules.order.query.OrderQuery;
import com.d2c.shop.modules.order.service.OrderItemService;
import com.d2c.shop.modules.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "ERP-订单业务接口")
@RestController
@RequestMapping("/e_api/order")
public class E_OrderController extends E_BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    @ApiOperation(value = "订单列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public R<Page<OrderDO>> list(OrderQuery query, Page page) {
        Page<OrderDO> pager = (Page<OrderDO>) orderService.page(page, QueryUtil.buildWrapper(query, false));
        for (OrderDO orderDO : pager.getRecords()) {
            OrderItemQuery orderItemQuery = new OrderItemQuery();
            orderItemQuery.setOrderSn(new String[]{orderDO.getSn()});
            orderDO.setOrderItemList(orderItemService.list(QueryUtil.buildWrapper(orderItemQuery, false)));
        }
        return Response.restResult(pager, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "订单发货")
    @RequestMapping(value = "/delivery", method = RequestMethod.POST)
    public R delivery(Long orderItemId, String logisticsCom, String logisticsNum) {
        OrderItemDO orderItemDO = new OrderItemDO();
        orderItemDO.setId(orderItemId);
        orderItemDO.setLogisticsCom(logisticsCom);
        orderItemDO.setLogisticsNum(logisticsNum);
        orderItemDO.setStatus(OrderItemDO.StatusEnum.DELIVERED.name());
        orderItemService.updateById(orderItemDO);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}
