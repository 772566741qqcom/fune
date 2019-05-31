package com.d2c.shop.b_api;

import com.baomidou.mybatisplus.extension.api.R;
import com.d2c.shop.b_api.base.B_BaseController;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.modules.core.model.ShopkeeperDO;
import com.d2c.shop.modules.member.model.FeedbackDO;
import com.d2c.shop.modules.member.service.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cai
 */
@Api(description = "意见反馈业务")
@RestController
@RequestMapping("/b_api/feedback")
public class B_FeedbackController extends B_BaseController {

    @Autowired
    private FeedbackService feedbackService;

    @ApiOperation(value = "新增")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public R<FeedbackDO> insert(@RequestBody FeedbackDO feedback) {
        ShopkeeperDO keeper = loginKeeperHolder.getLoginKeeper();
        feedback.setShopId(keeper.getShopId());
        feedback.setShopKeeperId(keeper.getId());
        feedback.setShopKeeperAccount(keeper.getAccount());
        feedbackService.save(feedback);
        return Response.restResult(feedback, ResultCode.SUCCESS);
    }

}
