package com.d2c.shop.modules.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.d2c.shop.common.api.base.BaseService;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.core.service.ShopService;
import com.d2c.shop.modules.order.mapper.SettlementMapper;
import com.d2c.shop.modules.order.model.PaymentDO;
import com.d2c.shop.modules.order.model.SettlementDO;
import com.d2c.shop.modules.order.query.PaymentQuery;
import com.d2c.shop.modules.order.service.PaymentService;
import com.d2c.shop.modules.order.service.SettlementService;
import com.d2c.shop.modules.product.service.AllotSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author BaiCai
 */
@Service
public class SettlementServiceImpl extends BaseService<SettlementMapper, SettlementDO> implements SettlementService {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private AllotSkuService allotSkuService;

    @Override
    @Transactional
    public boolean doPayment(SettlementDO settlement, PaymentDO.PaymentTypeEnum paymentType, String paymentSn, String mchId) {
        boolean success = true;
        // 支付结算单
        SettlementDO o = new SettlementDO();
        o.setId(settlement.getId());
        o.setStatus(SettlementDO.StatusEnum.PAID.name());
        o.setPaymentType(paymentType.name());
        o.setPaymentSn(paymentSn);
        success &= this.updateById(o);
        // 支付单核销
        PaymentDO p = new PaymentDO();
        p.setStatus(1);
        p.setPaymentSn(paymentSn);
        if (paymentType.equals(PaymentDO.PaymentTypeEnum.ALI_PAY)) {
            p.setPrepayId(paymentSn);
            p.setMchId(mchId);
        }
        PaymentQuery pq = new PaymentQuery();
        pq.setOrderSn(settlement.getSn());
        success &= paymentService.update(p, QueryUtil.buildWrapper(pq));
        // 返还保证金
        shopService.updateDeposit(settlement.getShopId(), settlement.getPayAmount(), BigDecimal.ZERO);
        // 扣减库存
        if (StrUtil.isNotBlank(settlement.getGoods())) {
            JSONArray array = JSONArray.parseArray(settlement.getGoods());
            for (int i = 0; i < array.size(); i++) {
                JSONObject job = array.getJSONObject(i);
                allotSkuService.doDeductStock(Long.valueOf(job.get("id").toString()), Integer.valueOf(job.get("count").toString()));
            }
        }
        return success;
    }

}
