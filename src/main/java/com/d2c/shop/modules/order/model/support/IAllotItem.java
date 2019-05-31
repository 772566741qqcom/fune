package com.d2c.shop.modules.order.model.support;

/**
 * @author Cai
 */
public interface IAllotItem {

    Long getShopId();

    Long getShopKeeperId();

    String getShopKeeperAccount();

    Long getProductId();

    Long getProductSkuId();

    Integer getQuantity();

    String getStandard();

    String getProductName();

    String getProductPic();

}
