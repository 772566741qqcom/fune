package com.d2c.shop.modules.order.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.d2c.shop.common.api.base.BaseService;
import com.d2c.shop.common.utils.ExecutorUtil;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.member.model.MemberCouponDO;
import com.d2c.shop.modules.member.service.MemberCouponService;
import com.d2c.shop.modules.order.mapper.CrowdGroupMapper;
import com.d2c.shop.modules.order.model.CrowdGroupDO;
import com.d2c.shop.modules.order.model.OrderItemDO;
import com.d2c.shop.modules.order.query.OrderItemQuery;
import com.d2c.shop.modules.order.service.CrowdGroupService;
import com.d2c.shop.modules.order.service.OrderItemService;
import com.d2c.shop.modules.product.model.CouponDO;
import com.d2c.shop.modules.product.model.ProductDO;
import com.d2c.shop.modules.product.service.CouponService;
import com.d2c.shop.modules.product.service.ProductService;
import com.d2c.shop.rabbitmq.sender.CrowdDelayedSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author BaiCai
 */
@Service
public class CrowdGroupServiceImpl extends BaseService<CrowdGroupMapper, CrowdGroupDO> implements CrowdGroupService {

    @Autowired
    private CrowdGroupMapper crowdGroupMapper;
    @Autowired
    private CrowdDelayedSender crowdDelayedSender;
    @Autowired
    private CouponService couponService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private MemberCouponService memberCouponService;

    @Override
    @Transactional
    public boolean save(CrowdGroupDO entity) {
        boolean success = super.save(entity);
        // 发送延迟消息
        ExecutorUtil.fixedPool.submit(() -> {
                    crowdDelayedSender.send(String.valueOf(entity.getId()), DateUtil.between(entity.getDeadline(), entity.getCreateDate(), DateUnit.SECOND));
                }
        );
        return success;
    }

    @Override
    @Transactional
    public int doAttend(Long id, String avatars) {
        return crowdGroupMapper.doAttend(id, avatars);
    }

    @Override
    @Transactional
    public int doCancel(Long id, String avatars) {
        return crowdGroupMapper.doCancel(id, avatars);
    }

    @Override
    @Transactional
    public void doSendCrowdCoupon(CrowdGroupDO crowdGroup) {
        ProductDO product = productService.getById(crowdGroup.getProductId());
        CouponDO coupon = couponService.getById(product.getCouponId());
        if (coupon != null) {
            OrderItemQuery noiq = new OrderItemQuery();
            noiq.setCrowdId(crowdGroup.getId());
            noiq.setStatus(new String[]{OrderItemDO.StatusEnum.PAID.name()});
            List<OrderItemDO> oiList = orderItemService.list(QueryUtil.buildWrapper(noiq));
            for (OrderItemDO item : oiList) {
                MemberCouponDO memberCoupon = new MemberCouponDO();
                memberCoupon.setMemberId(item.getMemberId());
                memberCoupon.setCouponId(coupon.getId());
                memberCoupon.setCouponName(coupon.getName());
                memberCoupon.setShopId(item.getShopId());
                memberCoupon.setShopName(item.getShopName());
                memberCoupon.setStatus(1);
                Date serviceStartDate = coupon.getServiceStartDate();
                Date serviceEndDate = coupon.getServiceEndDate();
                if (coupon.getServiceSustain() != null && coupon.getServiceSustain() > 0) {
                    serviceStartDate = new Date();
                    serviceEndDate = DateUtil.offsetHour(serviceStartDate, coupon.getServiceSustain());
                }
                memberCoupon.setServiceStartDate(serviceStartDate);
                memberCoupon.setServiceEndDate(serviceEndDate);
                memberCouponService.doSend(memberCoupon);
            }
        }
    }

}
