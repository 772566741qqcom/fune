package com.d2c.shop.c_api;

import com.baomidou.mybatisplus.extension.api.R;
import com.d2c.shop.c_api.base.C_BaseController;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.modules.cms.model.ModuleDO;
import com.d2c.shop.modules.cms.service.ModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "运营内容模块")
@RestController
@RequestMapping("/c_api/module")
public class C_ModuleController extends C_BaseController {

    @Autowired
    private ModuleService moduleService;
    private String id = "";

    @ApiOperation(value = "首页")
    @RequestMapping(value = "/v1/home", method = RequestMethod.POST)
    public R<ModuleDO> home() {
        ModuleDO moduleDO = moduleService.getById(id);
        return Response.restResult(moduleDO, ResultCode.SUCCESS);
    }

}
