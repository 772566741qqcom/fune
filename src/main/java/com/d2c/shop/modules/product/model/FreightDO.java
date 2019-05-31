package com.d2c.shop.modules.product.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.d2c.shop.common.api.annotation.Assert;
import com.d2c.shop.common.api.annotation.Prevent;
import com.d2c.shop.common.api.base.BaseDO;
import com.d2c.shop.common.api.emuns.AssertEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author BaiCai
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("P_FREIGHT")
@ApiModel(description = "运费模板表")
public class FreightDO extends BaseDO {

    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "名称")
    private String name;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "公式")
    private String formula;

    public static BigDecimal calculate(Integer quantity, String formula) {
        try {
            String[] array = formula.split(":");
            if (quantity <= Integer.valueOf(array[0])) {
                return new BigDecimal(array[1]);
            } else {
                return new BigDecimal(array[1]).add(new BigDecimal(quantity - 1).multiply(new BigDecimal(array[2])));
            }
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

}
