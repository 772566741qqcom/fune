package com.d2c.shop.modules.product.model.support;

import com.d2c.shop.modules.core.model.ShopDO;
import com.d2c.shop.modules.product.model.ProductDO;
import com.d2c.shop.modules.product.model.ProductSkuDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Cai
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "调拨SKU数据")
public class AllotSkuBean extends ProductSkuDO {

    @ApiModelProperty(value = "调拨记录ID")
    private Long allotSkuId;
    @ApiModelProperty(value = "调拨结算库存")
    private Integer settleStock;
    @ApiModelProperty(value = "来源店铺ID")
    private Long fromShopId;
    @ApiModelProperty(value = "来源店铺")
    private ShopDO shop;
    @ApiModelProperty(value = "调拨商品")
    private ProductDO product;

}
