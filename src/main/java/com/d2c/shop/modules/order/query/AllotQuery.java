package com.d2c.shop.modules.order.query;

import com.d2c.shop.common.api.annotation.Assert;
import com.d2c.shop.common.api.annotation.Condition;
import com.d2c.shop.common.api.base.BaseQuery;
import com.d2c.shop.common.api.emuns.AssertEnum;
import com.d2c.shop.common.api.emuns.ConditionEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author BaiCai
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AllotQuery extends BaseQuery {

    @Condition(condition = ConditionEnum.EQ)
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "发货方店铺ID")
    private Long fromShopId;
    @Condition(condition = ConditionEnum.EQ)
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "收货方店铺ID")
    private Long toShopId;
    @Condition(condition = ConditionEnum.EQ)
    @ApiModelProperty(value = "状态")
    private String status;

}
