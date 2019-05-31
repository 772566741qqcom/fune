package com.d2c.shop.b_api;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.d2c.shop.b_api.base.B_BaseController;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.PageModel;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.modules.core.model.BankCardDO;
import com.d2c.shop.modules.core.model.ShopkeeperDO;
import com.d2c.shop.modules.core.query.BankCardQuery;
import com.d2c.shop.modules.core.service.BankCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Cai
 */
@Api(description = "银行卡业务")
@RestController
@RequestMapping("/b_api/bank_card")
public class B_BankCardController extends B_BaseController {

    @Autowired
    private BankCardService bankCardService;

    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R<Page<BankCardDO>> list(PageModel page) {
        BankCardQuery query = new BankCardQuery();
        query.setShopId(loginKeeperHolder.getLoginShopId());
        Page<BankCardDO> pager = (Page<BankCardDO>) bankCardService.page(page, QueryUtil.buildWrapper(query));
        return Response.restResult(pager, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "根据ID查询")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R<BankCardDO> select(@PathVariable Long id) {
        BankCardDO bankCard = bankCardService.getById(id);
        Asserts.eq(bankCard.getShopId(), loginKeeperHolder.getLoginShopId(), "您不是本店店员");
        Asserts.notNull(ResultCode.RESPONSE_DATA_NULL, bankCard);
        return Response.restResult(bankCard, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "新增")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public R<BankCardDO> insert(@RequestBody BankCardDO bankCard) {
        bankCard.setShopId(loginKeeperHolder.getLoginShopId());
        bankCardService.save(bankCard);
        return Response.restResult(bankCard, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R<BankCardDO> update(@RequestBody BankCardDO bankCard) {
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        Asserts.eq(bankCard.getShopId(), keeper.getShopId(), "您不是本店店员");
        bankCardService.updateById(bankCard);
        return Response.restResult(bankCardService.getById(bankCard.getId()), ResultCode.SUCCESS);
    }

}
