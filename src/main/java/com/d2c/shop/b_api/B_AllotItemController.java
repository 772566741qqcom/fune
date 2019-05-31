package com.d2c.shop.b_api;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.d2c.shop.b_api.base.B_BaseController;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.PageModel;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.core.model.ShopkeeperDO;
import com.d2c.shop.modules.order.model.AllotDO;
import com.d2c.shop.modules.order.model.AllotItemDO;
import com.d2c.shop.modules.order.query.AllotItemQuery;
import com.d2c.shop.modules.order.service.AllotItemService;
import com.d2c.shop.modules.order.service.AllotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cai
 */
@Api(description = "调拨明细业务")
@RestController
@RequestMapping("/b_api/allot_item")
public class B_AllotItemController extends B_BaseController {

    @Autowired
    private AllotService allotService;
    @Autowired
    private AllotItemService allotItemService;

    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R<Page<AllotItemDO>> list(PageModel page, AllotItemQuery query) {
        query.setToShopId(loginKeeperHolder.getLoginShopId());
        Page<AllotItemDO> pager = (Page<AllotItemDO>) allotItemService.page(page, QueryUtil.buildWrapper(query));
        return Response.restResult(pager, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "根据ID查询")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R<AllotDO> select(@PathVariable Long id) {
        AllotItemDO allotItem = allotItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, allotItem);
        AllotDO allot = allotService.getById(allotItem.getAllotId());
        allot.getAllotItemList().add(allotItem);
        return Response.restResult(allot, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "调拨明细发货")
    @RequestMapping(value = "/deliver", method = RequestMethod.POST)
    public R deliver(Long id, String logisticsCom, String logisticsNum) {
        AllotItemDO allotItem = allotItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, allotItem);
        Asserts.eq(allotItem.getStatus(), AllotItemDO.StatusEnum.AGREE.name(), "调拨明细状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(allotItem.getFromShopId(), keeper.getShopId(), "您不是本店店员");
        AllotItemDO entity = new AllotItemDO();
        entity.setId(id);
        entity.setStatus(AllotItemDO.StatusEnum.DELIVER.name());
        entity.setLogisticsCom(logisticsCom);
        entity.setLogisticsNum(logisticsNum);
        allotItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "调拨明细入库")
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public R receive(Long id, Integer actualQuantity) {
        AllotItemDO allotItem = allotItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, allotItem);
        Asserts.eq(allotItem.getStatus(), AllotItemDO.StatusEnum.DELIVER.name(), "调拨明细状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(allotItem.getToShopId(), keeper.getShopId(), "您不是本店店员");
        allotItem.setActualQuantity(actualQuantity);
        if (!allotItem.getQuantity().equals(actualQuantity)) {
            allotItem.setDifference(1);
        }
        Long productId = allotItemService.doReceive(allotItem);
        return Response.restResult(productId, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "处理差异")
    @RequestMapping(value = "/handle/difference", method = RequestMethod.POST)
    public R handleDifference(Long id) {
        AllotItemDO allotItem = allotItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, allotItem);
        Asserts.eq(allotItem.getStatus(), AllotItemDO.StatusEnum.RECEIVE.name(), "调拨明细状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        allotItemService.doHandleDifference(allotItem);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "取消差异")
    @RequestMapping(value = "/cancel/difference", method = RequestMethod.POST)
    public R cancelDifference(Long id) {
        AllotItemDO allotItem = allotItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, allotItem);
        Asserts.eq(allotItem.getStatus(), AllotItemDO.StatusEnum.RECEIVE.name(), "调拨明细状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        allotItemService.doCancelDifference(allotItem);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}
