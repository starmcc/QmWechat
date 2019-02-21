# 微信API工具包

> 微信授权、支付、公众号等相关整合工具包

## 更新时间

> 2019年2月21日15:32:25

## 工具使用说明

> QmWechat

### 方法

> 小程序授权登录

```java
QmMiniProgramResult authMiniProgram(String jsCode);
// jsCode 小程序授权返回的jsCode，请结合微信官方api文档查阅。
```

> 公众号授权

```java
void authOfficialAccounts(HttpServletResponse response) throws IOException;
```

> 微信公众号获取用户信息

```java
WeChatUserInfo getInfoOfficialAccounts(HttpServletRequest request);
```

> 统一下单

```java
Map<String, Object> pay(QmWxPayInfo qmWxPayInfo, int type) throws IOException;
// type 支付类型 1=公众号 2=小程序
// return 前端调起支付所需信息的map集合
```

> 支付回调解析方法

```java
Map<String, String> parsePayCallBack(HttpServletRequest request, HttpServletResponse response)throws IOException, JDOMException;
// 请结合微信官方api文档查阅。
```

### 配置文件

> 在`resources`中增设配置文件`qmwx.properties`

```properties
# 微信API主要配置文件

# =========公众号API=========
# APPID
official_accounts_appid=
# 密钥
official_accounts_secret=
# 授权官方地址
official_accounts_auth_uri=https://open.weixin.qq.com/connect/oauth2/authorize
# 授权回调地址
official_accounts_redirect_uri=
# 公众号商户ID
official_accounts_mch_id=
# 公众号商户KEY
official_accounts_mch_key=
# 公众号商户支付货币类型 CNY 人民币
official_accounts_fee_type=CNY
# 公众号商户支付成功异步通知地址
official_accounts_notify_url=
# no_credit 上传此参数no_credit--可限制用户不能使用信用卡支付 指定支付方式
official_accounts_limit_pay=
# Y，传入Y时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效
official_accounts_receipt=

# =========小程序API=========
# APPID
miniprogram_appid=
# 密钥
miniprogram_secret=
# 授权官方地址
miniprogram_auth_uri=https://api.weixin.qq.com/sns/jscode2session
# 小程序商户ID
miniprogram_mch_id=
# 小程序商户KEY
miniprogram_mch_key=
# 小程序商户支付货币类型 CNY 人民币
miniprogram_fee_type=CNY
# 小程序商户支付成功异步通知地址
miniprogram_notify_url=
# no_credit 上传此参数no_credit--可限制用户不能使用信用卡支付 指定支付方式
miniprogram_limit_pay=
# Y，传入Y时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效
miniprogram_receipt=
```

