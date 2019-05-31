package com.d2c.shop.modules.order.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.d2c.shop.common.api.annotation.Assert;
import com.d2c.shop.common.api.annotation.Prevent;
import com.d2c.shop.common.api.base.extension.BaseDelDO;
import com.d2c.shop.common.api.emuns.AssertEnum;
import com.d2c.shop.modules.order.model.support.ITradeItem;
import com.d2c.shop.modules.product.model.ProductDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author BaiCai
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("O_ORDER_ITEM")
@ApiModel(description = "订单明细表")
public class OrderItemDO extends BaseDelDO implements ITradeItem {

    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "线上线下")
    private Integer offline;
    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "店铺名")
    private String shopName;
    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "会员ID")
    private Long memberId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "会员账号")
    private String memberAccount;
    @Excel(name = "商品ID")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品ID")
    private Long productId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品SKU的ID")
    private Long productSkuId;
    @Excel(name = "商品数量")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品数量")
    private Integer quantity;
    @Excel(name = "商品规格")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品规格")
    private String standard;
    @Excel(name = "商品名称")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品名称")
    private String productName;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品图片")
    private String productPic;
    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "订单号")
    private String orderSn;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "类型")
    private String type;
    @Excel(name = "状态", replace = {"待付款_WAIT_PAY", "已付款_PAID", "待发货_WAIT_DELIVER", "已发货_DELIVERED", "已收货_RECEIVED", "交易成功_SUCCESS", "交易关闭_CLOSED"})
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "状态")
    private String status;
    @Excel(name = "虚拟", replace = {"虚拟_1", "正常_0"})
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "虚拟 1,0")
    private Integer virtual;
    @ApiModelProperty(value = "支付方式")
    private String paymentType;
    @ApiModelProperty(value = "支付流水")
    private String paymentSn;
    @Excel(name = "商品单价")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品单价")
    private BigDecimal productPrice;
    @Excel(name = "实时单价")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "实时单价")
    private BigDecimal realPrice;
    @Excel(name = "实际支付")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "实际支付")
    private BigDecimal payAmount;
    @Excel(name = "优惠券折减")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "优惠券加权折减")
    private BigDecimal couponWeightingAmount;
    @ApiModelProperty(value = "拼团团ID")
    private Long crowdId;
    @ApiModelProperty(value = "物流代码")
    private String logisticsCode;
    @Excel(name = "物流公司")
    @ApiModelProperty(value = "物流公司")
    private String logisticsCom;
    @Excel(name = "物流单号")
    @ApiModelProperty(value = "物流单号")
    private String logisticsNum;
    @Excel(name = "售后类型", replace = {"退款_1", "退货退款_2"})
    @ApiModelProperty(value = "售后类型")
    private Integer afterType;
    @Excel(name = "售后时间", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "售后时间")
    private Date afterDate;
    @ApiModelProperty(value = "售后凭证")
    private String afterPic;
    @Excel(name = "售后理由")
    @ApiModelProperty(value = "售后理由")
    private String afterMemo;
    @Excel(name = "售后数量")
    @ApiModelProperty(value = "售后数量")
    private Integer afterQuantity;
    @Excel(name = "售后金额")
    @ApiModelProperty(value = "售后金额")
    private BigDecimal afterAmount;
    @ApiModelProperty(value = "先前状态")
    private String oldStatus;
    @ApiModelProperty(value = "退货物流代码")
    private String reshipLogisticsCode;
    @Excel(name = "退货物流公司")
    @ApiModelProperty(value = "退货物流公司")
    private String reshipLogisticsCom;
    @Excel(name = "退货物流单号")
    @ApiModelProperty(value = "退货物流单号")
    private String reshipLogisticsNum;
    @ApiModelProperty(value = "退货收货人")
    private String reshipName;
    @ApiModelProperty(value = "退货联系电话")
    private String reshipMobile;
    @ApiModelProperty(value = "退货收货地址")
    private String reshipAddress;
    @ApiModelProperty(value = "售后拒绝理由")
    private String refuseMemo;
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
    @ApiModelProperty(value = "活动商品")
    private ProductDO product;
    @TableField(exist = false)
    @ApiModelProperty(value = "拼团团组")
    private CrowdGroupDO crowdGroup;

    public String getTypeName() {
        if (StrUtil.isBlank(type)) return "";
        return OrderDO.TypeEnum.valueOf(type).getDescription();
    }

    public String getStatusName() {
        if (StrUtil.isBlank(status)) return "";
        return StatusEnum.valueOf(status).getDescription();
    }

    public String getPaymentTypeName() {
        if (StrUtil.isBlank(paymentType)) return "";
        return PaymentDO.PaymentTypeEnum.valueOf(paymentType).getDescription();
    }

    public enum StatusEnum {
        //
        WAIT_PAY("待付款"), PAID("已付款"), WAIT_DELIVER("待发货"),
        DELIVERED("已发货"), RECEIVED("已收货"), SUCCESS("交易成功"), CLOSED("交易关闭"),
        WAIT_RESHIP("退货审核"), REFUSE_RESHIP("拒绝退货"), AGREE_RESHIP("同意退货"), RESHIPED("退货发出"),
        WAIT_REFUND("退款审核"), REFUSE_REFUND("拒绝退款"), AGREE_REFUND("同意退款"), REFUNDED("退款完成");
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
