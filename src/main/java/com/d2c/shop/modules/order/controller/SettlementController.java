package com.d2c.shop.modules.order.controller;

import com.d2c.shop.common.api.base.extension.BaseExcelCtrl;
import com.d2c.shop.modules.order.model.SettlementDO;
import com.d2c.shop.modules.order.query.SettlementQuery;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BaiCai
 */
@Api(description = "结算单管理")
@RestController
@RequestMapping("/back/settlement")
public class SettlementController extends BaseExcelCtrl<SettlementDO, SettlementQuery> {

}
