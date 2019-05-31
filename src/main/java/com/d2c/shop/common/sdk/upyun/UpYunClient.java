package com.d2c.shop.common.sdk.upyun;

import cn.hutool.core.date.DateUtil;
import com.d2c.shop.common.sdk.upyun.core.UpException;
import com.d2c.shop.common.sdk.upyun.core.UpYun;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

@Component
public class UpYunClient {

    //根目录
    private static final String DIR_ROOT = "/";
    // 运行前先设置好以下三个参数
    private static String BUCKET_NAME;
    private static String OPERATOR_NAME;
    private static String OPERATOR_PWD;
    private static UpYun upyun = null;

    public static void main(String[] args) {
        UpYunClient upYunClient = new UpYunClient();
        upYunClient.writeNetFile("https://img.52z.com/upload/news/image/20171110/20171110111736_77675.jpg");
    }

    /**
     * 网络图片转存到upyun
     *
     * @param netUrl
     * @throws IOException
     * @throws UpException
     */
    public String writeNetFile(String netUrl) {
        upyun = new UpYun(BUCKET_NAME, OPERATOR_NAME, OPERATOR_PWD);
        String filePath = DIR_ROOT + DateUtil.format(new Date(), "yyyy/MM/dd") + "/" + UpYun.md5(netUrl);
        try {
            java.net.URL url = new URL(netUrl);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            boolean result = upyun.writeFile(filePath, dataInputStream, true, null);
            System.out.println(filePath + " 上传" + isSuccess(result));
            return filePath;
        } catch (IOException | UpException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传文件
     *
     * @throws IOException
     */
    public String writeFile(String picPath) {
        upyun = new UpYun(BUCKET_NAME, OPERATOR_NAME, OPERATOR_PWD);
        // 要传到upyun后的文件路径
        String filePath = DIR_ROOT + DateUtil.format(new Date(), "yyyy/MM/dd") + UpYun.md5(picPath);
        // 本地待上传的图片文件
        try {
            File file = new File(picPath);
            // 设置待上传文件的 Content-MD5 值
            // 如果又拍云服务端收到的文件MD5值与用户设置的不一致，将回报 406 NotAcceptable 错误
            upyun.setContentMD5(UpYun.md5(file));
            // 上传文件，并自动创建父级目录（最多10级）
            boolean result = upyun.writeFile(filePath, file, true);
            System.out.println(filePath + " 上传" + isSuccess(result));
            return filePath;
        } catch (IOException | UpException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String isSuccess(boolean result) {
        return result ? " 成功" : " 失败";
    }

    @Value("${shop.upyun.bucket-name}")
    public void setBUCKET_NAME(String BUCKET_NAME) {
        UpYunClient.BUCKET_NAME = BUCKET_NAME;
    }

    @Value("${shop.upyun.operator-name}")
    public void setOPERATOR_NAME(String OPERATOR_NAME) {
        UpYunClient.OPERATOR_NAME = OPERATOR_NAME;
    }

    @Value("${shop.upyun.operator-pwd}")
    public void setOPERATOR_PWD(String OPERATOR_PWD) {
        UpYunClient.OPERATOR_PWD = OPERATOR_PWD;
    }

}
