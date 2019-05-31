package com.d2c.shop.modules.order.service.impl;

import com.d2c.shop.common.api.base.BaseService;
import com.d2c.shop.modules.core.model.ShopFlowDO;
import com.d2c.shop.modules.core.service.ShopFlowService;
import com.d2c.shop.modules.order.mapper.OrderItemMapper;
import com.d2c.shop.modules.order.model.OrderItemDO;
import com.d2c.shop.modules.order.query.OrderItemQuery;
import com.d2c.shop.modules.order.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author BaiCai
 */
@Service
public class OrderItemServiceImpl extends BaseService<OrderItemMapper, OrderItemDO> implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ShopFlowService shopFlowService;

    @Override
    public Map<String, Object> countDaily(OrderItemQuery query) {
        return orderItemMapper.countDaily(query);
    }

    @Override
    public boolean doSuccessRefund(OrderItemDO orderItem) {
        OrderItemDO entity = new OrderItemDO();
        entity.setId(orderItem.getId());
        entity.setStatus(OrderItemDO.StatusEnum.REFUNDED.name());
        this.updateById(entity);
        ShopFlowDO sf = new ShopFlowDO();
        sf.setStatus(1);
        sf.setType(ShopFlowDO.TypeEnum.REFUND.name());
        sf.setShopId(orderItem.getShopId());
        sf.setOrderSn(orderItem.getOrderSn());
        sf.setPaymentType(orderItem.getPaymentType());
        sf.setPaymentSn(orderItem.getPaymentSn());
        sf.setAmount(orderItem.getAfterAmount().negate());
        shopFlowService.doFlowing(sf, orderItem.getAfterAmount().negate(), BigDecimal.ZERO);
        return true;
    }

}
