package com.d2c.shop.quartz.jobs;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.d2c.shop.common.sdk.oauth.WechatClient;
import com.d2c.shop.quartz.jobs.base.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Cai
 */
@Slf4j
@Component
public class WechatJob extends BaseJob {

    // 微信JS-API票据
    public static String TICKET_KEY = "WECHAT::js_api:";
    private static RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private WechatClient wechatClient;
    @Autowired
    private RedisTemplate redisTemplate;
    //
    @Value("${shop.profile}")
    private String profile;

    @Override
    public String getCronExpression() {
        return "0 0/30 * * * ?";
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 只允许正式环境执行
        if (profile.equals("dev") /*|| profile.equals("test")*/) {
            return;
        }
        String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + wechatClient.getAppId() + "&secret=" + wechatClient.getAppSecret();
        JSONObject result1 = restTemplate.getForObject(tokenUrl, JSONObject.class);
        String token = result1.getString("access_token");
        if (StrUtil.isBlank(token)) {
            log.error("微信token获取失败，" + result1.toJSONString());
            return;
        }
        String ticketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
                + token + "&type=jsapi";
        JSONObject result2 = restTemplate.getForObject(ticketUrl, JSONObject.class);
        String ticket = result2.getString("ticket");
        if (StrUtil.isBlank(ticket)) {
            log.error("微信ticket获取失败，" + result2.toJSONString());
            return;
        }
        redisTemplate.opsForValue().set(TICKET_KEY + wechatClient.getAppId(), ticket);
        log.info(this.getClass().getName() + " have done!");
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

}
