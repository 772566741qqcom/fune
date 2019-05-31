package com.d2c.shop.modules.member.controller;

import com.d2c.shop.common.api.base.BaseCtrl;
import com.d2c.shop.modules.member.model.FeedbackDO;
import com.d2c.shop.modules.member.query.FeedbackQuery;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BaiCai
 */
@Api(description = "意见反馈管理")
@RestController
@RequestMapping("/back/feedback")
public class FeedbackController extends BaseCtrl<FeedbackDO, FeedbackQuery> {

}
