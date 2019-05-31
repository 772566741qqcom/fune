package com.d2c.shop.modules.order.model;

import cn.hutool.core.util.StrUtil;
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
import java.util.Date;

/**
 * @author Cai
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("O_SETTLEMENT")
@ApiModel(description = "结算单表")
public class SettlementDO extends BaseDelDO {

    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;
    @Prevent
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "发起店员ID")
    private Long shopKeeperId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "发起店员账号")
    private String shopKeeperAccount;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "来源店铺ID")
    private Long fromShopId;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "结算单号")
    private String sn;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "状态")
    private String status;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "货品小计")
    private String goods;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "实际支付")
    private BigDecimal payAmount;
    @ApiModelProperty(value = "支付方式")
    private String paymentType;
    @ApiModelProperty(value = "支付流水")
    private String paymentSn;
    @ApiModelProperty(value = "过期时间")
    private Date expireDate;

    public String getStatusName() {
        if (StrUtil.isBlank(status)) return "";
        return OrderDO.StatusEnum.valueOf(status).getDescription();
    }

    public String getPaymentTypeName() {
        if (StrUtil.isBlank(paymentType)) return "";
        return PaymentDO.PaymentTypeEnum.valueOf(paymentType).getDescription();
    }

    public int getExpireMinute() {
        return 120;
    }

    public enum StatusEnum {
        //
        WAIT_PAY("待付款"), PAID("已付款");
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
