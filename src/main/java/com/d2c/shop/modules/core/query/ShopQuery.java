package com.d2c.shop.modules.core.query;

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
public class ShopQuery extends BaseQuery {

    @Condition(condition = ConditionEnum.EQ)
    @ApiModelProperty(value = "店铺名称")
    private String name;
    @Condition(condition = ConditionEnum.EQ)
    @ApiModelProperty(value = "商户名称")
    private String enterprise;
    @Condition(condition = ConditionEnum.EQ)
    @ApiModelProperty(value = "认证 1,0")
    private Integer authenticate;
    @Condition(condition = ConditionEnum.EQ)
    @ApiModelProperty(value = "电话")
    private String telephone;
    @Condition(condition = ConditionEnum.NE, field = "id")
    @ApiModelProperty(value = "排除ID")
    private Long excludeId;
    @Condition(condition = ConditionEnum.EQ)
    @ApiModelProperty(value = "状态 1,0")
    private Integer status;

}
