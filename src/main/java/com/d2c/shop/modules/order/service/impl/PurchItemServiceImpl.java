package com.d2c.shop.modules.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.d2c.shop.common.api.base.BaseService;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.common.utils.ReflectUtil;
import com.d2c.shop.modules.core.model.ShopFlowDO;
import com.d2c.shop.modules.core.service.ShopFlowService;
import com.d2c.shop.modules.order.mapper.PurchItemMapper;
import com.d2c.shop.modules.order.model.PurchItemDO;
import com.d2c.shop.modules.order.service.PurchItemService;
import com.d2c.shop.modules.product.model.ProductDO;
import com.d2c.shop.modules.product.model.ProductSkuDO;
import com.d2c.shop.modules.product.query.ProductQuery;
import com.d2c.shop.modules.product.query.ProductSkuQuery;
import com.d2c.shop.modules.product.service.ProductService;
import com.d2c.shop.modules.product.service.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author BaiCai
 */
@Service
public class PurchItemServiceImpl extends BaseService<PurchItemMapper, PurchItemDO> implements PurchItemService {

    @Autowired
    private ShopFlowService shopFlowService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductSkuService productSkuService;

    @Override
    @Transactional
    public Long doReceive(PurchItemDO purchItem) {
        // 返还库存
        ProductQuery pq = new ProductQuery();
        pq.setSourceId(purchItem.getProductId());
        pq.setShopId(purchItem.getToShopId());
        ProductDO target = productService.getOne(QueryUtil.buildWrapper(pq));
        if (target == null) {
            target = new ProductDO();
            ProductDO source = productService.getById(purchItem.getProductId());
            BeanUtil.copyProperties(source, target, ReflectUtil.clearPublicFields());
            target.setCrowdPrice(null);
            target.setCrowdStartDate(null);
            target.setCrowdEndDate(null);
            target.setCrowdGroupTime(null);
            target.setCrowdGroupNum(null);
            target.setCouponId(null);
            target.setCategoryId(null);
            target.setSourceId(purchItem.getProductId());
            target.setShopId(purchItem.getToShopId());
            target.setCrowd(0);
            target.setStatus(0);
            target.setAllot(0);
            target.setBuyout(0);
            target.setStock(0);
            productService.save(target);
        }
        ProductSkuQuery sq = new ProductSkuQuery();
        sq.setSourceId(purchItem.getProductSkuId());
        sq.setShopId(purchItem.getToShopId());
        ProductSkuDO target2 = productSkuService.getOne(QueryUtil.buildWrapper(sq));
        if (target2 == null) {
            target2 = new ProductSkuDO();
            ProductSkuDO source2 = productSkuService.getById(purchItem.getProductSkuId());
            BeanUtil.copyProperties(source2, target2, ReflectUtil.clearPublicFields());
            target2.setSourceId(purchItem.getProductSkuId());
            target2.setProductId(target.getId());
            target2.setShopId(purchItem.getToShopId());
            target2.setStatus(0);
            target2.setStock(0);
            productSkuService.save(target2);
        }
        productSkuService.doReturnStock(target2.getId(), target.getId(), purchItem.getActualQuantity());
        // 更新单据
        PurchItemDO entity = new PurchItemDO();
        entity.setId(purchItem.getId());
        entity.setStatus(PurchItemDO.StatusEnum.RECEIVE.name());
        entity.setActualQuantity(purchItem.getActualQuantity());
        entity.setDifference(purchItem.getDifference());
        entity.setTargetId(target.getId());
        this.updateById(entity);
        return target.getId();
    }

    @Override
    @Transactional
    public boolean doHandleDifference(PurchItemDO purchItem) {
        PurchItemDO entity = new PurchItemDO();
        entity.setId(purchItem.getId());
        entity.setDifference(8);
        this.updateById(entity);
        Integer diff = purchItem.getQuantity() - purchItem.getActualQuantity();
        // 退还差异余额
        ShopFlowDO sf = new ShopFlowDO();
        sf.setStatus(1);
        sf.setType(ShopFlowDO.TypeEnum.DIFF.name());
        sf.setShopId(purchItem.getToShopId());
        sf.setOrderSn(purchItem.getPurchSn());
        sf.setPaymentType(purchItem.getPaymentType());
        sf.setPaymentSn(purchItem.getPaymentSn());
        sf.setAmount(purchItem.getSupplyPrice().multiply(new BigDecimal(diff)));
        shopFlowService.doFlowing(sf, purchItem.getSupplyPrice().multiply(new BigDecimal(diff)), BigDecimal.ZERO);
        // 返还库存
        productSkuService.doReturnStock(purchItem.getProductSkuId(), purchItem.getProductId(), diff);
        return true;
    }

    @Override
    @Transactional
    public boolean doCancelDifference(PurchItemDO purchItem) {
        PurchItemDO entity = new PurchItemDO();
        entity.setId(purchItem.getId());
        entity.setDifference(0);
        this.updateById(entity);
        Integer diff = purchItem.getQuantity() - purchItem.getActualQuantity();
        // 返还库存
        ProductSkuQuery query = new ProductSkuQuery();
        query.setSourceId(purchItem.getProductSkuId());
        query.setShopId(purchItem.getToShopId());
        ProductSkuDO target = productSkuService.getOne(QueryUtil.buildWrapper(query));
        productSkuService.doReturnStock(target.getId(), target.getProductId(), diff);
        return true;
    }

}
