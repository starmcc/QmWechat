package com.qm.code.wechat.config;

import com.qm.frame.basic.util.PropertiesUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/15 15:46
 * @Description 微信API配置类
 */
public class QmWechatConfig {

    /**
     * 配置文件读取工具
     */
    private static final Properties PRO = getProperties();

    // ===============================================公众号-华丽的分割线=================================================
    /**
     * 公众号APPID
     */
    public static final String OFFICIAL_ACCOUNTS_APPID = PRO.getProperty("official_accounts_appid");
    /**
     *公众号SECRET
     */
    public static final String OFFICIAL_ACCOUNTS_SECRET = PRO.getProperty("official_accounts_secret");
    /**
     * 公众号授权请求接口URL
     */
    public static final String OFFICIAL_ACCOUNTS_AUTH_URI = PRO.getProperty("official_accounts_auth_uri");
    /**
     * 授权回调地址
     */
    public static final String OFFICIAL_ACCOUNTS_REDIRECT_URI = PRO.getProperty("official_accounts_redirect_uri");
    /**
     * 公众号商户ID
     */
    public static final String OFFICIAL_ACCOUNTS_MCH_ID = PRO.getProperty("official_accounts_mch_id");

    /**
     * 公众号商户支付key
     */
    public static final String OFFICIAL_ACCOUNTS_MCH_KEY = PRO.getProperty("official_accounts_mch_key");

    /**
     * 公众号支付成功异步通知地址
     */
    public static final String OFFICIAL_ACCOUNTS_NOTIFY_URL = PRO.getProperty("official_accounts_notify_url");

    /**
     * 公众号商支付货币类型 CNY 人民币
     */
    public static final String OFFICIAL_ACCOUNTS_FEE_TYPE = PRO.getProperty("official_accounts_fee_type");

    /**
     * 公众号支付no_credit 上传此参数no_credit--可限制用户不能使用信用卡支付 指定支付方式
     */
    public static final String OFFICIAL_ACCOUNTS_LIMIT_PAY = PRO.getProperty("official_accounts_limit_pay");

    /**
     * Y，传入Y时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效
     */
    public static final String OFFICIAL_ACCOUNTS_RECEIPT = PRO.getProperty("official_accounts_receipt");

    // ===============================================小程序-华丽的分割线=================================================

    /**
     * 小程序APPID
     */
    public static final String MINIPROGRAM_APPID = PRO.getProperty("miniprogram_appid");
    /**
     * 小程序SECRET
     */
    public static final String MINIPROGRAM_SECRET = PRO.getProperty("miniprogram_secret");
    /**
     * 小程序授权请求接口URL
     */
    public static final String MINIPROGRAM_AUTH_URI = PRO.getProperty("miniprogram_auth_uri");

    /**
     * 小程序商户ID
     */
    public static final String MINIPROGRAM_MCH_ID = PRO.getProperty("miniprogram_mch_id");

    /**
     * 小程序商户支付key
     */
    public static final String MINIPROGRAM_MCH_KEY = PRO.getProperty("miniprogram_mch_key");

    /**
     * 小程序支付成功异步通知地址
     */
    public static final String MINIPROGRAM_NOTIFY_URL = PRO.getProperty("miniprogram_notify_url");

    /**
     * 小程序商支付货币类型 CNY 人民币
     */
    public static final String MINIPROGRAM_FEE_TYPE = PRO.getProperty("miniprogram_fee_type");

    /**
     * 小程序支付no_credit 上传此参数no_credit--可限制用户不能使用信用卡支付 指定支付方式
     */
    public static final String MINIPROGRAM_LIMIT_PAY = PRO.getProperty("miniprogram_limit_pay");

    /**
     * Y，传入Y时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效
     */
    public static final String MINIPROGRAM_RECEIPT = PRO.getProperty("miniprogram_receipt");



    /**
     * 获取getProperties
     * @return
     */
    private static final Properties getProperties(){
        try {
            String frameFileName = "qmwx.properties";
            // 读取properties文件,使用InputStreamReader字符流防止文件中出现中文导致乱码
            InputStreamReader inStream = new InputStreamReader
                    (PropertiesUtil.class.getClassLoader().getResourceAsStream(frameFileName),"UTF-8");
            Properties proTemp = new Properties();
            proTemp.load(inStream);
            inStream.close();
            return proTemp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
