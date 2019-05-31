package com.d2c.shop.b_api;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.d2c.shop.b_api.base.B_BaseController;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.PageModel;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.member.model.DraweeDO;
import com.d2c.shop.modules.member.query.DraweeQuery;
import com.d2c.shop.modules.member.service.DraweeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Cai
 */
@Api(description = "付款人业务")
@RestController
@RequestMapping("/b_api/drawee")
public class B_DraweeController extends B_BaseController {

    @Autowired
    private DraweeService draweeService;

    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R<Page<DraweeDO>> list(PageModel page) {
        DraweeQuery query = new DraweeQuery();
        query.setShopId(loginKeeperHolder.getLoginShopId());
        Page<DraweeDO> pager = (Page<DraweeDO>) draweeService.page(page, QueryUtil.buildWrapper(query));
        return Response.restResult(pager, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "根据ID查询")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R<DraweeDO> select(@PathVariable Long id) {
        DraweeQuery query = new DraweeQuery();
        query.setId(id);
        query.setShopId(loginKeeperHolder.getLoginShopId());
        DraweeDO drawee = draweeService.getOne(QueryUtil.buildWrapper(query));
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, drawee);
        return Response.restResult(drawee, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "新增")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public R<DraweeDO> insert(@RequestBody DraweeDO drawee) {
        drawee.setShopId(loginKeeperHolder.getLoginShopId());
        draweeService.save(drawee);
        return Response.restResult(drawee, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R<DraweeDO> update(@RequestBody DraweeDO drawee) {
        Asserts.eq(drawee.getShopId(), loginKeeperHolder.getLoginShopId(), "您不是本店店员");
        draweeService.updateById(drawee);
        return Response.restResult(draweeService.getById(drawee.getId()), ResultCode.SUCCESS);
    }

    @ApiOperation(value = "通过ID删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public R delete(@PathVariable Long id) {
        DraweeDO drawee = draweeService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, drawee);
        Asserts.eq(drawee.getShopId(), loginKeeperHolder.getLoginShopId(), "您不是本店店员");
        draweeService.removeById(id);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}
