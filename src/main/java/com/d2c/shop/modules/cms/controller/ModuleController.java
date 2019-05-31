package com.d2c.shop.modules.cms.controller;

import com.d2c.shop.common.api.base.BaseCtrl;
import com.d2c.shop.common.api.base.BaseQuery;
import com.d2c.shop.modules.cms.model.ModuleDO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "运营内容管理")
@RestController
@RequestMapping("/back/module")
public class ModuleController extends BaseCtrl<ModuleDO, BaseQuery> {

}
