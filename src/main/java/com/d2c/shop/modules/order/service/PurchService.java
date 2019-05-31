package com.d2c.shop.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.d2c.shop.modules.order.model.PaymentDO;
import com.d2c.shop.modules.order.model.PurchDO;

/**
 * @author BaiCai
 */
public interface PurchService extends IService<PurchDO> {

    PurchDO doCreate(PurchDO purch);

    boolean doPayment(PurchDO purch, PaymentDO.PaymentTypeEnum paymentType, String paymentSn, String mchId);

    boolean doClose(PurchDO purch);

}
