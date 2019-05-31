package com.d2c.shop.modules.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.d2c.shop.common.api.base.BaseService;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.common.utils.ReflectUtil;
import com.d2c.shop.modules.core.service.ShopService;
import com.d2c.shop.modules.order.mapper.AllotItemMapper;
import com.d2c.shop.modules.order.model.AllotItemDO;
import com.d2c.shop.modules.order.service.AllotItemService;
import com.d2c.shop.modules.product.model.AllotSkuDO;
import com.d2c.shop.modules.product.model.ProductDO;
import com.d2c.shop.modules.product.model.ProductSkuDO;
import com.d2c.shop.modules.product.query.AllotSkuQuery;
import com.d2c.shop.modules.product.query.ProductQuery;
import com.d2c.shop.modules.product.query.ProductSkuQuery;
import com.d2c.shop.modules.product.service.AllotSkuService;
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
public class AllotItemServiceImpl extends BaseService<AllotItemMapper, AllotItemDO> implements AllotItemService {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private AllotSkuService allotSkuService;

    @Override
    @Transactional
    public Long doReceive(AllotItemDO allotItem) {
        // 返还库存
        ProductQuery pq = new ProductQuery();
        pq.setSourceId(allotItem.getProductId());
        pq.setShopId(allotItem.getToShopId());
        ProductDO target = productService.getOne(QueryUtil.buildWrapper(pq));
        if (target == null) {
            target = new ProductDO();
            ProductDO source = productService.getById(allotItem.getProductId());
            BeanUtil.copyProperties(source, target, ReflectUtil.clearPublicFields());
            target.setCrowdPrice(null);
            target.setCrowdStartDate(null);
            target.setCrowdEndDate(null);
            target.setCrowdGroupTime(null);
            target.setCrowdGroupNum(null);
            target.setCouponId(null);
            target.setCategoryId(null);
            target.setSourceId(allotItem.getProductId());
            target.setShopId(allotItem.getToShopId());
            target.setCrowd(0);
            target.setStatus(0);
            target.setAllot(0);
            target.setBuyout(0);
            target.setStock(0);
            productService.save(target);
        }
        ProductSkuQuery sq = new ProductSkuQuery();
        sq.setSourceId(allotItem.getProductSkuId());
        sq.setShopId(allotItem.getToShopId());
        ProductSkuDO target2 = productSkuService.getOne(QueryUtil.buildWrapper(sq));
        if (target2 == null) {
            target2 = new ProductSkuDO();
            ProductSkuDO source2 = productSkuService.getById(allotItem.getProductSkuId());
            BeanUtil.copyProperties(source2, target2, ReflectUtil.clearPublicFields());
            target2.setSourceId(allotItem.getProductSkuId());
            target2.setProductId(target.getId());
            target2.setShopId(allotItem.getToShopId());
            target2.setStatus(0);
            target2.setStock(0);
            productSkuService.save(target2);
        }
        productSkuService.doReturnStock(target2.getId(), target.getId(), allotItem.getActualQuantity());
        // 更新单据
        AllotItemDO entity = new AllotItemDO();
        entity.setId(allotItem.getId());
        entity.setStatus(AllotItemDO.StatusEnum.RECEIVE.name());
        entity.setActualQuantity(allotItem.getActualQuantity());
        entity.setDifference(allotItem.getDifference());
        entity.setTargetId(target.getId());
        this.updateById(entity);
        // 入库记录
        AllotSkuQuery query = new AllotSkuQuery();
        query.setSkuId(target2.getId());
        query.setFromShopId(allotItem.getFromShopId());
        query.setToShopId(allotItem.getToShopId());
        AllotSkuDO allotSku = allotSkuService.getOne(QueryUtil.buildWrapper(query));
        if (allotSku == null) {
            allotSku = new AllotSkuDO();
            allotSku.setFromShopId(allotItem.getFromShopId());
            allotSku.setToShopId(allotItem.getToShopId());
            allotSku.setSkuId(target2.getId());
            allotSku.setStock(allotItem.getActualQuantity());
            allotSkuService.save(allotSku);
        } else {
            allotSku.setStock(allotSku.getStock() + allotItem.getActualQuantity());
            allotSkuService.updateById(allotSku);
        }
        return target.getId();
    }

    @Override
    @Transactional
    public boolean doHandleDifference(AllotItemDO allotItem) {
        AllotItemDO entity = new AllotItemDO();
        entity.setId(allotItem.getId());
        entity.setDifference(8);
        this.updateById(entity);
        Integer diff = allotItem.getQuantity() - allotItem.getActualQuantity();
        // 退还差异保证金
        shopService.updateDeposit(allotItem.getToShopId(), allotItem.getSupplyPrice().multiply(new BigDecimal(diff)), BigDecimal.ZERO);
        // 返还库存
        productSkuService.doReturnStock(allotItem.getProductSkuId(), allotItem.getProductId(), diff);
        return true;
    }

    @Override
    @Transactional
    public boolean doCancelDifference(AllotItemDO allotItem) {
        AllotItemDO entity = new AllotItemDO();
        entity.setId(allotItem.getId());
        entity.setDifference(0);
        this.updateById(entity);
        Integer diff = allotItem.getQuantity() - allotItem.getActualQuantity();
        // 返还库存
        ProductSkuQuery query = new ProductSkuQuery();
        query.setSourceId(allotItem.getProductSkuId());
        query.setShopId(allotItem.getToShopId());
        ProductSkuDO target = productSkuService.getOne(QueryUtil.buildWrapper(query));
        productSkuService.doReturnStock(target.getId(), target.getProductId(), diff);
        return true;
    }

}
