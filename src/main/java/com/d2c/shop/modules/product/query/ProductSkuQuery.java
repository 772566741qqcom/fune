package com.d2c.shop.modules.product.query;

import com.d2c.shop.common.api.annotation.Condition;
import com.d2c.shop.common.api.base.BaseQuery;
import com.d2c.shop.common.api.emuns.ConditionEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author BaiCai
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductSkuQuery extends BaseQuery {

    @Condition(condition = ConditionEnum.EQ)
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;
    @Condition(condition = ConditionEnum.EQ)
    @ApiModelProperty(value = "商品ID")
    private Long productId;
    @Condition(condition = ConditionEnum.EQ)
    @ApiModelProperty(value = "来源ID")
    private Long sourceId;
    @Condition(condition = ConditionEnum.EQ)
    @ApiModelProperty(value = "外部编码")
    private String externalSn;

}
