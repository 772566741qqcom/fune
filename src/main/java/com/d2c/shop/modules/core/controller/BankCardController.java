package com.d2c.shop.modules.core.controller;

import com.d2c.shop.common.api.base.BaseCtrl;
import com.d2c.shop.modules.core.model.BankCardDO;
import com.d2c.shop.modules.core.query.BankCardQuery;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BaiCai
 */
@Api(description = "店铺银行卡管理")
@RestController
@RequestMapping("/back/bank_card")
public class BankCardController extends BaseCtrl<BankCardDO, BankCardQuery> {

}
