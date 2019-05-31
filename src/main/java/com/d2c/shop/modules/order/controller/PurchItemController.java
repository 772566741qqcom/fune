package com.d2c.shop.modules.order.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.api.base.extension.BaseExcelCtrl;
import com.d2c.shop.modules.order.model.PurchItemDO;
import com.d2c.shop.modules.order.query.PurchItemQuery;
import com.d2c.shop.modules.order.service.PurchItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BaiCai
 */
@Api(description = "采购明细管理")
@RestController
@RequestMapping("/back/purch_item")
public class PurchItemController extends BaseExcelCtrl<PurchItemDO, PurchItemQuery> {

    @Autowired
    private PurchItemService purchItemService;

    @ApiOperation(value = "采购明细发货")
    @RequestMapping(value = "/deliver", method = RequestMethod.POST)
    public R deliver(Long id, String logisticsCom, String logisticsNum) {
        PurchItemDO purchItem = purchItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, purchItem);
        Asserts.eq(purchItem.getStatus(), PurchItemDO.StatusEnum.PAID.name(), "调拨明细状态异常");
        PurchItemDO entity = new PurchItemDO();
        entity.setId(id);
        entity.setStatus(PurchItemDO.StatusEnum.DELIVER.name());
        entity.setLogisticsCom(logisticsCom);
        entity.setLogisticsNum(logisticsNum);
        purchItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "处理差异")
    @RequestMapping(value = "/handle/difference", method = RequestMethod.POST)
    public R handleDifference(Long id) {
        PurchItemDO purchItem = purchItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, purchItem);
        Asserts.eq(purchItem.getStatus(), PurchItemDO.StatusEnum.RECEIVE.name(), "采购明细状态异常");
        purchItemService.doHandleDifference(purchItem);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "取消差异")
    @RequestMapping(value = "/cancel/difference", method = RequestMethod.POST)
    public R cancelDifference(Long id) {
        PurchItemDO purchItem = purchItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, purchItem);
        Asserts.eq(purchItem.getStatus(), PurchItemDO.StatusEnum.RECEIVE.name(), "采购明细状态异常");
        purchItemService.doCancelDifference(purchItem);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}
