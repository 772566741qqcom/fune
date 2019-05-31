package com.d2c.shop.modules.cms.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.d2c.shop.common.api.base.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("V_MODULE")
@ApiModel(description = "内容模块表")
public class ModuleDO extends BaseDO {

    @ApiModelProperty(value = "内容")
    private String value;

}
