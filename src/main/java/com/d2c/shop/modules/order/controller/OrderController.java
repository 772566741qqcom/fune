package com.d2c.shop.modules.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.d2c.shop.common.api.PageModel;
import com.d2c.shop.common.api.base.extension.BaseExcelCtrl;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.order.model.OrderDO;
import com.d2c.shop.modules.order.model.OrderItemDO;
import com.d2c.shop.modules.order.query.OrderItemQuery;
import com.d2c.shop.modules.order.query.OrderQuery;
import com.d2c.shop.modules.order.service.OrderItemService;
import com.d2c.shop.modules.order.service.OrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author BaiCai
 */
@Api(description = "订单管理")
@RestController
@RequestMapping("/back/order")
public class OrderController extends BaseExcelCtrl<OrderDO, OrderQuery> {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    @Override
    public List<Object> selectListForExcelExport(Object o, int i) {
        OrderQuery query = (OrderQuery) o;
        Page page = new Page(i, PageModel.MAX_SIZE, false);
        Page<OrderDO> pager = (Page<OrderDO>) orderService.page(page, QueryUtil.buildWrapper(query, false));
        List<String> orderSns = new ArrayList<>();
        Map<String, OrderDO> orderMap = new ConcurrentHashMap<>();
        for (OrderDO order : pager.getRecords()) {
            orderSns.add(order.getSn());
            orderMap.put(order.getSn(), order);
        }
        if (orderSns.size() == 0) return new ArrayList<>();
        OrderItemQuery itemQuery = new OrderItemQuery();
        itemQuery.setOrderSn(orderSns.toArray(new String[0]));
        List<OrderItemDO> orderItemList = orderItemService.list(QueryUtil.buildWrapper(itemQuery));
        for (OrderItemDO orderItem : orderItemList) {
            if (orderMap.get(orderItem.getOrderSn()) != null) {
                orderMap.get(orderItem.getOrderSn()).getOrderItemList().add(orderItem);
            }
        }
        List<Object> result = new ArrayList<>();
        result.addAll(pager.getRecords());
        return result;
    }

}
