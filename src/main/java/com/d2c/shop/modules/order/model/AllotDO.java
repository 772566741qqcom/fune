package com.d2c.shop.modules.order.model;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.d2c.shop.common.api.annotation.Assert;
import com.d2c.shop.common.api.annotation.Prevent;
import com.d2c.shop.common.api.base.extension.BaseDelDO;
import com.d2c.shop.common.api.emuns.AssertEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cai
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("O_ALLOT")
@ApiModel(description = "调拨单表")
public class AllotDO extends BaseDelDO {

    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "调拨单号")
    private String sn;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "状态")
    private String status;
    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "发起店员ID")
    private Long shopKeeperId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "发起店员账号")
    private String shopKeeperAccount;
    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "发货方店铺ID")
    private Long fromShopId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "发货方收货人")
    private String fromName;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "发货方联系电话")
    private String fromMobile;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "发货方收货地址")
    private String fromAddress;
    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "收货方店铺ID")
    private Long toShopId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "收货方收货人")
    private String toName;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "收货方联系电话")
    private String toMobile;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "收货方收货地址")
    private String toAddress;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品总价")
    private BigDecimal productAmount;
    @TableField(exist = false)
    @ApiModelProperty(value = "状态名")
    private String statusName;
    @TableField(exist = false)
    @ApiModelProperty(value = "调拨明细列表")
    private List<AllotItemDO> allotItemList = new ArrayList<>();

    public String getStatusName() {
        if (StrUtil.isBlank(status)) return "";
        return AllotDO.StatusEnum.valueOf(status).getDescription();
    }

    public enum StatusEnum {
        //
        APPLY("待审核"), AGREE("已审核"), CLOSE("已关闭");
        //
        private String description;

        StatusEnum(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

}
