package com.d2c.shop.modules.order.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.api.base.extension.BaseExcelCtrl;
import com.d2c.shop.modules.order.model.AllotItemDO;
import com.d2c.shop.modules.order.query.AllotItemQuery;
import com.d2c.shop.modules.order.service.AllotItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BaiCai
 */
@Api(description = "调拨明细管理")
@RestController
@RequestMapping("/back/allot_item")
public class AllotItemController extends BaseExcelCtrl<AllotItemDO, AllotItemQuery> {

    @Autowired
    private AllotItemService allotItemService;

    @ApiOperation(value = "调拨明细发货")
    @RequestMapping(value = "/deliver", method = RequestMethod.POST)
    public R deliver(Long id, String logisticsCom, String logisticsNum) {
        AllotItemDO allotItem = allotItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, allotItem);
        Asserts.eq(allotItem.getStatus(), AllotItemDO.StatusEnum.AGREE.name(), "调拨明细状态异常");
        AllotItemDO entity = new AllotItemDO();
        entity.setId(id);
        entity.setStatus(AllotItemDO.StatusEnum.DELIVER.name());
        entity.setLogisticsCom(logisticsCom);
        entity.setLogisticsNum(logisticsNum);
        allotItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "处理差异")
    @RequestMapping(value = "/handle/difference", method = RequestMethod.POST)
    public R handleDifference(Long id) {
        AllotItemDO allotItem = allotItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, allotItem);
        Asserts.eq(allotItem.getStatus(), AllotItemDO.StatusEnum.RECEIVE.name(), "调拨明细状态异常");
        allotItemService.doHandleDifference(allotItem);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "取消差异")
    @RequestMapping(value = "/cancel/difference", method = RequestMethod.POST)
    public R cancelDifference(Long id) {
        AllotItemDO allotItem = allotItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, allotItem);
        Asserts.eq(allotItem.getStatus(), AllotItemDO.StatusEnum.RECEIVE.name(), "调拨明细状态异常");
        allotItemService.doCancelDifference(allotItem);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}
