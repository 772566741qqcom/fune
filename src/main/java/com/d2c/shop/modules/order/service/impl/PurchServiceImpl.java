package com.d2c.shop.modules.order.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.d2c.shop.common.api.base.BaseService;
import com.d2c.shop.common.utils.ExecutorUtil;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.core.model.ShopFlowDO;
import com.d2c.shop.modules.core.service.ShopFlowService;
import com.d2c.shop.modules.order.mapper.PurchMapper;
import com.d2c.shop.modules.order.model.AllotItemDO;
import com.d2c.shop.modules.order.model.PaymentDO;
import com.d2c.shop.modules.order.model.PurchDO;
import com.d2c.shop.modules.order.model.PurchItemDO;
import com.d2c.shop.modules.order.query.PaymentQuery;
import com.d2c.shop.modules.order.query.PurchItemQuery;
import com.d2c.shop.modules.order.service.PaymentService;
import com.d2c.shop.modules.order.service.PurchItemService;
import com.d2c.shop.modules.order.service.PurchService;
import com.d2c.shop.modules.product.service.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author BaiCai
 */
@Service
public class PurchServiceImpl extends BaseService<PurchMapper, PurchDO> implements PurchService {

    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private PurchItemService purchItemService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ShopFlowService shopFlowService;

    @Override
    @Transactional
    public PurchDO doCreate(PurchDO purch) {
        List<PurchItemDO> purchItemList = purch.getPurchItemList();
        if (purchItemList.size() == 0) {
            throw new ApiException("采购明细不能为空");
        }
        purch.setExpireDate(DateUtil.offsetMinute(new Date(), 20));
        this.save(purch);
        for (PurchItemDO purchItem : purchItemList) {
            // 扣减库存
            int success = productSkuService.doDeductStock(purchItem.getProductSkuId(), purchItem.getProductId(), purchItem.getQuantity());
            if (success == 0) {
                throw new ApiException(purchItem.getProductSkuId() + "的SKU库存不足");
            }
            purchItem.setPurchId(purch.getId());
            purchItem.setPurchSn(purch.getSn());
            purchItem.setFromShopName(purch.getFromName());
            purchItemService.save(purchItem);
        }
        return purch;
    }

    @Override
    @Transactional
    public boolean doPayment(PurchDO purch, PaymentDO.PaymentTypeEnum paymentType, String paymentSn, String mchId) {
        boolean success = true;
        // 支付采购
        PurchDO o = new PurchDO();
        o.setId(purch.getId());
        o.setStatus(PurchDO.StatusEnum.PAID.name());
        o.setPaymentType(paymentType.name());
        o.setPaymentSn(paymentSn);
        success &= this.updateById(o);
        // 支付采购单明细
        PurchItemDO oi = new PurchItemDO();
        oi.setStatus(PurchItemDO.StatusEnum.PAID.name());
        oi.setPaymentType(paymentType.name());
        oi.setPaymentSn(paymentSn);
        PurchItemQuery oiq = new PurchItemQuery();
        oiq.setPurchSn(new String[]{purch.getSn()});
        success &= purchItemService.update(oi, QueryUtil.buildWrapper(oiq));
        // 支付单核销
        PaymentDO p = new PaymentDO();
        p.setStatus(1);
        p.setPaymentSn(paymentSn);
        if (paymentType.equals(PaymentDO.PaymentTypeEnum.ALI_PAY)) {
            p.setPrepayId(paymentSn);
            p.setMchId(mchId);
        }
        PaymentQuery pq = new PaymentQuery();
        pq.setOrderSn(purch.getSn());
        success &= paymentService.update(p, QueryUtil.buildWrapper(pq));
        ExecutorUtil.fixedPool.submit(() -> {
                    // 最后统计消费
                    this.statisticConsumption(purch, paymentType, paymentSn);
                }
        );
        return success;
    }

    // 最后统计消费
    private void statisticConsumption(PurchDO purch, PaymentDO.PaymentTypeEnum paymentType, String paymentSn) {
        // 门店订单收入
        ShopFlowDO sf = new ShopFlowDO();
        sf.setStatus(1);
        sf.setType(ShopFlowDO.TypeEnum.ORDER.name());
        sf.setShopId(purch.getFromShopId());
        sf.setOrderSn(purch.getSn());
        sf.setPaymentType(paymentType.name());
        sf.setPaymentSn(paymentSn);
        sf.setAmount(purch.getPayAmount());
        shopFlowService.doFlowing(sf, purch.getPayAmount(), BigDecimal.ZERO);
    }

    @Override
    @Transactional
    public boolean doClose(PurchDO purch) {
        boolean success = true;
        // 关闭采购单
        PurchDO p = new PurchDO();
        p.setId(purch.getId());
        p.setStatus(PurchDO.StatusEnum.CLOSE.name());
        success &= this.updateById(p);
        // 关闭采购明细
        PurchItemDO pi = new PurchItemDO();
        pi.setStatus(AllotItemDO.StatusEnum.CLOSE.name());
        PurchItemQuery piq = new PurchItemQuery();
        piq.setPurchSn(new String[]{purch.getSn()});
        success &= purchItemService.update(pi, QueryUtil.buildWrapper(piq));
        List<PurchItemDO> purchItemList = purchItemService.list(QueryUtil.buildWrapper(piq));
        for (PurchItemDO purchItem : purchItemList) {
            // 返还库存
            productSkuService.doReturnStock(purchItem.getProductSkuId(), purchItem.getProductId(), purchItem.getQuantity());
        }
        return success;
    }

}
