## SaaS平台签名认证

 #### 对接准备：管理员分配 ［shopId］［shopSecret］
 
 #### 相关参数：
 
| 参数 | 类型 |描述 |
| ---- | ---- | ---- |
| shopId | String | 平台ID |
| shopSecret | String |  授权密钥 |
| timestamp | long |  时间戳(精确到毫秒)  |
| sign | String |  请求签名（签名方式:MD5） |
| 业务参数 |  |  业务参数(具体查看下面接口说明) |

 #### 注意事项：
 　　请贵司平台自己做好用户信息的真实性验证，平台无法对资料的真实性进行核对，默认接收贵司平台传递的任何参数，并根据这些参数生成相关数据。
 
 #### 签名方式：
 　　以上字段除去sign，按照参数名的字典顺序排列，以如下形式拼接成字符串，然后加上shopSecret字符串，将整个字符串MD5得到sign。
   
 ```
 示例-商品sku修改库存
 认证参数
 [shopId=1111166059554922498]
 [shopSecret=1234567890]
 
 业务参数
 [externalSn=sku123123]
 [stock=10]
 
 1.字典排列
 externalSn=sku123123&shopId=1111166059554922498&stock=10&timestamp=1559025660432
 
 2.加上appSecret
 externalSn=sku123123&shopId=1111166059554922498&stock=10&timestamp=15590256604321234567890
 
 3.最后MD5
 sign=df818d1e9c39786b0db99e076e7711f6
 ```
 
 #### JAVA签名示例代码：
 ```
    public String getSignContent(Map<String, String> params) {
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
    
    String sign = DigestUtil.md5Hex(this.getSignContent(PARAMS_MAP) + SHOP_SECRET);
 ```
## 订单查询接口

#### 接口地址：https://fune.store/e_api/order/list

#### 接口方式：POST（application/x-www-form-urlencoded）

#### 请求参数：
 
| 参数 | 类型 |描述 |
| ---- | ---- | ---- |
| sn | String | 订单编号 |
| createDateL | String |  订单创建开始时间（yyyy-MM-dd HH:mm:ss） |
| createDateR | String |  订单创建结束时间（yyyy-MM-dd HH:mm:ss） |
| modifyDateL | String |  订单修改开始时间（yyyy-MM-dd HH:mm:ss） |
| modifyDateR | String |  订单修改结束时间（yyyy-MM-dd HH:mm:ss） |

#### 返回成功示例
ps:不建议直接把将返回的json数据直接转为对象，随着业务的增长会造成新的字段数据。
```
{
    "code": 1,
    "data": {
        "records": [
            {
                "id": 1133251379750334465,
                "createDate": "2019-05-28 13:59:18",
                "createMan": "15156694204",
                "modifyDate": "2019-05-28 13:59:32",
                "modifyMan": null,
                "deleted": 0,
                "offline": 0,
                "memberId": 1109006507986309124,
                "memberAccount": "15156694222",
                "province": "北京市",
                "city": "北京市",
                "district": "东城区",
                "address": "人民路173号",
                "name": "张",
                "mobile": "15156694222",
                "shopId": 1133194204239740922,
                "shopName": "椋树服饰",
                "sn": "T1133251379712888832",
                "type": "NORMAL",
                "status": "PAID",
                "expireDate": "2019-05-28 15:59:18",
                "productAmount": 0.02,
                "couponId": null,
                "couponAmount": 0,
                "crowdId": null,
                "payAmount": 0.02,
                "paymentType": "WX_PAY",
                "paymentSn": "4200000294201905280554744380",
                "typeName": "普通",
                "statusName": "已付款",
                "paymentTypeName": "微信支付",
                "crowdGroup": null,
                "orderItemList": [
                    {
                        "id": 1133251380148793346,
                        "createDate": "2019-05-28 13:59:18",
                        "createMan": "15156694222",
                        "modifyDate": "2019-05-28 14:40:04",
                        "modifyMan": "15150000000",
                        "deleted": 0,
                        "offline": 0,
                        "shopId": 1133194204239740930,
                        "shopName": "椋树服饰",
                        "memberId": 1109006507986309122,
                        "memberAccount": "15156694222",
                        "productId": 1133242600736518146,
                        "productSkuId": 1133242601130782721,
                        "quantity": 1,
                        "standard": "38",
                        "productName": "平底鞋",
                        "productPic": "/app/f/19/05/27/5f79775be1a0409ac23916c7ca3c23d7",
                        "orderId": 1133251379750334465,
                        "orderSn": "T1133251379712888832",
                        "type": "NORMAL",
                        "status": "REFUNDED",
                        "virtual": 0,
                        "paymentType": "WX_PAY",
                        "paymentSn": "4200000294201905280554744380",
                        "productPrice": 0.02,
                        "realPrice": 0.02,
                        "payAmount": 0.02,
                        "couponWeightingAmount": 0,
                        "crowdId": null,
                        "logisticsCode": null,
                        "logisticsCom": null,
                        "logisticsNum": null,
                        "afterType": 1,
                        "afterDate": "2019-05-28 14:34:20",
                        "afterPic": "/2019/05/28/063417b5725f13dfafb4c79ba245732ecc1315.jpg",
                        "afterMemo": "帅哥哥",
                        "afterQuantity": 1,
                        "afterAmount": 0.02,
                        "oldStatus": "WAIT_DELIVER",
                        "reshipLogisticsCode": null,
                        "reshipLogisticsCom": null,
                        "reshipLogisticsNum": null,
                        "reshipName": null,
                        "reshipMobile": null,
                        "reshipAddress": null,
                        "refuseMemo": null,
                        "typeName": "普通",
                        "statusName": "退款完成",
                        "paymentTypeName": "微信支付",
                        "product": null,
                        "crowdGroup": null
                    }
                ],
                "couponList": [],
                "expireMinute": 120
            }
        ],
        "total": 1,
        "size": 10,
        "current": 1,
        "searchCount": true,
        "pages": 1
    },
    "msg": "操作成功"
}
```

