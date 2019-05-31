package com.d2c.shop.modules.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.d2c.shop.common.api.annotation.Assert;
import com.d2c.shop.common.api.annotation.Prevent;
import com.d2c.shop.common.api.base.extension.BaseDelDO;
import com.d2c.shop.common.api.emuns.AssertEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Cai
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("CORE_BANK_CARD")
@ApiModel(description = "店铺银行卡表")
public class BankCardDO extends BaseDelDO {

    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "银行名称")
    private String bankType;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "开户行地区")
    private String bankAddress;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "开户行名称")
    private String bankName;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "银行卡号")
    private String cardNum;

}
