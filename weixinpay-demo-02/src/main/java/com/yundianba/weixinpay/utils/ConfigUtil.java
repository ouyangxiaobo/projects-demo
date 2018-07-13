package com.yundianba.weixinpay.utils;

import org.springframework.context.annotation.Configuration;

import javax.naming.ConfigurationException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public interface ConfigUtil {


            String APP_ID="wx632c8f211f8122c6";   // 服务号的应用ID
            String MCH_ID="1497984412";   // 商户号
            String API_KEY="sbNCm1JnevqI36LrEaxFwcaT0hkGxFnC";  // API密钥
            String UFDOOER_URL= "https://api.mch.weixin.qq.com/pay/unifiedorder" ;   //回调地址
            String NOTIFY_URL= "http://pic.chenjunbo.xin/payment/result" ;   //回调地址
            String CREATE_IP=  "114.242.26.51" ;   //IP地址
            String APP_SECRET="xxxxxxxxx";     // 服务号的应用密钥
            String TOKEN="xxxxxxxxx";       // 服务号的配置token


            String SIGN_TYPE="xxxxxxxxx";    // 签名加密方式
            String CERT_PATH="xxxxxxxxx";   //微信支付证书


}
