package com.d2c.shop.modules.member.controller;

import com.d2c.shop.common.api.base.BaseCtrl;
import com.d2c.shop.modules.member.model.MemberFocusDO;
import com.d2c.shop.modules.member.query.MemberFocusQuery;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BaiCai
 */
@Api(description = "会员关注管理")
@RestController
@RequestMapping("/back/member_focus")
public class MemberFocusController extends BaseCtrl<MemberFocusDO, MemberFocusQuery> {

}
