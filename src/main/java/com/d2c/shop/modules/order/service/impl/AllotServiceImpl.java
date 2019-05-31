package com.d2c.shop.modules.order.service.impl;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.d2c.shop.common.api.base.BaseService;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.core.service.ShopService;
import com.d2c.shop.modules.order.mapper.AllotMapper;
import com.d2c.shop.modules.order.model.AllotDO;
import com.d2c.shop.modules.order.model.AllotItemDO;
import com.d2c.shop.modules.order.query.AllotItemQuery;
import com.d2c.shop.modules.order.service.AllotItemService;
import com.d2c.shop.modules.order.service.AllotService;
import com.d2c.shop.modules.product.service.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author BaiCai
 */
@Service
public class AllotServiceImpl extends BaseService<AllotMapper, AllotDO> implements AllotService {

    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private AllotItemService allotItemService;
    @Autowired
    private ShopService shopService;

    @Override
    @Transactional
    public AllotDO doCreate(AllotDO allot) {
        List<AllotItemDO> allotItemList = allot.getAllotItemList();
        if (allotItemList.size() == 0) {
            throw new ApiException("调拨明细不能为空");
        }
        this.save(allot);
        for (AllotItemDO allotItem : allotItemList) {
            // 扣减库存
            int success = productSkuService.doDeductStock(allotItem.getProductSkuId(), allotItem.getProductId(), allotItem.getQuantity());
            if (success == 0) {
                throw new ApiException(allotItem.getProductSkuId() + "的SKU库存不足");
            }
            allotItem.setAllotId(allot.getId());
            allotItem.setAllotSn(allot.getSn());
            allotItem.setFromShopName(allot.getFromName());
            allotItemService.save(allotItem);
        }
        // 扣除保证金
        shopService.updateDeposit(allot.getToShopId(), allot.getProductAmount().negate(), BigDecimal.ZERO);
        return allot;
    }

    @Override
    @Transactional
    public boolean doClose(AllotDO allot) {
        boolean success = true;
        // 关闭调拨单
        AllotDO a = new AllotDO();
        a.setId(allot.getId());
        a.setStatus(AllotDO.StatusEnum.CLOSE.name());
        success &= this.updateById(a);
        // 关闭调拨明细
        AllotItemDO ai = new AllotItemDO();
        ai.setStatus(AllotItemDO.StatusEnum.CLOSE.name());
        AllotItemQuery aiq = new AllotItemQuery();
        aiq.setAllotSn(new String[]{allot.getSn()});
        success &= allotItemService.update(ai, QueryUtil.buildWrapper(aiq));
        List<AllotItemDO> allotItemList = allotItemService.list(QueryUtil.buildWrapper(aiq));
        for (AllotItemDO allotItem : allotItemList) {
            // 返还库存
            productSkuService.doReturnStock(allotItem.getProductSkuId(), allotItem.getProductId(), allotItem.getQuantity());
        }
        // 返还保证金
        shopService.updateDeposit(allot.getToShopId(), allot.getProductAmount(), BigDecimal.ZERO);
        return success;
    }

}
