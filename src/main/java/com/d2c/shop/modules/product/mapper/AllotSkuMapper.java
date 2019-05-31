package com.d2c.shop.modules.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.d2c.shop.modules.product.model.AllotSkuDO;
import com.d2c.shop.modules.product.model.support.AllotSkuBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author BaiCai
 */
public interface AllotSkuMapper extends BaseMapper<AllotSkuDO> {

    int doDeductStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    List<AllotSkuBean> doListAllot(@Param("toShopId") Long toShopId);

    List<AllotSkuBean> doSettleAllot(@Param("fromShopId") Long fromShopId, @Param("toShopId") Long toShopId);

}