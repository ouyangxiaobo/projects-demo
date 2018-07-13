package com.yundianba.weixinpay.servlet;

import com.yundianba.weixinpay.utils.ConfigUtil;
import com.yundianba.weixinpay.utils.PayCommonUtil;
import com.yundianba.weixinpay.utils.XMLUtil;
import org.jdom.JDOMException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

public class ResultServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

     //微信支付通知
    public void weixin_notify(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
        String  writeContent="默认支付失败";
        String  path=request.getServletContext().getRealPath("file");//文件保存位置
        File file=new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        FileOutputStream fileOutputStream=new FileOutputStream(path+"/result.txt",true);

        //读取参数
        InputStream inputStream;
        StringBuffer sb=new StringBuffer();
        inputStream=request.getInputStream();
        String s;
        BufferedReader in=new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
        while((s=in.readLine())!=null){
           sb.append(s);
        }
        in.close();
        inputStream.close();

        //将XML解析为map
        Map<String,String> m=new HashMap<String,String>();
        try {
            m=XMLUtil.doXMLParse(sb.toString());
        } catch (JDOMException e) {
            e.printStackTrace();
        }

        //过滤空，设置 TreeMap
        SortedMap<Object,Object> packageParms=new TreeMap<Object, Object>();
        Iterator it=m.keySet().iterator();
        while(it.hasNext()){
          String parameter= (String) it.next();
          String parameterValue=m.get(parameter);

          String v="";
          if(null!=parameterValue){
              v=parameterValue.trim();
          }
          packageParms.put(parameter,v);
        }

        //账户信息
        String key=ConfigUtil.API_KEY;
        System.err.println(packageParms);
        String out_trade_no= (String) packageParms.get("out_trade_no");
        //判断签名是否正确
        if(PayCommonUtil.isTenpaySign("utf-8",packageParms,key)){
          //------------业务开始-----------//
            String resXml="";
            if("SUCCESS".equals ((String) packageParms.get("result_code"))){

                //支付成功
               String mch_id=(String)packageParms.get("mch_id");
               String opendid=(String)packageParms.get("opendid");
               String is_subscribe=(String)packageParms.get("is_subscribe");
               String total_fee=(String)packageParms.get("total_fee");

                System.err.println("mch_id"+mch_id);
                System.err.println("opendid"+opendid);
                System.err.println("is_subscribe"+is_subscribe);
                System.err.println("total_fee"+total_fee);

              //执行自己的业务
                System.out.println("支付成功......");
                writeContent="订单"+out_trade_no+"支付成功";
              //通知微信，异步确认成功，必须写，不然八次之后就提示交易失败
                resXml="<xml>"+"<return_code><![CDATA[SUCCESS]]></return_code>"
                +"<return_msg><![CDATA[OK]]></return_msg>"+"</xml>";
            }else{
                writeContent="订单"+out_trade_no+"支付失败，错误信息："+packageParms.get("error_msg");
                System.out.println("订单"+out_trade_no+"支付失败，错误信息："+packageParms.get("error_msg"));
                resXml=resXml="<xml>"+"<return_code><![CDATA[FAIL]]></return_code>"
                +"<return_msg><![CDATA[报文为空]]></return_msg>"+"</xml>" ;
            }

            //------------业务处理完毕----------------//
            BufferedOutputStream out=new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
        }else{
            writeContent="订单"+out_trade_no+"通知签名验证失败，支付失败";  //拼接支付结果信息，写入文件
            System.out.println("通知签名验证失败");
        }


    }

}
