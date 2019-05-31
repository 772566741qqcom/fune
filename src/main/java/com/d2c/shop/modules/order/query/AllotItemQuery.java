package com.d2c.shop.modules.order.query;

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
public class AllotItemQuery extends BaseQuery {

    @Condition(condition = ConditionEnum.EQ)
    @ApiModelProperty(value = "调拨单ID")
    private Long allotId;
    @Condition(condition = ConditionEnum.IN)
    @ApiModelProperty(value = "调拨单号")
    private String[] allotSn;
    @Condition(condition = ConditionEnum.EQ)
    @ApiModelProperty(value = "收货方店铺ID")
    private Long toShopId;
    @Condition(condition = ConditionEnum.EQ)
    @ApiModelProperty(value = "状态")
    private String status;
    @Condition(condition = ConditionEnum.EQ)
    @ApiModelProperty(value = "差异标识 0,1")
    private Integer difference;

}
