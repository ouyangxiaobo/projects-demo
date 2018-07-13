package com.yundianba.weixinpay.utils;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.yundianba.weixinpay.utils.ConfigUtil.API_KEY;
import static com.yundianba.weixinpay.utils.ConfigUtil.APP_ID;
import static com.yundianba.weixinpay.utils.ConfigUtil.MCH_ID;

public class PayCommonUtil {
    /**
     * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     *
     */
    @SuppressWarnings({ "rawtypes"})
    public static boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if(!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);
        //算出摘要
        String mysign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toLowerCase();
        String tenpaySign = ((String)packageParams.get("sign")).toLowerCase();
        return tenpaySign.equals(mysign);
    }
    /**
     * sign签名
     *
     */
    @SuppressWarnings({ "rawtypes"})
    public static String createSign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    /**
     * 将请求参数转换为xml格式的string
     *
     */
    @SuppressWarnings({ "rawtypes"})
    public static String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 取出一个指定长度大小的随机正整数.
     *
     */
    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * 获取当前时间 yyyyMMddHHmmss
     *
     */
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }

    public static String weixin_pay(String price,String body,String out_trade_no )throws Exception{
       //账户信息
        String appid=APP_ID;
        String mch_id=MCH_ID;
        String key=API_KEY;

        String currTime=PayCommonUtil.getCurrTime();
        String strTime=currTime.substring(8,currTime.length());
        String strRandom=PayCommonUtil.buildRandom(4)+"";
        String nonce_str=strTime+strRandom;

        SortedMap<Object,Object> packageParms=new TreeMap<Object, Object>();
        packageParms.put("APP_ID",APP_ID);
        packageParms.put("MCH_ID",MCH_ID);
        packageParms.put("API_KEY",API_KEY);
        packageParms.put("currTime",currTime);
        packageParms.put("strTime",strTime);
        packageParms.put("nonce_str",nonce_str);
        packageParms.put("body",body);
        packageParms.put("total_fee",price);

        String sign=PayCommonUtil.createSign("utf-8",packageParms,key);
        packageParms.put("sign",sign);

        String requestXML=PayCommonUtil.getRequestXml(packageParms);
        System.out.println("requestXML="+requestXML);

        String resXML=HttpUtil.postData(ConfigUtil.UFDOOER_URL,requestXML);
        System.out.println(resXML);

        Map map=XMLUtil.doXMLParse(resXML);
        String urlCode=(String) map.get("code_url");

        return urlCode;




    }
}
