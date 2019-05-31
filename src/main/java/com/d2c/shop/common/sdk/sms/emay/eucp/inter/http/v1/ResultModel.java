package com.d2c.shop.common.sdk.sms.emay.eucp.inter.http.v1;

public class ResultModel {

    private String code;
    private String result;

    public ResultModel(String code, String result) {
        this.code = code;
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
