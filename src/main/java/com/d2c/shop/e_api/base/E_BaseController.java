package com.d2c.shop.e_api.base;

import cn.hutool.crypto.digest.DigestUtil;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.modules.core.model.ShopDO;
import com.d2c.shop.modules.core.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class E_BaseController {

    @Autowired
    public HttpServletRequest request;
    @Autowired
    private ShopService shopService;

    @ModelAttribute
    public void checkSign() {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> params.put(k, v[0]));
        ShopDO shopDO = shopService.getById(params.get("shopId"));
        Asserts.notNull("商户ID不正确，请仔细检查", shopDO);
        String timestamp = params.get("timestamp");
        Asserts.notNull("时间戳不能为空", timestamp);
        // 验证签名
        String sign = params.get("sign");
        Asserts.notNull("签名不能为空", sign);
        String signContent = this.getSignContent(params);
        String signature = DigestUtil.md5Hex(signContent + shopDO.getSecret());
        Asserts.eq(sign, signature, "签名不正确，请仔细检查");
    }

    private String getSignContent(Map<String, String> params) {
        if (params == null) {
            return null;
        } else {
            params.remove("sign");
            StringBuffer content = new StringBuffer();
            List<String> keys = new ArrayList(params.keySet());
            Collections.sort(keys);
            for (int i = 0; i < keys.size(); ++i) {
                String key = String.valueOf(keys.get(i));
                String value = String.valueOf(params.get(key));
                content.append((i == 0 ? "" : "&") + key + "=" + value);
            }
            return content.toString();
        }
    }

}
