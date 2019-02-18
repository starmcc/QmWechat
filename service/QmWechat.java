package com.qm.code.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.qm.code.wechat.config.QmWechatConfig;
import com.qm.code.wechat.entity.QmWxPayInfo;
import com.qm.code.wechat.entity.WeChatUserInfo;
import com.qm.code.wechat.entity.QmMiniProgramResult;
import com.qm.code.wechat.util.QmWechatBasicUtils;
import com.qm.code.wechat.util.QmWechatHttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/15 15:04
 * @Description 微信API
 */
public class QmWechat {

    private final static Logger LOG = LoggerFactory.getLogger(QmWechat.class);


    /**
     * 小程序授权登录
     *
     * @param jsCode
     * @return
     */
    public static QmMiniProgramResult authMiniProgram(String jsCode) {
        Map<String, Object> sendGetParams = new HashMap<>();
        sendGetParams.put("APPID", QmWechatConfig.MINIPROGRAM_APPID);
        sendGetParams.put("secret", QmWechatConfig.MINIPROGRAM_SECRET);
        sendGetParams.put("js_code", jsCode);
        sendGetParams.put("grant_type", "authorization_code");
        String resStr = QmWechatHttpUtils.sendGet(QmWechatConfig.MINIPROGRAM_AUTH_URI, sendGetParams);
        JSONObject resJson = JSONObject.parseObject(resStr);
        QmMiniProgramResult qmMiniProgramResult = new QmMiniProgramResult();
        String session_key = (String) resJson.get("session_key");
        String openid = (String) resJson.get("openid");
        String unionid = (String) resJson.get("unionid");
        Integer errcode = (Integer) resJson.get("errcode");
        String errmsg = (String) resJson.get("errmsg");
        qmMiniProgramResult.setSession_key(session_key);
        qmMiniProgramResult.setOpenid(openid);
        qmMiniProgramResult.setUnionid(unionid);
        qmMiniProgramResult.setErrcode(errcode);
        qmMiniProgramResult.setErrmsg(errmsg);
        return qmMiniProgramResult;
    }

