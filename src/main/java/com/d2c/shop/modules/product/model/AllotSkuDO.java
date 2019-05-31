package com.d2c.shop.modules.product.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.d2c.shop.common.api.annotation.Assert;
import com.d2c.shop.common.api.base.BaseDO;
import com.d2c.shop.common.api.emuns.AssertEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author BaiCai
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("P_ALLOT_SKU")
@ApiModel(description = "调拨SKU表")
public class AllotSkuDO extends BaseDO {

    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "店铺ID")
    private Long fromShopId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "店铺ID")
    private Long toShopId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "skuID")
    private Long skuId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "库存")
    private Integer stock;

}
