package com.d2c.shop.b_api.support;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Cai
 */
@Data
@ApiModel(description = "订单请求POJO")
public class OrderRequestBeanB implements Serializable {

    @ApiModelProperty(value = "选货盒ID")
    private List<Long> packageIds;
    @ApiModelProperty(value = "SKU的ID")
    private Long skuId;
    @ApiModelProperty(value = "SKU的数量")
    private Integer quantity;
    @ApiModelProperty(value = "付款人ID")
    private Long draweeId;

}
