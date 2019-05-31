package com.d2c.shop.modules.order.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.d2c.shop.common.api.annotation.Assert;
import com.d2c.shop.common.api.annotation.Prevent;
import com.d2c.shop.common.api.base.extension.BaseDelDO;
import com.d2c.shop.common.api.emuns.AssertEnum;
import com.d2c.shop.modules.member.model.MemberCouponDO;
import com.d2c.shop.modules.member.model.support.IAddress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author BaiCai
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("O_ORDER")
@ApiModel(description = "订单表")
public class OrderDO extends BaseDelDO implements IAddress {

    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "线上线下")
    private Integer offline;
    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "会员ID")
    private Long memberId;
    @Excel(name = "会员账号")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "会员账号")
    private String memberAccount;
    @Excel(name = "省份")
    @ApiModelProperty(value = "省份")
    private String province;
    @Excel(name = "城市")
    @ApiModelProperty(value = "城市")
    private String city;
    @Excel(name = "区县")
    @ApiModelProperty(value = "区县")
    private String district;
    @Excel(name = "地址")
    @ApiModelProperty(value = "地址")
    private String address;
    @Excel(name = "姓名")
    @ApiModelProperty(value = "姓名")
    private String name;
    @Excel(name = "手机")
    @ApiModelProperty(value = "手机")
    private String mobile;
    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;
    @Excel(name = "店铺")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "店铺名")
    private String shopName;
    @Excel(name = "订单号")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "订单号")
    private String sn;
    @Excel(name = "类型", replace = {"普通_NORMAL", "拼团_CROWD"})
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "类型")
    private String type;
    @Excel(name = "状态", replace = {"待付款_WAIT_PAY", "已付款_PAID", "交易成功_SUCCESS", "交易关闭_CLOSED"})
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "状态")
    private String status;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "过期时间")
    private Date expireDate;
    @Excel(name = "商品总价")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品总价")
    private BigDecimal productAmount;
    @ApiModelProperty(value = "优惠券ID")
    private Long couponId;
    @Excel(name = "优惠券折减")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "优惠券折减")
    private BigDecimal couponAmount;
    @ApiModelProperty(value = "拼团团ID")
    private Long crowdId;
    @Excel(name = "实际支付")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "实际支付")
    private BigDecimal payAmount;
    @Excel(name = "支付方式", replace = {"支付宝_ALI_PAY", "微信支付_WX_PAY"})
    @ApiModelProperty(value = "支付方式")
    private String paymentType;
    @Excel(name = "支付流水")
    @ApiModelProperty(value = "支付流水")
    private String paymentSn;
    @TableField(exist = false)
    @ApiModelProperty(value = "类型名")
    private String typeName;
    @TableField(exist = false)
    @ApiModelProperty(value = "状态名")
    private String statusName;
    @TableField(exist = false)
    @ApiModelProperty(value = "支付方式名")
    private String paymentTypeName;
    @TableField(exist = false)
    @ApiModelProperty(value = "拼团团组")
    private CrowdGroupDO crowdGroup;
    @TableField(exist = false)
    @ExcelCollection(name = "订单明细", orderNum = "90")
    @ApiModelProperty(value = "订单明细列表")
    private List<OrderItemDO> orderItemList = new ArrayList<>();
    @TableField(exist = false)
    @ApiModelProperty(value = "可用优惠券列表")
    private List<MemberCouponDO> couponList = new ArrayList<>();

    public String getTypeName() {
        if (StrUtil.isBlank(type)) return "";
        return TypeEnum.valueOf(type).getDescription();
    }

    public String getStatusName() {
        if (StrUtil.isBlank(status)) return "";
        return StatusEnum.valueOf(status).getDescription();
    }

    public String getPaymentTypeName() {
        if (StrUtil.isBlank(paymentType)) return "";
        return PaymentDO.PaymentTypeEnum.valueOf(paymentType).getDescription();
    }

    public int getExpireMinute() {
        if (StrUtil.isBlank(type)) return TypeEnum.NORMAL.expireMinutes;
        switch (TypeEnum.valueOf(type)) {
            case NORMAL:
                return TypeEnum.NORMAL.expireMinutes;
            case CROWD:
                return TypeEnum.CROWD.expireMinutes;
            default:
                return TypeEnum.NORMAL.expireMinutes;
        }
    }

    public enum TypeEnum {
        //
        NORMAL("普通", 120), CROWD("拼团", 20);
        //
        private String description;
        private Integer expireMinutes;

        TypeEnum(String description, Integer expired) {
            this.description = description;
            this.expireMinutes = expired;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public enum StatusEnum {
        //
        WAIT_PAY("待付款"), PAID("已付款"),
        SUCCESS("交易成功"), CLOSED("交易关闭");
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
