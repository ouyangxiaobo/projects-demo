package com.yundianba.weixinpay.servlet;

import com.yundianba.weixinpay.utils.PayCommonUtil;
import com.yundianba.weixinpay.utils.ZxingUtil;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;



public class BuyServlet extends HttpServlet {

    private  Random random=new Random();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取用户购买的商品
        req.setCharacterEncoding("utf-8");
        String body=req.getParameter("body");
        String price="1";  //微信的价格单位是1分

        String order_id=random.nextInt(100000)+"";

        //生成二维码
        try {
            String result=PayCommonUtil.weixin_pay(price,body,order_id);
            BufferedImage image =ZxingUtil.createImage(result,300,300);
            System.out.println("image="+image);
            //跳转到支付页面，显示二维码
            req.getSession().setAttribute("image",image);
            req.getSession().setAttribute("oid",order_id);
            resp.sendRedirect("/payment.jsp");
        }catch (Exception e){
            e.printStackTrace();
        }

        //1.先得到二维码的原始字符串
        // 2.将字符串转换为二维码
        //跳转到支付页面，显示二维码


    }
}