    /**
     * 公众号授权
     *
     * @param jsCode
     * @return
     */
    public static void authOfficialAccounts(HttpServletResponse response) throws IOException {
        StringBuffer url = new StringBuffer();
        url.append(QmWechatConfig.OFFICIAL_ACCOUNTS_AUTH_URI);
        url.append("?appid=" + QmWechatConfig.OFFICIAL_ACCOUNTS_APPID);
        url.append("&redirect_uri=" + URLEncoder.encode(QmWechatConfig.OFFICIAL_ACCOUNTS_REDIRECT_URI, "UTF-8")); // 重定向地址
        url.append("&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
        response.sendRedirect(url.toString());
    }


    /**
     * 微信公众号获取用户信息
     * @param request
     * @return
     */
    public static WeChatUserInfo getInfoOfficialAccounts(HttpServletRequest request) {
        try {
            String code = request.getParameter("code");
            LOG.info("◆◆◆回调Code：" + code + "◆◆◆");
            if (code == null) {
                return null;
            }
            StringBuffer url = new StringBuffer();
            url.append("https://api.weixin.qq.com/sns/oauth2/access_token");
            url.append("?appid=" + QmWechatConfig.OFFICIAL_ACCOUNTS_APPID);
            url.append("&secret=" + QmWechatConfig.OFFICIAL_ACCOUNTS_SECRET);
            url.append("&code=" + code);
            url.append("&grant_type=authorization_code");
            JSONObject jsonObject = QmWechatBasicUtils.doGetJson(url.toString());
            //微信用户信息实体类
            WeChatUserInfo weChatUserInfo = new WeChatUserInfo();
            String openId = jsonObject.getString("openid");
            if (openId == null || openId.trim().equals("")) {
                return null;
            }
            weChatUserInfo.setOpenId(openId);
            String token = jsonObject.getString("access_token");
            weChatUserInfo.setToken(token);
            StringBuffer infoUrl = new StringBuffer();
            infoUrl.append("https://api.weixin.qq.com/sns/userinfo");
            infoUrl.append("?access_token=" + token);
            infoUrl.append("&openid=" + openId);
            infoUrl.append("&lang=zh_CN");
            JSONObject resJson = QmWechatBasicUtils.doGetJson(infoUrl.toString());
            LOG.info("◆◆◆微信用户信息如下：◆◆◆\n" + resJson.toString());
            weChatUserInfo.setHeadImgUrl(resJson.getString("headimgurl"));
            weChatUserInfo.setNickName(resJson.getString("nickname"));
            weChatUserInfo.setSex(resJson.getInteger("sex"));
            weChatUserInfo.setProvince(resJson.getString("province"));
            weChatUserInfo.setCity(resJson.getString("city"));
            weChatUserInfo.setCountry(resJson.getString("country"));
            weChatUserInfo.setPrivilege(resJson.getString("privilege"));
            weChatUserInfo.setUnionid(resJson.getString("unionid"));
            return weChatUserInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 统一下单
     *
     * @param qmWxPayInfo
     * @param type        支付类型 1=公众号 2=小程序
     * @return 前端调起支付所需信息的map集合
     */
    public static Map<String, Object> pay(QmWxPayInfo qmWxPayInfo, int type) throws IOException {
        if (qmWxPayInfo == null
                || StringUtils.isEmpty(qmWxPayInfo.getBody())
                || StringUtils.isEmpty(qmWxPayInfo.getOut_trade_no())
                || qmWxPayInfo.getTotal_fee() == 0) {
            return null;
        }
        SortedMap<String, Object> parMap = joinPayMap(qmWxPayInfo, type);
        if (parMap == null) {
            return null;
        }
        // scene_info 场景信息
        // goods_tag 订单优惠标记
        LOG.info(parMap.toString());
        String sign = QmWechatBasicUtils.createSign("UTF-8", parMap, QmWechatConfig.MINIPROGRAM_MCH_KEY);
        parMap.put("sign", sign);
        LOG.info("sign:" + sign);
        String requestXML = QmWechatBasicUtils.getRequestXml(parMap);
        LOG.info(requestXML);
        String requestUri = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String result = QmWechatBasicUtils.httpsRequest(requestUri, "POST", requestXML);
        LOG.info("微信支付返回信息：\n" + result);
        Map<String, String> map = null;
        try {
            map = QmWechatBasicUtils.doXMLParse(result);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //双签
        SortedMap<String, Object> payMap = new TreeMap<String, Object>();
        payMap.put("appId", QmWechatConfig.MINIPROGRAM_APPID);
        payMap.put("package", "prepay_id=" + map.get("prepay_id"));
        payMap.put("signType", "MD5");
        payMap.put("nonceStr", QmWechatBasicUtils.getRandomString(32));
        payMap.put("timeStamp", Long.toString(System.currentTimeMillis() / 1000));
        payMap.put("paySign", QmWechatBasicUtils.createSign("UTF-8", payMap, QmWechatConfig.MINIPROGRAM_MCH_KEY));
        return payMap;
    }

    /**
     * 拼接支付所需的参数
     *
     * @param qmWxPayInfo
     * @param type
     * @return
     * @throws UnknownHostException
     */
    private static SortedMap<String, Object> joinPayMap(QmWxPayInfo qmWxPayInfo, int type) throws UnknownHostException {
        SortedMap<String, Object> parMap = new TreeMap<>();
        parMap.put("nonce_str", QmWechatBasicUtils.getRandomString(32));
        //parMap.put("body", qmWxPayInfo.getBody());
        //parMap.put("out_trade_no", qmWxPayInfo.getOut_trade_no());
        //parMap.put("total_fee", qmWxPayInfo.getTotal_fee());
        parMap.put("spbill_create_ip", InetAddress.getLocalHost().getHostAddress());
        switch (type) {
            case 1: //
                // 公众号
                parMap.put("appid", QmWechatConfig.OFFICIAL_ACCOUNTS_APPID);
                parMap.put("mch_id", QmWechatConfig.OFFICIAL_ACCOUNTS_MCH_ID);
                parMap.put("notify_url", QmWechatConfig.OFFICIAL_ACCOUNTS_NOTIFY_URL);
                parMap.put("fee_type", QmWechatConfig.OFFICIAL_ACCOUNTS_FEE_TYPE);
                if (!StringUtils.isEmpty(QmWechatConfig.OFFICIAL_ACCOUNTS_LIMIT_PAY)) {
                    parMap.put("limit_pay", QmWechatConfig.OFFICIAL_ACCOUNTS_LIMIT_PAY);
                }
                parMap.put("trade_type", "JSAPI");
                parMap.put("openid", qmWxPayInfo.getOpenid());
                break;
            case 2: // 小程序
                parMap.put("appid", QmWechatConfig.MINIPROGRAM_APPID);
                parMap.put("mch_id", QmWechatConfig.MINIPROGRAM_MCH_ID);
                parMap.put("notify_url", QmWechatConfig.MINIPROGRAM_NOTIFY_URL);
                parMap.put("fee_type", QmWechatConfig.MINIPROGRAM_FEE_TYPE);
                if (!StringUtils.isEmpty(QmWechatConfig.MINIPROGRAM_LIMIT_PAY)) {
                    parMap.put("limit_pay", QmWechatConfig.MINIPROGRAM_LIMIT_PAY);
                }
                parMap.put("openid", qmWxPayInfo.getOpenid());
                break;
            default:
                return null;
        }
        Field[] fields = qmWxPayInfo.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 开放字段权限public
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            try {
                Object value = field.get(qmWxPayInfo);
                String key = field.getName();
                parMap.put(key, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return parMap;
    }

    /**
     * 支付回调解析方法
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws JDOMException
     */
    public static Map<String, String> parsePayCallBack(HttpServletRequest request, HttpServletResponse response)
            throws IOException, JDOMException {
        StringBuffer resXml = new StringBuffer();
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String resultStr = new String(outSteam.toByteArray(), "utf-8");
        LOG.info("支付成功的回调：" + resultStr);
        Map<String, String> resultMap = QmWechatBasicUtils.doXMLParse(resultStr);
        // 下面这些参数需要的可以获取,不需要也可以不用获取
        String out_trade_no = (String) resultMap.get("out_trade_no");
        String return_code = (String) resultMap.get("return_code");
        if (return_code.equals("SUCCESS")) {
            // XML成功
            resXml.append("<xml>");
            resXml.append("<return_code><![CDATA[SUCCESS]]></return_code>");
            resXml.append("<return_msg><![CDATA[OK]]></return_msg>");
            resXml.append("</xml>");
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.toString().getBytes());
        } else {
            // 支付失败的业务逻辑
            LOG.info("支付失败");
            resultMap = null;
        }
        return resultMap;
    }


}
