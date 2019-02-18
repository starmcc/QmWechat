package com.qm.code.wechat.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/15 15:01
 * @Description 微信请求工具
 */
public class QmWechatHttpUtils {

    // 提交方式
    private final static String POST = "post";
    private final static String GET = "get";
    // 默认字符编码
    private final static String ENCODING = "UTF-8";


    /**
     * get提交，指定字符编码
     *
     * @param url
     * @param params
     * @param encoding
     * @return
     */
    public static String sendGet(String url, Map<String, Object> params, String encoding) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        if (params != null) {
            // 处理参数
            HttpEntity entity = handleParam(params, encoding);
            try {
                String paramStr = EntityUtils.toString(entity);
                get = new HttpGet(url + "?" + paramStr);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        CloseableHttpResponse response = null;
        String content = null;
        try {
            response = httpClient.execute(get);
            content = EntityUtils.toString(response.getEntity(), encoding);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(response, httpClient);
        }
        System.out.println(content);
        return content;
    }

    /**
     * get提交，使用默认字符编码UTF-8
     *
     * @param url
     * @param params
     * @return
     */
    public static String sendGet(String url, Map<String, Object> params) {
        return sendGet(url, params, ENCODING);
    }

    /**
     * 处理参数
     *
     * @param params
     * @return
     */
    private static HttpEntity handleParam(Map<String, Object> params, String encoding) {
        List<NameValuePair> pList = new ArrayList<NameValuePair>();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            Object value = params.get(key);
            try {
                // 处理数组
                Object[] objs = (Object[]) value;
                for (Object obj : objs) {
                    pList.add(new BasicNameValuePair(key, obj.toString()));
                }
            } catch (Exception e) {
                // 处理普通类型
                pList.add(new BasicNameValuePair(key, value.toString()));
            }
        }
        UrlEncodedFormEntity uefEntity = null;
        try {
            uefEntity = new UrlEncodedFormEntity(pList, encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uefEntity;
    }


    /**
     * 关闭
     *
     * @param response
     * @param httpClient
     */
    private static void close(CloseableHttpResponse response, CloseableHttpClient httpClient) {
        try {
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
