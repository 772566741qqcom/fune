package com.d2c.shop.modules.member.controller;

import com.d2c.shop.common.api.base.BaseCtrl;
import com.d2c.shop.modules.member.model.DraweeDO;
import com.d2c.shop.modules.member.query.DraweeQuery;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BaiCai
 */
@Api(description = "付款人管理")
@RestController
@RequestMapping("/back/drawee")
public class DraweeController extends BaseCtrl<DraweeDO, DraweeQuery> {

}
