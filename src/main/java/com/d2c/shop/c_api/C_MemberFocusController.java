package com.d2c.shop.c_api;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.d2c.shop.c_api.base.C_BaseController;
import com.d2c.shop.common.api.PageModel;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.core.model.ShopDO;
import com.d2c.shop.modules.core.model.support.IShop;
import com.d2c.shop.modules.core.service.ShopService;
import com.d2c.shop.modules.member.model.MemberFocusDO;
import com.d2c.shop.modules.member.query.MemberFocusQuery;
import com.d2c.shop.modules.member.service.MemberFocusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cai
 */
@Api(description = "会员关注业务")
@RestController
@RequestMapping("/c_api/member_focus")
public class C_MemberFocusController extends C_BaseController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private MemberFocusService memberFocusService;

    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R<Page<IShop>> list(PageModel page) {
        MemberFocusQuery query = new MemberFocusQuery();
        query.setMemberId(loginMemberHolder.getLoginId());
        Page<MemberFocusDO> relationPager = (Page<MemberFocusDO>) memberFocusService.page(page, QueryUtil.buildWrapper(query));
        List<Long> shopIds = new ArrayList<>();
        relationPager.getRecords().forEach(r -> shopIds.add(r.getShopId()));
        if (shopIds.size() == 0) {
            return Response.restResult(new Page<IShop>(), ResultCode.SUCCESS);
        }
        List<ShopDO> shops = (List<ShopDO>) shopService.listByIds(shopIds);
        Page<IShop> shopPager = new Page<>();
        BeanUtil.copyProperties(relationPager, shopPager, "records", "optimizeCountSql");
        List<IShop> iShops = new ArrayList<>();
        iShops.addAll(shops);
        shopPager.setRecords(iShops);
        return Response.restResult(shopPager, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "新增")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public R<MemberFocusDO> insert(@RequestBody MemberFocusDO memberFocus) {
        memberFocus.setMemberId(loginMemberHolder.getLoginId());
        MemberFocusQuery query = new MemberFocusQuery();
        query.setMemberId(memberFocus.getMemberId());
        query.setShopId(memberFocus.getShopId());
        memberFocusService.remove(QueryUtil.buildWrapper(query));
        memberFocusService.save(memberFocus);
        return Response.restResult(memberFocus, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public R<MemberFocusDO> delete(@RequestBody MemberFocusDO memberFocus) {
        memberFocus.setMemberId(loginMemberHolder.getLoginId());
        MemberFocusQuery query = new MemberFocusQuery();
        query.setMemberId(memberFocus.getMemberId());
        query.setShopId(memberFocus.getShopId());
        memberFocusService.remove(QueryUtil.buildWrapper(query));
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}
