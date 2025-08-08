### 方法说明
```java
onTrackEvents(String eventName, String data)
```
1. **eventName**: 事件名称
2. **data**: 参数（JSON格式），字段如下：
   ```json
   {
     "oaid": "",
     "imei": "ADC95638CF0D43E48579D323E5D4C179",
     "idfa": "47debe54e4abd54a",
     "userNo": "BU160002125",
     "type": "普通账号登陆"
   }
   ```

### 事件参数表格
| eventName    | 事件描述   | 字段名称      | 字段说明     | 类型   | 备注                                   |
|--------------|------------|---------------|--------------|--------|----------------------------------------|
| ta_register  | 账号注册   | register_type | 注册方式     | 文本   |                                        |
| ta_login     | 账号登录   | -             | -            | -      |                                        |
| ta_logout    | 账号登出   | -             | -            | -      |                                        |
| init_order   | 发起订单   | currency_type | 币种         | 文本   | CNY、USD、JPY等（支持汇率换算）        |
|              |            | payment_type  | 付款方式     | 文本   | 如：微信、支付宝                       |
|              |            | order_id      | 订单ID       | 文本   |                                        |
|              |            | pay_amount    | 支付金额     | 数值   |                                        |
|              |            | payment_name  | 商品名称     | 文本   |                                        |
| ta_payment   | 充值成功   | order_id      | 订单ID       | 文本   |                                        |
|              |            | payment_name  | 商品名称     | 文本   |                                        |
|              |            | payment_num   | 购买数量     | 数值   |                                        |
|              |            | payment_type  | 付款方式     | 文本   | 如：微信、支付宝                       |
|              |            | currency_type | 币种         | 文本   | CNY、USD、JPY等（支持汇率换算）        |
|              |            | pay_amount    | 付款金额     | 数值   |                                        |