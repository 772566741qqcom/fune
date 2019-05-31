package com.d2c.shop.b_api;

import com.baomidou.mybatisplus.extension.api.R;
import com.d2c.shop.b_api.base.B_BaseController;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.cms.model.ModuleDO;
import com.d2c.shop.modules.cms.query.ModuleQuery;
import com.d2c.shop.modules.cms.service.ModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "运营模块内容")
@RestController
@RequestMapping("/b_api/module")
public class B_ModuleController extends B_BaseController {

    @Autowired
    private ModuleService moduleService;

    @ApiOperation(value = "对应模块")
    @RequestMapping(value = "/{type}", method = RequestMethod.POST)
    public R<ModuleDO> type(@PathVariable("type") String type) {
        ModuleQuery query = new ModuleQuery();
        ModuleDO moduleDO = moduleService.getOne(QueryUtil.buildWrapper(query, false));
        return Response.restResult(moduleDO, ResultCode.SUCCESS);
    }

}