## 订单发货接口

#### 接口地址：https://fune.store/e_api/order/delivery

#### 接口方式：POST（application/x-www-form-urlencoded）

#### 请求参数：
 
| 参数 | 类型 |描述 |
| ---- | ---- | ---- |
| orderItemId | long | 订单明细ID |
| logisticsCom | String |  物流公司 |
| logisticsNum | String |  物流单号 |

#### 返回成功示例

{"code":1,"data":null,"msg":"操作成功"}

## 修改SKU库存接口

#### 接口地址：https://fune.store/e_api/product/stock

#### 接口方式：POST（application/x-www-form-urlencoded）

#### 请求参数：
 
| 参数 | 类型 |描述 |
| ---- | ---- | ---- |
| externalSn | String | 外部编码(新建商品时填入) |
| stock | int |  库存数量 |

#### 返回成功示例

{"code":1,"data":null,"msg":"操作成功"}

## 新增商品接口

#### 接口地址：https://fune.store/e_api/product/add

#### 接口方式：POST（application/x-www-form-urlencoded）

#### 请求参数：

| 参数 | 类型 |描述 |
| ---- | ---- | ---- |
| name | String | 商品名称 |
| pic | String | 商品图片(多个图片请逗号分隔,图片必须是包含http的完整链接,且可以外网访问,平台将以此为依据对图片进行转存) |
| price | BigDecimal | 商品价格 |
| categoryId | long | 商品品类 |
| classifyId | long | 商品分类 |
| status | int | 状态 1上架,0下架 |
| stock | int | 商品库存 |
| skuLists | String | 商品明细(JSONArray字符串 结构看下表) |


#### 商品明细skuLists

| 参数 | 类型 |描述 |
| ---- | ---- | ---- |
| standard | String | 商品规格 |
| sellPrice | BigDecimal | 销售价 |
| stock | int | 库存 |
| externalSn | String | 外部编码(请保证唯一性) |

#### 返回成功示例
```
{
    "code": 1,
    "data": {
        "id": 1133612448180379650,
        "createDate": "2019-05-29 13:54:03",
        "createMan": null,
        "modifyDate": null,
        "modifyMan": null,
        "deleted": 0,
        "shopId": 1133194204239740930,
        "sourceId": null,
        "sn": null,
        "name": "测试",
        "pic": "/2019/05/29/8c3d3cb0092e53e22ebe58d10b4e34a9,/2019/05/29/4b8c707570ee33f3056503bca4ce9367",
        "video": null,
        "supplyPrice": null,
        "price": 100,
        "profit": null,
        "stock": 1,
        "freight": null,
        "brandId": null,
        "categoryId": 123123,
        "classifyId": 123123,
        "description": null,
        "status": 1,
        "buyout": null,
        "allot": null,
        "virtual": 0,
        "crowd": null,
        "crowdPrice": null,
        "crowdStartDate": null,
        "crowdEndDate": null,
        "crowdGroupTime": null,
        "crowdGroupNum": null,
        "couponId": null,
        "brand": null,
        "category": null,
        "classify": null,
        "standard": null,
        "skuList": [
            {
                "id": 1133612448272654337,
                "createDate": "2019-05-29 13:54:03",
                "createMan": null,
                "modifyDate": null,
                "modifyMan": null,
                "deleted": 0,
                "shopId": 1133194204239740930,
                "sourceId": null,
                "productId": 1133612448180379650,
                "standard": "123123",
                "supplyPrice": null,
                "sellPrice": 100,
                "status": 1,
                "virtual": 0,
                "stock": 1,
                "warnStock": null,
                "externalSn": "sku123123"
            }
        ],
        "firstPic": "/2019/05/29/8c3d3cb0092e53e22ebe58d10b4e34a9",
        "targetId": 1133612448180379650,
        "info": "100"
    },
    "msg": "操作成功"
}
```
