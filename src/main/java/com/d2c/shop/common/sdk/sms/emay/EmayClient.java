package com.d2c.shop.common.sdk.sms.emay;

import com.d2c.shop.common.sdk.sms.emay.eucp.inter.http.v1.ResultModel;
import com.d2c.shop.common.sdk.sms.emay.eucp.inter.http.v1.dto.request.SmsSingleRequest;
import com.d2c.shop.common.sdk.sms.emay.eucp.inter.http.v1.dto.response.SmsResponse;
import com.d2c.shop.common.sdk.sms.emay.util.AES;
import com.d2c.shop.common.sdk.sms.emay.util.GZIPUtils;
import com.d2c.shop.common.sdk.sms.emay.util.JsonHelper;
import com.d2c.shop.common.sdk.sms.emay.util.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cai
 */
@Component
public class EmayClient {

    private static String APP_ID;
    private static String SECRET_KEY;
    private static String HOST = "http://shmtn.b2m.cn";
    private static String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static String ENCODE = "UTF-8";

    @Value("${shop.sms.emay.app-id}")
    public void setAppId(String appId) {
        APP_ID = appId;
    }

    @Value("${shop.sms.emay.secret-key}")
    public void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    /**
     * 发送单条短信
     */
    public void sendSMS(String mobile, String content) {
        SmsSingleRequest params = new SmsSingleRequest();
        params.setContent(content);
        params.setCustomSmsId(null);
        params.setExtendedCode(null);
        params.setMobile(mobile);
        ResultModel result = request(APP_ID, SECRET_KEY, ALGORITHM, params, HOST + "/inter/sendSingleSMS", true, ENCODE);
        if ("SUCCESS".equals(result.getCode())) {
            SmsResponse response = JsonHelper.fromJson(SmsResponse.class, result.getResult());
        }
    }

    /**
     * 公共请求方法
     */
    private ResultModel request(String appId, String secretKey, String algorithm, Object content, String url, final boolean isGzip, String encode) {
        Map<String, String> headers = new HashMap<String, String>();
        HttpRequest<byte[]> request = null;
        try {
            headers.put("appId", appId);
            headers.put("encode", encode);
            String requestJson = JsonHelper.toJsonString(content);
            byte[] bytes = requestJson.getBytes(encode);
            if (isGzip) {
                headers.put("gzip", "on");
                bytes = GZIPUtils.compress(bytes);
            }
            byte[] paramBytes = AES.encrypt(bytes, secretKey.getBytes(), algorithm);
            HttpRequestParams<byte[]> params = new HttpRequestParams<byte[]>();
            params.setCharSet("UTF-8");
            params.setMethod("POST");
            params.setHeaders(headers);
            params.setParams(paramBytes);
            params.setUrl(url);
            if (url.startsWith("https://")) {
                request = new HttpsRequestBytes(params, null);
            } else {
                request = new HttpRequestBytes(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient client = new HttpClient();
        String code = null;
        String result = null;
        try {
            HttpResponseBytes res = client.service(request, new HttpResponseBytesPraser());
            if (res == null) {
                return new ResultModel(code, result);
            }
            if (res.getResultCode().equals(HttpResultCode.SUCCESS)) {
                if (res.getHttpCode() == 200) {
                    code = res.getHeaders().get("result");
                    if (code.equals("SUCCESS")) {
                        byte[] data = res.getResult();
                        data = AES.decrypt(data, secretKey.getBytes(), algorithm);
                        if (isGzip) {
                            data = GZIPUtils.decompress(data);
                        }
                        result = new String(data, encode);
                    }
                } else {
                    System.out.println("请求接口异常,请求码:" + res.getHttpCode());
                }
            } else {
                System.out.println("请求接口网络异常:" + res.getResultCode().getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResultModel re = new ResultModel(code, result);
        return re;
    }

}
