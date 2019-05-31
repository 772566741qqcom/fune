package com.d2c.shop.modules.core.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.d2c.shop.common.api.annotation.Assert;
import com.d2c.shop.common.api.base.extension.BaseDelDO;
import com.d2c.shop.common.api.emuns.AssertEnum;
import com.d2c.shop.modules.core.model.support.IShop;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@TableName("CORE_SHOP")
@ApiModel(description = "店铺表")
public class ShopDO extends BaseDelDO implements IShop {

    @Excel(name = "名称")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "Logo")
    private String logo;
    @ApiModelProperty(value = "Banner")
    private String banner;
    @Excel(name = "简介")
    @ApiModelProperty(value = "简介")
    private String summary;
    @Excel(name = "公告")
    @ApiModelProperty(value = "公告")
    private String notice;
    @Excel(name = "经营范围")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "经营范围")
    private String scope;
    @Excel(name = "地址")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "地址")
    private String address;
    @Excel(name = "电话")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "电话")
    private String telephone;
    @Excel(name = "微信")
    @ApiModelProperty(value = "微信")
    private String wechat;
    @Excel(name = "营业时间")
    @ApiModelProperty(value = "营业时间")
    private String hours;
    @Excel(name = "退货地址")
    @ApiModelProperty(value = "退货地址")
    private String returnAddress;
    @Excel(name = "认证", replace = {"已认证_1", "未认证_0"})
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "认证 1,0")
    private Integer authenticate;
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "状态 1,0")
    private Integer status;
    @Excel(name = "有效期")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "有效期")
    private Date validDate;
    @Excel(name = "企业名称")
    @ApiModelProperty(value = "企业名称")
    private String enterprise;
    @Excel(name = "营业执照号")
    @ApiModelProperty(value = "营业执照号")
    private String licenseNum;
    @ApiModelProperty(value = "营业执照图")
    private String licensePic;
    @Excel(name = "法人姓名")
    @ApiModelProperty(value = "法人姓名")
    private String corporationName;
    @Excel(name = "法人身份证号")
    @ApiModelProperty(value = "法人身份证号")
    private String corporationCard;
    @ApiModelProperty(value = "法人身份证图")
    private String corporationPic;
    @Excel(name = "资金余额")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "资金余额")
    private BigDecimal balance;
    @Excel(name = "资金余额")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "冻结余额")
    private BigDecimal frozen;
    @Excel(name = "保证金额")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "保证金额")
    private BigDecimal deposit;
    @Excel(name = "充值金额")
    @Assert(type = AssertEnum.NOT_NULL)
    @ApiModelProperty(value = "充值金额")
    private BigDecimal recharge;
    @ApiModelProperty(value = "密钥")
    private String secret;
    @TableField(exist = false)
    @ApiModelProperty(value = "是否关注 1,0")
    private Integer focus;

    @JsonIgnore
    public String getSecret() {
        return secret;
    }

    @JsonProperty
    public void setSecret(String secret) {
        this.secret = secret;
    }

}
