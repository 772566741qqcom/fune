package com.d2c.shop.modules.order.controller;

import com.d2c.shop.common.api.base.BaseCtrl;
import com.d2c.shop.modules.order.model.PackageDO;
import com.d2c.shop.modules.order.query.PackageQuery;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BaiCai
 */
@Api(description = "选货盒管理")
@RestController
@RequestMapping("/back/package")
public class PackageController extends BaseCtrl<PackageDO, PackageQuery> {

}
