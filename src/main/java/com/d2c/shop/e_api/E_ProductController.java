package com.d2c.shop.e_api;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.api.R;
import com.d2c.shop.common.api.Asserts;
import com.d2c.shop.common.api.Response;
import com.d2c.shop.common.api.ResultCode;
import com.d2c.shop.common.sdk.upyun.UpYunClient;
import com.d2c.shop.common.utils.QueryUtil;
import com.d2c.shop.e_api.base.E_BaseController;
import com.d2c.shop.modules.product.model.ProductDO;
import com.d2c.shop.modules.product.model.ProductSkuDO;
import com.d2c.shop.modules.product.query.ProductSkuQuery;
import com.d2c.shop.modules.product.service.ProductService;
import com.d2c.shop.modules.product.service.ProductSkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(description = "ERP-商品业务接口")
@RestController
@RequestMapping("/e_api/product")
public class E_ProductController extends E_BaseController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private UpYunClient upYunClient;

    @ApiOperation(value = "修改库存")
    @RequestMapping(value = "/stock", method = RequestMethod.POST)
    public R stock(String externalSn, Integer stock) {
        ProductSkuQuery query = new ProductSkuQuery();
        query.setExternalSn(externalSn);
        ProductSkuDO productSku = productSkuService.getOne(QueryUtil.buildWrapper(query));
        Asserts.notNull("SKU不存在", productSku);
        Integer distance = stock - productSku.getStock();
        ProductSkuDO entity = new ProductSkuDO();
        entity.setId(productSku.getId());
        entity.setStock(stock);
        productSkuService.updateById(entity);
        productService.doReturnStock(productSku.getProductId(), distance);
        return Response.restResult(null, ResultCode.SUCCESS);
    }

    @ApiOperation(value = "发布商品")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public R add(ProductDO product, String skuLists) {
        JSONArray array = JSONArray.parseArray(skuLists);
        List<ProductSkuDO> skuDOList = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            ProductSkuDO sku = array.getObject(i, ProductSkuDO.class);
            sku.setShopId(product.getShopId());
            skuDOList.add(sku);
        }
        product.setSkuList(skuDOList);
        product.setVirtual(0);
        ProductDO productDO = productService.doCreate(product);
        processNetPic(productDO);
        return Response.restResult(product, ResultCode.SUCCESS);
    }

    /**
     * 处理第三方的图片
     *
     * @param productDO
     */
    private void processNetPic(ProductDO productDO) {
        if (StringUtils.isNotBlank(productDO.getPic()) && productDO.getPic().contains("http")) {
            String[] pics = productDO.getPic().split(",");
            StringBuffer stringBuffer = new StringBuffer("");
            for (String pic : pics) {
                if (StringUtils.isNotBlank(pic) && pic.contains("http")) {
                    pic = upYunClient.writeNetFile(pic);
                    stringBuffer.append(pic).append(",");
                }
            }
            if (stringBuffer.length() > 1) {
                ProductDO productDO1 = new ProductDO();
                productDO1.setId(productDO.getId());
                productDO1.setPic(stringBuffer.substring(0, stringBuffer.length() - 1).toString());
                productService.updateById(productDO1);
                productDO.setPic(productDO1.getPic());
            }
        }
    }

}
