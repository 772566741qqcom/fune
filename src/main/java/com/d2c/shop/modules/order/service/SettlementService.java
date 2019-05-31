package com.d2c.shop.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.d2c.shop.modules.order.model.PaymentDO;
import com.d2c.shop.modules.order.model.SettlementDO;

/**
 * @author BaiCai
 */
public interface SettlementService extends IService<SettlementDO> {

    boolean doPayment(SettlementDO settlement, PaymentDO.PaymentTypeEnum paymentType, String paymentSn, String mchId);

}
