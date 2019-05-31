package com.d2c.shop.common.sdk.sms.emay.util.http;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Http响应解析器
 *
 * @param <T> http响应数据转换后实体
 * @author Frank
 */
public interface HttpResponsePraser<T> {

    /**
     * 解析
     *
     * @param resultCode Http 结果代码
     * @param httpCode   Http链接Code
     * @param headers    Http响应头
     * @param cookies    http响应Cookies
     * @param charSet    http字符集
     * @param result     http响应数据
     */
    public T prase(HttpResultCode resultCode, int httpCode, Map<String, String> headers, List<String> cookies, String charSet, ByteArrayOutputStream outputStream);

}
