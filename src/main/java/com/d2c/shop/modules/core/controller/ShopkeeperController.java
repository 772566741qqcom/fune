package com.d2c.shop.modules.core.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.d2c.shop.common.api.PageModel;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.api.base.BaseCtrl;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.core.model.ShopDO;
import com.d2c.shop.modules.core.model.ShopkeeperDO;
import com.d2c.shop.modules.core.query.ShopkeeperQuery;
import com.d2c.shop.modules.core.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author BaiCai
 */
@Api(description = "店铺员工管理")
@RestController
@RequestMapping("/back/shopkeeper")
public class ShopkeeperController extends BaseCtrl<ShopkeeperDO, ShopkeeperQuery> {

    @Autowired
    private ShopService shopService;

    @ApiOperation(value = "分页查询数据")
    @RequestMapping(value = "/select/page", method = RequestMethod.POST)
    public R<Page<ShopkeeperDO>> selectPage(PageModel page, ShopkeeperQuery query) {
        Page<ShopkeeperDO> pager = (Page<ShopkeeperDO>) service.page(page, QueryUtil.buildWrapper(query, false));
        Set<Long> shopIds = new HashSet<>();
        for (ShopkeeperDO sk : pager.getRecords()) {
            shopIds.add(sk.getShopId());
        }
        if (shopIds.size() == 0) return Response.restResult(pager, ResultCode.SUCCESS);
        List<ShopDO> shops = (List<ShopDO>) shopService.listByIds(shopIds);
        Map<Long, ShopDO> map = new ConcurrentHashMap<>();
        for (ShopDO shop : shops) {
            map.put(shop.getId(), shop);
        }
        for (ShopkeeperDO sk : pager.getRecords()) {
            if (sk.getShopId() != null) {
                sk.setShop(map.get(sk.getShopId()));
            }
        }
        return Response.restResult(pager, ResultCode.SUCCESS);
    }

}
