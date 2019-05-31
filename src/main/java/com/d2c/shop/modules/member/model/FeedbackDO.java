package com.d2c.shop.modules.member.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.d2c.shop.common.api.annotation.Assert;
import com.d2c.shop.common.api.annotation.Prevent;
import com.d2c.shop.common.api.base.BaseDO;
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
@TableName("M_MEMBER_FEEDBACK")
@ApiModel(description = "意见反馈表")
public class FeedbackDO extends BaseDO {

    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "店员ID")
    private Long shopKeeperId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "店员账号")
    private String shopKeeperAccount;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "内容")
    private String content;

}
