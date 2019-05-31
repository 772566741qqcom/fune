package com.d2c.shop.modules.product.controller;

import com.d2c.shop.common.api.base.BaseCtrl;
import com.d2c.shop.modules.product.model.FreightDO;
import com.d2c.shop.modules.product.query.FreightQuery;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BaiCai
 */
@Api(description = "运费管理")
@RestController
@RequestMapping("/back/freight")
public class FreightController extends BaseCtrl<FreightDO, FreightQuery> {

}
