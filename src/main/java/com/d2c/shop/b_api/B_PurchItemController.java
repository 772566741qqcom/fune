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
import com.d2c.shop.modules.order.model.PurchDO;
import com.d2c.shop.modules.order.model.PurchItemDO;
import com.d2c.shop.modules.order.query.PurchItemQuery;
import com.d2c.shop.modules.order.service.PurchItemService;
import com.d2c.shop.modules.order.service.PurchService;
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
@Api(description = "采购明细业务")
@RestController
@RequestMapping("/b_api/purch_item")
public class B_PurchItemController extends B_BaseController {

    @Autowired
    private PurchService purchService;
    @Autowired
    private PurchItemService purchItemService;

    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R<Page<PurchItemDO>> list(PageModel page, PurchItemQuery query) {
        query.setToShopId(loginKeeperHolder.getLoginShopId());
        Page<PurchItemDO> pager = (Page<PurchItemDO>) purchItemService.page(page, QueryUtil.buildWrapper(query));
        return Response.restResult(pager, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "根据ID查询")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R<PurchDO> select(@PathVariable Long id) {
        PurchItemDO purchItem = purchItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, purchItem);
        PurchDO purch = purchService.getById(purchItem.getPurchId());
        purch.getPurchItemList().add(purchItem);
        return Response.restResult(purch, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "采购明细发货")
    @RequestMapping(value = "/deliver", method = RequestMethod.POST)
    public R deliver(Long id, String logisticsCom, String logisticsNum) {
        PurchItemDO purchItem = purchItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, purchItem);
        Asserts.eq(purchItem.getStatus(), PurchItemDO.StatusEnum.PAID.name(), "调拨明细状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(purchItem.getFromShopId(), keeper.getShopId(), "您不是本店店员");
        PurchItemDO entity = new PurchItemDO();
        entity.setId(id);
        entity.setStatus(PurchItemDO.StatusEnum.DELIVER.name());
        entity.setLogisticsCom(logisticsCom);
        entity.setLogisticsNum(logisticsNum);
        purchItemService.updateById(entity);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "采购明细入库")
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public R deliver(Long id, Integer actualQuantity) {
        PurchItemDO purchItem = purchItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, purchItem);
        Asserts.eq(purchItem.getStatus(), PurchItemDO.StatusEnum.DELIVER.name(), "调拨明细状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(purchItem.getToShopId(), keeper.getShopId(), "您不是本店店员");
        purchItem.setActualQuantity(actualQuantity);
        if (!purchItem.getQuantity().equals(actualQuantity)) {
            purchItem.setDifference(1);
        }
        Long productId = purchItemService.doReceive(purchItem);
        return Response.restResult(productId, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "处理差异")
    @RequestMapping(value = "/handle/difference", method = RequestMethod.POST)
    public R handleDifference(Long id) {
        PurchItemDO purchItem = purchItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, purchItem);
        Asserts.eq(purchItem.getStatus(), PurchItemDO.StatusEnum.RECEIVE.name(), "采购明细状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        purchItemService.doHandleDifference(purchItem);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "取消差异")
    @RequestMapping(value = "/cancel/difference", method = RequestMethod.POST)
    public R cancelDifference(Long id) {
        PurchItemDO purchItem = purchItemService.getById(id);
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, purchItem);
        Asserts.eq(purchItem.getStatus(), PurchItemDO.StatusEnum.RECEIVE.name(), "采购明细状态异常");
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        purchItemService.doCancelDifference(purchItem);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}
