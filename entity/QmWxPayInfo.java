package com.qm.code.wechat.entity;

import java.util.Date;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/15 17:35
 * @Description 微信支付信息实体
 */
public class QmWxPayInfo {

    private String out_trade_no; // 订单号
    private String body; // 商品描述
    private String detail; // 商品详情
    private int total_fee; // 价格
    private String openid; // 用户openid
    private String attach; //  附加数据
    private Date time_start; // 交易开始时间
    private Date time_expire; // 交易结束时间
    private String product_id; // 商品ID
    private String device_info; // 设备号

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Date getTime_start() {
        return time_start;
    }

    public void setTime_start(Date time_start) {
        this.time_start = time_start;
    }

    public Date getTime_expire() {
        return time_expire;
    }

    public void setTime_expire(Date time_expire) {
        this.time_expire = time_expire;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }
}
