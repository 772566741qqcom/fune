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

/**
 * @author Cai
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("O_PURCH_ITEM")
@ApiModel(description = "采购明细表")
public class PurchItemDO extends BaseDelDO {

    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "采购单ID")
    private Long purchId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "采购单号")
    private String purchSn;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "状态")
    private String status;
    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "发货方店铺ID")
    private Long fromShopId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "发货方店铺名")
    private String fromShopName;
    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "收货方店铺ID")
    private Long toShopId;
    @ApiModelProperty(value = "目标ID")
    private Long targetId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品ID")
    private Long productId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品SKU的ID")
    private Long productSkuId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品数量")
    private Integer quantity;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品规格")
    private String standard;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品名称")
    private String productName;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品图片")
    private String productPic;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "商品价格")
    private BigDecimal productPrice;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "供货价")
    private BigDecimal supplyPrice;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "明细总价")
    private BigDecimal totalAmount;
    @ApiModelProperty(value = "物流代码")
    private String logisticsCode;
    @ApiModelProperty(value = "物流公司")
    private String logisticsCom;
    @ApiModelProperty(value = "物流单号")
    private String logisticsNum;
    @ApiModelProperty(value = "实收数量")
    private Integer actualQuantity;
    @ApiModelProperty(value = "差异标识 0,1")
    private Integer difference;
    @ApiModelProperty(value = "支付方式")
    private String paymentType;
    @ApiModelProperty(value = "支付流水")
    private String paymentSn;
    @TableField(exist = false)
    @ApiModelProperty(value = "状态名")
    private String statusName;

    public String getStatusName() {
        if (StrUtil.isBlank(status)) return "";
        return PurchItemDO.StatusEnum.valueOf(status).getDescription();
    }

    public enum StatusEnum {
        //
        WAIT_PAY("待付款"), PAID("已付款"), DELIVER("已发货"), RECEIVE("已入库"), CLOSE("已关闭");
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
