package com.d2c.shop.modules.order.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.api.base.extension.BaseExcelCtrl;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.order.model.AllotDO;
import com.d2c.shop.modules.order.model.AllotItemDO;
import com.d2c.shop.modules.order.query.AllotItemQuery;
import com.d2c.shop.modules.order.query.AllotQuery;
import com.d2c.shop.modules.order.service.AllotItemService;
import com.d2c.shop.modules.order.service.AllotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BaiCai
 */
@Api(description = "调拨单管理")
@RestController
@RequestMapping("/back/allot")
public class AllotController extends BaseExcelCtrl<AllotDO, AllotQuery> {

    @Autowired
    private AllotService allotService;
    @Autowired
    private AllotItemService allotItemService;

    @ApiOperation(value = "同意调拨")
    @RequestMapping(value = "/agree", method = RequestMethod.POST)
    public R agree(Long id) {
        AllotDO allot = allotService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, allot);
        Asserts.eq(allot.getStatus(), AllotDO.StatusEnum.APPLY.name(), "调拨单状态异常");
        AllotDO entity = new AllotDO();
        entity.setId(id);
        entity.setStatus(AllotDO.StatusEnum.AGREE.name());
        allotService.updateById(entity);
        AllotItemDO allotItem = new AllotItemDO();
        allotItem.setStatus(AllotItemDO.StatusEnum.AGREE.name());
        AllotItemQuery itemQuery = new AllotItemQuery();
        itemQuery.setAllotId(id);
        allotItemService.update(allotItem, QueryUtil.buildWrapper(itemQuery));
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "拒绝调拨")
    @RequestMapping(value = "/refuse", method = RequestMethod.POST)
    public R refuse(Long id) {
        AllotDO allot = allotService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, allot);
        Asserts.eq(allot.getStatus(), AllotDO.StatusEnum.APPLY.name(), "调拨单状态异常");
        allotService.doClose(allot);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}
