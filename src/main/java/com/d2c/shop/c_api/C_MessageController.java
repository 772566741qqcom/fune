package com.d2c.shop.c_api;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.d2c.shop.c_api.base.C_BaseController;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.sdk.sms.SmsConstant;
import com.d2c.shop.common.utils.RequestUtil;
import com.d2c.shop.modules.logger.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cai
 */
@Api(description = "短信业务")
@RestController
@RequestMapping("/c_api/message")
public class C_MessageController extends C_BaseController {

    @Autowired
    private SmsService smsService;

    @ApiOperation(value = "发送短信")
    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    public R sms(String mobile, String sign) {
        Asserts.eq(DigestUtil.md5Hex(mobile + SmsConstant.SIGN_KEY), sign, "签名错误");
        smsService.doSend(mobile, RequestUtil.getRequestIp(request));
        return Response.restResult(null, ResultCode.SUCCESS);
    }

}
